package br.com.avcar.controller;

import br.com.avcar.model.ServicoInterno;
import br.com.avcar.service.ServicoInternoService;
import io.javalin.Javalin;

public class ServicoInternoController {

    private final ServicoInternoService service = new ServicoInternoService();

    public ServicoInternoController(Javalin app) {
        app.get("/api/servicos-internos", ctx ->
            ctx.json(Response.ok(service.listar()))
        );

        app.get("/api/servicos-internos/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            ctx.json(Response.ok(service.buscarPorId(id)));
        });

        app.post("/api/servicos-internos", ctx -> {
            ServicoInterno s = ctx.bodyAsClass(ServicoInterno.class);
            ctx.json(Response.ok(service.criar(s)));
        });

        app.put("/api/servicos-internos/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            ServicoInterno s = ctx.bodyAsClass(ServicoInterno.class);
            s.setId(id);
            service.atualizar(s);
            ctx.json(Response.ok(service.buscarPorId(id)));
        });

        app.delete("/api/servicos-internos/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            service.remover(id);
            ctx.json(Response.ok(null, "removido"));
        });
    }
}
