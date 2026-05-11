package br.com.paulobarros.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "projeto")
public class Projeto implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 80)
    private  String nome;

    @Column(name = "inicio",nullable = false)
    private LocalDate inicio;

    @Column(name = "data_previsao_termino", nullable = false)
    private LocalDate dataPrevisaoTermino;

    @Column(name = "data_real_termino")
    private LocalDate dataRealTermino;

    @Column(name = "orcamento_total", nullable = false)
    private BigDecimal orcamentoTotal;

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjetoMembro> responsavel;

    @Column(name = "descricao", nullable = false, length = 255)
    private String descricao;

    @Enumerated(EnumType.ORDINAL)
    private StatusProjetoEnum status;
}
