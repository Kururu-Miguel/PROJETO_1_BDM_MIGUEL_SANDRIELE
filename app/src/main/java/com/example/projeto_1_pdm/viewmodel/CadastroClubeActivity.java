package com.example.projeto_1_pdm.viewmodel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

    private static final String TAG = "CadastroClube";
    private EditText etNome, etDescricao;
    private Button btnSalvar, btnEscolherFoto;
    private ImageView ivFotoClube;
    private ClubeDao clubeDao;
    private String selectedImagePath = null;

    // Launcher para abrir a galeria
    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        try {
                            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                                Uri selectedImageUri = result.getData().getData();
                                if (selectedImageUri != null) {
                                    Log.d(TAG, "Imagem selecionada: " + selectedImageUri.toString());
                                    String savedPath = saveImageToInternalStorage(selectedImageUri);
                                    if (savedPath != null) {
                                        selectedImagePath = savedPath;
                                        ivFotoClube.setImageURI(selectedImageUri);
                                        Toast.makeText(this, "Foto selecionada!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(this, "Erro ao salvar imagem", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(this, "Nenhuma imagem selecionada", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d(TAG, "Seletor de imagem cancelado ou falhou");
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Erro ao processar imagem selecionada", e);
                            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            // NÃO CHAMAMOS finish() AQUI
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
        ivFotoClube = findViewById(R.id.iv_foto);

        AppDatabase db = AppDatabase.getInstance(this);
        clubeDao = db.clubeDao();

        btnEscolherFoto.setOnClickListener(v -> openImagePicker());
        btnSalvar.setOnClickListener(v -> salvarClube());
    }

    private void openImagePicker() {
        try {
            // Verifica permissão para Android 13+
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
            // Abrir galeria
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao abrir seletor de imagens", e);
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private String saveImageToInternalStorage(Uri imageUri) {
        try {
            String fileName = "clube_" + System.currentTimeMillis() + ".jpg";
            File storageDir = getFilesDir();
            File imageFile = new File(storageDir, fileName);

            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            if (inputStream == null) {
                Log.e(TAG, "InputStream nulo");
                return null;
            }
            FileOutputStream outputStream = new FileOutputStream(imageFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            Log.d(TAG, "Imagem salva em: " + imageFile.getAbsolutePath());
            return imageFile.getAbsolutePath();
        } catch (Exception e) {
            Log.e(TAG, "Erro ao salvar imagem", e);
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker(); // Tenta abrir novamente
            } else {
                Toast.makeText(this, "Permissão negada para acessar fotos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void salvarClube() {
        try {
            String nome = etNome.getText().toString().trim();
            String descricao = etDescricao.getText().toString().trim();

            if (nome.isEmpty()) {
                etNome.setError("Nome é obrigatório");
                return;
            }

            Clube clube = new Clube();
            clube.nome = nome;
            clube.descricao = descricao;
            clube.fotoUrl = selectedImagePath;
            clube.criadoPorId = "usuario_temp";
            clube.createdAt = System.currentTimeMillis();

            new Thread(() -> {
                try {
                    long id = clubeDao.inserir(clube);
                    runOnUiThread(() -> {
                        if (id > 0) {
                            Toast.makeText(this, "Clube criado!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Erro ao salvar", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Erro ao inserir no banco", e);
                    runOnUiThread(() -> Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show());
                }
            }).start();
        } catch (Exception e) {
            Log.e(TAG, "Erro em salvarClube", e);
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}