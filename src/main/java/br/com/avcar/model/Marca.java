package br.com.avcar.model;

public class Marca extends BaseModel<Long> {

    private String nome;

    public Marca() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    @Override
    public String toString() {
        return "Marca{id=" + id + ", nome='" + nome + "'}";
    }
}
