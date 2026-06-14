package br.com.avcar.model;

public class PessoaJuridica extends Cliente {

    private String cnpj;
    private String inscricaoEstadual;
    private String nomeFantasia;
    private String nomeContato;

    public PessoaJuridica() {}

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getInscricaoEstadual() { return inscricaoEstadual; }
    public void setInscricaoEstadual(String inscricaoEstadual) { this.inscricaoEstadual = inscricaoEstadual; }

    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }

    public String getNomeContato() { return nomeContato; }
    public void setNomeContato(String nomeContato) { this.nomeContato = nomeContato; }

    @Override
    public String toString() {
        return "PessoaJuridica{id=" + id + ", cnpj='" + cnpj + "', nomeFantasia='" + nomeFantasia + "'}";
    }
}
