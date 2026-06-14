package br.com.avcar.model;

import br.com.avcar.model.enums.StatusPeca;
import java.math.BigDecimal;

public class OsPeca extends BaseModel<Long> {

    private Long idOs;
    private Long idPeca;
    private Long idFornecedor;
    private Integer qtd;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;
    private StatusPeca statusPeca;

    public OsPeca() {}

    public Long getIdOs() { return idOs; }
    public void setIdOs(Long idOs) { this.idOs = idOs; }

    public Long getIdPeca() { return idPeca; }
    public void setIdPeca(Long idPeca) { this.idPeca = idPeca; }

    public Long getIdFornecedor() { return idFornecedor; }
    public void setIdFornecedor(Long idFornecedor) { this.idFornecedor = idFornecedor; }

    public Integer getQtd() { return qtd; }
    public void setQtd(Integer qtd) { this.qtd = qtd; }

    public BigDecimal getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public StatusPeca getStatusPeca() { return statusPeca; }
    public void setStatusPeca(StatusPeca statusPeca) { this.statusPeca = statusPeca; }

    @Override
    public String toString() {
        return "OsPeca{id=" + id + ", idOs=" + idOs + ", idPeca=" + idPeca + ", status=" + statusPeca + "}";
    }
}
