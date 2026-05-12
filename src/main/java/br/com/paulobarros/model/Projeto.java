package br.com.paulobarros.model;

import br.com.paulobarros.model.enums.StatusProjetoEnum;
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
    @Column(name = "id_projeto")
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

    @Column(name = "descricao", nullable = false, length = 255)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusProjetoEnum status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_gerente_responsavel", nullable = false)
    private Membro gerenteResponsavel;

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjetoMembro> membros;

}
