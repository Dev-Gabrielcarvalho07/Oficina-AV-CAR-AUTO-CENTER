package br.com.avcar.controller;

import br.com.avcar.model.Peca;
import br.com.avcar.service.PecaService;
import io.javalin.Javalin;

public class PecaController {

    private final PecaService service = new PecaService();

    public PecaController(Javalin app) {
        app.get("/api/pecas", ctx ->
            ctx.json(Response.ok(service.listar()))
        );

        app.get("/api/pecas/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            ctx.json(Response.ok(service.buscarPorId(id)));
        });

        app.post("/api/pecas", ctx -> {
            Peca p = ctx.bodyAsClass(Peca.class);
            ctx.json(Response.ok(service.criar(p)));
        });

        app.put("/api/pecas/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            Peca p = ctx.bodyAsClass(Peca.class);
            p.setId(id);
            service.atualizar(p);
            ctx.json(Response.ok(service.buscarPorId(id)));
        });

        app.delete("/api/pecas/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            service.remover(id);
            ctx.json(Response.ok(null, "removido"));
        });
    }
}
