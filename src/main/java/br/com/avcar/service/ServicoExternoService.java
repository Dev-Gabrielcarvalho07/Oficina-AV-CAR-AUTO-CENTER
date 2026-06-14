package br.com.avcar.service;

import br.com.avcar.model.ServicoExterno;
import br.com.avcar.repository.ServicoExternoRepository;
import br.com.avcar.validation.ValidationException;
import java.math.BigDecimal;

public class ServicoExternoService extends BaseService<ServicoExterno, Long> {

    public ServicoExternoService() { super(new ServicoExternoRepository()); }

    @Override
    protected void validar(ServicoExterno s) {
        if (s.getDescricao() == null || s.getDescricao().isBlank()) {
            throw new ValidationException("ServicoExterno: descricao é obrigatória.");
        }
        if (s.getValorBase() == null) {
            throw new ValidationException("ServicoExterno: valorBase é obrigatório.");
        }
        if (s.getValorBase().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("ServicoExterno: valorBase não pode ser negativo.");
        }
    }
}
