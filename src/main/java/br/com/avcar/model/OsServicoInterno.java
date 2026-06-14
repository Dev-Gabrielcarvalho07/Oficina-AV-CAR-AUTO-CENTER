package br.com.avcar.model;

import java.math.BigDecimal;
import java.time.LocalTime;

public class OsServicoInterno extends BaseModel<Long> {

    private Long idOs;
    private Long idServicoInterno;
    private Long idPessoa;
    private Integer qtd;
    private BigDecimal valor;
    private LocalTime inicio;
    private LocalTime fim;

    public OsServicoInterno() {}

    public Long getIdOs() { return idOs; }
    public void setIdOs(Long idOs) { this.idOs = idOs; }

    public Long getIdServicoInterno() { return idServicoInterno; }
    public void setIdServicoInterno(Long idServicoInterno) { this.idServicoInterno = idServicoInterno; }

    public Long getIdPessoa() { return idPessoa; }
    public void setIdPessoa(Long idPessoa) { this.idPessoa = idPessoa; }

    public Integer getQtd() { return qtd; }
    public void setQtd(Integer qtd) { this.qtd = qtd; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public LocalTime getInicio() { return inicio; }
    public void setInicio(LocalTime inicio) { this.inicio = inicio; }

    public LocalTime getFim() { return fim; }
    public void setFim(LocalTime fim) { this.fim = fim; }

    @Override
    public String toString() {
        return "OsServicoInterno{id=" + id + ", idOs=" + idOs + ", idServicoInterno=" + idServicoInterno + "}";
    }
}
