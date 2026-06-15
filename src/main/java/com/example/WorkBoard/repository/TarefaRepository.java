package com.example.WorkBoard.repository;

import com.example.WorkBoard.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
}