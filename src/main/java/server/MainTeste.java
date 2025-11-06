package server;

import service.CategoriaService;
import service.ProdutoService;
import service.MovimentacaoService;
import service.RelatorioService;
import model.Categoria;
import model.Produto;
import model.EmbalagemProduto;
import model.TamanhoProduto;

public class MainTeste {
    public static void main(String[] args) {

        CategoriaService categoriaService = new CategoriaService();
        ProdutoService produtoService = new ProdutoService();
        MovimentacaoService movimentacaoService = new MovimentacaoService();
        RelatorioService relatorioService = new RelatorioService();

        System.out.println("==== TESTE DO SISTEMA DE ESTOQUE ====\n");

        // 1️⃣ Criar categoria
        Categoria c = new Categoria();
        c.setNome("Bebidas");
        c.setEmbalagem(EmbalagemProduto.LATA);
        c.setTamanho(TamanhoProduto.MEDIO);

        System.out.println("-> Inserindo Categoria...");
        categoriaService.salvar(c);

        // 2️⃣ Criar produto
        Produto p = new Produto();
        p.setNome("Coca-Cola");
        p.setUnidade("UN");
        p.setPrecoUnitario(5.50);
        p.setQuantidadeEstoque(50);
        p.setQuantidadeMinima(10);
        p.setQuantidadeMaxima(200);
        p.setCategoria(c);

        System.out.println("-> Inserindo Produto...");
        produtoService.inserir(p);

        // 3️⃣ Registrar movimentações
        System.out.println("-> Registrando entrada de 20 unidades...");
        System.out.println(movimentacaoService.registrarEstoque(p.getId(), 20, "ENTRADA", null));

        System.out.println("-> Registrando saída de 5 unidades...");
        System.out.println(movimentacaoService.registrarEstoque(p.getId(), 5, "SAIDA", null));

        // 4️⃣ Gerar relatório
        System.out.println("\n\n===== RELATÓRIO GERAL =====\n");
        String relatorio = relatorioService.gerarRelatorioCompleto();
        System.out.println(relatorio);
    }
}
