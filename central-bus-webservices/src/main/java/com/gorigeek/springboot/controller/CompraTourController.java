package com.gorigeek.springboot.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gorigeek.springboot.Module;
import com.gorigeek.springboot.entity.Estado;
import com.gorigeek.springboot.entity.ImagenesTour;
import com.gorigeek.springboot.entity.TourMovil;
import com.gorigeek.springboot.entity.DTO.TourDTO;
import com.gorigeek.springboot.repository.ComprarTourRepository;
import com.gorigeek.springboot.repository.EstadoRepository;
import com.gorigeek.springboot.repository.ImagenesTourRepository;

@RestController
@RequestMapping("/api/compraTour")
public class CompraTourController {
    
    @Autowired
    private ComprarTourRepository repo;
    
    @Autowired
    private EstadoRepository estadorepo;
    
    @Autowired
    private ImagenesTourRepository imagenesTourRepository;

    @GetMapping
    public List<TourMovil> GetAllTour(){
        return this.repo.findAll();
    }
    
    
  //Peticion para los Tours con sus diferentes peticiones
    @GetMapping("/dto")
    public List<TourDTO> GetTours(){
        
        List<TourDTO> tours = new ArrayList<>();
        
     // Obtener la fecha actual
        LocalDateTime fechaActual = LocalDateTime.now();
        
        //List<TourMovil> tourDB = this.repo.findAll();
        List<TourMovil> tourDBRespaldo = repo.findAllByOrderByFechaHoraSalidaAsc();
        //tourDB
        //tourDBRespaldo
       // System.out.println("exito");
        
        
     // Filtrar los tours que tienen fecha a partir de la fecha actual
        tourDBRespaldo = tourDBRespaldo.stream()
                      .filter(tour -> {
                          LocalDateTime fechaHoraSalida = LocalDateTime.parse(tour.getFechaHoraSalida(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                          return fechaHoraSalida.isAfter(fechaActual);
                      })
                      .collect(Collectors.toList());
        
        //filtrar por status afiliado
        List<TourMovil> tourDB =new ArrayList<TourMovil>();
        for (int i = 0; i < tourDBRespaldo.size(); i++) {
            if(tourDBRespaldo.get(i).getAfiliado().getCStatuscuentaActivo()==1) {
                tourDB.add(tourDBRespaldo.get(i));
            }
        }
        
        
        for(int i=0; i<tourDB.size(); i++) {
            TourDTO temp = new TourDTO();
            
            //caratulas
            List<ImagenesTour> portadas = imagenesTourRepository.findByTour(tourDB.get(i).getIdt_tour());
            //List<ImagenesTour> portadas = new ArrayList<>();
           // ImagenesTour portada = new ImagenesTour(1l,"vacio",tourDB.get(i).getIdt_tour());
           // ImagenesTour portada2 = new ImagenesTour(2l,"vacio2",tourDB.get(i).getIdt_tour());
           // portadas.add(portada);
           // portadas.add(portada2);
            //fin de caratulas
            
            temp.setIdTour(tourDB.get(i).getIdt_tour());
            temp.setEstadoOrigen(tourDB.get(i).getEstadoOrigen().getDescripcion());
            temp.setCiudadOrigen(tourDB.get(i).getCiudadesOrigen().getDescripcion());
            temp.setEstadoDestino(tourDB.get(i).getEstadoDestino().getDescripcion());
            temp.setCiudadDestino(tourDB.get(i).getCiudadesDestino().getDescripcion());
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime fechaHora = LocalDateTime.parse(tourDB.get(i).getFechaHoraSalida(), formatter);
            LocalDateTime fechaHoraRegreso = LocalDateTime.parse(tourDB.get(i).getFechaHoraRegreso(), formatter);
            temp.setFechaSalida(fechaHora.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".", ""));
            temp.setHoraSalida(fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim());
            temp.setFechaRegreso(fechaHoraRegreso.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".", ""));
            temp.setHoraRegreso(fechaHoraRegreso.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim());
            temp.setParadas(tourDB.get(i).getLugaresVisita());
            temp.setNombreTour(tourDB.get(i).getNombreTour());
            temp.setImagenesTour(portadas);
            
            tours.add(temp);
        }
        return tours;
    }
    
    //Peticion para los Tours con sus diferentes peticiones
    @Module("CentralBus - Comprar Tour/Obtener Tours")
    @GetMapping("/dtoPaginado/{pagina}")
    public List<TourDTO> GetToursPaginacion(@PathVariable (value="pagina")int pagina){
        
        System.out.println("el numero de pagina es: "+ pagina);
        //paginacion
        Pageable pageable = PageRequest.of(pagina, 10);
        
        // Obtener la fecha actual
        LocalDateTime fechaActual = LocalDateTime.now();
        //Page<TourMovil> paginaDB = repo.findAllByOrderByFechaHoraSalidaAsc(pageable);
        //Page<TourMovil> paginaDB = repo.findAllByFechaHoraSalidaAfterOrderByFechaHoraSalidaAsc(fechaActual.toString(), pageable);
        //Page<TourMovil> paginaDB = repo.findAllByFechaHoraSalidaAfterAndAfiliado_cStatuscuentaActivoOrderByFechaHoraSalidaAsc(fechaActual.toString(), 1, pageable);
        Page<TourMovil> paginaDB = repo.findAllByFechaHoraSalida(fechaActual.toString(), 1,"1", pageable);
        //Page<TourMovil> paginaDB = repo.findAllByFechaHoraSalidaAfterAndAfiliado_cStatuscuentaActivoAndStatusActivoOrderByFechaHoraSalidaAsc(fechaActual.toString(), 1,"1", pageable);
        List<TourMovil> tourDB = paginaDB.getContent();
        /*List<TourMovil> tourDB = paginaDB.getContent().stream()
                .filter(tour -> {
                    LocalDateTime fechaHoraSalida = LocalDateTime.parse(tour.getFechaHoraSalida(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    return fechaHoraSalida.isAfter(fechaActual) && tour.getAfiliado().getC_statuscuenta_activo() == 1;
                })
                .collect(Collectors.toList());*/
        
        List<TourDTO> tours = new ArrayList<>();
        
        
        
        // Filtrar los tours que tienen fecha a partir de la fecha actual
        /*tourDBRespaldo = tourDBRespaldo.stream()
                      .filter(tour -> {
                          LocalDateTime fechaHoraSalida = LocalDateTime.parse(tour.getFechaHoraSalida(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                          return fechaHoraSalida.isAfter(fechaActual);
                      })
                      .collect(Collectors.toList());
        
        //filtrar por status afiliado
        List<TourMovil> tourDB =new ArrayList<TourMovil>();
        for (int i = 0; i < tourDBRespaldo.size(); i++) {
            if(tourDBRespaldo.get(i).getAfiliado().getC_statuscuenta_activo()==1) {
                tourDB.add(tourDBRespaldo.get(i));
            }
        }*/
        
        
        for(int i=0; i<tourDB.size(); i++) {
            TourDTO temp = new TourDTO();
            System.out.println("Estado activo del afiliado en el elemento " + i + ": " + tourDB.get(i).getAfiliado().getCStatuscuentaActivo()+" y status normal "+tourDB.get(i).getStatusActivo());
            System.out.println("Estado activo del afiliado en el elemento " + i + ": " + tourDB.get(i).getAfiliado().getNombreTitular());
            
            String fechaSalida = tourDB.get(i).getFechaHoraSalida();
            
            // Imprimir la fecha de salida
            System.out.println("Fecha de salida del elemento " + i + ": " + fechaSalida);
            //caratulas
            List<ImagenesTour> portadas = imagenesTourRepository.findByTour(tourDB.get(i).getIdt_tour());
            
            temp.setIdTour(tourDB.get(i).getIdt_tour());
            temp.setEstadoOrigen(tourDB.get(i).getEstadoOrigen().getDescripcion());
            temp.setCiudadOrigen(tourDB.get(i).getCiudadesOrigen().getDescripcion());
            temp.setEstadoDestino(tourDB.get(i).getEstadoDestino().getDescripcion());
            temp.setCiudadDestino(tourDB.get(i).getCiudadesDestino().getDescripcion());
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime fechaHora = LocalDateTime.parse(tourDB.get(i).getFechaHoraSalida(), formatter);
            LocalDateTime fechaHoraRegreso = LocalDateTime.parse(tourDB.get(i).getFechaHoraRegreso(), formatter);
            temp.setFechaSalida(fechaHora.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".", ""));
            temp.setHoraSalida(fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim());
            temp.setFechaRegreso(fechaHoraRegreso.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".", ""));
            temp.setHoraRegreso(fechaHoraRegreso.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim());
            temp.setParadas(tourDB.get(i).getLugaresVisita());
            temp.setNombreTour(tourDB.get(i).getNombreTour());
            temp.setImagenesTour(portadas);
            
            tours.add(temp);
        }
        return tours;
    }
    
    //Filtro para el buscador de los Tours
    @GetMapping("/buscarTour/{fecha}/{origen}/{destino}")
    public List<TourDTO> GetToursBusqueda(@PathVariable (value="fecha")String fecha, 
            @PathVariable(value="origen")int origen, @PathVariable(value="destino")int destino){
        
        
        List<TourDTO> tours = new ArrayList<>();
        
        // Obtener la fecha actual
        LocalDateTime fechaActual = LocalDateTime.now();
        
        List<TourMovil> tourDBRespaldo = this.repo.findAllByBusqueda(fecha,origen,destino);
        
        System.out.println("SELECT t_tour.*  FROM t_tour \\r\\n\"\r\n"
        + "            + \"WHERE DATE(fecha_hora_salida) = "+fecha+"\\r\\n\"\r\n"
        + "            + \"OR c_estados_origen= "+origen+" OR c_estados_destino = "+destino);
        
    
       
     // Filtrar los tours que tienen fecha a partir de la fecha actual
        tourDBRespaldo = tourDBRespaldo.stream()
                      .filter(tour -> {
                          LocalDateTime fechaHoraSalida = LocalDateTime.parse(tour.getFechaHoraSalida(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                          return fechaHoraSalida.isAfter(fechaActual);
                      })
                      .collect(Collectors.toList());
        
        //filtrar por status afiliado
        List<TourMovil> tourDB =new ArrayList<TourMovil>();
        for (int i = 0; i < tourDBRespaldo.size(); i++) {
            if(tourDBRespaldo.get(i).getAfiliado().getCStatuscuentaActivo()==1) {
                tourDB.add(tourDBRespaldo.get(i));
            }
        }
        
        
        for(int i=0; i<tourDB.size(); i++) {
            TourDTO temp = new TourDTO();
            
          //caratulas
            List<ImagenesTour> portadas = imagenesTourRepository.findByTour(tourDB.get(i).getIdt_tour());
            
            temp.setIdTour(tourDB.get(i).getIdt_tour());
            temp.setEstadoOrigen(tourDB.get(i).getEstadoOrigen().getDescripcion());
            temp.setCiudadOrigen(tourDB.get(i).getCiudadesOrigen().getDescripcion());
            temp.setEstadoDestino(tourDB.get(i).getEstadoDestino().getDescripcion());
            temp.setCiudadDestino(tourDB.get(i).getCiudadesDestino().getDescripcion());
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime fechaHora = LocalDateTime.parse(tourDB.get(i).getFechaHoraSalida(), formatter);
            LocalDateTime fechaHoraRegreso = LocalDateTime.parse(tourDB.get(i).getFechaHoraRegreso(), formatter);
            temp.setFechaSalida(fechaHora.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".", ""));
            temp.setHoraSalida(fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim());
            temp.setFechaRegreso(fechaHoraRegreso.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".", ""));
            temp.setHoraRegreso(fechaHoraRegreso.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim());
            temp.setParadas(tourDB.get(i).getLugaresVisita());
            temp.setNombreTour(tourDB.get(i).getNombreTour());
            temp.setImagenesTour(portadas);
            
            tours.add(temp);
        }
        return tours;
    }
    
    
    @GetMapping("/filtrarTour/{fecha}/{minHora}/{maxHora}/{minPrecio}/{maxPrecio}")
    public List<TourDTO> GetToursFiltro(@PathVariable (value="fecha")String fecha, 
            @PathVariable(value="minHora")String minHora, @PathVariable(value="maxHora")String maxHora, 
            @PathVariable(value="minPrecio")Double minPrecio, @PathVariable(value="maxPrecio")Double maxPrecio){
        
        System.out.println("SELECT t_tour.*  FROM t_tour\r\n"
            + "JOIN t_costos_tour ON t_tour.idt_tour = t_costos_tour.t_tour\r\n"
            + "WHERE DATE(fecha_hora_salida) = ?1\r\n"
            + "AND (TIME(fecha_hora_salida) BETWEEN ?2 AND ?3)\r\n"
            + "AND ((t_costos_tour.adulto BETWEEN ?4 AND ?5)OR(t_costos_tour.nino BETWEEN ?4 AND ?5) OR (t_costos_tour.inapam BETWEEN ?4 AND ?5))");
        
        List<TourDTO> tours = new ArrayList<>();
        //
        List<TourMovil> tourDB = this.repo.findAllByFiltro(fecha,minHora,maxHora,minPrecio,maxPrecio);
        
        for(int i=0; i<tourDB.size(); i++) {
            TourDTO temp = new TourDTO();
            
            temp.setIdTour(tourDB.get(i).getIdt_tour());
            temp.setEstadoOrigen(tourDB.get(i).getEstadoOrigen().getDescripcion());
            temp.setCiudadOrigen(tourDB.get(i).getCiudadesOrigen().getDescripcion());
            temp.setEstadoDestino(tourDB.get(i).getEstadoDestino().getDescripcion());
            temp.setCiudadDestino(tourDB.get(i).getCiudadesDestino().getDescripcion());
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime fechaHora = LocalDateTime.parse(tourDB.get(i).getFechaHoraSalida(), formatter);
            LocalDateTime fechaHoraRegreso = LocalDateTime.parse(tourDB.get(i).getFechaHoraRegreso(), formatter);
            temp.setFechaSalida(fechaHora.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".", ""));
            temp.setHoraSalida(fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim());
            temp.setFechaRegreso(fechaHoraRegreso.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase().replace(".", ""));
            temp.setHoraRegreso(fechaHoraRegreso.format(DateTimeFormatter.ofPattern("hh:mm a")).replace(".", "").trim());
            temp.setParadas(tourDB.get(i).getLugaresVisita());
            
            tours.add(temp);
        }
        return tours;
    }
    
    
    
    
    @GetMapping("/buscarLugar")
    public List<Estado> GetEstados(){
        List<Estado> estados= new ArrayList<>();         
        estados=this.estadorepo.findAll();    
        System.out.println("estados=this.estadorepo.findAll();");
        return estados;
    }
    @GetMapping("/buscarLugar/{lugar}")
    public List<Estado> GetLugar(@PathVariable(value="lugar")String lugar){
        List<Estado> estados= new ArrayList<>();
        
        estados= this.estadorepo.findByDescripcionContaining(lugar);
        System.out.println("estados= this.estadorepo.findByDescripcionContaining(lugar);");
        return estados;
    }
}
 