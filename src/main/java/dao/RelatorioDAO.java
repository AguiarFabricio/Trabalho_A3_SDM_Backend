package dao;

import model.Produto;
import model.Categoria;
import model.EmbalagemProduto;
import model.TamanhoProduto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RelatorioDAO {

    public List<Produto> listarProdutosAbaixoDoMinimo() {
        return listarPorCondicao("p.quantidade_atual < p.quantidade_minima");
    }

    public List<Produto> listarProdutosAcimaDoMaximo() {
        return listarPorCondicao("p.quantidade_atual > p.quantidade_maxima");
    }

    public List<Produto> listarTodos() {
        return listarPorCondicao("1=1");
    }

    // 🔹 NOVO MÉTODO: quantidade de produtos por categoria
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

                // ✅ Proteção contra valores nulos
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

    // 🔹 Método reutilizado pelos outros relatórios
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
