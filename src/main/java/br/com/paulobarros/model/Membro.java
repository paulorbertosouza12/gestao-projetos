package br.com.paulobarros.model;

import br.com.paulobarros.model.enums.AtribuicaoEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "membro")
@Data
public class Membro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_membro")
    private Long id;

    @Column(name = "nome", nullable = false, length = 80)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "atribuicao", nullable = false)
    private AtribuicaoEnum atribuicao;

    @OneToMany(mappedBy = "membro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjetoMembro> projetos;
}
