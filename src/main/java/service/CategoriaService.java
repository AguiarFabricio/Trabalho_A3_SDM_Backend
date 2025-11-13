package service;

import dao.CategoriaDAO;
import model.Categoria;
import java.util.List;

/**
 * Classe {@code CategoriaService} responsável por aplicar regras de negócio
 * e realizar o gerenciamento das operações relacionadas à entidade
 * {@link model.Categoria}.
 *
 * <p>Esta classe funciona como intermediária entre a camada de controle
 * (servidor ou interface cliente) e a camada de persistência ({@link dao.CategoriaDAO}).</p>
 *
 * <p><b>Principais responsabilidades:</b></p>
 * <ul>
 *   <li>Validar os dados antes de enviar ao DAO</li>
 *   <li>Tratar exceções de forma amigável</li>
 *   <li>Retornar mensagens de status padronizadas para o servidor</li>
 * </ul>
 *
 * <p>Todos os métodos de operação retornam mensagens ou objetos de domínio,
 * evitando que exceções não tratadas cheguem à camada de controle.</p>
 *
 * @author Luiz
 * @version 1.0
 * @since 2025
 */
public class CategoriaService {

    /** Instância do {@link CategoriaDAO} usada para operações de persistência. */
    private final CategoriaDAO categoriaDAO;

    /**
     * Construtor padrão. Inicializa a instância do {@link CategoriaDAO}.
     */
    public CategoriaService() {
        this.categoriaDAO = new CategoriaDAO();
    }

    /**
     * Insere uma nova categoria no banco de dados, após validar os campos obrigatórios.
     *
     * @param categoria objeto {@link Categoria} contendo as informações a serem inseridas.
     * @return mensagem de status da operação:
     *         <ul>
     *             <li>{@code "OK: Categoria inserida com sucesso!"} se inserção for bem-sucedida;</li>
     *             <li>{@code "ERRO: ..."} caso ocorra erro de validação ou exceção.</li>
     *         </ul>
     */
    public String inserir(Categoria categoria) {
        if (categoria == null) {
            return "ERRO: Categoria nula.";
        }

        if (categoria.getNome() == null || categoria.getNome().isEmpty()) {
            return "ERRO: Nome da categoria não pode ser vazio.";
        }

        try {
            categoriaDAO.inserir(categoria);
            return "OK: Categoria inserida com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERRO ao inserir categoria: " + e.getMessage();
        }
    }

    /**
     * Retorna uma lista com todas as categorias cadastradas.
     *
     * @return uma {@link List} de {@link Categoria}, ou uma lista vazia se ocorrer erro.
     */
    public List<Categoria> listar() {
        try {
            return categoriaDAO.listar();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERRO ao listar categorias: " + e.getMessage());
            return List.of(); // Retorna lista vazia em caso de erro
        }
    }

    /**
     * Busca uma categoria específica pelo seu identificador (ID).
     *
     * @param id identificador único da categoria.
     * @return a {@link Categoria} correspondente, ou {@code null} se não encontrada ou se o ID for inválido.
     */
    public Categoria buscarPorId(int id) {
        if (id <= 0) {
            System.out.println("ID inválido.");
            return null;
        }

        try {
            List<Categoria> lista = categoriaDAO.listar();
            for (Categoria c : lista) {
                if (c.getId() == id) {
                    return c;
                }
            }
            System.out.println("Nenhuma categoria encontrada com o ID: " + id);
        } catch (Exception e) {
            System.out.println("Erro ao buscar categoria: " + e.getMessage());
        }

        return null;
    }

    /**
     * Atualiza as informações de uma categoria existente no banco de dados.
     *
     * @param categoria objeto {@link Categoria} contendo os dados atualizados.
     * @return mensagem de status da operação:
     *         <ul>
     *             <li>{@code "OK: Categoria atualizada com sucesso!"} se bem-sucedida;</li>
     *             <li>{@code "ERRO: ..."} em caso de falha ou validação incorreta.</li>
     *         </ul>
     */
    public String atualizar(Categoria categoria) {
        if (categoria == null || categoria.getId() <= 0) {
            return "ERRO: Categoria inválida para atualização.";
        }

        try {
            categoriaDAO.atualizar(categoria);
            return "OK: Categoria atualizada com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERRO ao atualizar categoria: " + e.getMessage();
        }
    }

    /**
     * Exclui uma categoria do banco de dados com base no seu ID.
     *
     * @param id identificador da categoria a ser removida.
     * @return mensagem de status da operação:
     *         <ul>
     *             <li>{@code "OK: Categoria excluída com sucesso!"} se exclusão ocorrer normalmente;</li>
     *             <li>{@code "ERRO: ..."} se o ID for inválido ou ocorrer exceção.</li>
     *         </ul>
     */
    public String excluir(int id) {
        if (id <= 0) {
            return "ERRO: ID inválido para exclusão.";
        }

        try {
            categoriaDAO.excluir(id);
            return "OK: Categoria excluída com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERRO ao excluir categoria: " + e.getMessage();
        }
    }
}
