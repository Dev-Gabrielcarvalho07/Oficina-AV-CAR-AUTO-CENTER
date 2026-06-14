package br.com.avcar.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
        this.url      = props.getProperty("db.url");
        this.user     = props.getProperty("db.user");
        this.password = props.getProperty("db.password");
    }

    public String getUrl()      { return url; }
    public String getUser()     { return user; }
    public String getPassword() { return password; }
}
