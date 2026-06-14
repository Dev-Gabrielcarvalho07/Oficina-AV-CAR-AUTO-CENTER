package br.com.avcar.controller;

import br.com.avcar.model.Funcao;
import br.com.avcar.service.FuncaoService;
import io.javalin.Javalin;

public class FuncaoController {

    private final FuncaoService service = new FuncaoService();

    public FuncaoController(Javalin app) {
        app.get("/api/funcoes", ctx ->
            ctx.json(Response.ok(service.listar()))
        );

        app.get("/api/funcoes/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            ctx.json(Response.ok(service.buscarPorId(id)));
        });

        app.post("/api/funcoes", ctx -> {
            Funcao f = ctx.bodyAsClass(Funcao.class);
            ctx.json(Response.ok(service.criar(f)));
        });

        app.put("/api/funcoes/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            Funcao f = ctx.bodyAsClass(Funcao.class);
            f.setId(id);
            service.atualizar(f);
            ctx.json(Response.ok(service.buscarPorId(id)));
        });

        app.delete("/api/funcoes/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            service.remover(id);
            ctx.json(Response.ok(null, "removido"));
        });
    }
}
