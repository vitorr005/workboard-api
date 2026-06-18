package com.example.WorkBoard.repository;

import com.example.WorkBoard.model.StatusTarefa;
import com.example.WorkBoard.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    List<Tarefa> findByResponsavelId(Long id);

    List<Tarefa> findByDataLimiteBeforeAndStatusNot(LocalDate data, StatusTarefa status);

}