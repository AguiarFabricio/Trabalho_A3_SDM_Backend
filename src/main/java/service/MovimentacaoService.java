package service;

import dao.MovimentacaoDAO;
import dao.ProdutoDAO;
import model.Movimentacao;
import model.Produto;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class MovimentacaoService {

    private final MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    /*
     * Registra movimentação de entrada ou saída e atualiza o estoque
     */
    public String registrarEstoque(int produtoId, int quantidade, String tipo, String dataStr) {
        try {
            // 1️⃣ Criar movimentação
            Movimentacao movimentacao = new Movimentacao();
            movimentacao.setProdutoId(produtoId);
            movimentacao.setQuantidade(quantidade);
            movimentacao.setTipo(tipo);

            // 2️⃣ Definir data
            LocalDateTime data;
            try {
                if (dataStr != null && !dataStr.isEmpty()) {
                    data = LocalDateTime.parse(dataStr);
                } else {
                    data = LocalDateTime.now();
                }
            } catch (DateTimeParseException e) {
                data = LocalDateTime.now();
            }
            movimentacao.setData(data);

            // 3️⃣ Registrar movimentação no banco
            movimentacaoDAO.inserir(movimentacao);

            // 4️⃣ Atualizar estoque do produto
            Produto produto = produtoDAO.buscarPorId(produtoId);
            if (produto == null) {
                return "ERRO: Produto não encontrado.";
            }

            if ("ENTRADA".equalsIgnoreCase(tipo)) {
                produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidade);
            } else if ("SAIDA".equalsIgnoreCase(tipo)) {
                produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade);
            } else {
                return "ERRO: Tipo de movimentação inválido.";
            }

            produtoDAO.atualizar(produto);

            // 5️⃣ Verificar se está fora dos limites
            if (produto.getQuantidadeEstoque() > produto.getQuantidadeMaxima()) {
                return "AVISO: Quantidade do produto '" + produto.getNome() + "' acima do máximo permitido.";
            } else if (produto.getQuantidadeEstoque() < produto.getQuantidadeMinima()) {
                return "AVISO: Quantidade do produto '" + produto.getNome() + "' abaixo do mínimo permitido.";
            }

            return "OK: Movimentação registrada com sucesso!";

        } catch (Exception e) {
            e.printStackTrace();
            return "ERRO ao registrar movimentação: " + e.getMessage();
        }
    }
}
