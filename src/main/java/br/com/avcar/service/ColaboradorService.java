package br.com.avcar.service;

import br.com.avcar.model.Colaborador;
import br.com.avcar.repository.ColaboradorRepository;
import br.com.avcar.validation.ValidationException;

public class ColaboradorService extends BaseService<Colaborador, Long> {

    public ColaboradorService() {
        super(new ColaboradorRepository());
    }

    @Override
    protected void validar(Colaborador c) {
        if (c.getNome() == null || c.getNome().isBlank()) {
            throw new ValidationException("Colaborador: nome é obrigatório.");
        }
        if (c.getMatricula() == null || c.getMatricula().isBlank()) {
            throw new ValidationException("Colaborador: matrícula é obrigatória.");
        }
        if (c.getIdFuncao() == null) {
            throw new ValidationException("Colaborador: função é obrigatória.");
        }
    }
}
