package br.com.paulobarros.controllers;

import br.com.paulobarros.data.dto.MembroDTO;
import br.com.paulobarros.model.enums.AtribuicaoEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/mock/membros-api/membros")
public class MembroMockController {

    private final Map<Long, MembroDTO> membros = new ConcurrentHashMap<>();
    private Long sequencial = 4L;

    public MembroMockController() {
        adicionarMembroMockado(1L, "João Silva", AtribuicaoEnum.FUNCIONARIO);
        adicionarMembroMockado(2L, "Maria Souza", AtribuicaoEnum.FUNCIONARIO);
        adicionarMembroMockado(3L, "Carlos Lima", AtribuicaoEnum.GERENTE);
    }

    @PostMapping
    public ResponseEntity<MembroDTO> criar(@RequestBody MembroDTO request) {
        Long id = sequencial++;

        MembroDTO membro = new MembroDTO();
        membro.setId(id);
        membro.setNome(request.getNome());
        membro.setAtribuicao(request.getAtribuicao());

        membros.put(id, membro);

        return ResponseEntity.status(HttpStatus.CREATED).body(membro);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembroDTO> buscarPorId(@PathVariable Long id) {
        MembroDTO membro = membros.get(id);

        if (membro == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(membro);
    }

    @GetMapping
    public ResponseEntity<List<MembroDTO>> listar() {
        return ResponseEntity.ok(new ArrayList<>(membros.values()));
    }

    private void adicionarMembroMockado(Long id, String nome, AtribuicaoEnum atribuicao) {
        MembroDTO membro = new MembroDTO();
        membro.setId(id);
        membro.setNome(nome);
        membro.setAtribuicao(atribuicao);

        membros.put(id, membro);
    }
}
