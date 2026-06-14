package br.com.avcar.controller;

import br.com.avcar.model.Veiculo;
import br.com.avcar.service.VeiculoService;
import io.javalin.Javalin;

public class VeiculoController {

    private final VeiculoService service = new VeiculoService();

    public VeiculoController(Javalin app) {
        app.get("/api/veiculos", ctx ->
            ctx.json(Response.ok(service.listar()))
        );

        app.get("/api/veiculos/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            ctx.json(Response.ok(service.buscarPorId(id)));
        });

        app.post("/api/veiculos", ctx -> {
            Veiculo v = ctx.bodyAsClass(Veiculo.class);
            ctx.json(Response.ok(service.criar(v)));
        });

        app.put("/api/veiculos/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            Veiculo v = ctx.bodyAsClass(Veiculo.class);
            v.setId(id);
            service.atualizar(v);
            ctx.json(Response.ok(service.buscarPorId(id)));
        });

        app.delete("/api/veiculos/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            service.remover(id);
            ctx.json(Response.ok(null, "removido"));
        });
    }
}
