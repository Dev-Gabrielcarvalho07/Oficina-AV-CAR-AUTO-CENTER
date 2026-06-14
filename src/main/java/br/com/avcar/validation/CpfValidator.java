package br.com.avcar.validation;

public class CpfValidator implements Validator<String> {

    @Override
    public void validate(String valor) {
        if (valor == null) {
            throw new ValidationException("CPF inválido. Confira os números digitados.");
        }

        String digits = valor.replaceAll("\\D", "");

        if (digits.length() != 11) {
            throw new ValidationException("CPF inválido. Confira os números digitados.");
        }

        if (digits.chars().distinct().count() == 1) {
            throw new ValidationException("CPF inválido. Confira os números digitados.");
        }

        if (!checkDigit(digits, 9) || !checkDigit(digits, 10)) {
            throw new ValidationException("CPF inválido. Confira os números digitados.");
        }
    }

    /**
     * Calcula o dígito verificador na posição pos (0-based).
     * Pesos: (pos+1) decrescendo até 2, multiplicados pelos dígitos 0..pos-1.
     */
    private boolean checkDigit(String digits, int pos) {
        int sum = 0;
        int weight = pos + 1;
        for (int i = 0; i < pos; i++) {
            sum += Character.getNumericValue(digits.charAt(i)) * weight--;
        }
        int remainder = sum % 11;
        int expected = (remainder < 2) ? 0 : 11 - remainder;
        return Character.getNumericValue(digits.charAt(pos)) == expected;
    }
}
