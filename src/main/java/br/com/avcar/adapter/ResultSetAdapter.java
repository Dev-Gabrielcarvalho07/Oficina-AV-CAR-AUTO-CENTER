package br.com.avcar.adapter;

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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

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

    private static Long getLong(ResultSet rs, String col) throws SQLException {
        long value = rs.getLong(col);
        return rs.wasNull() ? null : value;
    }

    private static Integer getInteger(ResultSet rs, String col) throws SQLException {
        int value = rs.getInt(col);
        return rs.wasNull() ? null : value;
    }

    private static LocalDate getLocalDate(ResultSet rs, String col) throws SQLException {
        Date date = rs.getDate(col);
        return date == null ? null : date.toLocalDate();
    }

    private static LocalTime getLocalTime(ResultSet rs, String col) throws SQLException {
        Time time = rs.getTime(col);
        return time == null ? null : time.toLocalTime();
    }

    private static BigDecimal getBigDecimal(ResultSet rs, String col) throws SQLException {
        return rs.getBigDecimal(col);
    }

    private static <E extends Enum<E>> E getEnum(ResultSet rs, String col, Class<E> type)
            throws SQLException {
        String value = rs.getString(col);
        return value == null ? null : Enum.valueOf(type, value);
    }

    /** Preenche os campos da tabela pessoa em qualquer subclasse concreta. */
    private static void fillPessoa(ResultSet rs, Pessoa pessoa) throws SQLException {
        pessoa.setId(getLong(rs, "id_pessoa"));
        pessoa.setNome(rs.getString("nome"));
        pessoa.setEmail(rs.getString("email"));
        pessoa.setCep(rs.getString("cep"));
        pessoa.setLogradouro(rs.getString("logradouro"));
        pessoa.setNumero(rs.getString("numero"));
        pessoa.setComplemento(rs.getString("complemento"));
    }

    /** Preenche os campos da tabela cliente (JOIN com pessoa assumido). */
    private static void fillCliente(ResultSet rs, Cliente cliente) throws SQLException {
        fillPessoa(rs, cliente);
        cliente.setDataCadastro(getLocalDate(rs, "data_cadastro"));
        cliente.setAtivo(rs.getBoolean("ativo"));
    }

    // ─── Builders públicos ───────────────────────────────────────────────────

    /**
     * pessoa JOIN cliente — telefones ficam vazios (repository carrega depois).
     */
    public static Cliente toCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        fillCliente(rs, cliente);
        return cliente;
    }

    /**
     * pessoa JOIN cliente JOIN pessoa_fisica
     */
    public static PessoaFisica toPessoaFisica(ResultSet rs) throws SQLException {
        PessoaFisica pessoaFisica = new PessoaFisica();
        fillCliente(rs, pessoaFisica);
        pessoaFisica.setCpf(rs.getString("cpf"));
        pessoaFisica.setRg(rs.getString("rg"));
        return pessoaFisica;
    }

    /**
     * pessoa JOIN cliente JOIN pessoa_juridica
     */
    public static PessoaJuridica toPessoaJuridica(ResultSet rs) throws SQLException {
        PessoaJuridica pessoaJuridica = new PessoaJuridica();
        fillCliente(rs, pessoaJuridica);
        pessoaJuridica.setCnpj(rs.getString("cnpj"));
        pessoaJuridica.setInscricaoEstadual(rs.getString("inscricao_estadual"));
        pessoaJuridica.setNomeFantasia(rs.getString("nome_fantasia"));
        pessoaJuridica.setNomeContato(rs.getString("nome_contato"));
        return pessoaJuridica;
    }

    /**
     * pessoa JOIN colaborador
     */
    public static Colaborador toColaborador(ResultSet rs) throws SQLException {
        Colaborador colaborador = new Colaborador();
        fillPessoa(rs, colaborador);
        colaborador.setMatricula(rs.getString("matricula"));
        colaborador.setDataAdmissao(getLocalDate(rs, "data_admissao"));
        colaborador.setIdFuncao(getLong(rs, "id_funcao"));
        return colaborador;
    }

    public static Telefone toTelefone(ResultSet rs) throws SQLException {
        Telefone telefone = new Telefone();
        telefone.setIdPessoa(getLong(rs, "id_pessoa"));
        telefone.setNumero(rs.getString("numero"));
        return telefone;
    }

    public static Funcao toFuncao(ResultSet rs) throws SQLException {
        Funcao funcao = new Funcao();
        funcao.setId(getLong(rs, "id_funcao"));
        funcao.setNome(rs.getString("nome"));
        funcao.setSalarioBase(getBigDecimal(rs, "salario_base"));
        return funcao;
    }

    public static Marca toMarca(ResultSet rs) throws SQLException {
        Marca marca = new Marca();
        marca.setId(getLong(rs, "id_marca"));
        marca.setNome(rs.getString("nome"));
        return marca;
    }

    public static Modelo toModelo(ResultSet rs) throws SQLException {
        Modelo modelo = new Modelo();
        modelo.setId(getLong(rs, "id_modelo"));
        modelo.setNome(rs.getString("nome"));
        modelo.setIdMarca(getLong(rs, "id_marca"));
        return modelo;
    }

    public static Veiculo toVeiculo(ResultSet rs) throws SQLException {
        Veiculo veiculo = new Veiculo();
        veiculo.setId(getLong(rs, "id_veiculo"));
        veiculo.setPlaca(rs.getString("placa"));
        veiculo.setChassi(rs.getString("chassi"));
        veiculo.setCor(rs.getString("cor"));
        veiculo.setAnoFabricacao(getInteger(rs, "ano_fabricacao"));
        veiculo.setQuilometragem(getInteger(rs, "quilometragem"));
        veiculo.setIdModelo(getLong(rs, "id_modelo"));
        return veiculo;
    }

    public static HistoricoDonos toHistoricoDonos(ResultSet rs) throws SQLException {
        HistoricoDonos historico = new HistoricoDonos();
        historico.setId(getLong(rs, "id_historico"));
        historico.setIdPessoa(getLong(rs, "id_pessoa"));
        historico.setIdVeiculo(getLong(rs, "id_veiculo"));
        historico.setDataInicio(getLocalDate(rs, "data_inicio"));
        historico.setDataFim(getLocalDate(rs, "data_fim"));
        historico.setAtual(rs.getBoolean("atual"));
        return historico;
    }

    public static ServicoInterno toServicoInterno(ResultSet rs) throws SQLException {
        ServicoInterno servico = new ServicoInterno();
        servico.setId(getLong(rs, "id_servico_interno"));
        servico.setDescricao(rs.getString("descricao"));
        servico.setValorBase(getBigDecimal(rs, "valor_base"));
        servico.setGarantia(getInteger(rs, "garantia"));
        return servico;
    }

    public static ServicoExterno toServicoExterno(ResultSet rs) throws SQLException {
        ServicoExterno servico = new ServicoExterno();
        servico.setId(getLong(rs, "id_servico_externo"));
        servico.setDescricao(rs.getString("descricao"));
        servico.setValorBase(getBigDecimal(rs, "valor_base"));
        servico.setGarantia(getInteger(rs, "garantia"));
        return servico;
    }

    public static Peca toPeca(ResultSet rs) throws SQLException {
        Peca peca = new Peca();
        peca.setId(getLong(rs, "id_peca"));
        peca.setCodigoNacional(rs.getString("codigo_nacional"));
        peca.setNome(rs.getString("nome"));
        peca.setValor(getBigDecimal(rs, "valor"));
        peca.setGarantia(getInteger(rs, "garantia"));
        return peca;
    }

    public static Fornecedor toFornecedor(ResultSet rs) throws SQLException {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(getLong(rs, "id_fornecedor"));
        fornecedor.setNome(rs.getString("nome"));
        fornecedor.setCnpj(rs.getString("cnpj"));
        fornecedor.setTipoFornecedor(getEnum(rs, "tipo_fornecedor", TipoFornecedor.class));
        fornecedor.setTelefone(rs.getString("telefone"));
        fornecedor.setEmail(rs.getString("email"));
        return fornecedor;
    }

    public static OrdemServico toOrdemServico(ResultSet rs) throws SQLException {
        OrdemServico ordemServico = new OrdemServico();
        ordemServico.setId(getLong(rs, "id_os"));
        ordemServico.setNumeroOs(getInteger(rs, "numero_os"));
        ordemServico.setDataEntrada(getLocalDate(rs, "data_entrada"));
        ordemServico.setDataSaida(getLocalDate(rs, "data_saida"));
        ordemServico.setStatusOs(getEnum(rs, "status_os", StatusOS.class));
        ordemServico.setPrioridade(getEnum(rs, "prioridade", Prioridade.class));
        ordemServico.setValorMaoObra(getBigDecimal(rs, "valor_mao_obra"));
        ordemServico.setValorPecas(getBigDecimal(rs, "valor_pecas"));
        ordemServico.setDeslocamento(getBigDecimal(rs, "deslocamento"));
        ordemServico.setServicoGuincho(getBigDecimal(rs, "servico_guincho"));
        ordemServico.setOutros(getBigDecimal(rs, "outros"));
        ordemServico.setTotal(getBigDecimal(rs, "total"));
        ordemServico.setIdPessoa(getLong(rs, "id_pessoa"));
        ordemServico.setIdVeiculo(getLong(rs, "id_veiculo"));
        return ordemServico;
    }

    public static OsServicoInterno toOsServicoInterno(ResultSet rs) throws SQLException {
        OsServicoInterno osServicoInterno = new OsServicoInterno();
        osServicoInterno.setId(getLong(rs, "id_os_servico_interno"));
        osServicoInterno.setIdOs(getLong(rs, "id_os"));
        osServicoInterno.setIdServicoInterno(getLong(rs, "id_servico_interno"));
        osServicoInterno.setIdPessoa(getLong(rs, "id_pessoa"));
        osServicoInterno.setQtd(getInteger(rs, "qtd"));
        osServicoInterno.setValor(getBigDecimal(rs, "valor"));
        osServicoInterno.setInicio(getLocalTime(rs, "inicio"));
        osServicoInterno.setFim(getLocalTime(rs, "fim"));
        return osServicoInterno;
    }

    public static OsServicoExterno toOsServicoExterno(ResultSet rs) throws SQLException {
        OsServicoExterno osServicoExterno = new OsServicoExterno();
        osServicoExterno.setId(getLong(rs, "id_os_servico_externo"));
        osServicoExterno.setIdOs(getLong(rs, "id_os"));
        osServicoExterno.setIdServicoExterno(getLong(rs, "id_servico_externo"));
        osServicoExterno.setIdFornecedor(getLong(rs, "id_fornecedor"));
        osServicoExterno.setValor(getBigDecimal(rs, "valor"));
        return osServicoExterno;
    }

    public static OsPeca toOsPeca(ResultSet rs) throws SQLException {
        OsPeca osPeca = new OsPeca();
        osPeca.setId(getLong(rs, "id_os_peca"));
        osPeca.setIdOs(getLong(rs, "id_os"));
        osPeca.setIdPeca(getLong(rs, "id_peca"));
        osPeca.setIdFornecedor(getLong(rs, "id_fornecedor"));
        osPeca.setQtd(getInteger(rs, "qtd"));
        osPeca.setValorUnitario(getBigDecimal(rs, "valor_unitario"));
        osPeca.setValorTotal(getBigDecimal(rs, "valor_total"));
        osPeca.setStatusPeca(getEnum(rs, "status_peca", StatusPeca.class));
        return osPeca;
    }
}
