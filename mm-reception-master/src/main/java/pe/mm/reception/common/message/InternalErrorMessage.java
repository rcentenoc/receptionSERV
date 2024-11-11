package pe.mm.reception.common.message;

public class InternalErrorMessage extends Message {
    public InternalErrorMessage(String message) {
        super("Internal Error", message);
    }
}
