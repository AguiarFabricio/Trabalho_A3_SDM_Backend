package model;

import java.io.Serializable;

/**
 * Representa um produto cadastrado no sistema de controle de estoque.
 * <p>
 * Cada produto possui informações sobre preço, unidade de medida, quantidade em
 * estoque, limites mínimos e máximos, além da categoria à qual pertence.
 * </p>
 *
 * <p>
 * Esta classe implementa {@link Serializable}, permitindo que objetos sejam
 * transmitidos ou gravados de forma persistente.
 * </p>
 *
 * @author Luiz
 * @version 1.0
 * @see model.Categoria
 */
public class Produto implements Serializable {

    /** Identificador de versão da classe para serialização. */
    private static final long serialVersionUID = 1L;

    /** Identificador único do produto. */
    private int id;

    /** Nome descritivo do produto. */
    private String nome;

    /** Preço unitário do produto. */
    private double preco;

    /** Tipo de unidade de medida (ex.: "kg", "un", "L"). */
    private String tipoUnidade;

    /** Quantidade atual disponível em estoque. */
    private int quantidadeAtual;

    /** Quantidade mínima recomendada para manter em estoque. */
    private int quantidadeMinima;

    /** Quantidade máxima recomendada para manter em estoque. */
    private int quantidadeMaxima;

    /** Categoria à qual o produto pertence. */
    private Categoria categoria;

    /**
     * Construtor padrão sem parâmetros.
     * <p>
     * Necessário para compatibilidade com frameworks e bibliotecas de
     * persistência.
     * </p>
     */
    public Produto() {
    }

    /**
     * Construtor completo que inicializa todos os atributos do produto.
     *
     * @param id identificador único do produto
     * @param nome nome do produto
     * @param preco preço unitário do produto
     * @param tipoUnidade tipo de unidade de medida (ex.: "kg", "un", "L")
     * @param quantidadeAtual quantidade atual em estoque
     * @param quantidadeMinima quantidade mínima recomendada
     * @param quantidadeMaxima quantidade máxima recomendada
     * @param categoria categoria associada ao produto
     */
    public Produto(int id, String nome, double preco, String tipoUnidade,
                   int quantidadeAtual, int quantidadeMinima, int quantidadeMaxima, Categoria categoria) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.tipoUnidade = tipoUnidade;
        this.quantidadeAtual = quantidadeAtual;
        this.quantidadeMinima = quantidadeMinima;
        this.quantidadeMaxima = quantidadeMaxima;
        this.categoria = categoria;
    }

    /**
     * Retorna o identificador único do produto.
     *
     * @return identificador numérico do produto
     */
    public int getId() {
        return id;
    }

    /**
     * Define o identificador único do produto.
     *
     * @param id identificador numérico do produto
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retorna o nome do produto.
     *
     * @return nome do produto
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do produto.
     *
     * @param nome nome do produto
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o preço unitário do produto.
     *
     * @return preço do produto
     */
    public double getPreco() {
        return preco;
    }

    /**
     * Define o preço unitário do produto.
     *
     * @param preco valor do produto
     */
    public void setPreco(double preco) {
        this.preco = preco;
    }

    /**
     * Retorna o tipo de unidade de medida do produto (ex.: "kg", "un", "L").
     *
     * @return tipo da unidade
     */
    public String getTipoUnidade() {
        return tipoUnidade;
    }

    /**
     * Define o tipo de unidade de medida do produto.
     *
     * @param tipoUnidade tipo da unidade (ex.: "kg", "un", "L")
     */
    public void setTipoUnidade(String tipoUnidade) {
        this.tipoUnidade = tipoUnidade;
    }

    /**
     * Retorna a quantidade atual em estoque.
     *
     * @return quantidade atual do produto
     */
    public int getQuantidadeAtual() {
        return quantidadeAtual;
    }

    /**
     * Define a quantidade atual em estoque.
     *
     * @param quantidadeAtual quantidade atual do produto
     */
    public void setQuantidadeAtual(int quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }

    /**
     * Retorna a quantidade mínima recomendada em estoque.
     *
     * @return quantidade mínima
     */
    public int getQuantidadeMinima() {
        return quantidadeMinima;
    }

    /**
     * Define a quantidade mínima recomendada em estoque.
     *
     * @param quantidadeMinima quantidade mínima
     */
    public void setQuantidadeMinima(int quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }

    /**
     * Retorna a quantidade máxima recomendada em estoque.
     *
     * @return quantidade máxima
     */
    public int getQuantidadeMaxima() {
        return quantidadeMaxima;
    }

    /**
     * Define a quantidade máxima recomendada em estoque.
     *
     * @param quantidadeMaxima quantidade máxima
     */
    public void setQuantidadeMaxima(int quantidadeMaxima) {
        this.quantidadeMaxima = quantidadeMaxima;
    }

    /**
     * Retorna a categoria associada ao produto.
     *
     * @return instância de {@link Categoria}
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * Define a categoria associada ao produto.
     *
     * @param categoria categoria do produto
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * Retorna uma representação textual do produto, exibindo nome e tipo de unidade.
     *
     * @return representação textual resumida do produto
     */
    @Override
    public String toString() {
        return nome + " (" + tipoUnidade + ")";
    }
}
