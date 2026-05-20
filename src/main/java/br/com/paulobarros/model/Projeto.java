package br.com.paulobarros.model;

import br.com.paulobarros.model.enums.StatusProjetoEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

   @Column(name = "id_gerente_responsavel")
    private Long idGerenteResponsavel;

    @OneToMany(
            mappedBy = "projeto",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<ProjetoMembro> membros = new HashSet<>();


    public void adicionarMembro(Long idMembro) {
        ProjetoMembro projetoMembro = new ProjetoMembro(this, idMembro);
        this.membros.add(projetoMembro);
    }

    public void removerMembro(Long idMembro) {
        this.membros.removeIf(membro -> membro.getIdMembro().equals(idMembro));
    }
}
