package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Produto;
import model.Categoria;

public class ProdutoDAO {

    public void inserir(Produto p) {
        String sql = "INSERT INTO produto (nome, unidade, categoria_id, quantidadeEstoque, quantidadeMinima, quantidadeMaxima, precoUnitario) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoDAO.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getUnidade());
            stmt.setInt(3, p.getCategoria() != null ? p.getCategoria().getId() : 0);
            stmt.setInt(4, p.getQuantidadeEstoque());
            stmt.setInt(5, p.getQuantidadeMinima());
            stmt.setInt(6, p.getQuantidadeMaxima());
            stmt.setDouble(7, p.getPrecoUnitario());
            stmt.executeUpdate();

            // recuperar id gerado se necessário
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao inserir produto: " + e.getMessage());
        }
    }

    public List<Produto> listar() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto";
        try (Connection conn = ConexaoDAO.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setUnidade(rs.getString("unidade"));
                p.setPrecoUnitario(rs.getDouble("precoUnitario"));
                p.setQuantidadeEstoque(rs.getInt("quantidadeEstoque"));
                p.setQuantidadeMinima(rs.getInt("quantidadeMinima"));
                p.setQuantidadeMaxima(rs.getInt("quantidadeMaxima"));

                // Carregar categoria básica (apenas id)
                int categoriaId = rs.getInt("categoria_id");
                if (categoriaId > 0) {
                    Categoria cat = new Categoria();
                    cat.setId(categoriaId);
                    p.setCategoria(cat);
                }

                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }
        return lista;
    }

    public void atualizar(Produto p) {
        String sql = "UPDATE produto SET nome=?, unidade=?, categoria_id=?, quantidadeEstoque=?, quantidadeMinima=?, quantidadeMaxima=?, precoUnitario=? WHERE id=?";
        try (Connection conn = ConexaoDAO.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getUnidade());
            stmt.setInt(3, p.getCategoria() != null ? p.getCategoria().getId() : 0);
            stmt.setInt(4, p.getQuantidadeEstoque());
            stmt.setInt(5, p.getQuantidadeMinima());
            stmt.setInt(6, p.getQuantidadeMaxima());
            stmt.setDouble(7, p.getPrecoUnitario());
            stmt.setInt(8, p.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM produto WHERE id=?";
        try (Connection conn = ConexaoDAO.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao excluir produto: " + e.getMessage());
        }
    }

    public Produto buscarPorId(int id) {
        String sql = "SELECT * FROM produto WHERE id = ?";
        Produto produto = null;

        try (Connection conn = ConexaoDAO.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    produto = new Produto();
                    produto.setId(rs.getInt("id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setUnidade(rs.getString("unidade"));
                    produto.setPrecoUnitario(rs.getDouble("precoUnitario"));
                    produto.setQuantidadeEstoque(rs.getInt("quantidadeEstoque"));
                    produto.setQuantidadeMinima(rs.getInt("quantidadeMinima"));
                    produto.setQuantidadeMaxima(rs.getInt("quantidadeMaxima"));

                    int categoriaId = rs.getInt("categoria_id");
                    if (categoriaId > 0) {
                        Categoria cat = new Categoria();
                        cat.setId(categoriaId);
                        produto.setCategoria(cat);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar produto por id: " + e.getMessage());
        }

        return produto;
    }

    public boolean reajustarPrecos(double percentual) {
        String sql = "UPDATE produto SET precoUnitario = precoUnitario + (precoUnitario * ? / 100)";
        try (Connection conn = ConexaoDAO.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, percentual);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao reajustar preços: " + e.getMessage());
            return false;
        }
    }
}
