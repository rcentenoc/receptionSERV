package pe.mm.reception.core.dto;

public class WhereStamen {
    /**
     * campo a comparar
     */
    private String field;
    /**
     * operación
     */
    private Comparative operator;
    /**
     * conector
     */
    private LogicalOperator logical;
    /**
     * separator
     */
    private Separator separator;
    /**
     * valor para comparar
     */
    private Object value;
    /**
     * valor para comparar
     */
    private boolean valueIsList;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Comparative getOperator() {
        return operator;
    }

    public void setOperator(Comparative operator) {
        this.operator = operator;
    }

    public LogicalOperator getLogical() {
        return logical;
    }

    public void setLogical(LogicalOperator logical) {
        this.logical = logical;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Separator getSeparator() {
        return separator;
    }

    public void setSeparator(Separator separator) {
        this.separator = separator;
    }

    public boolean isValueIsList() {
        return valueIsList;
    }

    public void setValueIsList(boolean valueIsList) {
        this.valueIsList = valueIsList;
    }

    public WhereStamen() {
        logical = LogicalOperator.Default;
        valueIsList =  false;
    }

    /**
     * Comparación: =,!=,<,>,<=,>=,in,isNull
     */
    public enum Comparative {
        Equal("="),
        NotEqual("!="),
        LessThan("<"),
        GreaterThan(">"),
        LessThanOrEqual("<="),
        GreaterThanOrEqual(">="),
        InList("in");

        public String getOperator() {
            return operator;
        }

        private String operator;

        Comparative(String operator) {
            this.operator = operator;
        }
    }

    /**
     * conector: and y or
     */
    public enum LogicalOperator {
        Default(""),
        And("and"),
        Or("or");

        public String getOperator() {
            return operator;
        }

        private final String operator;

        LogicalOperator(String operator) {
            this.operator = operator;
        }
    }

    /**
     * separator: ( y )
     */
    public enum Separator {
        Default(""),
        Open("("),
        Close(")");

        public String getSeparator() {
            return separator;
        }

        private final String separator;

        Separator(String separator) {
            this.separator = separator;
        }
    }
}
