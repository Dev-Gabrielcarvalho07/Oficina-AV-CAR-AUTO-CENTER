package br.com.avcar.repository;

import br.com.avcar.adapter.ResultSetAdapter;
import br.com.avcar.model.PessoaJuridica;
import java.sql.*;

public class PessoaJuridicaRepository extends AbstractPessoaRepository<PessoaJuridica> {

    private static final String SELECT_COLS =
        "SELECT p.id_pessoa, p.nome, p.email, p.cep, p.logradouro, p.numero, p.complemento,"
      + "       c.data_cadastro, c.ativo,"
      + "       pj.cnpj, pj.inscricao_estadual, pj.nome_fantasia, pj.nome_contato"
      + " FROM pessoa p"
      + " JOIN cliente c ON c.id_pessoa = p.id_pessoa"
      + " JOIN pessoa_juridica pj ON pj.id_pessoa = p.id_pessoa";

    @Override
    protected String getSelectByIdSql() {
        return SELECT_COLS + " WHERE p.id_pessoa = ?";
    }

    @Override
    protected String getSelectAllSql() {
        return SELECT_COLS;
    }

    @Override
    protected PessoaJuridica mapRow(ResultSet rs) throws SQLException {
        return ResultSetAdapter.toPessoaJuridica(rs);
    }

    @Override
    protected void insertSubtipo(Connection conn, PessoaJuridica pj) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO cliente (id_pessoa, data_cadastro, ativo) VALUES (?, ?, ?)")) {
            ps.setLong(1, pj.getId());
            ps.setObject(2, pj.getDataCadastro());
            ps.setBoolean(3, pj.isAtivo());
            ps.executeUpdate();
        }
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO pessoa_juridica"
              + " (id_pessoa, cnpj, inscricao_estadual, nome_fantasia, nome_contato)"
              + " VALUES (?, ?, ?, ?, ?)")) {
            ps.setLong(1, pj.getId());
            ps.setString(2, pj.getCnpj());
            ps.setString(3, pj.getInscricaoEstadual());
            ps.setString(4, pj.getNomeFantasia());
            ps.setString(5, pj.getNomeContato());
            ps.executeUpdate();
        }
    }

    @Override
    protected void updateSubtipo(Connection conn, PessoaJuridica pj) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE cliente SET data_cadastro = ?, ativo = ? WHERE id_pessoa = ?")) {
            ps.setObject(1, pj.getDataCadastro());
            ps.setBoolean(2, pj.isAtivo());
            ps.setLong(3, pj.getId());
            ps.executeUpdate();
        }
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE pessoa_juridica SET cnpj = ?, inscricao_estadual = ?,"
              + " nome_fantasia = ?, nome_contato = ? WHERE id_pessoa = ?")) {
            ps.setString(1, pj.getCnpj());
            ps.setString(2, pj.getInscricaoEstadual());
            ps.setString(3, pj.getNomeFantasia());
            ps.setString(4, pj.getNomeContato());
            ps.setLong(5, pj.getId());
            ps.executeUpdate();
        }
    }

    @Override
    protected void deleteSubtipo(Connection conn, Long id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM pessoa_juridica WHERE id_pessoa = ?")) {
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
