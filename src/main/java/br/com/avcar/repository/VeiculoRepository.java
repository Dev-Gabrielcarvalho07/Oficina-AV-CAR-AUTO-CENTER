package br.com.avcar.repository;

import br.com.avcar.adapter.ResultSetAdapter;
import br.com.avcar.model.Veiculo;
import java.sql.*;

public class VeiculoRepository extends BaseRepository<Veiculo, Long> {

    @Override protected String getTableName()  { return "veiculo"; }
    @Override protected String getIdColumn()   { return "id_veiculo"; }

    @Override
    protected Veiculo mapRow(ResultSet rs) throws SQLException {
        return ResultSetAdapter.toVeiculo(rs);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO veiculo (placa, chassi, cor, ano_fabricacao, quilometragem, id_modelo)"
             + " VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected void setInsertParams(PreparedStatement ps, Veiculo v) throws SQLException {
        ps.setString(1, v.getPlaca());
        ps.setString(2, v.getChassi());
        ps.setString(3, v.getCor());
        ps.setInt(4, v.getAnoFabricacao());
        ps.setObject(5, v.getQuilometragem());
        ps.setLong(6, v.getIdModelo());
    }

    @Override
    protected Long extractGeneratedId(ResultSet keys) throws SQLException {
        return keys.getLong(1);
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE veiculo SET placa = ?, chassi = ?, cor = ?, ano_fabricacao = ?,"
             + " quilometragem = ?, id_modelo = ? WHERE id_veiculo = ?";
    }

    @Override
    protected void setUpdateParams(PreparedStatement ps, Veiculo v) throws SQLException {
        ps.setString(1, v.getPlaca());
        ps.setString(2, v.getChassi());
        ps.setString(3, v.getCor());
        ps.setInt(4, v.getAnoFabricacao());
        ps.setObject(5, v.getQuilometragem());
        ps.setLong(6, v.getIdModelo());
        ps.setLong(7, v.getId());
    }
}
