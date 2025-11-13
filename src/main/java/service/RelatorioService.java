package service;

import dao.ConexaoDAO;
import java.sql.*;
import java.util.*;

/**
 * Classe de serviço responsável por gerar relatórios do sistema de estoque.
 * <p>
 * Cada relatório executa uma consulta SQL específica e retorna os resultados
 * como uma lista de mapas ({@code List<Map<String, Object>>}), onde cada mapa
 * representa uma linha do resultado, associando nomes de colunas a valores.
 * </p>
 *
 * <p><b>Principais relatórios:</b></p>
 * <ul>
 *   <li>Lista de preços de produtos;</li>
 *   <li>Balanço físico e financeiro;</li>
 *   <li>Produtos abaixo do estoque mínimo;</li>
 *   <li>Quantidade de produtos por categoria;</li>
 *   <li>Produto mais movimentado (entradas e saídas).</li>
 * </ul>
 *
 * <p>Utiliza {@link ConexaoDAO} para obter conexões JDBC e executa consultas SQL diretamente.</p>
 *
 * @author Luiz
 * @version 1.0
 * @since 2025
 */
public class RelatorioService {

    // ========================= LISTA DE PREÇOS =========================

    /**
     * Gera um relatório contendo os preços de todos os produtos cadastrados.
     *
     * @return uma lista de mapas contendo os campos:
     *         <ul>
     *             <li>{@code produto} — nome do produto;</li>
     *             <li>{@code categoria} — nome da categoria;</li>
     *             <li>{@code preco} — valor unitário do produto;</li>
     *             <li>{@code tipo_unidade} — tipo de unidade de medida.</li>
     *         </ul>
     */
    public List<Map<String, Object>> listarPrecos() {
        String sql = """
            SELECT p.nome AS produto, c.nome AS categoria, 
                   p.preco, p.tipo_unidade
            FROM produto p
            JOIN categoria c ON p.categoria_id = c.id
            ORDER BY p.nome
        """;

        return executarConsulta(sql, rs -> {
            Map<String, Object> linha = new HashMap<>();
            linha.put("produto", rs.getString("produto"));
            linha.put("categoria", rs.getString("categoria"));
            linha.put("preco", rs.getDouble("preco"));
            linha.put("tipo_unidade", rs.getString("tipo_unidade"));
            return linha;
        }, "Lista de preços");
    }

    // ========================= BALANÇO FÍSICO / FINANCEIRO =========================

    /**
     * Gera o relatório de balanço físico e financeiro.
     * <p>
     * Exibe a quantidade atual de cada produto, seu preço unitário e o valor total em estoque.
     * </p>
     *
     * @return lista de mapas contendo:
     *         <ul>
     *             <li>{@code produto} — nome do produto;</li>
     *             <li>{@code categoria} — nome da categoria;</li>
     *             <li>{@code quantidade} — quantidade atual em estoque;</li>
     *             <li>{@code preco} — preço unitário;</li>
     *             <li>{@code valor_total} — valor total (quantidade × preço).</li>
     *         </ul>
     */
    public List<Map<String, Object>> balancoFisicoFinanceiro() {
        String sql = """
            SELECT p.nome AS produto, c.nome AS categoria, 
                   p.quantidade_atual, p.preco
            FROM produto p
            JOIN categoria c ON p.categoria_id = c.id
            ORDER BY p.nome
        """;

        return executarConsulta(sql, rs -> {
            int qtd = rs.getInt("quantidade_atual");
            double preco = rs.getDouble("preco");
            Map<String, Object> linha = new HashMap<>();
            linha.put("produto", rs.getString("produto"));
            linha.put("categoria", rs.getString("categoria"));
            linha.put("quantidade", qtd);
            linha.put("preco", preco);
            linha.put("valor_total", qtd * preco);
            return linha;
        }, "Balanço físico/financeiro");
    }

    // ========================= PRODUTOS ABAIXO DO MÍNIMO =========================

    /**
     * Retorna um relatório de produtos cujo estoque está abaixo da quantidade mínima definida.
     *
     * @return lista de mapas contendo:
     *         <ul>
     *             <li>{@code produto} — nome do produto;</li>
     *             <li>{@code categoria} — nome da categoria;</li>
     *             <li>{@code quantidade_atual} — quantidade em estoque;</li>
     *             <li>{@code quantidade_minima} — quantidade mínima exigida.</li>
     *         </ul>
     */
    public List<Map<String, Object>> produtosAbaixoDoMinimo() {
        String sql = """
            SELECT p.nome AS produto, c.nome AS categoria,
                   p.quantidade_atual, p.quantidade_minima
            FROM produto p
            JOIN categoria c ON p.categoria_id = c.id
            WHERE p.quantidade_atual < p.quantidade_minima
            ORDER BY p.nome
        """;

        return executarConsulta(sql, rs -> {
            Map<String, Object> linha = new HashMap<>();
            linha.put("produto", rs.getString("produto"));
            linha.put("categoria", rs.getString("categoria"));
            linha.put("quantidade_atual", rs.getInt("quantidade_atual"));
            linha.put("quantidade_minima", rs.getInt("quantidade_minima"));
            return linha;
        }, "Produtos abaixo do mínimo");
    }

    // ========================= QUANTIDADE POR CATEGORIA =========================

    /**
     * Gera um relatório mostrando a quantidade de produtos cadastrados por categoria.
     *
     * @return lista de mapas contendo:
     *         <ul>
     *             <li>{@code categoria} — nome da categoria;</li>
     *             <li>{@code quantidade} — número de produtos cadastrados.</li>
     *         </ul>
     */
    public List<Map<String, Object>> quantidadePorCategoria() {
        String sql = """
            SELECT 
                c.nome AS categoria, 
                COALESCE(COUNT(p.id), 0) AS quantidade
            FROM categoria c
            LEFT JOIN produto p ON p.categoria_id = c.id
            GROUP BY c.nome
            ORDER BY c.nome
        """;

        return executarConsulta(sql, rs -> {
            Map<String, Object> linha = new HashMap<>();
            linha.put("categoria", rs.getString("categoria"));
            linha.put("quantidade", rs.getInt("quantidade"));
            return linha;
        }, "Quantidade por categoria");
    }

    // ========================= PRODUTO MAIS MOVIMENTADO =========================

    /**
     * Gera um relatório com os produtos mais movimentados, considerando tanto entradas quanto saídas.
     *
     * @return lista de mapas contendo:
     *         <ul>
     *             <li>{@code produto} — nome do produto;</li>
     *             <li>{@code categoria} — categoria associada;</li>
     *             <li>{@code entradas} — total de unidades que entraram em estoque;</li>
     *             <li>{@code saidas} — total de unidades que saíram;</li>
     *             <li>{@code total_movimentado} — soma total de entradas e saídas.</li>
     *         </ul>
     */
    public List<Map<String, Object>> produtoMaisMovimentado() {
        String sql = """
            SELECT p.nome AS produto, c.nome AS categoria,
                   SUM(CASE WHEN m.tipo = 'ENTRADA' THEN m.quantidade ELSE 0 END) AS entradas,
                   SUM(CASE WHEN m.tipo = 'SAIDA' THEN m.quantidade ELSE 0 END) AS saidas
            FROM movimentacao m
            JOIN produto p ON m.produto_id = p.id
            JOIN categoria c ON p.categoria_id = c.id
            GROUP BY p.nome, c.nome
            ORDER BY (SUM(CASE WHEN m.tipo = 'ENTRADA' THEN m.quantidade ELSE 0 END) +
                      SUM(CASE WHEN m.tipo = 'SAIDA' THEN m.quantidade ELSE 0 END)) DESC
        """;

        return executarConsulta(sql, rs -> {
            Map<String, Object> linha = new HashMap<>();
            linha.put("produto", rs.getString("produto"));
            linha.put("categoria", rs.getString("categoria"));
            linha.put("entradas", rs.getInt("entradas"));
            linha.put("saidas", rs.getInt("saidas"));
            linha.put("total_movimentado", rs.getInt("entradas") + rs.getInt("saidas"));
            return linha;
        }, "Produto mais movimentado");
    }

    // ======================================================================
    // MÉTODO AUXILIAR GENÉRICO PARA EXECUTAR QUALQUER CONSULTA SQL
    // ======================================================================

    /**
     * Executa uma consulta SQL e transforma cada linha do resultado em um mapa ({@code Map<String, Object>}).
     *
     * @param sql comando SQL a ser executado
     * @param mapper função que converte o {@link ResultSet} em um {@link Map}
     * @param nomeRelatorio nome amigável usado para logs e mensagens
     * @return uma lista de linhas ({@code List<Map<String, Object>>})
     */
    private List<Map<String, Object>> executarConsulta(
            String sql,
            ResultMapper mapper,
            String nomeRelatorio
    ) {
        List<Map<String, Object>> lista = new ArrayList<>();
        try (Connection conn = ConexaoDAO.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapper.map(rs));
            }

            System.out.printf("📊 %s gerado com sucesso. Total de registros: %d%n",
                    nomeRelatorio, lista.size());

        } catch (SQLException e) {
            System.err.printf("💥 Erro ao gerar relatório '%s': %s%n",
                    nomeRelatorio, e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Interface funcional interna responsável por mapear uma linha de {@link ResultSet}
     * em um {@link Map} contendo os dados do relatório.
     */
    @FunctionalInterface
    private interface ResultMapper {
        /**
         * Mapeia uma linha do {@link ResultSet} para um {@link Map}.
         *
         * @param rs conjunto de resultados da consulta SQL
         * @return mapa com colunas e valores correspondentes
         * @throws SQLException caso ocorra erro na leitura dos dados
         */
        Map<String, Object> map(ResultSet rs) throws SQLException;
    }
}
