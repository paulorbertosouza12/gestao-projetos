package br.com.paulobarros.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "membro")
@Data
public class Membro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 80)
    private String nome;

    @Column(name = "cargo", nullable = false, length = 80)
    private String cargo;

    @OneToMany(mappedBy = "membro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjetoMembro> projetos;
}
