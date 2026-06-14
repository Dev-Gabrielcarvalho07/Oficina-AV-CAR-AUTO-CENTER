package br.com.avcar.factory;

import br.com.avcar.model.Cliente;
import br.com.avcar.model.PessoaFisica;

public class PessoaFisicaFactory extends ClienteFactory {
    @Override
    public Cliente criar() { return new PessoaFisica(); }
}
