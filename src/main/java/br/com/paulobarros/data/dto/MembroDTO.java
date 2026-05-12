package br.com.paulobarros.data.dto;

import br.com.paulobarros.model.ProjetoMembro;
import br.com.paulobarros.model.enums.AtribuicaoEnum;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@JsonPropertyOrder({"id", "nome", "atribuicao"})
public class MembroDTO {
    private Long id;
    private String nome;
    private AtribuicaoEnum atribuicao;
}
