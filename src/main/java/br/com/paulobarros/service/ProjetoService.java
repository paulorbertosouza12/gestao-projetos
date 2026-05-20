package br.com.paulobarros.service;


import br.com.paulobarros.data.dto.ClassificacaoRiscoDTO;
import br.com.paulobarros.data.dto.ProjetoDTO;
import br.com.paulobarros.exception.RequiredObjectIsNullException;
import br.com.paulobarros.model.Projeto;
import br.com.paulobarros.repository.ProjetoRepository;
import br.com.paulobarros.service.validator.ProjetoValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static br.com.paulobarros.mapper.ObjectMapper.parseObject;


@Service
public class ProjetoService {

    private final Logger logger = LoggerFactory.getLogger(ProjetoService.class);

    @Autowired
    private ProjetoRepository repository;

    @Autowired
    private ProjetoValidator projetoValidator;

    @Autowired
    private  ClassificacaoRiscoService classificacaoRiscoService;

    public Page<ProjetoDTO> findAll(Pageable pageble){
        logger.info("Buscando todos os projetos");

        var projetos = repository.findAll(pageble);

        var projetosDTO = projetos.map(projeto -> {
            return parseObject(projeto,ProjetoDTO.class);
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

        projetoValidator.validarCreate(projeto);

        return parseObject(repository.save(entity), ProjetoDTO.class);
    }

    @Transactional
    public ProjetoDTO update(ProjetoDTO projeto){

        logger.info("Atualizando projeto: {}", projeto);

        if(projeto == null)
            throw new RequiredObjectIsNullException();


        Projeto entity = repository.findById(projeto.getId()).orElseThrow( () -> new IllegalArgumentException("Projeto não encontrado"));

        projetoValidator.validarUpdate(entity, projeto);

        atualizarDadosProjeto(entity, projeto);

        return parseObject(repository.save(entity), ProjetoDTO.class);
    }

    public void delete(Long id){
        logger.info("Deletando projeto por id: {}", id);
        Projeto entity = repository.findById(id).orElseThrow( () -> new IllegalArgumentException("Projeto não encontrado"));

        projetoValidator.validarDelete(entity);

        repository.delete(entity);
    }

    private void atualizarDadosProjeto(Projeto entity, ProjetoDTO projeto) {
        entity.setNome(projeto.getNome());
        entity.setStatus(projeto.getStatus());
        entity.setDescricao(projeto.getDescricao());
        entity.setOrcamentoTotal(projeto.getOrcamentoTotal());
        entity.setDataRealTermino(projeto.getDataRealTermino());
        entity.setDataPrevisaoTermino(projeto.getDataPrevisaoTermino());
        entity.setInicio(projeto.getInicio());
        entity.setIdGerenteResponsavel(projeto.getIdGerenteResponsavel());
    }


    public ClassificacaoRiscoDTO calcularClassificacaoRisco(Long idProjeto) {
        ProjetoDTO projeto = findById(idProjeto);
       return classificacaoRiscoService.calcular(projeto);
    }


    @Transactional(readOnly = true)
    public Page<ProjetoDTO> listarPorFiltrosPaginado(ProjetoDTO filter, Pageable pageable) {
        var spec = repository.filtro(filter);

        Page<Projeto> projetos = repository.findAll(spec, pageable);

        Page<ProjetoDTO> projetosDTO = projetos.map(projeto ->
                parseObject(projeto, ProjetoDTO.class)
        );

        return projetosDTO;
    }

}
