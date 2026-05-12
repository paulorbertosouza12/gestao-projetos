package br.com.paulobarros.model;

import br.com.paulobarros.model.enums.AtribuicaoEnum;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "projeto_membro")
public class ProjetoMembro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_projeto_membro")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto")
    private Projeto projeto;

    @Column(name = "id_membro")
    private Long idMembro;


}
