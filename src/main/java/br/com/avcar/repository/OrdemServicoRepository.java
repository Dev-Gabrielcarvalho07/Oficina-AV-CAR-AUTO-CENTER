package br.com.avcar.repository;

import br.com.avcar.adapter.ResultSetAdapter;
import br.com.avcar.config.ConnectionFactory;
import br.com.avcar.model.OrdemServico;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdemServicoRepository extends BaseRepository<OrdemServico, Long> {

    @Override protected String getTableName()  { return "ordem_servico"; }
    @Override protected String getIdColumn()   { return "id_os"; }

    @Override
    protected OrdemServico mapRow(ResultSet rs) throws SQLException {
        return ResultSetAdapter.toOrdemServico(rs);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO ordem_servico"
             + " (numero_os, data_entrada, data_saida, status_os, prioridade,"
             + "  valor_mao_obra, valor_pecas, deslocamento, servico_guincho, outros, total,"
             + "  id_pessoa, id_veiculo)"
             + " VALUES (?, ?, ?, ?::status_os_enum, ?::prioridade_enum, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected void setInsertParams(PreparedStatement ps, OrdemServico os) throws SQLException {
        ps.setInt(1,    os.getNumeroOs());
        ps.setObject(2, os.getDataEntrada());
        ps.setObject(3, os.getDataSaida());
        ps.setString(4, os.getStatusOs().name());
        ps.setString(5, os.getPrioridade().name());
        ps.setObject(6, os.getValorMaoObra());
        ps.setObject(7, os.getValorPecas());
        ps.setObject(8, os.getDeslocamento());
        ps.setObject(9, os.getServicoGuincho());
        ps.setObject(10, os.getOutros());
        ps.setObject(11, os.getTotal());
        ps.setLong(12,   os.getIdPessoa());
        ps.setLong(13,   os.getIdVeiculo());
    }

    @Override
    protected Long extractGeneratedId(ResultSet keys) throws SQLException {
        return keys.getLong(1);
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE ordem_servico SET"
             + " numero_os = ?, data_entrada = ?, data_saida = ?,"
             + " status_os = ?::status_os_enum, prioridade = ?::prioridade_enum,"
             + " valor_mao_obra = ?, valor_pecas = ?, deslocamento = ?,"
             + " servico_guincho = ?, outros = ?, total = ?,"
             + " id_pessoa = ?, id_veiculo = ?"
             + " WHERE id_os = ?";
    }

    @Override
    protected void setUpdateParams(PreparedStatement ps, OrdemServico os) throws SQLException {
        ps.setInt(1,    os.getNumeroOs());
        ps.setObject(2, os.getDataEntrada());
        ps.setObject(3, os.getDataSaida());
        ps.setString(4, os.getStatusOs().name());
        ps.setString(5, os.getPrioridade().name());
        ps.setObject(6, os.getValorMaoObra());
        ps.setObject(7, os.getValorPecas());
        ps.setObject(8, os.getDeslocamento());
        ps.setObject(9, os.getServicoGuincho());
        ps.setObject(10, os.getOutros());
        ps.setObject(11, os.getTotal());
        ps.setLong(12,   os.getIdPessoa());
        ps.setLong(13,   os.getIdVeiculo());
        ps.setLong(14,   os.getId());
    }

    public List<OrdemServico> findByIdVeiculo(Long idVeiculo) {
        String sql = "SELECT * FROM ordem_servico WHERE id_veiculo = ? ORDER BY data_entrada";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idVeiculo);
            try (ResultSet rs = ps.executeQuery()) {
                List<OrdemServico> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapRow(rs));
                return lista;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar OS por veículo: " + e.getMessage(), e);
        }
    }

    public List<OrdemServico> findByPeriodo(LocalDate inicio, LocalDate fim) {
        String sql = "SELECT * FROM ordem_servico WHERE data_entrada BETWEEN ? AND ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, inicio);
            ps.setObject(2, fim);
            try (ResultSet rs = ps.executeQuery()) {
                List<OrdemServico> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapRow(rs));
                return lista;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar OS por período: " + e.getMessage(), e);
        }
    }

    public int proximoNumeroOs() {
        String sql = "SELECT COALESCE(MAX(numero_os), 0) + 1 FROM ordem_servico";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 1;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter próximo número de OS: " + e.getMessage(), e);
        }
    }
}
