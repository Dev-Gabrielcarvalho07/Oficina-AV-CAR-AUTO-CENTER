package br.com.avcar.structure;

import br.com.avcar.model.OrdemServico;
import br.com.avcar.model.enums.Prioridade;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Insertion Sort manual (RF-023). Sem Collections.sort / Arrays.sort / Stream.sorted.
public class OrdenacaoOS {

    public enum CriterioOrdenacao { DATA, VALOR, PRIORIDADE, NUMERO }

    public List<OrdemServico> ordenar(List<OrdemServico> lista, CriterioOrdenacao criterio) {
        List<OrdemServico> resultado = new ArrayList<>(lista);
        int n = resultado.size();

        for (int i = 1; i < n; i++) {
            OrdemServico chave = resultado.get(i);
            int j = i - 1;
            while (j >= 0 && comparar(resultado.get(j), chave, criterio) > 0) {
                resultado.set(j + 1, resultado.get(j));
                j--;
            }
            resultado.set(j + 1, chave);
        }
        return resultado;
    }

    private int comparar(OrdemServico a, OrdemServico b, CriterioOrdenacao criterio) {
        return switch (criterio) {
            case DATA -> {
                if (a.getDataEntrada() == null && b.getDataEntrada() == null) yield 0;
                if (a.getDataEntrada() == null) yield -1;
                if (b.getDataEntrada() == null) yield 1;
                yield a.getDataEntrada().compareTo(b.getDataEntrada());
            }
            case VALOR -> {
                BigDecimal va = a.getTotal() != null ? a.getTotal() : BigDecimal.ZERO;
                BigDecimal vb = b.getTotal() != null ? b.getTotal() : BigDecimal.ZERO;
                yield va.compareTo(vb);
            }
            case PRIORIDADE -> ordemPrioridade(a.getPrioridade()) - ordemPrioridade(b.getPrioridade());
            case NUMERO -> {
                int na = a.getNumeroOs() != null ? a.getNumeroOs() : 0;
                int nb = b.getNumeroOs() != null ? b.getNumeroOs() : 0;
                yield na - nb;
            }
        };
    }

    // BAIXA=0 < NORMAL=1 < ALTA=2 < URGENTE=3
    private int ordemPrioridade(Prioridade p) {
        if (p == null) return 0;
        return switch (p) {
            case BAIXA   -> 0;
            case NORMAL  -> 1;
            case ALTA    -> 2;
            case URGENTE -> 3;
        };
    }
}
