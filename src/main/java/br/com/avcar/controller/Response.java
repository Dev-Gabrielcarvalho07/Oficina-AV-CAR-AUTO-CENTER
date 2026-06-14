package br.com.avcar.controller;

// PADRÃO: GENERICS — envelope padronizado para todas as respostas da API
public class Response<T> {

    private final boolean sucesso;
    private final String  mensagem;
    private final T       dados;

    private Response(boolean sucesso, String mensagem, T dados) {
        this.sucesso  = sucesso;
        this.mensagem = mensagem;
        this.dados    = dados;
    }

    public static <T> Response<T> ok(T dados) {
        return new Response<>(true, null, dados);
    }

    public static <T> Response<T> ok(T dados, String mensagem) {
        return new Response<>(true, mensagem, dados);
    }

    public static <T> Response<T> erro(String mensagem) {
        return new Response<>(false, mensagem, null);
    }

    public boolean isSucesso()  { return sucesso; }
    public String  getMensagem(){ return mensagem; }
    public T       getDados()   { return dados; }
}
