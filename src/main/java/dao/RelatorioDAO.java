package dao;

import model.Produto;
import model.Categoria;
import model.EmbalagemProduto;
import model.TamanhoProduto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por gerar relatórios de produtos e categorias no sistema de estoque.
 * <p>
 * Esta classe centraliza consultas SQL relacionadas à análise de estoque, permitindo
 * a obtenção de produtos fora dos limites estabelecidos (mínimo/máximo) e relatórios
 * agregados, como a quantidade de produtos por categoria.
 * </p>
 * <p>
 * Todas as conexões com o banco de dados são realizadas por meio da classe {@link ConexaoDAO}.
 * </p>
 *
 * <p><b>Principais funcionalidades:</b></p>
 * <ul>
 *     <li>Listar produtos abaixo da quantidade mínima.</li>
 *     <li>Listar produtos acima da quantidade máxima.</li>
 *     <li>Listar todos os produtos cadastrados.</li>
 *     <li>Gerar relatório de quantidade de produtos por categoria.</li>
 * </ul>
 *
 * @author Luiz
 * @version 1.0
 */
public class RelatorioDAO {

    /**
     * Retorna uma lista de produtos cuja quantidade atual está abaixo da quantidade mínima definida.
     *
     * @return lista de objetos {@link Produto} que estão com estoque abaixo do mínimo
     * @throws SQLException caso ocorra erro ao acessar o banco de dados
     */
    public List<Produto> listarProdutosAbaixoDoMinimo() {
        return listarPorCondicao("p.quantidade_atual < p.quantidade_minima");
    }

    /**
     * Retorna uma lista de produtos cuja quantidade atual está acima da quantidade máxima definida.
     *
     * @return lista de objetos {@link Produto} que estão com estoque acima do máximo permitido
     * @throws SQLException caso ocorra erro ao acessar o banco de dados
     */
    public List<Produto> listarProdutosAcimaDoMaximo() {
        return listarPorCondicao("p.quantidade_atual > p.quantidade_maxima");
    }

    /**
     * Retorna uma lista contendo todos os produtos cadastrados no sistema, independentemente de seus estoques.
     *
     * @return lista de todos os objetos {@link Produto} registrados no banco de dados
     * @throws SQLException caso ocorra erro ao acessar o banco de dados
     */
    public List<Produto> listarTodos() {
        return listarPorCondicao("1=1");
    }

    /**
     * Gera um relatório contendo a quantidade total de produtos associados a cada categoria.
     * <p>
     * O método utiliza a função SQL {@code COUNT(p.id)} para contabilizar os produtos de
     * cada categoria. Categorias sem produtos são retornadas com contagem igual a zero.
     * </p>
     *
     * @return lista de arrays de objetos, onde cada posição contém:
     *         <ul>
     *             <li>[0] → {@link String} nome da categoria</li>
     *             <li>[1] → {@link Integer} quantidade de produtos associados</li>
     *         </ul>
     * @throws SQLException caso ocorra erro durante a consulta ao banco de dados
     */
    public List<Object[]> listarQuantidadePorCategoria() {
        List<Object[]> lista = new ArrayList<>();

        String sql = """
            SELECT 
                c.nome AS categoria_nome,
                COUNT(p.id) AS quantidade
            FROM categoria c
            LEFT JOIN produto p ON p.categoria_id = c.id
            GROUP BY c.nome
            ORDER BY c.nome
        """;

        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String categoria = rs.getString("categoria_nome");
                int quantidade = rs.getInt("quantidade");

                // Proteção contra valores nulos
                if (rs.wasNull()) {
                    quantidade = 0;
                }

                lista.add(new Object[]{categoria, quantidade});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao gerar relatório de quantidade por categoria: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Executa a consulta SQL com base em uma condição informada, retornando uma lista
     * de produtos que atendem ao critério especificado.
     * <p>
     * Este método é utilizado internamente pelos relatórios de produtos acima, abaixo
     * e dentro dos limites de estoque.
     * </p>
     *
     * @param condicao condição SQL que define o filtro aplicado à consulta (ex: "p.quantidade_atual < p.quantidade_minima")
     * @return lista de objetos {@link Produto} que atendem à condição especificada
     * @throws SQLException caso ocorra erro durante a execução da consulta
     */
    private List<Produto> listarPorCondicao(String condicao) {
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
            WHERE %s
        """.formatted(condicao);

        try (Connection conn = ConexaoDAO.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Categoria cat = new Categoria();
                cat.setId(rs.getInt("categoria_id"));
                cat.setNome(rs.getString("categoria_nome"));

                // Conversão segura de string → enum
                try {
                    cat.setEmbalagem(EmbalagemProduto.valueOf(rs.getString("categoria_embalagem")));
                } catch (Exception e) {
                    cat.setEmbalagem(null);
                }

                try {
                    cat.setTamanho(TamanhoProduto.valueOf(rs.getString("categoria_tamanho")));
                } catch (Exception e) {
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
            System.out.println("Erro ao gerar relatório: " + e.getMessage());
        }

        return lista;
    }
}
