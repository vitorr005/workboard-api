package com.example.WorkBoard.controller;

import com.example.WorkBoard.model.TipoUsuario;
import com.example.WorkBoard.model.Usuario;
import com.example.WorkBoard.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ({"/usuario"})
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;


    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public List<Usuario> listarUsuario(){
        return usuarioRepository.findAll();
    }

    @PostMapping
    public Usuario criarUsuario(@RequestBody Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    @GetMapping("/{id}")
    public Usuario buscarPorId(@PathVariable Long id){
        return usuarioRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id){

        if (!usuarioRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id ,
                                                    @RequestBody Usuario usuarioAtualizado){
        return usuarioRepository.findById(id).map(usuario -> {

            usuarioAtualizado.setEmail(usuarioAtualizado.getEmail());
            usuarioAtualizado.setNome(usuarioAtualizado.getNome());
            usuarioAtualizado.setSenha(usuarioAtualizado.getSenha());
            usuarioAtualizado.setTipo(usuarioAtualizado.getTipo());

            Usuario usuarioSalvo = usuarioRepository.save(usuario);
            return ResponseEntity.ok(usuarioSalvo);
        }).orElse(ResponseEntity.notFound().build());


    }

    @GetMapping("/tipo/{tipo}")
    public List<Usuario> listarPorTipo(@PathVariable TipoUsuario tipo){
        return usuarioRepository.findByTipo(tipo);
    }

}
