package com.example.projeto_1_pdm.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "membros", primaryKeys = {"usuarioId", "clubeId"}) // chave composta (evita duplicidade)
    public class Membro {
    @NonNull
    public String usuarioId;
    @NonNull
    public long clubeId ;
    public String cargo;
    public long createdAt;
}