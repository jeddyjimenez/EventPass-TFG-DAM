package com.example.eventos_mobile;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eventos_mobile.EventoAsignado;
import java.util.List;

public class AdaptadorEventos extends RecyclerView.Adapter<AdaptadorEventos.VistaEvento> {

    public interface AlPulsarEvento {
        void alPulsar(EventoAsignado evento);
    }

    private final List<EventoAsignado> eventos;
    private final AlPulsarEvento listener;

    public AdaptadorEventos(List<EventoAsignado> eventos, AlPulsarEvento listener) {
        this.eventos = eventos;
        this.listener = listener;
    }

    @NonNull
    @Override // Le decimos como mostrar la información mediante el item_evento
    public VistaEvento onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evento, parent, false);
        return new VistaEvento(vista);
    }

    @Override // Relacionamos la información mostrada con los datos reales
    public void onBindViewHolder(@NonNull VistaEvento holder, int position) {
        EventoAsignado evento = eventos.get(position);
        holder.textoNombre.setText(evento.getNombre());
        holder.textoInfo.setText(evento.getUbicacion() + " • " + evento.getFechaInicio());

        holder.itemView.setOnClickListener(v -> listener.alPulsar(evento));
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    static class VistaEvento extends RecyclerView.ViewHolder {
        TextView textoNombre;
        TextView textoInfo;

        VistaEvento(@NonNull View itemView) {
            super(itemView);
            textoNombre = itemView.findViewById(R.id.tvNombre);
            textoInfo = itemView.findViewById(R.id.tvSub);
        }
    }
}
