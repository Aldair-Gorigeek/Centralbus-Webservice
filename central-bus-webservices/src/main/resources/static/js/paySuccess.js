const id = getParameterByName('id');
console.log('el id obtenido es: ' + id);

const idDevice = localStorage.getItem('idDevice');
const correo = localStorage.getItem('correo');
const idTransaccion = localStorage.getItem('idTransaccion');
let numAutorizacion = localStorage.getItem('numAutorizacion');
const numCard = localStorage.getItem('numCard');
const tipoTarjeta = localStorage.getItem('tipoTarjeta');
const monto = localStorage.getItem('monto');
const nombre = localStorage.getItem('nombre');

//datos para enviar a flutter
var datosEnvio={};
datosEnvio['idDevice']= idDevice;
datosEnvio['status']='success';
datosEnvio['correo']= correo;

datosEnvio['idTransaccion'] = idTransaccion;

datosEnvio['numCard']= numCard;
datosEnvio['tipoTarjeta']= parseInt(tipoTarjeta);
datosEnvio['monto']= monto;
datosEnvio['nombre']= nombre;


//solicitar datos de cargo
$.ajax({
		type: "GET",
		contentType: "application/json",
		url: "/api/openPay/getCargo/"+id,
		dataType: "html",
		success: function(data) {
			
			let dataJson = JSON.parse(data);
			
			console.log('los datos obtenidos son: '+ data);
			numAutorizacion = dataJson['numAutorizacion'];
			let status = dataJson['status'];
			datosEnvio['numAutorizacion']= numAutorizacion;
			datosEnvio['status']= status;
			
			//envio de informacion a la app
			enviarRespuesta(JSON.stringify(datosEnvio));
			
		},
		error: function(e) {
			//var datosEnvio={};
			//datosEnvio['idDevice']= dato2;
			datosEnvio['status']='fail';
			enviarRespuesta(JSON.stringify(datosEnvio));
			console.log('error '+ e);
		}
});




//FUNCIONES PARA MANEJAR LOS DATOS

// Obtiene los parámetros de la URL.
function getParameterByName(name) {
	const urlParams = new URLSearchParams(window.location.search);
	return urlParams.get(name);
}

//funcion para enviar respuesta a flutter
function enviarRespuesta(datosEnvio) {
	console.log(datosEnvio);
	Success.postMessage(datosEnvio);
}

//funcion para cerar la ventana
function cerrarVentana() {
	Close.postMessage('close');
}
