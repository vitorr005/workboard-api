package com.example.WorkBoard.controller;
import com.example.WorkBoard.dto.AtualizarStatusRequest;
import com.example.WorkBoard.model.HistoricoTarefa;
import com.example.WorkBoard.model.StatusTarefa;
import com.example.WorkBoard.repository.HistoricoTarefaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.WorkBoard.model.Tarefa;
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

    public TarefaController(TarefaRepository tarefaRepository,
                            HistoricoTarefaRepository historicoTarefaRepository) {
        this.tarefaRepository = tarefaRepository;
        this.historicoTarefaRepository = historicoTarefaRepository;
    }



    @GetMapping
    public List<Tarefa> listarTarefas(){
        return tarefaRepository.findAll();
    }

    @PostMapping
    public Tarefa criarTarefa(@RequestBody Tarefa tarefa) {
        return tarefaRepository.save(tarefa);
    }

    @GetMapping("/{id}")
    public Tarefa buscarPorId(@PathVariable Long id){
        return tarefaRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Long id){

         if (!tarefaRepository.existsById(id)){
             return ResponseEntity.notFound().build();
         }

         tarefaRepository.deleteById(id);
         return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizarTarefa(@PathVariable Long id,
                                                  @RequestBody Tarefa tarefaAtualizada)

    {
        return tarefaRepository.findById(id).map(tarefa -> {

            tarefa.setTitulo(tarefaAtualizada.getTitulo());
            tarefa.setStatus(tarefaAtualizada.getStatus());
            tarefa.setDataLimite(tarefaAtualizada.getDataLimite());

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


                    return ResponseEntity.ok(tarefaSalva);
                })
                .orElse(ResponseEntity.notFound().build());
    }



}
