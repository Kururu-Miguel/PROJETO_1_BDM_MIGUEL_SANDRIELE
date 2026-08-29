package com.example.projeto_1_pdm.viewmodel;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.projeto_1_pdm.R;
import com.example.projeto_1_pdm.model.AppDatabase;
import com.example.projeto_1_pdm.model.Clube;
import com.example.projeto_1_pdm.model.ClubeDao;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ClubeActivity extends AppCompatActivity {

    private static final String TAG = "ClubeActivity";
    private BottomNavigationView bottomNavigationView;
    private ClubeDao clubeDao;
    private long clubeId;
    private Clube clubeAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clube);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // 1. Obter ID do clube
        clubeId = getIntent().getLongExtra("CLUBE_ID", -1);
        if (clubeId == -1) {
            Toast.makeText(this, "Erro: clube não identificado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 2. Inicializar banco
        AppDatabase db = AppDatabase.getInstance(this);
        clubeDao = db.clubeDao();

        // 3. Carregar clube em background
        new Thread(() -> {
            clubeAtual = clubeDao.buscarPorId(clubeId);
            runOnUiThread(() -> {
                if (clubeAtual == null) {
                    Toast.makeText(this, "Clube não encontrado", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                exibirFragmentoInicial();
            });
        }).start();

        // 4. Navegação inferior
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.descricao) {
                selectedFragment = DescFragment.newInstance(clubeAtual);
            } else if (item.getItemId() == R.id.eventos) {
                selectedFragment = EventFragment.newInstance(clubeAtual);
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.tela, selectedFragment)
                        .commit();
            }
            return true;
        });
    }

    private void exibirFragmentoInicial() {
        Fragment fragment = DescFragment.newInstance(clubeAtual);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.tela, fragment)
                .commit();
        bottomNavigationView.setSelectedItemId(R.id.descricao);
    }
}