package br.com.paulobarros.controller;


import br.com.paulobarros.controller.docs.ProjetoControllerDocs;
import br.com.paulobarros.data.dto.ClassificacaoRiscoDTO;
import br.com.paulobarros.data.dto.ProjetoDTO;
import br.com.paulobarros.service.ProjetoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gestao-projetos/projeto/")
@Tag(name = "Projeto", description = "API REST para gerenciamento de projetos")
@RequiredArgsConstructor
public class ProjetoController implements ProjetoControllerDocs {

    @Autowired
    private ProjetoService projetoService;


    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public  ResponseEntity<Page<ProjetoDTO>> list(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ){
        var sort = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageble = PageRequest.of(page,size, Sort.by(sort,"nome"));
        return ResponseEntity.ok(projetoService.findAll(pageble));
    }

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ProjetoDTO findById(@PathVariable Long id){
        return projetoService.findById(id);
    }

    @PostMapping(value = "/save",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ProjetoDTO create(@RequestBody ProjetoDTO projeto){
        return projetoService.create(projeto);
    }

    @PutMapping(value = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ProjetoDTO update(@RequestBody ProjetoDTO projeto){
        return projetoService.update(projeto);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        projetoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/classificacao",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ClassificacaoRiscoDTO calcularClassificacaoRisco(@RequestParam Long idProjeto){
        return projetoService.calcularClassificacaoRisco(idProjeto);
    }

}
