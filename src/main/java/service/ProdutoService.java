package service;

import dao.ProdutoDAO;
import model.Produto;
import java.util.List;

public class ProdutoService {

    private ProdutoDAO produtoDAO;

    public ProdutoService() {
        this.produtoDAO = new ProdutoDAO();
    }

    public boolean salvar(Produto produto) {
        if (produto == null) {
            System.out.println("Produto inválido.");
            return false;
        }
        if (produto.getNome() == null || produto.getNome().isEmpty()) {
            System.out.println("O nome do produto não pode ser vazio.");
            return false;
        }
        if (produto.getCategoria() == null) {
            System.out.println("O produto deve estar vinculado a uma categoria.");
            return false;
        }
        if (produto.getPrecoUnitario() < 0) {
            System.out.println("O preço não pode ser negativo.");
            return false;
        }

        try {
            produtoDAO.inserir(produto);
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao salvar produto: " + e.getMessage());
            return false;
        }
    }

    public List<Produto> listar() {
        return produtoDAO.listar();
    }

    public boolean atualizar(Produto produto) {
        if (produto == null || produto.getId() <= 0) {
            System.out.println("Produto inválido para atualização.");
            return false;
        }

        try {
            produtoDAO.atualizar(produto);
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao atualizar produto: " + e.getMessage());
            return false;
        }
    }

    public boolean deletar(int id) {
        if (id <= 0) {
            System.out.println("ID inválido para exclusão.");
            return false;
        }

        try {
            produtoDAO.excluir(id);
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao excluir produto: " + e.getMessage());
            return false;
        }
    }
}
