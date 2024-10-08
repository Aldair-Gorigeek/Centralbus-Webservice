package com.gorigeek.springboot.controller;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.List;
import org.springframework.http.HttpStatus;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.util.Base64;
import com.gorigeek.crypto.EncryptCard;
import com.gorigeek.springboot.entity.TarjetaUsuarios;
import com.gorigeek.springboot.entity.TipoTarjeta;
import com.gorigeek.springboot.entity.DTO.Tarjetas_secondaryDTO;
import com.gorigeek.springboot.exception.ResourceNotFoundException;
import com.gorigeek.springboot.repository.TarjetaUsuariosRepository;
import com.gorigeek.springboot.repository.TarjetasSecondaryDAO;
import com.gorigeek.springboot.repository.TipoTarjetaRepository;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

@RestController
@RequestMapping("/api/tarjeta")
public class TarjetaUsuariosController {
    
    @Autowired
    private TarjetaUsuariosRepository tarjetausuarioRepo;
    
    @Autowired
    private TipoTarjetaRepository tipoTarjetaRepo;
    
    @Autowired
    private TarjetasSecondaryDAO tarjetasDAO;
    
      
  //obtener todos los boletos
    @GetMapping
    public List<TarjetaUsuarios> getAllTour(){
        return tarjetausuarioRepo.findAll();
        
    }
    //obtiene por id de la tabla primera
    @GetMapping("/obtener/{id}")
    public TarjetaUsuarios obtenerTarjetaPorId(@PathVariable Long id) {
        TarjetaUsuarios tarjeta = tarjetausuarioRepo.findByIdtTarjetasUsuarios(id);

        if (tarjeta != null) {
            String numeroTarjeta = tarjeta.getNumeroTarjeta();
            // Obtener la SecretKey
            TarjetasSecondaryDAO tarjetasDAO = new TarjetasSecondaryDAO();
            String keyTarjeta = null;

            try {
                String idPrincipal = tarjeta.getIdt_tarjetas_usuarios().toString();
                List<Tarjetas_secondaryDTO> tarjetas2 = tarjetasDAO.tarjetas(idPrincipal);

                // Desencriptar el número de tarjeta
                if (!tarjetas2.isEmpty()) {
                    Tarjetas_secondaryDTO tarjetaKey = tarjetas2.get(0);
                    byte[] decodedKey = DatatypeConverter.parseBase64Binary(tarjetaKey.getKey_tarjeta());

                    // Crear una instancia de SecretKey utilizando la clave decodificada
                    SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

                    EncryptCard encriptador = new EncryptCard();
                    String tarjetaDescifrada = encriptador.desencriptar(numeroTarjeta, secretKey);

                    // Modificar el número de tarjeta con la versión desencriptada
                    tarjeta.setNumeroTarjeta(tarjetaDescifrada);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Manejar excepciones según tus necesidades
            }
        }

        return tarjeta;
    }



    
   /* public TarjetaUsuarios obtenerTarjetaPorId(@PathVariable Long id) {
        return tarjetausuarioRepo.findByIdtTarjetasUsuarios(id);
        
        
    }*/
    
    //pedir toda la tabla de la segunda base que contiene la key para desencriptar
    @GetMapping("/todas")
    public List<Tarjetas_secondaryDTO> obtenerTodasLasTarjetas() {
        try {
            return tarjetasDAO.obtenerTodasLasTarjetas();
        } catch (SQLException e) {
            // Manejar la excepción según tus necesidades (puedes lanzar una excepción personalizada, loguear, etc.)
            e.printStackTrace();
            return null; // Otra opción sería devolver una respuesta de error apropiada
        }
    }
    
    //obtener tarjeta por id
    @GetMapping("/{id}")
    public TarjetaUsuarios obtenerId (@PathVariable(value = "id") Long id) {
        return tarjetausuarioRepo.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("rutaHasTransporte not found with id: " + id));
    }
    //obtener por id de usario final, este codigo funciona para ocultar y solo dejar 4 digitos
  /* @GetMapping("/usuario/{usuarioFinal}")
    public List<TarjetaUsuarios> obtenerTarjetasPorUsuario(@PathVariable(value = "usuarioFinal") Long usuarioFinal) {
        List<TarjetaUsuarios> tarjetas = tarjetausuarioRepo.findByUsuarioFinal(usuarioFinal);
        
        // Modificar los números de tarjeta antes de enviar la respuesta
        tarjetas.forEach(tarjeta -> {
            String numeroTarjeta = tarjeta.getNumeroTarjeta();
            if (numeroTarjeta != null && numeroTarjeta.length() > 4) {
                String lastFourDigits = numeroTarjeta.substring(numeroTarjeta.length() - 4);
                tarjeta.setNumeroTarjeta("X".repeat(numeroTarjeta.length() - 4) + lastFourDigits);
            }
        });

        return tarjetas;
   
        //return tarjetausuarioRepo.findByUsuarioFinal(usuarioFinal);
    }*/
    
    //este codigo sirve para separar de 4 en 4 y dejar los 4 digitos
    
    
    
    
    
  //obtener por id de usario final
  @GetMapping("/usuario/{usuarioFinal}") 
  public List<TarjetaUsuarios> obtenerTarjetasPorUsuario(@PathVariable(value = "usuarioFinal") Long usuarioFinal) {
   // Llamada al repositorio para obtener las tarjetas asociadas al usuarioFinal
      List<TarjetaUsuarios> tarjetas = tarjetausuarioRepo.findByUsuarioFinal(usuarioFinal);
     

      // Modificar los números de tarjeta antes de enviar la respuesta
      tarjetas.forEach(tarjeta -> {
          // Obtener el número de tarjeta actual
          String numeroTarjeta = tarjeta.getNumeroTarjeta();
          //obtener la SecretKey
          
          TarjetasSecondaryDAO tarjetasDAO = new TarjetasSecondaryDAO();
         
          String keyTarjeta = null; 

          // Llama al método que obtiene las tarjetas pasando el id_principal
          try {
              String idPrincipal = tarjeta.getIdt_tarjetas_usuarios().toString();
              List<Tarjetas_secondaryDTO> tarjetas2 = tarjetasDAO.tarjetas(idPrincipal);

            //desencriptar el numero de tarjeta
              Tarjetas_secondaryDTO tarjetaKey = tarjetas2.get(0);
              
              byte[] decodedKey = DatatypeConverter.parseBase64Binary(tarjetaKey.getKey_tarjeta());
              
              // Crear una instancia de SecretKey utilizando la clave decodificada
              SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
              
              
              EncryptCard encriptador = new EncryptCard();
              String tarjetaDescifrada = encriptador.desencriptar(numeroTarjeta, secretKey);
             
              //System.out.println("ID Principal: " + tarjetaKey.getId_principal());
              //System.out.println("Key Tarjeta: " + tarjetaKey.getKey_tarjeta());
              System.out.println("Get" );
              
              
           // Verificar si el número de tarjeta es válido y tiene más de 4 dígitos
              if (tarjetaDescifrada != null && tarjetaDescifrada.length() > 4) {
                  
                  // Construir la cadena oculta con espacios cada 4 caracteres
                  StringBuilder sbOculta = new StringBuilder();
                  for (int i = 0; i < tarjetaDescifrada.length() - 4; i++) {
                      if (i > 0 && i % 4 == 0) {
                          sbOculta.append(" "); // Insertar un espacio cada 4 caracteres
                      }
                      sbOculta.append("*"); // Ocultar todos los dígitos excepto los últimos cuatro
                  }

                  // Obtener los últimos cuatro dígitos
                  String lastFourDigits = tarjetaDescifrada.substring(tarjetaDescifrada.length() - 4);

                  // Construir la cadena final con espacios y los últimos 4 dígitos
                  String resultadoFinal = sbOculta.toString() + " " + lastFourDigits;

                  // Modificar el número de tarjeta con la cadena final
                  tarjeta.setNumeroTarjeta(resultadoFinal);
              }
              
              

          } catch (Exception e) {
              e.printStackTrace();
             
          };
     
          
      });

      
      return tarjetas;
  
  }
    
    
    @PostMapping("/guardar")
    @Transactional
    public String guardarTarjeta(@RequestBody TarjetaUsuarios tarjeta) throws SQLException {
        System.out.println(tarjeta.getTipoTarjeta().getIdcTipoTarjeta());
        TipoTarjeta tipoTarjeta =  tipoTarjetaRepo.findById(tarjeta.getTipoTarjeta().getIdcTipoTarjeta()).
                orElseThrow(() -> new ResourceNotFoundException("tipoTarjeta no encontrado"));
        tarjeta.setTipoTarjeta(tipoTarjeta);
        
        //encriptar numero tarjeta
        EncryptCard encriptador = new EncryptCard();
        
        List<Tarjetas_secondaryDTO> listaSecundaria = tarjetasDAO.obtenerTodasLasTarjetas();
        
        int cantidadDatos = listaSecundaria.size();
        String tarjetaEntrante = tarjeta.getNumeroTarjeta();
        boolean coinciden = false;
        for (int i = 0; i < cantidadDatos; i++) {
            Tarjetas_secondaryDTO elemento = listaSecundaria.get(i);         
           // TarjetaUsuarios tarjetaDatos = tarjetausuarioRepo.findByIdtTarjetasUsuarios( Long.parseLong(elemento.getId_principal().toString()) );
            TarjetaUsuarios tarjetaDatos = tarjetausuarioRepo.findByIdtTarjetasUsuarios(Long.valueOf(elemento.getId_principal()));
            
            if(tarjetaDatos == null) {
                continue;
            }

            //desencriptar el numero de tarjeta
            try {
                byte[] decodedKey = DatatypeConverter.parseBase64Binary(elemento.getKey_tarjeta());
                
                // Crear una instancia de SecretKey utilizando la clave decodificada
                SecretKey secretKey1 = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
                String numTarjetaDesencriptada = encriptador.desencriptar(tarjetaDatos.getNumeroTarjeta(), secretKey1);
              //  System.out.println("tarjeta entrante: "+ tarjetaEntrante + " tarjetaGuardada " + numTarjetaDesencriptada);
                //comparar
                if(tarjetaEntrante.equals(numTarjetaDesencriptada)) {
                    coinciden = true;
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                return "fail";
            }
            
            
        }
        //guardado
        if(coinciden) {
            return "coinciden";
        } else {
            try {
             // Generar una clave secreta para AES
                SecretKey secretKey = encriptador.generarClaveSecreta();
                
                // System.out.println("la clave secreta es: " + secretKey);
                // Encriptar el número de tarjeta
                String numeroTarjetaEncriptado = encriptador.encriptar(tarjeta.getNumeroTarjeta(), secretKey);
                tarjeta.setNumeroTarjeta(numeroTarjetaEncriptado);
                System.out.println("Post" );
                
                //SE GUARDA LA TARJETA OBTENIDA EN LA BASE DE DATOS
                TarjetaUsuarios tarjetaGuardada = tarjetausuarioRepo.save(tarjeta);
                
                
                
                //agregar key de tarjeta en base de datos
                TarjetasSecondaryDAO tarjetasDAO = new TarjetasSecondaryDAO();
                
                tarjetasDAO.guardar(tarjetaGuardada.getIdt_tarjetas_usuarios().toString(), DatatypeConverter.printBase64Binary(secretKey.getEncoded()));
               // tarjetasDAO.guardar(tarjetaGuardada.getIdt_tarjetas_usuarios().toString(), secretKey.getEncoded().toString());
                
                return "ok";
            } catch (Exception e) {
                e.printStackTrace();
                return "fail";
            }
               
        }
           
    }
    
   
    
    @DeleteMapping("/{id}")   
    @Transactional
    public ResponseEntity<String> eliminarPorId(@PathVariable Long id) throws SQLException {
        //try {
            System.out.println("inicion eliminar");
            // Obtener la tarjeta de la primera base de datos
            TarjetaUsuarios tarjeta = tarjetausuarioRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la tarjeta con el ID proporcionado"));
            System.out.println("inicion eliminar 2");
            
            // Eliminar de la segunda base de datos
            //private TarjetasSecondaryDAO tarjetasDAO = new TarjetasSecondaryDAO();
            tarjetasDAO.eliminarPorIdPrincipal(tarjeta.getIdt_tarjetas_usuarios().toString());
            System.out.println("inicion eliminar 3");
            // Eliminar de la primera base de datos
            tarjetausuarioRepo.deleteById(id);
            
            System.out.println("inicion eliminar 4");

            return ResponseEntity.ok("ok");
        /*} catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
        }*/
    }
    
    
    
    @DeleteMapping("respaldo/{id}")   
    @Transactional
    public ResponseEntity<String> eliminarPorIdprueba(@PathVariable Long id) {
        tarjetausuarioRepo.deleteByIdtTarjetasUsuarios(id);
        return ResponseEntity.ok("Registro eliminado exitosamente");
    }
    
    
    
   

}


