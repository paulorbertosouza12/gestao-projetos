package br.com.paulobarros.service;

import br.com.paulobarros.data.dto.ClassificacaoRiscoDTO;
import br.com.paulobarros.data.dto.ProjetoDTO;
import br.com.paulobarros.model.enums.ClassificacaoRiscoEnum;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class ClassificacaoRiscoService {


    public ClassificacaoRiscoDTO calcular(ProjetoDTO projeto){
        LocalDate inicio = projeto.getInicio();
        LocalDate previsaoTermino = projeto.getDataPrevisaoTermino();
        BigDecimal orcamento = projeto.getOrcamentoTotal();

        long prazoEmMeses = calcularPrazoEmMeses(inicio, previsaoTermino);
        ClassificacaoRiscoEnum classificacao = calcularClassificacao(orcamento, prazoEmMeses);

        ClassificacaoRiscoDTO dto = new ClassificacaoRiscoDTO();
        dto.setIdProjeto(projeto.getId());
        dto.setNomeProjeto(projeto.getNome());
        dto.setPrazoEmMeses(prazoEmMeses);
        dto.setOrcamentoTotal(orcamento);
        dto.setClassificacao(classificacao);

        return dto;
    }

    private long calcularPrazoEmMeses(LocalDate inicio, LocalDate fim) {
        long meses = ChronoUnit.MONTHS.between(inicio, fim);
        if (inicio.plusMonths(meses).isBefore(fim)) {
            meses++;
        }
        return meses;
    }

    private ClassificacaoRiscoEnum calcularClassificacao(BigDecimal orcamento, long prazoMeses) {
        BigDecimal limiteBaixo = new BigDecimal(100000);
        BigDecimal limiteAlto = new BigDecimal(500000);

        boolean baixoOrcamento = orcamento.compareTo(limiteBaixo) <= 0;
        boolean prazoCurto = prazoMeses <= 3;
        if (baixoOrcamento && prazoCurto) {
            return ClassificacaoRiscoEnum.BAIXO;
        }

        boolean orcamentoAlto = orcamento.compareTo(limiteAlto) > 0;
        boolean prazoLongo = prazoMeses > 6;
        if (orcamentoAlto && prazoLongo) {
            return ClassificacaoRiscoEnum.ALTO;
        }

        return ClassificacaoRiscoEnum.MEDIO;
    }

}
