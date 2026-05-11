package br.com.paulobarros.data.dto;

import br.com.paulobarros.model.ProjetoMembro;
import br.com.paulobarros.model.StatusProjetoEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@JsonPropertyOrder({"id", "nome", "inicio", "dataPrevisaoTermino", "dataRealTermino", "orcamentoTotal", "responsavel", "descricao", "status"})
public class ProjetoDTO implements java.io.Serializable{

    private Long id;
    private String nome;
    private LocalDate inicio;

    private LocalDate dataPrevisaoTermino;

    private LocalDate dataRealTermino;
    private BigDecimal orcamentoTotal;
    private List<ProjetoMembro> responsavel;
    private String descricao;
    private StatusProjetoEnum status;

}
