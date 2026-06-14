package br.com.avcar.service;

import br.com.avcar.model.Funcao;
import br.com.avcar.repository.FuncaoRepository;
import br.com.avcar.validation.ValidationException;

public class FuncaoService extends BaseService<Funcao, Long> {

    public FuncaoService() { super(new FuncaoRepository()); }

    @Override
    protected void validar(Funcao f) {
        if (f.getNome() == null || f.getNome().isBlank()) {
            throw new ValidationException("Funcao: nome é obrigatório.");
        }
    }
}
