package pe.mm.reception.common;

import pe.mm.reception.common.message.Message;

public class SigerException extends RuntimeException {
    protected Message msg;

    public SigerException(String message){
        super(message);
    }

    public SigerException(Exception ex){
        super(ex);
    }
    public Message getMsg() {
        return msg;
    }

    public void setMsg(Message msg) {
        this.msg = msg;
    }


}