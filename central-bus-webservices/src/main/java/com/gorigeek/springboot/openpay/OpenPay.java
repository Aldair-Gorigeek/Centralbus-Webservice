package com.gorigeek.springboot.openpay;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import mx.openpay.client.Address;
import mx.openpay.client.BankAccount;
import mx.openpay.client.Card;
import mx.openpay.client.Charge;
import mx.openpay.client.Customer;
import mx.openpay.client.Payout;
import mx.openpay.client.Plan;
import mx.openpay.client.Subscription;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.requests.RequestBuilder;
import mx.openpay.client.core.requests.transactions.CancelParams;
import mx.openpay.client.core.requests.transactions.CreateBankChargeParams;
import mx.openpay.client.core.requests.transactions.CreateBankPayoutParams;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.core.requests.transactions.CreateCardPayoutParams;
import mx.openpay.client.core.requests.transactions.CreateStoreChargeParams;
import mx.openpay.client.core.requests.transactions.RefundParams;
import mx.openpay.client.enums.PlanRepeatUnit;
import mx.openpay.client.enums.PlanStatusAfterRetry;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.SearchParams;

public class OpenPay {
    
    /**
    * Clase con los metodos y propiedades para implementar 
    * el método de pago con OpenPay para sus distintos módulos.
    * @privateKey : se refiere a la llave privada
    * @merchantId : es el ID de el propietario de OpenPay
    * */
    
    /**Credenciales que se requieren para el funcionamiento de OpenPay**/
    String privateKey = "sk_99940185eb48411ab11da6ba0a2c93d2";
    String publicKey = "pk_8a121b8f16534872b6302502ad5c8766";
    String merchantId = "mc1tgoh7r8fxgto55yoz";
    
//    @Value("${statusproject}")
    private String statusProject;
    
    //@Value("${openpayApi}")
    private String urlApi="https://sandbox-api.openpay.mx";  
    
    //@Value("${privateKey}")
    //private String privateKey;
    
    //@Value("${publicKey}")
    //private String publicKey;
    
    //@Value("${merchantId}")
    //private String merchantId;
    
    public OpenPay() {
    }
    
    public OpenPay(String url,String privateKey,String publicKey, String status) {
        this.publicKey=publicKey;
        this.statusProject=status;
        this.privateKey=privateKey;
        this.urlApi=url;
    }
    
    /*-------------Inicialización de el API de OpenPay-----------------*/
    @Autowired
    //OpenpayAPI api = new OpenpayAPI("https://sandbox-api.openpay.mx", privateKey, merchantId);
    OpenpayAPI api = new OpenpayAPI(urlApi, privateKey, merchantId);
    //OpenpayAPI api = new OpenpayAPI("https://sandbox-api.openpay.mx/v1/", privateKey, merchantId);
    
    
    /**
     * Método para poder crear un nuevo cliente------------------------------------
     * @adress son los datos de dirección de el nuevo cliente, que se insertan dentro de el parametro de customer
     * @customer son los datos personales de el nuevo cliente
     * */
    
    /**------------------------------------ CLIENTES------------------------------------------**/
    public Customer NuevoCliente(String calle1, String calle2, String calle3, String ciudad, String codigoPostal, String estado, String codigoPais, String nombre, String apellido, String correo, String telefono) throws OpenpayServiceException, ServiceUnavailableException {
        Address address = new Address()
                .line1(calle1)
                .line2(calle2)             // Optional
                .line3(calle3)                 // Optional
                .city(ciudad)
                .postalCode(codigoPostal)    
                .state(estado)
                .countryCode(codigoPais);                  // ISO 3166-1 two-letter code: el codigo deberá de ser de dos letras
        System.out.println("dentro de método");         
        Customer customer = api.customers().create(new Customer()
                .name(nombre)
                .lastName(apellido)
                .email(correo)
                .phoneNumber(telefono)
                .address(address));
        
        return customer;
    }
    
    /**metodo para obtener un cliente por id
     * @idCliente, se refiere al id de algun cliente ya registrado para poder realizar alguna transacción*/
    public Customer obtenerCliente(String idCliente) throws OpenpayServiceException, ServiceUnavailableException {
        Customer customer = api.customers().get(idCliente);
        return customer;
    }
    
    /**metodo para eliminar un cliente creado por id
     * @idCliente, se refiere al id de algun cliente ya registrado para poder eliminarlo*/
    public void eliminarCliente(String idCliente) throws OpenpayServiceException, ServiceUnavailableException {
        api.customers().delete(idCliente);
    }
    
    /**metodo para llamar a la lista de clientes*/
    public List<Customer> listaClientes() throws OpenpayServiceException, ServiceUnavailableException {
        SearchParams params = new SearchParams();
        List<Customer> clientes = api.customers().list(params);
        return clientes;
    }
    
    /**TARJETAS:   Metodo para obtener listado de tarjetas por cliente*/
    public List<Card> obtenerListaTarjetaPorCliente(Customer customer) throws ServiceUnavailableException, OpenpayServiceException {
        SearchParams request = new SearchParams();
        List<Card> tarjetas = api.cards().list(customer.getId(), request);
        return tarjetas;
    }
    
    /**********************************CARGOS*************************************/
    /**
     *  Método para poder realizar un cargo a pagar con tarjeta de crédito sin crear una cuenta de cliente, solo asignandole sus datos como referencia-----------------------------
     *  @customer se tiene que recibir a un cliente para obtener su id y poder realizar la transacción
     *  **/
    public Charge CargarCreditoSinCliente(String nombre, String apellido, String telefono, String email,String IdTarjeta,String deviceSessionId, String descripcionCargo, String monto, String idOrden, String moneda) throws OpenpayServiceException, ServiceUnavailableException {
        System.out.println("-- Procesando pago OpenPay/"+statusProject);      
        //crear cliente temporal para ingresar el cargo
        Customer customer = new Customer();
        customer.setName(nombre);
        customer.setLastName(apellido);
        customer.setPhoneNumber(telefono);
        customer.setEmail(email);
        
        CreateCardChargeParams request = new CreateCardChargeParams();
        request.cardId(IdTarjeta);
        request.deviceSessionId(deviceSessionId);
        request.amount(new BigDecimal(monto));
        request.currency(moneda);
        request.description(descripcionCargo);
        
        request.customer(customer);
        
        System.out.println("la url de openpay es: "+ urlApi);
        Charge charge = api.charges().createCharge(request);
        
        return charge;
    }
    
    /**
     *  Método para poder realizar un cargo a pagar con tarjeta de crédito sin crear una cuenta de cliente, solo asignandole sus datos como referencia-----------------------------
     *  @customer se tiene que recibir a un cliente para obtener su id y poder realizar la transacción utilizando 3d secure
     *  **/
    public Charge CargarCreditoSinClienteSecure(String nombre, String apellido, String telefono, String email,String IdTarjeta,String deviceSessionId, String descripcionCargo, String monto, String idOrden, String moneda, String numCard, String urlPago) throws OpenpayServiceException, ServiceUnavailableException {
        System.out.println("-- Procesando pago OpenPay/"+statusProject);
        //crear cliente temporal para ingresar el cargo
        Customer customer = new Customer();
        customer.setName(nombre);
        customer.setLastName(apellido);
        customer.setPhoneNumber(telefono);
        customer.setEmail(email);
        
        CreateCardChargeParams request = new CreateCardChargeParams();
        request.cardId(IdTarjeta);
        request.deviceSessionId(deviceSessionId);
        request.amount(new BigDecimal(monto));
        request.currency(moneda);
        request.description(descripcionCargo);
        
        //configuración para 3d Secure
        request.use3dSecure(true);
        request.redirectUrl(urlPago);
        //request.redirectUrl(dominio+"/paySuccess.html");
        request.customer(customer);
        
        
        Charge charge = api.charges().createCharge(request);
        
        //pruebas
        String url = charge.getPaymentMethod().getUrl();
        System.out.println("La url para redirigirse es: " + url);
        
        return charge;
    }
    
    /**
     *  Método para poder realizar un cargo a pagar con tarjeta de crédito sin crear una cuenta de cliente, solo asignandole sus datos como referencia-----------------------------
     *  @customer se tiene que recibir a un cliente para obtener su id y poder realizar la transacción
     *  **/
    public Charge CargarCreditoconCliente(Customer customer,String IdTarjeta,String deviceSessionId, String descripcionCargo, String monto, String idOrden) throws OpenpayServiceException, ServiceUnavailableException {      
        //crear cliente temporal para ingresar el cargo        
        CreateCardChargeParams request = new CreateCardChargeParams();
        request.cardId(IdTarjeta);
        request.deviceSessionId(deviceSessionId);
        request.amount(new BigDecimal(monto));
        request.currency("MXN");
        request.description(descripcionCargo);
        
        //request.orderId(idOrden);
        request.customer(customer);
        
        Charge charge = api.charges().createCharge(request);
        
        return charge;
    }
    
    public Charge CargarCredito2(Customer customer, String IdTarjeta,String deviceSessionId, String descripcionCargo, String monto, String idOrden) throws OpenpayServiceException, ServiceUnavailableException {
        System.out.println("4");
        OpenpayAPI api = new OpenpayAPI("https://sandbox-api.openpay.mx", privateKey, merchantId);
        //Card tarjeta = api.cards().get("kh9n8fnsbr0nhuvfxx40");
        
        
        CreateCardChargeParams request = new CreateCardChargeParams();
        request.cardId(IdTarjeta);
        request.deviceSessionId(deviceSessionId);
        request.amount(new BigDecimal(monto));
        request.currency("MXN");
        request.description(descripcionCargo);
        
        //request.orderId(idOrden);
        request.customer(customer);
        
        Charge charge = api.charges().createCharge(request);
        return charge;
    }
    /**
     * Método para poder realizar un reembolso de alguna transacción con tarjeta---------------
     * @customer se refiere al cliente que se recibe para poder realizar el reembolso
     * @charge se solicita el cargo de la transaccion realizada
     * */
    public Charge reembolsoTarjetaConCliente(Customer customer, String idCargo, String monto, String descripcion) throws OpenpayServiceException, ServiceUnavailableException {
        Charge refundedCharge = api.charges().refund(customer.getId(), new RefundParams()
                .chargeId(idCargo)
                .amount(new BigDecimal(monto)) //tiene que ser mayor a 0 y menor o igual al monto total de la transacción
                .description(descripcion));
        
        return refundedCharge;
    }
    
    /**
     * Método para poder realizar un reembolso de alguna transacción con tarjeta---------------
     * @customer se refiere al cliente que se recibe para poder realizar el reembolso
     * @charge se solicita el cargo de la transaccion realizada
     * */
    public Charge reembolsoTarjetaSinCliente(String idCargo, String monto, String descripcion) throws OpenpayServiceException, ServiceUnavailableException {
        Charge refundedCharge = api.charges().refund(new RefundParams()
                .chargeId(idCargo)
                .amount(new BigDecimal(monto)) //tiene que ser mayor a 0 y menor o igual al monto total de la transacción
                .description(descripcion));
        
        return refundedCharge;
    }
    
    /**     *  Método para poder realizar un cargo a pagar por transferencia bancaria-----------------------------
     *  @customer se tiene que recibir a un cliente para obtener su id y poder realizar la transacción*/
    public Charge cargoTransferenciaBancaria(Customer customer, String descripcionCargo, String monto, String idOrden) throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge =api.charges().createCharge(customer.getId(), new CreateBankChargeParams()
                .description(descripcionCargo)
                .amount(new BigDecimal(monto))
                .orderId(idOrden));
        
        return charge;
    }
    
    /**Realizar un cargo para pagar en tiendas**/
    public Charge crearCargoPaynet(String usuario, String correo,String descripcion, String monto) throws OpenpayServiceException, ServiceUnavailableException {
        //OpenpayAPI api = new OpenpayAPI("https://sandbox-api.openpay.mx",
          //      "sk_99940185eb48411ab11da6ba0a2c93d2", "mc1tgoh7r8fxgto55yoz");
        
        Customer cliente = new Customer();
        cliente.name(usuario);
        cliente.email(correo);
        
        //Calendar dueDate = Calendar.getInstance();
        //dueDate.set(2023, 11, 23, 10, 10, 0);
        CreateStoreChargeParams request = new CreateStoreChargeParams();
        request.amount(new BigDecimal(monto));
        request.description(descripcion);
        //request.amount(new BigDecimal("100.00"));
        //request.description("Cargo con tienda");
        //request.orderId("oid-00053");
        //request.dueDate(dueDate.getTime());
        request.customer(cliente);
        
        Charge charge = api.charges().create(request);
        
        System.out.println(charge.getDescription());
        //https://sandbox-dashboard.openpay.mx/paynet-pdf/mc1tgoh7r8fxgto55yoz/1010102268004217
       return charge;
    }
    
    
    /**Obtener un cargo*/
    public Charge obtenerCargo(String idCargo) throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = api.charges().get(idCargo);
        return charge;
    }
    /**Obtener un cargo de algun cliente ya registrado*/
    public Charge obtenerCargoPorCliente(Customer customer, String idCargo) throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = api.charges().get(customer.getId(),idCargo);
        return charge;
    }
    
    /****************************************PAGOS*****************************************/
    /** * Método para pago bancario
     * @customer se refiere al cliente que se pide para poder obtener su id y poder realizar la transacción* */
    /*public Payout pagoBancario(Customer customer, String clabe, String propietario, String alias, String monto, String descripcionPago, String idOrden) throws OpenpayServiceException, ServiceUnavailableException {
        OpenpayAPI api = new OpenpayAPI("https://sandbox-api.openpay.mx", "sk_a095e03a571647df86dc8ec12533b4f4", "mc1tgoh7r8fxgto55yoz");
        BankAccount bankAccount = new BankAccount()
                .clabe(clabe)            // CLABE
                .holderName(propietario)
                .alias(alias);       // Optional
        
        Payout payout = api.payouts().create(new CreateBankPayoutParams()
                .bankAccount(bankAccount)
                .amount(new BigDecimal(monto))
                .description(descripcionPago)
                .orderId(idOrden));
        
        return payout;
    }*/
    
    /**      * Método para pago con tarjeta de débito
     * @customer se refiere al cliente que se pide para poder obtener su id y poder realizar la transacción* */
    /*public Payout pagoDebito(Customer customer, String numeroTarjeta, String propietario, String codigoBanco, String monto, String descripcionPago, String idOrden) throws OpenpayServiceException, ServiceUnavailableException {
        OpenpayAPI api2 = new OpenpayAPI("https://sandbox-api.openpay.mx",privateKey,merchantId);
        
        Card card = new Card()
                .cardNumber(numeroTarjeta)         // No dashes or spaces
                .holderName(propietario)
                .bankCode(codigoBanco);

        Payout payout = api2.payouts().create(customer.getId(), new CreateCardPayoutParams()
                .card(card)
                .amount(new BigDecimal(monto))
                .description(descripcionPago)
                .orderId(idOrden));               // Optional transaction identifier
        
        return payout;
    }*/    
    
    /****************************************SUBSCRIPCIONES*****************************************/
    /**Las suscripciones le permiten realizar cargos recurrentes a sus clientes.
     * Método para crear un plan, en donde lo retorna para poder posteriormente subscribir clientes 
     * @throws OpenpayServiceException */
    /*public Plan planSubscripcion(String nombreSubscripcion, String monto) throws OpenpayServiceException, ServiceUnavailableException {
        Plan plan = api.plans().create(new Plan()
                .name(nombreSubscripcion)
                .amount(new BigDecimal(monto))       // Amount is in MXN: monto es en MXN
                .repeatEvery(1, PlanRepeatUnit.MONTH)           
                .retryTimes(100)
                .statusAfterRetry(PlanStatusAfterRetry.UNPAID));
        
        return plan;
    }*/
    
    /**     * Metodo para Subscribir clientes 
     * @customer cliente quien será suscrito
     * @plan, se refiere al plan al cual el cliente será suscrito**/
    /*public Subscription suscribirClientePlan(Customer customer,Plan plan) throws OpenpayServiceException, ServiceUnavailableException {
        Card card = new Card()
                .cardNumber("5555555555554444")         
                .holderName("Juan Pérez Nuñez")
                .cvv2("422")
                .expirationMonth(9)                  
                .expirationYear(14);

        Subscription subscription = api.subscriptions().create(customer.getId(), new Subscription()
                .planId(plan.getId())
                .card(card));      // You can also use withCardId to use a pre-registered card.
        
        return subscription;
    }*/
    
    /**     * Metodo para cancelar una subscripcion al final del periodo actual
     * @subscription, se refiere a la subscripcion la cual se va a cancelar**/
    /*public void cancelarSubscripcionFinPeriodo (Subscription subscription) throws OpenpayServiceException, ServiceUnavailableException {
        subscription.setCancelAtPeriodEnd(true);
        api.subscriptions().update(subscription);
    }*/
    /**     * Metodo para cancelar una subscripcion inmediatamente
     * @subscription, se refiere a la subscripcion la cual se va a cancelar
     * @customer se refiere al cliente relacionado con dicha subscripcion* **/
    /*public void cancelarSubscripcionInmediatamente(Customer customer, Subscription subscription) throws OpenpayServiceException, ServiceUnavailableException {
        api.subscriptions().delete(customer.getId(), subscription.getId());
    }*/
}
