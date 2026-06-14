package br.com.avcar.service;

import br.com.avcar.model.Peca;
import br.com.avcar.repository.PecaRepository;
import br.com.avcar.validation.ValidationException;
import java.math.BigDecimal;

public class PecaService extends BaseService<Peca, Long> {

    public PecaService() { super(new PecaRepository()); }

    @Override
    protected void validar(Peca p) {
        if (p.getCodigoNacional() == null || p.getCodigoNacional().isBlank()) {
            throw new ValidationException("Peca: codigoNacional é obrigatório.");
        }
        if (p.getNome() == null || p.getNome().isBlank()) {
            throw new ValidationException("Peca: nome é obrigatório.");
        }
        if (p.getValor() == null) {
            throw new ValidationException("Peca: valor é obrigatório.");
        }
        if (p.getValor().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Peca: valor não pode ser negativo.");
        }
    }
}
