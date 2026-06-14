package br.com.avcar.repository;

import br.com.avcar.adapter.ResultSetAdapter;
import br.com.avcar.model.Colaborador;
import java.sql.*;

public class ColaboradorRepository extends AbstractPessoaRepository<Colaborador> {

    private static final String SELECT_COLS =
        "SELECT p.id_pessoa, p.nome, p.email, p.cep, p.logradouro, p.numero, p.complemento,"
      + "       col.matricula, col.data_admissao, col.id_funcao"
      + " FROM pessoa p"
      + " JOIN colaborador col ON col.id_pessoa = p.id_pessoa";

    @Override
    protected String getSelectByIdSql() {
        return SELECT_COLS + " WHERE p.id_pessoa = ?";
    }

    @Override
    protected String getSelectAllSql() {
        return SELECT_COLS;
    }

    @Override
    protected Colaborador mapRow(ResultSet rs) throws SQLException {
        return ResultSetAdapter.toColaborador(rs);
    }

    @Override
    protected void insertSubtipo(Connection conn, Colaborador col) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO colaborador (id_pessoa, matricula, data_admissao, id_funcao)"
              + " VALUES (?, ?, ?, ?)")) {
            ps.setLong(1, col.getId());
            ps.setString(2, col.getMatricula());
            ps.setObject(3, col.getDataAdmissao());
            ps.setLong(4, col.getIdFuncao());
            ps.executeUpdate();
        }
    }

    @Override
    protected void updateSubtipo(Connection conn, Colaborador col) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE colaborador SET matricula = ?, data_admissao = ?, id_funcao = ?"
              + " WHERE id_pessoa = ?")) {
            ps.setString(1, col.getMatricula());
            ps.setObject(2, col.getDataAdmissao());
            ps.setLong(3, col.getIdFuncao());
            ps.setLong(4, col.getId());
            ps.executeUpdate();
        }
    }

    @Override
    protected void deleteSubtipo(Connection conn, Long id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM colaborador WHERE id_pessoa = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}
