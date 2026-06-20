package com.example.WorkBoard.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "historico_tarefa")
public class HistoricoTarefa {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)

    private Long id;

    private StatusTarefa statusAnterior;

    private StatusTarefa statusNovo;

    private LocalDateTime dataAlteracao;

    @ManyToOne
    @JoinColumn(name = "tarefa_id")
    private Tarefa tarefa;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


}
