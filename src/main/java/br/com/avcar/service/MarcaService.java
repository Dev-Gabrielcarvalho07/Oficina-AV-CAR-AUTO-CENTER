package br.com.avcar.service;

import br.com.avcar.model.Marca;
import br.com.avcar.repository.MarcaRepository;
import br.com.avcar.validation.ValidationException;

public class MarcaService extends BaseService<Marca, Long> {

    public MarcaService() {
        super(new MarcaRepository());
    }

    @Override
    protected void validar(Marca m) {
        if (m.getNome() == null || m.getNome().isBlank()) {
            throw new ValidationException("Marca: nome é obrigatório.");
        }
    }
}
