package com.example.eventos_mobile;

import android.os.Bundle;
import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PantallaEscanerActivity extends AppCompatActivity {

    private TextView textoEvento;
    private TextView textoResultado;
    private Button botonAbrirCamara;
    private Long idEvento;
    private ServicioApi servicioApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escaner);

        textoEvento = findViewById(R.id.tvEventoSeleccionado);
        textoResultado = findViewById(R.id.tvResultado);
        botonAbrirCamara = findViewById(R.id.btnAbrirCamara);
        // Servicio retrofit para comunicarnos con el backend
        servicioApi = ClienteRetrofit.obtenerServicio();
        // Obtenemos el evento que el validador elige
        idEvento = getIntent().getLongExtra("idEvento", -1L);
        String nombreEvento = getIntent().getStringExtra("nombreEvento");
        textoEvento.setText("Evento: " + (nombreEvento != null ? nombreEvento : ""));
        // Abrimos la camara al pulsar el boton
        botonAbrirCamara.setOnClickListener(v -> abrirEscaner());
    }

    private void abrirEscaner() {
        IntentIntegrator integrador = new IntentIntegrator(this);
        integrador.setPrompt("Escanea el QR");
        integrador.setBeepEnabled(true);
        integrador.setOrientationLocked(false);
        integrador.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        // Recoge el resultado del escaneo
        IntentResult resultado = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (resultado != null) {
            String tokenLeido = resultado.getContents();
            if (tokenLeido == null || tokenLeido.isEmpty()) {
                Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show();
            } else {
                validarQr(tokenLeido); // Enviamos token al backend
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void validarQr(String qrToken) {
        if (idEvento == null || idEvento <= 0) {
            textoResultado.setText("Error: evento no seleccionado");
            textoResultado.setTextColor(Color.RED);
            return;
        }
        // Creamos el objeto con los datos necesaros
        ValidacionRequest request = new ValidacionRequest(qrToken, idEvento);

        servicioApi.validarEntrada(request).enqueue(new Callback<ValidacionResponse>() {
            @Override
            public void onResponse(Call<ValidacionResponse> call, Response<ValidacionResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    textoResultado.setText("Error al validar (" + response.code() + ")");
                    textoResultado.setTextColor(Color.RED);
                    return;
                }
                // Si la entrada es válida lo mostramos
                ValidacionResponse r = response.body();
                if ("valida".equalsIgnoreCase(r.getEstado())) {
                    textoResultado.setText("VALIDA: " + r.getMensaje());
                    textoResultado.setTextColor(Color.parseColor("#2E7D32"));
                } else {
                    textoResultado.setText("NO VÁLIDA: " + r.getMensaje());
                    textoResultado.setTextColor(Color.RED);
                }
            }

            @Override
            public void onFailure(Call<ValidacionResponse> call, Throwable t) {
                textoResultado.setText("Error de red: " + t.getMessage());
                textoResultado.setTextColor(Color.RED);
            }
        });
    }
}
