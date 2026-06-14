package br.com.avcar.service;

import br.com.avcar.model.Modelo;
import br.com.avcar.repository.ModeloRepository;
import br.com.avcar.validation.ValidationException;

public class ModeloService extends BaseService<Modelo, Long> {

    public ModeloService() { super(new ModeloRepository()); }

    @Override
    protected void validar(Modelo m) {
        if (m.getNome() == null || m.getNome().isBlank()) {
            throw new ValidationException("Modelo: nome é obrigatório.");
        }
        if (m.getIdMarca() == null || m.getIdMarca() <= 0) {
            throw new ValidationException("Modelo: idMarca é obrigatório.");
        }
    }
}
