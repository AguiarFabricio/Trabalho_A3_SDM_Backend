package ProdutoService;

/**
 *
 * @author Fabricio de Aguiar
 */
import dao.ProdutoDAO;

public class ProdutoService {

    private final ProdutoDAO produtoDAO;

    public ProdutoService(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    public boolean reajustarPrecos(double percentual) {
        if (percentual <= 0) {
            throw new IllegalArgumentException("O percentual deve ser maior que zero.");
        }
        return produtoDAO.reajustarPrecos(percentual);
    }
}
