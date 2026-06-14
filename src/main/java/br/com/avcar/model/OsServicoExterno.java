package br.com.avcar.model;

import java.math.BigDecimal;

public class OsServicoExterno extends BaseModel<Long> {

    private Long idOs;
    private Long idServicoExterno;
    private Long idFornecedor;
    private BigDecimal valor;

    public OsServicoExterno() {}

    public Long getIdOs() { return idOs; }
    public void setIdOs(Long idOs) { this.idOs = idOs; }

    public Long getIdServicoExterno() { return idServicoExterno; }
    public void setIdServicoExterno(Long idServicoExterno) { this.idServicoExterno = idServicoExterno; }

    public Long getIdFornecedor() { return idFornecedor; }
    public void setIdFornecedor(Long idFornecedor) { this.idFornecedor = idFornecedor; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    @Override
    public String toString() {
        return "OsServicoExterno{id=" + id + ", idOs=" + idOs + ", idServicoExterno=" + idServicoExterno + "}";
    }
}
