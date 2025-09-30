/*
Para a interação do java com SQL optamos pelo MySQL usando localhost, pois se trata de um projeto
mais simplificado que não envolve um computador servidor, sistema qual outros IPs podem acessar o
banco de dados.
Para evitar dados sensíveis visível, foi utilizado um arquivo .properties como base para adquirir
os dados importantes de acesso ao banco de dados.

Fontes:
https://www.devmedia.com.br/utilizando-arquivos-de-propriedades-no-java/25546
https://www.devmedia.com.br/criando-uma-conexao-java-mysql-server/16753
https://www.devmedia.com.br/introducao-ao-jdbc/43900
https://pt.stackoverflow.com/questions/63778/conex%C3%A3o-ao-banco-de-dados-mysql-e-java
 */

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class BancoDeDados {
    private static String URL;
    private static String USUARIO;
    private static String SENHA;

    static {
        // o config.properties tem os dados do banco (dados sensíveis - arquivo em .gitignore)
        try (InputStream input = new FileInputStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            URL = prop.getProperty("DB_URL"); // url do banco
            USUARIO = prop.getProperty("DB_USER"); // root ou javauser
            SENHA = prop.getProperty("DB_PASS"); // senha de acesso ao banco do usuário 

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro na leitura do config.properties");
        }
    }

    public static Connection conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conexão realizada com sucesso!");
            return conexao;

        } catch (ClassNotFoundException e) {
            System.out.println("Driver JDBC não encontrado: " + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco: " + e.getMessage());

        }
        return null;

    }

    /* Apenas para testar a conexão com o banco - Só precisa descomentar e rodar o código

    public static void main(String[] args) {
        Connection c = conectar();
        try {
            if (c != null && !c.isClosed()) {
                c.close();
                System.out.println("Conexão fechada!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
     */
}