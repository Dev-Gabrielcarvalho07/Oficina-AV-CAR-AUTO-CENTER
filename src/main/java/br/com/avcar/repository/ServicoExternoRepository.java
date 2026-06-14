package br.com.avcar.repository;

import br.com.avcar.adapter.ResultSetAdapter;
import br.com.avcar.model.ServicoExterno;
import java.sql.*;

public class ServicoExternoRepository extends BaseRepository<ServicoExterno, Long> {

    @Override protected String getTableName()  { return "servico_externo"; }
    @Override protected String getIdColumn()   { return "id_servico_externo"; }

    @Override
    protected ServicoExterno mapRow(ResultSet rs) throws SQLException {
        return ResultSetAdapter.toServicoExterno(rs);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO servico_externo (descricao, valor_base, garantia) VALUES (?, ?, ?)";
    }

    @Override
    protected void setInsertParams(PreparedStatement ps, ServicoExterno s) throws SQLException {
        ps.setString(1, s.getDescricao());
        ps.setObject(2, s.getValorBase());
        ps.setObject(3, s.getGarantia());
    }

    @Override
    protected Long extractGeneratedId(ResultSet keys) throws SQLException {
        return keys.getLong(1);
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE servico_externo SET descricao = ?, valor_base = ?, garantia = ?"
             + " WHERE id_servico_externo = ?";
    }

    @Override
    protected void setUpdateParams(PreparedStatement ps, ServicoExterno s) throws SQLException {
        ps.setString(1, s.getDescricao());
        ps.setObject(2, s.getValorBase());
        ps.setObject(3, s.getGarantia());
        ps.setLong(4, s.getId());
    }
}
