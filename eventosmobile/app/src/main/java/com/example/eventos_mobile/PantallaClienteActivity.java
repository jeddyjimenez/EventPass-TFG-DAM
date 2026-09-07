package com.example.eventos_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PantallaClienteActivity extends AppCompatActivity {

    private ListView listaEntradas;
    private ServicioApi servicioApi;
    private List<EntradaCliente> entradas = new ArrayList<>();
    private List<String> textos = new ArrayList<>();
    private ArrayAdapter<String> adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_cliente); // Carga el layout

        listaEntradas = findViewById(R.id.listaEntradasCliente);
        servicioApi = ClienteRetrofit.obtenerServicio();
        // Asignamos el adaptador
        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, textos);
        listaEntradas.setAdapter(adaptador);

        listaEntradas.setOnItemClickListener((parent, view, position, id) -> {
            EntradaCliente entrada = entradas.get(position);
            // Al pulsar en una entrada mostramos QR
            Intent intent = new Intent(PantallaClienteActivity.this, PantallaQrActivity.class);
            intent.putExtra("entradaId", entrada.getId());
            startActivity(intent);
        });

        cargarEntradas();
    }

    private void cargarEntradas() {
        servicioApi.obtenerMisEntradas().enqueue(new Callback<List<EntradaCliente>>() {
            @Override
            public void onResponse(Call<List<EntradaCliente>> call, Response<List<EntradaCliente>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(PantallaClienteActivity.this, "Error al cargar entradas", Toast.LENGTH_SHORT).show();
                    return;
                }

                entradas.clear();
                textos.clear();

                entradas.addAll(response.body());

                for (EntradaCliente entrada : entradas) {
                    textos.add(
                            entrada.getNombreEvento()
                                    + "\nEstado: " + entrada.getEstado()
                                    + " | Pago: " + entrada.getEstadoPago()
                    );
                }

                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<EntradaCliente>> call, Throwable t) {
                Toast.makeText(PantallaClienteActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
