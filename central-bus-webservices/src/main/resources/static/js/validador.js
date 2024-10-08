//var nombre = $('#nombre').val();//Nombre
//let apellido = $('#apellido').val();//Apellido
//let telefono = $('#telefono').val();//Telefono
//let correo = $('#correo').val();//Correo
//let propietario = $('#holder_name').val();//propietario
//let numeroTarjeta = $('#card_number').val();//numero de tarjeta
//let expiracionMes = $('#expiration_month').val();//mes de expiración
//let expiracionYear = $('#expiration_year').val();//año de expiración
//let cvv2 = $('#cvv2').val();//cvv2

//let nombre = document.getElementById('nombre');
//let apellido = document.getElementById('apellido');
let telefono = document.getElementById('telefono');//Telefono
let correo = document.getElementById('correo');//Correo
let propietario = document.getElementById('holder_name');//propietario
let numeroTarjeta = document.getElementById('card_number');//numero de tarjeta
let expiracionMes = document.getElementById('expiration_month');//mes de expiración
let expiracionYear = document.getElementById('expiration_year');//año de expiración
let expiracionFecha = document.getElementById('fechaExp');//fecha exp
let cvv2 = document.getElementById('cvv2');//cvv2


//const nombreRegex=/^[a-zA-ZÀ-ÿ\s]+$/
//const nombreRegex=/^[a-zA-ZÀ-ÿ]+\s[a-zA-ZÀ-ÿ]+$/;
const nombreRegex=/^[a-zA-ZÀ-ÿ]+(\s[a-zA-ZÀ-ÿ]+)+$/;
const telefonoRegex=/^\d{10}$/;
const correoRegex=/^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;
const tarjetaRegex = /^\d{13,16}$/;
//const mesRegex=/^(0?[1-9]|1[0-2])$/;
const mesRegex=/^(0[1-9]|1[0-2])$/;
const yearRegex=/^(0[1-9]|[1-9][0-9])$/;
const cvv2Regex=/^\d{3,4}$/
const fechaRegex=/^(0[1-9]|1[0-2])\/\d{2}$/

//const mensajeNombre= document.getElementById('nombreMensaje');


function validarForm() {
	let validado = false;
	//console.log("el nombre es: "+nombre.value);
	if(telefono.value =="" 
	&& correo.value =="" && propietario.value==""&&numeroTarjeta.value==""&&expiracionMes.value==""&&expiracionYear.value==""&&cvv2.value=="") {
		mostrarAlerta("Por favor, rellena todos los campos.", "CAMPOS VACÍOS");
		validado=false;
	} else {
		//let validarNombre=validarElemento(nombre.value, nombreRegex, 'nombreMensaje',"Ingrese un nombre válido");
		//let validarApellido=validarElemento(apellido.value, nombreRegex, 'apellidoMensaje', "Ingrese un apellido válido");
		
		
		//let validarNom=validarElemento(nombre.value, nombreRegex);
		
		let validarTelefono=validarElemento(telefono.value, telefonoRegex, 'telefonoMensaje', "El numero telefónico debe contener 10 digitos");
		let validarCorreo=validarElemento(correo.value, correoRegex, 'correoMensaje',"Ingrese un correo válido");
		let validarPropietario=validarElemento(propietario.value.trim(), nombreRegex, 'propietarioMensaje',"Por favor, Ingrese un nombre y un apellido válido");
		//let validarPropietario=validarElemento(propietario.value, nombreRegex, 'propietarioMensaje',"Ingrese un nombre válido");
		let validarTarjeta=validarElemento(numeroTarjeta.value, tarjetaRegex, 'tarjetaMensaje',"Ingrese una tarjeta válida");
		//let validarMes=validarElemento(expiracionMes.value, mesRegex, 'mesMensaje',"Ingrese un mes válido");
		//let validarYear=validarElemento(expiracionYear.value, yearRegex, 'yearMensaje',"Ingrese un año válido");
		let validarMes=validarElemento(expiracionMes.value, mesRegex, 'fechaMensaje',"Ingrese un mes válido");
		let validarYear=validarElemento(expiracionYear.value, yearRegex, 'fechaMensaje',"Ingrese un año válido");
		let validarCvv2=validarElemento(cvv2.value, cvv2Regex, 'cvv2Mensaje',"El cvv deberá de ser de 3 o 4 dígitos");
		//nuevo elemento
		let validarFecha=validarElemento(expiracionFecha.value, fechaRegex, "fechaMensaje","Ingrese un dia y mes válido");
		
		if(validarTelefono==false) {
			mostrarAlerta("El número telefónico debe contener 10 digitos.", "TELÉFONO INCORRECTO");
		} else {
			if(validarCorreo==false) {
				mostrarAlerta("Ingrese un correo válido.", "CORREO INCORRECTO");
			}else {
				if(validarPropietario==false) {
					//mostrarAlerta("Ingrese un nombre válido", "NOMBRE INCORRECTO");
					mostrarAlerta("Ingrese un nombre y un apellido válido.", "NOMBRE INCORRECTO");
				}else {
					if(validarTarjeta==false) {
						//mostrarAlerta("El número de tarjeta no debe de exceder los 16 digitos", "TARJETA INCORRECTA");
						mostrarAlerta("El número de tarjeta debe contener mínimo 13 dígitos y no debe de exceder los 16 digitos.", "TARJETA INCORRECTA");
					}else {
						if(validarFecha == false) {
							mostrarAlerta("Formato de fecha inválida Ejem. 01-2024", "FECHA INCORRECTA");
						}else {
							
							if(validarMes == false) {
							mostrarAlerta("Formato de mes inválido Ejem. 01", "FECHA INCORRECTA");
							}else {
								if(validarYear==false) {
									mostrarAlerta("Formato de año inválido Ejem. 01", "FECHA INCORRECTA");
								} else {
									if(validarCvv2==false) {
										mostrarAlerta("El cvv deberá de ser de 3 o 4 dígitos.", "CVV INCORRECTO");
									}else {
										if(validarTelefono&&validarCorreo &&validarPropietario&&validarTarjeta&&validarMes&&validarYear&&validarCvv2) {
											validado=true;
										}	
									}	
								}
								
							}
						}
						
						
					}
				}
				
			}
			
			
		}
		
		
	}
	return validado;
}

function validarElemento(elemento, regex, idMensaje, texto){
	validado= false;
	const mensajeAlerta= document.getElementById(idMensaje);
	mensajeAlerta.classList.remove("activo");
	//idMensaje.classList.toggle("activo");
	if(elemento=="") {
		//mensajeAlerta.classList.toggle("activo");
		mensajeAlerta.classList.add("activo");
		//alert("El elemento está vacío");
	} else {
		if(elemento.match(regex)) {
			validado=true;
			console.log("correcto");
		}else {
			//alert("No coincide");
			mensajeAlerta.innerHTML="<p>"+texto+"</p>";
			//mensajeAlerta.classList.toggle("activo");
			mensajeAlerta.classList.add("activo");
		}
	}
	return validado;
}

function mostrarAlerta(textoMensaje, tituloMensaje) {
	const titulo = document.getElementById('modalCentralLabel');
            titulo.innerHTML=tituloMensaje;
            const mensaje = document.getElementById('mensajeCentral');
            mensaje.innerHTML=textoMensaje;
            
            const myModal = new bootstrap.Modal('#modalCentral', {
			  keyboard: false
			});
            
            modalToggle = document.getElementById('modalCentral');
            myModal.show(modalToggle)
			
			//loading.classList.toggle("active");
            $("#pay-button").prop("disabled", false);
            $("#btn-cancelar").prop("disabled", false);
}

//funciones para limitar caracteres
telefono.addEventListener('input', function() {
    if (this.value.length > 10) {
        this.value = this.value.slice(0,10); 
    }
});

numeroTarjeta.addEventListener('input', function() {
    if (this.value.length > 16) {
        this.value = this.value.slice(0,16); 
    }
});

cvv2.addEventListener('input', function() {
    if (this.value.length > 4) {
        this.value = this.value.slice(0,4); 
    }
});