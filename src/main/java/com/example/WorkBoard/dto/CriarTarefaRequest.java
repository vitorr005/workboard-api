package com.example.WorkBoard.dto;

import com.example.WorkBoard.model.StatusTarefa;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CriarTarefaRequest {

    private String titulo;
    private LocalDate dataLimite;
    private StatusTarefa status;

    private Long responsavelId;
    private Long criadorId;
    
}
