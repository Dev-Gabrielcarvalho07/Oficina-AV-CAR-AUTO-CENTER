package br.com.avcar.calculo;

import java.math.BigDecimal;

// PADRÃO: DECORATOR — adiciona acréscimos ao cálculo da OS sem alterar a classe.
public abstract class CalculoDecorator implements CalculoOS {

    protected final CalculoOS interno;

    protected CalculoDecorator(CalculoOS interno) {
        this.interno = interno;
    }

    @Override
    public BigDecimal calcular() {
        return interno.calcular();
    }
}
