package com.example.eventos_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.eventos_mobile.ClienteRetrofit;
import com.example.eventos_mobile.ServicioApi;
import okhttp3.ResponseBody;
import retrofit2.*;

public class PantallaLoginActivity extends AppCompatActivity {

    private EditText campoEmail;
    private EditText campoContrasena;
    private Button botonEntrar;
    private ServicioApi servicioApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoEmail = findViewById(R.id.etEmail);
        campoContrasena = findViewById(R.id.etPass);
        botonEntrar = findViewById(R.id.btnLogin);

        servicioApi = ClienteRetrofit.obtenerServicio();

        botonEntrar.setOnClickListener(v -> hacerLogin());
    }

    private void hacerLogin() {
        String email = campoEmail.getText().toString().trim();
        String contrasena = campoContrasena.getText().toString().trim();

        if (email.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Rellena email y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        servicioApi.iniciarSesion(email, contrasena).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() || response.code() == 302) {
                    comprobarTipoUsuario();
                } else {
                    Toast.makeText(PantallaLoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("LOGIN_API", "Fallo login", t);
                Toast.makeText(PantallaLoginActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Decide a que pantalla debe ir el usuario según su tipo
    private void comprobarTipoUsuario() {
        servicioApi.obtenerEventosAsignados().enqueue(new retrofit2.Callback<java.util.List<EventoAsignado>>() {
            @Override
            public void onResponse(retrofit2.Call<java.util.List<EventoAsignado>> call,
                                   retrofit2.Response<java.util.List<EventoAsignado>> response) {

                if (response.isSuccessful()) {
                    Intent irValidador = new Intent(PantallaLoginActivity.this, PantallaEventosActivity.class);
                    startActivity(irValidador);
                    finish();
                } else {
                    Intent irCliente = new Intent(PantallaLoginActivity.this, PantallaClienteActivity.class);
                    startActivity(irCliente);
                    finish();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<java.util.List<EventoAsignado>> call, Throwable t) {
                Intent irCliente = new Intent(PantallaLoginActivity.this, PantallaClienteActivity.class);
                startActivity(irCliente);
                finish();
            }
        });
    }
}
