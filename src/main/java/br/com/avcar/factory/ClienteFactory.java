package br.com.avcar.factory;

import br.com.avcar.model.Cliente;

/**
 * PADRÃO: FACTORY METHOD
 * A decisão de qual classe concreta instanciar (PessoaFisica ou PessoaJuridica)
 * fica encapsulada aqui. Quem chama usa ClienteFactory.paraTipo(tipo).criar()
 * sem precisar dar new diretamente na subclasse.
 */
public abstract class ClienteFactory {

    public abstract Cliente criar();

    public static ClienteFactory paraTipo(TipoCliente tipo) {
        return switch (tipo) {
            case FISICA   -> new PessoaFisicaFactory();
            case JURIDICA -> new PessoaJuridicaFactory();
        };
    }
}
