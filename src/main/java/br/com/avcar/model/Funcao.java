package br.com.avcar.model;

import java.math.BigDecimal;

public class Funcao extends BaseModel<Long> {

    private String nome;
    private BigDecimal salarioBase;

    public Funcao() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public BigDecimal getSalarioBase() { return salarioBase; }
    public void setSalarioBase(BigDecimal salarioBase) { this.salarioBase = salarioBase; }

    @Override
    public String toString() {
        return "Funcao{id=" + id + ", nome='" + nome + "'}";
    }
}
