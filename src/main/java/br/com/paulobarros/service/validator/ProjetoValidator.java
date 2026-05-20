package br.com.paulobarros.service.validator;

import br.com.paulobarros.data.dto.ProjetoDTO;
import br.com.paulobarros.exception.BusinessException;
import br.com.paulobarros.model.Projeto;
import br.com.paulobarros.model.enums.StatusProjetoEnum;
import org.springframework.stereotype.Service;

@Service
public class ProjetoValidator {


    public void validarCreate(ProjetoDTO projeto) {
        validarStatusInicial(projeto);
        validarDatas(projeto);
        validarOrcamento(projeto);
    }

    public void validarUpdate(Projeto projetoAtual, ProjetoDTO projetoAtualizado) {
        //validarObjeto(projetoAtualizado);
        //validarId(projetoAtualizado);
        //validarCamposObrigatorios(projetoAtualizado);
        validarDatas(projetoAtualizado);
        validarOrcamento(projetoAtualizado);
        validarTransicaoStatus(projetoAtual, projetoAtualizado);
    }

    public void validarDelete(Projeto projeto) {
        if (projeto.getStatus().permiteExclusao()) {
            throw new BusinessException("Não é possível excluir projeto em status: " + projeto.getStatus().getDescricao());
        }
    }

    private void validarDatas(ProjetoDTO projeto) {
        if (projeto.getInicio() == null) {
            throw new IllegalArgumentException("A data de início é obrigatória.");
        }

        if (projeto.getDataPrevisaoTermino() == null) {
            throw new IllegalArgumentException("A data de previsão de término é obrigatória.");
        }

        if (projeto.getDataPrevisaoTermino().isBefore(projeto.getInicio())) {
            throw new IllegalArgumentException("A data de previsão de término não pode ser anterior à data de início.");
        }

        if (projeto.getDataRealTermino() != null &&
                projeto.getDataRealTermino().isBefore(projeto.getInicio())) {
            throw new IllegalArgumentException("A data real de término não pode ser anterior à data de início.");
        }
    }

    private void validarOrcamento(ProjetoDTO projeto) {
        if (projeto.getOrcamentoTotal() == null) {
            throw new IllegalArgumentException("O orçamento total é obrigatório.");
        }

        if (projeto.getOrcamentoTotal().signum() <= 0) {
            throw new IllegalArgumentException("O orçamento total não pode ser negativo ou zero.");
        }
    }

    private void validarStatusInicial(ProjetoDTO projeto) {
        if (!StatusProjetoEnum.EM_ANALISE.equals(projeto.getStatus())) {
            throw new IllegalArgumentException("Todo projeto deve ser criado com o status EM_ANALISE.");
        }
    }

    private void validarTransicaoStatus(Projeto projetoAtual, ProjetoDTO projetoAtualizado) {
        StatusProjetoEnum statusAtual = projetoAtual.getStatus();
        StatusProjetoEnum novoStatus = projetoAtualizado.getStatus();

        if (!statusAtual.permiteTransicaoPara(novoStatus)) {
            throw new BusinessException("Transição de status inválida: "
                    + statusAtual.getDescricao()
                    + " -> "
                    + novoStatus.getDescricao());
        }
    }


}
