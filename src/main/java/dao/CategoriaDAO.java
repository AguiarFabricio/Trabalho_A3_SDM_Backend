package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Categoria;
import model.EmbalagemProduto;
import model.TamanhoProduto;

public class CategoriaDAO {

    public void inserir(Categoria categoria) {
        String sql = "INSERT INTO categoria (nome, embalagem, tamanho) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoDAO.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getEmbalagem().name());
            stmt.setString(3, categoria.getTamanho().name());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao inserir categoria: " + e.getMessage());
        }
    }

    public List<Categoria> listar() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categoria";
        try (Connection conn = ConexaoDAO.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Categoria c = new Categoria();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setEmbalagem(EmbalagemProduto.valueOf(rs.getString("embalagem")));
                c.setTamanho(TamanhoProduto.valueOf(rs.getString("tamanho")));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar categorias: " + e.getMessage());
        }
        return lista;
    }

    public void atualizar(Categoria categoria) {
        String sql = "UPDATE categoria SET nome=?, embalagem=?, tamanho=? WHERE id=?";
        try (Connection conn = ConexaoDAO.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getEmbalagem().name());
            stmt.setString(3, categoria.getTamanho().name());
            stmt.setInt(4, categoria.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar categoria: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM categoria WHERE id=?";
        try (Connection conn = ConexaoDAO.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao excluir categoria: " + e.getMessage());
        }
    }

    /**
     * Busca uma categoria por ID. Retorna null se não encontrado.
     */
    public Categoria buscarPorId(int id) {
        String sql = "SELECT * FROM categoria WHERE id = ?";
        Categoria categoria = null;

        try (Connection conn = ConexaoDAO.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    categoria = new Categoria();
                    categoria.setId(rs.getInt("id"));
                    categoria.setNome(rs.getString("nome"));
                    categoria.setEmbalagem(EmbalagemProduto.valueOf(rs.getString("embalagem")));
                    categoria.setTamanho(TamanhoProduto.valueOf(rs.getString("tamanho")));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar categoria por id: " + e.getMessage());
        }

        return categoria;
    }
}
