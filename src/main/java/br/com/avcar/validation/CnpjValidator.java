package br.com.avcar.validation;

public class CnpjValidator implements Validator<String> {

    private static final int[] WEIGHTS_1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] WEIGHTS_2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    @Override
    public void validate(String valor) {
        if (valor == null) {
            throw new ValidationException("CNPJ inválido. Confira os números digitados.");
        }

        String digits = valor.replaceAll("\\D", "");

        if (digits.length() != 14) {
            throw new ValidationException("CNPJ inválido. Confira os números digitados.");
        }

        if (digits.chars().distinct().count() == 1) {
            throw new ValidationException("CNPJ inválido. Confira os números digitados.");
        }

        if (!checkDigit(digits, WEIGHTS_1, 12) || !checkDigit(digits, WEIGHTS_2, 13)) {
            throw new ValidationException("CNPJ inválido. Confira os números digitados.");
        }
    }

    private boolean checkDigit(String digits, int[] weights, int pos) {
        int sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += Character.getNumericValue(digits.charAt(i)) * weights[i];
        }
        int remainder = sum % 11;
        int expected = (remainder < 2) ? 0 : 11 - remainder;
        return Character.getNumericValue(digits.charAt(pos)) == expected;
    }
}
