package br.com.paulobarros.service;

import br.com.paulobarros.data.dto.ProjetoDTO;
import br.com.paulobarros.exception.RequiredObjectIsNullException;
import br.com.paulobarros.mock.MockProjeto;
import br.com.paulobarros.model.Projeto;
import br.com.paulobarros.repository.ProjetoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class ProjetoServiceTest {

    MockProjeto input;

    @InjectMocks
    private ProjetoService projetoService;

    @Mock
    ProjetoRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockProjeto();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {
        Projeto entity = new Projeto();
        entity.setId(1L);
        entity.setNome("Projeto Teste");


        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        ProjetoDTO result = projetoService.findById(1L);


        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(1L, result.getId());
        assertEquals(entity.getNome(), result.getNome());
    }

    @Test
    void testeCreateQuandoNull() {

        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> projetoService.create(null));

        String menssagemEsperada = "Não é possível salvar objeto nulo";

        assertEquals(menssagemEsperada, exception.getMessage());

    }

    @Test
    void testeUpdateQuandoNull() {

        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> projetoService.update(null));

        String menssagemEsperada = "Não é possível salvar objeto nulo";

        assertEquals(menssagemEsperada, exception.getMessage());

    }

    @Test
    void create() {
        ProjetoDTO dto = input.mockDTO(1);

        Projeto persisted = new Projeto();
        persisted.setId(1L);
        persisted.setNome(dto.getNome());
        persisted.setStatus(dto.getStatus());
        persisted.setDescricao(dto.getDescricao());
        persisted.setOrcamentoTotal(dto.getOrcamentoTotal());

        when(repository.save(any(Projeto.class))).thenReturn(persisted);

        var result = projetoService.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(1L, result.getId());
        assertEquals(dto.getNome(), result.getNome());
        assertEquals(dto.getStatus(), result.getStatus());
    }

    @Test
    void update() {
        ProjetoDTO dto = input.mockDTO(1);
        dto.setId(1L);
        Projeto persisted = new Projeto();
        persisted.setId(1L);
        persisted.setNome(dto.getNome());
        persisted.setStatus(dto.getStatus());
        persisted.setDescricao(dto.getDescricao());
        persisted.setOrcamentoTotal(dto.getOrcamentoTotal());

        when(repository.findById(1L)).thenReturn(Optional.of(persisted));
        when(repository.save(any(Projeto.class))).thenReturn(persisted);

        var result = projetoService.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(1L, result.getId());
        assertEquals(dto.getNome(), result.getNome());
        assertEquals(dto.getStatus(), result.getStatus());
    }

    @Test
    void delete() {
        Projeto projeto = input.mockEntity(1);
        projeto.setId(1L);


        when(repository.findById(1L)).thenReturn(Optional.of(projeto));
        projetoService.delete(1L);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).delete(any(Projeto.class));
        verifyNoMoreInteractions(repository);

    }

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Projeto> lista = input.mockEntityList();
        Page<Projeto> page = new PageImpl<>(lista, pageable, lista.size());

        when(repository.findAll(pageable)).thenReturn(page);

        Page<ProjetoDTO> result = projetoService.findAll(pageable);

        assertNotNull(result);
        assertEquals(lista.size(), result.getContent().size());
        assertEquals(lista.size(), result.getTotalElements());
    }

}
