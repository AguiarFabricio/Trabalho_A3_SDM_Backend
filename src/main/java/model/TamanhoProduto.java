package model;

/**
 *
 * @author mario
 */
public enum TamanhoProduto {
    PEQUENO, MEDIO, GRANDE;

    @Override
    public String toString() {
        switch (this) {
            case PEQUENO:
                return "Pequeno";
            case MEDIO:
                return "Médio";
            case GRANDE:
                return "Grande";
            default:
                return super.toString();
        }
    }
}
