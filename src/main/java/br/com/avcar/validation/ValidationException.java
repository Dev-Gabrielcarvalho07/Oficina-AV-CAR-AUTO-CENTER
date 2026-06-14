package br.com.avcar.validation;

public class ValidationException extends RuntimeException {
    public ValidationException(String mensagem) {
        super(mensagem);
    }
}
