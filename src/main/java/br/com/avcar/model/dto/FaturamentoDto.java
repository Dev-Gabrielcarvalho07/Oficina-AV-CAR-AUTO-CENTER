package br.com.avcar.model.dto;

import br.com.avcar.model.OrdemServico;
import java.math.BigDecimal;
import java.util.List;

public class FaturamentoDto {

    private final List<OrdemServico> ordens;
    private final BigDecimal totalGeral;

    public FaturamentoDto(List<OrdemServico> ordens, BigDecimal totalGeral) {
        this.ordens = ordens;
        this.totalGeral = totalGeral;
    }

    public List<OrdemServico> getOrdens()      { return ordens; }
    public BigDecimal         getTotalGeral()  { return totalGeral; }
}
