package com.gorigeek.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Encrypt {
	 /**
     * Crea la clave de encriptacion usada internamente
     * @param clave Clave que se usara para encriptar
     * @return Clave de encriptacion
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException 
     */
    private static SecretKeySpec crearClave(String clave) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] claveEncriptacion = clave.getBytes("UTF-8");
        
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        
        claveEncriptacion = sha.digest(claveEncriptacion);
        claveEncriptacion = Arrays.copyOf(claveEncriptacion, 16);
        
        SecretKeySpec secretKey = new SecretKeySpec(claveEncriptacion, "AES");

        return secretKey;
    }

    /**
     * Aplica la encriptacion AES a la cadena de texto usando la clave indicada
     * @param datos Cadena a encriptar
     * @param claveSecreta Clave para encriptar
     * @return Información encriptada
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException 
     */
    public static String encriptar(String datos, String claveSecreta) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
    	SecretKeySpec secretKey = Encrypt.crearClave(claveSecreta);
        
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");        
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] datosEncriptar = datos.getBytes("UTF-8");
        byte[] bytesEncriptados = cipher.doFinal(datosEncriptar);
        String encriptado = Base64.getEncoder().encodeToString(bytesEncriptados);
        return encriptado;
    }
    
    public static String encriptarSinDiagonales(String datos, String claveSecreta) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKey = Encrypt.crearClave(claveSecreta);
        
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");        
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] datosEncriptar = datos.getBytes("UTF-8");
        byte[] bytesEncriptados = cipher.doFinal(datosEncriptar);
        String encriptado = Base64.getEncoder().encodeToString(bytesEncriptados) + "-";
        for(int i=0; i<encriptado.length(); i = i + 1) {
            if(encriptado.charAt(i) == '/') {
                if(i == 0) {
                    encriptado = "0" + encriptado.substring(i+1) + i + "-";
                }else {
                    encriptado = encriptado.substring(0, i) + "0" + encriptado.substring(i+1) + i + "-";
                }
                
            }
        }
        return encriptado;
    }

    /**
     * Desencripta la cadena de texto indicada usando la clave de encriptacion
     * @param datosEncriptados Datos encriptados
     * @param claveSecreta Clave de encriptacion
     * @return Informacion desencriptada
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException 
     */
    public static String desencriptar(String datosEncriptados, String claveSecreta) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKey = Encrypt.crearClave(claveSecreta);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] bytesEncriptados = Base64.getDecoder().decode(datosEncriptados);
        byte[] datosDesencriptados = cipher.doFinal(bytesEncriptados);
        String datos = new String(datosDesencriptados);
        
        return datos;
    }
    
    public static String desencriptarSinDiagonales(String datosEncriptados, String claveSecreta) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKey = Encrypt.crearClave(claveSecreta);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        if(datosEncriptados.charAt(24) == '-') {
            int i=24;
            while(i+2<datosEncriptados.length()) {
                String posString = "";
                if(datosEncriptados.charAt(i+2) == '-') {
                    posString = datosEncriptados.charAt(i+1) + "";
                    i = i + 2;
                }else {
                    posString = "" + datosEncriptados.charAt(i+1) + datosEncriptados.charAt(i+2);
                    i = i + 3;
                }
                
                int posInt = Integer.parseInt(posString);
                if(posInt == 0) {
                    datosEncriptados = "/" + datosEncriptados.substring(posInt+1);
                }else {
                    datosEncriptados = datosEncriptados.substring(0, posInt) + "/" + datosEncriptados.substring(posInt+1);
                }
            }
            datosEncriptados = datosEncriptados.substring(0, 24);
        }else {
            System.out.println("Error inesperado, el encriptado esperaba un -");
        }
        
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] bytesEncriptados = Base64.getDecoder().decode(datosEncriptados);
        byte[] datosDesencriptados = cipher.doFinal(bytesEncriptados);
        String datos = new String(datosDesencriptados);
        
        return datos;
    }
    
    
}
