package com.example.eventos_mobile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PantallaQrActivity extends AppCompatActivity {

    private ImageView imagenQr;
    private ServicioApi servicioApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_qr);

        imagenQr = findViewById(R.id.imagenQr);
        servicioApi = ClienteRetrofit.obtenerServicio();

        Long entradaId = getIntent().getLongExtra("entradaId", -1L);

        if (entradaId == -1L) {
            Toast.makeText(this, "Entrada no válida", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarQr(entradaId);
    }

    private void cargarQr(Long entradaId) {
        servicioApi.obtenerQr(entradaId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(PantallaQrActivity.this, "Error al cargar QR: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imagenQr.setImageBitmap(bitmap);
                } catch (Exception e) {
                    Toast.makeText(PantallaQrActivity.this, "Error mostrando QR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PantallaQrActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
