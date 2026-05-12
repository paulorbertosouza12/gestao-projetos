package br.com.paulobarros.service;


import br.com.paulobarros.data.dto.ClassificacaoRiscoDTO;
import br.com.paulobarros.data.dto.ProjetoDTO;
import br.com.paulobarros.exception.RequiredObjectIsNullException;
import br.com.paulobarros.model.Projeto;
import br.com.paulobarros.repository.ProjetoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static br.com.paulobarros.mapper.ObjectMapper.parseObject;


@Service
public class ProjetoService {

    private final Logger logger = LoggerFactory.getLogger(ProjetoService.class);

    @Autowired
    private ProjetoRepository repository;

    @Autowired
    private  ClassificacaoRiscoService classificacaoRiscoService;

    public Page<ProjetoDTO> findAll(Pageable pageble){
        logger.info("Buscando todos os projetos");

        var projetos = repository.findAll(pageble);

        var projetosDTO = projetos.map(projeto -> {
            var dto = parseObject(projeto,ProjetoDTO.class);
            return dto;
        });

        return projetosDTO;
    }


    public ProjetoDTO findById(Long id){
        logger.info("Buscando projeto por id: {}", id);

        var entity = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));

        return parseObject(entity,ProjetoDTO.class);
    }


    public ProjetoDTO create(ProjetoDTO projeto){
        if(projeto == null)
            throw new RequiredObjectIsNullException();

        logger.info("Salvando projeto: {}", projeto);

        var entity = parseObject(projeto, Projeto.class);


        return parseObject(repository.save(entity), ProjetoDTO.class);
    }

    public ProjetoDTO update(ProjetoDTO projeto){

        if(projeto == null)
            throw new RequiredObjectIsNullException();

        logger.info("Atualizando projeto: {}", projeto);

        Projeto entity = repository.findById(projeto.getId()).orElseThrow( () -> new IllegalArgumentException("Projeto não encontrado"));

        entity.setNome(projeto.getNome());
        entity.setStatus(projeto.getStatus());
        entity.setDescricao(projeto.getDescricao());
        entity.setOrcamentoTotal(projeto.getOrcamentoTotal());
        entity.setDataRealTermino(projeto.getDataRealTermino());
        entity.setDataPrevisaoTermino(projeto.getDataPrevisaoTermino());
        entity.setInicio(projeto.getInicio());
        //entity.setResponsavel(projeto.getResponsavel());

        return parseObject(repository.save(entity), ProjetoDTO.class);
    }

    public void delete(Long id){
        logger.info("Deletando projeto por id: {}", id);
        Projeto entity = repository.findById(id).orElseThrow( () -> new IllegalArgumentException("Projeto não encontrado"));
        repository.delete(entity);
    }

   public ClassificacaoRiscoDTO consultaClassificacaoRisco(Long id){
        var projeto = findById(id);

        return null;
   }

    public ClassificacaoRiscoDTO calcularClassificacaoRisco(Long idProjeto) {

        ProjetoDTO projeto = findById(idProjeto);

       return classificacaoRiscoService.calcular(projeto);
    }
}
