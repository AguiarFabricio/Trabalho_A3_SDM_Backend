package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Categoria;
import model.EmbalagemProduto;
import model.TamanhoProduto;

/**
 * Classe responsável pelo acesso e manipulação dos dados da entidade {@link Categoria}
 * no banco de dados.
 * <p>
 * Fornece métodos para operações CRUD (Create, Read, Update, Delete), além de consultas
 * específicas utilizando JDBC. Todas as conexões são obtidas através da classe
 * {@link ConexaoDAO}.
 * </p>
 *
 * @author Luiz
 * @version 1.0
 */
public class CategoriaDAO {

    /**
     * Insere uma nova categoria no banco de dados.
     *
     * @param categoria objeto {@link Categoria} contendo os dados a serem inseridos
     */
    public void inserir(Categoria categoria) {
        String sql = "INSERT INTO categoria (nome, embalagem, tamanho) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getEmbalagem() != null ? categoria.getEmbalagem().name() : null);
            stmt.setString(3, categoria.getTamanho() != null ? categoria.getTamanho().name() : null);
            stmt.executeUpdate();

            // Captura o ID gerado automaticamente pelo banco
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    categoria.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao inserir categoria: " + e.getMessage());
        }
    }

    /**
     * Retorna uma lista com todas as categorias cadastradas no banco de dados.
     *
     * @return lista de objetos {@link Categoria}
     */
    public List<Categoria> listar() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categoria";

        try (Connection conn = ConexaoDAO.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Categoria c = new Categoria();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));

                // Conversão segura para Enum de Embalagem
                try {
                    c.setEmbalagem(EmbalagemProduto.valueOf(rs.getString("embalagem")));
                } catch (Exception ex) {
                    c.setEmbalagem(null);
                }

                // Conversão segura para Enum de Tamanho
                try {
                    c.setTamanho(TamanhoProduto.valueOf(rs.getString("tamanho")));
                } catch (Exception ex) {
                    c.setTamanho(null);
                }

                lista.add(c);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar categorias: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Atualiza as informações de uma categoria existente no banco de dados.
     *
     * @param categoria objeto {@link Categoria} com os dados atualizados
     */
    public void atualizar(Categoria categoria) {
        String sql = "UPDATE categoria SET nome=?, embalagem=?, tamanho=? WHERE id=?";

        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getEmbalagem() != null ? categoria.getEmbalagem().name() : null);
            stmt.setString(3, categoria.getTamanho() != null ? categoria.getTamanho().name() : null);
            stmt.setInt(4, categoria.getId());

            stmt.executeUpdate();
            System.out.println("Categoria atualizada com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar categoria: " + e.getMessage());
        }
    }

    /**
     * Exclui uma categoria do banco de dados com base no seu identificador.
     * <p>
     * Também exibe mensagens claras sobre o resultado da operação, incluindo:
     * </p>
     * <ul>
     *     <li>Categoria excluída com sucesso</li>
     *     <li>Categoria não encontrada</li>
     *     <li>Categoria não pode ser excluída por possuir produtos vinculados</li>
     * </ul>
     *
     * @param id identificador da categoria a ser removida
     */
 /**
 * Exclui uma categoria do banco de dados com base no seu identificador.
 * <p>
 * Antes de excluir, verifica se existem produtos vinculados à categoria.
 * Caso existam, a exclusão é bloqueada e uma exceção é lançada com
 * mensagem clara para a camada de serviço.
 * </p>
 *
 * @param id identificador da categoria a ser removida
 * @throws Exception caso a exclusão não seja permitida
 */
public void excluir(int id) throws Exception {

    // 1) Verifica se existem produtos associados à categoria
    String sqlVerifica = "SELECT COUNT(*) FROM produto WHERE categoria_id = ?";
    try (Connection conn = ConexaoDAO.getConnection();
         PreparedStatement stmtVerifica = conn.prepareStatement(sqlVerifica)) {

        stmtVerifica.setInt(1, id);
        ResultSet rs = stmtVerifica.executeQuery();

        if (rs.next() && rs.getInt(1) > 0) {
            // ❌ Impede a exclusão e informa claramente o motivo
            throw new Exception("Não é possível excluir a categoria: existem produtos associados.");
        }
    }

    // 2) Realiza a exclusão somente se NÃO houver produtos vinculados
    String sqlDelete = "DELETE FROM categoria WHERE id=?";
    try (Connection conn = ConexaoDAO.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sqlDelete)) {

        stmt.setInt(1, id);
        int linhasAfetadas = stmt.executeUpdate();

        if (linhasAfetadas > 0) {
            System.out.println("🗑️ Categoria excluída com sucesso! ID: " + id);
        } else {
            System.out.println("⚠️ Nenhuma categoria encontrada para exclusão. ID informado: " + id);
        }

    } catch (SQLException e) {
        throw new Exception("Erro ao excluir categoria (ID " + id + "): " + e.getMessage());
    }
}


    /**
     * Busca uma categoria específica com base no seu identificador.
     *
     * @param id identificador da categoria
     * @return objeto {@link Categoria} correspondente ao ID informado,
     *         ou {@code null} caso não seja encontrada
     */
    public Categoria buscarPorId(int id) {
        String sql = "SELECT * FROM categoria WHERE id=?";
        Categoria categoria = null;

        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    categoria = new Categoria();
                    categoria.setId(rs.getInt("id"));
                    categoria.setNome(rs.getString("nome"));

                    // Converte a embalagem armazenada para Enum
                    try {
                        categoria.setEmbalagem(EmbalagemProduto.valueOf(rs.getString("embalagem")));
                    } catch (Exception ex) {
                        categoria.setEmbalagem(null);
                    }

                    // Converte o tamanho armazenado para Enum
                    try {
                        categoria.setTamanho(TamanhoProduto.valueOf(rs.getString("tamanho")));
                    } catch (Exception ex) {
                        categoria.setTamanho(null);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar categoria por ID: " + e.getMessage());
        }

        return categoria;
    }

    /**
     * Gera um relatório contendo a quantidade de produtos associados a cada categoria.
     * <p>
     * O resultado contém duas colunas:
     * <ul>
     *   <li>Nome da categoria</li>
     *   <li>Quantidade de produtos vinculados</li>
     * </ul>
     * </p>
     *
     * @return lista de objetos {@code Object[]} contendo os dados do relatório
     */
    public List<Object[]> quantidadePorCategoria() {
        List<Object[]> lista = new ArrayList<>();

        String sql = """
            SELECT 
                c.nome AS categoria_nome,
                COUNT(p.id) AS qtd_produtos
            FROM categoria c
            LEFT JOIN produto p ON p.categoria_id = c.id
            GROUP BY c.nome
            ORDER BY c.nome
        """;

        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nome = rs.getString("categoria_nome");
                int qtd = rs.getInt("qtd_produtos");

                // Garante que não retorne valor nulo
                if (rs.wasNull()) qtd = 0;

                lista.add(new Object[]{nome, qtd});
            }

        } catch (SQLException e) {
            System.out.println("Erro ao gerar relatório de quantidade por categoria: " + e.getMessage());
        }

        return lista;
    }
}
