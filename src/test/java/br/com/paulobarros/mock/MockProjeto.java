package br.com.paulobarros.mock;

import br.com.paulobarros.data.dto.ProjetoDTO;
import br.com.paulobarros.model.Projeto;
import br.com.paulobarros.model.StatusProjetoEnum;

import java.util.ArrayList;
import java.util.List;

public class MockProjeto {

    public static final Long ID = 1L;

    public Projeto mockEntity(){
        return mockEntity(0);
    }

    public ProjetoDTO mockDTO() {
        return mockDTO(0);
    }


    public Projeto mockEntity(Integer id){
        Projeto projeto = new Projeto();
        projeto.setId(id.longValue());
        projeto.setNome("Nome Teste");
        return projeto;
    }

    public ProjetoDTO mockDTO(Integer number){
        ProjetoDTO projetoDTO = new ProjetoDTO();
        projetoDTO.setId(number.longValue());
        projetoDTO.setNome("Nome Teste");
        projetoDTO.setStatus(StatusProjetoEnum.EM_ANALISE);
        return projetoDTO;
    }

    public List<Projeto> mockEntityList() {
        List<Projeto> persons = new ArrayList<Projeto>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockEntity());
        }
        return persons;
    }

    public List<ProjetoDTO> mockDTOList() {
        List<ProjetoDTO> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockDTO());
        }
        return persons;
    }
}
