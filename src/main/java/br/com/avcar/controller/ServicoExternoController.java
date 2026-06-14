package br.com.avcar.controller;

import br.com.avcar.model.ServicoExterno;
import br.com.avcar.service.ServicoExternoService;
import io.javalin.Javalin;

public class ServicoExternoController {

    private final ServicoExternoService service = new ServicoExternoService();

    public ServicoExternoController(Javalin app) {
        app.get("/api/servicos-externos", ctx ->
            ctx.json(Response.ok(service.listar()))
        );

        app.get("/api/servicos-externos/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            ctx.json(Response.ok(service.buscarPorId(id)));
        });

        app.post("/api/servicos-externos", ctx -> {
            ServicoExterno s = ctx.bodyAsClass(ServicoExterno.class);
            ctx.json(Response.ok(service.criar(s)));
        });

        app.put("/api/servicos-externos/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            ServicoExterno s = ctx.bodyAsClass(ServicoExterno.class);
            s.setId(id);
            service.atualizar(s);
            ctx.json(Response.ok(service.buscarPorId(id)));
        });

        app.delete("/api/servicos-externos/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            service.remover(id);
            ctx.json(Response.ok(null, "removido"));
        });
    }
}
