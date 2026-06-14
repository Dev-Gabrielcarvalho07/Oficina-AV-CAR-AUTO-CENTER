package br.com.avcar.service;

import br.com.avcar.factory.ClienteFactory;
import br.com.avcar.factory.TipoCliente;
import br.com.avcar.model.*;
import br.com.avcar.repository.PessoaFisicaRepository;
import br.com.avcar.repository.PessoaJuridicaRepository;
import br.com.avcar.validation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteService {

    private final PessoaFisicaRepository  pfRepo = new PessoaFisicaRepository();
    private final PessoaJuridicaRepository pjRepo = new PessoaJuridicaRepository();

    /** Usa a Factory para iniciar um objeto cliente vazio do tipo correto. */
    public Cliente novoVazio(TipoCliente tipo) {
        return ClienteFactory.paraTipo(tipo).criar();
    }

    public PessoaFisica cadastrar(PessoaFisica pf) {
        validarComuns(pf);
        new CpfValidator().validate(pf.getCpf());
        pf.setAtivo(true);
        return pfRepo.insert(pf);
    }

    public PessoaJuridica cadastrar(PessoaJuridica pj) {
        validarComuns(pj);
        new CnpjValidator().validate(pj.getCnpj());
        pj.setAtivo(true);
        return pjRepo.insert(pj);
    }

    public void atualizar(PessoaFisica pf) {
        validarComuns(pf);
        new CpfValidator().validate(pf.getCpf());
        pfRepo.update(pf);
    }

    public void atualizar(PessoaJuridica pj) {
        validarComuns(pj);
        new CnpjValidator().validate(pj.getCnpj());
        pjRepo.update(pj);
    }

    public Optional<Cliente> buscarPorId(Long id) {
        Optional<Cliente> pf = pfRepo.findById(id).map(c -> (Cliente) c);
        if (pf.isPresent()) return pf;
        return pjRepo.findById(id).map(c -> (Cliente) c);
    }

    public List<Cliente> listarTodos() {
        List<Cliente> todos = new ArrayList<>();
        todos.addAll(pfRepo.findAll());
        todos.addAll(pjRepo.findAll());
        return todos;
    }

    /** RD-004: exclusão LÓGICA — nunca chama deleteById. */
    public void inativar(Cliente c) {
        c.setAtivo(false);
        if (c instanceof PessoaFisica pf) {
            pfRepo.update(pf);
        } else if (c instanceof PessoaJuridica pj) {
            pjRepo.update(pj);
        }
    }

    private void validarComuns(Pessoa p) {
        if (p.getNome() == null || p.getNome().isBlank()) {
            throw new ValidationException("Informe o nome.");
        }
        if (p.getCep() == null || p.getCep().isBlank()) {
            throw new ValidationException("Informe o CEP.");
        }
        if (p.getLogradouro() == null || p.getLogradouro().isBlank()) {
            throw new ValidationException("Informe o logradouro.");
        }
    }
}
