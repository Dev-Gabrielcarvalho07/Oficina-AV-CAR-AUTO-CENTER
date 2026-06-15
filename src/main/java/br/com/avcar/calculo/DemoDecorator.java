package br.com.avcar.calculo;

import br.com.avcar.model.OrdemServico;
import br.com.avcar.model.enums.Prioridade;
import java.math.BigDecimal;

public class DemoDecorator {

    public static void main(String[] args) {
        OrdemServico osNormal = new OrdemServico();
        osNormal.setNumeroOs(1);
        osNormal.setPrioridade(Prioridade.NORMAL);
        osNormal.setTotal(new BigDecimal("500.00"));

        OrdemServico osUrgente = new OrdemServico();
        osUrgente.setNumeroOs(2);
        osUrgente.setPrioridade(Prioridade.URGENTE);
        osUrgente.setTotal(new BigDecimal("500.00"));

        System.out.println("=== E) PADRÃO DECORATOR — TaxaUrgenciaDecorator ===");

        CalculoOS baseNormal = new CalculoBaseOS(osNormal);
        CalculoOS comTaxaNormal = new TaxaUrgenciaDecorator(baseNormal, osNormal.getPrioridade());
        System.out.println("OS #1 (NORMAL)  — base: " + baseNormal.calcular()
                + " | com taxa: " + comTaxaNormal.calcular());

        CalculoOS baseUrgente = new CalculoBaseOS(osUrgente);
        CalculoOS comTaxaUrgente = new TaxaUrgenciaDecorator(baseUrgente, osUrgente.getPrioridade());
        System.out.println("OS #2 (URGENTE) — base: " + baseUrgente.calcular()
                + " | com taxa: " + comTaxaUrgente.calcular() + "  (+15%)");

        // decoradores empilhados: duas taxas sobre o mesmo base
        CalculoOS duplo = new TaxaUrgenciaDecorator(comTaxaUrgente, Prioridade.URGENTE);
        System.out.println("OS #2 duplo     — base: " + baseUrgente.calcular()
                + " | dois decoradores: " + duplo.calcular());
    }
}
