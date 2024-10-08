package com.gorigeek.springboot.distribution.service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.Set;
import java.util.HashSet;

import javax.annotation.PostConstruct;

import java.util.HashMap;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.gorigeek.springboot.distribution.entity.ReservationsConfirmRequest;
import com.gorigeek.springboot.distribution.entity.ReservationsCreateRequest;
import com.gorigeek.springboot.distribution.entity.ReservationsCreateResponse;
import com.gorigeek.springboot.distribution.entity.Seats;
import com.gorigeek.springboot.distribution.entity.FindRequest;
import com.gorigeek.springboot.distribution.entity.Response;
import com.gorigeek.springboot.distribution.entity.SeatsReponse;
import com.gorigeek.springboot.distribution.entity.StationsFromDistribution;
import com.gorigeek.springboot.distribution.entity.FindResponse;
import com.gorigeek.springboot.distribution.entity.ReservationsCancelRequest;
import com.gorigeek.springboot.distribution.entity.VacancyTripRequest;
import com.gorigeek.springboot.distribution.entity.VancancyTripResponse;
import com.gorigeek.springboot.distribution.utils.UtilDiscounts;
import com.gorigeek.springboot.distribution.utils.RequestToDistributionUtils;

@Service
public class DistributionRetailerService {
    private static final Logger logger = LogManager.getLogger(DistributionRetailerService.class);

    @Autowired
    private RequestToDistributionUtils requestUtils;

    @Autowired
    private UtilDiscounts utilDiscounts;

    private Cache<String, Response<JsonNode>> carriersCache;
    private Cache<String, ArrayList<String>> connectedStationsCache;

    @PostConstruct
    public void init() {
        carriersCache = Caffeine.newBuilder()
                .maximumSize(100) // Establece el tamaño maximo (en entradas)
                .expireAfterWrite(90, TimeUnit.DAYS)
                .build();
        connectedStationsCache = Caffeine.newBuilder()
                .maximumSize(100) // Establece el tamaño maximo (en entradas)
                .expireAfterWrite(8, TimeUnit.DAYS)
                .build();
    }

    // Método para establecer el valor de marketingCarrierDetail en caché
    public void setMarketingCarrierDetail(String key, Response<JsonNode> response) {
        carriersCache.put(key, response);
    }

    // Método para obtener el valor de marketingCarrierDetail del caché
    public Response<JsonNode> getMarketingCarrierDetail(String key) {
        return carriersCache.getIfPresent(key);
    }

    // Método para establecer el valor de connected stations en caché
    public void setConnectedStationsCache(String key, ArrayList<String> response) {
        connectedStationsCache.put(key, response);
    }

    // Método para obtener el valor de connected stations en caché
    public ArrayList<String> getConnectedStationsCache(String key) {
        return connectedStationsCache.getIfPresent(key);
    }

    public ResponseEntity<List<StationsFromDistribution>> getStations(String idStation, Boolean isOrigen) {
        try {
            final String endpoint = "/stations?idStation=" + idStation + "&isOrigen=" + isOrigen;
            Response<JsonNode> stationsFromDistribution = requestUtils.request(endpoint, "GET", null);
            if (stationsFromDistribution.getCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<StationsFromDistribution> list = objectMapper.convertValue(stationsFromDistribution.getBody(),
                        new TypeReference<List<StationsFromDistribution>>() {
                        });
                return ResponseEntity.ok(list);
            } else if (stationsFromDistribution.getCode() == 0 || stationsFromDistribution.getCode() == 500) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            logger.error("¡Error en el método: getStations! " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<FindResponse>> find(String departureStation, String arrivalStation, String departureDate,
            String returnDate, Integer pax) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        try {
            final String findEndpoint = "/find?departureStation=" + departureStation + "&arrivalStation="
                    + arrivalStation + "&departureDate=" + departureDate + "&returnDate=" + returnDate + "&pax=" + pax;

            Response<JsonNode> marketingCarrierDetail = null;
            HashMap<String, String> fareFeatures = new HashMap<>();
            HashMap<String, List<String>> faresIdsPerClass = new HashMap<>();
            List<FindResponse> listTravels = new ArrayList<FindResponse>();
            FindRequest body = new FindRequest();
            body.setDepartureStation(departureStation);
            body.setArrivalStation(arrivalStation);
            body.setDepartureDate(departureDate);
            Response<JsonNode> findRequest = requestUtils.request(findEndpoint, "GET", null);
            if (findRequest.getCode() == 200) {
                Iterator<JsonNode> elements0 = findRequest.getBody().get(0).elements();
                Iterator<JsonNode> included = findRequest.getBody().get(1).elements();
                while (included.hasNext()) {
                    JsonNode includedAttribute = included.next();
                    if (includedAttribute.get("type").asText().equals("fare_features")) {
                        JsonNode fare = includedAttribute;
                        fareFeatures.put(fare.get("id").asText(), fare.get("attributes").get("description").asText());
                    }

                    if (includedAttribute.get("type").asText().equals("fare_classes")) {
                        JsonNode fare = includedAttribute;
                        Iterator<JsonNode> fareIds = fare.get("relationships").get("fare_features").get("data")
                                .elements();
                        List<String> fareFeaturesIds = new ArrayList<>();
                        while (fareIds.hasNext()) {
                            JsonNode fareId = fareIds.next();
                            if (fareId.get("type").asText().equals("fare_features")) {
                                fareFeaturesIds.add(fareId.get("id").asText());
                            }
                        }
                        faresIdsPerClass.put(fare.get("id").asText(), fareFeaturesIds);
                    }
                }
                // Diferentes harario, misma fecha
                while (elements0.hasNext()) {
                    JsonNode bodyNode0 = elements0.next();
                    JsonNode travelAttributes = bodyNode0.path("attributes");
                    Iterator<JsonNode> travelFares = bodyNode0.path("relationships").path("fares").get("data")
                            .elements();
                    // Diferentes clases, misma horaio
                    while (travelFares.hasNext()) {
                        FindResponse tempTravel = new FindResponse();
                        JsonNode travelFare = travelFares.next();
                        JsonNode travelRelations = bodyNode0.path("relationships");
                        tempTravel.setDepartureTime(travelAttributes.get("departure_time").asText());
                        tempTravel.setArrivalTime(travelAttributes.get("arrival_time").asText());
                        tempTravel.setDuration(travelAttributes.get("duration").asLong());
                        tempTravel.setBookedOut(travelAttributes.get("booked_out").asBoolean());

                        tempTravel.setFareClassCode(travelFare.get("fare_class").get("code").asText());
                        int price = travelFare.get("price").asInt() / pax;
                        tempTravel.setPrice(convertirCentavosAPesos(price));
                        List<String> tempTravelFeatures = new ArrayList<>();

                        // obtener fare_features
                        String idFare = travelFare.get("fare_class").get("id").asText();

                        List<String> faresId = faresIdsPerClass.get(idFare);
                        if (faresId != null) {
                            for (String id : faresId) {
                                String fare = fareFeatures.get(id);
                                if (fare != null) {
                                    tempTravelFeatures.add(fare);
                                } else {
                                    logger.log(Level.INFO,
                                            "La característica de tarifa para el ID " + id + " es nula.");
                                }
                            }
                        } else {
                            logger.log(Level.INFO, "No se encontraron tarifas para el ID de tarifa " + idFare);
                        }

                        tempTravel.setFareFeatures(tempTravelFeatures);
                        tempTravel.setOfferBundle(travelAttributes.get("offer_bundle").isNull() ? null
                                : travelAttributes.get("offer_bundle").asText());
                        tempTravel.setOfferId(
                                travelAttributes.get("offer_id").isNull() ? null
                                        : travelAttributes.get("offer_id").asText());
                        tempTravel.setTotalSeatsLeft(travelAttributes.get("total_seats_left").isNull() ? null
                                : travelAttributes.get("total_seats_left").asInt());
                        tempTravel.setArrivalStation(
                                travelRelations.get("arrival_station").get("data").get("id").asText());
                        tempTravel.setDepartureStation(
                                travelRelations.get("departure_station").get("data").get("id").asText());
                        tempTravel.setIdMarketingCarrier(
                                travelRelations.get("marketing_carrier").get("data").get("id").asText());
                        // para obtener imagen del carrier
                        if (getMarketingCarrierDetail(tempTravel.getIdMarketingCarrier()) == null) {
                            // Ejecutar la solicitud solo si marketingCarrierDetail aún no se ha
                            // inicializado
                            String detailEndpoint = "/marketing-carrier-detail?idMarketingCarrier="
                                    + tempTravel.getIdMarketingCarrier();
                            marketingCarrierDetail = requestUtils.request(detailEndpoint, "GET", null);
                            setMarketingCarrierDetail(tempTravel.getIdMarketingCarrier(), marketingCarrierDetail);
                        } else {
                            marketingCarrierDetail = getMarketingCarrierDetail(tempTravel.getIdMarketingCarrier());
                        }
                        if (marketingCarrierDetail.getCode() == 200) {
                            tempTravel.setLogo(
                                    marketingCarrierDetail.getBody().get("attributes").get("white_label_logo")
                                            .asText());
                            tempTravel.setTradeNameMarketingCarrier(
                                    marketingCarrierDetail.getBody().get("attributes").get("trade_name")
                                            .asText());

                        } else {
                            setMarketingCarrierDetail(tempTravel.getIdMarketingCarrier(), null);
                        }

                        // add tipo de pasagero
                        Set<String> passengerTypes = new HashSet<String>();

                        Iterator<JsonNode> elements1 = findRequest.getBody().get(1).elements();
                        while (elements1.hasNext()) {
                            JsonNode bodyNode1 = elements1.next();
                            if (Objects.equals(bodyNode1.path("type").asText(), "passenger_types")) {
                                passengerTypes.add(bodyNode1.path("attributes").get("code").asText());
                            }
                        }
                        if (!passengerTypes.isEmpty()) {
                            StringBuilder resultBuilder = new StringBuilder();
                            for (String element : passengerTypes) {
                                resultBuilder.append(element).append(",");
                            }
                            // Remover coma
                            if (resultBuilder.length() > 0) {
                                resultBuilder.deleteCharAt(resultBuilder.length() - 1);
                            }
                            String result = resultBuilder.toString();
                            tempTravel.setPassengerTypes(result);
                        }
                        Iterator<JsonNode> includedNode = findRequest.getBody().get(1).elements();
                        while (includedNode.hasNext()) {
                            JsonNode includedAttribute = includedNode.next();

                            if (includedAttribute.get("type").asText().equals("stations")) {
                                JsonNode station = includedAttribute.get("attributes");
                                if (station.get("code").asText().equals(departureStation)) {
                                    String nombreTerminal = station.get("name").asText().trim();

                                    // Verificar si el nombre contiene "Mexico City" y reemplazarlo
                                    if (nombreTerminal.contains("Mexico City")) {
                                        nombreTerminal = nombreTerminal.replace("Mexico City", "Ciudad de México ");
                                    }
                                    // Establecer el nombre actualizado
                                    tempTravel.setDepartureStationName(nombreTerminal);
                                    tempTravel.setDepartureStationDirection(station.get("street_and_number").asText());
                                }

                                if (station.get("code").asText().equals(arrivalStation)) {
                                    String nombreTerminal = station.get("name").asText().trim();

                                    // Verificar si el nombre contiene "Mexico City" y reemplazarlo
                                    if (nombreTerminal.contains("Mexico City")) {
                                        nombreTerminal = nombreTerminal.replace("Mexico City", "Ciudad de México ");
                                    }
                                    // Establecer el nombre actualizado
                                    tempTravel.setArrivalStationName(nombreTerminal);
                                    tempTravel.setArrivalStationDirection(station.get("street_and_number").asText());
                                }

                            }
                        }
                        if (checkIfDepartureDateIsValid(tempTravel.getDepartureTime())
                                && tempTravel.isBookedOut() == false) {
                            listTravels.add(tempTravel);
                        }
                    }
                }
                // Comprobar si tienen promocion
                for (FindResponse travel : listTravels) {
                    int isDiscountValid = utilDiscounts.promotion99(travel.getIdMarketingCarrier(),
                            travel.getDepartureStation(), travel.getArrivalStation(), travel.getDepartureTime());
                    
                    //Verano
//                    Boolean isDiscountValid = utilDiscounts.promotion(travel.getArrivalStation(), travel.getDepartureTime());
                    if (isDiscountValid > 0) {
                        travel.setIsDiscountActivated(true);
                        travel.setDiscountRate(0);
                        travel.setTypeDiscount("PROMO99");
                        travel.setAlternativePrice(99.00);
                        travel.setAvailableSeatsDiscount(isDiscountValid);
                    } else {
                        //probar con descuento con porcentaje
                        Boolean isDiscountValidPercent = utilDiscounts.promotion(travel.getArrivalStation(), travel.getDepartureTime());
                        if (isDiscountValidPercent) {
                            travel.setIsDiscountActivated(true);
                            travel.setDiscountRate(30);
                            travel.setAlternativePrice(null);
                        } else {
                            travel.setIsDiscountActivated(false);
                            travel.setDiscountRate(0);
                            travel.setAlternativePrice(null);
                        }
                    }                    
                }

                // Ordenar boletos
                Collections.sort(listTravels, (t1, t2) -> {
                    LocalDateTime time1 = LocalDateTime.parse(t1.getDepartureTime(), formatter);
                    LocalDateTime time2 = LocalDateTime.parse(t2.getDepartureTime(), formatter);
                    return time1.compareTo(time2);
                });
                return ResponseEntity.ok(listTravels);
            } else if (findRequest.getCode() == 429) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
            } else {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            logger.error("¡Error en el método: find!" + e.getMessage(), e);
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public VancancyTripResponse vacancy(@RequestBody VacancyTripRequest body) {
        try {
            final String endpointVacancy = "/vacancy";
            final String endpointMarketingCarrierDetail = "/marketing-carrier-detail?idMarketingCarrier="
                    + body.getMarketingCarrier();
            Response<JsonNode> marketingCarrierDetail = null;
            String defaultTypePassenger = "";
            Boolean isPnosDefaultPassenger = false;
            VancancyTripResponse vacancyModel = new VancancyTripResponse();
            // Check seat availability
            if (getMarketingCarrierDetail(body.getMarketingCarrier()) == null) {
                // Ejecutar la solicitud solo si marketingCarrierDetail aún no se ha
                // inicializado
                marketingCarrierDetail = requestUtils.request(endpointMarketingCarrierDetail, "GET",
                        null);
                setMarketingCarrierDetail(body.getMarketingCarrier(), marketingCarrierDetail);
            } else {
                marketingCarrierDetail = getMarketingCarrierDetail(body.getMarketingCarrier());
            }
            if (marketingCarrierDetail.getCode() == 200) {
                JsonNode marketingCarrierDetailBody = marketingCarrierDetail.getBody();

                vacancyModel.setSupportsSeats(
                        marketingCarrierDetailBody.path("attributes").get("supports_seats").asBoolean());
                defaultTypePassenger = marketingCarrierDetailBody.path("relationships").get("default_passenger_type")
                        .get("data").get("id").asText();
                if (defaultTypePassenger.equals("PNOS")) {
                    isPnosDefaultPassenger = true;
                }

                String[] passengerTypes = body.getPassengerType().split(",");
                // Comparador personalizado para ordenar el array
                Arrays.sort(passengerTypes, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        // Si alguno de los elementos es "PNOS", se coloca primero
                        if (o1.equals("PNOS")) {
                            return -1; // o1 debe estar antes que o2
                        } else if (o2.equals("PNOS")) {
                            return 1; // o2 debe estar antes que o1
                        }
                        // Para otros casos, se utiliza el orden natural de cadenas
                        return o1.compareTo(o2);
                    }
                });

                // Get prices consume /vacancy several types using different TYPES OF PASSENGERS
                for (String element : passengerTypes) {
                    if (element.equals("PCIL") || element.equals("PINT")) {
                        body.setPassengerType(element + ",PNOS");
                    } else {
                        body.setPassengerType(element);
                    }
                    if (isPnosDefaultPassenger && element.equals("PNOS")) {
                        if (body.getReturnDepartureTime() != null) {
                            vacancyModel.setPnos(body.getCheapestPrice() * 2);
                        } else {
                            vacancyModel.setPnos(body.getCheapestPrice());
                        }
                        if (!vacancyModel.isVacant()) {
                            vacancyModel.setVacant(true);
                        }
                        vacancyModel
                                .setNumberOfTypesOfPassenger((vacancyModel.getNumberOfTypesOfPassenger() == null)
                                        ? 1
                                        : vacancyModel.getNumberOfTypesOfPassenger() + 1);
                        continue;

                    }
                    Response<JsonNode> vacancy = requestUtils.request(endpointVacancy, "POST", body);
                    if (vacancy.getCode() == 200) {
                        JsonNode vacancyBody = vacancy.getBody();
                        Boolean vacant = vacancyBody.path("attributes").get("vacant").asBoolean();
                        if (!vacancyModel.isVacant()) {
                            vacancyModel.setVacant(vacant);
                        }

                        // IF /vacancy returns true save the data and price in and Model (create model)
                        if (vacant == true) {
                            int totalPrice = vacancyBody.path("attributes").get("total_price").asInt();
                            vacancyModel
                                    .setNumberOfTypesOfPassenger((vacancyModel.getNumberOfTypesOfPassenger() == null)
                                            ? 1
                                            : vacancyModel.getNumberOfTypesOfPassenger() + 1);
                            switch (element) {
                                case "PINT":// Infant
                                    if (vacancyModel.getPnos() != null) {
                                        // Distribution returns the price with the adult price included so I just minus
                                        // it
                                        vacancyModel
                                                .setPint(convertirCentavosAPesos(
                                                        totalPrice - (vacancyModel.getPnos().intValue() * 100)));
                                    } else {
                                        vacancyModel.setPint(convertirCentavosAPesos(totalPrice));
                                    }
                                    break;
                                case "PCIL":// Child
                                    if (vacancyModel.getPnos() != null) {
                                        // Distribution returns the price with the adult price included so I just minus
                                        // it
                                        vacancyModel
                                                .setPcil(convertirCentavosAPesos(
                                                        totalPrice - (vacancyModel.getPnos().intValue() * 100)));
                                    } else {
                                        vacancyModel.setPcil(convertirCentavosAPesos(totalPrice));
                                    }
                                    vacancyModel.setPcil(convertirCentavosAPesos(totalPrice) - vacancyModel.getPnos());
                                    break;
                                case "PYPO": // Youth
                                    vacancyModel.setPypo(convertirCentavosAPesos(totalPrice));
                                    break;
                                case "PNOS":// Adult
                                    vacancyModel.setPnos(convertirCentavosAPesos(totalPrice));
                                    break;
                                case "PSOE":// Senior
                                    vacancyModel.setPsoe(convertirCentavosAPesos(totalPrice));
                                    break;
                                default:
                                    vacancyModel
                                            .setNumberOfTypesOfPassenger(
                                                    vacancyModel.getNumberOfTypesOfPassenger() - 1);
                                    break;
                            }
                        }
                    }
                }
                // return and object with the type of passenger and their prices
                return vacancyModel;
            } else {
                setMarketingCarrierDetail(body.getMarketingCarrier(), null);
                return null;
            }
        } catch (Exception e) {
            logger.error("¡Error en el método: vacancy!" + e.getMessage(), e);
            return null;
        }

    }

    public SeatsReponse seats(String marketingCarrier, String departureStation, String arrivalStation,
            String departureTime, String arrivalTime) {
        try {
            SeatsReponse seatsResponse = new SeatsReponse();
            final String method = "GET";
            final String endpoint = "/seats?marketing_carrier=" + marketingCarrier
                    + "&departure_station=" + departureStation
                    + "&arrival_station=" + arrivalStation
                    + "&departure_time=" + departureTime
                    + "&arrival_time=" + arrivalTime;
            Response<JsonNode> requestResponse = requestUtils.request(endpoint, method, null);
            if (requestResponse.getCode() == 200) {
                Iterator<JsonNode> elements1 = requestResponse.getBody().get(1).elements();
                List<Seats> seats = new ArrayList<Seats>();
                int numberOfSeats = 0;
                int numberOfWc = 0;
                while (elements1.hasNext()) {
                    JsonNode bodyNode1 = elements1.next();
                    Seats seat = new Seats();
                    if (Objects.equals(bodyNode1.path("type").asText(), "seats")) {
                        if (Objects.equals(bodyNode1.path("attributes").get("code").asText(), "WC")) {
                            numberOfWc += 1;
                        } else {
                            numberOfSeats += 1;
                            String codeValue = bodyNode1.path("attributes").get("label").asText();
                            // Check if the code is numeric before parsing
                            if (isNumeric(codeValue)) {                                
                                seat.setCode(codeValue);
                            } else {
                                // Handle non-numeric code (e.g., "ES")
                                if (numberOfSeats > 0) {
                                    numberOfSeats -= 1;
                                }
                            }

                            seat.setFareClass(bodyNode1.path("attributes").get("fare_class").asText());
                            seat.setVacant(bodyNode1.path("attributes").get("vacant").asBoolean());
                            seat.setPrice(bodyNode1.path("price").asInt());
                            if (seat.getCode() != null) {
                                seats.add(seat);
                            }
                        }
                    }

                }
                seatsResponse.setNumberOfSeats(numberOfSeats);
                seatsResponse.setNumberOfWc(numberOfWc);
                seatsResponse.setSeats(seats);
                return seatsResponse;
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("¡Error en el método: seats!" + e.getMessage(), e);
            return null;
        }
    }

    // Helper method to check if a string is numeric
    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    public ReservationsCreateResponse createReservations(ReservationsCreateRequest body) {
        try {
            final String endpoint = "/reservations/create";
            ReservationsCreateResponse res = new ReservationsCreateResponse();
            Response<JsonNode> requestResponse = requestUtils.request(endpoint, "POST", body);
            if (requestResponse.getCode() == 201) {
                JsonNode bodyNode = requestResponse.getBody();
                res.setId(bodyNode.get("id").asText());
                res.setType(bodyNode.get("type").asText());
                res.setState(bodyNode.path("attributes").get("state").asText());
                res.setTotalPrice(convertirCentavosAPesos(bodyNode.path("attributes").get("total_price").asInt()));
                res.setCreatedAt(bodyNode.path("attributes").get("created_at").asText());
                return res;
            } else {
                return null;
            }

        } catch (Exception e) {
            logger.error("¡Error in method: createReservations!" + e.getMessage(), e);
            return null;
        }

    }

    public ArrayList<String> getConnectedStations() {
        try {
            final String endpoint = "/connected-stations";
            if (getConnectedStationsCache("connected-stations") == null) {
                Response<JsonNode> requestResponse = requestUtils.request(endpoint, "GET", null);
                if (requestResponse.getCode() == 200) {
                    ArrayList<String> stringList = convertJsonNodeToStringList(requestResponse.getBody());
                    setConnectedStationsCache("connected-stations", stringList);
                    return stringList;
                } else {
                    return new ArrayList<>();
                }
            } else {
                return getConnectedStationsCache("connected-stations");
            }

        } catch (Exception e) {
            logger.error("Error en el método: getConnectedStations! " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private ArrayList<String> convertJsonNodeToStringList(JsonNode jsonNode) {
        ArrayList<String> stringList = new ArrayList<>();
        if (jsonNode.isArray()) {
            for (JsonNode node : jsonNode) {
                stringList.add(node.asText());
            }
        }
        return stringList;
    }

    public Boolean confirmReservations(ReservationsConfirmRequest body) {
        try {
            final String endpoint = "/reservations/confirm";
            Response<JsonNode> requestResponse = requestUtils.request(endpoint, "PUT", body);
            if (requestResponse.getCode() == 200) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("¡Error en el método: confirmReservations!" + e.getMessage(), e);
            return false;
        }
    }

    public Boolean cancelReservations(ReservationsCancelRequest body) {
        try {
            final String endpoint = "/reservations/cancel";
            Response<JsonNode> requestResponse = requestUtils.request(endpoint, "PUT", body);
            if (requestResponse.getCode() == 200) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("¡Error en el método: cancelReservations!" + e.getMessage(), e);
            return false;
        }

    }

    public static double convertirCentavosAPesos(int precioEnCentavos) {
        // Convertir centavos a pesos mexicanos
        double precioEnPesos = precioEnCentavos / 100.0;
        return precioEnPesos;
    }

    public Boolean checkIfDepartureDateIsValid(String strDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(strDate, formatter);
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (dateTime.isBefore(currentDateTime)) {
            return false;
        } else {
            return true;
        }
    }
}
