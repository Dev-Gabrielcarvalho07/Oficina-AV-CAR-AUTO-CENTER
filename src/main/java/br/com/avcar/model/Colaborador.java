package br.com.avcar.model;

import java.time.LocalDate;

public class Colaborador extends Pessoa {

    private String matricula;
    private LocalDate dataAdmissao;
    private Long idFuncao;

    public Colaborador() {}

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public LocalDate getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(LocalDate dataAdmissao) { this.dataAdmissao = dataAdmissao; }

    public Long getIdFuncao() { return idFuncao; }
    public void setIdFuncao(Long idFuncao) { this.idFuncao = idFuncao; }

    @Override
    public String toString() {
        return "Colaborador{id=" + id + ", matricula='" + matricula + "'}";
    }
}
