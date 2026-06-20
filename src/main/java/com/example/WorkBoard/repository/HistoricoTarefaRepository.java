package com.example.WorkBoard.repository;

import com.example.WorkBoard.model.HistoricoTarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoTarefaRepository extends JpaRepository<HistoricoTarefa , Long> {


    List<HistoricoTarefa> findByTarefaId(Long tarefaId);
}
