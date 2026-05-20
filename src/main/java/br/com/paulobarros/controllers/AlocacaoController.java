package br.com.paulobarros.controllers;

import br.com.paulobarros.controllers.docs.AlocacaoControllerDocs;
import br.com.paulobarros.data.dto.ProjetoDTO;
import br.com.paulobarros.service.AlocacaoMembroService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gestao-projetos/alocacao")
@Tag(name = "Alocação", description = "API REST para gerenciamento de alocações de funcionários em projetos")
@RequiredArgsConstructor
public class AlocacaoController implements AlocacaoControllerDocs {

    @Autowired
    AlocacaoMembroService alocacaoMembroService;

    public AlocacaoController(AlocacaoMembroService alocacaoMembroService) {
        this.alocacaoMembroService = alocacaoMembroService;
    }


    @PutMapping(value = "/{idProjeto}/membro/{idMembro}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjetoDTO> alocarMembro(@PathVariable Long idProjeto, @PathVariable Long idMembro) {
        return ResponseEntity.ok(alocacaoMembroService.alocarMembro(idProjeto, idMembro));
    }

    @PutMapping(value = "/{idProjeto}/membros", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjetoDTO> alocarMembroLote(
            @PathVariable Long idProjeto,
            @RequestBody List<Long> idsMembro
    ) {
        return ResponseEntity.ok(alocacaoMembroService.alocarMembroLote(idProjeto, idsMembro));
    }

    @DeleteMapping(value = "/{idProjeto}/membros/{idMembro}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjetoDTO> removerMembro(
            @PathVariable Long idProjeto,
            @PathVariable Long idMembro
    ) {
        return ResponseEntity.ok(alocacaoMembroService.removerMembro(idProjeto, idMembro));
    }

}
