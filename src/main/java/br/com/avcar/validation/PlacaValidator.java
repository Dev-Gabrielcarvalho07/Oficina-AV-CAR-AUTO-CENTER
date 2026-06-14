package br.com.avcar.validation;

import java.util.regex.Pattern;

public class PlacaValidator implements Validator<String> {

    // AAA-0000 ou AAA0000 (formato antigo)
    private static final Pattern ANTIGA   = Pattern.compile("^[A-Za-z]{3}-?[0-9]{4}$");
    // AAA0A00 ou AAA-0A00 (formato Mercosul)
    private static final Pattern MERCOSUL = Pattern.compile("^[A-Za-z]{3}-?[0-9][A-Za-z][0-9]{2}$");

    @Override
    public void validate(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new ValidationException("Placa inválida. Use o formato ABC-1234 ou ABC1D23 (Mercosul).");
        }
        if (!ANTIGA.matcher(valor).matches() && !MERCOSUL.matcher(valor).matches()) {
            throw new ValidationException("Placa inválida. Use o formato ABC-1234 ou ABC1D23 (Mercosul).");
        }
    }
}
