package com.gorigeek.springboot.controller;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.util.Date;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.gorigeek.crypto.Encrypt;

import com.gorigeek.springboot.entity.RutasDetalles;
import com.gorigeek.springboot.entity.Rutas;
import com.gorigeek.springboot.entity.Terminales;
import com.gorigeek.springboot.entity.Autobus;
import com.gorigeek.springboot.Module;
import com.gorigeek.springboot.distribution.utils.UtilDiscounts;
import com.gorigeek.springboot.entity.Afiliado;
import com.gorigeek.springboot.entity.Disponibilidad;
import com.gorigeek.springboot.entity.HorariosSalida;
import com.gorigeek.springboot.entity.RutaHasTransporte;
import com.gorigeek.springboot.entity.TipoTransporte;
import com.gorigeek.springboot.entity.TipoRuta;
import com.gorigeek.springboot.entity.DTO.AsientosOcupadosDTO;
import com.gorigeek.springboot.repository.RutaDetalleRepository;
import com.gorigeek.springboot.repository.RutaHasTransporteRepository;
import com.gorigeek.springboot.repository.TipoTransporteRepository;
import com.gorigeek.springboot.view.entity.AsientosOcupados;
import com.gorigeek.springboot.view.repository.AsientosOcupadosRepository;

@RestController
@RequestMapping("/api/detalleruta")
public class RutaDetalleController {
    @Autowired
    private RutaDetalleRepository repoDetRuta;
    @Autowired
    private RutaHasTransporteRepository repoRutaTransporte;

    @Autowired
    private TipoTransporteRepository tiptrRepo;

    @Autowired
    private AsientosOcupadosRepository asientosOcupadosRepo;

    @Autowired
    private AsientosController asientosController;

    @Autowired
    private UtilDiscounts utilDiscounts;

    @GetMapping
    public List<RutasDetalles> getAllRutaDetalle() {
        return this.repoDetRuta.findAll();
    }

    private final DataSource dataSource;

    public RutaDetalleController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/{id}")
    public Optional<RutasDetalles> getRutasDetallesById(@PathVariable("id") Long idDetalles)
            throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException {
        return this.repoDetRuta.findById(idDetalles);
    }
    /*
     * 
     * @GetMapping("/ruta/{id}")
     * public List<RutasDetalles> getRutaDetalleByRuta(@PathVariable ("id") String
     * idRuta) throws InvalidKeyException, UnsupportedEncodingException,
     * NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
     * BadPaddingException{
     * System.out.println("getRutasDetalles");
     * //List<RutasDetalles> lista =
     * this.repoDetRuta.findByIdRutas(Long.parseLong(Encrypt.desencriptar(idRuta.
     * replace("{*}","/"), "MODruta95")));
     * List<RutasDetalles> lista =
     * this.repoDetRuta.findAllByIdRutas(Long.parseLong(idRuta));
     * return lista;
     * }
     * 
     * // OBTENER DETALLE DE RUTA POR AUTOBUS Y AFILIADO
     * 
     * @GetMapping("ruta/autobus/{idAfiliado}/{idAutobus}")
     * public RutasDetalles getRutaDetalleByAutobus(@PathVariable ("idAfiliado")
     * Long idAfiliado, @PathVariable ("idAutobus") String idAutobus) throws
     * NumberFormatException, InvalidKeyException, UnsupportedEncodingException,
     * NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
     * BadPaddingException {
     * idAutobus = idAutobus.replace("{*}", "/");
     * Long val = Long.parseLong(Encrypt.desencriptarSinDiagonales(idAutobus,
     * "MAubus25"));
     * 
     * Afiliado afiliado= this.afiRepo.findById(idAfiliado)
     * .orElseThrow(() -> new
     * ResourceNotFoundException("Afiliado no encontrado con el Id: " +
     * idAfiliado));
     * RutasDetalles detalles = this.repoDetRuta.findByIdAutobusAndIdAfiliado(val,
     * afiliado);
     * if(detalles == null) {
     * return null;
     * }
     * return detalles;
     * }
     * 
     * @GetMapping("ruta/autobus/{idAutobus}")
     * public RutasDetalles getRutaDetallesByAutobus(@PathVariable("idAutobus")
     * String idAutobus) throws NumberFormatException, InvalidKeyException,
     * UnsupportedEncodingException, NoSuchAlgorithmException,
     * NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
     * idAutobus = idAutobus.replace("{*}", "/");
     * Long val = Long.parseLong(Encrypt.desencriptarSinDiagonales(idAutobus,
     * "MAubus25"));
     * return this.repoDetRuta.findByIdAutobus(val);
     * }
     * 
     * 
     * @PostMapping
     * public String saveRutaDetalle(@RequestBody RutasDetalles datos) throws
     * InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException,
     * NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
     * datos.setIdTerminalDestino(repoTerm.findById(datos.getIdTerminalDestino().
     * getIdt_terminales()).get());
     * datos.setIdTerminalOrigen(repoTerm.findById(datos.getIdTerminalOrigen().
     * getIdt_terminales()).get());
     * datos.setFechaCreacion();
     * datos.setIdAfiliado(afiRepo.findById(datos.getIdAfiliado().getIdt_afiliado())
     * .get());
     * return this.repoDetRuta.save(datos).getIdt_detalleruta();
     * }
     * 
     * 
     * @PutMapping
     * public String updateRutaDetalle(@RequestBody RutasDetalles datos) throws
     * Exception {
     * RutasDetalles datos2 =
     * repoDetRuta.findById(Long.parseLong(this.getValDecrypt(datos.
     * getIdt_detalleruta()))).get();
     * //RutasDetalles datos2 =
     * repoDetRuta.findById(Long.parseLong(datos.getIdt_detalleruta())).get();
     * //Terminales ter =
     * repoTerm.findById(datos.getIdTerminalDestino().getIdt_terminales()).get();
     * datos.setIdTerminalDestino(repoTerm.findById(datos.getIdTerminalDestino().
     * getIdt_terminales()).get());
     * //ter =
     * repoTerm.findById(datos.getIdTerminalOrigen().getIdt_terminales()).get();
     * datos.setIdTerminalOrigen(repoTerm.findById(datos.getIdTerminalOrigen().
     * getIdt_terminales()).get());
     * datos.setIdAfiliado(afiRepo.findById(datos.getIdAfiliado().getIdt_afiliado())
     * .get());
     * datos.setFechaCreacion(datos2.getFechaCreacion());
     * System.out.println("updateRutasDetalles");
     * return this.repoDetRuta.save(datos).getIdt_detalleruta();
     * }
     */

    /*
     * @GetMapping("/costo")
     * public List<Costo> getAllCostos(){
     * return costoRepo.findAll();
     * }
     */

    /*
     * @GetMapping("costo/detalle/{id}")
     * public List<Costo> getCostoByIdtRutadetalles(@PathVariable ("id") Long
     * idDetalles) throws InvalidKeyException, UnsupportedEncodingException,
     * NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
     * BadPaddingException {
     * RutasDetalles detalleruta = repoDetRuta.findById(idDetalles).get();
     * return this.costoRepo.findByIdDetalleRuta(detalleruta);
     * }
     */

    /*
     * @DeleteMapping("/costo/{id}")
     * public long deleteCostoByDetalle(@PathVariable ("id") Long idDetalles) throws
     * InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException,
     * NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
     * RutasDetalles detalleruta = repoDetRuta.findById(idDetalles).get();
     * return this.costoRepo.deleteByIdDetalleRuta(detalleruta);
     * }
     */

    /*
     * @DeleteMapping
     * public String deleteRutDetalle(@RequestBody RutasDetalles datos) throws
     * InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException,
     * NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
     * this.repoDetRuta.deleteById(Long.parseLong(this.getValDecrypt(datos.
     * getIdt_detalleruta())));
     * return "ok";
     * }
     */
    /*
     * @DeleteMapping("/{id}")
     * 
     * @ResponseBody
     * public String DeleteRutasDetallesById(@PathVariable ("id") Long idDetalles)
     * throws InvalidKeyException, UnsupportedEncodingException,
     * NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
     * BadPaddingException {
     * RutasDetalles detalles = this.repoDetRuta.findById(idDetalles).get();
     * System.out.println(detalles.getIdt_detalleruta());
     * this.repoDetRuta.eliminarById(idDetalles);
     * return "ok";
     * }
     */

    public String getValDecrypt(String val) throws InvalidKeyException, UnsupportedEncodingException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        val = val.replace("{*}", "/");
        return Encrypt.desencriptar(val, "MODrutadet95");
    }

    @GetMapping("/tipoTransporte")
    public List<TipoTransporte> getTipoTransporte() {
        return tiptrRepo.findAll();
    }
    /*
     * @PostMapping("/addCostos")
     * public List<Costo> saveCostos(@RequestBody List<Costo> datos) throws
     * InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException,
     * NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
     * return this.costoRepo.saveAll(datos);
     * }
     */

    /*
     * @PutMapping("/updateCostos")
     * public List<Costo> updateCostos(@RequestBody List<Costo> datos) throws
     * Exception{
     * 
     * for (int i = 0; i < datos.size(); i++) {
     * if (datos.get(i).getPrecioAdulto() == 0 && datos.get(i).getPrecioNino() == 0
     * && datos.get(i).getPrecioInapam() == 0 &&
     * datos.get(i).getIdTipoTransporte().getIdTipoTransporte() == null) {
     * costoRepo.deleteById(datos.get(i).getIdCosto());
     * datos.remove(i);
     * i--;
     * } else {
     * TipoTransporte tt =
     * tiptrRepo.findById(datos.get(i).getIdTipoTransporte().getIdTipoTransporte()).
     * get();
     * RutasDetalles idd =
     * repoDetRuta.findById(Long.parseLong(this.getValDecrypt(datos.get(i).
     * getIdDetalleRuta().getIdt_detalleruta()))).get();
     * datos.get(i).setIdTipoTransporte(tt);
     * datos.get(i).setIdDetalleRuta(idd);
     * }
     * 
     * }
     * return this.costoRepo.saveAll(datos);
     * }
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)   
    @Module("CentralBus - Comprar Boleto/Obtener Rutas")
    @GetMapping("/multi-allterminales")
    public ResponseEntity<List<List<RutaHasTransporte>>> getAllTerminalesWithRutas(
            @RequestParam(name = "departureStations", required = true) String[] departureStations,
            @RequestParam(name = "arrivalStations", required = true) String[] arrivalStations,
            @RequestParam(name = "departureDate", required = true) String departureDate,
            @RequestParam(name = "returnDate", required = false) String returnDate,
            @RequestParam(name = "hourWithMinutes", required = true) String hourWithMinutes,
            @RequestParam(name = "numPassengers", required = true) Integer numPassengers) {
        try {
            // Obtener rutas
            List<RutaHasTransporte> rutasIda = getRutas(departureStations, arrivalStations);
            List<RutaHasTransporte> rutasVuelta = new ArrayList<>();
            if (returnDate != null) {
                rutasVuelta = getRutas(arrivalStations, departureStations);
            }
            // Se obtienenAsientosOcupados de todos los viajes a partir de hoy
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fechaActual = LocalDate.now();
            String fechaActualFormateada = fechaActual.format(formatter);
            List<AsientosOcupados> asientosOcupados = asientosOcupadosRepo
                    .findAsientosOcupadosFromTodayOnwards(fechaActualFormateada);

            List<List<RutaHasTransporte>> combinedResponses = new ArrayList<>();
            for (String departureStation : departureStations) {
                for (String arrivalStation : arrivalStations) {
                    List<RutaHasTransporte> tempRutasIda = new ArrayList<>();
                    // Se hace el filter para solo mandar las rutas de las estaciones con id,
                    for (RutaHasTransporte ida : rutasIda) {
                        if (ida.getDetalleRuta().getIdTerminalOrigen().getIdt_terminales() == Long
                                .parseLong(departureStation) &&
                                ida.getDetalleRuta().getIdTerminalDestino().getIdt_terminales() == Long
                                        .parseLong(arrivalStation)) {
                            tempRutasIda.add(ida);

                        }
                    }

                    List<RutaHasTransporte> requestDeparture = getAllRutasInfo(departureStation,
                            arrivalStation, hourWithMinutes, departureDate, numPassengers, rutasIda, asientosOcupados);
                    if (!requestDeparture.isEmpty()) {
                        if (returnDate != null) {
                            // Se vuelve a hacer el filter "VUELTA"
                            List<RutaHasTransporte> tempRutasVuelta = new ArrayList<>();
                            for (RutaHasTransporte vuelta : rutasVuelta) {
                                if (vuelta.getDetalleRuta().getIdTerminalOrigen().getIdt_terminales() == Long
                                        .parseLong(arrivalStation) &&
                                        vuelta.getDetalleRuta().getIdTerminalDestino().getIdt_terminales() == Long
                                                .parseLong(departureStation)) {
                                    tempRutasVuelta.add(vuelta);

                                }
                            }

                            List<RutaHasTransporte> requestReturn = getAllRutasInfo(arrivalStation,
                                    departureStation, hourWithMinutes, returnDate, numPassengers, tempRutasVuelta,
                                    asientosOcupados);

                            if (!requestReturn.isEmpty()) {
                                requestDeparture.addAll(requestReturn);
                                combinedResponses.add(requestDeparture);
                            }
                        } else {
                            combinedResponses.add(requestDeparture);
                        }
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(combinedResponses);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public List<RutaHasTransporte> getRutas(String[] idTer1, String[] idTer2) {
        List<RutaHasTransporte> gettAllInfo = new ArrayList<>();
        // Placeholders para el IN
        String placeholders1 = String.join(",", Collections.nCopies(idTer1.length, "?"));
        String placeholders2 = String.join(",", Collections.nCopies(idTer2.length, "?"));

        String query = "SELECT "
                + "ruta_has_transporte.id_ruta_has_transporte, "
                + "ruta_has_transporte.t_autobus, "
                + "ruta_has_transporte.t_detalle_ruta, "
                + "ruta_has_transporte.adulto, "
                + "ruta_has_transporte.nino, "
                + "ruta_has_transporte.inapam, "
                + "ruta_has_transporte.t_horarios_salida, "
                + "ruta_has_transporte.adultoPB, "
                + "ruta_has_transporte.ninoPB, "
                + "ruta_has_transporte.inapamPB, "
                + "t_detalle_ruta.idt_detalle_ruta, "
                + "t_detalle_ruta.t_ruta, "
                + "t_detalle_ruta.t_terminales_origen, "
                + "t_detalle_ruta.t_terminales_destino, "
                + "t_detalle_ruta.secuencia, "
                + "t_detalle_ruta.tiempo_ruta, "
                + "t_autobus.idt_autobus, "
                + "t_autobus.modelo, "
                + "t_autobus.numero_autobus, "
                + "t_autobus.marca, "
                + "t_autobus.placas, "
                + "t_autobus.c_tipotransporte, "
                + "t_autobus.sanitario, "
                + "t_autobus.asientos_adultos, "
                + "t_autobus.asientos_ninios, "
                + "t_autobus.asientos_inapam, "
                + "t_autobus.t_afiliado, "
                + "t_autobus.c_disponibilidad, "
                + "t_autobus.estatus_seleccionAsientos, "
                + "t_horarios_salida.hora_salida, "
                + "t_ruta.fecha_periodo, "
                + "t_ruta.c_tiporuta, "
                + "t_ruta.dias_anticipacion, "
                + "t_ruta.c_status_ruta, "
                + "t_ruta.porcentaje_descuento, "
                + "t_ruta.dias_periodo, "
                + "t_afiliado.c_statuscuenta_activo, "
                + "t_afiliado.mostrar_direccion, "
                + "t_afiliado.nombre_linea, "
                + "t_afiliado.logotipo, "
                + "c_tipotransporte.icono, "
                + "origin.direccion AS origin_address, "
                + "destiny.direccion AS destination_address "
                + "FROM ruta_has_transporte "
                + "INNER JOIN t_detalle_ruta ON t_detalle_ruta.idt_detalle_ruta = ruta_has_transporte.t_detalle_ruta "
                + "INNER JOIN t_autobus ON t_autobus.idt_autobus = ruta_has_transporte.t_autobus "
                + "INNER JOIN t_horarios_salida ON t_horarios_salida.idt_horarios_salida = ruta_has_transporte.t_horarios_salida "
                + "INNER JOIN t_ruta ON t_ruta.idt_ruta = t_detalle_ruta.t_ruta "
                + "INNER JOIN t_afiliado ON t_afiliado.idt_afiliado = t_ruta.t_afiliado "
                + "INNER JOIN c_tipotransporte ON c_tipotransporte.idc_tipotransporte = t_autobus.c_tipotransporte "
                + "INNER JOIN t_terminales AS origin ON origin.idt_terminales = t_detalle_ruta.t_terminales_origen "
                + "INNER JOIN t_terminales AS destiny ON destiny.idt_terminales = t_detalle_ruta.t_terminales_destino "
                + "WHERE ruta_has_transporte.t_autobus IN ("
                + "SELECT DISTINCT ruta_has_transporte.t_autobus "
                + "FROM t_detalle_ruta AS busdes "
                + "INNER JOIN ruta_has_transporte ON ruta_has_transporte.t_detalle_ruta = busdes.idt_detalle_ruta "
                + "INNER JOIN ("
                + "SELECT DISTINCT t_autobus "
                + "FROM t_detalle_ruta AS busorg "
                + "INNER JOIN ruta_has_transporte ON ruta_has_transporte.t_detalle_ruta = busorg.idt_detalle_ruta "
                + "WHERE busorg.t_terminales_origen IN (" + placeholders1 + ")) coso "
                + "ON coso.t_autobus = ruta_has_transporte.t_autobus "
                + "WHERE busdes.t_terminales_destino IN (" + placeholders2 + "))";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            int index = 1;
            for (String id : idTer1) {
                pstmt.setLong(index++, Long.parseLong(id));
            }
            for (String id : idTer2) {
                pstmt.setLong(index++, Long.parseLong(id));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    RutaHasTransporte ruta = new RutaHasTransporte();

                    // ruta_has_transporte
                    ruta.setIdRutaHasTransporte(rs.getLong("id_ruta_has_transporte"));
                    ruta.setAdulto(rs.getString("adulto"));
                    ruta.setNino(rs.getString("nino"));
                    ruta.setInapam(rs.getString("inapam"));
                    ruta.setAdultoPB(rs.getString("adultoPB"));
                    ruta.setNinoPB(rs.getString("ninoPB"));
                    ruta.setInapamPB(rs.getString("inapamPB"));

                    // Horario salida
                    HorariosSalida tempHorarioSalida = new HorariosSalida();
                    tempHorarioSalida.setIdHorariosSalida(rs.getLong("t_horarios_salida"));
                    tempHorarioSalida.setHoraSalida(rs.getString("hora_salida"));
                    ruta.setHorariosSalida(tempHorarioSalida);
                    // Map t_autobus fields
                    Autobus tempAutobus = new Autobus();
                    tempAutobus.setIdt_autobus(rs.getLong("idt_autobus"));
                    tempAutobus.setModelo(rs.getString("modelo"));
                    tempAutobus.setNumeroAutobus(rs.getString("numero_autobus"));
                    tempAutobus.setMarca(rs.getString("marca"));
                    tempAutobus.setSanitario(rs.getInt("sanitario"));
                    tempAutobus.setPlacas(rs.getString("placas"));
                    TipoTransporte tempTipoTransporte = new TipoTransporte();
                    tempTipoTransporte.setIdTipoTransporte(rs.getLong("c_tipotransporte"));
                    tempTipoTransporte.setIcono(rs.getString("icono"));
                    tempAutobus.setTipoTransporte(tempTipoTransporte);
                    tempAutobus.setSanitario(rs.getInt("sanitario"));
                    tempAutobus.setAsientosAdultos(rs.getInt("asientos_adultos"));
                    tempAutobus.setAsientosNinios(rs.getInt("asientos_ninios"));
                    tempAutobus.setAsientosInapam(rs.getInt("asientos_inapam"));
                    Afiliado tempAfiliado = new Afiliado();
                    tempAfiliado.setIdt_afiliado(rs.getLong("t_afiliado"));
                    tempAfiliado.setCStatuscuentaActivo(rs.getInt("c_statuscuenta_activo"));
                    tempAfiliado.setLogotipo(rs.getString("logotipo"));
                    tempAfiliado.setNombreLinea(rs.getString("nombre_linea"));
                    tempAfiliado.setMostrarDireccion(rs.getInt("mostrar_direccion"));
                    tempAutobus.setAfiliado(tempAfiliado);
                    Disponibilidad tempDisponibilidad = new Disponibilidad();
                    tempDisponibilidad.setIdc_disponibilidad(rs.getLong("c_disponibilidad"));
                    tempAutobus.setDisponibilidad(tempDisponibilidad);
                    tempAutobus.setSeleccionAsientos(rs.getInt("estatus_seleccionAsientos"));
                    ruta.setIdAutobus(tempAutobus);

                    // Map t_detalle_ruta fields
                    RutasDetalles tempDetalles = new RutasDetalles();
                    tempDetalles.setIdt_detalle_ruta(rs.getLong("idt_detalle_ruta"));
                    Rutas tempRuta = new Rutas();
                    tempRuta.setIdt_ruta(rs.getLong("t_ruta"));
                    tempRuta.setDiasPeriodo(rs.getString("dias_periodo"));
                    tempRuta.setFechaPeriodo(rs.getString("fecha_periodo"));
                    tempRuta.setStatusRuta(rs.getString("c_status_ruta"));
                    tempRuta.setPorcentajeDescuento(rs.getString("porcentaje_descuento"));
                    tempRuta.setT_afiliado(tempAfiliado);
                    tempRuta.setDiasAnticipacion(Integer.parseInt(rs.getString("dias_anticipacion")));
                    TipoRuta tempTipoRuta = new TipoRuta();
                    tempTipoRuta.setIdc_tiporuta(rs.getLong("c_tiporuta"));
                    tempRuta.setId_tipoRuta(tempTipoRuta);
                    tempDetalles.setT_ruta(tempRuta);
                    Terminales tempTerminalOrigen = new Terminales();
                    tempTerminalOrigen.setIdt_terminales(rs.getLong("t_terminales_origen"));
                    tempTerminalOrigen.setDireccion(rs.getString("origin_address"));
                    Terminales tempTerminalDestino = new Terminales();
                    tempTerminalDestino.setIdt_terminales(rs.getLong("t_terminales_destino"));
                    tempTerminalDestino.setDireccion(rs.getString("destination_address"));
                    tempDetalles.setIdTerminalOrigen(tempTerminalOrigen);
                    tempDetalles.setIdTerminalDestino(tempTerminalDestino);
                    tempDetalles.setSecuencia(rs.getString("secuencia"));
                    tempDetalles.setTiempoRuta(rs.getString("tiempo_ruta"));
                    ruta.setDetalleRuta(tempDetalles);

                    gettAllInfo.add(ruta);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Imprimir el tiempo transcurrido
        return gettAllInfo;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)   
    @GetMapping("/allterminales/{idO}/{idD}/{hour}/{date}/{numPassengers}")
    public List<RutaHasTransporte> getAllRutasInfo(@PathVariable("idO") String idTer1,
            @PathVariable("idD") String idTer2, @PathVariable("hour") String hour, @PathVariable("date") String date,
            @PathVariable("numPassengers") Integer numPassengers, List<RutaHasTransporte> allRutas,
            List<AsientosOcupados> asientosOcupados)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, IOException, ParseException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Imprimir los valores recibidos
//        System.out.println("Viaja de terminal: " + idTer1 + " a terminal: " + idTer2);

        // Se convierte la fecha solicitada en una letra
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String[] dateparts = date.split("-");
        String startDateString = dateparts[1] + "/" + dateparts[2] + "/" + dateparts[0];
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(df.parse(startDateString));
        // prints day name
        switch (str) {
            case "lunes": {
                str = "L";
                break;
            }
            case "martes": {
                str = "M";
                break;
            }
            case "miércoles": {
                str = "Mi";
                break;
            }
            case "jueves": {
                str = "J";
                break;
            }
            case "viernes": {
                str = "V";
                break;
            }
            case "sábado": {
                str = "S";
                break;
            }
            case "domingo": {
                str = "D";
                break;
            }
            default: {
                str = "L";
                break;
            }
        }

        // Fecha de hoy en el servidor
        LocalDate dateObj = LocalDate.now();
        String fecha = dateObj.format(formatter);
        // System.out.println(fecha);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date firstDate = sdf.parse(date);
        Date secondDate = sdf.parse(fecha);
        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        // Se busca en la base de datos
//        final List<RutaHasTransporte> gettAllInfo = this.repoRutaTransporte.getAllRutasTrans(Long.parseLong(idTer1),
//                Long.parseLong(idTer2));      
        List<RutaHasTransporte> gettAllInfoResp = new ArrayList<RutaHasTransporte>();

//        System.out.println("Total de rutas encontradas: " + allRutas.size());

        for (int x = 0; x < allRutas.size(); x++) {
            RutaHasTransporte onlyOneInfoTemp1 = new RutaHasTransporte(allRutas.get(x));
            gettAllInfoResp.add(onlyOneInfoTemp1);
        }

        // Unificación del trayecto
        List<RutaHasTransporte> todaInfoTran = new ArrayList<RutaHasTransporte>();
        for (int i = 0; i < allRutas.size(); i++) {
            if (allRutas.get(i).getDetalleRuta().getIdTerminalOrigen().getIdt_terminales() == Long.parseLong(idTer1)
                    && allRutas.get(i).getDetalleRuta().getIdTerminalDestino().getIdt_terminales() == Long
                            .parseLong(idTer2)) {
                todaInfoTran.add(gettAllInfoResp.get(i));
            } else {
                RutaHasTransporte onlyOneInfoTemp = new RutaHasTransporte(allRutas.get(i));
                if (allRutas.get(i).getIdRutaHasTransporte() == 0) {
                    // System.out.println("Naranjas");l
                } else {
                    if (onlyOneInfoTemp.getDetalleRuta().getIdTerminalOrigen().getIdt_terminales() == Long
                            .parseLong(idTer1)) {
                        for (int w = i + 1; w < allRutas.size(); w++) {
                            int sumaMin = 0;
                            int sumaHrs = 0;
                            if (onlyOneInfoTemp.getDetalleRuta().getT_ruta().getIdt_ruta() == allRutas.get(w)
                                    .getDetalleRuta().getT_ruta().getIdt_ruta()) {
                                if (onlyOneInfoTemp.getIdAutobus().getIdt_autobus() == allRutas.get(w).getIdAutobus()
                                        .getIdt_autobus()) {
                                    if (Integer.parseInt(onlyOneInfoTemp.getDetalleRuta().getSecuencia()) + 1 == Integer
                                            .parseInt(allRutas.get(w).getDetalleRuta().getSecuencia())) {

                                        RutasDetalles rutaDetalleTemporal = new RutasDetalles(
                                                onlyOneInfoTemp.getDetalleRuta());
                                        // rutaDetalleTemporal = onlyOneInfoTemp.getDetalleRuta();
                                        rutaDetalleTemporal.setIdTerminalDestino(
                                                allRutas.get(w).getDetalleRuta().getIdTerminalDestino());
                                        // System.out.println("Este entró ^^");
                                        String[] tiempoTotalViaje = onlyOneInfoTemp.getDetalleRuta().getTiempoRuta()
                                                .split(":");
                                        String[] tiempoViajeTemp = allRutas.get(w).getDetalleRuta().getTiempoRuta()
                                                .split(":");
                                        sumaMin = Integer.parseInt(tiempoTotalViaje[1])
                                                + Integer.parseInt(tiempoViajeTemp[1]);
                                        sumaHrs = Integer.parseInt(tiempoTotalViaje[0])
                                                + Integer.parseInt(tiempoViajeTemp[0]);
                                        if (sumaMin >= 60) {
                                            sumaMin -= 60;
                                            sumaHrs++;
                                        }
                                        rutaDetalleTemporal.setTiempoRuta(sumaHrs + ":" + sumaMin + ":00");
                                        int sumsec = Integer.parseInt(onlyOneInfoTemp.getDetalleRuta().getSecuencia())
                                                + 1;
                                        rutaDetalleTemporal.setSecuencia(sumsec + "");
                                        onlyOneInfoTemp
                                                .setIdRutaHasTransporte(allRutas.get(w).getIdRutaHasTransporte());
                                        onlyOneInfoTemp.setIdAutobus(allRutas.get(w).getIdAutobus());
                                        onlyOneInfoTemp.setDetalleRuta(rutaDetalleTemporal);
                                        onlyOneInfoTemp.setAdulto(Integer.parseInt(onlyOneInfoTemp.getAdulto())
                                                + Integer.parseInt(allRutas.get(w).getAdulto()) + "");
                                        onlyOneInfoTemp.setNino(Integer.parseInt(onlyOneInfoTemp.getNino())
                                                + Integer.parseInt(allRutas.get(w).getNino()) + "");
                                        onlyOneInfoTemp.setInapam(Integer.parseInt(onlyOneInfoTemp.getInapam())
                                                + Integer.parseInt(allRutas.get(w).getInapam()) + "");
//                                        System.out.println("prueba suma planta baja: ");

                                        // pruebas costos planta baja

                                        onlyOneInfoTemp.setAdultoPB(Integer.parseInt(onlyOneInfoTemp.getAdultoPB())
                                                + Integer.parseInt(allRutas.get(w).getAdultoPB()) + "");
//                                        System.out.println("prueba suma adultoPB: " + onlyOneInfoTemp.getInapamPB());
                                        onlyOneInfoTemp.setNinoPB(Integer.parseInt(onlyOneInfoTemp.getNinoPB())
                                                + Integer.parseInt(allRutas.get(w).getNinoPB()) + "");
                                        onlyOneInfoTemp.setInapamPB(Integer.parseInt(onlyOneInfoTemp.getInapamPB())
                                                + Integer.parseInt(allRutas.get(w).getInapamPB()) + "");
                                        // fin prueba costos planta baja
//                                        System.out.println("prueba suma planta baja fin ");
                                        // onlyOneInfoTemp.setHorariosSalida(gettAllInfo.get(w).getHorariosSalida());
                                        allRutas.get(w).setIdRutaHasTransporte(Long.valueOf(0));
                                    }
                                }
                            }
                            if (onlyOneInfoTemp.getDetalleRuta().getIdTerminalOrigen().getIdt_terminales() == Long
                                    .parseLong(idTer1)
                                    && onlyOneInfoTemp.getDetalleRuta().getIdTerminalDestino()
                                            .getIdt_terminales() == Long.parseLong(idTer2)) {
                                todaInfoTran.add(onlyOneInfoTemp);
                                w = 999;
                            }
                        }
                    }
                }
            }
        }
        // Se realiza una lista con los filtros del día/fecha/hora
        List<RutaHasTransporte> todaInfoFiltrada = new ArrayList<RutaHasTransporte>();
        for (int y = 0; y < todaInfoTran.size(); y++) {
            RutaHasTransporte onlyOneTodaInfoFiltrada = new RutaHasTransporte(todaInfoTran.get(y));
            // Revisa rutas que son por día o fecha
            if (todaInfoTran.get(y).getDetalleRuta().getT_ruta().getId_tipoRuta().getIdc_tiporuta() == 2) {
                // Si la ruta funciona por días de la semana
                if (todaInfoTran.get(y).getDetalleRuta().getT_ruta().getDiasPeriodo() != null) {
                    String[] diasSplit = todaInfoTran.get(y).getDetalleRuta().getT_ruta().getDiasPeriodo().split(",");
                    for (int z = 0; z < diasSplit.length; z++) {
                        if (str.equals(diasSplit[z])) {
                            if (fecha.equals(date)) {
                                String[] parts1 = todaInfoTran.get(y).getHorariosSalida().getHoraSalida().split(":");
                                String[] parts2 = hour.split(":");
                                // System.out.println(Integer.parseInt(parts2[0]) +" debe ser menor o igual a "
                                // + Integer.parseInt(parts1[0]));
                                if (Integer.parseInt(parts2[0]) < Integer.parseInt(parts1[0])) {
                                    todaInfoFiltrada.add(onlyOneTodaInfoFiltrada);
                                } else {
                                    if (Integer.parseInt(parts2[0]) == Integer.parseInt(parts1[0])) {
                                        // System.out.println(Integer.parseInt(parts2[1]) +" debe ser menor o igual a "
                                        // + Integer.parseInt(parts1[1]));
                                        if (Integer.parseInt(parts2[1]) < Integer.parseInt(parts1[1])) {
                                            todaInfoFiltrada.add(onlyOneTodaInfoFiltrada);
                                        }
                                    }
                                }
                            } else {
                                todaInfoFiltrada.add(onlyOneTodaInfoFiltrada);
                            }
                        }
                    }
                }
                // Si la ruta funciona por fechas
                if (todaInfoTran.get(y).getDetalleRuta().getT_ruta().getFechaPeriodo() != null) {
                    // System.out.println("Esto de acá
                    // "+todaInfoTran.get(y).getDetalleRuta().getT_ruta().getFechaPeriodo() +"
                    // debería contener esto " + dateparts[2]);
                    if (todaInfoTran.get(y).getDetalleRuta().getT_ruta().getFechaPeriodo().contains(dateparts[2])) {
                        if (fecha.equals(date)) {
                            String[] parts1 = todaInfoTran.get(y).getHorariosSalida().getHoraSalida().split(":");
                            String[] parts2 = hour.split(":");
                            // System.out.println(Integer.parseInt(parts2[0]) +" debe ser menor o igual a "
                            // + Integer.parseInt(parts1[0]));
                            if (Integer.parseInt(parts2[0]) < Integer.parseInt(parts1[0])) {
                                todaInfoFiltrada.add(onlyOneTodaInfoFiltrada);
                            } else {
                                if (Integer.parseInt(parts2[0]) == Integer.parseInt(parts1[0])) {
                                    // System.out.println(Integer.parseInt(parts2[1]) +" debe ser menor o igual a "
                                    // + Integer.parseInt(parts1[1]));
                                    if (Integer.parseInt(parts2[1]) < Integer.parseInt(parts1[1])) {
                                        todaInfoFiltrada.add(onlyOneTodaInfoFiltrada);
                                    }
                                }
                            }
                        } else {
                            todaInfoFiltrada.add(onlyOneTodaInfoFiltrada);
                        }
                    }
                }
            }
            // Revisa rutas diarias
            else {
                // Revisa si la ruta es hoy
                if (fecha.equals(date)) {
                    String[] parts1 = todaInfoTran.get(y).getHorariosSalida().getHoraSalida().split(":");
                    String[] parts2 = hour.split(":");
                    // Revisa si la hora de la ruta ya pasó
                    // System.out.println(Integer.parseInt(parts2[0]) +" debe ser menor o igual a "
                    // + Integer.parseInt(parts1[0]));
                    if (Integer.parseInt(parts2[0]) < Integer.parseInt(parts1[0])) {
                        todaInfoFiltrada.add(onlyOneTodaInfoFiltrada);
                    } else {
                        if (Integer.parseInt(parts2[0]) == Integer.parseInt(parts1[0])) {
                            // System.out.println(Integer.parseInt(parts2[1]) +" debe ser menor o igual a "
                            // + Integer.parseInt(parts1[1]));
                            if (Integer.parseInt(parts2[1]) < Integer.parseInt(parts1[1])) {
                                todaInfoFiltrada.add(onlyOneTodaInfoFiltrada);
                            }
                        }
                    }
                }
                // Si no es hoy se agrega a la lista
                else {
                    todaInfoFiltrada.add(onlyOneTodaInfoFiltrada);
                }
            }
        }

        List<RutaHasTransporte> todaInfoFiltradaPorStatus = new ArrayList<RutaHasTransporte>();
        for (int x = 0; x < todaInfoFiltrada.size(); x++) {
            RutaHasTransporte onlyOneTodaInfoFiltradaPorStatus = new RutaHasTransporte(todaInfoFiltrada.get(x));

            if (onlyOneTodaInfoFiltradaPorStatus.getDetalleRuta().getT_ruta().getStatusRuta() != null) {
                if ("1".equals(
                        onlyOneTodaInfoFiltradaPorStatus.getDetalleRuta().getT_ruta().getStatusRuta().toString())
                        && onlyOneTodaInfoFiltradaPorStatus.getDetalleRuta().getT_ruta().getT_afiliado()
                                .getCStatuscuentaActivo() == 1) {
                    onlyOneTodaInfoFiltradaPorStatus.setOrigen(idTer1);
                    onlyOneTodaInfoFiltradaPorStatus.setDestino(idTer2);
                    onlyOneTodaInfoFiltradaPorStatus.setFecha(date);

//                  Comprobar cantidad de asiento ocupados                    
                    String fechaL = onlyOneTodaInfoFiltradaPorStatus.getFecha();
                    String horaL = onlyOneTodaInfoFiltradaPorStatus.getHorariosSalida().getHoraSalida();
                    Long idRutaL = onlyOneTodaInfoFiltradaPorStatus.getDetalleRuta().getT_ruta().getIdt_ruta();

                    Integer numAsientosOcupados = 0;
                    AsientosOcupados viajeSeleccionado = null;
                    if (!asientosOcupados.isEmpty()) {
                        for (AsientosOcupados asiento : asientosOcupados) {
                            if (asiento.getFechaViaje().equals(fechaL) && asiento.getHoraViaje().equals(horaL)
                                    && idRutaL == asiento.getIdRuta()) {
                                numAsientosOcupados += asiento.getTotalAsientos();
                                viajeSeleccionado = asiento;
                            } else if (viajeSeleccionado != null
                                    && viajeSeleccionado.getSecuencia() < asiento.getSecuencia()
                                    && viajeSeleccionado.getIdRuta() == asiento.getIdRuta()) {
                                numAsientosOcupados += asiento.getTotalAsientos();
                            } else {
                                viajeSeleccionado = null;
                            }
                        }
                    }
                    // Suma de asientos por tipo de pasajero para obtener el total de asientos del
                    // autobus :)
                    int totalAsientosAutobus = onlyOneTodaInfoFiltradaPorStatus.getIdAutobus().getAsientosNinios() +
                            onlyOneTodaInfoFiltradaPorStatus.getIdAutobus().getAsientosAdultos()
                            + onlyOneTodaInfoFiltradaPorStatus.getIdAutobus().getAsientosInapam();
                    // Se comprueba si aun hay disponibilidad
                    if ((totalAsientosAutobus - numAsientosOcupados) >= numPassengers) {
                        // Se comprueba si es valido los dias de anticipacion
                        if (diff >= onlyOneTodaInfoFiltradaPorStatus.getDetalleRuta().getT_ruta().getDiasAnticipacion()
                                && onlyOneTodaInfoFiltradaPorStatus.getDetalleRuta().getT_ruta()
                                        .getDiasAnticipacion() != 0
                                && !onlyOneTodaInfoFiltradaPorStatus.getDetalleRuta().getT_ruta()
                                        .getPorcentajeDescuento().isEmpty()
                                && Integer.parseInt(onlyOneTodaInfoFiltradaPorStatus.getDetalleRuta().getT_ruta()
                                        .getPorcentajeDescuento()) > 0) {
                            onlyOneTodaInfoFiltradaPorStatus.setDiscountActivated(true);
                        } else {
                            onlyOneTodaInfoFiltradaPorStatus.setDiscountActivated(false);
                        }
                        // PROMO99 Verifica si aun hay disponibilidad de asientos con descuento (Precio
                        // base: 99)
                        String lineaPromo = onlyOneTodaInfoFiltradaPorStatus.getDetalleRuta().getT_ruta()
                                .getT_afiliado().getIdt_afiliado().toString();
                        String origenPromo = onlyOneTodaInfoFiltradaPorStatus.getDetalleRuta().getIdTerminalOrigen().getIdt_terminales()
                                .toString();
                        String destinoPromo = onlyOneTodaInfoFiltradaPorStatus.getDetalleRuta().getIdTerminalDestino().getIdt_terminales()
                                .toString();
                        String horaSalidaPromo = onlyOneTodaInfoFiltradaPorStatus.getHorariosSalida().getHoraSalida();
                        String fechaSalidaPromo = onlyOneTodaInfoFiltradaPorStatus.getFecha();
                        String[] horaSalidaPromoParts = horaSalidaPromo.split(":");
                        String fechaHoraFormateada = fechaSalidaPromo + "T" + horaSalidaPromoParts[0]+":"+horaSalidaPromoParts[1];
//                        System.err.println("linea = " + lineaPromo + " - origenPromo: " + origenPromo
//                                + " - destinoPromo: " + destinoPromo + " - fechaSalidaPromo: " + fechaHoraFormateada);
                        int asientosConDescuentoDisponibles = utilDiscounts.isValidFor99Promotion(lineaPromo, origenPromo, destinoPromo, fechaHoraFormateada);
//                        System.err.println("Cb, cantidad de asientos con descuento " + asientosConDescuentoDisponibles);
                        if(asientosConDescuentoDisponibles > 0) {
                            onlyOneTodaInfoFiltradaPorStatus.setAvailableSeatsDiscount(asientosConDescuentoDisponibles);
                            onlyOneTodaInfoFiltradaPorStatus.setAlternativePrice(99.00);
                        }
                        todaInfoFiltradaPorStatus.add(onlyOneTodaInfoFiltradaPorStatus);
                    } else {
                        System.err.println("Total asientos ocupados = " + numAsientosOcupados);
                        System.err.println("Total asientos autobus = " + totalAsientosAutobus);
                        System.err.println("Ruta = " + idRutaL + " Fecha = " + fechaL + " Hora = " + horaL
                                + " SIN DISPONIBILIDAD!");
                    }

                }
            }
        }
        return todaInfoFiltradaPorStatus;
    }
}
