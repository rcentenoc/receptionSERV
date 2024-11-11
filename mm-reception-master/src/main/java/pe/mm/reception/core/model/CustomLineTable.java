package pe.mm.reception.core.model;

import pe.mm.reception.common.InternalException;

public class CustomLineTable {
    private static void validData(Line line){
        if(line.getPlant() == null)
            throw new InternalException("La planta dentro de la linea: "+line.getId()+" es null");
        if(line.getPlant().getUserCompany()==null)
            throw new InternalException("La compañia dentro de la linea: "+line.getId()+" es null");
    }

    public static String getCombinationNameFile(Line line, int idFile){
        return getCombinationName(line)+"_f_"+idFile;
    }

    public static String getCombinationName(Line line){
        validData(line);
        return line.getPlant().getUserCompany().getId()+"_"+line.getPlant().getId()+"_"+line.getId();
    }
    public static String getTableName(Line line, int idFile) {
        return "data_"+getCombinationNameFile(line,idFile);
    }

    public static String getTableIndexName(Line line, int idFile) {
        return "data_index_"+getCombinationNameFile(line,idFile);
    }

    public static String getTableDerivedName(Line line, int idFile) {
        return "data_derived_"+getCombinationNameFile(line,idFile);
    }

    public static String getTableProductName(Line line) {return "product_"+getCombinationName(line);}
}
