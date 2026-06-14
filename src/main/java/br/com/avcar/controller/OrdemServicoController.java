package br.com.avcar.controller;

import br.com.avcar.model.OrdemServico;
import br.com.avcar.model.OsPeca;
import br.com.avcar.model.OsServicoInterno;
import br.com.avcar.model.dto.FaturamentoDto;
import br.com.avcar.repository.OrdemServicoRepository;
import br.com.avcar.service.OrdemServicoService;
import br.com.avcar.structure.BuscaOS;
import io.javalin.Javalin;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrdemServicoController {

    private final OrdemServicoService   service  = new OrdemServicoService();
    private final BuscaOS               buscaOS  = new BuscaOS();
    private final OrdemServicoRepository osRepo  = new OrdemServicoRepository();

    public OrdemServicoController(Javalin app) {
        app.get("/api/ordens-servico", ctx ->
            ctx.json(Response.ok(service.listar()))
        );

        app.get("/api/ordens-servico/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            ctx.json(Response.ok(service.buscarPorId(id)));
        });

        app.post("/api/ordens-servico", ctx -> {
            OrdemServico os = ctx.bodyAsClass(OrdemServico.class);
            ctx.json(Response.ok(service.abrir(os)));
        });

        app.put("/api/ordens-servico/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            OrdemServico os = ctx.bodyAsClass(OrdemServico.class);
            os.setId(id);
            service.atualizar(os);
            ctx.json(Response.ok(service.buscarPorId(id)));
        });

        app.patch("/api/ordens-servico/{id}/avancar", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            OrdemServico os = service.buscarPorId(id);
            service.avancarStatus(os);
            ctx.json(Response.ok(os));
        });

        app.get("/api/ordens-servico/buscar/{numero}", ctx -> {
            int numero = Integer.parseInt(ctx.pathParam("numero"));
            OrdemServico resultado = buscaOS.buscarPorNumero(service.listar(), numero);
            ctx.json(Response.ok(resultado));
        });

        // RF-002: adicionar serviço interno à OS
        app.post("/api/ordens-servico/{id}/servicos", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            OsServicoInterno osi = ctx.bodyAsClass(OsServicoInterno.class);
            osi.setIdOs(id);
            ctx.json(Response.ok(service.adicionarServico(osi)));
        });

        // RF-003: adicionar peça à OS
        app.post("/api/ordens-servico/{id}/pecas", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            OsPeca osp = ctx.bodyAsClass(OsPeca.class);
            osp.setIdOs(id);
            ctx.json(Response.ok(service.adicionarPeca(osp)));
        });

        // RF-019: histórico de OS por veículo
        app.get("/api/veiculos/{id}/historico", ctx -> {
            Long idVeiculo = Long.parseLong(ctx.pathParam("id"));
            ctx.json(Response.ok(osRepo.findByIdVeiculo(idVeiculo)));
        });

        // RF-021: faturamento por período
        app.get("/api/relatorios/faturamento", ctx -> {
            LocalDate inicio = LocalDate.parse(ctx.queryParam("inicio"));
            LocalDate fim    = LocalDate.parse(ctx.queryParam("fim"));
            List<OrdemServico> ordens = osRepo.findByPeriodo(inicio, fim);
            BigDecimal total = ordens.stream()
                .map(os -> os.getTotal() != null ? os.getTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            ctx.json(Response.ok(new FaturamentoDto(ordens, total)));
        });
    }
}
