package pe.mm.reception.common;

import net.sf.oval.ConstraintViolation;
import pe.mm.reception.common.message.ErrorMessage;

import java.util.List;

public class ValidationException extends SigerException {
    public ValidationException(String message){
        super("Error");
        setMsg(new ErrorMessage(getMessage()+": "+message));
    }

    public ValidationException(Exception message){
        super(message);
    }

    public ValidationException(List<ConstraintViolation> violations) {
        super("Se encontró "+violations.size()+ " error" +(violations.size()>1?"es":"") ) ;
        String msg = "";
        for (int i=0;i<violations.size();i++){
            msg += violations.get(i).getMessage() + ", ";
        }
        msg = msg.substring(0,msg.length()-2);
        setMsg(new ErrorMessage(getMessage()+": "+msg));
    }
}