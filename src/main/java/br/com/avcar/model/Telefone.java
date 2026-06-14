package br.com.avcar.model;

// Chave composta (id_pessoa, numero) — não estende BaseModel
public class Telefone {

    private Long idPessoa;
    private String numero;

    public Telefone() {}

    public Long getIdPessoa() { return idPessoa; }
    public void setIdPessoa(Long idPessoa) { this.idPessoa = idPessoa; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    @Override
    public String toString() {
        return "Telefone{idPessoa=" + idPessoa + ", numero='" + numero + "'}";
    }
}
