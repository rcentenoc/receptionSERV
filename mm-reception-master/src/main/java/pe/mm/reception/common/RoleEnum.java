package pe.mm.reception.common;



/**
 * Created by admin1 on 04/11/16.
 */
public enum RoleEnum {
        ROLE_PLANT_ADMIN(1),
        ROLE_PLANT_ANALISTA(3),
        ROLE_ROOT(4),
        ROLE_OPERARIO(5),
        ROLE_CORP_ANALISTA(6),
        ROLE_CORP_COPORATIVO(7);

    private int code;

    RoleEnum(int code){
        this.code= code;
    }

    public int getCode() {
        return code;
    }


}


