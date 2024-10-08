package com.gorigeek.springboot.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gorigeek.springboot.Module;
import com.gorigeek.springboot.entity.AsientoNormalTemp;
import com.gorigeek.springboot.entity.Autobus;
import com.gorigeek.springboot.entity.DetalleVentaMovil;
import com.gorigeek.springboot.entity.RutaHasTransporte;
import com.gorigeek.springboot.entity.RutasDetalles;
import com.gorigeek.springboot.entity.Terminales;
import com.gorigeek.springboot.entity.TipoBoleto;
import com.gorigeek.springboot.entity.DTO.AsientosOcupadosDTO;
import com.gorigeek.springboot.entity.DTO.RutaHasTransporteDTO;
import com.gorigeek.springboot.exception.ResourceNotFoundException;
import com.gorigeek.springboot.repository.AsientoNormalTempRepository;
import com.gorigeek.springboot.repository.AutobusRepository;
import com.gorigeek.springboot.repository.DetalleVentaRepository;
import com.gorigeek.springboot.repository.RutaDetalleRepository;
import com.gorigeek.springboot.repository.RutaHasTransporteRepository;
import com.gorigeek.springboot.repository.TerminalRepository;
import com.gorigeek.springboot.repository.TipoBoletoRepository;

@RestController
@RequestMapping("/api/asientos")
public class AsientosController {

    @Autowired
    private RutaHasTransporteRepository rutaTransporteRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private RutaDetalleRepository rutadetalleRepository;

    @Autowired
    private AsientoNormalTempRepository asientoTempNormalRepository;

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private TipoBoletoRepository tipoBoletoRepository;

    @Autowired
    private AutobusRepository autobusRepository;

    @GetMapping
    public List<RutaHasTransporteDTO> getAll() {
        List<RutaHasTransporte> listaRutasHasTransportes = rutaTransporteRepository.findAll();

        List<RutaHasTransporteDTO> listaDTO = new ArrayList<>();
        System.out.println(listaRutasHasTransportes.size());
        for (int i = 0; i < listaRutasHasTransportes.size(); i++) {
            RutaHasTransporteDTO temRutaHasTransporteDTO = new RutaHasTransporteDTO();

            temRutaHasTransporteDTO.setIdRutaTransporte(listaRutasHasTransportes.get(i).getIdRutaHasTransporte());
            temRutaHasTransporteDTO.setTerminalOrigen(
                    listaRutasHasTransportes.get(i).getDetalleRuta().getIdTerminalOrigen().getNombreterminal());
            temRutaHasTransporteDTO.setTerminalDestino(
                    listaRutasHasTransportes.get(i).getDetalleRuta().getIdTerminalDestino().getNombreterminal());
            temRutaHasTransporteDTO.setIdTerminalOrigen(
                    listaRutasHasTransportes.get(i).getDetalleRuta().getIdTerminalOrigen().getIdt_terminales());
            temRutaHasTransporteDTO.setIdTerminalDestino(
                    listaRutasHasTransportes.get(i).getDetalleRuta().getIdTerminalDestino().getIdt_terminales());
            temRutaHasTransporteDTO.setHoraSalida(listaRutasHasTransportes.get(i).getHorariosSalida().getHoraSalida());

            // temRutaHasTransporteDTO.setLogo(listaRutasHasTransportes.get(i).getAutobus().getAfiliado().getLogotipo());
            temRutaHasTransporteDTO.setIdAutobus(listaRutasHasTransportes.get(i).getIdAutobus().getIdt_autobus());
            temRutaHasTransporteDTO.setCantAdultos(listaRutasHasTransportes.get(i).getIdAutobus().getAsientosAdultos());
            temRutaHasTransporteDTO.setCantNinos(listaRutasHasTransportes.get(i).getIdAutobus().getAsientosNinios());
            temRutaHasTransporteDTO.setCantInapam(listaRutasHasTransportes.get(i).getIdAutobus().getAsientosInapam());
            temRutaHasTransporteDTO.setSanitario(listaRutasHasTransportes.get(i).getIdAutobus().getSanitario());
            temRutaHasTransporteDTO.setTipoTrasporte(
                    listaRutasHasTransportes.get(i).getIdAutobus().getTipoTransporte().getIdTipoTransporte());
            temRutaHasTransporteDTO.setPrecioAdultos(Double.parseDouble(listaRutasHasTransportes.get(i).getAdulto()));
            temRutaHasTransporteDTO.setPrecioNinos(Double.parseDouble(listaRutasHasTransportes.get(i).getNino()));
            temRutaHasTransporteDTO.setPrecioInapam(Double.parseDouble(listaRutasHasTransportes.get(i).getInapam()));
            temRutaHasTransporteDTO.setDisponibilidadAutobusLong(
                    listaRutasHasTransportes.get(i).getIdAutobus().getDisponibilidad().getIdc_disponibilidad());
            temRutaHasTransporteDTO
                    .setIdRuta(listaRutasHasTransportes.get(i).getDetalleRuta().getT_ruta().getIdt_ruta());

            listaDTO.add(temRutaHasTransporteDTO);

        }

        return listaDTO;
    }

    @GetMapping("/{id}")
    public RutaHasTransporteDTO getById(@PathVariable(value = "id") Long id) {

        RutaHasTransporte rutaTransporte = rutaTransporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("rutaHasTransporte not found with id: " + id));

        RutaHasTransporteDTO temRutaHasTransporteDTO = new RutaHasTransporteDTO();

        temRutaHasTransporteDTO.setIdRutaTransporte(rutaTransporte.getIdRutaHasTransporte());
        temRutaHasTransporteDTO
                .setTerminalOrigen(rutaTransporte.getDetalleRuta().getIdTerminalOrigen().getNombreterminal());
        temRutaHasTransporteDTO
                .setTerminalDestino(rutaTransporte.getDetalleRuta().getIdTerminalDestino().getNombreterminal());
        temRutaHasTransporteDTO
                .setIdTerminalOrigen(rutaTransporte.getDetalleRuta().getIdTerminalOrigen().getIdt_terminales());
        temRutaHasTransporteDTO
                .setIdTerminalDestino(rutaTransporte.getDetalleRuta().getIdTerminalDestino().getIdt_terminales());
        temRutaHasTransporteDTO.setHoraSalida(rutaTransporte.getHorariosSalida().getHoraSalida());

        // temRutaHasTransporteDTO.setLogo(listaRutasHasTransportes.get(i).getAutobus().getAfiliado().getLogotipo());
        temRutaHasTransporteDTO.setIdAutobus(rutaTransporte.getIdAutobus().getIdt_autobus());
        temRutaHasTransporteDTO.setCantAdultos(rutaTransporte.getIdAutobus().getAsientosAdultos());
        temRutaHasTransporteDTO.setCantNinos(rutaTransporte.getIdAutobus().getAsientosNinios());
        temRutaHasTransporteDTO.setCantInapam(rutaTransporte.getIdAutobus().getAsientosInapam());
        temRutaHasTransporteDTO.setSanitario(rutaTransporte.getIdAutobus().getSanitario());
        temRutaHasTransporteDTO
                .setTipoTrasporte(rutaTransporte.getIdAutobus().getTipoTransporte().getIdTipoTransporte());
        temRutaHasTransporteDTO.setPrecioAdultos(Double.parseDouble(rutaTransporte.getAdulto()));
        temRutaHasTransporteDTO.setPrecioNinos(Double.parseDouble(rutaTransporte.getNino()));
        temRutaHasTransporteDTO.setPrecioInapam(Double.parseDouble(rutaTransporte.getInapam()));
        temRutaHasTransporteDTO.setDisponibilidadAutobusLong(
                rutaTransporte.getIdAutobus().getDisponibilidad().getIdc_disponibilidad());
        temRutaHasTransporteDTO.setIdRuta(rutaTransporte.getDetalleRuta().getT_ruta().getIdt_ruta());

        return temRutaHasTransporteDTO;
    }

    @GetMapping("/terminales/{idOrigen}/{idDestino}")
    public List<RutaHasTransporte> getByTerminales(@PathVariable(value = "idOrigen") Long idOrigen,
            @PathVariable(value = "idDestino") Long idDestino) {
        return rutaTransporteRepository
                .findByDetalleRutaIdTerminalOrigenIdTerminalesAndDetalleRutaIdTerminalDestinoIdTerminales(idOrigen,
                        idDestino);
    }

    @GetMapping("/terminales/{idOrigen}/{idDestino}/{horario}")
    public RutaHasTransporte getByTerminalesHorario(@PathVariable(value = "idOrigen") Long idOrigen,
            @PathVariable(value = "idDestino") Long idDestino, @PathVariable(value = "horario") String hora) {
        return rutaTransporteRepository
                .findByDetalleRutaIdTerminalOrigenIdTerminalesAndDetalleRutaIdTerminalDestinoIdTerminalesAndHorariosSalidaHoraSalida(
                        idOrigen, idDestino, hora);
    }

    // lista general asientos reservados............
    @GetMapping("/temporalAsiento")
    public List<AsientoNormalTemp> getTemporal() {
        List<AsientoNormalTemp> listaAsientoTemp = asientoTempNormalRepository.findAll();
        return listaAsientoTemp;
    }

    // TRAER ASIENTOS OCUPADOS
    @Module("CentralBus - Comprar Boleto/Obtener Asientos Ocupados")
    @GetMapping("/asientoOcupado/{idAutobus}/{fecha}/{terminalOrigen}/{terminalDestino}/{hora}/{ruta}") // se agregó la
                                                                                                        // hora, y la
    @Transactional(isolation = Isolation.SERIALIZABLE) // ruta
    public List<AsientosOcupadosDTO> getAsientos(@PathVariable(value = "idAutobus") Long idAutobus,
            @PathVariable(value = "fecha") String fecha, @PathVariable(value = "terminalOrigen") int origen,
            @PathVariable(value = "terminalDestino") int destino, @PathVariable(value = "hora") String hora,
            @PathVariable(value = "ruta") int ruta) {
        // variable de retorno
        List<AsientosOcupadosDTO> asientosOcupados = new ArrayList<>();
        List<DetalleVentaMovil> ventaViaje = detalleVentaRepository.getVentaViaje(idAutobus, fecha, ruta);

        Map<LocalDateTime, Integer> fechaHoraSecuencias = new HashMap<>();

        if (!ventaViaje.isEmpty()) {
            // Esta fecha solo se utiliza para obtener su secuencia, la cual sirve para
            // calcular las fechas de salida
            String fechaViaje = ventaViaje.get(0).getFechaViaje(); // formato: '2024-06-30 14:00:00' string
            List<RutasDetalles> detalleRutaDetalles = rutadetalleRepository.findListByRuta(ruta);
            if (detalleRutaDetalles.size() >= 1) {
                LocalDateTime fechaSalidaPrimera = null;
                LocalDateTime fechaSalidaUltima = null;

                LocalDateTime fechaInicio = LocalDateTime.parse(fechaViaje,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                int secuenciaPrimera = 1;
                int secuenciaUltima = 1;
                int secuencia = 1;

                int horasAcumuladas = 0;
                int minutosAcumulados = 0;
                int segundosAcumulados = 0;

                for (RutasDetalles rd : detalleRutaDetalles) {
                    if (rd.getIdTerminalDestino().getIdt_terminales() == ventaViaje.get(0).getVentaViajes()
                            .getTerminalOrigen().getIdt_terminales()) {
                        secuencia = Integer.parseInt(rd.getSecuencia());
                    }
                }

                for (RutasDetalles rd : detalleRutaDetalles) {
                    int secuenciaActual = Integer.parseInt(rd.getSecuencia());
                    String[] tiempoRutaArray = rd.getTiempoRuta().split(":");
                    int horasRuta = Integer.parseInt(tiempoRutaArray[0]);
                    int minutosRuta = Integer.parseInt(tiempoRutaArray[1]);
                    int segundosRuta = Integer.parseInt(tiempoRutaArray[2]);

                    horasAcumuladas += horasRuta;
                    minutosAcumulados += minutosRuta;
                    segundosAcumulados += segundosRuta;

                    // Ajustar minutos y segundos
                    if (segundosAcumulados >= 60) {
                        minutosAcumulados += segundosAcumulados / 60;
                        segundosAcumulados %= 60;
                    }
                    if (minutosAcumulados >= 60) {
                        horasAcumuladas += minutosAcumulados / 60;
                        minutosAcumulados %= 60;
                    }

                    if ((secuencia == secuenciaActual
                            && !rd.equals(detalleRutaDetalles.get(detalleRutaDetalles.size() - 1)))
                            || (secuencia == 1 && 1 == secuenciaActual)) {
                        horasAcumuladas = 0;
                        minutosAcumulados = 0;
                        segundosAcumulados = 0;
                    }
                    // 2
                    if (secuencia > secuenciaActual) {
//                        System.err.println("1===");
                        secuenciaPrimera = (secuenciaPrimera > secuenciaActual) ? secuenciaActual : secuenciaPrimera;
                        fechaSalidaPrimera = ajustarFecha(fechaInicio, horasAcumuladas, minutosAcumulados,
                                segundosAcumulados, false);

                    } else if (secuencia < secuenciaActual) {
//                        System.err.println("2===");
                        fechaSalidaUltima = ajustarFecha(fechaInicio, horasAcumuladas, minutosAcumulados,
                                segundosAcumulados, true);

                        secuenciaUltima = (secuenciaUltima < secuenciaActual) ? secuenciaActual : secuenciaUltima;
                    } else if (secuencia == secuenciaActual) {
//                        System.err.println("3===");
                        // Verifica si es el último elemento
                        if (rd.equals(detalleRutaDetalles.get(detalleRutaDetalles.size() - 1))) {
                            secuenciaPrimera = (secuenciaPrimera > secuenciaActual) ? secuenciaActual
                                    : secuenciaPrimera;

                            fechaSalidaPrimera = ajustarFecha(fechaInicio, horasAcumuladas, minutosAcumulados,
                                    segundosAcumulados, false);

                            secuenciaUltima = secuenciaActual;
                            fechaSalidaUltima = fechaInicio;
                        } else {
                            secuenciaPrimera = (secuenciaPrimera > secuenciaActual) ? secuenciaActual
                                    : secuenciaPrimera;
                            fechaSalidaPrimera = ajustarFecha(fechaInicio, horasAcumuladas, minutosAcumulados,
                                    segundosAcumulados, false);
                        }
                    }
                }

                if (fechaSalidaPrimera != null && fechaSalidaUltima != null) {
                    // AGREGAR FECHAS Y HORARIOS PARA TODAS LAS SECUENCIAS
                    LocalDateTime fechaAgregar = fechaSalidaPrimera;
                    int horasAcumuladas2 = 0;
                    int minutosAcumulados2 = 0;
                    int segundosAcumulados2 = 0;
                    int indexSecuencia = 0;
                    for (RutasDetalles rd : detalleRutaDetalles) {
                        indexSecuencia++;
                        int secuenciaActual = Integer.parseInt(rd.getSecuencia());
                        String[] tiempoRutaArray = rd.getTiempoRuta().split(":");
                        int horasRuta = Integer.parseInt(tiempoRutaArray[0]);
                        int minutosRuta = Integer.parseInt(tiempoRutaArray[1]);
                        int segundosRuta = Integer.parseInt(tiempoRutaArray[2]);

                        if (!rd.getSecuencia().trim().equals("1")) {
                            horasAcumuladas2 += horasRuta;
                            minutosAcumulados2 += minutosRuta;
                            segundosAcumulados2 += segundosRuta;
                        }

                        fechaAgregar = ajustarFecha(fechaSalidaPrimera, horasAcumuladas2, minutosAcumulados2,
                                segundosAcumulados2, true);
                        fechaHoraSecuencias.put(fechaAgregar, indexSecuencia);
                    }
//                  para ver
//                    for (Map.Entry<LocalDateTime, Integer> entry : fechaHoraSecuencias.entrySet()) {
//                        LocalDateTime key = entry.getKey();
//                        Integer value = entry.getValue();
//                        System.out.println("Clave: " + key + ", Valor: " + value);
//                    }

                    // obtener las secuencia a donde viaja
                    int secuenciaIda = 0;
                    int secuenciaVuelta = 0;
                    for (RutasDetalles rd : detalleRutaDetalles) {
                        if (Long.valueOf(rd.getIdTerminalOrigen().getIdt_terminales()).intValue() == origen) {
                            secuenciaIda = Integer.parseInt(rd.getSecuencia());
                        }
                        if (Long.valueOf(rd.getIdTerminalDestino().getIdt_terminales()).intValue() == destino) {
                            secuenciaVuelta = Integer.parseInt(rd.getSecuencia());
                        }
                    }
//                    System.out.println("La secuencia de ida es " + secuenciaIda);
//                    System.out.println("La secuencia de vuelta es " + secuenciaVuelta);
//                    System.out.println("Esta ruta tiene un total de secuencias: " + fechaHoraSecuencias.size());

                    // Se convierte a string para hacer la consulta sql
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String fechaSalidaPrimeraStr = fechaSalidaPrimera.format(formatter);
                    String fechaSalidaUltimaStr = fechaSalidaUltima.format(formatter);
                    List<DetalleVentaMovil> detalleVentas = detalleVentaRepository.getDetalleVentas(idAutobus,
                            fechaSalidaPrimeraStr, fechaSalidaUltimaStr, ruta);

                    if (!detalleVentas.isEmpty()) {
                        for (int i = 0; i < detalleVentas.size(); i++) {
                            // obtener las secuencia a donde viaja
                            int secuenciaIdaL = 0;
                            int secuenciaVueltaL = 0;
                            for (RutasDetalles rd : detalleRutaDetalles) {
                                if (Long.valueOf(rd.getIdTerminalOrigen().getIdt_terminales())
                                        .intValue() == detalleVentas.get(i).getVentaViajes().getTerminalOrigen()
                                                .getIdt_terminales()) {
                                    secuenciaIdaL = Integer.parseInt(rd.getSecuencia());
                                }
                                if (Long.valueOf(rd.getIdTerminalDestino().getIdt_terminales())
                                        .intValue() == detalleVentas.get(i).getVentaViajes().getTerminalDestino()
                                                .getIdt_terminales()) {
                                    secuenciaVueltaL = Integer.parseInt(rd.getSecuencia());
                                }
                            }
                            // Si el asiento es desocupado antes de abordar
                            if (secuenciaVueltaL < secuenciaIda) {
                                continue;
                                // Si el asiento es ocupado despues de desabordar
                            } else if (secuenciaVuelta < secuenciaIdaL) {
                                continue;
                            }

                            AsientosOcupadosDTO temp = new AsientosOcupadosDTO();
                            temp.setIdTerminalOrigen(
                                    detalleVentas.get(i).getVentaViajes().getTerminalOrigen().getIdt_terminales());
                            temp.setIdTerminalDestino(
                                    detalleVentas.get(i).getVentaViajes().getTerminalDestino().getIdt_terminales());
                            temp.setIdRuta(detalleVentas.get(i).getVentaViajes().getRuta());
                            temp.setIdAutobus((long) detalleVentas.get(i).getAutobus());
                            temp.setNumeroAsiento(detalleVentas.get(i).getNumeroAsiento());
                            temp.setFechaHoraViaje(detalleVentas.get(i).getFechaViaje());

                            if (detalleVentas.get(i).getStatusDisponible() == 3) {
                                temp.setStatusCancelado(1);
                            } else {
                                temp.setStatusCancelado(0);
                            }

                            temp.setTipoAsiento(detalleVentas.get(i).getTipoBoleto().getIdc_tipoBoleto());
                            temp.setAsientoTemp("Ocupado");
                            temp.setTipoPlanta(detalleVentas.get(i).getTipoPlanta());

                            asientosOcupados.add(temp);

                        }

                    }
//                    System.out.println("Fecha de salida de la primera secuencia: " + fechaSalidaPrimeraStr);
//                    System.out.println("Fecha de salida de la primera secuencia: " + fechaInicio);
//                    System.out.println("Fecha de salida de la última secuencia: " + fechaSalidaUltimaStr);
//                    System.out.println("Secuencia primera = " + secuenciaPrimera);
//                    System.out.println("Secuencia ultima = " + secuenciaUltima);
                }
            } else {
                System.out.println("No se encontraron detalles de ruta.");
            }
        } else {
            System.out.println("Sin Asientos Ocupados");
        }

        // asientos temporales
        List<AsientoNormalTemp> asientosTemp = asientoTempNormalRepository.getAsientosOcupadosTemp(idAutobus, fecha,
                origen, destino, hora + ":00");
        System.out.println("Buscando asientos temporales... ");
        for (int i = 0; i < asientosTemp.size(); i++) {

            AsientosOcupadosDTO temp = new AsientosOcupadosDTO();
            temp.setIdTerminalOrigen(asientosTemp.get(i).getTerminalOrigen().getCiudad().getIdc_ciudades());
            temp.setIdTerminalDestino(asientosTemp.get(i).getTerminalDestino().getCiudad().getIdc_ciudades());
            temp.setIdRuta(Long.valueOf(asientosTemp.get(i).getRuta()));
            temp.setNumeroAsiento(asientosTemp.get(i).getNumeroAsiento());
            temp.setFechaHoraViaje(asientosTemp.get(i).getFechaViaje());
            temp.setTipoAsiento(asientosTemp.get(i).getTipoBoleto().getIdc_tipoBoleto());
            temp.setIdAutobus((long) asientosTemp.get(i).getAutobus());
            temp.setAsientoTemp("Reservado");
            temp.setTipoPlanta(asientosTemp.get(i).getTipoPlanta());

            asientosOcupados.add(temp);

        }
        // fin de asientos temporales

        return asientosOcupados;
    }

    private static LocalDateTime ajustarFecha(LocalDateTime fechaInicio, int horas, int minutos, int segundos,
            boolean esSumar) {
        int dias = 0;

        // Ajustar minutos y segundos que exceden 60
        if (segundos >= 60) {
            minutos += segundos / 60;
            segundos %= 60;
        }

        if (minutos >= 60) {
            horas += minutos / 60;
            minutos %= 60;
        }

        // Ajustar horas que exceden 24
        if (horas >= 24) {
            dias = horas / 24;
            horas %= 24;
        }

        if (esSumar) {
            fechaInicio = fechaInicio.plusDays(dias)
                    .plusHours(horas)
                    .plusMinutes(minutos)
                    .plusSeconds(segundos);
        } else {
            fechaInicio = fechaInicio.minusDays(dias)
                    .minusHours(horas)
                    .minusMinutes(minutos)
                    .minusSeconds(segundos);
        }

        return fechaInicio;
    }

    /** LLENAR TABLA TEMP NORMAL */
    @SuppressWarnings("unchecked")
    @Module("CentralBus - Comprar Boleto/Reservar Asiento")
    @PostMapping("/crear")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<List<AsientoNormalTemp>> crearAsientoNormalTemp(@RequestBody JSONObject asientoNormalTemp) {
        List<AsientoNormalTemp> tempAsientos = new ArrayList<>();
        Terminales origen = this.terminalRepository
                .findById(Long.parseLong(asientoNormalTemp.get("terminalOrigen").toString()))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + asientoNormalTemp.get("terminalOrigen")));
        Terminales destino = this.terminalRepository
                .findById(Long.parseLong(asientoNormalTemp.get("terminalDestino").toString()))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + asientoNormalTemp.get("terminalDestino")));
        Autobus autobus = this.autobusRepository.findById(Long.parseLong(asientoNormalTemp.get("autobus").toString()))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Bus not found with id: " + asientoNormalTemp.get("autobus")));

        int totalAsientosAutobus = autobus.getAsientosNinios() + autobus.getAsientosAdultos()
                + autobus.getAsientosInapam();
        int plantaBajaAsientosAutobusOcupado = 0;
        int plantaAltaAsientosAutobusOcupado = 0;
        int plantaBajaAsientosAutobusReservar = 0;
        int plantaAltaAsientosAutobusReservar = 0;
        Boolean sinSeleccionAsiento = false;

        String fechaViaje = asientoNormalTemp.get("fechaViaje").toString().split(" ")[0];
        // formatt //mm:ss:ms
        String horaViaje = asientoNormalTemp.get("fechaViaje").toString().split(" ")[1];
        // formatt //mm:ss
        String modifiedTime = horaViaje.substring(0, horaViaje.length() - 3);
        List<AsientosOcupadosDTO> asientosOcupados = getAsientos(
                Long.parseLong(asientoNormalTemp.get("autobus").toString()),
                fechaViaje,
                origen.getIdt_terminales().intValue(), destino.getIdt_terminales().intValue(),
                modifiedTime,
                Integer.parseInt(asientoNormalTemp.get("ruta").toString()));
        System.err.println("Cantidad de asientos ocupados " + " = " + asientosOcupados.size());

        List<String> asientos = (List<String>) asientoNormalTemp.get("asientos");

        // Comprobar si los asiento que ha seleccionado no los ha comprado nadie mas
        Integer asientosAunDisponibles = totalAsientosAutobus - asientosOcupados.size();
        if (asientos.size() > asientosAunDisponibles) {
            // Hay mas asiento que los pasajeros que selecciono al viajar, redirigir al
            // inicio de la app
            return ResponseEntity.status(HttpStatus.CONFLICT).body(tempAsientos);
        }

        System.err.println("Asientos aun disponibles " + asientosAunDisponibles);
        for (int i = 0; i < asientos.size(); i++) {
            // se crea el objeto temp
            AsientoNormalTemp tempAsiento = new AsientoNormalTemp();
            //
            String[] as = asientos.get(i).split("%");
            int nAsiento = Integer.parseInt(as[0]);
            Long tipoBLong = Long.parseLong(as[1]);
            Long idTipoBoleto = tipoBLong;
            // se assignan boletos y planta
            // planta baja vip = 1 && planta alta = 2
            System.err.println("Mi tipo de boleto = " + tipoBLong);
            if (tipoBLong == 4l) {
                idTipoBoleto = 1l;
                tempAsiento.setTipoPlanta(1);
                plantaBajaAsientosAutobusReservar += 1;
            } else if (tipoBLong == 5l) {
                idTipoBoleto = 2l;
                tempAsiento.setTipoPlanta(1);
                plantaBajaAsientosAutobusReservar += 1;
            } else if (tipoBLong == 6l) {
                idTipoBoleto = 3l;
                tempAsiento.setTipoPlanta(1);
                plantaBajaAsientosAutobusReservar += 1;
            } else {
                idTipoBoleto = tipoBLong;
                tempAsiento.setTipoPlanta(2);
                plantaAltaAsientosAutobusReservar += 1;
            }
            //
            TipoBoleto tipoBoleto = this.tipoBoletoRepository.findById(idTipoBoleto)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + tipoBLong));

            // se rellena el objeto temp
            tempAsiento.setTerminalOrigen(origen);
            tempAsiento.setTerminalDestino(destino);
            tempAsiento.setRuta(Integer.parseInt(asientoNormalTemp.get("ruta").toString()));
            tempAsiento.setNumeroAsiento(nAsiento);
            tempAsiento.setFechaViaje(asientoNormalTemp.get("fechaViaje").toString());
            tempAsiento.setTipoBoleto(tipoBoleto);
            tempAsiento.setAutobus(Double.parseDouble(asientoNormalTemp.get("autobus").toString()));
            tempAsiento.setFechaInsert(asientoNormalTemp.get("fechaInsert").toString());
            for (AsientosOcupadosDTO asientoOcupado : asientosOcupados) {
                if (asientoOcupado.getNumeroAsiento() != 0
                        && asientoOcupado.getNumeroAsiento() == tempAsiento.getNumeroAsiento() &&
                        asientoOcupado.getTipoAsiento().intValue() == tempAsiento.getTipoPlanta()) {
                    // Alguno de los asientos seleccionados ya fue reservado, actualizar pantalla de
                    // asientos con los asientos recientemente reservados
                    System.err.println("Alguno de los asientos seleccionados ya fue reservado");
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(tempAsientos);

                } else {
                    if (asientoOcupado.getNumeroAsiento() == 0) {
                        sinSeleccionAsiento = true;
                    }
                }
            }
        }

        // Comprobar si aun hay disponibilidad para sin seleccion de asientos
        if (sinSeleccionAsiento) {
            for (AsientosOcupadosDTO asientoOcupado : asientosOcupados) {
                if (asientoOcupado.getTipoPlanta() == 1) {
                    plantaBajaAsientosAutobusOcupado += 1;
                } else {
                    plantaAltaAsientosAutobusOcupado += 1;
                }
            }
            int totalAsientosPlantaBaja = 0;
            int totalAsientosPlantaAlta = 0;
            if (autobus.getTipoTransporte().getIdTipoTransporte() == 1
                    || autobus.getTipoTransporte().getIdTipoTransporte() == 2
                    || autobus.getTipoTransporte().getIdTipoTransporte() == 3
                    || autobus.getTipoTransporte().getIdTipoTransporte() == 4
                    || autobus.getTipoTransporte().getIdTipoTransporte() == 5
                    || autobus.getTipoTransporte().getIdTipoTransporte() == 6) {
                // No cuentan con planta de lujo
                totalAsientosPlantaAlta = totalAsientosAutobus;
                // 62 pasajeros, planta baja = 9
            } else if (autobus.getTipoTransporte().getIdTipoTransporte() == 7) {
                totalAsientosPlantaBaja = 9;
                totalAsientosPlantaAlta = totalAsientosAutobus - totalAsientosPlantaBaja;
                // 64 pasajeros, planta baja = 16
            } else if (autobus.getTipoTransporte().getIdTipoTransporte() == 8) {
                totalAsientosPlantaBaja = 16;
                totalAsientosPlantaAlta = totalAsientosAutobus - totalAsientosPlantaBaja;
                // 67 pasajeros, planta baja = 9
            } else if (autobus.getTipoTransporte().getIdTipoTransporte() == 9) {
                totalAsientosPlantaBaja = 9;
                totalAsientosPlantaAlta = totalAsientosAutobus - totalAsientosPlantaBaja;
            } else {
                System.err.println("No se reconoce este tipo de autobus. Error en la logica, validar de nuevo");
            }

            int totalAsientosBajaDisponible = totalAsientosPlantaBaja - plantaBajaAsientosAutobusOcupado;
            int totalAsientosAltaDisponible = totalAsientosPlantaAlta - plantaAltaAsientosAutobusOcupado;
            System.err.println("Mi tipo de transporte = " + autobus.getTipoTransporte().getIdTipoTransporte());
            System.err.println("Planta Alta (a reservar/disponibles) " + plantaAltaAsientosAutobusReservar + " = "
                    + totalAsientosAltaDisponible);
            System.err.println("Planta Baja (a reservar/disponibles) " + plantaBajaAsientosAutobusReservar + " = "
                    + totalAsientosBajaDisponible);
            if (plantaAltaAsientosAutobusReservar > totalAsientosAltaDisponible) {
                // Sin asientos, los asientos de plata alta ya se agotaron
                System.err.println("Los asientos de plata alta ya se agotaron");
                if (totalAsientosPlantaBaja == 0) {
                    System.err.println("Alguno de los asientos seleccionados ya fue reservado");
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(tempAsientos);
                } else {
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(tempAsientos);
                }
            }
            if (plantaBajaAsientosAutobusReservar > totalAsientosBajaDisponible) {
                // Sin asientos, los asientos de plata baja ya se agotaron
                System.err.println("Los asientos de plata baja ya se agotaron");
                return ResponseEntity.status(HttpStatus.RESET_CONTENT).body(tempAsientos);
            }

        }
        // Ahora siii, esta ok reservar
        for (int i = 0; i < asientos.size(); i++) {
            // se crea el objeto temp
            AsientoNormalTemp asientoGuardado = new AsientoNormalTemp();
            // se llaman los datos
            String[] as = asientos.get(i).split("%");
            int nAsiento = Integer.parseInt(as[0]);
            Long tipoBLong = Long.parseLong(as[1]);
            Long idTipoBoleto = tipoBLong;
            // se assignan boletos y planta
            if (tipoBLong == 4l) {
                idTipoBoleto = 1l;
                asientoGuardado.setTipoPlanta(1);
            } else if (tipoBLong == 5l) {
                idTipoBoleto = 2l;
                asientoGuardado.setTipoPlanta(1);
            } else if (tipoBLong == 6l) {
                idTipoBoleto = 3l;
                asientoGuardado.setTipoPlanta(1);
            } else {
                idTipoBoleto = tipoBLong;
                asientoGuardado.setTipoPlanta(2);
            }

            TipoBoleto tipoBoleto = this.tipoBoletoRepository.findById(idTipoBoleto)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + tipoBLong));

            // se rellena el objeto temp
            asientoGuardado.setTerminalOrigen(origen);
            asientoGuardado.setTerminalDestino(destino);
            asientoGuardado.setRuta(Integer.parseInt(asientoNormalTemp.get("ruta").toString()));
            asientoGuardado.setNumeroAsiento(nAsiento);
            asientoGuardado.setFechaViaje(asientoNormalTemp.get("fechaViaje").toString());
            asientoGuardado.setTipoBoleto(tipoBoleto);
            asientoGuardado.setAutobus(Double.parseDouble(asientoNormalTemp.get("autobus").toString()));
            asientoGuardado.setFechaInsert(asientoNormalTemp.get("fechaInsert").toString());

            if (!asientoNormalTemp.get("usuarioFinal").toString().isEmpty()) {
                asientoGuardado.setUsuarioFinal(Long.parseLong(asientoNormalTemp.get("usuarioFinal").toString()));
            } else {
                asientoGuardado.setUsuarioFinal(null);
            }
            AsientoNormalTemp guardado = asientoTempNormalRepository.save(asientoGuardado);
            tempAsientos.add(guardado);
        }
        return ResponseEntity.ok(tempAsientos);
    }

    // crear asiento temporal para invitado
    @SuppressWarnings("unchecked")
    @Module("CentralBus - Comprar Boleto/Invitado/Reservar Asiento")
    @PostMapping("/crearInvitado")
    @Transactional
    public ResponseEntity<List<AsientoNormalTemp>> crearAsientoNormalTempInvitado(
            @RequestBody JSONObject asientoNormalTemp) {
        List<AsientoNormalTemp> tempAsientos = new ArrayList<>();
        Terminales origen = this.terminalRepository
                .findById(Long.parseLong(asientoNormalTemp.get("terminalOrigen").toString()))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + asientoNormalTemp.get("terminalOrigen")));
        Terminales destino = this.terminalRepository
                .findById(Long.parseLong(asientoNormalTemp.get("terminalDestino").toString()))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + asientoNormalTemp.get("terminalDestino")));
        Autobus autobus = this.autobusRepository.findById(Long.parseLong(asientoNormalTemp.get("autobus").toString()))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Bus not found with id: " + asientoNormalTemp.get("autobus")));

        int totalAsientosAutobus = autobus.getAsientosNinios() + autobus.getAsientosAdultos()
                + autobus.getAsientosInapam();
        int plantaBajaAsientosAutobusOcupado = 0;
        int plantaAltaAsientosAutobusOcupado = 0;
        int plantaBajaAsientosAutobusReservar = 0;
        int plantaAltaAsientosAutobusReservar = 0;
        Boolean sinSeleccionAsiento = false;

        String fechaViaje = asientoNormalTemp.get("fechaViaje").toString().split(" ")[0];
        // formatt //mm:ss:ms
        String horaViaje = asientoNormalTemp.get("fechaViaje").toString().split(" ")[1];
        // formatt //mm:ss
        String modifiedTime = horaViaje.substring(0, horaViaje.length() - 3);
        List<AsientosOcupadosDTO> asientosOcupados = getAsientos(
                Long.parseLong(asientoNormalTemp.get("autobus").toString()),
                fechaViaje,
                origen.getIdt_terminales().intValue(), destino.getIdt_terminales().intValue(),
                modifiedTime,
                Integer.parseInt(asientoNormalTemp.get("ruta").toString()));
        System.err.println("Cantidad de asientos ocupados " + " = " + asientosOcupados.size());

        List<String> asientos = (List<String>) asientoNormalTemp.get("asientos");

        // Comprobar si los asiento que ha seleccionado no los ha comprado nadie mas
        Integer asientosAunDisponibles = totalAsientosAutobus - asientosOcupados.size();
        if (asientos.size() > asientosAunDisponibles) {
            // Hay mas asiento que los pasajeros que selecciono al viajar, redirigir al
            // inicio de la app
            return ResponseEntity.status(HttpStatus.CONFLICT).body(tempAsientos);
        }

        System.err.println("asiento aun disponibles " + asientosAunDisponibles);
        for (int i = 0; i < asientos.size(); i++) {
            // se crea el objeto temp
            AsientoNormalTemp tempAsiento = new AsientoNormalTemp();
            //
            String[] as = asientos.get(i).split("%");
            int nAsiento = Integer.parseInt(as[0]);
            Long tipoBLong = Long.parseLong(as[1]);
            Long idTipoBoleto = tipoBLong;
            // se assignan boletos y planta
            // planta baja vip = 1 && planta alta = 2
            if (tipoBLong == 4l) {
                idTipoBoleto = 1l;
                tempAsiento.setTipoPlanta(1);
                plantaBajaAsientosAutobusReservar += 1;
            } else if (tipoBLong == 5l) {
                idTipoBoleto = 2l;
                tempAsiento.setTipoPlanta(1);
                plantaBajaAsientosAutobusReservar += 1;
            } else if (tipoBLong == 6l) {
                idTipoBoleto = 3l;
                tempAsiento.setTipoPlanta(1);
                plantaBajaAsientosAutobusReservar += 1;
            } else {
                idTipoBoleto = tipoBLong;
                tempAsiento.setTipoPlanta(2);
                plantaAltaAsientosAutobusReservar += 1;
            }
            //
            TipoBoleto tipoBoleto = this.tipoBoletoRepository.findById(idTipoBoleto)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + tipoBLong));

            // se rellena el objeto temp
            tempAsiento.setTerminalOrigen(origen);
            tempAsiento.setTerminalDestino(destino);
            tempAsiento.setRuta(Integer.parseInt(asientoNormalTemp.get("ruta").toString()));
            tempAsiento.setNumeroAsiento(nAsiento);
            tempAsiento.setFechaViaje(asientoNormalTemp.get("fechaViaje").toString());
            tempAsiento.setTipoBoleto(tipoBoleto);
            tempAsiento.setAutobus(Double.parseDouble(asientoNormalTemp.get("autobus").toString()));
            tempAsiento.setFechaInsert(asientoNormalTemp.get("fechaInsert").toString());
            for (AsientosOcupadosDTO asientoOcupado : asientosOcupados) {
                if (asientoOcupado.getNumeroAsiento() != 0
                        && asientoOcupado.getNumeroAsiento() == tempAsiento.getNumeroAsiento() &&
                        asientoOcupado.getTipoAsiento().intValue() == tempAsiento.getTipoPlanta()) {
                    // Alguno de los asientos seleccionados ya fue reservado, actualizar pantalla de
                    // asientos con los asientos recientemente reservados
                    System.err.println("Alguno de los asientos seleccionados ya fue reservado");
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(tempAsientos);

                } else {
                    if (asientoOcupado.getNumeroAsiento() == 0) {
                        sinSeleccionAsiento = true;
                    }
                }
            }
        }

        // Comprobar si aun hay disponibilidad para sin seleccion de asientos
        if (sinSeleccionAsiento) {
            for (AsientosOcupadosDTO asientoOcupado : asientosOcupados) {
                if (asientoOcupado.getTipoPlanta() == 1) {
                    plantaBajaAsientosAutobusOcupado += 1;
                } else {
                    plantaAltaAsientosAutobusOcupado += 1;
                }
            }
            int totalAsientosPlantaBaja = 0;
            int totalAsientosPlantaAlta = 0;
            if (autobus.getTipoTransporte().getIdTipoTransporte() == 1
                    || autobus.getTipoTransporte().getIdTipoTransporte() == 2
                    || autobus.getTipoTransporte().getIdTipoTransporte() == 3
                    || autobus.getTipoTransporte().getIdTipoTransporte() == 4
                    || autobus.getTipoTransporte().getIdTipoTransporte() == 5
                    || autobus.getTipoTransporte().getIdTipoTransporte() == 6) {
                // No cuentan con planta de lujo
                totalAsientosPlantaAlta = totalAsientosAutobus;
                // 62 pasajeros, planta baja = 9
            } else if (autobus.getTipoTransporte().getIdTipoTransporte() == 7) {
                totalAsientosPlantaBaja = 9;
                totalAsientosPlantaAlta = totalAsientosAutobus - totalAsientosPlantaBaja;
                // 64 pasajeros, planta baja = 16
            } else if (autobus.getTipoTransporte().getIdTipoTransporte() == 8) {
                totalAsientosPlantaBaja = 16;
                totalAsientosPlantaAlta = totalAsientosAutobus - totalAsientosPlantaBaja;
                // 67 pasajeros, planta baja = 9
            } else if (autobus.getTipoTransporte().getIdTipoTransporte() == 9) {
                totalAsientosPlantaBaja = 9;
                totalAsientosPlantaAlta = totalAsientosAutobus - totalAsientosPlantaBaja;
            } else {
                System.err.println("No se reconoce este tipo de autobus. Error en la logica, validar de nuevo");
            }

            int totalAsientosBajaDisponible = totalAsientosPlantaBaja - plantaBajaAsientosAutobusOcupado;
            int totalAsientosAltaDisponible = totalAsientosPlantaAlta - plantaAltaAsientosAutobusOcupado;
            System.err.println("Mi tipo de transporte = " + autobus.getTipoTransporte().getIdTipoTransporte());
            System.err.println("Planta Alta (a reservar/disponibles) " + plantaAltaAsientosAutobusReservar + " = "
                    + totalAsientosAltaDisponible);
            System.err.println("Planta Baja (a reservar/disponibles) " + plantaBajaAsientosAutobusReservar + " = "
                    + totalAsientosBajaDisponible);
            if (plantaAltaAsientosAutobusReservar > totalAsientosAltaDisponible) {
                // Sin asientos, los asientos de plata alta ya se agotaron
                System.err.println("Los asientos de plata alta ya se agotaron");
                if (totalAsientosPlantaBaja == 0) {
                    System.err.println("Alguno de los asientos seleccionados ya fue reservado");
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(tempAsientos);
                } else {
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(tempAsientos);
                }
            }
            if (plantaBajaAsientosAutobusReservar > totalAsientosBajaDisponible) {
                // Sin asientos, los asientos de plata baja ya se agotaron
                System.err.println("Los asientos de plata baja ya se agotaron");
                return ResponseEntity.status(HttpStatus.RESET_CONTENT).body(tempAsientos);
            }

        }
        // Ahora siii, esta ok reservar
        for (int i = 0; i < asientos.size(); i++) {
            // se crea el objeto temp
            AsientoNormalTemp asientoGuardado = new AsientoNormalTemp();
            //
            String[] as = asientos.get(i).split("%");
            int nAsiento = Integer.parseInt(as[0]);
            Long tipoBLong = Long.parseLong(as[1]);
            Long idTipoBoleto = tipoBLong;
            // se assignan boletos y planta
            if (tipoBLong == 4l) {
                idTipoBoleto = 1l;
                asientoGuardado.setTipoPlanta(1);
            } else if (tipoBLong == 5l) {
                idTipoBoleto = 2l;
                asientoGuardado.setTipoPlanta(1);
            } else if (tipoBLong == 6l) {
                idTipoBoleto = 3l;
                asientoGuardado.setTipoPlanta(1);
            } else {
                idTipoBoleto = tipoBLong;
                asientoGuardado.setTipoPlanta(2);
            }
            //
            TipoBoleto tipoBoleto = this.tipoBoletoRepository.findById(idTipoBoleto)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + tipoBLong));

            // se rellena el objeto temp
            asientoGuardado.setTerminalOrigen(origen);
            asientoGuardado.setTerminalDestino(destino);
            asientoGuardado.setRuta(Integer.parseInt(asientoNormalTemp.get("ruta").toString()));
            asientoGuardado.setNumeroAsiento(nAsiento);
            asientoGuardado.setFechaViaje(asientoNormalTemp.get("fechaViaje").toString());
            asientoGuardado.setTipoBoleto(tipoBoleto);
            asientoGuardado.setAutobus(Double.parseDouble(asientoNormalTemp.get("autobus").toString()));
            asientoGuardado.setFechaInsert(asientoNormalTemp.get("fechaInsert").toString());

            AsientoNormalTemp guardado = asientoTempNormalRepository.save(asientoGuardado);
            tempAsientos.add(guardado);
        }

        return ResponseEntity.ok(tempAsientos);
    }

    // ELIMINAR TABLA TEMPORAL TOUR
    @Module("CentralBus - Comprar Boleto/Eliminar Asiento Temporal 0_0")
    @DeleteMapping("/delete/{usuarioFinal}")
    @Transactional
    public ResponseEntity<String> eliminarPorId(@PathVariable Long usuarioFinal) {
        asientoTempNormalRepository.deleteByUsuarioFinal(usuarioFinal);
        return ResponseEntity.ok("Registro eliminado exitosamente");
    }

    // ELIMINAR TABLA TEMPORAL POR NUMERO DE ASIENTO
    @SuppressWarnings("unchecked")
    @Module("CentralBus - Comprar Boleto/Eliminar Asiento Temporal 0_0")
    @DeleteMapping("/eliminarTemporales")
    @Transactional
    public ResponseEntity<String> eliminarPorAsiento(@RequestBody JSONObject asientos) {

        List<Integer> asientosList = (List<Integer>) asientos.get("asientos");
        String fechaHora = asientos.get("fechaHora").toString();
        double autobus = (double) asientos.get("autobus");

        // System.out.println(asientosList.get(0));
        System.out.println(asientosList.toString());
        for (int i = 0; i < asientosList.size(); i++) {
            int numAsiento = asientosList.get(i);
            asientoTempNormalRepository.deleteByNumeroAsientoAndFechaViajeAndAutobus(numAsiento, fechaHora, autobus);
        }

        return ResponseEntity.ok("Registro eliminado exitosamente");
    }

}
