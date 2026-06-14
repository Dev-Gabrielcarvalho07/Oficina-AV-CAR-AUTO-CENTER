package br.com.avcar.repository;

import br.com.avcar.adapter.ResultSetAdapter;
import br.com.avcar.model.OsServicoInterno;
import java.sql.*;

public class OsServicoInternoRepository extends BaseRepository<OsServicoInterno, Long> {

    @Override protected String getTableName()  { return "os_servico_interno"; }
    @Override protected String getIdColumn()   { return "id_os_servico_interno"; }

    @Override
    protected OsServicoInterno mapRow(ResultSet rs) throws SQLException {
        return ResultSetAdapter.toOsServicoInterno(rs);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO os_servico_interno"
             + " (id_os, id_servico_interno, id_pessoa, qtd, valor, inicio, fim)"
             + " VALUES (?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected void setInsertParams(PreparedStatement ps, OsServicoInterno osi) throws SQLException {
        ps.setLong(1, osi.getIdOs());
        ps.setLong(2, osi.getIdServicoInterno());
        ps.setLong(3, osi.getIdPessoa());
        ps.setInt(4,  osi.getQtd());
        ps.setObject(5, osi.getValor());
        ps.setObject(6, osi.getInicio());
        ps.setObject(7, osi.getFim());
    }

    @Override
    protected Long extractGeneratedId(ResultSet keys) throws SQLException {
        return keys.getLong(1);
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE os_servico_interno SET id_os = ?, id_servico_interno = ?,"
             + " id_pessoa = ?, qtd = ?, valor = ?, inicio = ?, fim = ?"
             + " WHERE id_os_servico_interno = ?";
    }

    @Override
    protected void setUpdateParams(PreparedStatement ps, OsServicoInterno osi) throws SQLException {
        ps.setLong(1, osi.getIdOs());
        ps.setLong(2, osi.getIdServicoInterno());
        ps.setLong(3, osi.getIdPessoa());
        ps.setInt(4,  osi.getQtd());
        ps.setObject(5, osi.getValor());
        ps.setObject(6, osi.getInicio());
        ps.setObject(7, osi.getFim());
        ps.setLong(8, osi.getId());
    }
}
