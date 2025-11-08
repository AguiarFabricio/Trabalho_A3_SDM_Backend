package service;

import dao.ProdutoDAO;
import model.Produto;
import java.util.List;

public class ProdutoService {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    public String inserir(Produto produto) {
        return produtoDAO.inserir(produto);
    }

    public List<Produto> listar() {
        return produtoDAO.listar();
    }

    public String atualizar(Produto produto) {
        try {
            produtoDAO.atualizar(produto);
            return "Produto atualizado com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao atualizar produto: " + e.getMessage();
        }
    }

}
