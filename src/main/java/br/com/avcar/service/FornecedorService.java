package br.com.avcar.service;

import br.com.avcar.model.Fornecedor;
import br.com.avcar.repository.FornecedorRepository;
import br.com.avcar.validation.ValidationException;

public class FornecedorService extends BaseService<Fornecedor, Long> {

    public FornecedorService() { super(new FornecedorRepository()); }

    @Override
    protected void validar(Fornecedor f) {
        if (f.getNome() == null || f.getNome().isBlank()) {
            throw new ValidationException("Fornecedor: nome é obrigatório.");
        }
        if (f.getTipoFornecedor() == null) {
            throw new ValidationException("Fornecedor: tipoFornecedor é obrigatório.");
        }
    }
}
