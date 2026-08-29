package com.example.projeto_1_pdm.viewmodel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projeto_1_pdm.R;
import com.example.projeto_1_pdm.model.AppDatabase;
import com.example.projeto_1_pdm.model.Clube;
import com.example.projeto_1_pdm.model.Evento;
import com.example.projeto_1_pdm.model.EventoAdapter;
import com.example.projeto_1_pdm.model.EventoDao;
import java.util.List;

public class EventFragment extends Fragment {

    private Clube clube;
    private RecyclerView recyclerView;
    private EventoAdapter adapter;
    private EventoDao eventoDao;

    public static EventFragment newInstance(Clube clube) {
        EventFragment fragment = new EventFragment();
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
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        // Configurar título
        TextView tvEventos = view.findViewById(R.id.tv_eventos_titulo);
        if (clube != null) {
            tvEventos.setText("Eventos do clube: " + clube.nome);
        }

        // Configurar RecyclerView
        recyclerView = view.findViewById(R.id.recycler_eventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EventoAdapter(getContext());
        recyclerView.setAdapter(adapter);

        // Configurar botão (FAB)
        Button fab = view.findViewById(R.id.fab_adicionar2);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CadastroEventoActivity.class);
            if (clube != null) {
                intent.putExtra("CLUBE_ID", clube.id);
            }
            startActivity(intent);
        });

        // Carregar eventos do banco
        carregarEventos();

        return view;
    }

    private void carregarEventos() {
        if (clube == null) return;

        AppDatabase db = AppDatabase.getInstance(getContext());
        eventoDao = db.eventoDao();

        // Usar LiveData para atualizar automaticamente quando o banco mudar
        eventoDao.buscarEventosDoClube(clube.id).observe(getViewLifecycleOwner(), new Observer<List<Evento>>() {
            @Override
            public void onChanged(List<Evento> eventos) {
                adapter.setEventos(eventos);
            }
        });
    }
}