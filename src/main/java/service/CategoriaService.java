package service;

import dao.CategoriaDAO;
import model.Categoria;
import java.util.List;

/**
 * Classe {@code CategoriaService} responsável por aplicar regras de negócio
 * e realizar o gerenciamento das operações relacionadas à entidade
 * {@link model.Categoria}.
 *
 * <p>Atua como uma camada intermediária entre:</p>
 * <ul>
 *     <li><b>Camada de controle</b> — servidor ou interface Swing (Cliente Socket)</li>
 *     <li><b>Camada de persistência</b> — {@link dao.CategoriaDAO}</li>
 * </ul>
 *
 * <p>Suas principais funções incluem:</p>
 * <ul>
 *     <li>Validar dados recebidos do cliente;</li>
 *     <li>Delegar chamadas ao DAO;</li>
 *     <li>Retornar mensagens padronizadas ao servidor;</li>
 *     <li>Evitar que exceções "estourem" para outras camadas.</li>
 * </ul>
 *
 * <p>A ideia é manter o servidor simples, deixando para o service
 * as validações e regras necessárias.</p>
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

        // 🔍 Verifica se o objeto veio nulo
        if (categoria == null) {
            return "ERRO: Categoria nula.";
        }

        // 🔍 Valida nome obrigatório
        if (categoria.getNome() == null || categoria.getNome().isEmpty()) {
            return "ERRO: Nome da categoria não pode ser vazio.";
        }

        try {
            categoriaDAO.inserir(categoria);
            return "OK: Categoria inserida com sucesso!";
        } catch (Exception e) {

            // Imprime stack trace para debug, mas envia retorno limpo ao cliente
            e.printStackTrace();
            return "ERRO ao inserir categoria: " + e.getMessage();
        }
    }

    /**
     * Retorna uma lista com todas as categorias cadastradas.
     *
     * @return uma {@link List} de {@link Categoria}, ou lista vazia se ocorrer erro.
     */
    public List<Categoria> listar() {
        try {
            return categoriaDAO.listar();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERRO ao listar categorias: " + e.getMessage());

            // Evita null pointer no cliente retornando lista vazia
            return List.of();
        }
    }

    /**
     * Busca uma categoria específica pelo seu identificador (ID).
     *
     * @param id identificador único da categoria.
     * @return a {@link Categoria} correspondente, ou {@code null} se não encontrada.
     */
    public Categoria buscarPorId(int id) {

        // Validação básica do ID
        if (id <= 0) {
            System.out.println("ID inválido.");
            return null;
        }

        try {
            List<Categoria> lista = categoriaDAO.listar();

            // 🔎 Procura manualmente na lista retornada
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
     * Atualiza as informações de uma categoria existente.
     *
     * @param categoria objeto {@link Categoria} contendo os dados atualizados.
     * @return mensagem de status:
     *         <ul>
     *             <li>{@code "OK: Categoria atualizada com sucesso!"}</li>
     *             <li>{@code "ERRO: ..."} em caso de falha</li>
     *         </ul>
     */
    public String atualizar(Categoria categoria) {

        // Validação do objeto e do ID
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
     * Exclui uma categoria do banco pelo seu ID.
     *
     * <p>O DAO já contém validação que impede excluir categorias
     * que possuem produtos associados.</p>
     *
     * @param id identificador da categoria.
     * @return mensagem de status padronizada.
     */
    public String excluir(int id) {

        // Validação simples
        if (id <= 0) {
            return "ERRO: ID inválido para exclusão.";
        }

        try {
            categoriaDAO.excluir(id);
            return "OK: Categoria excluída com sucesso!";

        } catch (Exception e) {

            // Aqui cai quando o DAO lança a Exception:
            // "Não é possível excluir a categoria: existem produtos associados."
            e.printStackTrace();
            return "ERRO ao excluir categoria: " + e.getMessage();
        }
    }
}
