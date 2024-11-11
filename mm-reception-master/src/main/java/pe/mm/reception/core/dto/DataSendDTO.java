package pe.mm.reception.core.dto;

import java.util.Date;
import java.util.List;

public class DataSendDTO {
    /**
     * Nombre código del archivo
     */
    private String code;
    /**
     * Fecha de los datos envíados
     */
    private Date time;
    /**
     * Cabeceras o columnas
     */
    private List<String> head;
    /**
     * Lista de valores
     */
    private List<String> data;
    public DataSendDTO() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public List<String> getHead() {
        return head;
    }

    public void setHead(List<String> head) {
        this.head = head;
    }
}

