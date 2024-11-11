package pe.mm.reception.core.dto;

public class UserCompanyUpdateStatusDTO {
    private int id;
    private int status;

    public UserCompanyUpdateStatusDTO() {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
