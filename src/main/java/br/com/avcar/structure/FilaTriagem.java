package br.com.avcar.structure;

import java.util.Iterator;
import java.util.NoSuchElementException;

// PADRÃO: ITERATOR — percorre a fila FIFO sem expor os nós internos.
// Fila FIFO manual p/ ordem de chegada na triagem (RF-022). Sem java.util.Queue.
public class FilaTriagem<T> implements Iterable<T> {

    private static class No<T> {
        T dado;
        No<T> proximo;

        No(T dado) {
            this.dado = dado;
        }
    }

    private No<T> cabeca;
    private No<T> cauda;
    private int tamanho;

    public void enfileirar(T item) {
        No<T> novo = new No<>(item);
        if (cauda != null) {
            cauda.proximo = novo;
        }
        cauda = novo;
        if (cabeca == null) {
            cabeca = novo;
        }
        tamanho++;
    }

    public T desenfileirar() {
        if (estaVazia()) {
            throw new IllegalStateException("Fila de triagem vazia.");
        }
        T dado = cabeca.dado;
        cabeca = cabeca.proximo;
        if (cabeca == null) {
            cauda = null;
        }
        tamanho--;
        return dado;
    }

    public T espiar() {
        if (estaVazia()) {
            throw new IllegalStateException("Fila de triagem vazia.");
        }
        return cabeca.dado;
    }

    public boolean estaVazia() {
        return tamanho == 0;
    }

    public int tamanho() {
        return tamanho;
    }

    @Override
    public Iterator<T> iterator() {
        return new FilaIterator();
    }

    private class FilaIterator implements Iterator<T> {
        private No<T> atual = cabeca;

        @Override
        public boolean hasNext() {
            return atual != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Sem mais elementos na fila.");
            }
            T dado = atual.dado;
            atual = atual.proximo;
            return dado;
        }
    }
}
