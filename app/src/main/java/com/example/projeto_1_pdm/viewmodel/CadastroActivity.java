package com.example.projeto_1_pdm.viewmodel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto_1_pdm.R;
import com.example.projeto_1_pdm.helper.FirebaseHelper;
import com.google.firebase.auth.FirebaseAuth;

public class CadastroActivity extends AppCompatActivity {

    private EditText edit_nome;
    private EditText edit_email;
    private EditText edit_senha;
    private Button button_cadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);
        iniciarComponentes();
        configClique();
    }

    protected void iniciarComponentes() {
        edit_nome = findViewById(R.id.editTextText3);
        edit_email = findViewById(R.id.editTextTextEmailAddress4);
        edit_senha = findViewById(R.id.editTextTextPassword3);
        button_cadastrar = findViewById(R.id.button4);
    }

    protected void cadastrar(String nome, String email, String senha) {
        FirebaseAuth auth = FirebaseHelper.getAuth();
        auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        startActivity(new Intent(this, ClubeActivity.class));
                    } else {
                        Toast.makeText(this, "Deu errado", Toast.LENGTH_LONG).show();
                    }
        });
    }

    protected void validarDados() {
        String nome = edit_nome.getText().toString().trim();
        String email = edit_email.getText().toString().trim();
        String senha = edit_senha.getText().toString().trim();

        if (!nome.isEmpty()) {
            if (!email.isEmpty()) {
                if (!senha.isEmpty()) {
                    cadastrar(nome, email, senha);
                } else {
                    edit_senha.requestFocus();
                    edit_senha.setError("Digite sua senha.");
                }
            } else {
                edit_senha.requestFocus();
                edit_email.setError("Digite seu e-mail.");
            }
        } else {
            edit_nome.requestFocus();
            edit_nome.setError("Digite seu nome.");
        }
    }

    protected void configClique() {
        button_cadastrar.setOnClickListener( view -> validarDados());
    }
}
