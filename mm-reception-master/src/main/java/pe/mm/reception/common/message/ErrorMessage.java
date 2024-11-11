package pe.mm.reception.common.message;

public class ErrorMessage extends Message {
    public ErrorMessage(String message) {
        super("Error", message);
    }
}