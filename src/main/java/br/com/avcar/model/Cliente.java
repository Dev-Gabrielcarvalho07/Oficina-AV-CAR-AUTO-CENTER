package br.com.avcar.model;

import java.time.LocalDate;

public class Cliente extends Pessoa {

    private LocalDate dataCadastro;
    private boolean ativo;

    public Cliente() {}

    public LocalDate getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDate dataCadastro) { this.dataCadastro = dataCadastro; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    @Override
    public String toString() {
        return "Cliente{id=" + id + ", dataCadastro=" + dataCadastro + ", ativo=" + ativo + "}";
    }
}
