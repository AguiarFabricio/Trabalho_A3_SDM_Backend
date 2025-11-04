package service;

import dao.ProdutoDAO;
import model.Produto;
import java.util.List;

public class ProdutoService {

    private final ProdutoDAO produtoDAO;

    public ProdutoService() {
        this.produtoDAO = new ProdutoDAO();
    }

    //Inserir novo produto
    public String inserir(Produto produto) {
        if (produto == null) {
            return "ERRO: Produto inválido.";
        }
        if (produto.getNome() == null || produto.getNome().isEmpty()) {
            return "ERRO: O nome do produto não pode ser vazio.";
        }
        if (produto.getCategoria() == null) {
            return "ERRO: O produto deve estar vinculado a uma categoria.";
        }
        if (produto.getPrecoUnitario() < 0) {
            return "ERRO: O preço não pode ser negativo.";
        }

        try {
            produtoDAO.inserir(produto);
            return "OK: Produto inserido com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERRO ao inserir produto: " + e.getMessage();
        }
    }

    // Listar todos os produtos
    public List<Produto> listar() {
        try {
            return produtoDAO.listar();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERRO ao listar produtos: " + e.getMessage());
            return List.of();
        }
    }

    //Atualizar produto existente
    public String atualizar(Produto produto) {
        if (produto == null || produto.getId() <= 0) {
            return "ERRO: Produto inválido para atualização.";
        }

        try {
            produtoDAO.atualizar(produto);
            return "OK: Produto atualizado com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERRO ao atualizar produto: " + e.getMessage();
        }
    }

    // Excluir produto por ID
    public String excluir(int id) {
        if (id <= 0) {
            return "ERRO: ID inválido para exclusão.";
        }

        try {
            produtoDAO.excluir(id);
            return "OK: Produto excluído com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERRO ao excluir produto: " + e.getMessage();
        }
    }
}
