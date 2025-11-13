package model;

import java.io.Serializable;

/**
 * Representa uma categoria de produtos dentro do sistema de estoque.
 * <p>
 * Cada categoria possui um identificador único, um nome descritivo,
 * e atributos opcionais relacionados à embalagem e ao tamanho dos produtos
 * pertencentes a essa categoria.
 * </p>
 *
 * <p>
 * Esta classe é serializável, permitindo que objetos de {@code Categoria}
 * sejam transmitidos ou armazenados em disco de forma persistente.
 * </p>
 *
 * @author Luiz
 * @version 1.0
 */
public class Categoria implements Serializable {

    /** Identificador único da categoria. */
    private int id;

    /** Nome descritivo da categoria. */
    private String nome;

    /** Tipo de embalagem associado à categoria (por exemplo: CAIXA, PACOTE, UNIDADE). */
    private EmbalagemProduto embalagem;

    /** Tamanho padrão dos produtos pertencentes à categoria (por exemplo: PEQUENO, MÉDIO, GRANDE). */
    private TamanhoProduto tamanho;

    /**
     * Construtor padrão sem parâmetros.
     * <p>
     * Necessário para compatibilidade com frameworks e ferramentas de persistência.
     * </p>
     */
    public Categoria() {
    }

    /**
     * Construtor completo para inicialização de todos os campos.
     *
     * @param id identificador único da categoria
     * @param nome nome da categoria
     * @param embalagem tipo de {@link EmbalagemProduto} associado à categoria
     * @param tamanho tipo de {@link TamanhoProduto} associado à categoria
     */
    public Categoria(int id, String nome, EmbalagemProduto embalagem, TamanhoProduto tamanho) {
        this.id = id;
        this.nome = nome;
        this.embalagem = embalagem;
        this.tamanho = tamanho;
    }

    /**
     * Retorna o identificador único da categoria.
     *
     * @return identificador numérico da categoria
     */
    public int getId() {
        return id;
    }

    /**
     * Define o identificador da categoria.
     *
     * @param id identificador numérico da categoria
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retorna o nome da categoria.
     *
     * @return nome da categoria
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome da categoria.
     *
     * @param nome nome descritivo da categoria
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o tipo de embalagem associado à categoria.
     *
     * @return tipo de {@link EmbalagemProduto} ou {@code null} se não definido
     */
    public EmbalagemProduto getEmbalagem() {
        return embalagem;
    }

    /**
     * Define o tipo de embalagem da categoria.
     *
     * @param embalagem tipo de {@link EmbalagemProduto} associado
     */
    public void setEmbalagem(EmbalagemProduto embalagem) {
        this.embalagem = embalagem;
    }

    /**
     * Retorna o tamanho padrão associado à categoria.
     *
     * @return tipo de {@link TamanhoProduto} ou {@code null} se não definido
     */
    public TamanhoProduto getTamanho() {
        return tamanho;
    }

    /**
     * Define o tamanho padrão da categoria.
     *
     * @param tamanho tipo de {@link TamanhoProduto} associado
     */
    public void setTamanho(TamanhoProduto tamanho) {
        this.tamanho = tamanho;
    }

    /**
     * Retorna uma representação textual da categoria.
     * <p>
     * Este método sobrescreve o comportamento padrão para exibir apenas o nome da categoria,
     * sendo útil em componentes visuais como {@code JComboBox} ou {@code JList}.
     * </p>
     *
     * @return nome da categoria como {@link String}
     */
    @Override
    public String toString() {
        return nome;
    }
}
