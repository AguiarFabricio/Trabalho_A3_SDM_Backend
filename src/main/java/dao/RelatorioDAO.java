package dao;

import model.Produto;
import model.Categoria;
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

    private List<Produto> listarPorCondicao(String condicao) {
        List<Produto> lista = new ArrayList<>();
        String sql = """
            SELECT p.*, c.id AS categoria_id, c.nome AS categoria_nome
            FROM produto p
            JOIN categoria c ON p.categoria_id = c.id
            WHERE %s
        """.formatted(condicao);

        try (Connection conn = ConexaoDAO.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Categoria cat = new Categoria(rs.getInt("categoria_id"), rs.getString("categoria_nome"));
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
}
