package br.com.paulobarros.model.enums;

public enum AtribuicaoEnum {

    GERENTE("Gerente", 1),
    FUNCIONARIO("Funcionário", 2);

    private final String descricao;
    private final int codigo;

    AtribuicaoEnum(String descricao, int codigo) {
        this.descricao = descricao;
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }
    public int getOrdem() {
        return codigo;
    }
}
