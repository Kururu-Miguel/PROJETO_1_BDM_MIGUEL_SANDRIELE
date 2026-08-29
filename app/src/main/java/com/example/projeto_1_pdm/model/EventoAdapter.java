package com.example.projeto_1_pdm.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projeto_1_pdm.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {

    private List<Evento> eventos = new ArrayList<>();
    private Context context;

    public EventoAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_evento, parent, false);
        return new EventoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder holder, int position) {
        Evento evento = eventos.get(position);

        holder.tvTitulo.setText(evento.titulo);

        // Formatar horário
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String data = sdf.format(new Date(evento.horario));
        holder.tvHorarioLocal.setText(data + " - " + evento.local);
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public void setEventos(List<Evento> novosEventos) {
        this.eventos.clear();
        this.eventos.addAll(novosEventos);
        notifyDataSetChanged();
    }

    static class EventoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvHorarioLocal;

        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tv_titulo);
            tvHorarioLocal = itemView.findViewById(R.id.tv_horario_local);
        }
    }
}