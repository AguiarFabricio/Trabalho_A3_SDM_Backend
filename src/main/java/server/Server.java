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
        try (Socket s = socket) {
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());

            CategoriaService categoriaService = new CategoriaService();
            ProdutoService produtoService = new ProdutoService();

            String comando = in.readUTF();
            System.out.println("comando recebido: " + comando);

            switch (comando) {

                // ---- CATEGORIAS ----
                case "INSERIR_CATEGORIA" -> {
                    Categoria c = (Categoria) in.readObject();
                    String resposta = categoriaService.inserir(c);
                    out.writeUTF(resposta);
                    out.flush();
                }

                case "LISTAR_CATEGORIAS" -> {
                    List<Categoria> lista = categoriaService.listar();
                    out.writeObject(lista);
                    out.flush();
                }

                // ---- PRODUTOS ----
                case "INSERIR_PRODUTO" -> {
                    Produto p = (Produto) in.readObject();
                    String resposta = produtoService.inserir(p);
                    out.writeUTF(resposta);
                    out.flush();
                }

                case "LISTAR_PRODUTOS" -> {
                    List<Produto> lista = produtoService.listar();
                    out.writeObject(lista);
                    out.flush();
                }

                default -> {
                    out.writeUTF("ERRO: comando desconhecido");
                    out.flush();
                }
            }

        } catch (Exception e) {
            System.err.println("Erro ao atender cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
