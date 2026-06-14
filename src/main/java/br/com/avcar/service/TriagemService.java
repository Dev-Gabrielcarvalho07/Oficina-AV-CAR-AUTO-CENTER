package br.com.avcar.service;

import br.com.avcar.model.OrdemServico;
import br.com.avcar.structure.FilaTriagem;
import java.util.ArrayList;
import java.util.List;

public class TriagemService {

    private final FilaTriagem<OrdemServico> filaTriagem = new FilaTriagem<>();

    public void enfileirar(OrdemServico os) {
        filaTriagem.enfileirar(os);
    }

    public OrdemServico chamarProximo() {
        return filaTriagem.desenfileirar();
    }

    public List<OrdemServico> listarFila() {
        // Percorre copiando para lista sem remover da fila original
        FilaTriagem<OrdemServico> copia = new FilaTriagem<>();
        List<OrdemServico> resultado = new ArrayList<>();

        // Esvazia a fila colhendo elementos, depois reinsere
        while (!filaTriagem.estaVazia()) {
            OrdemServico os = filaTriagem.desenfileirar();
            resultado.add(os);
            copia.enfileirar(os);
        }
        while (!copia.estaVazia()) {
            filaTriagem.enfileirar(copia.desenfileirar());
        }
        return resultado;
    }

    public int tamanho() {
        return filaTriagem.tamanho();
    }
}
