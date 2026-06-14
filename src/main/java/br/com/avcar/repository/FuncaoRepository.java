package br.com.avcar.repository;

import br.com.avcar.adapter.ResultSetAdapter;
import br.com.avcar.model.Funcao;
import java.sql.*;

public class FuncaoRepository extends BaseRepository<Funcao, Long> {

    @Override protected String getTableName()  { return "funcao"; }
    @Override protected String getIdColumn()   { return "id_funcao"; }

    @Override
    protected Funcao mapRow(ResultSet rs) throws SQLException {
        return ResultSetAdapter.toFuncao(rs);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO funcao (nome, salario_base) VALUES (?, ?)";
    }

    @Override
    protected void setInsertParams(PreparedStatement ps, Funcao f) throws SQLException {
        ps.setString(1, f.getNome());
        ps.setObject(2, f.getSalarioBase());
    }

    @Override
    protected Long extractGeneratedId(ResultSet keys) throws SQLException {
        return keys.getLong(1);
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE funcao SET nome = ?, salario_base = ? WHERE id_funcao = ?";
    }

    @Override
    protected void setUpdateParams(PreparedStatement ps, Funcao f) throws SQLException {
        ps.setString(1, f.getNome());
        ps.setObject(2, f.getSalarioBase());
        ps.setLong(3, f.getId());
    }
}
