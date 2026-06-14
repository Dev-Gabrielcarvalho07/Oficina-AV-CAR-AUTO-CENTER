package br.com.avcar.model;

import java.math.BigDecimal;

public class Peca extends BaseModel<Long> {

    private String codigoNacional;
    private String nome;
    private BigDecimal valor;
    private Integer garantia;

    public Peca() {}

    public String getCodigoNacional() { return codigoNacional; }
    public void setCodigoNacional(String codigoNacional) { this.codigoNacional = codigoNacional; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public Integer getGarantia() { return garantia; }
    public void setGarantia(Integer garantia) { this.garantia = garantia; }

    @Override
    public String toString() {
        return "Peca{id=" + id + ", codigoNacional='" + codigoNacional + "', nome='" + nome + "'}";
    }
}
