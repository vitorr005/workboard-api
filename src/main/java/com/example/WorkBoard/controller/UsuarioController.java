package com.example.WorkBoard.controller;

import com.example.WorkBoard.dto.LoginRequest;
import com.example.WorkBoard.dto.UsuarioResponse;
import com.example.WorkBoard.model.TipoUsuario;
import com.example.WorkBoard.model.Usuario;
import com.example.WorkBoard.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping ({"/usuarios"})
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;


    public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<UsuarioResponse> listarUsuario(){
        return usuarioRepository.findAll()
                .stream()
                .map(usuario -> new UsuarioResponse(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getTipo()
                ))
                .toList();
    }

    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario){

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            return ResponseEntity.status(409).build();
        }

        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return ResponseEntity.ok(usuarioSalvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id){
        return usuarioRepository.findById(id)
                .map(usuario -> new UsuarioResponse(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getTipo()
                        )
                ).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse> validarUsuario(@RequestBody LoginRequest loginRequest){

        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(loginRequest.getEmail());

       if (usuarioOptional.isEmpty()){
           return ResponseEntity.notFound().build();
       }

       Usuario usuario = usuarioOptional.get();

       if (!passwordEncoder.matches(loginRequest.getSenha(), usuario.getSenha())){
           return ResponseEntity.status(401).build();
       }

       UsuarioResponse usuarioResponse = new UsuarioResponse(
               usuario.getId(),
               usuario.getNome(),
               usuario.getEmail(),
               usuario.getTipo()
       );

       return ResponseEntity.ok(usuarioResponse);
    }









}
