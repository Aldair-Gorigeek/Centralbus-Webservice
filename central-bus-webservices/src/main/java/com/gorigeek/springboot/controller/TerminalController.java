package com.gorigeek.springboot.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gorigeek.springboot.Module;
import com.gorigeek.springboot.entity.Ciudad;
import com.gorigeek.springboot.entity.Estado;
import com.gorigeek.springboot.entity.RutasDetalles;
import com.gorigeek.springboot.entity.Terminales;
import com.gorigeek.springboot.exception.ResourceNotFoundException;
import com.gorigeek.springboot.repository.CiudadRepository;
import com.gorigeek.springboot.repository.EstadoRepository;
import com.gorigeek.springboot.repository.RutaDetalleRepository;
import com.gorigeek.springboot.repository.TerminalRepository;

@RestController
@RequestMapping("/api/terminales")
public class TerminalController {
    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private RutaDetalleRepository detalleRepository;

    @Autowired
    private CiudadRepository ciudadRepository;

    private static final Logger logger = LogManager.getLogger(TerminalController.class);

    @GetMapping
    public List<Terminales> getAllTerminales() {
        try {
            List<Terminales> lista = this.terminalRepository.findAllActive();
            lista.forEach(terminal -> terminal.getAfiliado().setLogotipo(null));
            return lista;
        } catch (Exception e) {
            System.out.print("Error: " + e);
            return new ArrayList<>();
        }
    }

    @Module("CentralBus - Comprar Boleto/Obtener Destinos")
    @GetMapping(value = "/all/{originOrDestiny}/{idStations}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Terminales>> getAllTerminalesOrigenDestino(
            @PathVariable(value = "originOrDestiny") Boolean originOrDestiny,
            @PathVariable(value = "idStations") String idStations) {
        try {
            List<Terminales> terminales;
            if ("all".equals(idStations)) {
                terminales = originOrDestiny ? this.terminalRepository.findAllTerminalesOrigen()
                        : this.terminalRepository.findAllTerminalesDestino();
            } else {
                Set<Long> idStationsSet = Arrays.stream(idStations.split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toSet());
                terminales = originOrDestiny
                        ? this.terminalRepository.findAllTerminalesOrigenByDestinoIds(idStationsSet)
                        : this.terminalRepository.findAllTerminalesDestinoByOrigenIds(idStationsSet);
            }
            terminales.forEach(terminal -> terminal.getAfiliado().setLogotipo(null));
            return ResponseEntity.ok(terminales);
        } catch (NumberFormatException e) {
            logger.log(Level.FATAL, "Error al parsear idStations: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.log(Level.FATAL, "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/afiliados/{ids}/{idStations}/{isOrigin}")
    public List<Terminales> getTerminales(@PathVariable(value = "ids") String idAfiliados,
            @PathVariable(value = "idStations") String idStations,
            @PathVariable(value = "isOrigin") Boolean isOrigin) {

        String[] idAfiliadosArray = idAfiliados.split(",");
        String[] idStationsArray = idStations.split(",");
        // Para asegurarse que no haya afiliados repetidos
        Set<Long> afiliadosIds = Arrays.stream(idAfiliadosArray)
                .map(Long::parseLong)
                .collect(Collectors.toSet());

        Set<Long> idStationsSet = Arrays.stream(idStationsArray)
                .map(Long::parseLong)
                .collect(Collectors.toSet());

        List<RutasDetalles> detalleRutas = detalleRepository.findAllRutasActivas();

        List<Terminales> terminales = terminalRepository.findAllByAfiliados(afiliadosIds);

        Set<Terminales> terminalesSet = new HashSet<>();
        for (Terminales terminal : terminales) {
            for (Long idStation : idStationsSet) {
                for (RutasDetalles detalle : detalleRutas) {
                    if ((!isOrigin && detalle.getIdTerminalOrigen().getIdt_terminales() == idStation
                            && terminal.getIdt_terminales() == detalle.getIdTerminalDestino().getIdt_terminales())
                            || (isOrigin && detalle.getIdTerminalDestino().getIdt_terminales() == idStation && terminal
                                    .getIdt_terminales() == detalle.getIdTerminalOrigen().getIdt_terminales())) {
                        terminal.getAfiliado().setLogotipo(null);
                        terminalesSet.add(terminal);
                        break;
                    }
                }
            }
        }

        return new ArrayList<>(terminalesSet);
    }

    @GetMapping("/{id}")
    public Terminales getTerminalById(@PathVariable(value = "id") long terminalId) {
        return this.terminalRepository.findById(terminalId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id:" + terminalId));
    }

    @GetMapping("/afiliado/{id}")
    public List<Terminales> getTerminalByIdAfiliado(@PathVariable(value = "id") int idAfiliado) {
        List<Terminales> lista = new ArrayList<>();
        List<Terminales> listaFiltrada = new ArrayList<>();
        try {
            lista = this.terminalRepository.findByIdAfiliado(idAfiliado);
            System.out.println("Búsqueda por afiliado de terminales hecha." + idAfiliado);
            System.out.println("size:" + lista.size());
            for (int i = 0; i < lista.size(); i++) {
                if ("1".equals(lista.get(i).getStatusTerminal())) {
                    listaFiltrada.add(lista.get(i));
                }
            }
            System.out.println("Búsqueda por afiliado filtrada de terminales hecha.");
            System.out.println("size:" + listaFiltrada.size());
            return listaFiltrada;
        } catch (Exception e) {
            System.out.print("Error: " + e);
            // TODO: handle exception
        }
        return lista;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteTerminales(@PathVariable(value = "id") long terminalId) {
        try {
            this.terminalRepository.deleteById(terminalId);
            return "ok";
        } catch (Exception e) {
            Throwable error = e.getCause();
            if (error != null) {
                if (error.getCause().getMessage()
                        .startsWith("Cannot delete or update a parent row: a foreign key constraint fails")) {
                    return "1451";
                } else if (error.getCause().getMessage().startsWith("HikariPool-1 - Connection is not available")) {
                    return "0";
                }
            } else {
                return "ok";
            }
            return "0";
        }
    }

    @PutMapping
    public Terminales updateTerminal(@RequestBody Terminales terminal) {
        terminal.setCiudad(ciudadRepository.findById(terminal.getCiudad().getIdc_ciudades()).get());
        terminal.setEstados(estadoRepository.findById(terminal.getEstados().getIdc_estados()).get());
        return this.terminalRepository.save(terminal);
    }

    @GetMapping("/estados")
    @ResponseBody
    public List<Estado> getAllEstados() {
        return estadoRepository.findAll();
    }

    @GetMapping("/{id}/ciudades")
    @ResponseBody
    public List<Ciudad> getAllCiudades(@PathVariable(value = "id") long estadoId) {
        return ciudadRepository.findByIdEstado(estadoId);
    }

    /**
     * Endpoint para obtener las ciudades filtradas por estado y que tengan una
     * terminal asignada
     */
    @GetMapping("/{id}/ciudadesTerminal")
    @ResponseBody
    public List<Ciudad> getAllCiudadesT(@PathVariable(value = "id") long estadoId) {
        return ciudadRepository.findByIdEstadoAndTerminal(estadoId);
    }

    @PostMapping
    public Terminales createTerminales(@RequestBody Terminales terminales) {
        return terminalRepository.save(terminales);
    }

    @GetMapping("/{id}/ciudades/terminales")
    @ResponseBody
    public List<Terminales> getAllTerminalesByCiudad(@PathVariable(value = "id") long idciudad) {
        Ciudad ciudad = ciudadRepository.findById(idciudad);
        return terminalRepository.findByCiudad(ciudad);
    }

    @GetMapping("/ciudades")
    @ResponseBody
    public List<Ciudad> getCiudades() {
        return ciudadRepository.findAll();
    }

}
