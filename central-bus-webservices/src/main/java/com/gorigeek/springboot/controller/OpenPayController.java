package com.gorigeek.springboot.controller;

import javax.mail.MessagingException;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gorigeek.springboot.Module;
import com.gorigeek.springboot.entity.Transacciones;
import com.gorigeek.springboot.openpay.OpenPay;
import com.gorigeek.springboot.repository.TransaccionRepository;
import com.gorigeek.springboot.util.EmailService;

import mx.openpay.client.Card;
import mx.openpay.client.Charge;
import mx.openpay.client.Customer;
import mx.openpay.client.Payout;
import mx.openpay.client.core.requests.transactions.CreateStoreChargeParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;



@RestController
@RequestMapping("/api/openPay")
public class OpenPayController {
    @Value("${publicKey}")
    private String publicKey;
    @Value("${statusproject}")
    private String statusProject;
    @Value("${privateKey}")
    private String privateKey;
    
    @Value("${openpayApi}")
    private String urlApi;
    
    @Value("${openpayApiProd}")
    private String urlApiProduccion;
    
    
    
    @Value("${merchantId}")
    private String merchantId;
    
    
    @Value("${urlPaySuccess}")
    private String urlPaySuccess;
    
    @Autowired
    private TransaccionRepository repoTransaccion;
    
    @Autowired
    private EmailService emailService;
    
    /** CREACION DE CLIENTE
     * @calle2 esta variable es opcional por lo cual si no recibe algun valor se le asigna un valor vacio
    **/
    @PostMapping("/cliente")//FUNCIONA
    public String createCliente(@RequestBody JSONObject cliente) throws OpenpayServiceException, ServiceUnavailableException {
        
        String calle1 = cliente.get("Calle").toString();
        String calle2 = ""; //opcional
        String calle3 = "";
        String ciudad = cliente.get("Ciudad").toString();         
        String codigoP = cliente.get("CodigoP").toString();          
        String estado = cliente.get("Estado").toString();         
        String codigoCiudad = cliente.get("CodigoCiudad").toString();         
        String nombre = cliente.get("Nombre").toString();             
        String apellido = cliente.get("Apellido").toString();         
        String email = cliente.get("Email").toString();            
        String celular = cliente.get("Celular").toString();         
        /*String direccion = cliente.get("Direccion").toString();  */
        
        if(cliente.containsKey("Calle2")) {
           calle2 = cliente.get("Calle2").toString();
        } 
        if(cliente.containsKey("Calle3")) {
           calle3 = cliente.get("Calle3").toString();   
        }
            
      //url de openpay
        String urlOpenPay= urlApi;
        if(statusProject.equals("DEV")) {
            urlOpenPay = urlApi;
        } else if(statusProject.equals("PROD")) {
            urlOpenPay = urlApiProduccion;
        }
        System.out.println("la url de openpay es: "+ urlOpenPay);
       //instanciar el objeto de openPay
        OpenPay openPay = new OpenPay(urlOpenPay, privateKey,publicKey, statusProject);
        openPay.NuevoCliente(calle1, calle2, calle3, ciudad, codigoP, estado, codigoCiudad, nombre, apellido, email, celular);
        //System.out.println("el id del cliente es: "+customer.getId());
        
        return "ok";
        
    } 
    /**MOTODO PARA ELIMINAR UN CLIENTE**/
    @DeleteMapping("cliente/eliminar") //FUNCIONA
    public String eliminarCliente(@RequestBody JSONObject cliente) throws OpenpayServiceException, ServiceUnavailableException {
        String idCliente;
        idCliente = cliente.get("id").toString();
      //url de openpay
        String urlOpenPay= urlApi;
        if(statusProject.equals("DEV")) {
            urlOpenPay = urlApi;
        } else if(statusProject.equals("PROD")) {
            urlOpenPay = urlApiProduccion;
        }
        System.out.println("la url de openpay es: "+ urlOpenPay);
       //instanciar el objeto de openPay
        OpenPay openPay = new OpenPay(urlOpenPay, privateKey,publicKey, statusProject);
        openPay.eliminarCliente(idCliente);
        return "ok";
    }
    
    
    /**-----------------------------------CARGOS--------------------------------**/
    @PostMapping("/tarjeta")
    public JSONObject pagoTarjeta(@RequestBody JSONObject credito) throws OpenpayServiceException, ServiceUnavailableException {
      
      String  ordenId = "";
      String moneda= "MXN";
      String deviceSessionId = credito.get("DeviceSessionId").toString();
      String IdTarjeta = credito.get("IdTarjeta").toString();
      String nombre = credito.get("Nombre").toString();
      String apellidos = credito.get("Apellidos").toString();
      String telefono = credito.get("Telefono").toString();
      String correo = credito.get("Correo").toString();
      String descripcion = credito.get("Descripcion").toString();
      String monto = credito.get("Monto").toString();
      if (credito.containsKey("OrdenId")){
          ordenId = credito.get("OrdenId").toString();  //opcional
      }
      if (credito.containsKey("Moneda")){
          moneda = credito.get("Moneda").toString();  //opcional
      }
      //String numeroTarjeta = credito.get("Tarjeta").toString();
      //String propietario = credito.get("Propietario").toString();
      //String cvv2 = credito.get("Cvv2").toString();
      //String mesExpiracion = credito.get("MesExpiracion").toString();
      //String anoExpiracion = credito.get("AnoExpiracion").toString();
      
      System.out.println("---- datos recibidos Pago con tarjeta openPay ----");
      System.out.println("deviceSesion" + deviceSessionId);
      System.out.println("idTarjeta" + IdTarjeta);
      System.out.println("nombre" + nombre);
      System.out.println("apellidos" + apellidos);
      System.out.println("telefono" + telefono);
      System.out.println("correo" + correo);
      System.out.println("descripcion" + descripcion);
      System.out.println("monto" + monto);
      
    //url de openpay
      String urlOpenPay= urlApi;
      if(statusProject.equals("DEV")) {
          urlOpenPay = urlApi;
      } else if(statusProject.equals("PROD")) {
          urlOpenPay = urlApiProduccion;
      }
      System.out.println("la url de openpay es: "+ urlOpenPay);
     //instanciar el objeto de openPay
      OpenPay openPay = new OpenPay(urlOpenPay, privateKey,publicKey, statusProject);         
      
      //realizar cargo a tarjeta de credito
      
      Charge cargoCredito = openPay.CargarCreditoSinCliente(nombre, apellidos,telefono, correo, IdTarjeta, deviceSessionId, descripcion, monto, ordenId, moneda);
      System.out.println("Se realizo un pago para:"+cargoCredito.getDescription());
      System.out.println("Se realizo un pago para id :"+cargoCredito.getId());
      System.out.println("Se realizo un pago para id :"+cargoCredito.getAuthorization());
      System.out.println("Se realizo un pago para id :"+cargoCredito.getAmount());
      
      JSONObject resp = new JSONObject();
      resp.put("idTransaccion", cargoCredito.getId());
      resp.put("numAutorizacion", cargoCredito.getAuthorization());
      resp.put("monto", cargoCredito.getAmount());
      //return cargoCredito.getId();
      return resp;

    }
    
    @Module("CentralBus - Proceso de Pago/3D Secure")
    @PostMapping("/tarjetaSecure")
    public JSONObject pagoTarjetaSecure(@RequestBody JSONObject credito) throws OpenpayServiceException, ServiceUnavailableException {
      
      String  ordenId = "";
      String moneda= "MXN";
      String deviceSessionId = credito.get("DeviceSessionId").toString();
      String IdTarjeta = credito.get("IdTarjeta").toString();
      String nombre = credito.get("Nombre").toString();
      String apellidos = credito.get("Apellidos").toString();
      String telefono = credito.get("Telefono").toString();
      String correo = credito.get("Correo").toString();
      String descripcion = credito.get("Descripcion").toString();
      String monto = credito.get("Monto").toString();
      
      if (credito.containsKey("OrdenId")){
          ordenId = credito.get("OrdenId").toString();  //opcional
      }
      if (credito.containsKey("Moneda")){
          moneda = credito.get("Moneda").toString();  //opcional
      }
      
      String numCard = credito.get("numCard").toString();
      String dominio = credito.get("urlActual").toString();
      
      System.out.println("---- datos recibidos Pago con tarjeta openPay ----");
      System.out.println("deviceSesion" + deviceSessionId);
      System.out.println("idTarjeta" + IdTarjeta);
      System.out.println("nombre" + nombre);
      System.out.println("apellidos" + apellidos);
      System.out.println("telefono" + telefono);
      System.out.println("correo" + correo);
      System.out.println("descripcion" + descripcion);
      System.out.println("monto" + monto);
      
      //url de redireccionamiento
      String url = "";
      //url de openpay
      String urlOpenPay= urlApi;
      if(statusProject.equals("DEV")) {
          urlOpenPay = urlApi;
      } else if(statusProject.equals("PROD")) {
          urlOpenPay = urlApiProduccion;
      }
      System.out.println("la url de openpay es: "+ urlOpenPay);
      //instanciar el objeto de openPay
      //OpenPay openPay = new OpenPay(urlApi, privateKey,publicKey, statusProject);         
      OpenPay openPay = new OpenPay(urlOpenPay, privateKey,publicKey, statusProject);         
      //crear url para redireccionar
      String urlPago = dominio + urlPaySuccess;
      System.out.println("url de redireccion a pago realizado "+urlPago);
      //realizar cargo a tarjeta de credito
      
      Charge cargoCredito = openPay.CargarCreditoSinClienteSecure(nombre, apellidos,telefono, correo, IdTarjeta, deviceSessionId, descripcion, monto, ordenId, moneda, numCard, urlPago);
      System.out.println("Se realizo un pago para:"+cargoCredito.getDescription());
      System.out.println("Se realizo un pago para id :"+cargoCredito.getId());
      System.out.println("Se realizo un pago para id :"+cargoCredito.getAuthorization());
      System.out.println("Se realizo un pago para id :"+cargoCredito.getAmount());
      System.out.println("status del cargo: "+ cargoCredito.getStatus());
      
      JSONObject resp = new JSONObject();
      resp.put("idTransaccion", cargoCredito.getId());
      resp.put("numAutorizacion", cargoCredito.getAuthorization());
      resp.put("monto", cargoCredito.getAmount());
      resp.put("nombre", nombre);
      resp.put("url", cargoCredito.getPaymentMethod().getUrl());
      //return cargoCredito.getId();
      return resp;

    }
    
    @Module("CentralBus - Proceso de Pago/Detalles de Cargo")
    @GetMapping("/getCargo/{id}")
    public JSONObject getCargo(@PathVariable(value = "id")String id) throws OpenpayServiceException, ServiceUnavailableException {
     
      //url de openpay
        String urlOpenPay= urlApi;
        if(statusProject.equals("DEV")) {
            urlOpenPay = urlApi;
        } else if(statusProject.equals("PROD")) {
            urlOpenPay = urlApiProduccion;
        }
        System.out.println("la url de openpay es: "+ urlOpenPay);
       //instanciar el objeto de openPay
        OpenPay openPay = new OpenPay(urlOpenPay, privateKey,publicKey, statusProject);         
      
      Charge cargoCredito = openPay.obtenerCargo(id);
      
      JSONObject resp = new JSONObject();
      resp.put("idTransaccion", cargoCredito.getId());
      resp.put("numAutorizacion", cargoCredito.getAuthorization());
      resp.put("monto", cargoCredito.getAmount());
      resp.put("status", cargoCredito.getStatus());
      System.out.println("status del cargo: "+ cargoCredito.getStatus());
      return resp;
    }
    
    @PostMapping("/tarjetaCliente")
    public String pagoTarjetaCliente(@RequestBody JSONObject credito) throws OpenpayServiceException, ServiceUnavailableException {
        
      String  ordenId = "";
      String deviceSessionId = credito.get("DeviceSessionId").toString();
      String IdTarjeta = credito.get("IdTarjeta").toString();
      String numeroTarjeta = credito.get("Tarjeta").toString();
      String propietario = credito.get("Propietario").toString();
      String cvv2 = credito.get("Cvv2").toString();
      String mesExpiracion = credito.get("MesExpiracion").toString();
      String anoExpiracion = credito.get("AnoExpiracion").toString();
      String descripcion = credito.get("Descripcion").toString();
      String monto = credito.get("Monto").toString();
      if (credito.containsKey("OrdenId")){
          ordenId = credito.get("OrdenId").toString();  //opcional
      }
    //url de openpay
      String urlOpenPay= urlApi;
      if(statusProject.equals("DEV")) {
          urlOpenPay = urlApi;
      } else if(statusProject.equals("PROD")) {
          urlOpenPay = urlApiProduccion;
      }
      System.out.println("la url de openpay es: "+ urlOpenPay);
     //instanciar el objeto de openPay
      OpenPay openPay = new OpenPay(urlOpenPay, privateKey,publicKey, statusProject);           
      // CREAR CLIENTE
      
      Customer customer =openPay.NuevoCliente("calle", "calle2", "calle3", "ciudad", "c.p", "estado", "MX", "nombre", "apellido", "jammleo@gmail.com", "9621415408");
      //realizar cargo a tarjeta de credito
      
      Charge cargoCredito = openPay.CargarCreditoconCliente(customer, IdTarjeta, deviceSessionId, descripcion, monto, ordenId);
      System.out.println("Se realizo un pago para:"+cargoCredito.getDescription());
      System.out.println("Se realizo un pago para id :"+cargoCredito.getId());
      return cargoCredito.getId();

    }
    
    /*CANCELAR CARGO*/
    @PutMapping("/cargoCancelar")
    public String cancelarCargo(@RequestBody JSONObject cargo) throws OpenpayServiceException, ServiceUnavailableException {
      String idCargo = cargo.get("idCargo").toString();
      String descripcion = cargo.get("descripcion").toString();
      String monto = cargo.get("monto").toString();
           
    //url de openpay
      String urlOpenPay= urlApi;
      if(statusProject.equals("DEV")) {
          urlOpenPay = urlApi;
      } else if(statusProject.equals("PROD")) {
          urlOpenPay = urlApiProduccion;
      }
      System.out.println("la url de openpay es: "+ urlOpenPay);
     //instanciar el objeto de openPay
      OpenPay openPay = new OpenPay(urlOpenPay, privateKey,publicKey, statusProject);    
      
      //Cancelar cargo a tarjeta de credito
      Charge reembolso = openPay.reembolsoTarjetaSinCliente(idCargo, monto, descripcion);
      
      System.out.println(reembolso.getId());
      System.out.println(reembolso.getRefund().getAmount());
      System.out.println(reembolso.getRefund().getDescription());
      
      return reembolso.getId();

    }
    
    /**------------------------------------ALMACENAR EN LA BD-------------------------------------------*/
    @PostMapping("/guardar")
    public Transacciones guardarTransaccion(@RequestBody Transacciones transaccion) throws OpenpayServiceException, ServiceUnavailableException {
   
        return repoTransaccion.save(transaccion);
    }
    
    /**-------------------------------PAYNET-------------------------- 
     * @throws MessagingException **/
    @PostMapping("/paynet")
    @Transactional
    public String cargoPaynet(@RequestBody JSONObject cargo) throws OpenpayServiceException, ServiceUnavailableException, MessagingException {
        System.out.println("Realizando cargo a tienda...");
        System.out.println("status: "+ statusProject);
        Charge charge = new Charge();
        
        //parametro de url
        String sandbox = "https://sandbox-dashboard.openpay.mx";
        String produccion = "https://dashboard.openpay.mx";
        String rutaDashboard = sandbox;
        
      //url de openpay
        String urlOpenPay= urlApi;
        if(statusProject.equals("DEV")) {
            urlOpenPay = urlApi;
            rutaDashboard = sandbox;
        } else if(statusProject.equals("PROD")) {
            urlOpenPay = urlApiProduccion;
            rutaDashboard = produccion;
        }
        System.out.println("la url de openpay es: "+ urlOpenPay);
       //instanciar el objeto de openPay
        OpenPay openPay = new OpenPay(urlOpenPay, privateKey,publicKey, statusProject);
        
        charge = openPay.crearCargoPaynet(cargo.get("nombre").toString(), cargo.get("correo").toString(), cargo.get("descripcion").toString(), cargo.get("monto").toString());
        String referencia = charge.getPaymentMethod().getReference();
        System.out.println("la referencia es: "+ referencia);
        System.out.println("el merchantId es " + merchantId);
        System.out.println("el link de el ticket de pago es:");
        System.out.println(rutaDashboard +"/paynet-pdf/"+merchantId+"/"+referencia);
        //https://sandbox-dashboard.openpay.mx/paynet-pdf/mc1tgoh7r8fxgto55yoz/1010102268004217
        
        String rutaRecibo = rutaDashboard +"/paynet-pdf/"+merchantId+"/"+referencia;
        
        //enviar correo
        emailService.sendEmailReciboPaynet(cargo.get("correo").toString(), "Pago en tiendas", rutaRecibo);
                                   
        //return charge;
        return rutaRecibo;
    }
    
    /**------------------------------------PAGO-------------------------------------------*/
   /* @PostMapping("/debito")
    public String createTarjetaDebito(@RequestBody JSONObject debito) throws OpenpayServiceException, ServiceUnavailableException {
        
        
        
        String numeroTarjeta = debito.get("Tarjeta").toString();
        String propietario = debito.get("Propietario").toString();
        String codigoBank = debito.get("CodigoBank").toString();
        String monto = debito.get("Monto").toString();
        String descripcion = debito.get("Descripcion").toString();
        String ordenId = "";
        if (debito.containsKey("OrdenId")) {
            ordenId = debito.get("OrdenId").toString();
        }
        
        OpenPay openPay = new OpenPay();
        //llamar cliente por ID
        Customer customer = openPay.obtenerCliente("abr4heuaytqui3gx4vc9");
        Payout pagoDebito = openPay.pagoDebito(customer, numeroTarjeta, propietario, codigoBank, monto, descripcion, ordenId);
        
        System.out.println("La descripcion del pago es:"+pagoDebito.getDescription());
        
                                      
        return "ok";
    }*/
    
    
    /*
    @PostMapping("/pagoBancario")
    public String pagoBancario(@RequestBody JSONObject pagoBancario) throws OpenpayServiceException, ServiceUnavailableException {
        
        String clabe = pagoBancario.get("Clabe").toString();
        String propietario = pagoBancario.get("Propietario").toString();
        String alias="";
        if(pagoBancario.containsKey("Alias")) {
            alias = pagoBancario.get("Alias").toString();
           }
        
        String monto = pagoBancario.get("Monto").toString();
        String descripcion = pagoBancario.get("Descripcion").toString();
        
        String ordenId = "";
        if(pagoBancario.containsKey("OrdenId")) {
            ordenId =pagoBancario.get("OrdenId").toString();
           }
        
        
        
        
        OpenPay openPay = new OpenPay();
      //llamar cliente por ID
        Customer customer = openPay.obtenerCliente("abr4heuaytqui3gx4vc9");
        Payout pagobancario = openPay.pagoBancario(customer, clabe, propietario, alias, monto, descripcion, ordenId);
        System.out.println("El pago bancario es:"+pagobancario.getDescription());
        
            
        
        
        return "ok";    
        }*/
}