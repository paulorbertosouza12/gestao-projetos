package br.com.paulobarros.data.dto;

import br.com.paulobarros.model.enums.StatusProjetoEnum;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@JsonPropertyOrder({"id", "nome", "inicio", "dataPrevisaoTermino", "dataRealTermino", "orcamentoTotal", "responsavel", "descricao", "status","gerenteResponsavel","membros"})
public class ProjetoDTO implements java.io.Serializable{

    private Long id;
    private String nome;

    private LocalDate inicio;
    private LocalDate dataPrevisaoTermino;

    private LocalDate dataRealTermino;
    private BigDecimal orcamentoTotal;
    private String descricao;
    private StatusProjetoEnum status;

    private MembroDTO gerenteResponsavel ;
    private List<ProjetoMembroDTO> alocacoes;

}
