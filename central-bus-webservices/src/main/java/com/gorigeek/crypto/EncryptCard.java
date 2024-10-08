package com.gorigeek.crypto;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
public class EncryptCard {

    /*public static void main(String[] args) {
        try {
                // Generar una clave secreta para AES
                SecretKey secretKey = generarClaveSecreta();

                // Número de tarjeta bancaria a encriptar
                String numeroTarjeta = "1234567890123456";

                // Encriptar el número de tarjeta
                String numeroTarjetaEncriptado = encriptar(numeroTarjeta, secretKey);

                System.out.println("Número de tarjeta original: " + numeroTarjeta);
                System.out.println("Número de tarjeta encriptado: " + numeroTarjetaEncriptado);

                // Desencriptar el número de tarjeta
                String numeroTarjetaDesencriptado = desencriptar(numeroTarjetaEncriptado, secretKey);

                System.out.println("Número de tarjeta desencriptado: " + numeroTarjetaDesencriptado);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        public static SecretKey generarClaveSecreta() throws Exception {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256); // Tamaño de la clave en bits (128, 192 o 256)
            return keyGenerator.generateKey();
        }

        public static String encriptar(String textoPlano, Key clave) throws Exception {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, clave);
            byte[] textoCifrado = cipher.doFinal(textoPlano.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(textoCifrado);
        }

        public static String desencriptar(String textoCifrado, Key clave) throws Exception {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, clave);
            byte[] textoPlano = cipher.doFinal(Base64.getDecoder().decode(textoCifrado));
            return new String(textoPlano, StandardCharsets.UTF_8);
        }
    



}
