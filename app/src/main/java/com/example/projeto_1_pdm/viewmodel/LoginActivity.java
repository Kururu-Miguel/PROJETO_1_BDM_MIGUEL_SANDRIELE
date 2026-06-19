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

public class LoginActivity extends AppCompatActivity {

    private EditText edit_email, edit_senha;
    private Button button_registrar, button_logar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        iniciarComponentes();
        configClique();
    }

    protected void iniciarComponentes() {
        edit_email = findViewById(R.id.editTextTextEmailAddress);
        edit_senha = findViewById(R.id.editTextTextPassword);
        button_registrar = findViewById(R.id.button);
        button_logar = findViewById(R.id.button2);
    }

    protected void configClique() {
        button_registrar.setOnClickListener( view -> startActivity(new Intent(this, CadastroActivity.class)));
        button_logar.setOnClickListener(view-> validarDados());
    }

    protected void logar(String email, String senha) {
        FirebaseAuth auth = FirebaseHelper.getAuth();
        auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        startActivity(new Intent(this, ClubeActivity.class));
                    } else {
                        Toast.makeText(this, "Deu errado", Toast.LENGTH_LONG).show();
                    }
                });
    }

    protected void validarDados() {
        String email = edit_email.getText().toString().trim();
        String senha = edit_senha.getText().toString().trim();

        if (!email.isEmpty()) {
            if (!senha.isEmpty()) {
                logar(email, senha);
            } else {
                edit_senha.requestFocus();
                edit_senha.setError("Digite sua senha.");
            }
        } else {
            edit_senha.requestFocus();
            edit_email.setError("Digite seu e-mail.");
        }
    }

}
