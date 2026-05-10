package br.com.paulobarros.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Projeto {

    private  String nome;

    private LocalDate inicio;

    private LocalDate dataPrevisaoTermino;

    private LocalDate dataRealTermino;

    private BigDecimal orcamentoTotal;

    private Membro responsavel;

    private String descricao;

    private StatusProjetoEnum status;
}
