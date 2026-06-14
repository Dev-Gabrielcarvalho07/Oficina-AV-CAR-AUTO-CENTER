package br.com.avcar.controller;

import br.com.avcar.model.Modelo;
import br.com.avcar.service.ModeloService;
import io.javalin.Javalin;

public class ModeloController {

    private final ModeloService service = new ModeloService();

    public ModeloController(Javalin app) {
        app.get("/api/modelos", ctx ->
            ctx.json(Response.ok(service.listar()))
        );

        app.get("/api/modelos/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            ctx.json(Response.ok(service.buscarPorId(id)));
        });

        app.post("/api/modelos", ctx -> {
            Modelo m = ctx.bodyAsClass(Modelo.class);
            ctx.json(Response.ok(service.criar(m)));
        });

        app.put("/api/modelos/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            Modelo m = ctx.bodyAsClass(Modelo.class);
            m.setId(id);
            service.atualizar(m);
            ctx.json(Response.ok(service.buscarPorId(id)));
        });

        app.delete("/api/modelos/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            service.remover(id);
            ctx.json(Response.ok(null, "removido"));
        });
    }
}
