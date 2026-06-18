package com.example.WorkBoard.repository;

import com.example.WorkBoard.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}