package br.com.avcar.repository;

import br.com.avcar.adapter.ResultSetAdapter;
import br.com.avcar.model.Fornecedor;
import java.sql.*;

public class FornecedorRepository extends BaseRepository<Fornecedor, Long> {

    @Override protected String getTableName()  { return "fornecedor"; }
    @Override protected String getIdColumn()   { return "id_fornecedor"; }

    @Override
    protected Fornecedor mapRow(ResultSet rs) throws SQLException {
        return ResultSetAdapter.toFornecedor(rs);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO fornecedor (nome, cnpj, tipo_fornecedor, telefone, email)"
             + " VALUES (?, ?, ?::tipo_fornecedor_enum, ?, ?)";
    }

    @Override
    protected void setInsertParams(PreparedStatement ps, Fornecedor f) throws SQLException {
        ps.setString(1, f.getNome());
        ps.setString(2, f.getCnpj());
        ps.setString(3, f.getTipoFornecedor().name());
        ps.setString(4, f.getTelefone());
        ps.setString(5, f.getEmail());
    }

    @Override
    protected Long extractGeneratedId(ResultSet keys) throws SQLException {
        return keys.getLong(1);
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE fornecedor SET nome = ?, cnpj = ?, tipo_fornecedor = ?::tipo_fornecedor_enum,"
             + " telefone = ?, email = ? WHERE id_fornecedor = ?";
    }

    @Override
    protected void setUpdateParams(PreparedStatement ps, Fornecedor f) throws SQLException {
        ps.setString(1, f.getNome());
        ps.setString(2, f.getCnpj());
        ps.setString(3, f.getTipoFornecedor().name());
        ps.setString(4, f.getTelefone());
        ps.setString(5, f.getEmail());
        ps.setLong(6, f.getId());
    }
}
