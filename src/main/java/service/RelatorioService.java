package service;

import dao.MovimentacaoDAO;
import dao.ProdutoDAO;
import model.Movimentacao;
import model.Produto;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class RelatorioService {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();

    public String gerarRelatorioCompleto() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        List<Produto> produtos = produtoDAO.listar();
        List<Movimentacao> movimentos = movimentacaoDAO.listar();

        sb.append("===== PRODUTOS ABAIXO DO MÍNIMO =====\n");
        boolean anyBelow = false;
        for (Produto p : produtos) {
            if (p.getQuantidadeEstoque() < p.getQuantidadeMinima()) {
                anyBelow = true;
                sb.append(String.format("ID: %d | %s | Estoque: %d | Min: %d\n",
                        p.getId(), p.getNome(), p.getQuantidadeEstoque(), p.getQuantidadeMinima()));
            }
        }
        if (!anyBelow) sb.append("Nenhum produto abaixo do mínimo.\n");
        sb.append("\n");

        sb.append("===== PRODUTOS ACIMA DO MÁXIMO =====\n");
        boolean anyAbove = false;
        for (Produto p : produtos) {
            if (p.getQuantidadeEstoque() > p.getQuantidadeMaxima()) {
                anyAbove = true;
                sb.append(String.format("ID: %d | %s | Estoque: %d | Max: %d\n",
                        p.getId(), p.getNome(), p.getQuantidadeEstoque(), p.getQuantidadeMaxima()));
            }
        }
        if (!anyAbove) sb.append("Nenhum produto acima do máximo.\n");
        sb.append("\n");

        sb.append("===== HISTÓRICO DE MOVIMENTAÇÕES =====\n");
        if (movimentos.isEmpty()) {
            sb.append("Nenhuma movimentação registrada.\n");
        } else {
            for (Movimentacao m : movimentos) {
                Produto p = produtoDAO.buscarPorId(m.getProdutoId());
                String nomeProduto = (p != null) ? p.getNome() : ("[Produto ID " + m.getProdutoId() + "]");
                sb.append(String.format("ID: %d | Produto: %s | Tipo: %s | Qtd: %d | Data: %s\n",
                        m.getId(), nomeProduto, m.getTipo(), m.getQuantidade(), m.getData().format(dtf)));
            }
        }
        sb.append("\n");

        sb.append("===== VALOR TOTAL DO ESTOQUE =====\n");
        double totalValor = 0.0;
        for (Produto p : produtos) {
            totalValor += p.getQuantidadeEstoque() * p.getPrecoUnitario();
        }
        sb.append(String.format("Valor total do estoque: R$ %.2f\n", totalValor));
        sb.append("\n");

        sb.append("===== RESUMO =====\n");
        sb.append(String.format("Total produtos listados: %d\n", produtos.size()));
        sb.append(String.format("Total movimentações: %d\n", movimentos.size()));

        return sb.toString();
    }
}
