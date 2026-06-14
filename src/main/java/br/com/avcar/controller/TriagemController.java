package br.com.avcar.controller;

import br.com.avcar.model.OrdemServico;
import br.com.avcar.service.OrdemServicoService;
import br.com.avcar.service.TriagemService;
import io.javalin.Javalin;

public class TriagemController {

    private final TriagemService triagem = new TriagemService();
    private final OrdemServicoService osService = new OrdemServicoService();

    public TriagemController(Javalin app) {
        app.post("/api/triagem/enfileirar", ctx -> {
            Long idOs = ctx.bodyAsClass(IdOsRequest.class).idOs();
            OrdemServico os = osService.buscarPorId(idOs);
            triagem.enfileirar(os);
            ctx.json(Response.ok(null, "OS #" + os.getNumeroOs() + " enfileirada"));
        });

        app.post("/api/triagem/chamar-proximo", ctx -> {
            if (triagem.tamanho() == 0) {
                ctx.status(400).json(Response.erro("Fila de triagem vazia."));
                return;
            }
            OrdemServico os = triagem.chamarProximo();
            ctx.json(Response.ok(os));
        });

        app.get("/api/triagem", ctx ->
            ctx.json(Response.ok(triagem.listarFila()))
        );
    }

    private record IdOsRequest(Long idOs) {}
}
