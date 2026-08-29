package com.example.projeto_1_pdm.viewmodel;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projeto_1_pdm.R;
import com.example.projeto_1_pdm.model.AppDatabase;
import com.example.projeto_1_pdm.model.Evento;
import com.example.projeto_1_pdm.model.EventoDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CadastroEventoActivity extends AppCompatActivity {

    private EditText etTitulo, etDescricao, etHorario, etLocal;
    private Button btnSalvar;
    private EventoDao eventoDao;
    private long clubeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_evento);

        etTitulo = findViewById(R.id.et_titulo);
        etDescricao = findViewById(R.id.et_descricao);
        etHorario = findViewById(R.id.et_horario);
        etLocal = findViewById(R.id.et_local);
        btnSalvar = findViewById(R.id.btn_salvar);

        // Obter ID do clube passado pela Intent
        clubeId = getIntent().getLongExtra("CLUBE_ID", -1);
        if (clubeId == -1) {
            Toast.makeText(this, "Erro: clube não identificado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        AppDatabase db = AppDatabase.getInstance(this);
        eventoDao = db.eventoDao();

        btnSalvar.setOnClickListener(v -> salvarEvento());
    }

    private void salvarEvento() {
        String titulo = etTitulo.getText().toString().trim();
        String descricao = etDescricao.getText().toString().trim();
        String horarioStr = etHorario.getText().toString().trim();
        String local = etLocal.getText().toString().trim();

        if (TextUtils.isEmpty(titulo)) {
            etTitulo.setError("Título é obrigatório");
            return;
        }
        if (TextUtils.isEmpty(horarioStr)) {
            etHorario.setError("Horário é obrigatório");
            return;
        }
        if (TextUtils.isEmpty(local)) {
            etLocal.setError("Local é obrigatório");
            return;
        }

        // Converter horário para timestamp (formato esperado: dd/MM/yyyy HH:mm)
        long horarioTimestamp;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            Date data = sdf.parse(horarioStr);
            horarioTimestamp = data.getTime();
        } catch (ParseException e) {
            etHorario.setError("Formato inválido. Use: dd/MM/yyyy HH:mm");
            return;
        }

        Evento evento = new Evento();
        evento.clubeId = clubeId;
        evento.titulo = titulo;
        evento.descricao = descricao.isEmpty() ? null : descricao;
        evento.horario = horarioTimestamp;
        evento.local = local;
        evento.createdAt = System.currentTimeMillis();

        new Thread(() -> {
            long id = eventoDao.inserir(evento);
            runOnUiThread(() -> {
                if (id > 0) {
                    Toast.makeText(CadastroEventoActivity.this, "Evento criado!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CadastroEventoActivity.this, "Erro ao salvar evento", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}