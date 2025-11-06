package service;

import dao.MovimentacaoDAO;
import model.Movimentacao;
import java.util.List;

public class MovimentacaoService {
    private final MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();

    public String registrar(Movimentacao movimentacao) {
        return movimentacaoDAO.inserir(movimentacao);
    }

    public List<Movimentacao> listar() {
        return movimentacaoDAO.listar();
    }

    public List<Movimentacao> listarPorProduto(int produtoId) {
        return movimentacaoDAO.listarPorProduto(produtoId);
    }

    public List<Movimentacao> listarPorTipo(String tipo) {
        return movimentacaoDAO.listarPorTipo(tipo);
    }
}
