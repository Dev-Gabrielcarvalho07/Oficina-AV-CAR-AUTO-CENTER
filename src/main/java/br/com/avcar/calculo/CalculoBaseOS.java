package br.com.avcar.calculo;

import br.com.avcar.model.OrdemServico;
import java.math.BigDecimal;

public class CalculoBaseOS implements CalculoOS {

    private final OrdemServico os;

    public CalculoBaseOS(OrdemServico os) {
        this.os = os;
    }

    @Override
    public BigDecimal calcular() {
        return os.getTotal() != null ? os.getTotal() : BigDecimal.ZERO;
    }
}
