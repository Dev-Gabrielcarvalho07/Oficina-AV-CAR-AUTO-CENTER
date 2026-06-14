package br.com.avcar.model;

import br.com.avcar.model.enums.TipoFornecedor;

public class Fornecedor extends BaseModel<Long> {

    private String nome;
    private String cnpj;
    private TipoFornecedor tipoFornecedor;
    private String telefone;
    private String email;

    public Fornecedor() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public TipoFornecedor getTipoFornecedor() { return tipoFornecedor; }
    public void setTipoFornecedor(TipoFornecedor tipoFornecedor) { this.tipoFornecedor = tipoFornecedor; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Fornecedor{id=" + id + ", nome='" + nome + "', tipo=" + tipoFornecedor + "}";
    }
}
