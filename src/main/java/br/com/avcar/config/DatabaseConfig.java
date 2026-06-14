package br.com.avcar.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Configuração de conexão com o banco de dados — lê database.properties do classpath. */
public class DatabaseConfig {

    private final String url;
    private final String user;
    private final String password;

    public DatabaseConfig() {
        Properties props = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (in == null) {
                throw new RuntimeException(
                    "Copie database.properties.example para database.properties e configure."
                );
            }
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar database.properties", e);
        }

        this.url = required(props, "db.url");
        this.user = required(props, "db.user");
        this.password = required(props, "db.password");
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    private static String required(Properties props, String key) {
        String value = props.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new RuntimeException(
                "Configure a propriedade " + key + " em src/main/resources/database.properties."
            );
        }
        return value.trim();
    }
}
