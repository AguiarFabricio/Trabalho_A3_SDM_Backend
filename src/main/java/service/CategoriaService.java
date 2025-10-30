package service;

import dao.CategoriaDAO;
import model.Categoria;
import java.util.List;

public class CategoriaService {

    private CategoriaDAO categoriaDAO;

    public CategoriaService() {
        this.categoriaDAO = new CategoriaDAO();

    }

    public boolean salvar(Categoria categoria) {
        if (categoria == null) {
            System.out.println("Categoria nula.");
            return false;
        }
        if (categoria.getNome() == null || categoria.getNome().isEmpty()) {
            System.out.println("Nome da categoria não pode ser vazio.");
            return false;
        }

        try {
            categoriaDAO.inserir(categoria);
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao salvar categoria." + e.getMessage());
            return false;
        }
    }

    public List<Categoria> listar() {
        return categoriaDAO.listar();
    }

    public Categoria bucarPorId(int id) {
        if (id <= 0) {
            System.out.println("ID inválido");
            return null;
        }
        try {
            List<Categoria> lista = categoriaDAO.listar();
            for (Categoria c : lista) {
                if (c.getId() == id) {
                    return c;
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar categoria: " + e.getMessage());
        }
        return null;
    }
    
    public boolean atualizar(Categoria categoria) {
        if (categoria == null || categoria.getId() <= 0) {
            System.out.println("Categoria inválida para atualização.");
            return false;
        }
        try {
            categoriaDAO.atualizar(categoria);
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao atualizar categoria: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deletar(int id) {
        if (id <= 0) {
            System.out.println("ID inválido para exclusão.");
            return false;
        }
        try {
            categoriaDAO.excluir(id);
            return true;
        } catch (Exception e) {
            System.out.println("Errro ao excluir categoria: " + e.getMessage());
            return false;
        }
    }
}
