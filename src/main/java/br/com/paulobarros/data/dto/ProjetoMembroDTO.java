package br.com.paulobarros.data.dto;


import lombok.Data;

@Data
public class ProjetoMembroDTO {


    private Long id;
    private ProjetoDTO projeto;
    private MembroDTO membro;
}
