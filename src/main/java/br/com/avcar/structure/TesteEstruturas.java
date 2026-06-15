package br.com.avcar.structure;

import br.com.avcar.model.OrdemServico;
import br.com.avcar.structure.OrdenacaoOS.CriterioOrdenacao;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TesteEstruturas {

    public static void main(String[] args) {
        testarFila();
        testarIterator();
        testarOrdenacao();
        testarBusca();
    }

    private static void testarFila() {
        System.out.println("=== A) FilaTriagem ===");
        FilaTriagem<String> fila = new FilaTriagem<>();
        fila.enfileirar("A");
        fila.enfileirar("B");
        fila.enfileirar("C");
        System.out.println("Tamanho: " + fila.tamanho());
        System.out.println("Desenfileirar: " + fila.desenfileirar());
        System.out.println("Desenfileirar: " + fila.desenfileirar());
        System.out.println("Desenfileirar: " + fila.desenfileirar());
        System.out.println("Vazia: " + fila.estaVazia());
        System.out.println();
    }

    private static void testarIterator() {
        System.out.println("=== D) FilaTriagem — ITERATOR (for-each, FIFO) ===");
        FilaTriagem<String> fila = new FilaTriagem<>();
        fila.enfileirar("OS-001");
        fila.enfileirar("OS-002");
        fila.enfileirar("OS-003");
        for (String item : fila) {
            System.out.println("  item: " + item);
        }
        System.out.println("Tamanho apos iteracao (fila intacta): " + fila.tamanho());
        System.out.println();
    }

    private static void testarOrdenacao() {
        System.out.println("=== B) OrdenacaoOS (VALOR) ===");
        List<OrdemServico> lista = new ArrayList<>();
        lista.add(criarOS(1, 300));
        lista.add(criarOS(2, 100));
        lista.add(criarOS(3, 200));

        OrdenacaoOS ord = new OrdenacaoOS();
        List<OrdemServico> ordenada = ord.ordenar(lista, CriterioOrdenacao.VALOR);
        for (OrdemServico os : ordenada) {
            System.out.println("Total: " + os.getTotal());
        }
        System.out.println();
    }

    private static void testarBusca() {
        System.out.println("=== C) BuscaOS (binária por número) ===");
        List<OrdemServico> lista = new ArrayList<>();
        lista.add(criarOSNumero(10));
        lista.add(criarOSNumero(20));
        lista.add(criarOSNumero(30));

        BuscaOS busca = new BuscaOS();
        OrdemServico achou = busca.buscarPorNumero(lista, 20);
        System.out.println("Buscar 20: " + (achou != null ? "achou numeroOs=" + achou.getNumeroOs() : "null"));
        OrdemServico naoAchou = busca.buscarPorNumero(lista, 99);
        System.out.println("Buscar 99: " + (naoAchou != null ? "achou" : "null (não encontrado)"));
    }

    private static OrdemServico criarOS(int numero, int total) {
        OrdemServico os = new OrdemServico();
        os.setNumeroOs(numero);
        os.setTotal(new BigDecimal(total));
        return os;
    }

    private static OrdemServico criarOSNumero(int numero) {
        OrdemServico os = new OrdemServico();
        os.setNumeroOs(numero);
        return os;
    }
}
