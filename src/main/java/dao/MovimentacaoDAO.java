package dao;

import model.Movimentacao;
import model.Produto;
import model.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por realizar operações de acesso e manipulação dos dados da entidade
 * {@link Movimentacao} no banco de dados.
 * <p>
 * Implementa operações de inserção e consultas diversas relacionadas às movimentações
 * de produtos (entradas e saídas) no estoque. Todas as conexões são obtidas através da
 * classe {@link ConexaoDAO}.
 * </p>
 * 
 * <p><b>Funções principais:</b></p>
 * <ul>
 *     <li>Registrar novas movimentações (entrada ou saída)</li>
 *     <li>Listar todas as movimentações realizadas</li>
 *     <li>Filtrar movimentações por produto ou por tipo</li>
 * </ul>
 * 
 * @author Luiz
 * @version 1.0
 */
public class MovimentacaoDAO {

    /**
     * Insere uma nova movimentação no banco de dados.
     * <p>
     * Registra as informações do produto, tipo de movimentação, quantidade e data.
     * </p>
     *
     * @param mov objeto {@link Movimentacao} contendo os dados da movimentação
     * @return mensagem de sucesso ou erro referente ao resultado da operação
     * @throws SQLException caso ocorra erro de comunicação com o banco de dados
     */
    public String inserir(Movimentacao mov) {
        String sql = "INSERT INTO movimentacao (produto_id, tipo, quantidade, data_movimentacao) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, mov.getProduto().getId());
            stmt.setString(2, mov.getTipo());
            stmt.setInt(3, mov.getQuantidade());
            stmt.setTimestamp(4, new java.sql.Timestamp(mov.getDataMovimentacao().getTime()));

            stmt.executeUpdate();
            return "Movimentação registrada com sucesso!";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao registrar movimentação: " + e.getMessage();
        }
    }

    /**
     * Retorna uma lista contendo todas as movimentações registradas no banco de dados.
     * <p>
     * A consulta retorna também os dados associados do {@link Produto} e da {@link Categoria}
     * correspondente, ordenados pela data da movimentação (mais recente primeiro).
     * </p>
     *
     * @return lista de objetos {@link Movimentacao} com seus respectivos produtos e categorias
     * @throws SQLException caso ocorra erro de comunicação com o banco de dados
     */
    public List<Movimentacao> listar() {
        List<Movimentacao> lista = new ArrayList<>();
        String sql = """
            SELECT m.*, 
                   p.id AS produto_id, p.nome AS produto_nome, p.preco, 
                   p.tipo_unidade, p.quantidade_atual, p.quantidade_minima, p.quantidade_maxima,
                   c.id AS categoria_id, c.nome AS categoria_nome
            FROM movimentacao m
            JOIN produto p ON m.produto_id = p.id
            JOIN categoria c ON p.categoria_id = c.id
            ORDER BY m.data_movimentacao DESC
        """;

        try (Connection conn = ConexaoDAO.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Categoria
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("categoria_id"));
                categoria.setNome(rs.getString("categoria_nome"));

                // Produto
                Produto produto = new Produto();
                produto.setId(rs.getInt("produto_id"));
                produto.setNome(rs.getString("produto_nome"));
                produto.setPreco(rs.getDouble("preco"));
                produto.setTipoUnidade(rs.getString("tipo_unidade"));
                produto.setQuantidadeAtual(rs.getInt("quantidade_atual"));
                produto.setQuantidadeMinima(rs.getInt("quantidade_minima"));
                produto.setQuantidadeMaxima(rs.getInt("quantidade_maxima"));
                produto.setCategoria(categoria);

                // Movimentação
                Movimentacao mov = new Movimentacao();
                mov.setId(rs.getInt("id"));
                mov.setProduto(produto);
                mov.setTipo(rs.getString("tipo"));
                mov.setQuantidade(rs.getInt("quantidade"));
                mov.setDataMovimentacao(rs.getTimestamp("data_movimentacao"));

                lista.add(mov);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Retorna todas as movimentações associadas a um determinado produto.
     *
     * @param produtoId identificador do produto cujas movimentações serão buscadas
     * @return lista de objetos {@link Movimentacao} do produto informado
     * @throws SQLException caso ocorra erro de comunicação com o banco de dados
     */
    public List<Movimentacao> listarPorProduto(int produtoId) {
        List<Movimentacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimentacao WHERE produto_id = ? ORDER BY data_movimentacao DESC";

        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, produtoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Movimentacao mov = new Movimentacao();
                mov.setId(rs.getInt("id"));
                mov.setTipo(rs.getString("tipo"));
                mov.setQuantidade(rs.getInt("quantidade"));
                mov.setDataMovimentacao(rs.getTimestamp("data_movimentacao"));

                lista.add(mov);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Retorna todas as movimentações filtradas por tipo.
     * <p>
     * O tipo normalmente indica se a movimentação foi uma <b>entrada</b> ou <b>saída</b> de estoque.
     * </p>
     *
     * @param tipo tipo da movimentação (ex: "entrada" ou "saída")
     * @return lista de objetos {@link Movimentacao} correspondentes ao tipo informado
     * @throws SQLException caso ocorra erro de comunicação com o banco de dados
     */
    public List<Movimentacao> listarPorTipo(String tipo) {
        List<Movimentacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimentacao WHERE tipo = ? ORDER BY data_movimentacao DESC";

        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Movimentacao mov = new Movimentacao();
                mov.setId(rs.getInt("id"));
                mov.setTipo(rs.getString("tipo"));
                mov.setQuantidade(rs.getInt("quantidade"));
                mov.setDataMovimentacao(rs.getTimestamp("data_movimentacao"));

                lista.add(mov);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
