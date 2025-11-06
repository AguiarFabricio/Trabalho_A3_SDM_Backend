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
}
