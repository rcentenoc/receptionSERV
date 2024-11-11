package pe.mm.reception.common.message;

public class SuccessMessage extends Message {
    public SuccessMessage(String message) {
        super("Success", message);
    }
}
