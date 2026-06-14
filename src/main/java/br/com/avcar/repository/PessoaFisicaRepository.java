package br.com.avcar.repository;

import br.com.avcar.adapter.ResultSetAdapter;
import br.com.avcar.model.PessoaFisica;
import java.sql.*;

public class PessoaFisicaRepository extends AbstractPessoaRepository<PessoaFisica> {

    private static final String SELECT_COLS =
        "SELECT p.id_pessoa, p.nome, p.email, p.cep, p.logradouro, p.numero, p.complemento,"
      + "       c.data_cadastro, c.ativo,"
      + "       pf.cpf, pf.rg"
      + " FROM pessoa p"
      + " JOIN cliente c ON c.id_pessoa = p.id_pessoa"
      + " JOIN pessoa_fisica pf ON pf.id_pessoa = p.id_pessoa";

    @Override
    protected String getSelectByIdSql() {
        return SELECT_COLS + " WHERE p.id_pessoa = ?";
    }

    @Override
    protected String getSelectAllSql() {
        return SELECT_COLS;
    }

    @Override
    protected PessoaFisica mapRow(ResultSet rs) throws SQLException {
        return ResultSetAdapter.toPessoaFisica(rs);
    }

    @Override
    protected void insertSubtipo(Connection conn, PessoaFisica pf) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO cliente (id_pessoa, data_cadastro, ativo) VALUES (?, ?, ?)")) {
            ps.setLong(1, pf.getId());
            ps.setObject(2, pf.getDataCadastro());
            ps.setBoolean(3, pf.isAtivo());
            ps.executeUpdate();
        }
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO pessoa_fisica (id_pessoa, cpf, rg) VALUES (?, ?, ?)")) {
            ps.setLong(1, pf.getId());
            ps.setString(2, pf.getCpf());
            ps.setString(3, pf.getRg());
            ps.executeUpdate();
        }
    }

    @Override
    protected void updateSubtipo(Connection conn, PessoaFisica pf) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE cliente SET data_cadastro = ?, ativo = ? WHERE id_pessoa = ?")) {
            ps.setObject(1, pf.getDataCadastro());
            ps.setBoolean(2, pf.isAtivo());
            ps.setLong(3, pf.getId());
            ps.executeUpdate();
        }
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE pessoa_fisica SET cpf = ?, rg = ? WHERE id_pessoa = ?")) {
            ps.setString(1, pf.getCpf());
            ps.setString(2, pf.getRg());
            ps.setLong(3, pf.getId());
            ps.executeUpdate();
        }
    }

    @Override
    protected void deleteSubtipo(Connection conn, Long id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM pessoa_fisica WHERE id_pessoa = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM cliente WHERE id_pessoa = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}
