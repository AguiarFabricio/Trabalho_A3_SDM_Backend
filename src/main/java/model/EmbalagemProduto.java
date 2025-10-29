package model;

/**
 *
 * @author mario
 */
public enum EmbalagemProduto {
    VIDRO, PLASTICO, LATA;
    
    @Override
    public String toString() {
        switch (this) {
            case VIDRO:
                return "Vidro";
            case PLASTICO:
                return "Plástico";
            case LATA:
                return "Lata";
            default:
                return super.toString();
        }
    }
}
