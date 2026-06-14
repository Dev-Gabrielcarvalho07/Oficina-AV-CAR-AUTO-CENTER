package br.com.avcar.repository;

import br.com.avcar.config.ConnectionFactory;
import br.com.avcar.model.BaseModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * PADRÃO: TEMPLATE METHOD + GENERICS
 *
 * O BaseRepository define o esqueleto dos cinco métodos de CRUD (findAll,
 * findById, insert, update, deleteById). Cada etapa variável — qual tabela,
 * qual coluna de PK, como mapear um ResultSet, qual SQL de escrita — é
 * delegada aos hooks abstratos que cada repositório concreto implementa.
 *
 * O parâmetro T garante type-safety: a subclasse declara seu próprio tipo de
 * entidade e o tipo do ID, sem casts espalhados pelo código.
 *
 * Conexões e statements são sempre abertos em try-with-resources; SQLException
 * é encapsulada em RuntimeException para manter a API do Service livre de
 * exceções checadas.
 */
public abstract class BaseRepository<T extends BaseModel<ID>, ID>
        implements Repository<T, ID> {

    // ─── Hooks abstratos ────────────────────────────────────────────────────

    protected abstract String getTableName();

    protected abstract String getIdColumn();

    protected abstract T mapRow(ResultSet rs) throws SQLException;

    protected abstract String getInsertSql();

    protected abstract void setInsertParams(PreparedStatement ps, T entity) throws SQLException;

    protected abstract ID extractGeneratedId(ResultSet keys) throws SQLException;

    protected abstract String getUpdateSql();

    protected abstract void setUpdateParams(PreparedStatement ps, T entity) throws SQLException;

    // ─── Métodos-template ────────────────────────────────────────────────────

    public List<T> findAll() {
        String sql = "SELECT * FROM " + getTableName();
        List<T> result = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro em findAll() [" + getTableName() + "]: " + e.getMessage(), e);
        }
        return result;
    }

    public Optional<T> findById(ID id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + getIdColumn() + " = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro em findById() [" + getTableName() + ", id=" + id + "]: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public T insert(T entity) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS)) {
            setInsertParams(ps, entity);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setId(extractGeneratedId(keys));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro em insert() [" + getTableName() + "]: " + e.getMessage(), e);
        }
        return entity;
    }

    public void update(T entity) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(getUpdateSql())) {
            setUpdateParams(ps, entity);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro em update() [" + getTableName() + "]: " + e.getMessage(), e);
        }
    }

    public void deleteById(ID id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE " + getIdColumn() + " = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro em deleteById() [" + getTableName() + ", id=" + id + "]: " + e.getMessage(), e);
        }
    }
}
