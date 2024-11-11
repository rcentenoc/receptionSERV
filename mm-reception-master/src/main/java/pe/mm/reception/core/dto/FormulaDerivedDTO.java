package pe.mm.reception.core.dto;

import pe.mm.reception.common.ValidationException;

public class FormulaDerivedDTO {

    /**
     * El tipo de formula:
     * - 1: Operación. Operación entre valores de var1 y var2 del mismo tiempo
     * - 2: Acumulativa. Operación entre el valor de un tiempo anteriror de va1 y el tiempo actual de var2
     * - 3: Condicional. Dependiendo de la comparción se realiza la acción del ifStament
     * - 4: Operación con índice: Operación entre el valor de una variable y el índice de var2
     */
    private int type;
    /**
     * Operación que se realiza:
     * - 1: Suma el valor de la variable var1 y el valor de la variable var2
     * - 2: Resta el valor de la variable var1 y el valor de la variable var2
     * - 3: Multiplica el valor de la variable var1 y el valor de la variable var2
     * - 4: Divide el valor de la variable var1 y el valor de la variable var2
     * - 5: Si el valor de la variable var1 es menor que el valor de la variable var2
     * - 6: Si el valor de la variable var1 es mayor que el valor de la variable var2
     * - 7: Si el valor de la variable var1 es igual que el valor de la variable var2
     * - 8: Si el valor de la variable var1 es menor igual que el valor de la variable var2
     * - 9: Si el valor de la variable var1 es mayor igual que el valor de la variable var2
     * - 10: Si el valor de la variable var1 es diferente que el valor de la variable var2
     */
    private int operation;
    /**
     * Primera variable a considerar en la operación
     */
    private VariableEssentialDTO var1;
    /**
     * Segunda variable a considarar en la operación.
     * Si type es 4 (Operación con índice) es null
     */
    private VariableEssentialDTO var2;
    /**
     * Si type es 4:
     * - 1: velociadad nominal del producto
     * - 2: factor de conversion
     * de lo constrario es 0
     */
    private int option;

    /**
     * Es null si type no es 3
     */
    private ifStament IF;

    /**
     * Parametros para una formula de tipo condicional.
     */
    public static class ifStament {
        /**
         * La acción que se va a relizar dependiendo si la operación de comparación es verdadera:
         * 1: Suma entre var1 y var2
         * 2: Resta entre var1 y var2
         * 3: Multiplicación entre var1 y var2
         * 4: Disivión entre var1 y var2
         * 11: Retorna value
         * 12: Retorna el valor de la variable
         */
        private int then;
        /**
         * La acción que se va a relizar dependiendo si la operación de comparación es falsa:
         * 1: Suma entre var1 y var2
         * 2: Resta entre var1 y var2
         * 3: Multiplicación entre var1 y var2
         * 4: Disivión entre var1 y var2
         * 11: Retorna value
         * 12: Retorna Retorna el valor de la variable
         */
        private int ELSE;
        /**
         * Es un valor fijo
         */
        private Double value;
        /**
         * Es una variable
         */
        private VariableEssentialDTO variable;

        public ifStament() {
            value = new Double(0);
            variable = new VariableEssentialDTO();
            variable.setId(0);
        }

        public int getThen() {
            return then;
        }

        public void setThen(int then) {
            this.then = then;
        }

        public int getELSE() {
            return ELSE;
        }

        public void setELSE(int ELSE) {
            this.ELSE = ELSE;
        }

        public VariableEssentialDTO getVariable() {
            return variable;
        }

        public void setVariable(VariableEssentialDTO variable) {
            this.variable = variable;
        }

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }
    }

    public FormulaDerivedDTO() {
    }

    public FormulaDerivedDTO(String formula) {
        String[] strings = formula.split(",");
        if (strings.length == 0) throw new ValidationException("Formula vacía");
        type = Integer.parseInt(strings[0]);
        if (type == 1 || type == 2 || type == 3 || type == 4) {
            if ((type == 1 || type == 2 || type == 4) && strings.length != 4)
                throw new ValidationException("Formula incorrecta");
            if (type == 3 && strings.length != 8)
                throw new ValidationException("Formula incorrecta");
            var1 = new VariableEssentialDTO();
            var1.setId(Integer.parseInt(strings[1]));
            setOperation(Integer.parseInt(strings[2]));
            if ((type == 1 || type == 2 || type == 4)) {
                if (!isOperation(operation))
                    throw new ValidationException("Operador incorrecto");
                if (type == 4) {
                    option =Integer.parseInt(strings[3]);
                    if (!(option == 1 || option == 2))//1 velocidad nominal, 2 factor de conversion
                        throw new ValidationException("Caracterítica Incorrecta");
                } else {
                    var2 = new VariableEssentialDTO();
                    var2.setId(Integer.parseInt(strings[3]));
                }
            } else if (type == 3) {
                var2 = new VariableEssentialDTO();
                var2.setId(Integer.parseInt(strings[3]));
                if (!isComparation(operation))
                    throw new ValidationException("Operador incorrecto");
                IF = new ifStament();
                IF.setThen(Integer.parseInt(strings[4]));
                IF.setELSE(Integer.parseInt(strings[5]));
                IF.setValue(Double.parseDouble(strings[6]));
                IF.setVariable(new VariableEssentialDTO());
                IF.getVariable().setId(Integer.parseInt(strings[7]));
                if (IF.getThen()== IF.getELSE()||
                        !(isOperation(IF.getThen()) || isValue(IF.getThen()) || isVariable(IF.getThen())) ||
                        !(isOperation(IF.getELSE()) || isValue(IF.getELSE()) || isVariable(IF.getELSE())) )
                    throw new ValidationException("Operador incorrecto");
            }
        } else {
            throw new ValidationException("El tipo de la ratio es incorrecto");
        }

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public VariableEssentialDTO getVar1() {
        return var1;
    }

    public void setVar1(VariableEssentialDTO var1) {
        this.var1 = var1;
    }

    public VariableEssentialDTO getVar2() {
        return var2;
    }

    public void setVar2(VariableEssentialDTO var2) {
        this.var2 = var2;
    }

    public ifStament getIF() {
        return IF;
    }

    public void setIF(ifStament IF) {
        this.IF = IF;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    /**
     * En caso de que type
     * - 1: type + id de var1 + operation + id de var2
     * - 2: type + id de var1 + operation + id de var2
     * - 3: type + id de var1 + operation + id de var2 + operación de comparación verdarera + operación de comparación false + value + id de variable de condicional
     * - 4: type + id de var1 + operation + option
     * @return formula en formato String
     */
    @Override
    public String toString() {
        String string = type+",";
        if (type == 3) {
            if(var1!=null && var2!=null && IF !=null)
                string += var1.getId() + "," + operation + "," + var2.getId() + "," + IF.getThen() + "," + IF.getELSE() + "," + IF.getValue()+","+IF.getVariable().getId();
            else
                throw new ValidationException("Error de formato en la formula");
        } else if (type == 2 || type == 1) {
            if(var1!=null && var2!=null)
                string += var1.getId() + "," + operation + "," + var2.getId();
            else
                throw new ValidationException("Error de formato en la formula");
        } else if (type==4){
            if(var1!=null)
                string += var1.getId() + "," + operation + "," + option;
            else
                throw new ValidationException("Error de formato en la formula");
        }
        return string;
    }

    private boolean isOperation(int operation) {
        return operation > 0 && operation < 5;
    }

    private boolean isComparation(int operation) {
        return operation > 4 && operation < 11;
    }

    private boolean isValue(int operation) {
        return operation == 11;
    }

    private boolean isVariable(int operation) {
        return operation == 12;
    }
}
