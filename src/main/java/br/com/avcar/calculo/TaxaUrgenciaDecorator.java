package br.com.avcar.calculo;

import br.com.avcar.model.enums.Prioridade;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class TaxaUrgenciaDecorator extends CalculoDecorator {

    private static final BigDecimal PERCENTUAL_URGENCIA = new BigDecimal("0.15");

    private final Prioridade prioridade;

    public TaxaUrgenciaDecorator(CalculoOS interno, Prioridade prioridade) {
        super(interno);
        this.prioridade = prioridade;
    }

    @Override
    public BigDecimal calcular() {
        BigDecimal base = interno.calcular();
        if (Prioridade.URGENTE.equals(prioridade)) {
            BigDecimal acrescimo = base.multiply(PERCENTUAL_URGENCIA)
                                       .setScale(2, RoundingMode.HALF_UP);
            return base.add(acrescimo).setScale(2, RoundingMode.HALF_UP);
        }
        return base;
    }
}
