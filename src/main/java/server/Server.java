package server;

import java.io.*;
import java.net.*;
import java.util.List;
import model.Categoria;
import model.Produto;
import service.CategoriaService;
import service.ProdutoService;

public class Server {

    private static final int PORTA = 1234;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORTA)) {
            System.out.println("Servidor iniciado na porta " + PORTA);

            while (true) {
                Socket cliente = server.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress());
                new Thread(() -> atenderCliente(cliente)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void atenderCliente(Socket socket) {
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            CategoriaService categoriaService = new CategoriaService();
            ProdutoService produtoService = new ProdutoService();

            String comando = in.readUTF();
            System.out.println("Comando recebido: " + comando);

            switch (comando) {

                // ---- CATEGORIAS ----
                case "INSERIR_CATEGORIA" -> {
                    Categoria c = (Categoria) in.readObject();
                    String resposta = categoriaService.inserir(c);
                    out.writeUTF(resposta);
                    out.flush();
                    System.out.println("Categoria inserida: " + c.getNome());
                }
                case "ATUALIZAR_CATEGORIA" -> {
                    try {
                        Categoria categoria = (Categoria) in.readObject();
                        System.out.println("Servidor recebendo categoria para atualizar: " + categoria);

                        categoriaService.atualizar(categoria);

                        out.writeUTF("Categoria atualizada com sucesso!");
                        out.flush();

                    } catch (Exception e) {
                        e.printStackTrace();
                        out.writeUTF("Erro ao atualizar categoria: " + e.getMessage());
                        out.flush();
                    }
                }

                case "LISTAR_CATEGORIAS" -> {
                    try {
                        List<Categoria> lista = categoriaService.listar();
                        System.out.println("Servidor enviando categorias: " + lista);

                        out.writeObject(lista);
                        out.flush();
                        System.out.println("✅ Lista de categorias enviada com sucesso!");
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.writeObject("Erro ao listar categorias: " + e.getMessage());
                        out.flush();
                    }
                }

                case "EXCLUIR_CATEGORIA" -> {
                    try {
                        Integer idCategoria = (Integer) in.readObject();  // receber o id
                        System.out.println("Servidor recebendo ID para exclusão: " + idCategoria);

                        String resposta = categoriaService.excluir(idCategoria); // chama service
                        out.writeUTF(resposta);
                        out.flush();
                        System.out.println(resposta);

                    } catch (Exception e) {
                        e.printStackTrace();
                        out.writeUTF("Erro ao excluir categoria: " + e.getMessage());
                        out.flush();
                    }
                }

                // ---- PRODUTOS ----
                case "INSERIR_PRODUTO" -> {
                    Produto p = (Produto) in.readObject();
                    String resposta = produtoService.inserir(p);
                    out.writeUTF(resposta);
                    out.flush();
                    System.out.println("Produto inserido: " + p.getNome());
                }

                case "LISTAR_PRODUTOS" -> {
                    List<Produto> lista = produtoService.listar();
                    out.writeObject(lista);
                    out.flush();
                    System.out.println("Lista de produtos enviada.");
                }

                default -> {
                    out.writeUTF("ERRO: comando desconhecido");
                    out.flush();
                    System.err.println("Comando desconhecido recebido: " + comando);
                }
            }

        } catch (Exception e) {
            System.err.println("Erro ao atender cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ignored) {
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ignored) {
            }
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ignored) {
            }
            System.out.println("Conexão encerrada com o cliente.");
        }
    }
}
