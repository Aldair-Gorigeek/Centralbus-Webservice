package com.gorigeek.springboot.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gorigeek.springboot.Module;
import com.gorigeek.springboot.entity.AsientosTourTemp;
import com.gorigeek.springboot.entity.DetalleVentaTourMovil;
import com.gorigeek.springboot.entity.TipoBoleto;
import com.gorigeek.springboot.entity.TourMovil;
import com.gorigeek.springboot.entity.DTO.AsientosOcupadosTourDTO;
import com.gorigeek.springboot.exception.ResourceNotFoundException;
import com.gorigeek.springboot.repository.AsientoTourTempRepository;
import com.gorigeek.springboot.repository.DetalleVentaTourRepository;
import com.gorigeek.springboot.repository.TipoBoletoRepository;
import com.gorigeek.springboot.repository.TourMovilRepository;
import com.gorigeek.springboot.repository.VentaTourRepository;

@RestController
@RequestMapping("/api/asientosTour")
public class AsientosTourController {
    
   
    
    @Autowired
    private DetalleVentaTourRepository detalleVentaTourRepository;
    
    @Autowired
    private AsientoTourTempRepository asientoTempTourlRepository;
    
    
    @Autowired
    private TourMovilRepository tourMovilRepository;
    
    @Autowired
    private TipoBoletoRepository tipoBoletoRepository;
    
    
    
    
    @Autowired
    private VentaTourRepository ventaTourRepository;
    
    
    //lista general asientos reservados............
    @GetMapping("/temporalAsientoTour")
    public List<AsientosTourTemp> getTemporal() {
        List<AsientosTourTemp> listaAsientoTemp = asientoTempTourlRepository.findAll();
        return listaAsientoTemp;
    }
    
  //TRAER ASIENTOS OCUPADOS
    @Module("CentralBus - Comprar Tour/Obtener Asientos Ocupados")
    @GetMapping("/asientoOcupado/{idAutobus}/{fecha}/{tour}")//se agregó la hora, y la ruta
    //@GetMapping("/asientoOcupado/{idAutobus}/{fecha}/{terminalOrigen}/{terminalDestino}/{hora}/{ruta}")//se agregó la hora, y la ruta
   // public List<AsientosOcupadosTourDTO> getAsientos(@PathVariable(value = "idAutobus")Long idAutobus, @PathVariable(value = "fecha")String fecha, @PathVariable(value = "terminalOrigen")int origen,  @PathVariable(value = "terminalDestino")int destino,  @PathVariable(value = "hora")String hora ,  @PathVariable(value = "ruta")int ruta ) {
    public List<AsientosOcupadosTourDTO> getAsientos(@PathVariable(value = "idAutobus")Long idAutobus, @PathVariable(value = "fecha")String fecha, @PathVariable(value = "tour")int tour ) {
        System.out.println("dentro0");
        //List<DetalleVentaTourMovil> listaAsientosDB= detalleVentaTourRepository.getAsientosOcupados(idAutobus,fecha, origen, destino, hora+":00");
        List<DetalleVentaTourMovil> listaAsientosDB= detalleVentaTourRepository.getAsientosOcupados(idAutobus,fecha,tour);        
        List<AsientosOcupadosTourDTO> asientosOcupados= new ArrayList<>();
        if (!listaAsientosDB.isEmpty()) {
            for(int i= 0; i < listaAsientosDB.size();i++) {
                AsientosOcupadosTourDTO temp = new AsientosOcupadosTourDTO();
                temp.setIdTerminalOrigen(listaAsientosDB.get(i).getVentaTour().getTour().getCiudadesOrigen().getIdc_ciudades());
                temp.setIdTerminalDestino(listaAsientosDB.get(i).getVentaTour().getTour().getCiudadesDestino().getIdc_ciudades());
                temp.setIdTour(listaAsientosDB.get(i).getVentaTour().getTour().getIdt_tour());
                temp.setIdAutobus((long) listaAsientosDB.get(i).getAutobus());
                temp.setNumeroAsiento(listaAsientosDB.get(i).getNumeroAsiento());
                temp.setFechaHoraViaje(listaAsientosDB.get(i).getFechaViaje());
                
                temp.setTipoAsiento(listaAsientosDB.get(i).getTipoBoleto().getIdc_tipoBoleto());
                temp.setAsientoTemp("Ocupado");
                
                //Integer statusCancelado = listaAsientosDB.get(i).getStatusDisponible();
                
                //if (statusCancelado != null) {
                  //  temp.setStatusCancelado(statusCancelado);
                //} else {
                  //  temp.setStatusCancelado(0); // O asigna cualquier otro valor predeterminado
                //}
            
            
           
           
            
                System.out.println("dentro1");
            
            
                asientosOcupados.add(temp);
            
            }
            System.out.println("los asientos son----"+asientosOcupados.size());
            System.out.println("AQUI PASO1-----");
            //return asientosOcupados;
        
        }
        
        System.out.println("AQUI PASO2-----");
        

        
        //asientos temporales Tour
      List<AsientosTourTemp> asientosTemp= asientoTempTourlRepository.getAsientosOcupadosTemp(idAutobus,fecha,tour);
        System.out.println("dentro de temporales");
        for(int i= 0; i < asientosTemp.size();i++) {
            
          AsientosOcupadosTourDTO temp = new AsientosOcupadosTourDTO();
        temp.setIdTerminalOrigen(asientosTemp.get(i).getTour().getCiudadesOrigen().getIdc_ciudades());           
        temp.setIdTerminalDestino(asientosTemp.get(i).getTour().getCiudadesDestino().getIdc_ciudades());
        temp.setIdTour(asientosTemp.get(i).getTour().getIdt_tour());
        temp.setIdAutobus((long) asientosTemp.get(i).getAutobus());
        temp.setNumeroAsiento(asientosTemp.get(i).getNumeroAsiento());
        temp.setFechaHoraViaje(asientosTemp.get(i).getFechaViaje());
           
        temp.setTipoAsiento(asientosTemp.get(i).getTipoBoleto().getIdc_tipoBoleto());    
        temp.setAsientoTemp("Reservado");
            
            
        asientosOcupados.add(temp);
            
        }
        //fin de asientos temporales
        
        return asientosOcupados;
    }
    
    //RELLENAR TABLA TEMPORAL TOUR
    @Module("CentralBus - Comprar Tour/Reservacion")
    @PostMapping("/guardarTour")
    @Transactional
    public List<AsientosTourTemp> creacionTourTemp(@RequestBody JSONObject asientoTourTemp){
        TourMovil tour = this.tourMovilRepository.findById(Long.parseLong(asientoTourTemp.get("tour").toString()))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + asientoTourTemp.get("tour")));
        
        List<AsientosTourTemp> tempAsientos = new ArrayList<>();
        List<String> asientos = (List<String>) asientoTourTemp.get("asientos");
        for(int i = 0; i<asientos.size(); i++) {
            System.out.println("cantidad de asientos " + i+1);
            //
            String[] as =asientos.get(i).split("%");
            int nAsiento = Integer.parseInt(as[0]);
            Long tipoBLong = Long.parseLong(as[1]);
            Long idTipoBoleto = tipoBLong;
            //se assignan boletos y planta
            if(tipoBLong == 4l) {
                idTipoBoleto = 1l;
            }else if(tipoBLong == 5l) {
                idTipoBoleto = 2l;
            }else if(tipoBLong == 6l) {
                idTipoBoleto = 3l;
            }else {
                idTipoBoleto = tipoBLong;
            }
            //
            TipoBoleto tipoBoleto = this.tipoBoletoRepository.findById(idTipoBoleto)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + tipoBLong));
            //se crea el objeto temp
            AsientosTourTemp asientoGuardado = new AsientosTourTemp();
            //se rellena el objeto temp
            asientoGuardado.setTour(tour);
            asientoGuardado.setNumeroAsiento(nAsiento);
            asientoGuardado.setFechaViaje(asientoTourTemp.get("fechaViaje").toString());
            asientoGuardado.setTipoBoleto(tipoBoleto);
            asientoGuardado.setAutobus(Double.parseDouble(asientoTourTemp.get("autobus").toString()));
            asientoGuardado.setFechaInsert(asientoTourTemp.get("fechaInsert").toString());
            asientoGuardado.setUsuarioFinal(Long.parseLong(asientoTourTemp.get("usuarioFinal").toString()));
            
            
            AsientosTourTemp guardado = asientoTempTourlRepository.save(asientoGuardado);
            tempAsientos.add(guardado);
            
        }
        
        return tempAsientos;
    }
    @Module("CentralBus - Comprar Tour/Invitado/Reservacion")
    @PostMapping("/guardarTourInvitado")
    @Transactional
    public List<AsientosTourTemp> creacionTourTempInvitado(@RequestBody JSONObject asientoTourTemp){
        TourMovil tour = this.tourMovilRepository.findById(Long.parseLong(asientoTourTemp.get("tour").toString()))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + asientoTourTemp.get("tour")));
        
        List<AsientosTourTemp> tempAsientos = new ArrayList<>();
        List<String> asientos = (List<String>) asientoTourTemp.get("asientos");
        for(int i = 0; i<asientos.size(); i++) {
            System.out.println("cantidad de asientos " + i+1);
            //
            String[] as =asientos.get(i).split("%");
            int nAsiento = Integer.parseInt(as[0]);
            Long tipoBLong = Long.parseLong(as[1]);
            Long idTipoBoleto = tipoBLong;
            //se assignan boletos y planta
            if(tipoBLong == 4l) {
                idTipoBoleto = 1l;
            }else if(tipoBLong == 5l) {
                idTipoBoleto = 2l;
            }else if(tipoBLong == 6l) {
                idTipoBoleto = 3l;
            }else {
                idTipoBoleto = tipoBLong;
            }
            //
            TipoBoleto tipoBoleto = this.tipoBoletoRepository.findById(idTipoBoleto)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + tipoBLong));
            //se crea el objeto temp
            AsientosTourTemp asientoGuardado = new AsientosTourTemp();
            //se rellena el objeto temp
            asientoGuardado.setTour(tour);
            asientoGuardado.setNumeroAsiento(nAsiento);
            asientoGuardado.setFechaViaje(asientoTourTemp.get("fechaViaje").toString());
            asientoGuardado.setTipoBoleto(tipoBoleto);
            asientoGuardado.setAutobus(Double.parseDouble(asientoTourTemp.get("autobus").toString()));
            asientoGuardado.setFechaInsert(asientoTourTemp.get("fechaInsert").toString());
            //asientoGuardado.setUsuarioFinal(Long.parseLong(asientoTourTemp.get("usuarioFinal").toString()));
            
            
            AsientosTourTemp guardado = asientoTempTourlRepository.save(asientoGuardado);
            tempAsientos.add(guardado);
            
        }
        
        return tempAsientos;
    }
    
  //ELIMINAR TABLA TEMPORAL TOUR
    @Module("CentralBus - Comprar Tour/Eliminar Asiento Temporal 0_0")
    @DeleteMapping("/{usuarioFinalTour}")
    @Transactional
    public ResponseEntity<String> eliminarPorId(@PathVariable Long usuarioFinalTour) {
        asientoTempTourlRepository.deleteByUsuarioFinal(usuarioFinalTour);
        return ResponseEntity.ok("Registro eliminado exitosamente");
    }
    
  //ELIMINAR TABLA TEMPORAL TOUR
    @DeleteMapping("/eliminarTemporales")
    @Transactional
    public ResponseEntity<String> eliminarPorAsiento(@RequestBody JSONObject asientos) {
        
        List<Integer> asientosList = (List<Integer>) asientos.get("asientos");
        String fechaHora = asientos.get("fechaHora").toString();
        double autobus = (double) asientos.get("autobus");
        
        //System.out.println(asientosList.get(0));
        System.out.println(asientosList.toString());
        for(int i = 0; i<asientosList.size(); i++) {
            int numAsiento = asientosList.get(i);
            asientoTempTourlRepository.deleteByNumeroAsientoAndFechaViajeAndAutobus(numAsiento, fechaHora, autobus);
        }
        
        //asientoTempTourlRepository.deleteByUsuarioFinal(usuarioFinalTour);
        return ResponseEntity.ok("Registro eliminado exitosamente");
    }
    
    
    
    /*public ResponseEntity<String> guardarAsiento(@RequestBody AsientosTourTemp asiento) {
        TourMovil tour =this.tourMovilRepository.findById(asiento.getTour().getIdt_tour())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + asiento.getTour().getIdt_tour()));
        asiento.setTour(tour);
        
        TipoBoleto tipoBoleto = this.tipoBoletoRepository.findById(asiento.getTipoBoleto().getIdc_tipoBoleto())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + asiento.getTipoBoleto().getIdc_tipoBoleto()));
        
        asiento.setTour(tour);
        asiento.setFechaViaje(asiento.getFechaViaje());
        asiento.setAutobus(asiento.getAutobus());
        asiento.setFechaInsert("2023-10-14 08:46:22");
        asiento.setNumeroAsiento(asiento.getNumeroAsiento());
        asiento.setTipoBoleto(tipoBoleto);
        
        
        AsientosTourTemp asientoGuardado = asientoTempTourlRepository.save(asiento);
        
        return ResponseEntity.ok("Asiento guardado con éxito con ID: " + asientoGuardado.getIdtemp_asientos_tour());
    }*/

    

}


