package model;

/**
 *
 * @author mario
 */
public class Categoria {
    
    private int id;
    private String nome;   
    private EmbalagemProduto embalagem;         
    private TamanhoProduto tamanho;

    public Categoria() {
    }

    public Categoria(int id, String nome, EmbalagemProduto embalagem, TamanhoProduto tamanho) {
        this.id = id;
        this.nome = nome;
        this.embalagem = embalagem;
        this.tamanho = tamanho;
    }
    
    public Categoria(int idCategoria, String nomeCategoria) {
        this.id = idCategoria;
        this.nome = nomeCategoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public EmbalagemProduto getEmbalagem() {
        return embalagem;
    }

    public void setEmbalagem(EmbalagemProduto embalagem) {
        this.embalagem = embalagem;
    }

    public TamanhoProduto getTamanho() {
        return tamanho;
    }

    public void setTamanho(TamanhoProduto tamanho) {
        this.tamanho = tamanho;
    }
    
    @Override
    public String toString() {
        return id + " / " + nome;
    }
}
