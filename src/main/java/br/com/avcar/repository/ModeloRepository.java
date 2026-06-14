package br.com.avcar.repository;

import br.com.avcar.adapter.ResultSetAdapter;
import br.com.avcar.model.Modelo;
import java.sql.*;

public class ModeloRepository extends BaseRepository<Modelo, Long> {

    @Override protected String getTableName()  { return "modelo"; }
    @Override protected String getIdColumn()   { return "id_modelo"; }

    @Override
    protected Modelo mapRow(ResultSet rs) throws SQLException {
        return ResultSetAdapter.toModelo(rs);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO modelo (nome, id_marca) VALUES (?, ?)";
    }

    @Override
    protected void setInsertParams(PreparedStatement ps, Modelo m) throws SQLException {
        ps.setString(1, m.getNome());
        ps.setLong(2, m.getIdMarca());
    }

    @Override
    protected Long extractGeneratedId(ResultSet keys) throws SQLException {
        return keys.getLong(1);
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE modelo SET nome = ?, id_marca = ? WHERE id_modelo = ?";
    }

    @Override
    protected void setUpdateParams(PreparedStatement ps, Modelo m) throws SQLException {
        ps.setString(1, m.getNome());
        ps.setLong(2, m.getIdMarca());
        ps.setLong(3, m.getId());
    }
}
