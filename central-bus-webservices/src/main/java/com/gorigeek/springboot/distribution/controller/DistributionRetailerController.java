package com.gorigeek.springboot.distribution.controller;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.gorigeek.springboot.distribution.entity.SeatsReponse;
import com.gorigeek.springboot.distribution.entity.StationsFromDistribution;
import com.gorigeek.springboot.Module;
import com.gorigeek.springboot.distribution.entity.FindResponse;
import com.gorigeek.springboot.distribution.entity.ReservationDetailRequest;
import com.gorigeek.springboot.distribution.entity.ReservationsCancelRequest;
import com.gorigeek.springboot.distribution.entity.VacancyTripRequest;
import com.gorigeek.springboot.distribution.entity.VancancyTripResponse;
import com.gorigeek.springboot.distribution.entity.ReservationsConfirmRequest;
import com.gorigeek.springboot.distribution.entity.ReservationsCreateRequest;
import com.gorigeek.springboot.distribution.entity.ReservationsCreateResponse;
import com.gorigeek.springboot.distribution.service.DistributionRetailerService;
import com.gorigeek.springboot.distribution.service.VentasDistributionService;

@RestController
@RequestMapping("/api/distribution")
public class DistributionRetailerController {

    @Autowired
    private DistributionRetailerService retailerService;

    @Autowired
    private VentasDistributionService ventasService;

    @Module("Distribution - Comprar Boleto/Obtener Destinos")
    @GetMapping("/terminales")
    public ResponseEntity<List<StationsFromDistribution>> getStations(
            @RequestParam(name = "idStation", required = false, defaultValue = "null") String idStation,
            @RequestParam(name = "isOrigen", required = false, defaultValue = "true") Boolean isOrigen) {
        ResponseEntity<List<StationsFromDistribution>> stations = this.retailerService.getStations(idStation, isOrigen);
        if (stations.getStatusCodeValue() == 200) {
            return ResponseEntity.status(HttpStatus.OK).body(stations.getBody());

        } else if (stations.getStatusCodeValue() == 204) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(stations.getBody());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/find")
    public ResponseEntity<List<FindResponse>> find(
            @RequestParam(name = "departureStation", required = true) String departureStation,
            @RequestParam(name = "arrivalStation", required = true) String arrivalStation,
            @RequestParam(name = "departureDate", required = true) String departureDate,
            @RequestParam(name = "returnDate", required = false, defaultValue = "null") String returnDate,
            @RequestParam(name = "pax", required = false, defaultValue = "1") Integer pax) {
        try {
            ResponseEntity<List<FindResponse>> find = this.retailerService.find(departureStation, arrivalStation,
                    departureDate,
                    returnDate, pax);
            if (find != null && find.getStatusCode().value() == 200) {
                // apesar
                return ResponseEntity.status(HttpStatus.OK).body(find.getBody());
            } else if (find != null && find.getStatusCode().value() == 429) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(find.getBody());
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Module("Distribution - Comprar Boleto/Obtener Rutas")
    @GetMapping("/carriers")
    public ResponseEntity<List<List<FindResponse>>> getCarriers(
            @RequestParam(name = "departureStations", required = true) String[] departureStations,
            @RequestParam(name = "arrivalStations", required = true) String[] arrivalStations,
            @RequestParam(name = "departureDate", required = true) String departureDate,
            @RequestParam(name = "returnDate", required = false, defaultValue = "null") String returnDate,
            @RequestParam(name = "pax", required = false, defaultValue = "1") Integer pax) {
        try {
            ArrayList<String> connectedStations = null;
            // Lista para almacenar las respuestas combinadas
            List<List<FindResponse>> combinedResponses = new ArrayList<>();

            // Comprobar la longitud de los array para determinar si es necesario hacer la
            // consulta de connected stations
            if (departureStations.length > 1 || arrivalStations.length > 1) {
                connectedStations = this.retailerService.getConnectedStations();
            }
            // Bucle para buscar respuestas para todas las combinaciones de estaciones y
            // fechas
            for (String departureStation : departureStations) {
                for (String arrivalStation : arrivalStations) {

                    ResponseEntity<List<FindResponse>> find = null;
                    if (connectedStations != null) {
                        if (connectedStations.contains(departureStation.concat("-").concat(arrivalStation))) {
                            find = this.retailerService.find(departureStation, arrivalStation, departureDate,
                                    returnDate, pax);
                        }
                    } else {
                        find = this.retailerService.find(departureStation, arrivalStation, departureDate,
                                returnDate, pax);
                    }
                    if (find != null && find.getStatusCode().value() == 200) {
                        combinedResponses.add(find.getBody());
                    } else if (find != null && find.getStatusCode().value() == 429) {
                        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
                    }
                }
            }

            if (!combinedResponses.isEmpty()) {
                // Mapa para agrupar respuestas por idMarketingCarrier
                Map<String, List<FindResponse>> marketingCarrierMap = new HashMap<>();

                // Agrupar las respuestas por idMarketingCarrier
                for (List<FindResponse> temp : combinedResponses) {
                    for (FindResponse find : temp) {
                        String marketingCarrier = find.getIdMarketingCarrier();
                        List<FindResponse> responsesForCarrier = marketingCarrierMap.get(marketingCarrier);
                        if (responsesForCarrier == null) {
                            responsesForCarrier = new ArrayList<>();
                            responsesForCarrier.add(find);
                            marketingCarrierMap.put(marketingCarrier, responsesForCarrier);
                        } else {
                            responsesForCarrier.add(find);
                            marketingCarrierMap.put(marketingCarrier, responsesForCarrier);
                        }
                    }
                }

                // Lista para almacenar las respuestas combinadas por idMarketingCarrier
                List<List<FindResponse>> combinedMarketingResponses = new ArrayList<>();

                for (List<FindResponse> responsesForCarrier : marketingCarrierMap.values()) {
                    combinedMarketingResponses.add(responsesForCarrier);
                }

                // Comprobar que realmente están las fechas de regreso e ida si es redondo
                if (!returnDate.equals("null")) {
                    DateTimeFormatter formatterWithoutHours = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate departureDateObj = LocalDate.parse(departureDate, formatterWithoutHours);
                    LocalDate returnDateObj = LocalDate.parse(returnDate, formatterWithoutHours);

                    for (int i = 0; i < combinedMarketingResponses.size(); i++) {
                        List<FindResponse> travelList = combinedMarketingResponses.get(i);
                        boolean isDepartureTripsPresent = false;
                        boolean isReturnTripsPresent = false;
                        for (int j = 0; j < travelList.size(); j++) {
                            FindResponse travel = travelList.get(j);
                            String onlyDate = travel.getDepartureTime().split("T")[0];
                            LocalDate travelDepartureDateObj = LocalDate.parse(onlyDate,
                                    formatterWithoutHours);
                            if (travelDepartureDateObj.isEqual(departureDateObj) && isDepartureTripsPresent == false) {
                                isDepartureTripsPresent = true;
                            } else if (travelDepartureDateObj.isEqual(returnDateObj)) {
                                isReturnTripsPresent = true;
                            }
                        }
                        if (!isDepartureTripsPresent || !isReturnTripsPresent) {
                            combinedMarketingResponses.remove(i);
                        }
                    }

                }
                // Devolver las respuestas combinadas
                return ResponseEntity.status(HttpStatus.OK).body(combinedMarketingResponses);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/multifind")
    public ResponseEntity<List<FindResponse>> multiFind(
            @RequestParam(name = "departureStations", required = true) String[] departureStations,
            @RequestParam(name = "arrivalStations", required = true) String[] arrivalStations,
            @RequestParam(name = "departureDate", required = true) String departureDate,
            @RequestParam(name = "returnDate", required = false, defaultValue = "null") String returnDate,
            @RequestParam(name = "pax", required = false, defaultValue = "1") Integer pax) {
        try {
            ArrayList<String> connectedStations = null;
            List<List<FindResponse>> combinedResponses = new ArrayList<>();
            for (String departureStation : departureStations) {
                for (String arrivalStation : arrivalStations) {

                    ResponseEntity<List<FindResponse>> find = null;
                    if (connectedStations != null) {
                        if (connectedStations.contains(departureStation.concat("-").concat(arrivalStation))) {
                            find = this.retailerService.find(departureStation, arrivalStation, departureDate,
                                    returnDate, pax);
                        }
                    } else {
                        find = this.retailerService.find(departureStation, arrivalStation, departureDate,
                                returnDate, pax);
                    }
                    if (find != null && find.getStatusCode().value() == 200) {
                        combinedResponses.add(find.getBody());
                    } else if (find != null && find.getStatusCode().value() == 429) {
                        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
                    }
                }
            }

            if (!combinedResponses.isEmpty()) {
                Map<String, List<FindResponse>> marketingCarrierMap = new HashMap<>();

                // Agrupar las respuestas por idMarketingCarrier
                for (List<FindResponse> temp : combinedResponses) {
                    for (FindResponse find : temp) {
                        String marketingCarrier = find.getIdMarketingCarrier();
                        List<FindResponse> responsesForCarrier = marketingCarrierMap.get(marketingCarrier);
                        if (responsesForCarrier == null) {
                            responsesForCarrier = new ArrayList<>();
                            responsesForCarrier.add(find);
                            marketingCarrierMap.put(marketingCarrier, responsesForCarrier);
                        } else {

                            responsesForCarrier.add(find);
                            marketingCarrierMap.put(marketingCarrier, responsesForCarrier);
                        }
                    }
                }

                List<FindResponse> combinedMarketingResponses = new ArrayList<>();
                for (List<FindResponse> responsesForCarrier : marketingCarrierMap.values()) {
                    combinedMarketingResponses.addAll(responsesForCarrier);
                }
                return ResponseEntity.status(HttpStatus.OK).body(combinedMarketingResponses);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Module("Distribution - Comprar Boleto/Vacantes")
    @PostMapping("/vacancy")
    public ResponseEntity<VancancyTripResponse> vacancyTrip(@RequestBody VacancyTripRequest body) {
        VancancyTripResponse vacancy = retailerService.vacancy(body);
        if (vacancy == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(vacancy);
        }
        return ResponseEntity.status(HttpStatus.OK).body(vacancy);
    }

    @Module("Distribution - Comprar Boleto/Obtener Asientos")
    @GetMapping("/seats")
    public ResponseEntity<?> seats(
            @RequestParam(name = "marketing_carrier", required = true) String marketingCarrier,
            @RequestParam(name = "departure_station", required = true) String departureStation,
            @RequestParam(name = "arrival_station", required = true) String arrivalStation,
            @RequestParam(name = "departure_time", required = true) String departureTime,
            @RequestParam(name = "arrival_time", required = true) String arrivalTime) {
        SeatsReponse seats = retailerService.seats(marketingCarrier, departureStation, arrivalStation, departureTime,
                arrivalTime);
        if (seats == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(seats);
        }
        return ResponseEntity.status(HttpStatus.OK).body(seats);
    }

    @Module("Distribution - Comprar Boleto/Crear Reservacion")
    @PostMapping("/reservations/create")
    public ResponseEntity<?> createReservations(@RequestBody ReservationsCreateRequest body) {
        ReservationsCreateResponse create = retailerService.createReservations(body);
        if (create == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(create);
        }
        return ResponseEntity.status(HttpStatus.OK).body(create);
    }

    @Module("Distribution - Comprar Boleto/Confirmar Reservacion")
    @PutMapping("/reservations/confirm")
    public ResponseEntity<?> confirmReservations(@RequestBody ReservationsConfirmRequest body) {
        Boolean confirm = retailerService.confirmReservations(body);
        if (confirm == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(confirm);
        }
        return ResponseEntity.status(HttpStatus.OK).body(confirm);
    }

    @Module("Distribution - Comprar Boleto/Generar Boleto")
    @PostMapping("/reservations/detail")
    public ResponseEntity<JSONObject> reservationsDetail(@RequestBody ReservationDetailRequest body) {
        JSONObject reservationsDetail = this.ventasService.reservationsDetail(body);
        if (reservationsDetail == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(reservationsDetail);
        } else if (reservationsDetail.get("error") != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(reservationsDetail);
        }
        return ResponseEntity.status(HttpStatus.OK).body(reservationsDetail);
    }

    @Module("Distribution - Comprar Boleto/Cancelar Reservacion")
    @PutMapping("/reservations/cancel")
    public ResponseEntity<?> cancelReservations(@RequestBody ReservationsCancelRequest body) {
        Boolean cancel = retailerService.cancelReservations(body);
        if (cancel == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cancel);
        }
        return ResponseEntity.status(HttpStatus.OK).body(cancel);
    }
}
