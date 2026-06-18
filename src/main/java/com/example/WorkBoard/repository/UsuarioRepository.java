package com.example.WorkBoard.repository;

import com.example.WorkBoard.model.TipoUsuario;
import com.example.WorkBoard.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

List<Usuario> findByTipo(TipoUsuario tipo);
}