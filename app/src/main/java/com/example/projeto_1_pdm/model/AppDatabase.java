package com.example.projeto_1_pdm.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Clube.class, Membro.class, Evento.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ClubeDao clubeDao();
    public abstract MembroDao membroDao();
    public abstract EventoDao eventoDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "meu_app.db")
                    .allowMainThreadQueries() // ⚠️ só para testes; em produção use background
                    .build();
        }
        return INSTANCE;
    }
}
