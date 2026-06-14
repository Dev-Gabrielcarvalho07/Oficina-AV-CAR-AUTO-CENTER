package br.com.avcar.controller;

import br.com.avcar.model.Cliente;
import br.com.avcar.model.PessoaFisica;
import br.com.avcar.model.PessoaJuridica;
import br.com.avcar.service.ClienteService;
import io.javalin.Javalin;
import java.util.Optional;

public class ClienteController {

    private final ClienteService service = new ClienteService();

    public ClienteController(Javalin app) {
        app.get("/api/clientes", ctx ->
            ctx.json(Response.ok(service.listarTodos()))
        );

        app.get("/api/clientes/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            Optional<Cliente> cliente = service.buscarPorId(id);
            if (cliente.isEmpty()) {
                ctx.status(404).json(Response.erro("Cliente não encontrado: id=" + id));
                return;
            }
            ctx.json(Response.ok(cliente.get()));
        });

        app.post("/api/clientes/pessoa-fisica", ctx -> {
            PessoaFisica pf = ctx.bodyAsClass(PessoaFisica.class);
            ctx.json(Response.ok(service.cadastrar(pf)));
        });

        app.post("/api/clientes/pessoa-juridica", ctx -> {
            PessoaJuridica pj = ctx.bodyAsClass(PessoaJuridica.class);
            ctx.json(Response.ok(service.cadastrar(pj)));
        });

        app.put("/api/clientes/pessoa-fisica/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            PessoaFisica pf = ctx.bodyAsClass(PessoaFisica.class);
            pf.setId(id);
            service.atualizar(pf);
            ctx.json(Response.ok(service.buscarPorId(id).orElse(null)));
        });

        app.put("/api/clientes/pessoa-juridica/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            PessoaJuridica pj = ctx.bodyAsClass(PessoaJuridica.class);
            pj.setId(id);
            service.atualizar(pj);
            ctx.json(Response.ok(service.buscarPorId(id).orElse(null)));
        });

        app.patch("/api/clientes/{id}/inativar", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            Optional<Cliente> cliente = service.buscarPorId(id);
            if (cliente.isEmpty()) {
                ctx.status(404).json(Response.erro("Cliente não encontrado: id=" + id));
                return;
            }
            service.inativar(cliente.get());
            ctx.json(Response.ok(null, "cliente inativado"));
        });
    }
}
