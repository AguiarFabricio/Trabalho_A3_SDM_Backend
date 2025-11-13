package service;

import dao.ProdutoDAO;
import model.Produto;
import java.util.List;

/**
 * Classe {@code ProdutoService} responsável por gerenciar as regras de negócio
 * relacionadas aos produtos do sistema de estoque.
 *
 * <p>Faz a intermediação entre a camada de controle (por exemplo, o servidor)
 * e a camada de persistência representada por {@link ProdutoDAO}.</p>
 *
 * <p><b>Responsabilidades principais:</b></p>
 * <ul>
 *   <li>Inserir novos produtos no banco de dados;</li>
 *   <li>Listar todos os produtos cadastrados;</li>
 *   <li>Atualizar dados de produtos existentes.</li>
 * </ul>
 *
 * <p>Esta classe não realiza validações complexas — apenas encaminha as operações
 * para o DAO correspondente, podendo ser expandida futuramente com regras
 * de negócio adicionais (ex.: verificação de estoque mínimo ou categorias).</p>
 *
 * @author Luiz
 * @version 1.0
 * @since 2025
 */
public class ProdutoService {

    /** DAO responsável pelas operações de persistência da entidade {@link Produto}. */
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    /**
     * Insere um novo produto no banco de dados.
     *
     * @param produto objeto {@link Produto} a ser inserido.
     * @return uma {@link String} indicando o resultado da operação:
     *         <ul>
     *             <li>{@code "Produto inserido com sucesso!"} em caso de sucesso;</li>
     *             <li>{@code "Erro ao inserir produto: ..."} em caso de falha.</li>
     *         </ul>
     */
    public String inserir(Produto produto) {
        return produtoDAO.inserir(produto);
    }

    /**
     * Lista todos os produtos cadastrados no sistema.
     *
     * @return uma {@link List} de objetos {@link Produto} representando os produtos registrados.
     */
    public List<Produto> listar() {
        return produtoDAO.listar();
    }

    /**
     * Atualiza os dados de um produto existente.
     *
     * @param produto objeto {@link Produto} contendo os novos dados a serem persistidos.
     * @return uma {@link String} indicando o resultado da operação:
     *         <ul>
     *             <li>{@code "Produto atualizado com sucesso!"} caso a atualização seja bem-sucedida;</li>
     *             <li>{@code "Erro ao atualizar produto: ..."} em caso de falha.</li>
     *         </ul>
     */
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
