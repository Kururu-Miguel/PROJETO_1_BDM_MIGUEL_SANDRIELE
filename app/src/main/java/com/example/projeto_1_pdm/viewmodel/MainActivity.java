package com.example.projeto_1_pdm.viewmodel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_1_pdm.R;
import com.example.projeto_1_pdm.model.AppDatabase;
import com.example.projeto_1_pdm.model.Clube;
import com.example.projeto_1_pdm.model.ClubeDao;
import com.example.projeto_1_pdm.model.ClubeAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private ClubeAdapter adapter;
    private ClubeDao clubeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);

            // 1. Configurar RecyclerView
            recyclerView = findViewById(R.id.recycler_clubes);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new ClubeAdapter(this);
            recyclerView.setAdapter(adapter);

            // 2. Configurar listener de clique (AGORA COM LOG E TRATAMENTO)
            adapter.setOnItemClickListener(new ClubeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Clube clube) {
                    Log.d(TAG, "Clube clicado: " + clube.nome + " (ID: " + clube.id + ")");
                    if (clube == null || clube.id == 0) {
                        Toast.makeText(MainActivity.this, "Erro: clube inválido", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(MainActivity.this, ClubeActivity.class);
                    intent.putExtra("CLUBE_ID", clube.id);
                    startActivity(intent);
                }
            });

            // 3. Inicializar banco
            AppDatabase db = AppDatabase.getInstance(this);
            clubeDao = db.clubeDao();

            carregarClubes();

            // 4. Botão de adicionar (FAB)
            Button fab = findViewById(R.id.fab_adicionar);
            fab.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, CadastroClubeActivity.class);
                startActivity(intent);
            });

            // 5. Ajustar padding
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

        } catch (Exception e) {
            Log.e(TAG, "ERRO NO ONCREATE", e);
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void carregarClubes() {
        try {
            clubeDao.buscarTodos().observe(this, new Observer<List<Clube>>() {
                @Override
                public void onChanged(List<Clube> clubes) {
                    if (clubes != null) {
                        adapter.setClubes(clubes);
                    } else {
                        adapter.setClubes(new ArrayList<>());
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar clubes", e);
        }
    }
}