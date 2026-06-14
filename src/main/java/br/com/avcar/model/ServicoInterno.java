package br.com.avcar.model;

import java.math.BigDecimal;

public class ServicoInterno extends BaseModel<Long> {

    private String descricao;
    private BigDecimal valorBase;
    private Integer garantia;

    public ServicoInterno() {}

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getValorBase() { return valorBase; }
    public void setValorBase(BigDecimal valorBase) { this.valorBase = valorBase; }

    public Integer getGarantia() { return garantia; }
    public void setGarantia(Integer garantia) { this.garantia = garantia; }

    @Override
    public String toString() {
        return "ServicoInterno{id=" + id + ", descricao='" + descricao + "'}";
    }
}
