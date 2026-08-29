package com.example.projeto_1_pdm.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projeto_1_pdm.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClubeAdapter extends RecyclerView.Adapter<ClubeAdapter.ClubeViewHolder> {

    private List<Clube> clubes = new ArrayList<>();
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Clube clube);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ClubeAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ClubeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista, parent, false);
        return new ClubeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClubeViewHolder holder, int position) {
        Clube clube = clubes.get(position);

        holder.tvNome.setText(clube.nome);

        // Carregar imagem (URL ou arquivo local)
        if (clube.fotoUrl != null && !clube.fotoUrl.isEmpty()) {
            if (clube.fotoUrl.startsWith("http")) {
                Glide.with(context)
                        .load(clube.fotoUrl)
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                        .into(holder.ivFoto);
            } else {
                Glide.with(context)
                        .load(new File(clube.fotoUrl))
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                        .into(holder.ivFoto);
            }
        } else {
            holder.ivFoto.setImageResource(R.drawable.ic_placeholder);
        }

        // 🔥 Configurar clique no item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(clube);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clubes.size();
    }

    public void setClubes(List<Clube> novosClubes) {
        this.clubes.clear();
        if (novosClubes != null) {
            this.clubes.addAll(novosClubes);
        }
        notifyDataSetChanged();
    }

    static class ClubeViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoto;
        TextView tvNome;

        public ClubeViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoto = itemView.findViewById(R.id.iv_foto);
            tvNome = itemView.findViewById(R.id.tv_nome);
        }
    }
}