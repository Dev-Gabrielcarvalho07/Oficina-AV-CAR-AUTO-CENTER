package br.com.avcar.model;

import java.time.LocalDate;

public class HistoricoDonos extends BaseModel<Long> {

    private Long idPessoa;
    private Long idVeiculo;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private boolean atual;

    public HistoricoDonos() {}

    public Long getIdPessoa() { return idPessoa; }
    public void setIdPessoa(Long idPessoa) { this.idPessoa = idPessoa; }

    public Long getIdVeiculo() { return idVeiculo; }
    public void setIdVeiculo(Long idVeiculo) { this.idVeiculo = idVeiculo; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public boolean isAtual() { return atual; }
    public void setAtual(boolean atual) { this.atual = atual; }

    @Override
    public String toString() {
        return "HistoricoDonos{id=" + id + ", idPessoa=" + idPessoa + ", idVeiculo=" + idVeiculo + "}";
    }
}
