package br.com.paulobarros.controller.docs;

import br.com.paulobarros.data.dto.ProjetoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface AlocacaoControllerDocs {



    @Operation(summary = "Remover membro de projeto", tags = "Alocação", responses = {
            @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ProjetoDTO.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    ResponseEntity<ProjetoDTO> removerMembro(@PathVariable Long idProjeto, @PathVariable Long idMembro);


    @Operation(summary = "Aloca membro em projeto", tags = "Alocação", responses = {
            @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ProjetoDTO.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    ResponseEntity<ProjetoDTO> alocarMembro(@PathVariable Long idProjeto, @PathVariable Long idMembro);


    @Operation(summary = "Aloca membro em lote ao projeto", tags = "Alocação", responses = {
            @ApiResponse(
                    description = "Membro alocado com sucesso",
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ProjetoDTO.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    ResponseEntity<ProjetoDTO> alocarMembroLote(@PathVariable Long idProjeto, @RequestBody List<Long> idsMembro);

}

