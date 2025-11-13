package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por gerenciar a conexão com o banco de dados MySQL do sistema de estoque.
 * <p>
 * Centraliza as informações de acesso (URL, usuário e senha) e fornece um método estático
 * para obtenção de conexões via JDBC. Esta classe é utilizada por todas as classes DAO
 * do projeto.
 * </p>
 * 
 * <p><b>Observação:</b> As credenciais e a URL devem ser alteradas conforme o ambiente 
 * de execução (desenvolvimento, teste ou produção).</p>
 * 
 * @author Luiz
 * @version 1.0
 */
public class ConexaoDAO {

    /** URL de conexão com o banco de dados MySQL. */
    private static final String URL = "jdbc:mysql://localhost:3306/estoque_db";

    /** Nome de usuário para autenticação no banco de dados. */
    private static final String USER = "root";

    /** Senha correspondente ao usuário do banco de dados. */
    private static final String PASSWORD = "1234";

    /**
     * Obtém uma conexão válida com o banco de dados MySQL.
     * <p>
     * O método tenta estabelecer a conexão utilizando o {@link DriverManager}.
     * Caso ocorra falha, a exceção é tratada internamente e o método retorna {@code null}.
     * </p>
     *
     * @return um objeto {@link Connection} ativo, ou {@code null} se a conexão falhar
     * @throws SQLException se ocorrer um erro crítico durante a tentativa de conexão
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco: " + e.getMessage());
            return null;
        }
    }
}
