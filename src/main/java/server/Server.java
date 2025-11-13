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
 * Classe {@code Server} responsável por gerenciar as conexões de clientes
 * e processar os comandos recebidos relacionados a {@link Categoria},
 * {@link Produto}, {@link Movimentacao} e relatórios de estoque.
 * <p>
 * Este servidor utiliza comunicação via {@link Socket} e opera na porta
 * {@value #PORTA}. Cada cliente conectado é atendido por uma <b>thread</b>
 * separada, garantindo processamento paralelo e não bloqueante.
 * </p>
 *
 * <p><b>Principais funcionalidades:</b></p>
 * <ul>
 *     <li>Gerenciamento de categorias (CRUD)</li>
 *     <li>Gerenciamento de produtos (CRUD)</li>
 *     <li>Registro e listagem de movimentações de estoque</li>
 *     <li>Geração de relatórios de controle e análise</li>
 * </ul>
 *
 * <p>O servidor se comunica com os serviços da camada {@code service}
 * e utiliza os DAOs para persistência no banco de dados.</p>
 *
 * <p>Exemplo de inicialização:</p>
 * <pre>{@code
 *     java server.Server
 * }</pre>
 *
 * @author Luiz
 * @version 1.0
 * @since 2025
 */
public class Server {

    /** Porta fixa onde o servidor ficará escutando as conexões dos clientes. */
    private static final int PORTA = 1234;

    /**
     * Método principal responsável por inicializar o servidor e aceitar conexões.
     * <p>
     * Cada nova conexão de cliente é tratada em uma thread independente.
     * </p>
     *
     * @param args argumentos de inicialização (não utilizados).
     */
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORTA)) {
            System.out.println("✅ Servidor iniciado na porta " + PORTA);

            // Aceita conexões indefinidamente
            while (true) {
                Socket cliente = server.accept();
                System.out.println("🔗 Cliente conectado: " + cliente.getInetAddress());

                // Cria uma nova thread para atender o cliente
                new Thread(() -> atenderCliente(cliente)).start();
            }

        } catch (IOException e) {
            System.err.println("💥 Erro ao iniciar o servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Atende um cliente específico conectado ao servidor.
     * <p>
     * Este método é executado dentro de uma thread separada para cada cliente,
     * garantindo concorrência e isolamento entre as conexões.
     * </p>
     *
     * <p>Responsável por:</p>
     * <ul>
     *     <li>Ler o comando enviado pelo cliente</li>
     *     <li>Executar a ação correspondente (via camada service ou DAO)</li>
     *     <li>Enviar a resposta de volta ao cliente</li>
     * </ul>
     *
     * @param socket o {@link Socket} de comunicação com o cliente.
     */
    private static void atenderCliente(Socket socket) {
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            // Instancia os serviços necessários
            CategoriaService categoriaService = new CategoriaService();
            ProdutoService produtoService = new ProdutoService();
            RelatorioService relatorioService = new RelatorioService();

            // Lê o comando textual enviado pelo cliente
            String comando = in.readUTF();
            System.out.println("📥 Comando recebido: " + comando);

            // ===================================================================
            //                  INTERPRETAÇÃO E EXECUÇÃO DOS COMANDOS
            // ===================================================================
            switch (comando) {

                // ===============================================================
                // ------------------------- CATEGORIAS --------------------------
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
                // --------------------------- PRODUTOS --------------------------
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
                // ------------------------ MOVIMENTAÇÕES ------------------------
                // ===============================================================
                case "INSERIR_MOVIMENTACAO" -> {
                    try {
                        Movimentacao movimentacao = (Movimentacao) in.readObject();
                        MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();

                        String resposta = movimentacaoDAO.inserir(movimentacao);
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
                // -------------------------- RELATÓRIOS -------------------------
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
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
                System.out.println("🔒 Conexão encerrada com o cliente.\n");
            } catch (IOException ignored) { }
        }
    }

    /**
     * Envia uma lista de registros (normalmente de relatórios) convertendo seus
     * valores para texto antes de transmitir ao cliente.
     *
     * @param out   o {@link ObjectOutputStream} usado para enviar dados ao cliente.
     * @param lista a lista de mapas contendo os dados do relatório.
     * @throws IOException se ocorrer erro de I/O durante o envio.
     */
    private static void enviarListaComoTexto(ObjectOutputStream out, List<Map<String, Object>> lista)
            throws IOException {
        out.writeObject(lista.stream()
                .map(map -> map.entrySet().stream()
                        .collect(java.util.stream.Collectors.toMap(
                                Map.Entry::getKey,
                                e -> (e.getValue() != null ? e.getValue().toString() : "")
                        ))).toList());
        out.flush();
        System.out.println("📊 Relatório enviado com sucesso! Total de registros: " + lista.size());
    }
}
