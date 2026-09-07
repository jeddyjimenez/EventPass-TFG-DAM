package com.tfg.eventos.servicio;


import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Service
public class QrService {
  public byte[] generarPng(String texto, int w, int h) {
    try {
      // Genera una matriz QR a partir del texto recibido
      BitMatrix matrix = new MultiFormatWriter().encode(texto, BarcodeFormat.QR_CODE, w, h);
      // Convierte la matriz en una imagen PNG y cierra el stream al terminar
      try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        return out.toByteArray();
      }
    } catch (Exception e) {
      throw new RuntimeException("Error generando QR", e);
    }
  }
}
