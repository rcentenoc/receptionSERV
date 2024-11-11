package pe.mm.reception.common;

import java.util.Date;

/**
 * Created by César Calle on 16/08/2016.
 */
public class ValidationRaspberryException extends ValidationException{
    private String code;
    private Date time;

    public ValidationRaspberryException(String message, String code, Date time){
        super(code+ "("+time.getTime()+") "+message );
        this.code = code;
        this.time = time;
    }

    public ValidationRaspberryException(Exception message, String code, Date time){
        super(message);
        this.code = code;
        this.time = time;
    }

    public String getCode(){
        return  code;
    }

    public Date getTime(){
        return time;
    }
}
