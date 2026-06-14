package br.com.avcar.repository;

import br.com.avcar.adapter.ResultSetAdapter;
import br.com.avcar.model.OsPeca;
import java.sql.*;

public class OsPecaRepository extends BaseRepository<OsPeca, Long> {

    @Override protected String getTableName()  { return "os_peca"; }
    @Override protected String getIdColumn()   { return "id_os_peca"; }

    @Override
    protected OsPeca mapRow(ResultSet rs) throws SQLException {
        return ResultSetAdapter.toOsPeca(rs);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO os_peca"
             + " (id_os, id_peca, id_fornecedor, qtd, valor_unitario, valor_total, status_peca)"
             + " VALUES (?, ?, ?, ?, ?, ?, ?::status_peca_enum)";
    }

    @Override
    protected void setInsertParams(PreparedStatement ps, OsPeca op) throws SQLException {
        ps.setLong(1, op.getIdOs());
        ps.setLong(2, op.getIdPeca());
        ps.setLong(3, op.getIdFornecedor());
        ps.setInt(4,  op.getQtd());
        ps.setObject(5, op.getValorUnitario());
        ps.setObject(6, op.getValorTotal());
        ps.setString(7, op.getStatusPeca().name());
    }

    @Override
    protected Long extractGeneratedId(ResultSet keys) throws SQLException {
        return keys.getLong(1);
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE os_peca SET id_os = ?, id_peca = ?, id_fornecedor = ?,"
             + " qtd = ?, valor_unitario = ?, valor_total = ?,"
             + " status_peca = ?::status_peca_enum WHERE id_os_peca = ?";
    }

    @Override
    protected void setUpdateParams(PreparedStatement ps, OsPeca op) throws SQLException {
        ps.setLong(1, op.getIdOs());
        ps.setLong(2, op.getIdPeca());
        ps.setLong(3, op.getIdFornecedor());
        ps.setInt(4,  op.getQtd());
        ps.setObject(5, op.getValorUnitario());
        ps.setObject(6, op.getValorTotal());
        ps.setString(7, op.getStatusPeca().name());
        ps.setLong(8, op.getId());
    }
}
