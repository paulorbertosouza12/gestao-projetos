package br.com.paulobarros.service.validator;

import br.com.paulobarros.data.dto.MembroDTO;
import br.com.paulobarros.exception.BusinessException;
import br.com.paulobarros.model.Projeto;
import br.com.paulobarros.model.ProjetoMembro;
import br.com.paulobarros.model.enums.AtribuicaoEnum;
import br.com.paulobarros.model.enums.StatusProjetoEnum;
import br.com.paulobarros.repository.ProjetoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlocacaoMembroValidatorTest {

    @InjectMocks
    private AlocacaoMembroValidator validator;

    @Mock
    private ProjetoRepository projetoRepository;




    @Test
    void validarAlocacao_devePassar_quandoDadosValidos() {
        Projeto projeto = criarProjetoComMembros(2); // < 10 membros
        MembroDTO membro = criarMembroFuncionario(1L);

        // Membro com menos de 3 projetos ativos
        when(projetoRepository.countProjetosAtivosByMembroId(
                eq(membro.getId()),
                anyList()
        )).thenReturn(2L);

        assertDoesNotThrow(() -> validator.validarAlocacao(projeto, membro));

        verify(projetoRepository, times(1))
                .countProjetosAtivosByMembroId(eq(membro.getId()), anyList());
    }


    @Test
    void validarAlocacao_deveLancarBusinessException_quandoMembroNaoFuncionario() {
        Projeto projeto = criarProjetoComMembros(2);
        MembroDTO membro = criarMembroFuncionario(1L);
        membro.setAtribuicao(AtribuicaoEnum.GERENTE); // qualquer outro que não seja FUNCIONARIO

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> validator.validarAlocacao(projeto, membro)
        );

        assertEquals("Apenas membros com atribuição FUNCIONARIO podem ser associados ao projeto.",
                ex.getMessage());
        verifyNoInteractions(projetoRepository);
    }

    @Test
    void validarAlocacao_deveLancarBusinessException_quandoLimiteMaximoMembrosAtingido() {
        // 10 membros (>= LIMITE_MAXIMO_MEMBROS)
        Projeto projeto = criarProjetoComMembros(10);
        MembroDTO membro = criarMembroFuncionario(1L);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> validator.validarAlocacao(projeto, membro)
        );

        assertEquals("Limite máximo de membros atingido para o projeto.", ex.getMessage());
    }

    @Test
    void validarAlocacao_deveLancarBusinessException_quandoMembroJaEstaEmMuitosProjetosAtivos() {
        Projeto projeto = criarProjetoComMembros(2);
        MembroDTO membro = criarMembroFuncionario(1L);

        when(projetoRepository.countProjetosAtivosByMembroId(
                eq(membro.getId()),
                anyList()
        )).thenReturn(3L);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> validator.validarAlocacao(projeto, membro)
        );

        assertEquals("O membro não pode estar alocado em mais de 3 projetos ativos", ex.getMessage());

        verify(projetoRepository, times(1))
                .countProjetosAtivosByMembroId(eq(membro.getId()), anyList());
    }




    @Test
    void validarLimiteMinimoMembros_deveLancarBusinessException_quandoMenosOuIgualAoMinimo() throws Exception {
        Projeto projeto = criarProjetoComMembros(1); // <= LIMITE_MINIMO_MEMBROS (1)

        Method method = AlocacaoMembroValidator.class
                .getDeclaredMethod("validarLimiteMinimoMembros", Projeto.class);
        method.setAccessible(true);

        InvocationTargetException ex = assertThrows(
                InvocationTargetException.class,
                () -> method.invoke(validator, projeto)
        );

        assertTrue(ex.getCause() instanceof BusinessException);
        assertEquals("O projeto deve possuir no mínimo 1 membro.", ex.getCause().getMessage());
    }

    @Test
    void validarLimiteMinimoMembros_naoDeveLancarExcecao_quandoAcimaDoMinimo() throws Exception {
        Projeto projeto = criarProjetoComMembros(2); // > 1

        Method method = AlocacaoMembroValidator.class
                .getDeclaredMethod("validarLimiteMinimoMembros", Projeto.class);
        method.setAccessible(true);

        assertDoesNotThrow(() -> method.invoke(validator, projeto));
    }

    @Test
    void validarMembroFuncionario_deveLancarBusinessException_quandoAtribuicaoNaoFuncionario() throws Exception {
        MembroDTO membro = criarMembroFuncionario(1L);
        membro.setAtribuicao(AtribuicaoEnum.GERENTE);

        Method method = AlocacaoMembroValidator.class
                .getDeclaredMethod("validarMembroFuncionario", MembroDTO.class);
        method.setAccessible(true);

        InvocationTargetException ex = assertThrows(
                InvocationTargetException.class,
                () -> method.invoke(validator, membro)
        );

        assertTrue(ex.getCause() instanceof BusinessException);
        assertEquals("Apenas membros com atribuição FUNCIONARIO podem ser associados ao projeto.",
                ex.getCause().getMessage());
    }

    @Test
    void validarMembroFuncionario_naoDeveLancarExcecao_quandoFuncionario() throws Exception {
        MembroDTO membro = criarMembroFuncionario(1L);

        Method method = AlocacaoMembroValidator.class
                .getDeclaredMethod("validarMembroFuncionario", MembroDTO.class);
        method.setAccessible(true);

        assertDoesNotThrow(() -> method.invoke(validator, membro));
    }


    private Projeto criarProjetoComMembros(int quantidade) {
        Projeto projeto = new Projeto();
        projeto.setId(1L);
        projeto.setStatus(StatusProjetoEnum.EM_ANALISE);

        Set<ProjetoMembro> membros = new HashSet<>();
        for (int i = 0; i < quantidade; i++) {
            ProjetoMembro pm = new ProjetoMembro();
            pm.setId((long) i + 1);
            pm.setProjeto(projeto);
            pm.setIdMembro((long) i + 100);
            membros.add(pm);
        }
        projeto.setMembros(membros);

        return projeto;
    }

    private MembroDTO criarMembroFuncionario(Long id) {
        MembroDTO membro = new MembroDTO();
        membro.setId(id);
        membro.setNome("Membro " + id);
        membro.setAtribuicao(AtribuicaoEnum.FUNCIONARIO);
        return membro;
    }
}
