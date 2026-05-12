package br.com.paulobarros.service;


import br.com.paulobarros.data.dto.MembroDTO;
import br.com.paulobarros.exception.BusinessException;
import br.com.paulobarros.model.enums.AtribuicaoEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
public class MembroClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;


    public MembroClient(RestTemplate restTemplate,
                        @Value("${api.membros.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public MembroDTO create(String nome, String atribuicao){
        MembroDTO request = new MembroDTO();
        request.setNome(nome);
        request.setAtribuicao(AtribuicaoEnum.valueOf(atribuicao));

        String url = this.baseUrl + "/membro";
        MembroDTO response = restTemplate.postForObject(url, request, MembroDTO.class);

        if (response == null || response.getId() == null)
            throw new BusinessException("Erro ao criar membro.");

        return response;
    }

    public MembroDTO buscarPorId(Long idMembro) {
        String url = this.baseUrl + "/membros/" + idMembro;

        MembroDTO response = restTemplate.getForObject(url, MembroDTO.class);

        if (response == null) {
            throw new BusinessException("Membro não encontrado.");
        }

        return response;
    }


}
