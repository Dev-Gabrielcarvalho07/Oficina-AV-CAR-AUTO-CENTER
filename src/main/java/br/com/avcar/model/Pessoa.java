package br.com.avcar.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Pessoa extends BaseModel<Long> {

    private String nome;
    private String email;
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private List<Telefone> telefones = new ArrayList<>();

    public Pessoa() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }

    public List<Telefone> getTelefones() { return telefones; }
    public void setTelefones(List<Telefone> telefones) { this.telefones = telefones; }

    @Override
    public String toString() {
        return "Pessoa{id=" + id + ", nome='" + nome + "'}";
    }
}
