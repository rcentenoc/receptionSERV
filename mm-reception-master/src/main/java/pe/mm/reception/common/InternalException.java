package pe.mm.reception.common;

import pe.mm.reception.common.message.InternalErrorMessage;

public class InternalException  extends SigerException {

    public InternalException(String message){
        super("Internal Error");
        setMsg(new InternalErrorMessage(message));
    }
}