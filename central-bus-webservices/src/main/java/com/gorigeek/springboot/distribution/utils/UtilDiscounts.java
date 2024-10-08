package com.gorigeek.springboot.distribution.utils;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gorigeek.springboot.distribution.entity.StationsFromDistribution;
import com.gorigeek.springboot.distribution.service.DistributionRetailerService;
import com.gorigeek.springboot.entity.TBoletosDescuentos;
import com.gorigeek.springboot.repository.TBoletosDescuentosService;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

@Component
public class UtilDiscounts {
    @Autowired
    private DistributionRetailerService retailerService;
    @Autowired
    private TBoletosDescuentosService boletosRepository;

    // Metodo para obtener todas las terminales origen y destino. Sera necesaria
    // para aplicar descuento del 29 de Julio al 07 de Agosto del 2024
    public Boolean promotion(String arrivalStation, String departureTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        List<StationsFromDistribution> arrivalStations = null;

        // fecha/hora actual del sistema
        LocalDateTime currentDateSystem = LocalDateTime.now();

        // fecha limite de la promocion
        LocalDateTime dateStartPromotion = LocalDateTime.of(2024, Month.JULY, 29, 0, 0, 0);
        LocalDateTime dateEndPromotion = LocalDateTime.of(2024, Month.OCTOBER, 07, 23, 59, 59);
        LocalDateTime MaximumTravelDatePromotion = LocalDateTime.of(2024, Month.OCTOBER, 30, 23, 59, 59);
        // fecha ida y regreso
        LocalDateTime departureTimeObj = LocalDateTime.parse(departureTime, formatter);

        if (currentDateSystem.isAfter(dateStartPromotion) && currentDateSystem.isBefore(dateEndPromotion)
                && departureTimeObj.isBefore(MaximumTravelDatePromotion)) {

            ResponseEntity<List<StationsFromDistribution>> terminalesDestinoRequest = retailerService.getStations(null,
                    false);
            if (terminalesDestinoRequest.getStatusCode().value() == 200) {
                arrivalStations = terminalesDestinoRequest.getBody();
                if (arrivalStations != null) {
                    // Toca revisar si el destino es valido
                    for (StationsFromDistribution stationLlegadaIda : arrivalStations) {
                        if (stationLlegadaIda.getIdTerminal().trim().equals(arrivalStation.trim())) {
                            Boolean arrivalValid = UtilDiscounts
                                    .isDestinationValidForPromotion(stationLlegadaIda);
                            return arrivalValid;
                        }
                    }
                } else {
                    System.err.println("No se pudo obtener las terminales de destino. PROMOCION VERANO");
                    return false;
                }
            }
        } else {
            return false;
        }
        return false;

    }

    // Promocion - Destinos populares, 2 lugares a 99

    public int promotion99(String carrier, String departureStationId, String arrivalStationId,
            String departureTime) {

        List<StationsFromDistribution> departureStations = null;
        List<StationsFromDistribution> arrivalStations = null;

        //departureTime a LocalDate
        DateTimeFormatter formatt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;      
        LocalDateTime dateTimeDepartureTime = LocalDateTime.parse(departureTime, formatt);        
        // fecha/hora actual del sistema
        LocalDateTime currentDateSystem = LocalDateTime.now();
        // fecha limite de la promocion
        LocalDateTime dateStartPromotion = LocalDateTime.of(2024, Month.AUGUST, 23, 0, 0, 0);
        LocalDateTime dateEndPromotion = LocalDateTime.of(2024, Month.OCTOBER, 07, 23, 59, 59);

        if (currentDateSystem.isAfter(dateStartPromotion) && currentDateSystem.isBefore(dateEndPromotion) &&
                dateTimeDepartureTime.isAfter(dateStartPromotion) && dateTimeDepartureTime.isBefore(dateEndPromotion)) {

            ResponseEntity<List<StationsFromDistribution>> terminalesOrigenRequest = retailerService.getStations(null,
                    true);

            ResponseEntity<List<StationsFromDistribution>> terminalesDestinoRequest = retailerService.getStations(null,
                    false);

            if (terminalesOrigenRequest.getStatusCode().value() == 200
                    && terminalesDestinoRequest.getStatusCode().value() == 200) {
                departureStations = terminalesOrigenRequest.getBody();
                arrivalStations = terminalesDestinoRequest.getBody();

                if (departureStations != null && arrivalStations != null) {
                    // Toca revisar si la ruta es valida, primero obtenemos las estaciones
                    // respectivas
                    for (StationsFromDistribution stationOrigen : departureStations) {
                        if (stationOrigen.getIdTerminal().trim().equals(departureStationId.trim())) {
                            for (StationsFromDistribution stationLlegada : arrivalStations) {
                                if (stationLlegada.getIdTerminal().trim().equals(arrivalStationId.trim())) {
                                    // ya contamos con toda la info de la estacion de origen y llegada
                                    // (stationOrigen, stationLlegada), toca revisar si aun aplica el descuento
                                    /*
                                     * Paramentos a validar:
                                     * -Origen
                                     * -Destino
                                     * -Linea
                                     * -Hora
                                     * -Disponibilidad
                                     * 
                                     */
                                    int returnArrivalValid = isValidFor99Promotion(carrier,
                                            stationOrigen.getIdTerminal(),
                                            stationLlegada.getIdTerminal(),
                                            departureTime);
                                    return returnArrivalValid;

                                }

                            }
                        }

                    }
                } else {
                    System.err.println("No se puedo obtener las terminales de destino. PROMOCION 99");
                    return 0;
                }
            }
        } else

        {
            return 0;
        }
        return 0;

    }

    /*
     * Paramentos a validar:
     * -Origen
     * -Destino
     * -Linea
     * -Hora
     * -Disponibilidad (2 boletos, en x dia, a 99 )
     */
    public int isValidFor99Promotion(String carrier, String departureStationId,
            String arrivalStationId, String departureTime) {
        int quantitySeatsDiscountAvailable = 0;
        //departureTime a LocalDate
        DateTimeFormatter formatt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;      
        LocalDateTime dateTimeDepartureTime = LocalDateTime.parse(departureTime, formatt);        
        // fecha/hora actual del sistema
        LocalDateTime currentDateSystem = LocalDateTime.now();
        // fecha limite de la promocion
        LocalDateTime dateStartPromotion = LocalDateTime.of(2024, Month.AUGUST, 23, 0, 0, 0);
        LocalDateTime dateEndPromotion = LocalDateTime.of(2024, Month.OCTOBER, 07, 23, 59, 59);

        if (currentDateSystem.isAfter(dateStartPromotion) && currentDateSystem.isBefore(dateEndPromotion) &&
                dateTimeDepartureTime.isAfter(dateStartPromotion) && dateTimeDepartureTime.isBefore(dateEndPromotion)) {            
            Set<String> routesWithDiscount = new HashSet<>();
            // Zapopan Guadalajara Central de autobuses del norte de la ciudad de méxico ETN
            // miércoles 23:00
            routesWithDiscount.add("BILM-MXQCPZAP-MXMCICIU=miércoles=23:00");
            // CDMX (ciudad de méxico calz taxqueña) Acapulco de juarez central bus station
            // Diamante jueves 8:00
            routesWithDiscount.add("EDOR-MXMCIMCU-MXADJADJ=jueves=08:00");
            // Central de autobuses del norte de la ciudad de méxico Puebla central bus
            // station ADO martes 10:55
            routesWithDiscount.add("ZOEA-MXMCICIU-MXPBCPZO=martes=10:55");
            // Chiapas (tuxtla gutitrrez bus station) Oaxaca bus station OCC viernes 23:00
            routesWithDiscount.add("AOCC-MXTGZTBS-MXOAXOBS=viernes=23:00");
            // Guadalajara central bus station Central de autobuses de Puerto Vallarta ETN
            // viernes 7:10
            routesWithDiscount.add("BILM-MXGDLGBA-MXPVRPUE=viernes=07:10");
            // Mérida unica bus station Campeche Ado bus station ADO Jueves 8:30
            routesWithDiscount.add("ZOEA-MXMIDMBU-MXCACCAB=jueves=08:30");
            // Mérida unica bus station Cancún ado bus station ADO viernes 7:07
            routesWithDiscount.add("ZOEA-MXMIDMBU-MXCUNCAB=viernes=07:07");
            // Xalapa centrak bus station Puebla central bus station ADO gel domingo 17:00
            routesWithDiscount.add("ADGL-MXJALXZO-MXPBCPAL=domingo=17:00");

            // prueba occ tuxtla - oaxaca 21:40
            routesWithDiscount.add("AOCC-MXTGZTBS-MXOAXOBS=viernes=21:40");
            routesWithDiscount.add("AOCC-MXOAXOBS-MXTGZTBS=viernes=21:00");
            routesWithDiscount.add("APLA-MXMIDMFA-MXCUNCAB=viernes=07:07");
            // Prueba CB
            // id afiliado - id terminal origen - id terminal destino - dia - hora
            routesWithDiscount.add("148-38-79=martes=13:00");
            routesWithDiscount.add("148-79-38=miércoles=10:00");

            String route = carrier.concat("-".concat(
                    departureStationId.trim().concat("-").concat(arrivalStationId.trim())));

            // Obtener dia y hora del departureTime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(departureTime, formatter);
            DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
            String dayInSpanish = dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, new Locale("es", "ES"));
            String timeIn24HourFormat = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));

            for (String discount : routesWithDiscount) {
                String[] discountParts = discount.split("=");
                if (discountParts[0].equals(route)) {
                    if (discountParts[1].equals(dayInSpanish) && discountParts[2].equals(timeIn24HourFormat)) {
//                    System.err.println("Day in Spanish: " + dayInSpanish);
//                    System.err.println("Time (24-hour format): " + timeIn24HourFormat);
//                    System.err.println("Departure time: " + departureTime);
//                    System.err.println("ruta candidata a descuento");
                        /*
                         * Comprobar si aun hay lugares disponibles para el mismo dia
                         * 
                         */
                        String[] stationsAndCarrier = discountParts[0].split("-"); // [linea, origen, destino]
                        // buscar por linea, origen, destino y departureTime
                        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
                        Date tenMinutesAgoDate = Date.from(tenMinutesAgo.atZone(ZoneId.systemDefault()).toInstant());
                        List<TBoletosDescuentos> registered = boletosRepository.findByLineaOrigenDestinoAndFechaSalida(
                                stationsAndCarrier[0], stationsAndCarrier[1], stationsAndCarrier[2], departureTime);
                        int quantitySeatsDiscountUnavailable = 0;
                        for (TBoletosDescuentos boleto : registered) {
                            if (boleto.getEstatus() == 2) {
                                quantitySeatsDiscountUnavailable += 1;
                                // Excluir los que van a ser borrados por deleteOldRecords()
                            } else if (boleto.getFechaCreacion().after(tenMinutesAgoDate) && boleto.getEstatus() == 1) {
                                quantitySeatsDiscountUnavailable += 1;
                            }
                        }
                        quantitySeatsDiscountAvailable = 2 - quantitySeatsDiscountUnavailable;
                        if (quantitySeatsDiscountAvailable < 0) {
                            quantitySeatsDiscountAvailable = 0;
                        }

//                    System.err.println(
//                            "Cantidad de asientos con descuento disponibles = " + quantitySeatsDiscountAvailable);
                        return quantitySeatsDiscountAvailable;
                    }
                }
            }
            return quantitySeatsDiscountAvailable;
        } else {
            return quantitySeatsDiscountAvailable;
        }
    }

    @Scheduled(fixedRate = 600000) // 10 min en millisegundos
    public void deleteOldRecords() {
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
        Date tenMinutesAgoDate = Date.from(tenMinutesAgo.atZone(ZoneId.systemDefault()).toInstant());

        // Encuentra reservaciones que tiene mas de 10 min que fueron creados y aun no
        // han sido confirmados
        List<TBoletosDescuentos> oldRecords = boletosRepository.findOldRecords(tenMinutesAgoDate);

        for (TBoletosDescuentos record : oldRecords) {
            boletosRepository.delete(record);
        }
    }

    public static Boolean isDestinationValidForPromotion(StationsFromDistribution station) {
        Set<String> destinationsCities = new HashSet<>();
        Set<String> destinationsStates = new HashSet<>();
        // pruebas
//        destinationsCities.add("Morelia");
//        destinationsCities.add("Los Barriles");
//        destinationsCities.add("La Paz");
        // end pruebas
        destinationsCities.add("Mazatlán");
        destinationsCities.add("Acapulco de Juárez");
        destinationsCities.add("Puerto Vallarta"); // de JALISCO
        destinationsStates.add("CIUDAD DE MÉXICO"); // estado
        destinationsStates.add("MÉXICO"); // estado
        destinationsCities.add("Cancun");
        destinationsStates.add("VERACRUZ DE IGNACIO DE LA LLAVE"); // estado
        destinationsStates.add("CHIAPAS");
        destinationsCities.add("Manzanillo");
        destinationsCities.add("Monterrey");
        destinationsCities.add("Zihuatanejo");
        destinationsCities.add("Guadalajara");
        String city = station.getCiudad().getDescripcion().trim();
        String state = station.getCiudad().getIdEstado().trim();
        if (destinationsCities.contains(city)) {
            if (city.equalsIgnoreCase("Puerto Vallarta")) {
                return state.equalsIgnoreCase("JALISCO") ? true : false;
            } else {
                return true;
            }
        }
        if (destinationsStates.contains(state)) {
            return true;
        }

        return false;
    }
}
