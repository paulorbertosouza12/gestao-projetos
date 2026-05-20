package br.com.paulobarros.service;

import br.com.paulobarros.data.dto.ProjetoDTO;
import br.com.paulobarros.model.Projeto;
import br.com.paulobarros.repository.ProjetoRepository;
import br.com.paulobarros.service.validator.AlocacaoMembroValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static br.com.paulobarros.mapper.ObjectMapper.parseObject;

@Service
public class AlocacaoMembroService {


    private final ProjetoRepository projetoRepository;
    private final MembroClient membroApiClient;
    private final AlocacaoMembroValidator alocacaoMembroValidator;

    public AlocacaoMembroService(
            ProjetoRepository projetoRepository,
            MembroClient membroApiClient,
            AlocacaoMembroValidator alocacaoMembroValidator
    ) {
        this.projetoRepository = projetoRepository;
        this.membroApiClient = membroApiClient;
        this.alocacaoMembroValidator = alocacaoMembroValidator;
    }



    @Transactional
    public ProjetoDTO alocarMembro(Long idProjeto, Long idMembro){
        var projeto = projetoRepository.findById(idProjeto).orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));
        var membro = membroApiClient.buscarPorId(idMembro);

        alocacaoMembroValidator.validarAlocacao(projeto,membro);

        projeto.adicionarMembro(membro.getId());

        Projeto projetoSalvo = projetoRepository.save(projeto);

        return parseObject(projetoSalvo, ProjetoDTO.class);
    }

    @Transactional
    public ProjetoDTO removerMembro(Long idProjeto, Long idMembro){
        var projeto = projetoRepository.findById(idProjeto).orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));

        //alocacaoMembroValidator.validarRemocao(projeto, idMembro);

        projeto.removerMembro(idMembro);

        Projeto projetoSalvo = projetoRepository.save(projeto);

        return parseObject(projetoSalvo, ProjetoDTO.class);
    }

    @Transactional
    public ProjetoDTO alocarMembroLote(Long idProjeto, List<Long> idsMembro) {
        for (Long idMembro : idsMembro) {
            alocarMembro(idProjeto, idMembro);
        }

        return parseObject(projetoRepository.findById(idProjeto).orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado")), ProjetoDTO.class);
    }
}
