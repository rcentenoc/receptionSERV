package pe.mm.reception.core.service;

import org.springframework.stereotype.Service;

@Service
public class VariableDerivedUtilService {
    public boolean comparar(double value1, int operation, double value2) {
        switch (operation) {
            case 5:
                return value1 < value2;
            case 6:
                return value1 > value2;
            case 7:
                return value1 == value2;
            case 8:
                return value1 <= value2;
            case 9:
                return value1 >= value2;
            case 10:
                return value1 != value2;
        }
        return false;
    }

    public Double operar(Double value1, int operation, Double value2) {
        switch (operation) {
            case 1:
                return value1 + value2;
            case 2:
                return value1 - value2;
            case 3:
                return value1 * value2;
            case 4:
                if (value2 != 0) return value1 / value2;
        }
        return new Double(0);
    }

    public String operarInSQL(String value1, int operation, String value2) {
        switch (operation) {
            case 1:
                return "ROUND("+CaseNull(value1) +" + "+ CaseNull(value2)+",4)";
            case 2:
                return "ROUND("+CaseNull(value1) +" - "+ CaseNull(value2)+",4)";
            case 3:
                return "ROUND("+CaseNull(value1) +" * "+ CaseNull(value2)+",4)";
            case 4:
                return "case when "+ value2 +" = 0 then 0 ELSE ROUND("+value1 + " div "+ value2+",4) END";
        }
        return " 0 ";

    }

    private String CaseNull (String value){
        return "(case when "+ value +" is null then 0 ELSE "+value+" END)";
    }


    public String compararInSQL(String value1, int operation, String value2, String then, String els) {
        switch (operation) {
            case 5:
                return "case when "+value1+ " < "+ value2+ " then "+then+" else "+ els+ " end";
            case 6:
                return "case when "+value1+ " > "+ value2+ " then "+then+" else "+ els+ " end";
            case 7:
                return "case when "+value1+ " == "+ value2+ " then "+then+" else "+ els+ " end";
            case 8:
                return "case when "+value1+ " <= "+ value2+ " then "+then+" else "+ els+ " end";
            case 9:
                return "case when "+value1+ " >= "+ value2+ " then "+then+" else "+ els+ " end";
            case 10:
                return "case when "+value1+ " <> "+ value2+ " then "+then+" else "+ els+ " end";
            default:
                throw new RuntimeException("Comparación invalida");
        }


    }

    public String conditionalResultSQL(int resultContion, int asValue, String asColumnSQL1, String asColumnSQL2) {
        if (resultContion == 11) {//valor
            return asValue+"";
        } else if (resultContion == 12) {//valor de variable
            return asColumnSQL1;
        } else {
            return operarInSQL(asColumnSQL1,resultContion, asColumnSQL2);
        }
    }

    public String getIndexByOption(int option) {
        String index = null;;
        if (option == 1)//velociadad nominarl
            index = "rated_speed";
        else if (option == 2)//factor de conversion;
            index = "measure_per_unit";
        return index;
    }
}
