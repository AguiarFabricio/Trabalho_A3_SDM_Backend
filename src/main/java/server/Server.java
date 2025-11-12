package server;

import dao.MovimentacaoDAO;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Map;
import model.Categoria;
import model.Movimentacao;
import model.Produto;
import service.CategoriaService;
import service.ProdutoService;
import service.RelatorioService;

/**
 * Servidor principal responsável por gerenciar conexões de clientes e executar
 * comandos recebidos (Categoria, Produto e Movimentação).
 *
 * Cada cliente é atendido em uma thread separada.
 */
public class Server {

    // Porta fixa onde o servidor escutará as conexões
    private static final int PORTA = 1234;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORTA)) {
            System.out.println("✅ Servidor iniciado na porta " + PORTA);

            // Loop infinito para aceitar conexões de clientes
            while (true) {
                Socket cliente = server.accept();
                System.out.println("🔗 Cliente conectado: " + cliente.getInetAddress());

                // Cria uma nova thread para atender cada cliente individualmente
                new Thread(() -> atenderCliente(cliente)).start();
            }

        } catch (IOException e) {
            System.err.println("💥 Erro ao iniciar o servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Método responsável por atender cada cliente conectado, lendo o comando
     * enviado e executando a ação correspondente.
     */
    private static void atenderCliente(Socket socket) {
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {
            // ✅ Criação dos streams de comunicação (apenas 1 par por cliente)
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            // Instancia os serviços usados pelo servidor
            CategoriaService categoriaService = new CategoriaService();
            ProdutoService produtoService = new ProdutoService();
            RelatorioService relatorioService = new RelatorioService();

            // ✅ Lê o comando enviado pelo cliente
            String comando = in.readUTF();
            System.out.println("📥 Comando recebido: " + comando);

            // ===================================================================
            //                        SWITCH DE COMANDOS
            // ===================================================================
            switch (comando) {

                // ===============================================================
                // ---------------------- CATEGORIAS -----------------------------
                // ===============================================================
                case "INSERIR_CATEGORIA" -> {
                    Categoria c = (Categoria) in.readObject();
                    String resposta = categoriaService.inserir(c);
                    out.writeUTF(resposta);
                    out.flush();
                    System.out.println("🟢 Categoria inserida: " + c.getNome());
                }

                case "ATUALIZAR_CATEGORIA" -> {
                    try {
                        Categoria categoria = (Categoria) in.readObject();
                        categoriaService.atualizar(categoria);

                        out.writeUTF("Categoria atualizada com sucesso!");
                        out.flush();
                        System.out.println("🟡 Categoria atualizada: " + categoria.getNome());
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.writeUTF("Erro ao atualizar categoria: " + e.getMessage());
                        out.flush();
                    }
                }

                case "LISTAR_CATEGORIAS" -> {
                    try {
                        List<Categoria> lista = categoriaService.listar();
                        out.writeObject(lista);
                        out.flush();
                        System.out.println("📤 Lista de categorias enviada! Total: " + lista.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.writeUTF("Erro ao listar categorias: " + e.getMessage());
                        out.flush();
                    }
                }

                case "EXCLUIR_CATEGORIA" -> {
                    try {
                        Integer idCategoria = (Integer) in.readObject();
                        String resposta = categoriaService.excluir(idCategoria);
                        out.writeUTF(resposta);
                        out.flush();
                        System.out.println("🗑️ Categoria excluída: ID " + idCategoria);
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.writeUTF("Erro ao excluir categoria: " + e.getMessage());
                        out.flush();
                    }
                }

                // ===============================================================
                // ------------------------ PRODUTOS -----------------------------
                // ===============================================================
                case "INSERIR_PRODUTO" -> {
                    Produto p = (Produto) in.readObject();
                    String resposta = produtoService.inserir(p);
                    out.writeUTF(resposta);
                    out.flush();
                    System.out.println("🟢 Produto inserido: " + p.getNome());
                }

                case "ALTERAR_PRODUTO" -> {
                    try {
                        Produto produto = (Produto) in.readObject();
                        String resposta = produtoService.atualizar(produto);
                        out.writeUTF(resposta);
                        out.flush();
                        System.out.println("🟡 Produto atualizado: " + produto.getNome());
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.writeUTF("Erro ao alterar produto: " + e.getMessage());
                        out.flush();
                    }
                }

                case "LISTAR_PRODUTOS" -> {
                    try {
                        List<Produto> lista = produtoService.listar();
                        out.writeObject(lista);
                        out.flush();
                        System.out.println("📦 Lista de produtos enviada! Total: " + lista.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.writeUTF("Erro ao listar produtos: " + e.getMessage());
                        out.flush();
                    }
                }

                // ===============================================================
                // --------------------- MOVIMENTAÇÕES ----------------------------
                // ===============================================================
                case "INSERIR_MOVIMENTACAO" -> {
                    try {
                        Movimentacao movimentacao = (Movimentacao) in.readObject();
                        MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();

                        // Insere a movimentação no banco
                        String resposta = movimentacaoDAO.inserir(movimentacao);

                        // Retorna a resposta ao cliente
                        out.writeUTF(resposta);
                        out.flush();

                        System.out.println("📦 Movimentação registrada: "
                                + movimentacao.getTipo() + " - "
                                + movimentacao.getQuantidade()
                                + " (Produto ID: " + movimentacao.getProduto().getId() + ")");
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.writeUTF("Erro ao registrar movimentação: " + e.getMessage());
                        out.flush();
                    }
                }

                case "LISTAR_MOVIMENTACOES" -> {
                    try {
                        MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();
                        List<Movimentacao> lista = movimentacaoDAO.listar();

                        out.writeObject(lista);
                        out.flush();

                        System.out.println("Lista de movimentações enviada com sucesso. Total: " + lista.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.writeUTF("Erro ao listar movimentações: " + e.getMessage());
                        out.flush();
                    }
                }

                // ===============================================================
                // --------------------- RELATÓRIOS ------------------------------
                // ===============================================================
                case "RELATORIO_LISTA_PRECOS", "RELATORIO_LISTA_PREC" -> {
                    List<Map<String, Object>> lista = relatorioService.listarPrecos();
                    enviarListaComoTexto(out, lista);
                }

                case "RELATORIO_BALANCO" -> {
                    List<Map<String, Object>> lista = relatorioService.balancoFisicoFinanceiro();
                    enviarListaComoTexto(out, lista);
                }

                case "RELATORIO_ABAIXO_MINIMO", "RELATORIO_ABAIXO_MIN" -> {
                    List<Map<String, Object>> lista = relatorioService.produtosAbaixoDoMinimo();
                    enviarListaComoTexto(out, lista);
                }

                case "RELATORIO_QTD_POR_CATEGORIA", "RELATORIO_QTD_CAT" -> {
                    List<Map<String, Object>> lista = relatorioService.quantidadePorCategoria();
                    enviarListaComoTexto(out, lista);
                }

                case "RELATORIO_MAIS_MOVIMENTADO", "RELATORIO_MAIS_MOV" -> {
                    List<Map<String, Object>> lista = relatorioService.produtoMaisMovimentado();
                    enviarListaComoTexto(out, lista);
                }

                // ===============================================================
                // ---------------------- COMANDO INVÁLIDO ------------------------
                // ===============================================================
                default -> {
                    out.writeUTF("ERRO: comando desconhecido");
                    out.flush();
                    System.err.println("❌ Comando desconhecido recebido: " + comando);
                }
            }

        } catch (Exception e) {
            System.err.println("💥 Erro ao atender cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // ===============================================================
            // ------------------ FECHAMENTO DE CONEXÃO ------------------------
            // ===============================================================
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
                System.out.println("🔒 Conexão encerrada com o cliente.\n");
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Método auxiliar para converter Map<Object,Object> em texto e enviar via socket.
     */
    private static void enviarListaComoTexto(ObjectOutputStream out, List<Map<String, Object>> lista) throws IOException {
        out.writeObject(lista.stream()
                .map(map -> map.entrySet().stream()
                        .collect(java.util.stream.Collectors.toMap(
                                Map.Entry::getKey,
                                e -> (e.getValue() != null ? e.getValue().toString() : "")
                        )))
                .toList());
        out.flush();
        System.out.println("📊 Relatório enviado com sucesso! Total de registros: " + lista.size());
    }
}
