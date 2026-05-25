package br.com.paulobarros.service;

import br.com.paulobarros.data.dto.MembroDTO;
import br.com.paulobarros.data.dto.ProjetoDTO;
import br.com.paulobarros.mapper.custom.ProjetoMapper;
import br.com.paulobarros.model.Projeto;
import br.com.paulobarros.model.enums.AtribuicaoEnum;
import br.com.paulobarros.model.enums.StatusProjetoEnum;
import br.com.paulobarros.repository.ProjetoRepository;
import br.com.paulobarros.service.validator.AlocacaoMembroValidator;
import br.com.paulobarros.service.MembroClient; // ajuste o pacote se necessário
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlocacaoMembroServiceTest {

    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private MembroClient membroApiClient;

    @Spy
    private ProjetoMapper converter = new ProjetoMapper();

    @Mock
    private AlocacaoMembroValidator alocacaoMembroValidator;

    @InjectMocks
    @Spy
    private AlocacaoMembroService alocacaoMembroService;

    @Test
    void alocarMembro_deveAlocarComSucesso_quandoProjetoEMembroExistem() {
        Long idProjeto = 1L;
        Long idMembro = 10L;

        Projeto projeto = criarProjetoComStatus(StatusProjetoEnum.EM_ANDAMENTO);
        MembroDTO membro = criarMembroFuncionario(idMembro);

        when(projetoRepository.findById(idProjeto)).thenReturn(Optional.of(projeto));
        when(membroApiClient.buscarPorId(idMembro)).thenReturn(membro);
        when(projetoRepository.save(any(Projeto.class))).thenReturn(projeto);

        ProjetoDTO resultado = assertDoesNotThrow(
                () -> alocacaoMembroService.alocarMembro(idProjeto, idMembro)
        );

        assertNotNull(resultado);
        verify(projetoRepository, times(1)).findById(idProjeto);
        verify(membroApiClient, times(1)).buscarPorId(idMembro);
        verify(alocacaoMembroValidator, times(1))
                .validarAlocacao(eq(projeto), eq(membro));
        verify(projetoRepository, times(1)).save(eq(projeto));
    }

    @Test
    void removerMembro_deveRemoverComSucesso_quandoProjetoExiste() {
        Long idProjeto = 1L;
        Long idMembro = 10L;

        Projeto projeto = criarProjetoComStatus(StatusProjetoEnum.EM_ANDAMENTO);

        when(projetoRepository.findById(idProjeto)).thenReturn(Optional.of(projeto));
        when(projetoRepository.save(any(Projeto.class))).thenReturn(projeto);

        ProjetoDTO resultado = assertDoesNotThrow(
                () -> alocacaoMembroService.removerMembro(idProjeto, idMembro)
        );

        assertNotNull(resultado);
        verify(projetoRepository, times(1)).findById(idProjeto);
        verify(projetoRepository, times(1)).save(eq(projeto));
    }


    private Projeto criarProjetoComStatus(StatusProjetoEnum status) {
        Projeto projeto = new Projeto();
        projeto.setId(1L);
        projeto.setStatus(status);
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
