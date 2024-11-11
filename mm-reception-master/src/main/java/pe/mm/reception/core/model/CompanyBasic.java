package pe.mm.reception.core.model;

import java.io.Serializable;

public class CompanyBasic implements Serializable {
    private  int id;
    private String denomination;
    private int status;

    public CompanyBasic(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
