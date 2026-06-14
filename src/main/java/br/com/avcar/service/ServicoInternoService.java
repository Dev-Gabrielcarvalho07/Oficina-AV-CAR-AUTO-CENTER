package br.com.avcar.service;

import br.com.avcar.model.ServicoInterno;
import br.com.avcar.repository.ServicoInternoRepository;
import br.com.avcar.validation.ValidationException;
import java.math.BigDecimal;

public class ServicoInternoService extends BaseService<ServicoInterno, Long> {

    public ServicoInternoService() { super(new ServicoInternoRepository()); }

    @Override
    protected void validar(ServicoInterno s) {
        if (s.getDescricao() == null || s.getDescricao().isBlank()) {
            throw new ValidationException("ServicoInterno: descricao é obrigatória.");
        }
        if (s.getValorBase() == null) {
            throw new ValidationException("ServicoInterno: valorBase é obrigatório.");
        }
        if (s.getValorBase().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("ServicoInterno: valorBase não pode ser negativo.");
        }
    }
}
