package br.com.paulobarros.model.enums;

public enum StatusProjetoEnum {

    EM_ANALISE("Em análise", 1),
    ANALISE_REALIZADA("Análise realizada", 2),
    ANALISE_APROVADA("Análise aprovada", 3),
    INICIADO("Iniciado", 4),
    PLANEJADO("Planejado", 5),
    EM_ANDAMENTO("Em andamento", 6),
    ENCERRADO("Encerrado", 7),
    CANCELADO("Cancelado", 8);

    private final String descricao;
    private final int ordem;

    StatusProjetoEnum(String descricao, int ordem) {
        this.descricao = descricao;
        this.ordem = ordem;
    }

    public String getDescricao() {
        return descricao;
    }
    public int getOrdem() {
        return ordem;
    }


    public boolean permiteTransicaoPara(StatusProjetoEnum novoStatus) {
        if (novoStatus == null) {
            return false;
        }

        if (this == novoStatus) {
            return true;
        }

        if (novoStatus == CANCELADO) {
            return true;
        }

        if (this == CANCELADO) {
            return false;
        }

        if (this == ENCERRADO) {
            return false;
        }

        return novoStatus.getOrdem() == this.ordem + 1 ;
    }

    public boolean permiteExclusao() {
        return this != INICIADO && this != EM_ANDAMENTO && this != ENCERRADO ;
    }
}
