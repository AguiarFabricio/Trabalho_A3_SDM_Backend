package dao;

import model.Produto;
import model.Categoria;
import model.EmbalagemProduto;
import model.TamanhoProduto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por executar operações de acesso e manipulação dos dados da entidade
 * {@link Produto} no banco de dados.
 * <p>
 * Esta classe implementa métodos CRUD (Create, Read, Update) para o gerenciamento de produtos,
 * incluindo o relacionamento com {@link Categoria}.
 * Todas as conexões são estabelecidas por meio da classe {@link ConexaoDAO}.
 * </p>
 * 
 * <p><b>Principais operações:</b></p>
 * <ul>
 *     <li>Inserção de novos produtos</li>
 *     <li>Listagem de produtos com suas categorias associadas</li>
 *     <li>Atualização de informações de produtos existentes</li>
 * </ul>
 * 
 * @author Luiz
 * @version 1.0
 */
public class ProdutoDAO {

    /**
     * Insere um novo produto no banco de dados.
     * <p>
     * O método registra um novo produto com seus respectivos atributos e a categoria
     * vinculada. Em caso de sucesso, retorna uma mensagem descritiva; caso contrário,
     * retorna o erro ocorrido.
     * </p>
     *
     * @param produto objeto {@link Produto} contendo os dados a serem inseridos
     * @return mensagem indicando o sucesso ou falha da operação
     * @throws SQLException caso ocorra erro de comunicação com o banco de dados
     */
    public String inserir(Produto produto) {
        String sql = """
            INSERT INTO produto 
            (nome, preco, tipo_unidade, quantidade_atual, quantidade_minima, quantidade_maxima, categoria_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setString(3, produto.getTipoUnidade());
            stmt.setInt(4, produto.getQuantidadeAtual());
            stmt.setInt(5, produto.getQuantidadeMinima());
            stmt.setInt(6, produto.getQuantidadeMaxima());
            stmt.setInt(7, produto.getCategoria().getId());

            stmt.executeUpdate();
            return "Produto inserido com sucesso!";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao inserir produto: " + e.getMessage();
        }
    }

    /**
     * Retorna uma lista contendo todos os produtos cadastrados, juntamente com as informações
     * de suas categorias correspondentes.
     * <p>
     * O método realiza junção (JOIN) com a tabela de categorias para trazer também os dados
     * de embalagem e tamanho da categoria associada a cada produto.
     * </p>
     *
     * @return lista de objetos {@link Produto} com seus respectivos dados e categorias
     * @throws SQLException caso ocorra erro de comunicação com o banco de dados
     */
    public List<Produto> listar() {
        List<Produto> lista = new ArrayList<>();

        String sql = """
            SELECT 
                p.id, p.nome, p.preco, p.tipo_unidade,
                p.quantidade_atual, p.quantidade_minima, p.quantidade_maxima,
                c.id AS categoria_id, 
                c.nome AS categoria_nome,
                c.embalagem AS categoria_embalagem,
                c.tamanho AS categoria_tamanho
            FROM produto p
            JOIN categoria c ON p.categoria_id = c.id
        """;

        try (Connection conn = ConexaoDAO.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Categoria cat = new Categoria();
                cat.setId(rs.getInt("categoria_id"));
                cat.setNome(rs.getString("categoria_nome"));

                // Conversão segura de strings em enums
                try {
                    cat.setEmbalagem(EmbalagemProduto.valueOf(rs.getString("categoria_embalagem")));
                } catch (Exception ex) {
                    cat.setEmbalagem(null);
                }

                try {
                    cat.setTamanho(TamanhoProduto.valueOf(rs.getString("categoria_tamanho")));
                } catch (Exception ex) {
                    cat.setTamanho(null);
                }

                Produto p = new Produto(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getDouble("preco"),
                    rs.getString("tipo_unidade"),
                    rs.getInt("quantidade_atual"),
                    rs.getInt("quantidade_minima"),
                    rs.getInt("quantidade_maxima"),
                    cat
                );

                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Atualiza as informações de um produto existente no banco de dados.
     * <p>
     * O método substitui os valores atuais pelos novos informados no objeto {@link Produto},
     * incluindo o vínculo com a categoria correspondente.
     * </p>
     *
     * @param produto objeto {@link Produto} contendo os novos dados
     * @throws SQLException caso ocorra erro de comunicação com o banco de dados
     */
    public void atualizar(Produto produto) {
        String sql = """
            UPDATE produto
            SET nome=?, preco=?, tipo_unidade=?, quantidade_atual=?, 
                quantidade_minima=?, quantidade_maxima=?, categoria_id=?
            WHERE id=?
        """;

        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setString(3, produto.getTipoUnidade());
            stmt.setInt(4, produto.getQuantidadeAtual());
            stmt.setInt(5, produto.getQuantidadeMinima());
            stmt.setInt(6, produto.getQuantidadeMaxima());
            stmt.setInt(7, produto.getCategoria().getId());
            stmt.setInt(8, produto.getId());

            stmt.executeUpdate();
            System.out.println("Produto atualizado com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao atualizar produto: " + e.getMessage());
        }
    }
}
