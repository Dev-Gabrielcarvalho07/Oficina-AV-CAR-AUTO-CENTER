package br.com.avcar.repository;

import br.com.avcar.adapter.ResultSetAdapter;
import br.com.avcar.model.OsServicoExterno;
import java.sql.*;

public class OsServicoExternoRepository extends BaseRepository<OsServicoExterno, Long> {

    @Override protected String getTableName()  { return "os_servico_externo"; }
    @Override protected String getIdColumn()   { return "id_os_servico_externo"; }

    @Override
    protected OsServicoExterno mapRow(ResultSet rs) throws SQLException {
        return ResultSetAdapter.toOsServicoExterno(rs);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO os_servico_externo (id_os, id_servico_externo, id_fornecedor, valor)"
             + " VALUES (?, ?, ?, ?)";
    }

    @Override
    protected void setInsertParams(PreparedStatement ps, OsServicoExterno ose) throws SQLException {
        ps.setLong(1, ose.getIdOs());
        ps.setLong(2, ose.getIdServicoExterno());
        ps.setLong(3, ose.getIdFornecedor());
        ps.setObject(4, ose.getValor());
    }

    @Override
    protected Long extractGeneratedId(ResultSet keys) throws SQLException {
        return keys.getLong(1);
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE os_servico_externo SET id_os = ?, id_servico_externo = ?,"
             + " id_fornecedor = ?, valor = ? WHERE id_os_servico_externo = ?";
    }

    @Override
    protected void setUpdateParams(PreparedStatement ps, OsServicoExterno ose) throws SQLException {
        ps.setLong(1, ose.getIdOs());
        ps.setLong(2, ose.getIdServicoExterno());
        ps.setLong(3, ose.getIdFornecedor());
        ps.setObject(4, ose.getValor());
        ps.setLong(5, ose.getId());
    }
}
