package service;

import dao.CategoriaDAO;
import model.Categoria;
import java.util.List;

public class CategoriaService {

    private final CategoriaDAO categoriaDAO;

    public CategoriaService() {
        this.categoriaDAO = new CategoriaDAO();
    }

    // ➕ Inserir nova categoria
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

    // 📋 Listar todas as categorias
    public List<Categoria> listar() {
        try {
            return categoriaDAO.listar();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERRO ao listar categorias: " + e.getMessage());
            return List.of(); // retorna lista vazia em caso de erro
        }
    }

    // 🔍 Buscar categoria por ID
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

    // ✏️ Atualizar categoria existente
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

    // ❌ Excluir categoria por ID
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
