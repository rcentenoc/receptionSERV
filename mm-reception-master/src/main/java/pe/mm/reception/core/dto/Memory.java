package pe.mm.reception.core.dto;

import pe.mm.reception.core.model.Files;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class Memory implements DateOfMemory, Serializable {
    /**
     * Fecha de los datos actuales
     */
    private Date time;
    /**
     * Archivo
     */
    private Files file;
    /**
     * <Line id, Memoria de las sus variables>
     */
    private Map<Integer, MemoryVariablesLine> memoryByLine;
    /**
     * <Id de variables continuas, Nombre de columnas>
     */
    private Map<Integer, String> idToColumnName;
    /**
     * true: se esta usando,
     * false: libre
     */
    private boolean processing = false;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    private int totalCount;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Map<Integer, MemoryVariablesLine> getMemoryByLine() {
        return memoryByLine;
    }

    public void setMemoryByLine(Map<Integer, MemoryVariablesLine> memoryByLine) {
        this.memoryByLine = memoryByLine;
    }

    public Files getFile() {
        return file;
    }

    public void setFile(Files file) {
        this.file = file;
    }

    public Map<Integer, String> getIdToColumnName() {
        return idToColumnName;
    }

    public void setIdToColumnName(Map<Integer, String> idToColumnName) {
        this.idToColumnName = idToColumnName;
    }

    public String convertToFormat(String columnName) {
        columnName = columnName.replaceAll(" ", "_");
        return columnName.replaceAll("[^A-Za-z0-9_]", "");
    }

    /**
     * Verifica si esta memoria esta siendo procesada actualmente
     * @return si esta siendo procesada
     */
    public boolean isProcessing() {
        return processing;
    }

    public void setProcessing(boolean processing) {
        this.processing = processing;
    }

    @Override
    public Date getDate() {
        return time;
    }
}
