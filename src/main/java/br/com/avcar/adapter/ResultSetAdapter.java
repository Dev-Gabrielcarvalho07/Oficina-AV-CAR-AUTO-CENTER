package br.com.avcar.adapter;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import br.com.avcar.model.Cliente;
import br.com.avcar.model.Colaborador;
import br.com.avcar.model.Fornecedor;
import br.com.avcar.model.Funcao;
import br.com.avcar.model.HistoricoDonos;
import br.com.avcar.model.Marca;
import br.com.avcar.model.Modelo;
import br.com.avcar.model.OrdemServico;
import br.com.avcar.model.OsPeca;
import br.com.avcar.model.OsServicoExterno;
import br.com.avcar.model.OsServicoInterno;
import br.com.avcar.model.Peca;
import br.com.avcar.model.Pessoa;
import br.com.avcar.model.PessoaFisica;
import br.com.avcar.model.PessoaJuridica;
import br.com.avcar.model.ServicoExterno;
import br.com.avcar.model.ServicoInterno;
import br.com.avcar.model.Telefone;
import br.com.avcar.model.Veiculo;
import br.com.avcar.model.enums.Prioridade;
import br.com.avcar.model.enums.StatusOS;
import br.com.avcar.model.enums.StatusPeca;
import br.com.avcar.model.enums.TipoFornecedor;

/**
 * PADRÃO: ADAPTER
 * Adapta a API do java.sql.ResultSet para os objetos de domínio,
 * isolando o JDBC do resto do sistema.
 *
 * Pessoa é abstract: suas colunas são lidas pelo helper privado fillPessoa,
 * chamado pelos builders da hierarquia (toCliente, toPessoaFisica,
 * toPessoaJuridica, toColaborador) que assumem um ResultSet já com JOIN.
 */
public class ResultSetAdapter {

    private ResultSetAdapter() {}

    // ─── Helpers privados ────────────────────────────────────────────────────

    private static Long getNullableLong(ResultSet rs, String col) throws SQLException { // serve
        long v = rs.getLong(col);
        return rs.wasNull() ? null : v;
    }

    private static LocalDate getLocalDate(ResultSet rs, String col) throws SQLException {
        Date d = rs.getDate(col);
        return d == null ? null : d.toLocalDate();
    }

    private static LocalTime getLocalTime(ResultSet rs, String col) throws SQLException {
        Time t = rs.getTime(col);
        return t == null ? null : t.toLocalTime();
    }

    private static BigDecimal getBigDecimal(ResultSet rs, String col) throws SQLException {
        return rs.getBigDecimal(col);
    }

    private static <E extends Enum<E>> E getEnum(ResultSet rs, String col, Class<E> type)
            throws SQLException {
        String v = rs.getString(col);
        return v == null ? null : Enum.valueOf(type, v);
    }

    /** Preenche os campos da tabela pessoa em qualquer subclasse concreta. */
    private static void fillPessoa(ResultSet rs, Pessoa p) throws SQLException {
        p.setId(rs.getLong("id_pessoa"));
        p.setNome(rs.getString("nome"));
        p.setEmail(rs.getString("email"));
        p.setCep(rs.getString("cep"));
        p.setLogradouro(rs.getString("logradouro"));
        p.setNumero(rs.getString("numero"));
        p.setComplemento(rs.getString("complemento"));
    }

    /** Preenche os campos da tabela cliente (JOIN com pessoa assumido). */
    private static void fillCliente(ResultSet rs, Cliente c) throws SQLException {
        fillPessoa(rs, c);
        c.setDataCadastro(getLocalDate(rs, "data_cadastro"));
        c.setAtivo(rs.getBoolean("ativo"));
    }

    // ─── Builders públicos ───────────────────────────────────────────────────

    /**
     * pessoa JOIN cliente — telefones ficam vazios (repository carrega depois).
     */
    public static Cliente toCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        fillCliente(rs, c);
        return c;
    }

    /**
     * pessoa JOIN cliente JOIN pessoa_fisica
     */
    public static PessoaFisica toPessoaFisica(ResultSet rs) throws SQLException {
        PessoaFisica pf = new PessoaFisica();
        fillCliente(rs, pf);
        pf.setCpf(rs.getString("cpf"));
        pf.setRg(rs.getString("rg"));
        return pf;
    }

    /**
     * pessoa JOIN cliente JOIN pessoa_juridica
     */
    public static PessoaJuridica toPessoaJuridica(ResultSet rs) throws SQLException {
        PessoaJuridica pj = new PessoaJuridica();
        fillCliente(rs, pj);
        pj.setCnpj(rs.getString("cnpj"));
        pj.setInscricaoEstadual(rs.getString("inscricao_estadual"));
        pj.setNomeFantasia(rs.getString("nome_fantasia"));
        pj.setNomeContato(rs.getString("nome_contato"));
        return pj;
    }

    /**
     * pessoa JOIN colaborador
     */
    public static Colaborador toColaborador(ResultSet rs) throws SQLException {
        Colaborador col = new Colaborador();
        fillPessoa(rs, col);
        col.setMatricula(rs.getString("matricula"));
        col.setDataAdmissao(getLocalDate(rs, "data_admissao"));
        col.setIdFuncao(rs.getLong("id_funcao"));
        return col;
    }

    public static Telefone toTelefone(ResultSet rs) throws SQLException {
        Telefone t = new Telefone();
        t.setIdPessoa(rs.getLong("id_pessoa"));
        t.setNumero(rs.getString("numero"));
        return t;
    }

    public static Funcao toFuncao(ResultSet rs) throws SQLException {
        Funcao f = new Funcao();
        f.setId(rs.getLong("id_funcao"));
        f.setNome(rs.getString("nome"));
        f.setSalarioBase(getBigDecimal(rs, "salario_base"));
        return f;
    }

    public static Marca toMarca(ResultSet rs) throws SQLException {
        Marca m = new Marca();
        m.setId(rs.getLong("id_marca"));
        m.setNome(rs.getString("nome"));
        return m;
    }

    public static Modelo toModelo(ResultSet rs) throws SQLException {
        Modelo m = new Modelo();
        m.setId(rs.getLong("id_modelo"));
        m.setNome(rs.getString("nome"));
        m.setIdMarca(rs.getLong("id_marca"));
        return m;
    }

    public static Veiculo toVeiculo(ResultSet rs) throws SQLException {
        Veiculo v = new Veiculo();
        v.setId(rs.getLong("id_veiculo"));
        v.setPlaca(rs.getString("placa"));
        v.setChassi(rs.getString("chassi"));
        v.setCor(rs.getString("cor"));
        v.setAnoFabricacao(rs.getInt("ano_fabricacao"));
        v.setQuilometragem(rs.getObject("quilometragem", Integer.class));
        v.setIdModelo(rs.getLong("id_modelo"));
        return v;
    }

    public static HistoricoDonos toHistoricoDonos(ResultSet rs) throws SQLException {
        HistoricoDonos h = new HistoricoDonos();
        h.setId(rs.getLong("id_historico"));
        h.setIdPessoa(rs.getLong("id_pessoa"));
        h.setIdVeiculo(rs.getLong("id_veiculo"));
        h.setDataInicio(getLocalDate(rs, "data_inicio"));
        h.setDataFim(getLocalDate(rs, "data_fim"));
        h.setAtual(rs.getBoolean("atual"));
        return h;
    }

    public static ServicoInterno toServicoInterno(ResultSet rs) throws SQLException {
        ServicoInterno s = new ServicoInterno();
        s.setId(rs.getLong("id_servico_interno"));
        s.setDescricao(rs.getString("descricao"));
        s.setValorBase(getBigDecimal(rs, "valor_base"));
        s.setGarantia(rs.getObject("garantia", Integer.class));
        return s;
    }

    public static ServicoExterno toServicoExterno(ResultSet rs) throws SQLException {
        ServicoExterno s = new ServicoExterno();
        s.setId(rs.getLong("id_servico_externo"));
        s.setDescricao(rs.getString("descricao"));
        s.setValorBase(getBigDecimal(rs, "valor_base"));
        s.setGarantia(rs.getObject("garantia", Integer.class));
        return s;
    }

    public static Peca toPeca(ResultSet rs) throws SQLException {
        Peca p = new Peca();
        p.setId(rs.getLong("id_peca"));
        p.setCodigoNacional(rs.getString("codigo_nacional"));
        p.setNome(rs.getString("nome"));
        p.setValor(getBigDecimal(rs, "valor"));
        p.setGarantia(rs.getObject("garantia", Integer.class));
        return p;
    }

    public static Fornecedor toFornecedor(ResultSet rs) throws SQLException {
        Fornecedor f = new Fornecedor();
        f.setId(rs.getLong("id_fornecedor"));
        f.setNome(rs.getString("nome"));
        f.setCnpj(rs.getString("cnpj"));
        f.setTipoFornecedor(getEnum(rs, "tipo_fornecedor", TipoFornecedor.class));
        f.setTelefone(rs.getString("telefone"));
        f.setEmail(rs.getString("email"));
        return f;
    }

    public static OrdemServico toOrdemServico(ResultSet rs) throws SQLException {
        OrdemServico os = new OrdemServico();
        os.setId(rs.getLong("id_os"));
        os.setNumeroOs(rs.getInt("numero_os"));
        os.setDataEntrada(getLocalDate(rs, "data_entrada"));
        os.setDataSaida(getLocalDate(rs, "data_saida"));
        os.setStatusOs(getEnum(rs, "status_os", StatusOS.class));
        os.setPrioridade(getEnum(rs, "prioridade", Prioridade.class));
        os.setValorMaoObra(getBigDecimal(rs, "valor_mao_obra"));
        os.setValorPecas(getBigDecimal(rs, "valor_pecas"));
        os.setDeslocamento(getBigDecimal(rs, "deslocamento"));
        os.setServicoGuincho(getBigDecimal(rs, "servico_guincho"));
        os.setOutros(getBigDecimal(rs, "outros"));
        os.setTotal(getBigDecimal(rs, "total"));
        os.setIdPessoa(rs.getLong("id_pessoa"));
        os.setIdVeiculo(rs.getLong("id_veiculo"));
        return os;
    }

    public static OsServicoInterno toOsServicoInterno(ResultSet rs) throws SQLException {
        OsServicoInterno osi = new OsServicoInterno();
        osi.setId(rs.getLong("id_os_servico_interno"));
        osi.setIdOs(rs.getLong("id_os"));
        osi.setIdServicoInterno(rs.getLong("id_servico_interno"));
        osi.setIdPessoa(rs.getLong("id_pessoa"));
        osi.setQtd(rs.getInt("qtd"));
        osi.setValor(getBigDecimal(rs, "valor"));
        osi.setInicio(getLocalTime(rs, "inicio"));
        osi.setFim(getLocalTime(rs, "fim"));
        return osi;
    }

    public static OsServicoExterno toOsServicoExterno(ResultSet rs) throws SQLException {
        OsServicoExterno ose = new OsServicoExterno();
        ose.setId(rs.getLong("id_os_servico_externo"));
        ose.setIdOs(rs.getLong("id_os"));
        ose.setIdServicoExterno(rs.getLong("id_servico_externo"));
        ose.setIdFornecedor(rs.getLong("id_fornecedor"));
        ose.setValor(getBigDecimal(rs, "valor"));
        return ose;
    }

    public static OsPeca toOsPeca(ResultSet rs) throws SQLException {
        OsPeca op = new OsPeca();
        op.setId(rs.getLong("id_os_peca"));
        op.setIdOs(rs.getLong("id_os"));
        op.setIdPeca(rs.getLong("id_peca"));
        op.setIdFornecedor(rs.getLong("id_fornecedor"));
        op.setQtd(rs.getInt("qtd"));
        op.setValorUnitario(getBigDecimal(rs, "valor_unitario"));
        op.setValorTotal(getBigDecimal(rs, "valor_total"));
        op.setStatusPeca(getEnum(rs, "status_peca", StatusPeca.class));
        return op;
    }
}
