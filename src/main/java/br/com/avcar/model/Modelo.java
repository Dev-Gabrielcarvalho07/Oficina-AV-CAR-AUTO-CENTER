package br.com.avcar.model;

public class Modelo extends BaseModel<Long> {

    private String nome;
    private Long idMarca;

    public Modelo() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Long getIdMarca() { return idMarca; }
    public void setIdMarca(Long idMarca) { this.idMarca = idMarca; }

    @Override
    public String toString() {
        return "Modelo{id=" + id + ", nome='" + nome + "', idMarca=" + idMarca + "}";
    }
}
