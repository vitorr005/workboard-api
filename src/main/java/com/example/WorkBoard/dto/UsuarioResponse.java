package com.example.WorkBoard.dto;

import com.example.WorkBoard.model.TipoUsuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResponse {

    private Long id;
    private String nome;
    private String email;
    private TipoUsuario tipo;

    public UsuarioResponse(Long id, String nome, String email, TipoUsuario tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
    }
}
