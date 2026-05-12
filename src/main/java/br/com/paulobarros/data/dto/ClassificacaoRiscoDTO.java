package br.com.paulobarros.data.dto;

import br.com.paulobarros.model.enums.ClassificacaoRiscoEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClassificacaoRiscoDTO {

    private Long idProjeto;
    private String nomeProjeto;
    private ClassificacaoRiscoEnum classificacao;
    private Long prazoEmMeses;
    private BigDecimal orcamentoTotal;
}
