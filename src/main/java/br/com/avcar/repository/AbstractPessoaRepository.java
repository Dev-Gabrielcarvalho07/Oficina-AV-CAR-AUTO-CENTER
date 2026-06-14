package br.com.avcar.repository;

import br.com.avcar.adapter.ResultSetAdapter;
import br.com.avcar.config.ConnectionFactory;
import br.com.avcar.model.Pessoa;
import br.com.avcar.model.Telefone;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * PADRÃO: TEMPLATE METHOD + GENERICS
 *
 * Define o esqueleto do CRUD para entidades da hierarquia Pessoa, cujos dados
 * se distribuem por múltiplas tabelas. Cada operação de escrita roda numa única
 * transação (setAutoCommit false / commit / rollback). Os hooks abstratos
 * implementam os passos específicos de cada subtipo (pessoa_fisica, pessoa_juridica,
 * colaborador), enquanto os passos comuns — pessoa e telefone — ficam fixos aqui.
 */
public abstract class AbstractPessoaRepository<T extends Pessoa>
        implements Repository<T, Long> {

    private static final String INSERT_PESSOA =
        "INSERT INTO pessoa (nome, email, cep, logradouro, numero, complemento)"
      + " VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_PESSOA =
        "UPDATE pessoa SET nome = ?, email = ?, cep = ?, logradouro = ?, numero = ?,"
      + " complemento = ? WHERE id_pessoa = ?";

    private static final String DELETE_PESSOA =
        "DELETE FROM pessoa WHERE id_pessoa = ?";

    private static final String DELETE_TELEFONES =
        "DELETE FROM telefone WHERE id_pessoa = ?";

    // ─── Hooks abstratos ────────────────────────────────────────────────────

    protected abstract String getSelectByIdSql();
    protected abstract String getSelectAllSql();
    protected abstract T mapRow(ResultSet rs) throws SQLException;
    protected abstract void insertSubtipo(Connection conn, T e) throws SQLException;
    protected abstract void updateSubtipo(Connection conn, T e) throws SQLException;
    protected abstract void deleteSubtipo(Connection conn, Long id) throws SQLException;

    // ─── Métodos-template ────────────────────────────────────────────────────

    public T insert(T e) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(
                        INSERT_PESSOA, Statement.RETURN_GENERATED_KEYS)) {
                    setPessoaParams(ps, e);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            e.setId(keys.getLong(1));
                        }
                    }
                }
                insertSubtipo(conn, e);
                insertTelefones(conn, e);
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Erro em insert() [pessoa]: " + ex.getMessage(), ex);
        }
        return e;
    }

    public Optional<T> findById(Long id) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            T e = null;
            try (PreparedStatement ps = conn.prepareStatement(getSelectByIdSql())) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        e = mapRow(rs);
                    }
                }
            }
            if (e != null) {
                loadTelefones(conn, e);
                return Optional.of(e);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Erro em findById() [pessoa, id=" + id + "]: " + ex.getMessage(), ex);
        }
        return Optional.empty();
    }

    public List<T> findAll() {
        List<T> result = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(getSelectAllSql());
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
            for (T e : result) {
                loadTelefones(conn, e);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Erro em findAll() [pessoa]: " + ex.getMessage(), ex);
        }
        return result;
    }

    public void update(T e) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(UPDATE_PESSOA)) {
                    setPessoaParams(ps, e);
                    ps.setLong(7, e.getId());
                    ps.executeUpdate();
                }
                updateSubtipo(conn, e);
                replaceTelefones(conn, e);
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Erro em update() [pessoa, id=" + e.getId() + "]: " + ex.getMessage(), ex);
        }
    }

    public void deleteById(Long id) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(DELETE_TELEFONES)) {
                    ps.setLong(1, id);
                    ps.executeUpdate();
                }
                deleteSubtipo(conn, id);
                try (PreparedStatement ps = conn.prepareStatement(DELETE_PESSOA)) {
                    ps.setLong(1, id);
                    ps.executeUpdate();
                }
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Erro em deleteById() [pessoa, id=" + id + "]: " + ex.getMessage(), ex);
        }
    }

    // ─── Helpers privados ───────────────────────────────────────────────────

    private void setPessoaParams(PreparedStatement ps, T e) throws SQLException {
        ps.setString(1, e.getNome());
        ps.setString(2, e.getEmail());
        ps.setString(3, e.getCep());
        ps.setString(4, e.getLogradouro());
        ps.setString(5, e.getNumero());
        ps.setString(6, e.getComplemento());
    }

    private void insertTelefones(Connection conn, T e) throws SQLException {
        if (e.getTelefones() == null || e.getTelefones().isEmpty()) return;
        String sql = "INSERT INTO telefone (id_pessoa, numero) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Telefone t : e.getTelefones()) {
                ps.setLong(1, e.getId());
                ps.setString(2, t.getNumero());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void loadTelefones(Connection conn, T e) throws SQLException {
        String sql = "SELECT id_pessoa, numero FROM telefone WHERE id_pessoa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, e.getId());
            try (ResultSet rs = ps.executeQuery()) {
                List<Telefone> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(ResultSetAdapter.toTelefone(rs));
                }
                e.setTelefones(lista);
            }
        }
    }

    private void replaceTelefones(Connection conn, T e) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(DELETE_TELEFONES)) {
            ps.setLong(1, e.getId());
            ps.executeUpdate();
        }
        insertTelefones(conn, e);
    }
}
