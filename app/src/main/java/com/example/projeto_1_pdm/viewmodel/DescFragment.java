package com.example.projeto_1_pdm.viewmodel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.projeto_1_pdm.R;
import com.example.projeto_1_pdm.model.Clube;

public class DescFragment extends Fragment {

    private Clube clube;

    public static DescFragment newInstance(Clube clube) {
        DescFragment fragment = new DescFragment();
        Bundle args = new Bundle();
        args.putSerializable("CLUBE", clube);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clube = (Clube) getArguments().getSerializable("CLUBE");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_desc, container, false);

        if (clube != null) {
            ImageView ivFoto = view.findViewById(R.id.iv_foto_clube);
            TextView tvNome = view.findViewById(R.id.tv_nome);
            TextView tvDescricao = view.findViewById(R.id.tv_descricao);

            tvNome.setText(clube.nome);
            tvDescricao.setText(clube.descricao != null ? clube.descricao : "Sem descrição");

            // Carregar foto (se tiver URL)
            if (clube.fotoUrl != null && !clube.fotoUrl.isEmpty()) {
                Glide.with(this)
                        .load(clube.fotoUrl)
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                        .into(ivFoto);
            } else {
                ivFoto.setImageResource(R.drawable.ic_placeholder);
            }
        }

        return view;
    }
}