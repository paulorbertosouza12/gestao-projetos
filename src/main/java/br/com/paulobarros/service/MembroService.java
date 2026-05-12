package br.com.paulobarros.service;


import br.com.paulobarros.data.dto.MembroDTO;
import br.com.paulobarros.exception.RequiredObjectIsNullException;
import br.com.paulobarros.model.Membro;
import br.com.paulobarros.repository.MembroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static br.com.paulobarros.mapper.ObjectMapper.parseObject;


@Service
public class MembroService {

    private final Logger logger = LoggerFactory.getLogger(MembroService.class);

    @Autowired
    private MembroRepository repository;

    public Page<MembroDTO> findAll(Pageable pageble){
        logger.info("Buscando todos os membros");

        var membros = repository.findAll(pageble);

        return membros.map(membro -> {
            return parseObject(membro,MembroDTO.class);
        });
    }


    public MembroDTO findById(Long id){
        logger.info("Buscando membro por id: {}", id);

        var entity = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Membro não encontrado"));

        return parseObject(entity,MembroDTO.class);
    }


    public MembroDTO create(MembroDTO membro){
        if(membro == null)
            throw new RequiredObjectIsNullException();

        logger.info("Salvando membro: {}", membro);

        var entity = parseObject(membro, Membro.class);


        return parseObject(repository.save(entity), MembroDTO.class);
    }

    public MembroDTO update(MembroDTO membro){

        if(membro == null)
            throw new RequiredObjectIsNullException();

        logger.info("Atualizando membro: {}", membro);

        Membro entity = repository.findById(membro.getId()).orElseThrow( () -> new IllegalArgumentException("Membro não encontrado"));
        
        entity.setNome(membro.getNome());
        entity.setAtribuicao(membro.getAtribuicao());

        return parseObject(repository.save(entity), MembroDTO.class);
    }

    public void delete(Long id){
        logger.info("Deletando membro por id: {}", id);
        Membro entity = repository.findById(id).orElseThrow( () -> new IllegalArgumentException("Membro não encontrado"));
        repository.delete(entity);
    }

}
