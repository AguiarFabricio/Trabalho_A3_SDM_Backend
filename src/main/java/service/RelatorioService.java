package service;

import dao.RelatorioDAO;
import model.Produto;
import java.util.List;

public class RelatorioService {
    private final RelatorioDAO relatorioDAO = new RelatorioDAO();

    public List<Produto> listarProdutosAbaixoDoMinimo() {
        return relatorioDAO.listarProdutosAbaixoDoMinimo();
    }

    public List<Produto> listarProdutosAcimaDoMaximo() {
        return relatorioDAO.listarProdutosAcimaDoMaximo();
    }

    public List<Produto> listarTodosProdutos() {
        return relatorioDAO.listarTodos();
    }
}
