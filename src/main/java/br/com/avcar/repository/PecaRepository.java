package br.com.avcar.repository;

import br.com.avcar.adapter.ResultSetAdapter;
import br.com.avcar.model.Peca;
import java.sql.*;

public class PecaRepository extends BaseRepository<Peca, Long> {

    @Override protected String getTableName()  { return "peca"; }
    @Override protected String getIdColumn()   { return "id_peca"; }

    @Override
    protected Peca mapRow(ResultSet rs) throws SQLException {
        return ResultSetAdapter.toPeca(rs);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO peca (codigo_nacional, nome, valor, garantia) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected void setInsertParams(PreparedStatement ps, Peca p) throws SQLException {
        ps.setString(1, p.getCodigoNacional());
        ps.setString(2, p.getNome());
        ps.setObject(3, p.getValor());
        ps.setObject(4, p.getGarantia());
    }

    @Override
    protected Long extractGeneratedId(ResultSet keys) throws SQLException {
        return keys.getLong(1);
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE peca SET codigo_nacional = ?, nome = ?, valor = ?, garantia = ? WHERE id_peca = ?";
    }

    @Override
    protected void setUpdateParams(PreparedStatement ps, Peca p) throws SQLException {
        ps.setString(1, p.getCodigoNacional());
        ps.setString(2, p.getNome());
        ps.setObject(3, p.getValor());
        ps.setObject(4, p.getGarantia());
        ps.setLong(5, p.getId());
    }
}
