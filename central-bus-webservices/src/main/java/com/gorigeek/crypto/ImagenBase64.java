package com.gorigeek.crypto;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.tomcat.util.codec.binary.Base64;

public class ImagenBase64 {
    
  /*METODO PARA CARGAR Y CONVERTIR UNA IMAGEN EN BASE64
   * 
   * ruta: El método solicita la ruta en donde se encuentra la imagen almacenada
   * Busca si se encuentra la imagen dentro de la carpeta,
   * si encuentra la imagen lo transforma en base64 para posteriormente retornarla como String
   * */
    public String imagenABase64(String ruta) throws IOException {
        Path path = Paths.get("");
        System.out.println(ruta);
        File file= new File(path.toAbsolutePath().normalize().toString()+ruta.toString());
        String imgcode=null;
        if(file.exists()){
            System.out.println("Existe logotipo seleccionado");
            
            int lenght = (int)file.length();
            BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
            byte[] bytes = new byte[lenght];
            reader.read(bytes, 0, lenght);
            reader.close();
            
            if(bytes != null){
                //imgcode = DatatypeConverter.printBase64Binary(bytes);
                imgcode = new String(Base64.encodeBase64(bytes), "UTF-8");
            }
        } else {
            System.out.println("No existe el logotipo");
        }
        return imgcode;
    }

    
}
