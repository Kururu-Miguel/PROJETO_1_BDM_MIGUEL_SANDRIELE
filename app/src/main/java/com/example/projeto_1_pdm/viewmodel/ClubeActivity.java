package com.example.projeto_1_pdm.viewmodel;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import com.example.projeto_1_pdm.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ClubeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clube);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.tela, new DescFragment()).commit();

        bottomNavigationView.setOnItemSelectedListener( item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.descricao){
                selectedFragment = new DescFragment();
            } else if (item.getItemId() == R.id.eventos){
                selectedFragment = new EventFragment();
            }

            if (selectedFragment != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.tela, selectedFragment).commit();
            }

            return true;
        });
    }
}