package com.example.projeto_1_pdm.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MembroDao {
    @Insert
    void inserir(Membro membro);

    @Query("DELETE FROM membros WHERE usuarioId = :usuarioId AND clubeId = :clubeId")
    void remover(String usuarioId, long clubeId);

    @Query("SELECT * FROM membros WHERE clubeId = :clubeId")
    LiveData<List<Membro>> buscarMembrosDoClube(long clubeId);

    @Query("SELECT * FROM membros WHERE usuarioId = :usuarioId")
    LiveData<List<Membro>> buscarClubesDoUsuario(String usuarioId);
}
