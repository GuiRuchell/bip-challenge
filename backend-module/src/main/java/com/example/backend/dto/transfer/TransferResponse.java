package com.example.backend.dto.transfer;

import com.example.backend.dto.transaction.TransactionalRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Resposta detalhada de uma transferência realizada")
public class TransferResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Indica se a transferência foi bem-sucedida", example = "true")
    private Boolean success;

    @Schema(description = "Mensagem descritiva do resultado", example = "Transferência realizada com success")
    private String message;

    @Schema(description = "Detalhes da transação realizada")
    private TransactionalRequest transactional;

    @Override
    public String toString() {
        return "TransferResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", transactional=" + transactional +
                '}';
    }
}
