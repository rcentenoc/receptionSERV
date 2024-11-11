package pe.mm.reception.core.model;

import java.io.Serializable;

public class MemoryStatus implements Serializable {
    private boolean isProcessing;
    private String fileName;
    public  MemoryStatus(){};

    public Boolean getProcessing() {
        return isProcessing;
    }

    public void setProcessing(Boolean processing) {
        isProcessing = processing;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
