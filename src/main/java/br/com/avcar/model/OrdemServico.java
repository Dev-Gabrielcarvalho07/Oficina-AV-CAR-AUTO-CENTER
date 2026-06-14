package br.com.avcar.model;

import br.com.avcar.model.enums.Prioridade;
import br.com.avcar.model.enums.StatusOS;
import java.math.BigDecimal;
import java.time.LocalDate;

public class OrdemServico extends BaseModel<Long> {

    private Integer numeroOs;
    private LocalDate dataEntrada;
    private LocalDate dataSaida;
    private StatusOS statusOs;
    private Prioridade prioridade;
    private BigDecimal valorMaoObra;
    private BigDecimal valorPecas;
    private BigDecimal deslocamento;
    private BigDecimal servicoGuincho;
    private BigDecimal outros;
    private BigDecimal total;
    private Long idPessoa;
    private Long idVeiculo;

    public OrdemServico() {}

    public Integer getNumeroOs() { return numeroOs; }
    public void setNumeroOs(Integer numeroOs) { this.numeroOs = numeroOs; }

    public LocalDate getDataEntrada() { return dataEntrada; }
    public void setDataEntrada(LocalDate dataEntrada) { this.dataEntrada = dataEntrada; }

    public LocalDate getDataSaida() { return dataSaida; }
    public void setDataSaida(LocalDate dataSaida) { this.dataSaida = dataSaida; }

    public StatusOS getStatusOs() { return statusOs; }
    public void setStatusOs(StatusOS statusOs) { this.statusOs = statusOs; }

    public Prioridade getPrioridade() { return prioridade; }
    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade; }

    public BigDecimal getValorMaoObra() { return valorMaoObra; }
    public void setValorMaoObra(BigDecimal valorMaoObra) { this.valorMaoObra = valorMaoObra; }

    public BigDecimal getValorPecas() { return valorPecas; }
    public void setValorPecas(BigDecimal valorPecas) { this.valorPecas = valorPecas; }

    public BigDecimal getDeslocamento() { return deslocamento; }
    public void setDeslocamento(BigDecimal deslocamento) { this.deslocamento = deslocamento; }

    public BigDecimal getServicoGuincho() { return servicoGuincho; }
    public void setServicoGuincho(BigDecimal servicoGuincho) { this.servicoGuincho = servicoGuincho; }

    public BigDecimal getOutros() { return outros; }
    public void setOutros(BigDecimal outros) { this.outros = outros; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public Long getIdPessoa() { return idPessoa; }
    public void setIdPessoa(Long idPessoa) { this.idPessoa = idPessoa; }

    public Long getIdVeiculo() { return idVeiculo; }
    public void setIdVeiculo(Long idVeiculo) { this.idVeiculo = idVeiculo; }

    @Override
    public String toString() {
        return "OrdemServico{id=" + id + ", numeroOs=" + numeroOs + ", status=" + statusOs + "}";
    }
}
