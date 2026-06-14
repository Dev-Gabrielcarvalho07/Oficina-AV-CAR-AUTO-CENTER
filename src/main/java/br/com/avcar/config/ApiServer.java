package br.com.avcar.config;

import br.com.avcar.controller.ClienteController;
import br.com.avcar.controller.ColaboradorController;
import br.com.avcar.controller.FornecedorController;
import br.com.avcar.controller.FuncaoController;
import br.com.avcar.controller.MarcaController;
import br.com.avcar.controller.ModeloController;
import br.com.avcar.controller.OrdemServicoController;
import br.com.avcar.controller.PecaController;
import br.com.avcar.controller.Response;
import br.com.avcar.controller.ServicoExternoController;
import br.com.avcar.controller.ServicoInternoController;
import br.com.avcar.controller.TriagemController;
import br.com.avcar.controller.VeiculoController;
import br.com.avcar.validation.ValidationException;
import io.javalin.Javalin;

public class ApiServer {

    private ApiServer() {}

    public static void start() {
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(JsonConfig.mapper());
            config.bundledPlugins.enableCors(cors ->
                cors.addRule(rule -> rule.anyHost())
            );
        });

        app.exception(ValidationException.class, (e, ctx) ->
            ctx.status(400).json(Response.erro(e.getMessage()))
        );
        app.exception(RuntimeException.class, (e, ctx) ->
            ctx.status(500).json(Response.erro(e.getMessage()))
        );

        new MarcaController(app);
        new FuncaoController(app);
        new ModeloController(app);
        new VeiculoController(app);
        new PecaController(app);
        new FornecedorController(app);
        new ServicoInternoController(app);
        new ServicoExternoController(app);
        new ClienteController(app);
        new ColaboradorController(app);
        new OrdemServicoController(app);
        new TriagemController(app);

        app.start(7000);
    }
}
