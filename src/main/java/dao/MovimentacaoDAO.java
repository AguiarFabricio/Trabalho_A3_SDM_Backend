package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Movimentacao;

public class MovimentacaoDAO {

    public void inserir(Movimentacao m) {
        String sql = "INSERT INTO movimentacao (produtoId, quantidade, tipo, data) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoDAO.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, m.getProdutoId());
            stmt.setInt(2, m.getQuantidade());
            stmt.setString(3, m.getTipo());
            stmt.setTimestamp(4, Timestamp.valueOf(m.getData()));
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir movimentação: " + e.getMessage());
        }
    }

    public List<Movimentacao> listar() {
        List<Movimentacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimentacao";

        try (Connection conn = ConexaoDAO.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Movimentacao m = new Movimentacao();
                m.setId(rs.getInt("id"));
                m.setProdutoId(rs.getInt("produtoId"));
                m.setQuantidade(rs.getInt("quantidade"));
                m.setTipo(rs.getString("tipo"));
                m.setData(rs.getTimestamp("data").toLocalDateTime());
                lista.add(m);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar movimentações: " + e.getMessage());
        }
        return lista;
    }

    public void excluir(int id) {
        String sql = "DELETE FROM movimentacao WHERE id=?";
        try (Connection conn = ConexaoDAO.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao excluir movimentação: " + e.getMessage());
        }
    }
}
