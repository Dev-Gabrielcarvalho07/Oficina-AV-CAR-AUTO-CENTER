package br.com.avcar.service;

import br.com.avcar.model.Veiculo;
import br.com.avcar.repository.VeiculoRepository;
import br.com.avcar.validation.PlacaValidator;
import br.com.avcar.validation.ValidationException;
import java.time.Year;

public class VeiculoService extends BaseService<Veiculo, Long> {

    private static final PlacaValidator PLACA_VALIDATOR = new PlacaValidator();

    public VeiculoService() { super(new VeiculoRepository()); }

    @Override
    protected void validar(Veiculo v) {
        PLACA_VALIDATOR.validate(v.getPlaca());

        if (v.getIdModelo() == null || v.getIdModelo() <= 0) {
            throw new ValidationException("Selecione o modelo do veículo.");
        }
        if (v.getAnoFabricacao() == null) {
            throw new ValidationException("Informe o ano de fabricação.");
        }
        int ano     = v.getAnoFabricacao();
        int anoAtual = Year.now().getValue();
        if (ano < 1900) {
            throw new ValidationException("O ano de fabricação parece inválido (anterior a 1900).");
        }
        if (ano > anoAtual) {
            throw new ValidationException(
                "O ano de fabricação não pode ser maior que o ano atual (" + anoAtual + ").");
        }
        if (v.getQuilometragem() != null && v.getQuilometragem() < 0) {
            throw new ValidationException("A quilometragem não pode ser negativa.");
        }
    }
}
