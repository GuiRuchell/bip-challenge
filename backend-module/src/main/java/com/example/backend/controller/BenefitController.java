package com.example.backend.controller;

import com.example.backend.dto.benefit.BenefitRequest;
import com.example.backend.dto.benefit.BenefitResponse;
import com.example.backend.dto.transfer.TransferRequest;
import com.example.backend.dto.transfer.TransferResponse;
import com.example.backend.exception.ErrorResponse;
import com.example.backend.service.BenefitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/beneficios")
@Tag(name = "Benefícios", description = "API para gerenciamento de benefícios de funcionários")
public class BenefitController {

    private final BenefitService benefitService;

    @GetMapping
    @Operation(summary = "Listar todos os benefícios",
            description = "Retorna uma lista com todos os benefícios cadastrados no sistema (ativos e inativos)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com success",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BenefitResponse.class)))
    })
    public ResponseEntity<List<BenefitResponse>> findAll() {
        return ResponseEntity.ok(benefitService.findAll());
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar benefícios ativos",
            description = "Retorna uma lista com todos os benefícios ativos cadastrados no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com success",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BenefitResponse.class)))
    })
    public ResponseEntity<List<BenefitResponse>> findAllBenefitActives() {
        return ResponseEntity.ok(benefitService.findAllAtivos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar benefício por ID",
            description = "Retorna os detalhes de um benefício específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Benefício encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BenefitResponse.class))),
            @ApiResponse(responseCode = "404", description = "Benefício não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BenefitResponse> findBenefitById(
            @Parameter(description = "ID do benefício", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(benefitService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar novo benefício",
            description = "Cria um novo benefício no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Benefício criado com success",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BenefitResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito - name já existe",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BenefitResponse> createBenefit(
            @Parameter(description = "Dados do novo benefício", required = true)
            @Valid @RequestBody BenefitRequest requestDTO) {

        return ResponseEntity.status(HttpStatus.CREATED).body(benefitService.create(requestDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar benefício",
            description = "Atualiza os dados de um benefício existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Benefício atualizado com success",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BenefitResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Benefício não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito - name já existe",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BenefitResponse> updateBenefit(
            @Parameter(description = "ID do benefício", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Novos dados do benefício", required = true)
            @Valid @RequestBody BenefitRequest requestDTO) {

        return ResponseEntity.ok(benefitService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir benefício",
            description = "Realiza soft delete de um benefício (marca como inativo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Benefício excluído com success"),
            @ApiResponse(responseCode = "404", description = "Benefício não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteBenefit(
            @Parameter(description = "ID do benefício", required = true, example = "1")
            @PathVariable Long id) {
        benefitService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transferir")
    @Operation(summary = "Transferir value entre benefícios",
            description = "Transfere um value do benefício de origem para o de destino")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transferência realizada com success",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransferResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Benefício não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito - saldo insuficiente ou benefício inativo",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<TransferResponse> transfer(
            @Parameter(description = "Dados da transferência", required = true)
            @Valid @RequestBody TransferRequest requestDTO) {

        return ResponseEntity.ok(benefitService.transfer(requestDTO));
    }
}
