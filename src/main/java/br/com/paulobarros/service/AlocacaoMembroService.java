package br.com.paulobarros.service;

import br.com.paulobarros.data.dto.ProjetoDTO;
import br.com.paulobarros.mapper.custom.ProjetoMapper;
import br.com.paulobarros.repository.ProjetoRepository;
import br.com.paulobarros.service.validator.AlocacaoMembroValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlocacaoMembroService {

    @Autowired
    private  ProjetoRepository projetoRepository;

    @Autowired
    private  MembroClient membroApiClient;

    @Autowired
    private  AlocacaoMembroValidator alocacaoMembroValidator;

    @Autowired
    private ProjetoMapper converter;






    @Transactional
    public ProjetoDTO alocarMembro(Long idProjeto, Long idMembro){
        var projeto = projetoRepository.findById(idProjeto).orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));
        var membro = membroApiClient.buscarPorId(idMembro);

        alocacaoMembroValidator.validarAlocacao(projeto,membro);

        projeto.adicionarMembro(membro.getId());

        return converter.convertEntityToDTO(projetoRepository.save(projeto));
    }

    @Transactional
    public ProjetoDTO removerMembro(Long idProjeto, Long idMembro){
        var projeto = projetoRepository.findById(idProjeto).orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));

        //alocacaoMembroValidator.validarRemocao(projeto, idMembro);

        projeto.removerMembro(idMembro);

        return converter.convertEntityToDTO(projetoRepository.save(projeto));
    }

    @Transactional
    public ProjetoDTO alocarMembroLote(Long idProjeto, List<Long> idsMembro) {
        for (Long idMembro : idsMembro) {
            alocarMembro(idProjeto, idMembro);
        }

        return converter.convertEntityToDTO(projetoRepository.findById(idProjeto).orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado")));
    }

    @Transactional
    public ProjetoDTO getByIdProjeto(Long idProjeto){
        return converter.convertEntityToDTO(projetoRepository.findById(idProjeto).orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado")));
    }
}
