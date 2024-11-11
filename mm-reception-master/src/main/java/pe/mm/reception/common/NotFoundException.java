package pe.mm.reception.common;

import pe.mm.reception.common.message.ErrorMessage;

public class NotFoundException extends SigerException {

    public NotFoundException(int... ids){
        super("Objeto no encontrado");
        String message = "No se encontró el ";
        for (int id : ids) {
            message += "id: "+id+",";
        }
        message = message.substring(0,message.length()-1);
        setMsg(new ErrorMessage(message));
    }
}
