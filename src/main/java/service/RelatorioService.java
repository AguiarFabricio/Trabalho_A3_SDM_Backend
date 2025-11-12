package service;

import dao.ConexaoDAO;
import java.sql.*;
import java.util.*;

/**
 * Serviço responsável por gerar os dados dos relatórios do estoque.
 * Cada método executa uma consulta SQL e retorna uma lista de mapas (List<Map<String, Object>>),
 * onde cada mapa representa uma linha do resultado.
 */
public class RelatorioService {

    // ========================= LISTA DE PREÇOS =========================
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
            linha.put("quantidade", rs.getInt("quantidade")); // 🔒 Nunca nulo
            return linha;
        }, "Quantidade por categoria");
    }

    // ========================= PRODUTO MAIS MOVIMENTADO =========================
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
     * Executa uma consulta SQL e transforma cada linha em um mapa (Map<String, Object>).
     *
     * @param sql SQL a ser executado
     * @param mapper Função que transforma o ResultSet em um Map
     * @param nomeRelatorio nome amigável (para logs)
     * @return lista de linhas (List<Map<String, Object>>)
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

    // Interface funcional interna para mapear resultados genéricos
    @FunctionalInterface
    private interface ResultMapper {
        Map<String, Object> map(ResultSet rs) throws SQLException;
    }
}
