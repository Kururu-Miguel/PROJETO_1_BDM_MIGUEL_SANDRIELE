package com.example.projeto_1_pdm.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ClubeDao {
    @Insert
    long inserir(Clube clube); // retorna o ID gerado

    @Update
    void atualizar(Clube clube);

    @Query("SELECT * FROM clubes WHERE id = :id")
    Clube buscarPorId(long id);

    @Query("SELECT * FROM clubes")
    LiveData<List<Clube>> buscarTodos();
}
