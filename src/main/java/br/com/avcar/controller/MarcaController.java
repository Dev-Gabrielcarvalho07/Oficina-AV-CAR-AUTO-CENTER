package br.com.avcar.controller;

import br.com.avcar.model.Marca;
import br.com.avcar.service.MarcaService;
import io.javalin.Javalin;

public class MarcaController {

    private final MarcaService service = new MarcaService();

    public MarcaController(Javalin app) {
        app.get("/api/marcas", ctx ->
            ctx.json(Response.ok(service.listar()))
        );

        app.get("/api/marcas/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            ctx.json(Response.ok(service.buscarPorId(id)));
        });

        app.post("/api/marcas", ctx -> {
            Marca m = ctx.bodyAsClass(Marca.class);
            ctx.json(Response.ok(service.criar(m)));
        });

        app.put("/api/marcas/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            Marca m = ctx.bodyAsClass(Marca.class);
            m.setId(id);
            service.atualizar(m);
            ctx.json(Response.ok(service.buscarPorId(id)));
        });

        app.delete("/api/marcas/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            service.remover(id);
            ctx.json(Response.ok(null, "removida"));
        });
    }
}
