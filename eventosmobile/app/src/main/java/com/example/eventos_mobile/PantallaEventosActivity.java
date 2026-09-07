package com.example.eventos_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eventos_mobile.EventoAsignado;
import com.example.eventos_mobile.ClienteRetrofit;
import com.example.eventos_mobile.ServicioApi;
import java.util.List;
import retrofit2.*;

public class PantallaEventosActivity extends AppCompatActivity {

    private RecyclerView listaEventos;
    private ServicioApi servicioApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        listaEventos = findViewById(R.id.rvEventos);
        listaEventos.setLayoutManager(new LinearLayoutManager(this));

        servicioApi = ClienteRetrofit.obtenerServicio();
        cargarEventos();
    }

    private void cargarEventos() {
        servicioApi.obtenerEventosAsignados().enqueue(new Callback<List<EventoAsignado>>() {
            @Override
            public void onResponse(Call<List<EventoAsignado>> call, Response<List<EventoAsignado>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(PantallaEventosActivity.this, "No se pudieron cargar eventos", Toast.LENGTH_SHORT).show();
                    return;
                }

                AdaptadorEventos adaptador = new AdaptadorEventos(response.body(), evento -> {
                    Intent irEscaner = new Intent(PantallaEventosActivity.this, PantallaEscanerActivity.class);
                    irEscaner.putExtra("idEvento", evento.getId());
                    irEscaner.putExtra("nombreEvento", evento.getNombre());
                    startActivity(irEscaner);
                });

                listaEventos.setAdapter(adaptador);
            }

            @Override
            public void onFailure(Call<List<EventoAsignado>> call, Throwable t) {
                Toast.makeText(PantallaEventosActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
