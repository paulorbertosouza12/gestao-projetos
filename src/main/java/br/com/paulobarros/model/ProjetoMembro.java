package br.com.paulobarros.model;

import jakarta.persistence.*;

@Entity
@Table(name = "projeto_membro")
@lombok.Data
public class ProjetoMembro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto")
    private Projeto projeto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_membro")
    private Membro membro;

}
