package ProdutoService;

/**
 *
 * @author Fabricio de Aguiar
 */
import dao.CategoriaDAO;
import java.util.List;
import model.Categoria;

public class CategoriaService {

    private final CategoriaDAO categoriaDAO;

    public CategoriaService() {
        this.categoriaDAO = new CategoriaDAO();
    }

    public String inserir(Categoria categoria) {
        try {
            // Validação simples
            if (categoria.getNome() == null || categoria.getNome().isBlank()) {
                return "ERRO: Nome da categoria não pode estar vazio.";
            }
            if (categoria.getEmbalagem() == null || categoria.getTamanho() == null) {
                return "ERRO: Embalagem e tamanho são obrigatórios.";
            }

            categoriaDAO.inserir(categoria);
            return "OK: Categoria inserida com sucesso!";
        } catch (Exception e) {
            return "ERRO ao inserir categoria: " + e.getMessage();
        }
    }

    public List<Categoria> listar() {
        try {
            return categoriaDAO.listar();
        } catch (Exception e) {
            System.err.println("ERRO ao listar categorias: " + e.getMessage());
            return List.of(); // retorna lista vazia em caso de erro
        }
    }

    public String atualizar(Categoria categoria) {
        try {
            if (categoria.getId() <= 0) {
                return "ERRO: ID inválido.";
            }
            categoriaDAO.atualizar(categoria);
            return "OK: Categoria atualizada com sucesso!";
        } catch (Exception e) {
            return "ERRO ao atualizar categoria: " + e.getMessage();
        }
    }

    public String excluir(int id) {
        try {
            if (id <= 0) {
                return "ERRO: ID inválido.";
            }
            categoriaDAO.excluir(id);
            return "OK: Categoria excluída com sucesso!";
        } catch (Exception e) {
            return "ERRO ao excluir categoria: " + e.getMessage();
        }
    }
}
