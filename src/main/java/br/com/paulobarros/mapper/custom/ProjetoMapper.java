package br.com.paulobarros.mapper.custom;

import br.com.paulobarros.data.dto.ProjetoDTO;
import br.com.paulobarros.data.dto.ProjetoMembroDTO;
import br.com.paulobarros.model.Projeto;
import br.com.paulobarros.model.ProjetoMembro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjetoMapper {

    private static final Logger log = LoggerFactory.getLogger(ProjetoMapper.class);

    public ProjetoDTO convertEntityToDTO(Projeto projeto) {
        if (projeto == null) return null;

        ProjetoDTO dto = new ProjetoDTO();
        dto.setId(projeto.getId());
        dto.setNome(projeto.getNome());
        dto.setStatus(projeto.getStatus());
        dto.setDescricao(projeto.getDescricao());
        dto.setOrcamentoTotal(projeto.getOrcamentoTotal());
        dto.setDataRealTermino(projeto.getDataRealTermino());
        dto.setDataPrevisaoTermino(projeto.getDataPrevisaoTermino());
        dto.setInicio(projeto.getInicio());
        dto.setIdGerenteResponsavel(projeto.getIdGerenteResponsavel());

        if (projeto.getMembros() != null) {
            List<ProjetoMembroDTO> membrosDTO = projeto.getMembros()
                    .stream()
                    .map(membro -> convertProjetoMembroToDTO(membro, projeto.getId()))
                    .collect(Collectors.toList());

            dto.setAlocacaoProjeto(membrosDTO);
        }

        return dto;
    }

    public Projeto convertDTOtoEntity(ProjetoDTO projetoDTO){
        if (projetoDTO == null) return null;

        Projeto entity = new Projeto();

        entity.setId(projetoDTO.getId());
        entity.setNome(projetoDTO.getNome());
        entity.setStatus(projetoDTO.getStatus());
        entity.setDescricao(projetoDTO.getDescricao());
        entity.setOrcamentoTotal(projetoDTO.getOrcamentoTotal());
        entity.setDataRealTermino(projetoDTO.getDataRealTermino());
        entity.setDataPrevisaoTermino(projetoDTO.getDataPrevisaoTermino());
        entity.setInicio(projetoDTO.getInicio());
        entity.setIdGerenteResponsavel(projetoDTO.getIdGerenteResponsavel());

        if (projetoDTO.getAlocacaoProjeto() != null && !projetoDTO.getAlocacaoProjeto().isEmpty()) {
            entity.setMembros(
                    projetoDTO.getAlocacaoProjeto().stream()
                            .map(pmDTO -> convertDTOToProjetoMembro(pmDTO, entity))
                            .collect(Collectors.toSet())
            );
        }

        return entity;
    }

    private ProjetoMembroDTO convertProjetoMembroToDTO(ProjetoMembro projetoMembro, Long idProjeto) {
        ProjetoMembroDTO dto = new ProjetoMembroDTO();
        dto.setIdProjeto(idProjeto);
        dto.setIdMembro(projetoMembro.getIdMembro());
        return dto;
    }

    private ProjetoMembro convertDTOToProjetoMembro(ProjetoMembroDTO dto, Projeto projeto) {
        ProjetoMembro projetoMembro = new ProjetoMembro();
        projetoMembro.setProjeto(projeto);
        projetoMembro.setIdMembro(dto.getIdMembro());
        return projetoMembro;
    }
}
