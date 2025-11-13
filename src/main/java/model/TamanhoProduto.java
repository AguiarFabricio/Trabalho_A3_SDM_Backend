package model;

/**
 * Enumeração que representa os diferentes tamanhos disponíveis para um produto.
 * <p>
 * Este enum é utilizado nas classes {@link model.Categoria} e {@link model.Produto}
 * para definir o porte físico ou a capacidade do produto, de forma padronizada.
 * </p>
 *
 * <p>
 * Os tamanhos definidos são:
 * <ul>
 *     <li>{@link #PEQUENO} — Produto de tamanho ou volume reduzido.</li>
 *     <li>{@link #MEDIO} — Produto de tamanho intermediário.</li>
 *     <li>{@link #GRANDE} — Produto de grande porte ou volume.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Essa enumeração facilita a categorização e o controle de estoque, permitindo
 * filtros e relatórios baseados no porte do produto.
 * </p>
 *
 * @author Luiz
 * @version 1.0
 */
public enum TamanhoProduto {

    /** Produto de tamanho pequeno. */
    PEQUENO,

    /** Produto de tamanho médio. */
    MEDIO,

    /** Produto de tamanho grande. */
    GRANDE
}
