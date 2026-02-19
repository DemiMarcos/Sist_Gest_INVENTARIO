package ucan.gestaoInventario.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler
{

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> badRequest(IllegalArgumentException ex)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErroResponse(400, ex.getMessage(), null));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErroResponse> conflict(IllegalStateException ex)
    {
        // usamos 409 para regras de negócio / conflitos
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ErroResponse(409, ex.getMessage(), null));
    }

    /**
     * Quando falta um @RequestParam obrigatório, ex:
     * /inventario/relatorios/produtos (sem prefixo, sem dataRef)
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErroResponse> missingRequestParam(MissingServletRequestParameterException ex)
    {
        String msg = "Parâmetro obrigatório em falta: " + ex.getParameterName();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErroResponse(400, msg, null));
    }

    /**
     * Quando um @RequestParam vem com tipo/formato inválido, ex:
     * dataRef=2026/01/29 (em vez de 2026-01-29T10:00:00)
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> typeMismatch(MethodArgumentTypeMismatchException ex)
    {
        // nome do parâmetro que falhou (ex.: dataRef)
        String param = ex.getName();

        // valor recebido (se existir)
        Object valor = ex.getValue();
        String valorStr = (valor == null) ? "null" : String.valueOf(valor);

        String msg = "Parâmetro inválido: " + param + " = " + valorStr;

        // dica extra se for o caso do teu endpoint (dataRef)
        if ("dataRef".equals(param))
        {
            msg += ". Formato esperado: yyyy-MM-dd'T'HH:mm:ss (ex.: 2026-01-29T10:00:00)";
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErroResponse(400, msg, null));
    }

    /**
     * Quando o Spring não consegue ler/parsear o corpo (JSON inválido etc.)
     * (não é o teu caso no GET, mas deixa o projeto mais "à prova de
     * professor")
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> notReadable(HttpMessageNotReadableException ex)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErroResponse(400, "Corpo da requisição inválido ou ilegível.", null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> validation(MethodArgumentNotValidException ex)
    {
        List<ErroCampo> erros = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fe
            -> erros.add(new ErroCampo(fe.getField(), fe.getDefaultMessage()))
        );

        return ResponseEntity.unprocessableEntity()
            .body(new ErroResponse(422, "Validação falhou", erros));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> generic(Exception ex)
    {
        // Ideal: logar o stacktrace aqui (logger.error), mas mantive teu padrão.
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErroResponse(500, "Erro interno. Verifique os logs.", null));
    }

    public record ErroCampo(String campo, String mensagem)
        {

    }

    public record ErroResponse(int status, String mensagem, List<ErroCampo> erros)
        {

    }
}
