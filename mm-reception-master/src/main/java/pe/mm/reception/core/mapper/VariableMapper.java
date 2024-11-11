package pe.mm.reception.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import pe.mm.reception.core.model.*;

import java.util.List;
@Repository
@Mapper
public interface VariableMapper {
    List<Variable> getAll(@Param("user_company_id") int idCompany);

    List<Variable> getAllByDevice(@Param("device_id") int idDevice,
                                  @Param("startIndex") Integer startIndex,
                                  @Param("sizePage") Integer sizePage);

    List<Variable> getAllByPlant(@Param("plant_id") int idPlant);

    List<VariableContinuing> getAllContinuingByDevice(@Param("device_id") int idDevice,
                                                      @Param("startIndex") Integer startIndex,
                                                      @Param("sizePage") Integer sizePage);

    List<VariableContinuing> getAllContinuing(@Param("user_company_id") int idCompany);

    List<VariableDerived> getAllDerivedByDevice(@Param("device_id") int idDevice,
                                                @Param("startIndex") Integer startIndex,
                                                @Param("sizePage") Integer sizePage);

    List<VariableDerived> getAllDerived(@Param("user_company_id") int idCompany);

    List<VariableFixed> getAllFixed(@Param("user_company_id") int idCompany);

    List<Variable> getAllByField(@Param("field") String field, @Param("value") String value, @Param("device_id") int idDevice);

    void insert(@Param("variable") Variable variable, @Param("user_id") int idUser);

    void insertContinuing(VariableContinuing variable);

    void insertFixed(VariableFixed variable);

    void insertDerived(VariableDerived variable);

    void update(@Param("variable") Variable variable, @Param("user_id") int idUser);

    void updateDerived(VariableDerived variable);
    void updateDerivedWhitoutFile(VariableDerived variable);

    void updateFixed(VariableFixed variable);
    void updateContinuing(VariableContinuing variable);

    Variable getById(@Param("variable_id") int id);
    Variable getFullById(@Param("variable_id") int id);

    VariableContinuing getContinuingById(@Param("variable_id") int id);

    VariableDerived getDerivedById(@Param("variable_id") int id);

    VariableFixed getFixedById(@Param("variable_id") int id);

    void softDeleted(@Param("variable_id") int idVariable, @Param("deleted") int statusDeleted, @Param("user_id") int idUser);

    List<VariableFixed> getAllFixedByDevice(@Param("device_id") int idDevice,
                                            @Param("startIndex") Integer startIndex,
                                            @Param("sizePage") Integer sizePage);

    List<VariableDerived> getAllDerivedByPlant(@Param("plant_id") int idPlant);

    List<VariableDerived> getAllDerivedByLineFile(@Param("variable_id") int idVariable,
                                                  @Param("file_id") int idFile);
    List<VariableDerived> getAllDerivedByLinewithoutFile(@Param("line_id") int line_id);

    List<VariableContinuing> getAllContinuingByFile(@Param("file") String id);

    Variable getBatchVariable(@Param("line_id") int idLine);

    List<Variable> getAllByPlantLineDerived (@Param("idPlant") int idPlant,
                                             @Param("idLine") int idLine);
    List<Variable> getAllByPlantLineFixed (@Param("idPlant") int idPlant,
                                           @Param("idLine") int idLine);
    List<Variable> getAllByPlantLineContinuing (@Param("idPlant") int idPlant,
                                                @Param("idLine") int idLine);
    List<VariableContinuing>getAllVariablesLegendLineTime(@Param("idPlant") int idPlant,
                                                          @Param("idLine") int idLine);


    List<MemoryForVariablesMixed> getAllDerivedMixByCompany(@Param("user_company_id") int idCompany);

    List<Variable> getAllByPlantCustom (@Param("idPlant") int idPlant,@Param("typeId") int typeId);
    List<GenericClassificationVariable> getAllGenericClassification ();
    List<Variable> getVariablesByTypeFile(@Param("idLine") int idPlant,@Param("codeType") String codeType);

}
