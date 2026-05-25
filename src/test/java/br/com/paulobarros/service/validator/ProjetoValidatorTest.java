package br.com.paulobarros.service.validator;

import br.com.paulobarros.data.dto.ProjetoDTO;
import br.com.paulobarros.exception.BusinessException;
import br.com.paulobarros.model.Projeto;
import br.com.paulobarros.model.enums.StatusProjetoEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProjetoValidatorTest {

    private ProjetoValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ProjetoValidator();
    }



    @Test
    void validarCreate_devePassar_quandoProjetoValido() {
        ProjetoDTO dto = criarProjetoDTOValido();
        assertDoesNotThrow(() -> validator.validarCreate(dto));
    }

    @Test
    void validarCreate_deveLancarExcecao_quandoStatusInicialInvalido() {
        ProjetoDTO dto = criarProjetoDTOValido();
        dto.setStatus(StatusProjetoEnum.INICIADO);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validarCreate(dto)
        );

        assertEquals("Todo projeto deve ser criado com o status EM_ANALISE.", ex.getMessage());
    }

    @Test
    void validarCreate_deveLancarExcecao_quandoInicioNulo() {
        ProjetoDTO dto = criarProjetoDTOValido();
        dto.setInicio(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validarCreate(dto)
        );

        assertEquals("A data de início é obrigatória.", ex.getMessage());
    }

    @Test
    void validarCreate_deveLancarExcecao_quandoDataPrevisaoTerminoNula() {
        ProjetoDTO dto = criarProjetoDTOValido();
        dto.setDataPrevisaoTermino(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validarCreate(dto)
        );

        assertEquals("A data de previsão de término é obrigatória.", ex.getMessage());
    }

    @Test
    void validarCreate_deveLancarExcecao_quandoDataPrevisaoAntesDoInicio() {
        ProjetoDTO dto = criarProjetoDTOValido();
        dto.setInicio(LocalDate.of(2024, 1, 10));
        dto.setDataPrevisaoTermino(LocalDate.of(2024, 1, 5));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validarCreate(dto)
        );

        assertEquals("A data de previsão de término não pode ser anterior à data de início.", ex.getMessage());
    }

    @Test
    void validarCreate_deveLancarExcecao_quandoDataRealTerminoAntesDoInicio() {
        ProjetoDTO dto = criarProjetoDTOValido();
        dto.setInicio(LocalDate.of(2024, 1, 10));
        dto.setDataRealTermino(LocalDate.of(2024, 1, 5));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validarCreate(dto)
        );

        assertEquals("A data real de término não pode ser anterior à data de início.", ex.getMessage());
    }

    @Test
    void validarCreate_deveLancarExcecao_quandoOrcamentoNulo() {
        ProjetoDTO dto = criarProjetoDTOValido();
        dto.setOrcamentoTotal(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validarCreate(dto)
        );

        assertEquals("O orçamento total é obrigatório.", ex.getMessage());
    }

    @Test
    void validarCreate_deveLancarExcecao_quandoOrcamentoZero() {
        ProjetoDTO dto = criarProjetoDTOValido();
        dto.setOrcamentoTotal(BigDecimal.ZERO);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validarCreate(dto)
        );

        assertEquals("O orçamento total não pode ser negativo ou zero.", ex.getMessage());
    }

    @Test
    void validarCreate_deveLancarExcecao_quandoOrcamentoNegativo() {
        ProjetoDTO dto = criarProjetoDTOValido();
        dto.setOrcamentoTotal(new BigDecimal("-10.00"));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validarCreate(dto)
        );

        assertEquals("O orçamento total não pode ser negativo ou zero.", ex.getMessage());
    }


    @Test
    void validarUpdate_devePassar_quandoDadosValidosETransicaoPermitida() {
        Projeto projetoAtual = criarProjetoValido();
        projetoAtual.setStatus(StatusProjetoEnum.EM_ANALISE);

        ProjetoDTO projetoAtualizado = criarProjetoDTOValido();
        // Exemplo: transição permitida EM_ANALISE -> INICIADO
        projetoAtualizado.setStatus(StatusProjetoEnum.ANALISE_REALIZADA);

        assertDoesNotThrow(() -> validator.validarUpdate(projetoAtual, projetoAtualizado));
    }

    @Test
    void validarUpdate_deveLancarBusinessException_quandoTransicaoStatusInvalida() {
        Projeto projetoAtual = criarProjetoValido();
        projetoAtual.setStatus(StatusProjetoEnum.ENCERRADO);

        ProjetoDTO projetoAtualizado = criarProjetoDTOValido();
        projetoAtualizado.setStatus(StatusProjetoEnum.EM_ANALISE);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> validator.validarUpdate(projetoAtual, projetoAtualizado)
        );

        assertTrue(ex.getMessage().contains("Transição de status inválida"));
    }


    @Test
    void validarDelete_deveLancarBusinessException_quandoStatusNaoPermiteExclusao() {
        Projeto projeto = criarProjetoValido();

        projeto.setStatus(StatusProjetoEnum.EM_ANALISE);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> validator.validarDelete(projeto)
        );

        assertTrue(ex.getMessage().contains("Não é possível excluir projeto em status"));
    }

    @Test
    void validarDelete_naoDeveLancarExcecao_quandoStatusPermiteExclusao() {
        Projeto projeto = criarProjetoValido();
        projeto.setStatus(StatusProjetoEnum.INICIADO);
        assertDoesNotThrow(() -> validator.validarDelete(projeto));
    }


    private ProjetoDTO criarProjetoDTOValido() {
        ProjetoDTO dto = new ProjetoDTO();
        dto.setNome("Projeto Teste");
        dto.setInicio(LocalDate.of(2024, 1, 1));
        dto.setDataPrevisaoTermino(LocalDate.of(2024, 1, 31));
        dto.setDataRealTermino(null);
        dto.setOrcamentoTotal(new BigDecimal("1000.00"));
        dto.setDescricao("Projeto para testes");
        dto.setStatus(StatusProjetoEnum.EM_ANALISE);
        dto.setIdGerenteResponsavel(1L);
        return dto;
    }

    private Projeto criarProjetoValido() {
        Projeto projeto = new Projeto();
        projeto.setId(1L);
        projeto.setNome("Projeto Teste");
        projeto.setInicio(LocalDate.of(2024, 1, 1));
        projeto.setDataPrevisaoTermino(LocalDate.of(2024, 1, 31));
        projeto.setDataRealTermino(null);
        projeto.setOrcamentoTotal(new BigDecimal("1000.00"));
        projeto.setDescricao("Projeto para testes");
        projeto.setStatus(StatusProjetoEnum.EM_ANALISE);
        projeto.setIdGerenteResponsavel(1L);
        return projeto;
    }
}
