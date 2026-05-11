package br.com.paulobarros.data.dto;

import br.com.paulobarros.model.ProjetoMembro;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
public class MembroDTO {


    private Long id;
    private String nome;
    private String cargo;
    private List<ProjetoMembro> projetos;
}
