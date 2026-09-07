package com.tfg.eventos.servicio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class SupabaseStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.service-role-key}")
    private String serviceRoleKey;

    @Value("${supabase.storage.bucket}")
    private String bucket;

    public String subirImagen(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            return null;
        }

        // Se conserva la extensión para que la imagen se guarde correctamente
        String extension = "";
        String originalName = archivo.getOriginalFilename();
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf('.'));
        }

        // Se genera un nombre único para evitar colisiones en el bucket
        String nombreArchivo = UUID.randomUUID() + extension;
        String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucket + "/" + nombreArchivo;

        try {
            // Se crea un cliente local para que las conexiones HTTP se liberen al terminar
            RestClient restClient = RestClient.create();
            // Se sube el archivo al bucket de Supabase Storage
            restClient.post()
                    .uri(uploadUrl)
                    .header("Authorization", "Bearer " + serviceRoleKey)
                    .header("apikey", serviceRoleKey)
                    .contentType(MediaType.parseMediaType(
                            archivo.getContentType() != null ? archivo.getContentType() : "image/jpeg"))
                    .body(archivo.getBytes())
                    .retrieve()
                    .toBodilessEntity();

            // Devuelve la URL pública que luego se guardará en el evento
            return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + nombreArchivo;

        } catch (IOException e) {
            throw new RuntimeException("No se pudo subir la imagen a Supabase Storage", e);
        }
    }
}
