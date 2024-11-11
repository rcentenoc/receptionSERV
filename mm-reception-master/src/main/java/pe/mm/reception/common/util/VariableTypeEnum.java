package pe.mm.reception.common.util;

public enum VariableTypeEnum {
    ENERGIA("CE100"),
    PRODUCCION("PR100"),
    OTROS("OT100"),
    RATIOS("RA100"),
    CONTEO_UNITARIO("CONT"),
    T_DISPONIBLE("TDISPONIBLE"),
    T_PARADA_NO_ASIGNADA("TPARADANOASIG"),
    T_MICROPARADA("TMICROPARADA"),
    T_PARADA_OBLIGATORIA("TPARADOBLIG"),
    T_PARADA_NO_PROGRAMADA("TPARADANOPROG"),
    T_PARADA_PROGRAMADA("TPARADAPROG");

    private String code;

    VariableTypeEnum(String code){
        this.code= code;
    }

    public String getCode() {
        return code;
    }



}
