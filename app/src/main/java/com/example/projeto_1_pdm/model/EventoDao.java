package com.example.projeto_1_pdm.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EventoDao {
    @Insert
    long inserir(Evento evento);

    @Update
    void atualizar(Evento evento);

    @Query("DELETE FROM eventos WHERE id = :id")
    void deletar(long id);

    @Query("SELECT * FROM eventos WHERE clubeId = :clubeId ORDER BY horario ASC")
    LiveData<List<Evento>> buscarEventosDoClube(long clubeId);
}