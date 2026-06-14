package br.com.avcar.controller;

import br.com.avcar.model.Fornecedor;
import br.com.avcar.service.FornecedorService;
import io.javalin.Javalin;

public class FornecedorController {

    private final FornecedorService service = new FornecedorService();

    public FornecedorController(Javalin app) {
        app.get("/api/fornecedores", ctx ->
            ctx.json(Response.ok(service.listar()))
        );

        app.get("/api/fornecedores/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            ctx.json(Response.ok(service.buscarPorId(id)));
        });

        app.post("/api/fornecedores", ctx -> {
            Fornecedor f = ctx.bodyAsClass(Fornecedor.class);
            ctx.json(Response.ok(service.criar(f)));
        });

        app.put("/api/fornecedores/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            Fornecedor f = ctx.bodyAsClass(Fornecedor.class);
            f.setId(id);
            service.atualizar(f);
            ctx.json(Response.ok(service.buscarPorId(id)));
        });

        app.delete("/api/fornecedores/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            service.remover(id);
            ctx.json(Response.ok(null, "removido"));
        });
    }
}
