package ucan.gestaoInventario.dto.rto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PessoaUpdateRequestDTO
{

    @Size(max = 120, message = "nome deve ter no máximo 120 caracteres")
    private String nome;

    private Boolean activo;

    // Se vier null -> não mexe.
    // Se vier lista vazia -> apaga todos.
    private List<@Email(message = "email inválido") String> emails;
    private List<@Size(min = 3, max = 40, message = "telefone inválido") String> telefones;
}
