package br.com.avcar.structure;

import br.com.avcar.model.OrdemServico;
import br.com.avcar.structure.OrdenacaoOS.CriterioOrdenacao;
import java.util.ArrayList;
import java.util.List;

// Busca binária por número (O(log n)) + sequencial (RF-024). Sem Stream/Collections.
public class BuscaOS {

    private final OrdenacaoOS ordenacao = new OrdenacaoOS();

    public OrdemServico buscarPorNumero(List<OrdemServico> lista, int numeroOs) {
        List<OrdemServico> ordenada = ordenacao.ordenar(lista, CriterioOrdenacao.NUMERO);
        int esq = 0;
        int dir = ordenada.size() - 1;
        while (esq <= dir) {
            int meio = esq + (dir - esq) / 2;
            int num = ordenada.get(meio).getNumeroOs() != null ? ordenada.get(meio).getNumeroOs() : 0;
            if (num == numeroOs) return ordenada.get(meio);
            if (num < numeroOs)  esq = meio + 1;
            else                 dir = meio - 1;
        }
        return null;
    }

    public List<OrdemServico> buscarPorIdVeiculo(List<OrdemServico> lista, Long idVeiculo) {
        List<OrdemServico> resultado = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {
            OrdemServico os = lista.get(i);
            if (idVeiculo != null && idVeiculo.equals(os.getIdVeiculo())) {
                resultado.add(os);
            }
        }
        return resultado;
    }

    public List<OrdemServico> buscarPorIdCliente(List<OrdemServico> lista, Long idPessoa) {
        List<OrdemServico> resultado = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {
            OrdemServico os = lista.get(i);
            if (idPessoa != null && idPessoa.equals(os.getIdPessoa())) {
                resultado.add(os);
            }
        }
        return resultado;
    }
}
