package br.com.avcar.validation;

import br.com.avcar.model.OrdemServico;
import java.math.BigDecimal;

public class OrdemServicoValidator implements Validator<OrdemServico> {

    @Override
    public void validate(OrdemServico os) {
        if (os == null) {
            throw new ValidationException("Ordem de serviço inválida: objeto nulo.");
        }

        // RD-001: cliente e veículo obrigatórios
        if (os.getIdPessoa() == null || os.getIdPessoa() <= 0) {
            throw new ValidationException("O campo Cliente é obrigatório.");
        }
        if (os.getIdVeiculo() == null || os.getIdVeiculo() <= 0) {
            throw new ValidationException("O campo Veículo é obrigatório.");
        }

        // Total não pode ser negativo
        assertNaoNegativo(os.getTotal(), "total");

        // Campos financeiros opcionais não podem ser negativos
        assertNaoNegativo(os.getValorMaoObra(),    "mão de obra");
        assertNaoNegativo(os.getValorPecas(),      "peças");
        assertNaoNegativo(os.getDeslocamento(),    "deslocamento");
        assertNaoNegativo(os.getServicoGuincho(),  "serviço de guincho");

        // RD-011: "outros" PODE ser negativo (desconto) — sem validação de sinal
    }

    private void assertNaoNegativo(BigDecimal valor, String label) {
        if (valor != null && valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("O valor de " + label + " não pode ser negativo.");
        }
    }
}
