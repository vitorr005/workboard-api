package com.example.WorkBoard.controller;
import com.example.WorkBoard.dto.AtualizarStatusRequest;
import com.example.WorkBoard.dto.CriarTarefaRequest;
import com.example.WorkBoard.model.*;
import com.example.WorkBoard.repository.HistoricoTarefaRepository;
import com.example.WorkBoard.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.WorkBoard.repository.TarefaRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {
    private final TarefaRepository tarefaRepository;
    private final HistoricoTarefaRepository historicoTarefaRepository;
    private final UsuarioRepository usuarioRepository;

    public TarefaController(TarefaRepository tarefaRepository,
                            HistoricoTarefaRepository historicoTarefaRepository, UsuarioRepository usuarioRepository) {
        this.tarefaRepository = tarefaRepository;
        this.historicoTarefaRepository = historicoTarefaRepository;
        this.usuarioRepository = usuarioRepository;
    }



    @GetMapping
    public List<Tarefa> listarTarefas(){
        return tarefaRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Tarefa> criarTarefa(@RequestBody CriarTarefaRequest request) {

        Usuario criador = usuarioRepository.findById(request.getCriadorId())
                .orElse(null);

        if (criador == null){
            return ResponseEntity.notFound().build();
        }

        if (criador.getTipo() != TipoUsuario.GESTOR){
            return ResponseEntity.status(403).build();
        }

        Usuario responsavel = usuarioRepository.findById(request.getResponsavelId())
                .orElse(null);

        if(responsavel == null){
            return ResponseEntity.notFound().build();
        }

        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(request.getTitulo());
        tarefa.setDataLimite(request.getDataLimite());
        tarefa.setStatus(request.getStatus());
        tarefa.setResponsavel(responsavel);

        return ResponseEntity.ok(tarefaRepository.save(tarefa));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> buscarPorId(@PathVariable Long id){
        return tarefaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Long id,
                                              @RequestParam Long usuarioId){


        Usuario deletar = usuarioRepository.findById(usuarioId)
                .orElse(null);

        if (deletar == null){
            return ResponseEntity.notFound().build();
        }

        if (deletar.getTipo() != TipoUsuario.GESTOR){
            return ResponseEntity.status(403).build();
        }


         if (!tarefaRepository.existsById(id)){
             return ResponseEntity.notFound().build();
         }

         tarefaRepository.deleteById(id);
         return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizarTarefa(@PathVariable Long id,
                                                  @RequestBody CriarTarefaRequest request,
                                                  @RequestParam Long usuarioId) {

        Usuario alterar = usuarioRepository.findById(usuarioId)
                .orElse(null);

        if (alterar == null){
            return ResponseEntity.notFound().build();
        }

        if (alterar.getTipo() != TipoUsuario.GESTOR){
            return ResponseEntity.status(403).build();
        }

        Usuario responsavel = usuarioRepository.findById(request.getResponsavelId())
                .orElse(null);

        if (responsavel == null){
            return ResponseEntity.notFound().build();
        }

        return tarefaRepository.findById(id).map(tarefa -> {

            tarefa.setTitulo(request.getTitulo());
            tarefa.setStatus(request.getStatus());
            tarefa.setDataLimite(request.getDataLimite());
            tarefa.setResponsavel(responsavel);

            Tarefa tarefaSalva = tarefaRepository.save(tarefa);
            return ResponseEntity.ok(tarefaSalva);

        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{id}")
    public List<Tarefa> listarPorUsuario(@PathVariable Long id) {
        return tarefaRepository.findByResponsavelId(id);
    }

    @GetMapping("/atrasadas")
    public List<Tarefa> listarAtrasadas(){
        return tarefaRepository.findByDataLimiteBeforeAndStatusNot(LocalDate.now(),
                StatusTarefa.CONCLUIDA);
    }

    @GetMapping("/status/{status}")
    public List<Tarefa> listarPorStatus(@PathVariable StatusTarefa status){
        return tarefaRepository.findByStatus(status);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Tarefa> atualizarStatus(
            @PathVariable Long id,
            @RequestBody AtualizarStatusRequest request) {

        return tarefaRepository.findById(id)
                .map(tarefa -> {
                    StatusTarefa statusAnterior = tarefa.getStatus();

                    tarefa.setStatus(request.getStatus());

                    HistoricoTarefa historico = new HistoricoTarefa();
                    historico.setTarefa(tarefa);
                    historico.setStatusAnterior(statusAnterior);
                    historico.setStatusNovo(request.getStatus());
                    historico.setDataAlteracao(LocalDateTime.now());

                    Tarefa tarefaSalva = tarefaRepository.save(tarefa);
                    historicoTarefaRepository.save(historico);


                    return ResponseEntity.ok(tarefaSalva);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/historico")
    public List<HistoricoTarefa> listarHistorico (@PathVariable Long id){
        return historicoTarefaRepository.findByTarefaId(id);

    }



}
