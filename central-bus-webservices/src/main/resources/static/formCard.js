let tipoViaje='No asignado';
let origen='No asignado';
let destino = 'No asignado';
let fecha='No asignado';
let noAdulto=0;
let noNino=0;
let noInapam=0;
let total=0;
let moneda = 'MXN';
//let idUser=0;

let descripcion='';

const loading = document.getElementById('loading');




// sistema antifraude
$(document).ready(function () {
	OpenPay.setId('mc1tgoh7r8fxgto55yoz');
    OpenPay.setApiKey('pk_8a121b8f16534872b6302502ad5c8766');
    OpenPay.setSandboxMode(true);
    var deviceSessionId = OpenPay.deviceData.setup($("#payment-form"), $("deviceIdHiddenFieldName"));
    $('#deviceIdHiddenFieldName').val(deviceSessionId);
    console.log("el id es " + deviceSessionId);
    
    //obtener y actualizar datos
    // Obtiene el valor del parámetro 'message'.
    const viaje = getParameterByName('tipoViaje');
    const origenDat = getParameterByName('origen');
    const destinoDat = getParameterByName('destino');
    const fechaDat = getParameterByName('fecha');
    const adulto = getParameterByName('adulto');
    const nino = getParameterByName('nino');
    const inapam = getParameterByName('inapam');
    const totalDat = getParameterByName('total');
    const monedaDat = getParameterByName('moneda');
    //const usuarioDat = getParameterByName('usuario');
    
    // Realiza alguna acción con el valor del mensaje recibido desde Flutter.
    if(viaje && origenDat && destinoDat && fechaDat && adulto && nino && inapam && totalDat) {
		//idUser=usuarioDat;
		if(monedaDat) {
			moneda = monedaDat;
		}
		actualizarDatos(viaje, origenDat, destinoDat, fechaDat, adulto, nino, inapam, totalDat);
	}
	
    
});

function actualizarDatos(viaje, origenDat, destinoDat, fechaDat, adulto, nino, inapam, totalDat) {
	tipoViaje=viaje;
	origen=origenDat;
	destino = destinoDat;
	fecha=fechaDat;
	noAdulto=adulto;
	noNino=nino;
	noInapam=inapam;
	total=totalDat;

}


// Obtiene los parámetros de la URL.
function getParameterByName(name) {
	const urlParams = new URLSearchParams(window.location.search);
	return urlParams.get(name);
}

function enviarRespuesta(datosEnvio) {
	
	console.log(datosEnvio);
	Success.postMessage(datosEnvio);
}

function cerrarVentana() {
	Close.postMessage('close');
}

//funcion para el boton de enter
document.getElementById("cvv2").addEventListener("keyup", function(event) {
  if (event.keyCode === 13) {
    event.preventDefault();
    //alert("Has presionado enter");
    //quitar el enfoque
    $("#pay-button").blur();
    $("#btn-cancelar").blur();
    $("#telefono").blur();
	$("#correo").blur();
	$("#holder_name").blur();
	$("#card_number").blur();
	$("#fechaExp").blur();
	$("#cvv2").blur();
  }
});
//atrapar el evento del boton pagar
$('#pay-button').on('click', function (event) {
    event.preventDefault();
    
    $("#pay-button").prop("disabled", true);
    $("#btn-cancelar").prop("disabled", true);
    
    //desabilitar inputs
    $("#telefono").prop("disabled", true);
    $("#correo").prop("disabled", true);
    $("#holder_name").prop("disabled", true);
    $("#card_number").prop("disabled", true);
    $("#fechaExp").prop("disabled", true);
    $("#cvv2").prop("disabled", true);
    //var validado = validarForm();
    if(validarForm()) {
		loading.classList.toggle("active");
		
		OpenPay.token.extractFormAndCreate($('#payment-form'), success_callbak, error_callbak);
	} else {
		$("#pay-button").prop("disabled", false);
    	$("#btn-cancelar").prop("disabled", false);
    	//desabilitar inputs
	    $("#telefono").prop("disabled", false);
	    $("#correo").prop("disabled", false);
	    $("#holder_name").prop("disabled", false);
	    $("#card_number").prop("disabled", false);
	    $("#fechaExp").prop("disabled", false);
	    $("#cvv2").prop("disabled", false);
	}
});
        
$('#btn-cancelar').on('click', function(event){
	event.preventDefault();
	//window.history.back();
	cerrarVentana();
			
});

var success_callbak = function(response) {
	var token_id = response.data.id;
	$('#token_id').val(token_id);
	//alert('Operación exitosa ');

	// variables para enviar al servidor
            
	var send = {}
    
    
    var dato1 = $('#token_id').val();//id_token
    var dato2 = $('#deviceIdHiddenFieldName').val();//id del dispositivo
    //var dato3 = $('#nombre').val();//Nombre
    var dato3 = $('#holder_name').val();//Nombre
    //var dato4 = $('#apellido').val();//Apellido
    var dato4 = "";//Apellido
    var dato5 = $('#telefono').val();//Telefono
    var dato6 = $('#correo').val();//Correo
    var dato8 = "Pago de viaje ";//descripcion
    var dato9 = total;//monto
    var dato10 = moneda;
    
    //datos de tarjeta agregados con secure
    //retorno numero
	let card = $('#card_number').val();
	let masked = card.slice(-4).padStart(card.length, '*');
	let datoCard = masked.match(/.{1,4}/g).join(' ');
    //
    
    const urlActual = window.location.protocol + "//"+ window.location.host;
            
    send["IdTarjeta"] = dato1;
	send["DeviceSessionId"] = dato2;
	send["Nombre"] = dato3;
	send["Apellidos"] = dato4;
	send["Telefono"] = dato5;
	send["Correo"] = dato6;
	send["Descripcion"] = dato8;
	send["Monto"]= dato9
	send["Moneda"] = dato10;
	
	//datos agregados con secure
	send["numCard"] = datoCard;
	send["urlActual"] = urlActual;
            
			
			
			
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: "/api/openPay/tarjetaSecure",
		//url: "/api/openPay/tarjeta",
		data: JSON.stringify(send),
		dataType: "html",
		success: function(data) {
			
			let dataJson = JSON.parse(data);
			
			const date = new Date();
			const offset = date.getTimezoneOffset();
			date.setMinutes(date.getMinutes() - offset);
			const formattedDate = date.toISOString().replace(/T/, ' ').replace(/\..+/, '');
			//datos modificados recientemente para almacenar por separado la transaccion
			var datosEnvio={};
			datosEnvio['idDevice']= dato2;
			datosEnvio['status']='success';
			datosEnvio['correo']= dato6;
			
			//retorno numero
			//let card = $('#card_number').val();
			//let masked = card.slice(-4).padStart(card.length, '*');
			//let datoCard = masked.match(/.{1,4}/g).join(' ');
			let tipoTarjeta = 1;
			
			//conocer el tipo de tarjeta
			if(card[0] === '4') {
				tipoTarjeta = 1;
			} else if(card[0] === '5') {
				tipoTarjeta = 2;
			}
			
			datosEnvio['idTransaccion'] = dataJson['idTransaccion'];
			datosEnvio['numAutorizacion']= dataJson['numAutorizacion'];
			datosEnvio['numCard']= datoCard;
			datosEnvio['tipoTarjeta']= tipoTarjeta;
			datosEnvio['monto']= dataJson['monto'];
			datosEnvio['nombre']= dato3;
			datosEnvio['url']= dataJson['url'];
			
			//almacenamiento
			localStorage.setItem('idDevice', dato2);
			localStorage.setItem('correo', dato6);
			localStorage.setItem('idTransaccion', dataJson['idTransaccion']);
			localStorage.setItem('numAutorizacion', dataJson['numAutorizacion']);
			localStorage.setItem('numCard', datoCard);
			localStorage.setItem('tipoTarjeta', tipoTarjeta);
			localStorage.setItem('monto', dataJson['monto']);
			localStorage.setItem('nombre', dato3);
			
			//redireccionar a el codigo para 3d secure
			//let url = dataJson['url'];
			//window.location.href = url;
			//
			
			loading.classList.toggle("active");
			//se comenta linea de enviar respuesta para 3d secure
			enviarRespuesta(JSON.stringify(datosEnvio));
			
			//fin datos modificados recientemente para almacenar por separado la transaccion
			//guardarDatos(dataJson['monto'], idUser, formattedDate ,dataJson['numAutorizacion'], 1,dataJson['idTransaccion']);
			//descomentar la linea de arriba para regresar a como estaba antes
			
		},
		error: function(e) {
			var datosEnvio={};
			datosEnvio['idDevice']= dato2;
			datosEnvio['status']='fail';
			loading.classList.toggle("active");
			enviarRespuesta(JSON.stringify(datosEnvio));
			
		}
	});

	console.log(dato1);
    console.log(dato2);
    console.log(dato3);
    console.log(dato4);
    console.log(dato5);
    console.log(dato6);
    console.log(dato8);
    console.log(dato9);
            

};


        
var error_callbak = function(response) {
	var desc = response.data.description != undefined ?
        response.data.description : response.message;
    console.log("El error es: "+ response);
	const titulo = document.getElementById('modalCentralLabel');
            titulo.innerHTML='Ha ocurrido un error';
            const mensaje = document.getElementById('mensajeCentral');
            mensaje.innerHTML=desc;
            
            /***-------------- */
            const myModal = new bootstrap.Modal('#modalCentral', {
			  keyboard: false
			});
			
			modalToggle = document.getElementById('modalCentral');
            myModal.show(modalToggle)
            /***--------------- */
            
            //quitar el enfoque
            $("#pay-button").blur();
            $("#btn-cancelar").blur();
            $("#telefono").blur();
		    $("#correo").blur();
		    $("#holder_name").blur();
		    $("#card_number").blur();
		    $("#fechaExp").blur();
            $("#cvv2").blur();
            
			//bloquear inputs y botones
			loading.classList.toggle("active");
            $("#pay-button").prop("disabled", false);
            $("#btn-cancelar").prop("disabled", false);
            //desabilitar inputs
		    $("#telefono").prop("disabled", false);
		    $("#correo").prop("disabled", false);
		    $("#holder_name").prop("disabled", false);
		    $("#card_number").prop("disabled", false);
		    $("#fechaExp").prop("disabled", false);
		    $("#cvv2").prop("disabled", false);
};
        
function guardarDatos(monto, usuarioFinal, fechaHora,numAutorizacion, statusAprobado,numTransaccion) {
	
	var send = {}
    
    
    var dato1 = monto
    var dato2 = usuarioFinal
    var dato3 = fechaHora 
    var dato4 = numAutorizacion
    var dato5 = statusAprobado
    var dato6 = numTransaccion
            
    send["montoPago"] = dato1;
	send["usuarioFinal"] = dato2;
	send["fechaHora"] = dato3;
	send["numAutorizacion"] = dato4;
	send["statusAprobado"] = dato5;
	send["numTransaccion"] = dato6;
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: "/api/openPay/guardar",
		data: JSON.stringify(send),
		dataType: "html",
		success: function(data) {
			
			var datosEnvio={};
			datosEnvio['idDevice']= dato2;
			datosEnvio['status']='success';
			
			data['idTransaccion'];
			data['numAutorizacion'];
			data['monto'];
			
			datosEnvio['idTransaccion'] = dato6;
			datosEnvio['numAutorizacion']= dato4;
			datosEnvio['monto']= dato1;
			
			loading.classList.toggle("active");
			enviarRespuesta(JSON.stringify(datosEnvio));
			
			
		},
		error: function(e) {
			var datosEnvio={};
			datosEnvio['idDevice']= dato2;
			datosEnvio['status']='fail';
			datosEnvio['idTransaccion']=numTransaccion;
			loading.classList.toggle("active");
			enviarRespuesta(JSON.stringify(datosEnvio));
			
		}
	});
}
        
        
    