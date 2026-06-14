package br.com.avcar.factory;

import br.com.avcar.model.Cliente;
import br.com.avcar.model.PessoaJuridica;

public class PessoaJuridicaFactory extends ClienteFactory {
    @Override
    public Cliente criar() { return new PessoaJuridica(); }
}
