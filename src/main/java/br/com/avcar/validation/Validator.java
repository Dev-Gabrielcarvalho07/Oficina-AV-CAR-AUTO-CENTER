package br.com.avcar.validation;

/**
 * PADRÃO: STRATEGY
 * Cada implementação é uma estratégia de validação intercambiável atrás do
 * mesmo contrato: validate() passa silenciosamente quando o valor é válido
 * e lança ValidationException quando não é.
 */
public interface Validator<T> {
    void validate(T valor);
}
