package br.com.paulobarros.service;

import br.com.paulobarros.data.dto.ClassificacaoRiscoDTO;
import br.com.paulobarros.data.dto.ProjetoDTO;
import br.com.paulobarros.model.enums.ClassificacaoRiscoEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.AssertionsKt.assertNotNull;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class ClassificacaoRiscoServiceTest {

    @InjectMocks
    private ClassificacaoRiscoService classificacaoRiscoService;



    @Test
    void calcular_deveClassificarComoBaixo_quandoOrcamentoBaixoEPrazoCurto() {
        ProjetoDTO projeto = new ProjetoDTO();
        projeto.setId(1L);
        projeto.setNome("Projeto Baixo Risco");
        projeto.setInicio(LocalDate.of(2024, 1, 1));
        projeto.setDataPrevisaoTermino(LocalDate.of(2024, 1, 20));
        projeto.setOrcamentoTotal(new BigDecimal("9000")); // <= 100000

        ClassificacaoRiscoDTO result = classificacaoRiscoService.calcular(projeto);

        assertNotNull(result);
        assertEquals(projeto.getId(), result.getIdProjeto());
        assertEquals(projeto.getNome(), result.getNomeProjeto());
        assertEquals(ClassificacaoRiscoEnum.BAIXO, result.getClassificacao());
        assertEquals(projeto.getOrcamentoTotal(), result.getOrcamentoTotal());
        assertTrue(result.getPrazoEmMeses() > 0); // apenas garantindo que foi calculado
    }

    @Test
    void calcular_deveClassificarComoAlto_quandoOrcamentoAltoEPrazoLongo() {
        ProjetoDTO projeto = new ProjetoDTO();
        projeto.setId(2L);
        projeto.setNome("Projeto Alto Risco");
        projeto.setInicio(LocalDate.of(2024, 1, 1));
        // prazo > 6 meses → diferença grande entre datas
        projeto.setDataPrevisaoTermino(LocalDate.of(2024, 10, 1));
        projeto.setOrcamentoTotal(new BigDecimal("600000")); // > 500000

        ClassificacaoRiscoDTO result = classificacaoRiscoService.calcular(projeto);

        assertNotNull(result);
        assertEquals(projeto.getId(), result.getIdProjeto());
        assertEquals(projeto.getNome(), result.getNomeProjeto());
        assertEquals(ClassificacaoRiscoEnum.ALTO, result.getClassificacao());
        assertEquals(projeto.getOrcamentoTotal(), result.getOrcamentoTotal());
        assertTrue(result.getPrazoEmMeses() > 0);
    }

    @Test
    void calcular_deveClassificarComoMedio_quandoNaoForBaixoNemAlto() {
        ProjetoDTO projeto = new ProjetoDTO();
        projeto.setId(3L);
        projeto.setNome("Projeto Médio Risco");
        projeto.setInicio(LocalDate.of(2024, 1, 1));
        // prazo intermediário
        projeto.setDataPrevisaoTermino(LocalDate.of(2024, 5, 1));
        // orçamento entre 100000 e 500000
        projeto.setOrcamentoTotal(new BigDecimal("300000"));

        ClassificacaoRiscoDTO result = classificacaoRiscoService.calcular(projeto);

        assertNotNull(result);
        assertEquals(projeto.getId(), result.getIdProjeto());
        assertEquals(projeto.getNome(), result.getNomeProjeto());
        assertEquals(ClassificacaoRiscoEnum.MEDIO, result.getClassificacao());
        assertEquals(projeto.getOrcamentoTotal(), result.getOrcamentoTotal());
        assertTrue(result.getPrazoEmMeses() > 0);
    }

}
