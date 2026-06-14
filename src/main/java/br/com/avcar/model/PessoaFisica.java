package br.com.avcar.model;

public class PessoaFisica extends Cliente {

    private String cpf;
    private String rg;

    public PessoaFisica() {}

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }

    @Override
    public String toString() {
        return "PessoaFisica{id=" + id + ", cpf='" + cpf + "'}";
    }
}
