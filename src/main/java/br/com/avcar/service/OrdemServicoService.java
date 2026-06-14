package br.com.avcar.service;

import br.com.avcar.model.OrdemServico;
import br.com.avcar.model.OsPeca;
import br.com.avcar.model.OsServicoInterno;
import br.com.avcar.model.enums.Prioridade;
import br.com.avcar.model.enums.StatusOS;
import br.com.avcar.repository.OrdemServicoRepository;
import br.com.avcar.repository.OsPecaRepository;
import br.com.avcar.repository.OsServicoInternoRepository;
import br.com.avcar.validation.OrdemServicoValidator;
import br.com.avcar.validation.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class OrdemServicoService extends BaseService<OrdemServico, Long> {

    private final OrdemServicoRepository   osRepo;
    private final OsServicoInternoRepository osiRepo = new OsServicoInternoRepository();
    private final OsPecaRepository           opRepo  = new OsPecaRepository();

    public OrdemServicoService() {
        super(new OrdemServicoRepository());
        this.osRepo = (OrdemServicoRepository) repository;
    }

    @Override
    protected void validar(OrdemServico os) {
        new OrdemServicoValidator().validate(os);
    }

    /** RF-001: abre uma OS nova em ORCAMENTO com número sequencial. */
    public OrdemServico abrir(OrdemServico os) {
        os.setStatusOs(StatusOS.ORCAMENTO);
        if (os.getPrioridade() == null) {
            os.setPrioridade(Prioridade.NORMAL);       // RD-009
        }
        os.setDataEntrada(LocalDate.now());
        os.setNumeroOs(osRepo.proximoNumeroOs());
        os.setTotal(calcularTotal(os));
        return criar(os);                              // valida (RD-001) + insere
    }

    /**
     * RF-009 / RD-002: única fonte do total.
     * Soma todos os campos financeiros; nulos = ZERO; "outros" pode ser negativo (RD-011).
     */
    public BigDecimal calcularTotal(OrdemServico os) {
        return sum(os.getValorMaoObra())
             .add(sum(os.getValorPecas()))
             .add(sum(os.getDeslocamento()))
             .add(sum(os.getServicoGuincho()))
             .add(os.getOutros() != null ? os.getOutros() : BigDecimal.ZERO);
    }

    public void recalcularTotal(OrdemServico os) {
        os.setTotal(calcularTotal(os));
        atualizar(os);
    }

    /** RF-008: avança o status na sequência ORCAMENTO→EXECUCAO→PAGAMENTO→FINALIZACAO. */
    public void avancarStatus(OrdemServico os) {
        StatusOS atual = os.getStatusOs();
        StatusOS proximo = switch (atual) {
            case ORCAMENTO  -> StatusOS.EXECUCAO;
            case EXECUCAO   -> StatusOS.PAGAMENTO;
            case PAGAMENTO  -> StatusOS.FINALIZACAO;
            case FINALIZACAO -> throw new ValidationException(
                "OS finalizada não pode avançar de status.");
        };
        os.setStatusOs(proximo);
        osRepo.update(os);
    }

    /** RD-003: OS finalizada é imutável. */
    @Override
    public void atualizar(OrdemServico os) {
        osRepo.findById(os.getId()).ifPresent(persistida -> {
            if (persistida.getStatusOs() == StatusOS.FINALIZACAO) {
                throw new ValidationException("RD-003: OS finalizada é imutável.");
            }
        });
        super.atualizar(os);
    }

    /** RF-002: vincula serviço interno à OS. */
    public OsServicoInterno adicionarServico(OsServicoInterno osi) {
        if (osi.getIdOs() == null)
            throw new ValidationException("RF-002: idOs é obrigatório.");
        if (osi.getIdServicoInterno() == null)
            throw new ValidationException("RF-002: idServicoInterno é obrigatório.");
        if (osi.getIdPessoa() == null)
            throw new ValidationException("RF-002: idPessoa (colaborador) é obrigatório.");
        if (osi.getQtd() == null || osi.getQtd() <= 0)
            throw new ValidationException("RF-002: qtd deve ser maior que zero.");
        return osiRepo.insert(osi);
    }

    /** RF-003: vincula peça à OS. */
    public OsPeca adicionarPeca(OsPeca osp) {
        if (osp.getIdOs() == null)
            throw new ValidationException("RF-003: idOs é obrigatório.");
        if (osp.getIdPeca() == null)
            throw new ValidationException("RF-003: idPeca é obrigatório.");
        if (osp.getIdFornecedor() == null)
            throw new ValidationException("RF-003: idFornecedor é obrigatório.");
        if (osp.getQtd() == null || osp.getQtd() <= 0)
            throw new ValidationException("RF-003: qtd deve ser maior que zero.");
        return opRepo.insert(osp);
    }

    private BigDecimal sum(BigDecimal valor) {
        return valor != null ? valor : BigDecimal.ZERO;
    }
}
