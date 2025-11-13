package service;

import dao.MovimentacaoDAO;
import model.Movimentacao;
import java.util.List;

/**
 * Classe {@code MovimentacaoService} responsável por gerenciar as regras de negócio
 * relacionadas às movimentações de estoque (entradas e saídas de produtos).
 *
 * <p>Serve como intermediária entre a camada de controle (como o servidor)
 * e a camada de persistência ({@link dao.MovimentacaoDAO}).</p>
 *
 * <p><b>Principais responsabilidades:</b></p>
 * <ul>
 *   <li>Registrar movimentações de entrada e saída de produtos;</li>
 *   <li>Listar movimentações por produto ou tipo;</li>
 *   <li>Delegar as operações de acesso ao banco de dados ao {@link MovimentacaoDAO}.</li>
 * </ul>
 *
 * @author Luiz
 * @version 1.0
 * @since 2025
 */
public class MovimentacaoService {

    /** DAO responsável pelas operações de persistência da entidade {@link Movimentacao}. */
    private final MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();

    /**
     * Registra uma nova movimentação (entrada ou saída) no banco de dados.
     *
     * @param movimentacao objeto {@link Movimentacao} contendo os dados da operação.
     * @return uma {@link String} com o status da operação:
     *         <ul>
     *             <li>{@code "OK: ..."} caso a movimentação seja registrada com sucesso;</li>
     *             <li>{@code "ERRO: ..."} caso ocorra algum problema.</li>
     *         </ul>
     */
    public String registrar(Movimentacao movimentacao) {
        return movimentacaoDAO.inserir(movimentacao);
    }

    /**
     * Lista todas as movimentações registradas no sistema.
     *
     * @return uma {@link List} de {@link Movimentacao} representando todas as movimentações registradas.
     */
    public List<Movimentacao> listar() {
        return movimentacaoDAO.listar();
    }

    /**
     * Lista todas as movimentações associadas a um determinado produto.
     *
     * @param produtoId identificador único do produto a ser filtrado.
     * @return uma {@link List} de {@link Movimentacao} relacionadas ao produto informado.
     */
    public List<Movimentacao> listarPorProduto(int produtoId) {
        return movimentacaoDAO.listarPorProduto(produtoId);
    }

    /**
     * Lista todas as movimentações filtradas por tipo.
     *
     * @param tipo tipo da movimentação (por exemplo: {@code "ENTRADA"} ou {@code "SAIDA"}).
     * @return uma {@link List} de {@link Movimentacao} filtradas pelo tipo informado.
     */
    public List<Movimentacao> listarPorTipo(String tipo) {
        return movimentacaoDAO.listarPorTipo(tipo);
    }
}
