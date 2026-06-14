package br.com.avcar.controller;

import br.com.avcar.model.Colaborador;
import br.com.avcar.service.ColaboradorService;
import io.javalin.Javalin;

public class ColaboradorController {

    private final ColaboradorService service = new ColaboradorService();

    public ColaboradorController(Javalin app) {
        app.get("/api/colaboradores", ctx ->
            ctx.json(Response.ok(service.listar()))
        );

        app.get("/api/colaboradores/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            ctx.json(Response.ok(service.buscarPorId(id)));
        });

        app.post("/api/colaboradores", ctx -> {
            Colaborador c = ctx.bodyAsClass(Colaborador.class);
            ctx.json(Response.ok(service.criar(c)));
        });

        app.put("/api/colaboradores/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            Colaborador c = ctx.bodyAsClass(Colaborador.class);
            c.setId(id);
            service.atualizar(c);
            ctx.json(Response.ok(service.buscarPorId(id)));
        });

        app.delete("/api/colaboradores/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            service.remover(id);
            ctx.json(Response.ok(null, "removido"));
        });
    }
}
