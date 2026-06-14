package br.com.avcar.model;

public class Veiculo extends BaseModel<Long> {

    private String placa;
    private String chassi;
    private String cor;
    private Integer anoFabricacao;
    private Integer quilometragem;
    private Long idModelo;

    public Veiculo() {}

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getChassi() { return chassi; }
    public void setChassi(String chassi) { this.chassi = chassi; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public Integer getAnoFabricacao() { return anoFabricacao; }
    public void setAnoFabricacao(Integer anoFabricacao) { this.anoFabricacao = anoFabricacao; }

    public Integer getQuilometragem() { return quilometragem; }
    public void setQuilometragem(Integer quilometragem) { this.quilometragem = quilometragem; }

    public Long getIdModelo() { return idModelo; }
    public void setIdModelo(Long idModelo) { this.idModelo = idModelo; }

    @Override
    public String toString() {
        return "Veiculo{id=" + id + ", placa='" + placa + "', idModelo=" + idModelo + "}";
    }
}
