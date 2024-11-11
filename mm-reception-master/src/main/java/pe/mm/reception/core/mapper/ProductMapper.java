package pe.mm.reception.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pe.mm.reception.core.dto.ProductFeatureDTO;
import pe.mm.reception.core.dto.ProductPropertyDTO;
import pe.mm.reception.core.model.Product;
import pe.mm.reception.core.model.Value;
import pe.mm.reception.core.model.Variable;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Mapper
public interface ProductMapper {
    List<Product> getAllByLine(@Param("line_id") int idLine,
                               @Param("startIndex") Integer startIndex,
                               @Param("sizePage") Integer sizePage);

    Product getByCharacteristics(@Param("characteristics") Map<Variable, Double> characteristics,
                                 @Param("table") String tableName);

    void insertByFeature(@Param("line_id") int line_id,@Param("variable_id") int variable_id, @Param("user_id") int idUser);
    Product insert(@Param("table") String table,@Param("product") Product product, @Param("user_id") int idUser);
    void update(@Param("table_name") String table, @Param("product") Product product, @Param("user_id") int idUser);
    void createProductForLine(@Param("line_id") int line_id);
    Integer existVariableByLine(@Param("line_id") int line_id,@Param("variable_id") int variable_id);

    List<String> getAllValuesByProduct(@Param("characteristics") List<Variable> characteristics,
                                       @Param("table_name") String table,
                                       @Param("product_id") int productId);

    List<Variable> getAllCharacteristicsByLine(@Param("line_id") int idLine);
    List<Value> getIndexData(@Param("column") String column,
                             @Param("data_table") String data_table,
                             @Param("product_table") String product_table,
                             @Param("variables")List<Variable> variables,
                             @Param("start_date") Date startDate,
                             @Param("end_date") Date endDate);

    List<ProductFeatureDTO> getFeatureByLine (@Param("line_id") int line_id);
    List<ProductFeatureDTO> getFeatureAddByLine (@Param("line_id") int line_id);
    void updateTableByLine(@Param("table_name") String table_name, @Param("column_name") String column_name);
    List<ProductFeatureDTO> getVarContinByLine (@Param("line_id") int line_id);
    void updateBatchByLine(@Param("variable_id") int variable_id, @Param("line_id") int line_id,@Param("user_id") int user_id);
    void insertReg(@Param("table") String table, @Param("columns") String columns, @Param("values") String values );
    void updateRegisterProduct(@Param("table") String table, @Param("product") ProductPropertyDTO product, @Param("ColumnasDynamic") String ColumnasDynamic, @Param("user_id") int user_id);
    //Product getProductFeatureById(@Param("table_name")String table_name,@Param("idProduct")int idProduct);
    //List<Value>nameColumnsTable(@Param("table_name")String table_name);
    Map<String, Locale.Category> getProductFeatureById(@Param("table_name")String table_name, @Param("idProduct")int idProduct);

    void softDelete(@Param("table_name")String tableProductName,@Param("idProduct") int idPropertie,@Param("user_id") int usermod);
    Product getProductDefaultByLine(@Param("table_name") String table_name, @Param("line_id") int line_id);
    Map<String,Locale.Category> getProductFeatureDefaultByLine(@Param("table_name") String table_name, @Param("line_id") int line_id);
}
