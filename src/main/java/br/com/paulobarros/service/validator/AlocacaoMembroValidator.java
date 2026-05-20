package br.com.paulobarros.service.validator;

import br.com.paulobarros.data.dto.MembroDTO;
import br.com.paulobarros.exception.BusinessException;
import br.com.paulobarros.model.Projeto;
import br.com.paulobarros.model.enums.AtribuicaoEnum;
import br.com.paulobarros.model.enums.StatusProjetoEnum;
import br.com.paulobarros.repository.ProjetoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlocacaoMembroValidator {

    private static final int LIMITE_MINIMO_MEMBROS = 1;
    private static final int LIMITE_MAXIMO_MEMBROS = 10;
    private static final int LIMITE_MAXIMO_PROJETOS_ATIVOS_POR_MEMBRO = 3;

    private final ProjetoRepository projetoRepository;

    public AlocacaoMembroValidator(ProjetoRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }


    public void validarAlocacao(Projeto projeto, MembroDTO membro) {
        validarMembroFuncionario(membro);
        validarLimiteMaximoMembros(projeto);
        validarLimiteMaximoProjetosAtivosPorMembro(membro);


        if(projeto == null) {
            throw new IllegalArgumentException("Projeto não pode ser nulo");
        }

        if(membro == null) {
            throw new IllegalArgumentException("Membro não pode ser nulo");
        }
    }

    private void validarLimiteMinimoMembros(Projeto projeto) {
        if (projeto.getMembros() != null && projeto.getMembros().size() <= LIMITE_MINIMO_MEMBROS){
            throw new BusinessException("O projeto deve possuir no mínimo 1 membro.");
        }
    }

    private void validarLimiteMaximoMembros(Projeto projeto) {
        if (projeto.getMembros() !=null && projeto.getMembros().size() >= LIMITE_MAXIMO_MEMBROS){
            throw new BusinessException("Limite máximo de membros atingido para o projeto.");
        }
    }

    private void validarLimiteMaximoProjetosAtivosPorMembro(MembroDTO membro) {
        if (projetoRepository.countProjetosAtivosByMembroId(membro.getId(), List.of(StatusProjetoEnum.ENCERRADO,StatusProjetoEnum.CANCELADO)) >= LIMITE_MAXIMO_PROJETOS_ATIVOS_POR_MEMBRO){
            throw new BusinessException("O membro não pode estar alocado em mais de 3 projetos ativos");
        }
    }

    private void validarMembroFuncionario(MembroDTO membro) {
        if (!membro.getAtribuicao().equals(AtribuicaoEnum.FUNCIONARIO)){
            throw new BusinessException("Apenas membros com atribuição FUNCIONARIO podem ser associados ao projeto.");
        }
    }
}
