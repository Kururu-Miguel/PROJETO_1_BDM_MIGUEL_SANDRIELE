package com.example.projeto_1_pdm.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "eventos")
public class Evento {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long clubeId;
    public String titulo;
    public String descricao;
    public long horario; // timestamp
    public String local;
    public long createdAt;
}
