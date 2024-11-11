package pe.mm.reception.core.dto;

import pe.mm.reception.core.model.Line;
import pe.mm.reception.core.model.VariableContinuing;
import pe.mm.reception.core.model.VariableDerived;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MemoryVariablesLine implements Serializable {
    private Line line;
    /**
     * variables continuas de una línea
     */
    private List<VariableContinuing> continuing;
    /**
     * variables derivadas de una línea
     */
    private List<VariableDerived> derives;
    /**
     * datos de continuas y derivadas
     */
    private Map<Integer, Double> data;

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public List<VariableContinuing> getContinuing() {
        return continuing;
    }

    public void setContinuing(List<VariableContinuing> continuing) {
        this.continuing = continuing;
    }

    public List<VariableDerived> getDerives() {
        return derives;
    }

    public void setDerives(List<VariableDerived> derives) {
        this.derives = derives;
    }

    public Map<Integer, Double> getData() {
        return data;
    }

    public void setData(Map<Integer, Double> data) {
        this.data = data;
    }
}
