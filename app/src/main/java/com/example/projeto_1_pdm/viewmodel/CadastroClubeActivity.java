package com.example.projeto_1_pdm.viewmodel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.projeto_1_pdm.R;
import com.example.projeto_1_pdm.model.AppDatabase;
import com.example.projeto_1_pdm.model.Clube;
import com.example.projeto_1_pdm.model.ClubeDao;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class CadastroClubeActivity extends AppCompatActivity {

    private EditText etNome, etDescricao;
    private Button btnSalvar, btnEscolherFoto;
    private ImageView ivFotoClube;
    private ClubeDao clubeDao;
    private String selectedImagePath = null; // Caminho da imagem salva

    // Launcher para abrir a galeria e receber o resultado
    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Uri selectedImageUri = result.getData().getData();
                            if (selectedImageUri != null) {
                                // Salva a imagem no armazenamento interno do app
                                String savedPath = saveImageToInternalStorage(selectedImageUri);
                                if (savedPath != null) {
                                    selectedImagePath = savedPath;
                                    // Exibe a imagem no ImageView
                                    ivFotoClube.setImageURI(selectedImageUri);
                                    Toast.makeText(this, "Foto selecionada!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(this, "Erro ao salvar imagem", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_clube);

        etNome = findViewById(R.id.et_nome);
        etDescricao = findViewById(R.id.et_descricao);
        btnSalvar = findViewById(R.id.btn_salvar);
        btnEscolherFoto = findViewById(R.id.btn_escolher_foto);
        ivFotoClube = findViewById(R.id.iv_foto_clube);

        AppDatabase db = AppDatabase.getInstance(this);
        clubeDao = db.clubeDao();

        // Botão para abrir a galeria
        btnEscolherFoto.setOnClickListener(v -> openImagePicker());

        // Botão Salvar
        btnSalvar.setOnClickListener(v -> salvarClube());
    }

    // Abrir a galeria (com verificação de permissão)
    private void openImagePicker() {
        // Verifica permissão para Android 13+ (READ_MEDIA_IMAGES) ou versões antigas (READ_EXTERNAL_STORAGE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 100);
                return;
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                return;
            }
        }

        // Se já tem permissão, abre a galeria
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    // Salvar imagem no armazenamento interno do app
    private String saveImageToInternalStorage(Uri imageUri) {
        try {
            // Gera um nome único para o arquivo
            String fileName = "clube_" + System.currentTimeMillis() + ".jpg";
            File storageDir = getFilesDir(); // Diretório interno do app
            File imageFile = new File(storageDir, fileName);

            // Copia o conteúdo da URI para o arquivo interno
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            FileOutputStream outputStream = new FileOutputStream(imageFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return imageFile.getAbsolutePath(); // Retorna o caminho absoluto
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Resultado da solicitação de permissão
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker(); // Tenta abrir novamente após a permissão ser concedida
            } else {
                Toast.makeText(this, "Permissão negada para acessar fotos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void salvarClube() {
        String nome = etNome.getText().toString().trim();
        String descricao = etDescricao.getText().toString().trim();

        if (nome.isEmpty()) {
            etNome.setError("Nome é obrigatório");
            return;
        }

        Clube clube = new Clube();
        clube.nome = nome;
        clube.descricao = descricao;
        clube.fotoUrl = selectedImagePath; // Salva o caminho da imagem
        clube.criadoPorId = "usuario_temp";
        clube.createdAt = System.currentTimeMillis();

        new Thread(() -> {
            long id = clubeDao.inserir(clube);
            runOnUiThread(() -> {
                if (id > 0) {
                    Toast.makeText(CadastroClubeActivity.this, "Clube criado!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CadastroClubeActivity.this, "Erro ao salvar", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}