package br.com.paulobarros.controllers;


import br.com.paulobarros.controllers.docs.MembroControllerDocs;
import br.com.paulobarros.data.dto.MembroDTO;
import br.com.paulobarros.service.MembroService;
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
@RequestMapping("/gestao-projetos/membro/")
@Tag(name = "Membro", description = "API REST para gerenciamento de membros")
@RequiredArgsConstructor
public class MembroController implements MembroControllerDocs {

    @Autowired
    private MembroService membroService;


    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public  ResponseEntity<Page<MembroDTO>> list(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ){
        var sort = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageble = PageRequest.of(page,size, Sort.by(sort,"nome"));
        return ResponseEntity.ok(membroService.findAll(pageble));
    }

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public MembroDTO findById(@PathVariable Long id){
        return membroService.findById(id);
    }

    @PostMapping(value = "/save",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public MembroDTO create(@RequestBody MembroDTO membro){
        return membroService.create(membro);
    }

    @PutMapping(value = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public MembroDTO update(@RequestBody MembroDTO membro){
        return membroService.update(membro);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        membroService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
