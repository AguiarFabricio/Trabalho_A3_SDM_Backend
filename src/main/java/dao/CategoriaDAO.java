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
 * Implementa operações CRUD (Create, Read, Update, Delete) e consultas específicas 
 * utilizando JDBC. Todas as conexões são obtidas através da classe {@link ConexaoDAO}.
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
     * @throws SQLException caso ocorra erro de comunicação com o banco de dados
     */
    public void inserir(Categoria categoria) {
        String sql = "INSERT INTO categoria (nome, embalagem, tamanho) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getEmbalagem() != null ? categoria.getEmbalagem().name() : null);
            stmt.setString(3, categoria.getTamanho() != null ? categoria.getTamanho().name() : null);
            stmt.executeUpdate();

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
     * @throws SQLException caso ocorra erro de comunicação com o banco de dados
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

                try {
                    c.setEmbalagem(EmbalagemProduto.valueOf(rs.getString("embalagem")));
                } catch (Exception ex) {
                    c.setEmbalagem(null);
                }

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
     * @throws SQLException caso ocorra erro de comunicação com o banco de dados
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
     *
     * @param id identificador da categoria a ser removida
     * @throws SQLException caso ocorra erro de comunicação com o banco de dados
     */
    public void excluir(int id) {
        String sql = "DELETE FROM categoria WHERE id=?";
        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao excluir categoria: " + e.getMessage());
        }
    }

    /**
     * Busca uma categoria específica com base no seu identificador.
     *
     * @param id identificador da categoria
     * @return objeto {@link Categoria} correspondente ao ID informado, 
     *         ou {@code null} caso não seja encontrada
     * @throws SQLException caso ocorra erro de comunicação com o banco de dados
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

                    try {
                        categoria.setEmbalagem(EmbalagemProduto.valueOf(rs.getString("embalagem")));
                    } catch (Exception ex) {
                        categoria.setEmbalagem(null);
                    }

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
     *   <li>Quantidade de produtos</li>
     * </ul>
     * </p>
     *
     * @return lista de objetos {@code Object[]} contendo o nome da categoria e a quantidade de produtos
     * @throws SQLException caso ocorra erro de comunicação com o banco de dados
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

                if (rs.wasNull()) qtd = 0;

                lista.add(new Object[]{nome, qtd});
            }

        } catch (SQLException e) {
            System.out.println("Erro ao gerar relatório de quantidade por categoria: " + e.getMessage());
        }

        return lista;
    }
}
