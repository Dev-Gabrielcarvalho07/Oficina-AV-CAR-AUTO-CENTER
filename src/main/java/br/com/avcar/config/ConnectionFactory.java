// Padrão: SINGLETON
// Justificativa: DatabaseConfig é lido uma única vez na inicialização;
// centralizar a fábrica evita leituras redundantes do arquivo de propriedades
// e garante ponto único de criação de conexões em toda a aplicação.
package br.com.avcar.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static ConnectionFactory instance;

    static {
        instance = new ConnectionFactory();
    }

    private final DatabaseConfig config;

    private ConnectionFactory() {
        this.config = new DatabaseConfig();
    }

    public static ConnectionFactory getInstance() {
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            config.getUrl(),
            config.getUser(),
            config.getPassword()
        );
    }
}
