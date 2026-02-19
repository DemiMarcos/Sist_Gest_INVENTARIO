package ucan.gestaoInventario.dto.rto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PessoaCreateRequestDTO
{

    @NotBlank(message = "nome é obrigatório")
    @Size(max = 120, message = "nome deve ter no máximo 120 caracteres")
    private String nome;

    // opcional (se vier null, assume true no service)
    private Boolean activo;

    // opcionais
    private List<@Email(message = "email inválido") String> emails;
    private List<@Size(min = 3, max = 40, message = "telefone inválido") String> telefones;
}
