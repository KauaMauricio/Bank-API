package br.com.scheduletransfer.controller;

import br.com.scheduletransfer.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import java.lang.reflect.Method;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApiExceptionHandlerTest {

    private ApiExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ApiExceptionHandler();
    }

    @Test
    void deveRetornar404QuandoRecursoNaoForEncontrado() {
        ResourceNotFoundException exception =
                new ResourceNotFoundException("Transferência não encontrada");

        ResponseEntity<Map<String, Object>> response =
                handler.notFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().get("status"));
        assertEquals("Not Found", response.getBody().get("error"));
        assertEquals("Transferência não encontrada",
                response.getBody().get("message"));
        assertNotNull(response.getBody().get("timestamp"));
    }

    @Test
    void deveRetornar400QuandoArgumentoForInvalido() {
        IllegalArgumentException exception =
                new IllegalArgumentException("Valor inválido");

        ResponseEntity<Map<String, Object>> response =
                handler.badRequest(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Bad Request", response.getBody().get("error"));
        assertEquals("Valor inválido",
                response.getBody().get("message"));
    }

    @Test
    void deveRetornar400QuandoEstadoForInvalido() {
        IllegalStateException exception =
                new IllegalStateException("Estado inválido");

        ResponseEntity<Map<String, Object>> response =
                handler.badRequest(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Estado inválido",
                response.getBody().get("message"));
    }

    @Test
    void deveRetornar400QuandoValidacaoFalhar() throws NoSuchMethodException {
        Object objeto = new Object();

        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(objeto, "transferRequest");

        bindingResult.addError(
                new FieldError(
                        "transferRequest",
                        "valor",
                        "deve ser maior que zero"
                )
        );

        Method metodo = ApiExceptionHandlerTest.class
                .getDeclaredMethod("deveRetornar400QuandoValidacaoFalhar");

        MethodParameter parametro = new MethodParameter(metodo, -1);

        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(parametro, bindingResult);

        ResponseEntity<Map<String, Object>> response =
                handler.validation(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertEquals(
                "valor: deve ser maior que zero",
                response.getBody().get("message")
        );
    }

}