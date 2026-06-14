package br.com.avcar.repository;

import br.com.avcar.adapter.ResultSetAdapter;
import br.com.avcar.model.Marca;
import java.sql.*;

public class MarcaRepository extends BaseRepository<Marca, Long> {

    @Override
    protected String getTableName() { return "marca"; }

    @Override
    protected String getIdColumn() { return "id_marca"; }

    @Override
    protected Marca mapRow(ResultSet rs) throws SQLException {
        return ResultSetAdapter.toMarca(rs);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO marca (nome) VALUES (?)";
    }

    @Override
    protected void setInsertParams(PreparedStatement ps, Marca m) throws SQLException {
        ps.setString(1, m.getNome());
    }

    @Override
    protected Long extractGeneratedId(ResultSet keys) throws SQLException {
        return keys.getLong(1);
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE marca SET nome = ? WHERE id_marca = ?";
    }

    @Override
    protected void setUpdateParams(PreparedStatement ps, Marca m) throws SQLException {
        ps.setString(1, m.getNome());
        ps.setLong(2, m.getId());
    }
}
