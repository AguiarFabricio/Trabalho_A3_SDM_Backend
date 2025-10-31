package server;

import java.io.*;
import java.net.*;
import java.util.List;
import dao.CategoriaDAO;
import model.Categoria;

public class Server {

    private static final int PORTA = 1234;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORTA)) {
            System.out.println("✅ Servidor na porta " + PORTA);

            while (true) {
                Socket cliente = server.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress());
                new Thread(() -> atender(cliente)).start();
            }

        } catch (IOException e) {
            e.printStackTrace(); // importante pra saber se deu erro ao abrir porta
        }
    }

    private static void atender(Socket socket) {
        try (Socket s = socket) {
            // ordem correta das streams
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            out.flush(); // evita deadlock
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());

            String comando = in.readUTF();
            System.out.println("Comando recebido: " + comando);

            switch (comando) {
                case "INSERIR_CATEGORIA" -> {
                    Categoria c = (Categoria) in.readObject();
                    new CategoriaDAO().inserir(c);
                    out.writeUTF("OK: Categoria inserida");
                    out.flush();
                }

                case "LISTAR_CATEGORIAS" -> {
                    List<Categoria> lista = new CategoriaDAO().listar();
                    out.writeObject(lista);
                    out.flush();
                }

                case "ATUALIZAR_CATEGORIA" -> {
                    Categoria cUpd = (Categoria) in.readObject();
                    new CategoriaDAO().atualizar(cUpd);
                    out.writeUTF("OK: Categoria atualizada");
                    out.flush();
                }

                case "EXCLUIR_CATEGORIA" -> {
                    int id = in.readInt();
                    new CategoriaDAO().excluir(id);
                    out.writeUTF("OK: Categoria excluída");
                    out.flush();
                }

                default -> {
                    out.writeUTF("ERRO: comando desconhecido");
                    out.flush();
                }
            }

        } catch (Exception ex) {
            System.err.println("Erro ao atender cliente: " + ex.getMessage());
        }
    }
}
