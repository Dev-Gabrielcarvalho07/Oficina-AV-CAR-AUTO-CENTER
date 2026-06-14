package br.com.avcar.repository;

import br.com.avcar.adapter.ResultSetAdapter;
import br.com.avcar.model.HistoricoDonos;
import java.sql.*;

public class HistoricoDonosRepository extends BaseRepository<HistoricoDonos, Long> {

    @Override protected String getTableName()  { return "historico_donos"; }
    @Override protected String getIdColumn()   { return "id_historico"; }

    @Override
    protected HistoricoDonos mapRow(ResultSet rs) throws SQLException {
        return ResultSetAdapter.toHistoricoDonos(rs);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO historico_donos (id_pessoa, id_veiculo, data_inicio, data_fim, atual)"
             + " VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected void setInsertParams(PreparedStatement ps, HistoricoDonos h) throws SQLException {
        ps.setLong(1, h.getIdPessoa());
        ps.setLong(2, h.getIdVeiculo());
        ps.setObject(3, h.getDataInicio());
        ps.setObject(4, h.getDataFim());
        ps.setBoolean(5, h.isAtual());
    }

    @Override
    protected Long extractGeneratedId(ResultSet keys) throws SQLException {
        return keys.getLong(1);
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE historico_donos SET id_pessoa = ?, id_veiculo = ?,"
             + " data_inicio = ?, data_fim = ?, atual = ? WHERE id_historico = ?";
    }

    @Override
    protected void setUpdateParams(PreparedStatement ps, HistoricoDonos h) throws SQLException {
        ps.setLong(1, h.getIdPessoa());
        ps.setLong(2, h.getIdVeiculo());
        ps.setObject(3, h.getDataInicio());
        ps.setObject(4, h.getDataFim());
        ps.setBoolean(5, h.isAtual());
        ps.setLong(6, h.getId());
    }
}
