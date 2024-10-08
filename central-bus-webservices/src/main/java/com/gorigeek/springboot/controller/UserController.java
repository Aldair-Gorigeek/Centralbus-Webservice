package com.gorigeek.springboot.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;

import javax.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.simple.JSONObject;
import com.gorigeek.crypto.ImagenBase64;
import com.gorigeek.springboot.Module;
import com.gorigeek.springboot.entity.Afiliado;
import com.gorigeek.springboot.entity.StatusCuenta;

import com.gorigeek.springboot.entity.TipoUsuario;
import com.gorigeek.springboot.entity.User;
import com.gorigeek.springboot.entity.UserFinal;
import com.gorigeek.springboot.exception.ResourceNotFoundException;
import com.gorigeek.springboot.repository.AfiliadoRepository;
import com.gorigeek.springboot.repository.RegistrarCuentaRepository;
import com.gorigeek.springboot.repository.StatusCuentaRepository;

import com.gorigeek.springboot.repository.TipoUsuarioRepository;
import com.gorigeek.springboot.repository.UserRepository;
import java.time.LocalDate;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TipoUsuarioRepository userTypeRepository;

    // @Autowired
    // private StatusRecordarRepository statusRecordarRepository;

    @Autowired
    private StatusCuentaRepository statusCuentaRepository;

    // @Autowired
    // private TipoRegistroRepository tipoRegistroRepository;

    @Autowired
    private AfiliadoRepository afiliadoRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RegistrarCuentaRepository register;

    // Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    // Get User by id
    @GetMapping("/{id}")
    public User getUserById(@PathVariable(value = "id") long userId) throws IOException {
        User usuario = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Afiliado afiliado = usuario.getIdt_afiliado();
        if (afiliado != null && afiliado.getLogotipo() != null) {
            System.out.println("existe afiliado");
            ImagenBase64 convertirImagen = new ImagenBase64();
            String imgbase64 = convertirImagen.imagenABase64(afiliado.getLogotipo());
            afiliado.setLogotipo(imgbase64);
            usuario.setIdt_afiliado(afiliado);
        }

        return usuario;
    }

    @PostMapping("/email")
    public String enviarMail(@RequestBody JSONObject email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("centralbus2021@gmail.com");
        message.setTo(email.get("email").toString());
        message.setText("BODY");
        message.setSubject("Cambio de contraseña");
        mailSender.send(message);
        System.out.println("Email enviado");
        return "ok";
    }

    /*
     * @PostMapping("/register")
     * public String createUser(@RequestBody JSONObject user) {
     * //Valida el correo
     * Boolean ValiEmail = validateEmail(user.get("email").toString());
     * if(ValiEmail) {
     * //Guarda el afiliado
     * Afiliado afiliado = new Afiliado();
     * afiliado.setNombrelinea(user.get("nombrelinea").toString());
     * afiliado.setRfc(user.get("rfc").toString());
     * afiliado.setBanco(user.get("banco").toString());
     * afiliado.setTitular(user.get("titular").toString());
     * afiliado.setNumerocuenta(user.get("numerocuenta").toString());
     * afiliado = this.afiliadoRepository.save(afiliado);
     * //Guarda el usuario
     * User usuario = new User();
     * usuario.setNombre(user.get("nombre").toString());
     * usuario.setEmail(user.get("email").toString());
     * LocalDate todaysDate = LocalDate.now();
     * System.out.println(todaysDate);
     * usuario.setFechaRegistro(todaysDate.toString());
     * usuario.setPass(BCrypt.hashpw(user.get("pass").toString(),
     * BCrypt.gensalt(10)));
     * usuario.setTelefono(Long.parseLong(user.get("telefono").toString()));
     * usuario.setDireccion(user.get("direccion").toString());
     * TipoUsuario userType =
     * this.checkTipoUsuario(Integer.parseInt(user.get("idc_tipousuario").toString()
     * ));
     * if(userType != null) {
     * usuario.setIdc_tipousuario(userType);
     * usuario.setIdt_afiliado(afiliado);
     * StatusRecordar statusRecordar =
     * this.checkStatusRecordar(Integer.parseInt(user.get("idc_statusrecordar").
     * toString()));
     * if( statusRecordar != null) {
     * usuario.setIdc_statusrecordar(statusRecordar);
     * StatusCuenta statusCuenta = this.checkStatusCuenta(Integer.parseInt(user.get(
     * "c_status_cuenta_idc_status_cuenta").toString()));
     * if(statusCuenta != null) {
     * usuario.setC_status_cuenta_idc_status_cuenta(statusCuenta);
     * TipoRegistro registerType = this.checkTipoRegistro(Integer.parseInt(
     * user.get("c_tiporegistro_idc_tiporegistro").toString()));
     * if( registerType != null) {
     * usuario.setC_tiporegistro_idc_tiporegistro(registerType);
     * usuario = this.userRepository.save(usuario);
     * Afiliado newAfiliado = saveLogo(user.get("logotipo").toString(), afiliado);
     * if (newAfiliado.getLogotipo()!=null) {
     * return "ok";
     * }else {
     * return "error";
     * }
     * }else {
     * return " No se encontró el Tipo de Registro.";
     * }
     * 
     * }else {
     * return "No se encontró el Estatus de la cuenta.";
     * }
     * 
     * }else {
     * return "No se encontró el Estatus de recordar.";
     * }
     * 
     * }else {
     * return "No se encontró el tipo de usuario";
     * }
     * 
     * }else {
     * return "userOcupado";
     * }
     * }
     */

    private TipoUsuario checkTipoUsuario(int id) {
        return this.userTypeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró ningún tipo de usuario con el ID proporcionado: " + id));
    }

    /*
     * private StatusRecordar checkStatusRecordar(int id) {
     * return this.statusRecordarRepository.findById(id).orElseThrow(() -> null);
     * }
     */

    private StatusCuenta checkStatusCuenta(int id) {
        return this.statusCuentaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró ningún estado de cuenta con el ID proporcionado: " + id));
    }

    // private TipoRegistro checkTipoRegistro(int id) {
    // return this.tipoRegistroRepository.findById(id).orElseThrow(() -> null);
    // }

    // Create user
    @PostMapping
    public Long createUser(@RequestBody User user) {
        StatusCuenta statuscuenta = this
                .checkStatusCuenta(user.getC_status_cuenta_idc_status_cuenta().getIdc_statusCuenta());
        TipoUsuario tipousuario = this.checkTipoUsuario(user.getIdc_tipousuario().getIdc_tipousuario());
        Afiliado afiliado = this.afiliadoRepository.findById(user.getIdt_afiliado().getIdt_afiliado()).get();

        user.setC_status_cuenta_idc_status_cuenta(statuscuenta);
        user.setIdc_tipousuario(tipousuario);
        user.setIdt_afiliado(afiliado);

        return this.userRepository.save(user).getIdtUsuariosadmin();
    }

    /*
     * @GetMapping("/validate/{correo}")
     * public String validateUser(@PathVariable ("correo") String correo) {
     * try{
     * User ocupado = this.userRepository.findFirstByEmail(correo);
     * if (ocupado != null) {
     * return "userocupado";
     * }else {
     * return "ok";
     * }
     * }catch(Exception e){
     * return "error";
     * }
     * }
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/saveCode/{correo}")
    public String validateUserChangePass(@PathVariable("correo") String correo) {
        try {
            User user = this.userRepository.findFirstByEmail(correo);
            if (user != null && user.getC_status_cuenta_idc_status_cuenta().getIdc_statusCuenta() == 1
                    && user.getIdc_tipousuario().getIdc_tipousuario() == 3) {
                int value = (int) (10000 * Math.random());
                user.setCodigoVerificacion(value);
                // send email
                JSONObject user2 = new JSONObject();
                user2.put("correo", correo);
                user2.put("nombre", user.getNombre());
                user2.put("apeliido", "prueba");
                user2.put("telefono", user.getTelefono());
                user2.put("code", value);
                sendEmailCode(user2);
                String respose = this.userRepository.save(user).getIdtUsuariosadmin().toString();
                return respose;
            } else {
                return "invalid";
            }
        } catch (Exception e) {
            return "error";
        }
    }

    private void sendEmailCode(JSONObject user) {
        // TODO Auto-generated method stub
        HttpURLConnection conn;
        URL urlapi;
        try {
            urlapi = new URL("http://localhost:3000/sendmail");
            conn = (HttpURLConnection) urlapi.openConnection();
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json;charset=UTF-8");
            conn.setDoOutput(true);
            String data = user.toString();
            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(data.getBytes("UTF-8"));
                wr.flush();
            }
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @GetMapping("/cancelCode/{User}")
    public void cancelCode(@PathVariable("User") Long IdUser) {
        User user = this.userRepository.findByIdtUsuariosadmin(IdUser);
        user.setCodigoVerificacion(null);
        this.userRepository.save(user);
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/ValidateUser")
    public JSONObject login(@RequestBody User user) {
        String contrasenaecriptada = BCrypt.hashpw(user.getPass(), BCrypt.gensalt(10));
        System.out.println("REQUEST PASS sin encrypt: " + user.getPass());
        System.out.println("REQUEST USEREMAIL sin encrypt: " + user.getEmail());
        User userMatch = this.userRepository.findFirstByEmail(user.getEmail());
        JSONObject resp = new JSONObject();
        if (userMatch == null) {
            resp.put("status", "userError");
        } else {
            if (userMatch.getEmail() != null && userMatch.getPass() != null
                    && userMatch.getIdc_tipousuario().getIdc_tipousuario() == 3) {
                System.out.println("REQUEST PASS: " + BCrypt.hashpw(user.getPass(), BCrypt.gensalt(10)));
                System.out.println("USER PASS: " + userMatch.getPass());
                if (BCrypt.checkpw(user.getPass(), userMatch.getPass())) {
                    if (userMatch.getC_status_cuenta_idc_status_cuenta().getIdc_statusCuenta() == 1) {
                        resp.put("status", "ok");
                        resp.put("idt_usuariosadmin", userMatch.getIdtUsuariosadmin());
                        resp.put("nombre", userMatch.getNombre());
                        resp.put("email", userMatch.getEmail());
                        resp.put("idt_afiliado", userMatch.getIdt_afiliado().getIdt_afiliado());
                        resp.put("idc_tipousuario", userMatch.getIdc_tipousuario());
                        resp.put("c_statusCuenta_idc_statusCuenta", userMatch.getC_status_cuenta_idc_status_cuenta());
                    } else {
                        // Cuenta no verificada
                        resp.put("status", "unconfirmed");
                    }

                } else {
                    // Contraseña erronea
                    resp.put("status", "userError");
                }
            } else {
                // Usuario no encontrado
                resp.put("status", "userError");
            }
        }
        System.out.println(resp);
        return resp;
    }

    @SuppressWarnings("unchecked")
    @Module("CentralBus - Login/Validar Credenciales")
    @PostMapping("/ValidateUserApp")
    public JSONObject loginapp(@RequestBody User user) {
        String contrasenaecriptada = BCrypt.hashpw(user.getPass(), BCrypt.gensalt(10));
        // User userMatch = this.userRepository.findFirstByEmail(user.getEmail());
        UserFinal userMatch = this.register.findFirstByEmail(user.getEmail());
        // System.err.println("USERMATCH NAME::: "+userMatch.getNombre());
        JSONObject resp = new JSONObject();
        if (userMatch == null) {
            resp.put("status", "userError");
        } else {
            if (userMatch.getEstatusActivo() == 0) {
                resp.put("status", "userDesactivado");
                resp.put("idt_usuariosadmin", userMatch.getIdtUsuariosfinal());
                resp.put("email", userMatch.getEmail());
                resp.put("nombre", userMatch.getNombre());
            } else {
                if (userMatch.getEmail() != null && userMatch.getPass() != null) {
                    if (BCrypt.checkpw(user.getPass(), userMatch.getPass())) {
                        if (userMatch.getEstatusActivo() == 1) {
                            resp.put("status", "ok");
                            resp.put("idt_usuariosadmin", userMatch.getIdtUsuariosfinal());
                            resp.put("email", userMatch.getEmail());
                            resp.put("nombre", userMatch.getNombre());
                        } else {
                            // Cuenta no verificada
                            resp.put("status", "unconfirmed");
                        }

                    } else {
                        // Contraseña erronea
                        resp.put("status", "userError");
                    }
                } else {
                    // Usuario no encontrado
                    resp.put("status", "userError");
                }
            }
        }
        System.out.println(resp);
        return resp;
    }

    @SuppressWarnings("unchecked")
    @Module("CentralBus - Login/Generar Token")
    @PostMapping("/SaveToken")
    public JSONObject saveToken(@RequestBody UserFinal user) {
        System.out.println("Cambiar el token");
        System.out.println("Email de usuario: " + user.getEmail());
        System.out.println("Token: " + user.getToken());
        this.register.updateTokenWithEmail(user.getToken(), user.getEmail());
        return null;
    }

    /*
     * // Update user
     * 
     * @PutMapping("/{id}")
     * public User updateUser(@RequestBody User user, @PathVariable ("id") long
     * userId) {
     * ///String contrasenaecriptada = BCrypt.hashpw(user.getPass(),
     * BCrypt.gensalt(10));
     * 
     * User existingUser = this.userRepository.findById(userId)
     * .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " +
     * userId));
     * //actualiza el afiliado
     * Afiliado afiliado =
     * this.afiliadoRepository.findById(existingUser.getIdt_afiliado().
     * getIdt_afiliado())
     * .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " +
     * existingUser.getIdt_afiliado().getIdt_afiliado()));
     * afiliado.setNombrelinea(user.getIdt_afiliado().getNombrelinea());
     * afiliado.setRfc(user.getIdt_afiliado().getRfc());
     * afiliado.setBanco(user.getIdt_afiliado().getBanco());
     * afiliado.setTitular(user.getIdt_afiliado().getTitular());
     * afiliado.setNumerocuenta(user.getIdt_afiliado().getNumerocuenta());
     * afiliado = this.afiliadoRepository.save(afiliado);
     * 
     * //System.out.print(contrasenaecriptada);
     * //existingUser.setNombre(user.getNombre());
     * existingUser.setEmail(user.getEmail());
     * //existingUser.setSeed(user.getSeed());
     * existingUser.setTelefono(user.getTelefono());
     * existingUser.setDireccion(user.getDireccion());
     * //existingUser.setIdc_tipousuario(user.getIdc_tipousuario());
     * //existingUser.setIdt_afiliado(user.getIdt_afiliado());
     * //existingUser.setIdc_statusrecordar(user.getIdc_statusrecordar());
     * //existingUser.setC_statusCuenta_idc_statusCuenta(user.
     * getC_statusCuenta_idc_statusCuenta());
     * //existingUser.setC_tiporegistro_idc_tiporegistro(user.
     * getC_tiporegistro_idc_tiporegistro());
     * //existingUser.setCodigoVerificacion(user.getCodigoVerificacion());
     * return this.userRepository.save(existingUser);
     * }
     * 
     */

    /*---Modificar datos de usuario en lista de usuario*/
    @PutMapping("UserList/{id}")
    public User updateUserLista(@RequestBody User user, @PathVariable("id") long userId) {

        User existingUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        TipoUsuario tipousuario = this.userTypeRepository.findById(user.getIdc_tipousuario().getIdc_tipousuario())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + user.getIdc_tipousuario().getIdc_tipousuario()));
        existingUser.setNombre(user.getNombre());
        existingUser.setTelefono(user.getTelefono());
        existingUser.setDireccion(user.getDireccion());
        existingUser.setEmail(user.getEmail());
        existingUser.setIdc_tipousuario(tipousuario);

        // existingUser.setC_statusCuenta_idc_statusCuenta(user.getC_statusCuenta_idc_statusCuenta());
        // existingUser.setC_tiporegistro_idc_tiporegistro(user.getC_tiporegistro_idc_tiporegistro());
        return this.userRepository.save(existingUser);
    }

    /*---Modificar el estado de activo o inactivo de usuario*/
    @PutMapping("UserList/status/{id}")
    public User updateUserStatus(@RequestBody User user, @PathVariable("id") long userId) {

        User existingUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        StatusCuenta status = this.checkStatusCuenta(user.getC_status_cuenta_idc_status_cuenta().getIdc_statusCuenta());

        existingUser.setC_status_cuenta_idc_status_cuenta(status);

        return this.userRepository.save(existingUser);
    }

    // Delete user by id
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") long userId) {
        User existingUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        this.userRepository.delete(existingUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validateCode")
    public String validateCode(@RequestBody User user) {
        user = this.userRepository.findByIdtUsuariosadminAndCodigoVerificacion(user.getIdtUsuariosadmin(),
                user.getCodigoVerificacion());
        if (user != null) {
            return "ok";
        } else {
            return "error";
        }
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody User user) {
        // TRAER EL USUARIO DEL ID
        User usuario = this.userRepository.findByIdtUsuariosadmin(user.getIdtUsuariosadmin());
        if (usuario != null) {
            // GUARDAR LA CONTRASEÑA
            String newPass = BCrypt.hashpw(user.getPass(), BCrypt.gensalt(10));
            usuario.setPass(newPass);
            user = this.userRepository.save(usuario);
            if (user != null) {
                return "ok";
            } else {
                return "error";
            }
        } else {
            return "error";
        }
    }

    private Afiliado saveLogo(String base64, Afiliado afiliado) { /// FUNCIONES DE CREATE USER
        String base64String = base64;
        String[] strings = base64String.split(",");
        String extension;
        switch (strings[0]) {// check image's extension
            case "data:image/jpeg;base64":
                extension = "jpeg";
                break;
            case "data:image/png;base64":
                extension = "png";
                break;
            default:// should write cases for more images types
                extension = "jpg";
                break;
        }
        // convert base64 string to binary data
        byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
        Path path = Paths.get("");

        //
        // ENCRIPTAR ID Y GUARDAR LA IMAGEN ASÍ
        String idUserEncrypt = BCrypt.hashpw(afiliado.getIdt_afiliado().toString(), BCrypt.gensalt(5)).replace("/", "")
                .replace("\\", "");
        String directoryInt = "\\src\\images\\" + idUserEncrypt + "." + extension;

        String directoryName = path.toAbsolutePath().normalize().toString() + directoryInt;
        File file = new File(directoryName);
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        afiliado.setLogotipo(directoryInt);
        return this.afiliadoRepository.save(afiliado);
    }
    /*
     * 
     * public String imagen(String ruta) throws IOException {
     * Path path = Paths.get("");
     * System.out.println(ruta);
     * File file= new
     * File(path.toAbsolutePath().normalize().toString()+ruta.toString());
     * String imgcode=null;
     * if(file.exists()){
     * System.out.println("Existe logotipo");
     * 
     * int lenght = (int)file.length();
     * BufferedInputStream reader = new BufferedInputStream(new
     * FileInputStream(file));
     * byte[] bytes = new byte[lenght];
     * reader.read(bytes, 0, lenght);
     * reader.close();
     * 
     * if(bytes != null){
     * //imgcode = DatatypeConverter.printBase64Binary(bytes);
     * imgcode = new String(Base64.encodeBase64(bytes), "UTF-8");
     * }
     * } else {
     * System.out.println("No existe");
     * }
     * return imgcode;
     * }
     */

    private Boolean validateEmail(String email) { /// FUNCIONES DE CREATE USER
        User ocupado = this.userRepository.findFirstByEmail(email);
        if (ocupado != null) {
            return false;
        } else {
            return true;
        }
    }

}
