package pe.mm.reception.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.mm.reception.common.ValidationException;
import pe.mm.reception.common.util.VariableTypeEnum;
import pe.mm.reception.core.dto.*;
import pe.mm.reception.core.mapper.*;
import pe.mm.reception.core.model.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class ManagmentDataServiceImp implements ManagmentDataService {

    @Autowired
    BatchMapper batchMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    FilesMapper filesMapper;

    @Autowired
    VariableMapper variableMapper;

    @Autowired
    CalendaryMapper calendaryMapper;

    @Autowired
    TableMapper tableMapper;

    @Autowired
    MemoryRedisService memoryRedisService;

    @Autowired
    VariableDerivedUtilService derivedUtil;

    @Autowired
    GraphicVariableMapper graphicVariableMapper;

    @Autowired
    LegendMapper legendMapper;

    @Autowired
    DataService dataService;//dataservice implement2

    /**
     * Limpia un perido de tiempo, para que no tenga seleccionado un lote, debe no tener producción
     *
     * @param started
     * @param ended
     * @param line
     * @param userId
     */
    @Override
    public void cleanProduction(Date started, Date ended, Line line, Integer userId) {
        //verificar memoria libre y ocupar o terminar
        List<Memory> memories = null;
        try {
            memories = verify_memory_available(line);
            //Verifica que no haya produccion en el rango de tiempo
            List<Variable> variableList = variableMapper.getVariablesByTypeFile(line.getId(),
                    VariableTypeEnum.CONTEO_UNITARIO.getCode());
            if (variableList.size() < 1) {
                throw new ValidationException("No se tiene la variable " + VariableTypeEnum.CONTEO_UNITARIO.name() + " configurada");
            } else {
                Variable variable = variableList.get(0);
                String tableData = "";
                if (variable.getType().equals(Variable.TYPE_CONTINUING)) {
                    VariableContinuing var1 = variableMapper.getContinuingById(variable.getId());
                    Integer f = var1.getIdFile();
                    tableData = CustomLineTable.getTableName(var1.getDevice().getLocation().getLine(), f);
                } else if (variable.getType().equals(Variable.TYPE_DERIVED)) {
                    VariableDerived var = variableMapper.getDerivedById(variable.getId());
                    tableData = var.getTable().getName();
                }
                int idType = 2; //agrupamiento por formato de fecha
                List<Value> valueAcum = graphicVariableMapper.getDataOperation(idType, variable.getId(), started, ended,
                        "sum", "-", tableData, 0);
                if (valueAcum.size() > 0 && valueAcum.get(0).getY() != null) {
                    Double cantidadTotalSumada = Double.parseDouble(valueAcum.get(0).getY());
                    if (cantidadTotalSumada > 0)
                        throw new ValidationException("Se ha encontrado " + cantidadTotalSumada + " productos en el rango de tiempo");
                }
            }


            List<Batch> listOfbatchs = batchMapper.getAllByLineDateRangeWithEndNull(line.getId(), started,
                    ended);
            if (listOfbatchs.size() >= 1) {// No hay ningun lote asignado
                updateBeforeBatch(started, ended, listOfbatchs, userId, line);
            }


            List<Variable> variableUpated = updateIndexAndProductVariablesOfData(null, started, ended, line, null);
            variableUpated = updateDeriveds(started, ended, line, variableUpated);
            variableUpated = updateMixVariables(started, ended, line, variableUpated);

            //actualizar variables de paradas y tiempos
            List<Variable> variableUpated2 = updateTimesAndStops(started, ended, line);
            variableUpated2 = updateDeriveds(started, ended, line, variableUpated2);
            variableUpated2 = updateMixVariables(started, ended, line, variableUpated2);
            List<Variable> finalVariableUpdated = variableUpated;
            variableUpated2.forEach(variable -> {
                if (!finalVariableUpdated.stream().anyMatch(v -> v.getId() == variable.getId())) {
                    finalVariableUpdated.add(variable);
                }
            });
            //actualizar variables de memoria
            updateMemory(started, ended, line, finalVariableUpdated, memories);

            //liberar memoria
            break_free_memory(memories);

        } catch (ValidationException e) {
            //liberar memoria
            break_free_memory(memories);
            throw e;
        } catch (Exception e) {
            break_free_memory(memories);
            e.printStackTrace();
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public BatchDTO createBatch(BatchDTO newBatchDto, Line line, Integer userId) {
        //verificar memoria libre y ocupar o terminar
        List<Memory> memories = null;

        try {
            memories = verify_memory_available(line);
            Batch newBatch = new Batch();
            newBatch.setStarted(newBatchDto.getStarted());
            newBatch.setEnded(newBatchDto.getEnded());
            newBatch.setLine(line);

            String tableProduct = CustomLineTable.getTableProductName(newBatch.getLine());
            Map product = productMapper.getProductFeatureById(tableProduct, newBatchDto.getProductId());
            if (product == null) {
                throw new ValidationException("El producto no existe");
            }
            List<Batch> listOfbatchs = batchMapper.getAllByLineDateRangeWithEndNull(newBatchDto.getLineId(), newBatchDto.getStarted(),
                    newBatchDto.getEnded());
            if (listOfbatchs.size() == 0) {// No hay ningun lote asignado
                batchMapper.insert(newBatch, userId);
            } else {// Hay un lote o mas anteriores
                updateBeforeBatch(newBatch.getStarted(), newBatch.getEnded(), listOfbatchs, userId, line);
                batchMapper.insert(newBatch, userId);
            }
            newBatchDto.setId(newBatch.getId());
            List<Variable> variableUpated = updateIndexAndProductVariablesOfData(newBatch.getId(), newBatch.getStarted(), newBatch.getEnded(), line, product);
            variableUpated = updateDeriveds(newBatch.getStarted(), newBatch.getEnded(), line, variableUpated);
            variableUpated = updateMixVariables(newBatch.getStarted(), newBatch.getEnded(), line, variableUpated);

            //actualizar variabkes de memoria
            updateMemory(newBatchDto.getStarted(), newBatchDto.getEnded(), line, variableUpated, memories);

            //liberar memoria
            break_free_memory(memories);

            return newBatchDto;
        } catch (ValidationException e) {
            //liberar memoria
            break_free_memory(memories);
            throw e;
        } catch (Exception e) {
            break_free_memory(memories);
            throw new ValidationException(e.getMessage());
        }
    }

    private boolean containsVariable(List<Variable> variables, Integer id) {
        return variables.stream().filter(v -> v.getId() == id).count() == 1;
    }

    private List<Variable> updateTimesAndStops(Date started, Date ended, Line line) {
        List<Files> files = filesMapper.getListFilesByLine(line.getId());
        List<Variable> variablesUpdated = new ArrayList<>();
        List<Integer> variableIds = new ArrayList<>();
        variableIds.add(7);
        variableIds.add(10);
        variableIds.add(11);
        variableIds.add(12);
        variableIds.add(8);
        variableIds.add(9);

        for (Files file : files) {
            //obtener nombre de tabla data
            String tableName = CustomLineTable.getTableName(line, file.getId());

            List<WhereStamen> where = new ArrayList<>();

            //limite inferior de fecha
            WhereStamen lowerLimit = new WhereStamen();
            lowerLimit.setField("`create`");
            lowerLimit.setOperator(WhereStamen.Comparative.GreaterThanOrEqual);
            lowerLimit.setValue(started);
            where.add(lowerLimit);

            //limite superior de fecha
            WhereStamen upperLimit = new WhereStamen();
            upperLimit.setField("`create`");
            upperLimit.setOperator(WhereStamen.Comparative.LessThan);
            upperLimit.setValue(ended);
            upperLimit.setLogical(WhereStamen.LogicalOperator.And);
            where.add(upperLimit);

            //obtener id de lineas a actualizar
            List<Map<String, Object>> resutl = tableMapper.getDynamic(Arrays.asList("id"), where, tableName,
                    "id asc", 0);
            if (resutl.size() == 0)
                continue;
            Integer minId = (Integer) resutl.get(0).get("id");
            Integer maxId = (Integer) resutl.get(resutl.size() - 1).get("id");

            System.out.println("fileId: " + file.getId());
            System.out.println("minId: " + minId);
            System.out.println("maxId: " + maxId);

            //limite inferior de id de linea
            lowerLimit.setValue(minId);
            lowerLimit.setField("id");
            //limite superior de id de linea
            upperLimit.setOperator(WhereStamen.Comparative.LessThanOrEqual);
            upperLimit.setValue(maxId);
            upperLimit.setField("id");

            //select de variables a actualizar
            List<WhereStamen> whereData = new ArrayList<>();

            WhereStamen noDeleteds = new WhereStamen();//where de tipos de variables
            noDeleteds.setField("`deleted`");
            noDeleteds.setOperator(WhereStamen.Comparative.Equal);
            noDeleteds.setValue(0);
            whereData.add(noDeleteds);

            WhereStamen thisLine = new WhereStamen();//where de tipos de variables
            thisLine.setField("`line_id`");
            thisLine.setOperator(WhereStamen.Comparative.Equal);
            thisLine.setValue(line.getId());
            thisLine.setLogical(WhereStamen.LogicalOperator.And);
            whereData.add(thisLine);

            WhereStamen variableType = new WhereStamen();//where de tipos de variables
            variableType.setField("`variable_type_id`");
            variableType.setOperator(WhereStamen.Comparative.InList);
            variableType.setValue(variableIds);
            variableType.setValueIsList(true);
            variableType.setLogical(WhereStamen.LogicalOperator.And);
            whereData.add(variableType);

            //select id from variable where variable_type_id in (7, 10, 11, 12, 8, 0) and line_id = line.getId() and deleted = 0;
            List<Map<String, Object>> variables = tableMapper.getDynamic(Arrays.asList("id"), whereData, "variable", "id asc", 0);
            Map mapDerived = new HashMap();
            Map mapContinuing = new HashMap();

            for (Map<String, Object> var : variables) {
                Variable v = variableMapper.getById((int) var.get("id"));
                if (v.getType().equals(Variable.TYPE_CONTINUING)) {
                    VariableContinuing variableContinuing = variableMapper.getContinuingById(v.getId());
                    if (variableContinuing.getIdFile() == file.getId()) {
                        variablesUpdated.add(v);
                        mapContinuing.put(var.get("id"), 0);
                    }
                } else if (v.getType().equals(Variable.TYPE_DERIVED)) {
                    VariableDerived variableDerived = variableMapper.getDerivedById(v.getId());
                    if (variableDerived.getIdFile() == file.getId()) {
                        variablesUpdated.add(v);
                        mapDerived.put(var.get("id"), 0);
                    }
                }

            }
            //where de id para actulizar
            List<WhereStamen> wheresUpdate = new ArrayList<>();
            wheresUpdate.add(lowerLimit);
            wheresUpdate.add(upperLimit);
            if (minId != null && maxId != null) {
                if (mapContinuing.size() > 0)
                    tableMapper.updateByCondition(tableName, mapContinuing, wheresUpdate);

                if (mapDerived.size() > 0) {
                    String tableDerived = CustomLineTable.getTableDerivedName(line, file.getId());
                    tableMapper.updateByCondition(tableDerived, mapDerived, wheresUpdate);
                }

            }

        }
        return variablesUpdated;
    }

    /**
     * Actualiza las derivadas y sus dependientes, en el rango del tiempo del nuevo
     * lote que hayan sido modificadas
     *
     * @param started   inico del rango a acualizar
     * @param ended     fin del rango a actualizar
     * @param line      linea que se actualiza
     * @param variables
     * @return retorna la lista de variables actualizadas unidas con la lista
     * inicial.
     */
    private List<Variable> updateDeriveds(Date started, Date ended, Line line, List<Variable> variables) {
        variables.sort(Comparator.comparingLong(Variable::getId));
        Map<Integer, Double> fixeds = memoryRedisService.getFixedByCompany(line.getPlant().getUserCompany());
        List<Files> files = filesMapper.getListFilesByLine(line.getId());

        for (Files file : files) {
            Memory memory = memoryRedisService.getFileMemory(file.getName());
            for (MemoryVariablesLine memoryVariablesLine : memory.getMemoryByLine().values()) {
                Map<Integer, Double> map = new HashMap<>();
                if (memoryVariablesLine.getDerives().size() == 0)
                    continue;
                String tableNameDerived = memoryVariablesLine.getDerives().get(0).getTable().getName();
                String tableName = CustomLineTable.getTableName(line, file.getId());
                List<WhereStamen> where = new ArrayList<>();

                WhereStamen lowerLimit = new WhereStamen();//crear limite inferior
                lowerLimit.setField("`create`");
                lowerLimit.setOperator(WhereStamen.Comparative.GreaterThanOrEqual);
                lowerLimit.setValue(started);
                where.add(lowerLimit);

                WhereStamen upperLimit = new WhereStamen();
                upperLimit.setField("`create`");
                upperLimit.setOperator(WhereStamen.Comparative.LessThan);
                if (ended != null) {
                    upperLimit.setValue(ended);
                } else {
                    Date lastDataTime = memory.getTime();
                    upperLimit.setValue(lastDataTime);
                    upperLimit.setOperator(WhereStamen.Comparative.LessThanOrEqual);
                }
                upperLimit.setLogical(WhereStamen.LogicalOperator.And);
                where.add(upperLimit);

                List<Map<String, Object>> resutl = tableMapper.getDynamic(Arrays.asList("id"), where, tableName,
                        "id asc", 0);
                if (resutl.size() == 0)
                    continue;
                Integer minId = (Integer) resutl.get(0).get("id");
                Integer maxId = (Integer) resutl.get(resutl.size() - 1).get("id");
                memoryVariablesLine.getDerives().forEach(d -> {
                    VariableDerivedDTO dto = new VariableDerivedDTO(d);
                    FormulaDerivedDTO formula = dto.getFormula();
                    if (formula.getType() == 4 && !containsVariable(variables, d.getId())) {//Operación especial
                        variables.add(d);
                        operarVariable(d, formula, fixeds, minId, maxId, tableName, tableNameDerived);
                    } else if (containsVariable(variables, formula.getVar1().getId())
                            || (formula.getVar2() != null && containsVariable(variables, formula.getVar2().getId()))) {
                        variables.add(d);
                        operarVariable(d, formula, fixeds, minId, maxId, tableName, tableNameDerived);
                    }

                });
            }
        }
        return variables;
    }

    private void operarVariable(VariableDerived derived, FormulaDerivedDTO formula, Map<Integer, Double> fixeds,
                                Integer minId, Integer maxId, String tableName, String tableNameDerived) {
        Variable variable1 = variableMapper.getFullById(formula.getVar1().getId());
        Variable variable2 = null;
        Map<String, String> values = new HashMap<String, String>();
        switch (formula.getType()) {
            case 1:
                variable2 = variableMapper.getFullById(formula.getVar2().getId());
                String valueOperation = "";
                if (variable1.getType().equals(Variable.TYPE_FIXED)) {
                    if (variable2.getType().equals(Variable.TYPE_FIXED)) {
                        valueOperation = derivedUtil.operar(fixeds.get(formula.getVar1().getId()), formula.getOperation(),
                                fixeds.get(formula.getVar2().getId())).toString();
                    } else {
                        valueOperation = derivedUtil.operarInSQL(fixeds.get(formula.getVar1().getId()).toString(),
                                formula.getOperation(), "`" + formula.getVar2().getId() + "`");
                    }
                } else {
                    if (variable2.getType().equals(Variable.TYPE_FIXED)) {
                        valueOperation = derivedUtil.operarInSQL("`" + formula.getVar1().getId() + "`",
                                formula.getOperation(), fixeds.get(formula.getVar2().getId()).toString());
                    } else {
                        valueOperation = derivedUtil.operarInSQL("`" + formula.getVar1().getId() + "`",
                                formula.getOperation(), "`" + formula.getVar2().getId() + "`");
                    }
                }
                values.put(derived.getId() + "", valueOperation);
                tableMapper.updateDerivedOperationValue(tableName, tableNameDerived, values, minId, maxId);
                break;
            case 2:
                variable2 = variableMapper.getFullById(formula.getVar2().getId());
                String valueAcum = "";
                valueAcum = derivedUtil.operarInSQL("a.`" + formula.getVar1().getId() + "`", formula.getOperation(),
                        "b.`" + formula.getVar2().getId() + "`");
                values.put(derived.getId() + "", valueAcum);
                String table1 = variable1.getType().equals(Variable.TYPE_CONTINUING) ? tableName : tableNameDerived;
                String table2 = variable2.getType().equals(Variable.TYPE_CONTINUING) ? tableName : tableNameDerived;
                tableMapper.updateDerivedAcumValue(table1, table2, values, minId, maxId);
                break;
            case 3:
                String valueConditional = derivedUtil.compararInSQL("`" + formula.getVar1().getId() + "`",
                        formula.getOperation(), "`" + formula.getVar2().getId() + "`",
                        derivedUtil.conditionalResultSQL(formula.getIF().getThen(), formula.getVar1().getId(),
                                "`" + formula.getVar1().getId() + "`", "`" + formula.getVar2().getId() + "`"),
                        derivedUtil.conditionalResultSQL(formula.getIF().getELSE(), formula.getVar2().getId(),
                                "`" + formula.getVar2().getId() + "`", "`" + formula.getVar2().getId() + "`"));
                values.put(derived.getId() + "", valueConditional);
                tableMapper.updateDerivedOperationValue(tableName, tableNameDerived, values, minId, maxId);
                break;
            case 4:
                String valueSpecial = derivedUtil.operarInSQL("`" + formula.getVar1().getId() + "`", formula.getOperation(),
                        derivedUtil.getIndexByOption(formula.getOption()));
                String tableIndexName = derived.getTable().getIndex();
                values.put(derived.getId() + "", valueSpecial);
                tableMapper.updateDerivedSpecialValue(tableName, tableNameDerived, tableIndexName, values, minId, maxId);
                break;
            default:
                break;
        }
    }

    private Integer getIdFileOfLine(Line line) {
        Variable tempVariable = variableMapper.getBatchVariable(line.getId());
        if (tempVariable != null) {// Si la data actual de la linea actual tiene asignada una variable de lote
            VariableContinuing batchVariable = variableMapper.getContinuingById(tempVariable.getId());
            return batchVariable.getIdFile();
        } else {
            List<Variable> variableList = variableMapper.getVariablesByTypeFile(line.getId(),
                    VariableTypeEnum.CONTEO_UNITARIO.getCode());
            if (variableList.size() > 0) {
                if (variableList.get(0).getType().equals(Variable.TYPE_CONTINUING)) {
                    return variableMapper.getContinuingById(variableList.get(0).getId()).getIdFile();
                } else if (variableList.get(0).getType().equals(Variable.TYPE_DERIVED)) {
                    return variableMapper.getDerivedById(variableList.get(0).getId()).getIdFile();
                }
            }
        }
        return null;
    }

    /**
     * Actualiza los indices de las tablas dinamicas de la linea y las variables que
     * * caracterizan los productos
     *
     * @param idBatch id del lote a crear, puede ser nulo
     * @param started fecha inicial del rango a actualizar
     * @param ended   fecha final del rango a actualizar
     * @param line    linea a la que afecta el lote
     * @param product mapa con la lista de valores del producto por cada variable
     *                que lo caracteriza, puede ser null
     * @return retorna la lista de variables actualizadas
     */
    private List<Variable> updateIndexAndProductVariablesOfData(Integer idBatch, Date started, Date ended, Line line, Map product) {
        List<Files> files = filesMapper.getListFilesByLine(line.getId());
        Integer idFileProduction = getIdFileOfLine(line);
        List<Variable> variableOfProduct = new ArrayList<Variable>();
        for (Files file : files) {
            Map map = new HashMap();
            map.put("batch", idBatch);
            String tableName = CustomLineTable.getTableName(line, file.getId());
            List<WhereStamen> where = new ArrayList<>();

            WhereStamen lowerLimit = new WhereStamen();
            lowerLimit.setField("`create`");
            lowerLimit.setOperator(WhereStamen.Comparative.GreaterThanOrEqual);
            lowerLimit.setValue(started);
            where.add(lowerLimit);

            WhereStamen upperLimit = new WhereStamen();
            upperLimit.setField("`create`");
            upperLimit.setOperator(WhereStamen.Comparative.LessThan);
            if (ended != null) {
                upperLimit.setValue(ended);
            } else {
                Date lastDataTime = memoryRedisService.getFileMemory(file.getName()).getTime();
                upperLimit.setValue(lastDataTime);
                upperLimit.setOperator(WhereStamen.Comparative.LessThanOrEqual);
            }
            upperLimit.setLogical(WhereStamen.LogicalOperator.And);
            where.add(upperLimit);

            List<Map<String, Object>> resutl = tableMapper.getDynamic(Arrays.asList("id"), where, tableName, "id asc",
                    0);
            if (resutl.size() == 0)
                continue;
            Integer minId = (Integer) resutl.get(0).get("id");
            Integer maxId = (Integer) resutl.get(resutl.size() - 1).get("id");
            String tableIndex = CustomLineTable.getTableIndexName(line, file.getId());

            lowerLimit.setValue(minId);
            upperLimit.setOperator(WhereStamen.Comparative.LessThanOrEqual);
            upperLimit.setValue(maxId);
            if (file.getId() == idFileProduction) {
                upperLimit.setField("id");
                lowerLimit.setField("id");
                map.put("product_id", product == null ? null : product.get("id"));
                map.put("rated_speed", product == null ? null : product.get("rated_speed"));
                map.put("measure_per_unit", product == null ? null : product.get("measure_per_unit"));

                List<ProductFeatureDTO> variablesOfProduct = productMapper.getFeatureAddByLine(line.getId());
                Map mapVariable = new HashMap();
                variablesOfProduct.forEach(variable -> {
                    Variable v = new Variable();
                    v.setId(variable.getId());
                    v.setType(Variable.TYPE_CONTINUING);
                    v.setName(variable.getName());
                    variableOfProduct.add(v);
                    mapVariable.put(variable.getId(), product == null ? null : Double.parseDouble(product.get(variable.getId() + "").toString()));
                });
                tableMapper.updateByCondition(tableName, mapVariable, where);
            }
            upperLimit.setField("data_id");
            lowerLimit.setField("data_id");
            tableMapper.updateByCondition(tableIndex, map, where);
        }
        return variableOfProduct;
    }

    /**
     * Actualiza los indices de las tablas dinamicas de la linea únicamete el lote
     *
     * @param idBatch id del lote a crear, puede ser nulo
     * @param started fecha inicial del rango a actualizar
     * @param ended   fecha final del rango a actualizar
     * @param line    linea a la que afecta el lote
     */
    private void updateIndexBatch(Integer idBatch, Date started, Date ended, Line line) {
        List<Files> files = filesMapper.getListFilesByLine(line.getId());
        for (Files file : files) {
            Map map = new HashMap();
            map.put("batch", idBatch);
            String tableName = CustomLineTable.getTableName(line, file.getId());
            List<WhereStamen> where = new ArrayList<>();

            WhereStamen lowerLimit = new WhereStamen();
            lowerLimit.setField("`create`");
            lowerLimit.setOperator(WhereStamen.Comparative.GreaterThanOrEqual);
            lowerLimit.setValue(started);
            where.add(lowerLimit);

            WhereStamen upperLimit = new WhereStamen();
            upperLimit.setField("`create`");
            upperLimit.setOperator(WhereStamen.Comparative.LessThan);
            if (ended != null) {
                upperLimit.setValue(ended);
            } else {
                Date lastDataTime = memoryRedisService.getFileMemory(file.getName()).getTime();
                upperLimit.setValue(lastDataTime);
                upperLimit.setOperator(WhereStamen.Comparative.LessThanOrEqual);
            }
            upperLimit.setLogical(WhereStamen.LogicalOperator.And);
            where.add(upperLimit);

            List<Map<String, Object>> resutl = tableMapper.getDynamic(Arrays.asList("id"), where, tableName, "id asc",
                    0);
            if (resutl.size() == 0)
                continue;
            Integer minId = (Integer) resutl.get(0).get("id");
            Integer maxId = (Integer) resutl.get(resutl.size() - 1).get("id");
            String tableIndex = CustomLineTable.getTableIndexName(line, file.getId());

            lowerLimit.setValue(minId);
            upperLimit.setOperator(WhereStamen.Comparative.LessThanOrEqual);
            upperLimit.setValue(maxId);
            upperLimit.setField("data_id");
            lowerLimit.setField("data_id");
            tableMapper.updateByCondition(tableIndex, map, where);
        }
    }

    /**
     * Valida si la actualización del lote con el que encaja es posible partir, y lo
     * actualiza en caso sea correcto
     *
     * @param started      fecha inicial del rango a actualizar
     * @param ended        fecha final del rango a actualizar, puede ser nulo
     * @param listOfbatchs
     * @param line         linea que se esta trabajando
     * @param userId
     */
    private void updateBeforeBatch(Date started, Date ended, List<Batch> listOfbatchs, int userId, Line line) {
        for (Batch batch : listOfbatchs) {
            if (batch.getEnded() == null) {
                Files file = filesMapper.getById(getIdFileOfLine(batch.getLine()));
                Date lastDataTime = new Date(memoryRedisService.getFileMemory(file.getName()).getTime().getTime());
                if (batch.getStarted().after(started) || batch.getStarted().equals(started)) {
                    if (ended == null) {
                        batchMapper.softDeleted(batch.getId(), 1, userId);
                    } else if (lastDataTime.after(ended)) {
                        batch.setStarted(ended);
                        batchMapper.update(batch, userId);
                    } else if (lastDataTime.equals(ended) || lastDataTime.before(ended)) {//falta ver que pasa con la data que no esta considerada en el nuevo lote
                        batchMapper.softDeleted(batch.getId(), 1, userId);
                    }
                } else if (batch.getStarted().before(started)) {
                    if (ended == null) {
                        batch.setEnded(started);
                        batchMapper.update(batch, userId);
                    } else if (lastDataTime.after(ended) || lastDataTime.equals(ended)) {
                        Batch newBatch = new Batch();
                        newBatch.setStarted(ended);
                        newBatch.setEnded(batch.getEnded());

                        batch.setEnded(started);
                        batchMapper.update(batch, userId);

                        newBatch.setLine(line);
                        batchMapper.insert(newBatch, userId);
                        updateIndexBatch(newBatch.getId(), newBatch.getStarted(), newBatch.getEnded(), line);
                    } else if (lastDataTime.before(ended)) {
                        batch.setEnded(started);
                        batchMapper.update(batch, userId);

                    }
                }
            } else {
                if (batch.getStarted().after(started) || batch.getStarted().equals(started)) {
                    if (ended == null) {
                        batchMapper.softDeleted(batch.getId(), 1, userId);
                    } else if (batch.getEnded().after(ended)) {
                        batch.setStarted(ended);
                        batchMapper.update(batch, userId);
                    } else if (batch.getEnded().equals(ended) || batch.getEnded().before(ended)) {//aqui esta el error
                        batchMapper.softDeleted(batch.getId(), 1, userId);
                    }
                } else if (batch.getStarted().before(started)) {
                    if (ended == null) {
                        batch.setEnded(started);
                        batchMapper.update(batch, userId);
                    } else if (batch.getEnded().after(ended)) {
                        Batch newBatch = new Batch();
                        newBatch.setStarted(ended);
                        newBatch.setEnded(batch.getEnded());
                        batch.setEnded(started);
                        batchMapper.update(batch, userId);
                        newBatch.setLine(line);
                        batchMapper.insert(newBatch, userId);
                        updateIndexBatch(newBatch.getId(), newBatch.getStarted(), newBatch.getEnded(), line);
                    } else if (batch.getEnded().equals(ended) || batch.getEnded().before(ended)) {
                        batch.setEnded(started);
                        batchMapper.update(batch, userId);

                    }
                }
            }
        }
    }

    /**
     * Actualiza las derivadas mixtas y sus dependientes, en el rango del tiempo del
     * nuevo lote que hayan sido modificadas
     *
     * @param started   fecha inicial del rango a actualizar
     * @param ended     fecha final del rango a actualizar
     * @param line      linea que se actualiza
     * @param variables
     * @return retorna la lista de variables actualizadas unidas con la lista
     * inicial.
     */
    private List<Variable> updateMixVariables(Date started, Date ended, Line line, List<Variable> variables) {
        variables.sort(Comparator.comparingLong(Variable::getId));
        Map<Integer, Double> fixeds = memoryRedisService.getFixedByCompany(line.getPlant().getUserCompany());
        Map<String, MemoryForVariablesMixed> memoryForMixes = memoryRedisService.getMemoriesMixByIdCompany(line.getPlant().getUserCompany().getId());
        memoryForMixes.forEach((fileId, memoryForVariablesMix) -> {
            if (memoryForVariablesMix.getMemory1() != null &&
                    memoryForVariablesMix.getMemory2() != null) {
                Date nowParent1 = memoryRedisService.getDateOfMemoryMix(memoryForVariablesMix.getMemory1());
                Date nowParent2 = memoryRedisService.getDateOfMemoryMix(memoryForVariablesMix.getMemory2());
                if (nowParent1 != null && nowParent2 != null) {
                    /**
                     * Tiempo menor entre los datos de las tablas de los padres
                     */
                    Date minDateFromParents = null, maxDateFromParents;

                    if (nowParent1.getTime() < nowParent2.getTime()) {
                        minDateFromParents = nowParent1;
                        maxDateFromParents = nowParent2;
                    } else {
                        minDateFromParents = nowParent2;
                        maxDateFromParents = nowParent1;
                    }

                    if (minDateFromParents.before(started)) {
                        minDateFromParents = started;
                    }

                    if (ended != null && maxDateFromParents.after(ended)) {
                        maxDateFromParents = ended;
                    }

                    List<WhereStamen> where = new ArrayList<>();

                    WhereStamen lowerLimit = new WhereStamen();
                    lowerLimit.setField("`create`");
                    lowerLimit.setOperator(WhereStamen.Comparative.GreaterThanOrEqual);
                    lowerLimit.setValue(minDateFromParents);
                    where.add(lowerLimit);

                    WhereStamen upperLimit = new WhereStamen();
                    upperLimit.setField("`create`");
                    upperLimit.setOperator(ended == null ? WhereStamen.Comparative.LessThanOrEqual : WhereStamen.Comparative.LessThan);
                    upperLimit.setValue(maxDateFromParents);
                    upperLimit.setLogical(WhereStamen.LogicalOperator.And);
                    where.add(upperLimit);

                    List<Map<String, Object>> resutl = tableMapper.getDynamic(Arrays.asList("id"), where,
                            memoryForVariablesMix.getTableName().getName(), "id asc", 0);
                    if (resutl.size() > 0) {
                        Integer minId = (Integer) resutl.get(0).get("id");
                        Integer maxId = (Integer) resutl.get(resutl.size() - 1).get("id");
                        memoryForVariablesMix.getDeriveds().forEach(d -> {
                            VariableDerivedDTO dto = new VariableDerivedDTO(d);
                            FormulaDerivedDTO formula = dto.getFormula();
                            if ((containsVariable(variables, formula.getVar1().getId())
                                    || (formula.getVar2() != null
                                    && containsVariable(variables, formula.getVar2().getId())))
                                    && !containsVariable(variables, d.getId())) {
                                variables.add(d);
                                Map<String, String> values = new HashMap<String, String>();
                                Variable variable1 = variableMapper.getFullById(formula.getVar1().getId());
                                Variable variable2;
                                switch (formula.getType()) {
                                    case 1:
                                        variable2 = variableMapper.getFullById(formula.getVar2().getId());
                                        String valueOperation = "";
                                        if (variable1.getType().equals(Variable.TYPE_FIXED)) {
                                            if (variable2.getType().equals(Variable.TYPE_FIXED)) {
                                                valueOperation = derivedUtil.operar(
                                                        fixeds.get(formula.getVar1().getId()), formula.getOperation(),
                                                        fixeds.get(formula.getVar2().getId())).toString();
                                            } else {
                                                valueOperation = derivedUtil.operarInSQL(
                                                        fixeds.get(formula.getVar1().getId()).toString(),
                                                        formula.getOperation(), "`" + formula.getVar2().getId() + "`");
                                            }
                                        } else {
                                            if (variable2.getType().equals(Variable.TYPE_FIXED)) {
                                                valueOperation = derivedUtil.operarInSQL(
                                                        "`" + formula.getVar1().getId() + "`", formula.getOperation(),
                                                        fixeds.get(formula.getVar2().getId()).toString());
                                            } else {
                                                valueOperation = derivedUtil.operarInSQL(
                                                        "`" + formula.getVar1().getId() + "`", formula.getOperation(),
                                                        "`" + formula.getVar2().getId() + "`");
                                            }
                                        }
                                        values.put(d.getId() + "", valueOperation);
                                        tableMapper.updateDerivedMixOperationValue(
                                                memoryForVariablesMix.getTableName().getName(),
                                                memoryForVariablesMix.getTableName().getParent1(),
                                                memoryForVariablesMix.getTableName().getParent2(), values, minId,
                                                maxId);
                                        break;
                                    default:
                                        break;
                                }

                            }
                        });
                    }
                }
            }
        });
        return variables;

    }

    @Override
    public void updateStop(Line line, Integer userId, StopDTO stop) {

        //verificar memoria libre y ocupar o terminar
        List<Memory> memories = null;

        try {
            memories = verify_memory_available(line);
            Legend legend = null;
            int value = 0;
            if (stop.getLegendId() > 0) {
                legend = legendMapper.getById(stop.getLegendId());
                value = Integer.parseInt(legend.getValue());//para insertar en columna tipo
            }

            Variable tipo = null;
            String tablenameTipo = null;
            if (stop.getStopTypeId() > 0) {
                tipo = variableMapper.getById(stop.getStopTypeId());//variable tipo para tabla de modificar
                tablenameTipo = getName(tipo, line);
            }
            Variable tiempo = variableMapper.getById(stop.getStopTimeId());//variable tiempo para tabla de modificar
            String tablenameTiempo = getName(tiempo, line);

            Variable selected = variableMapper.getFullById(stop.getVariableSelectedId());//variable selected para tabla de modificar
            String tablenameSelected = getName(selected, line);

            Variable selected_related = null;
            String tablenameSelectedRelated = null;
            if (selected.getRelated() != null && selected.getRelated() != 0) {
                selected_related = variableMapper.getById(selected.getRelated());//variable selected para tabla de modificar
                tablenameSelectedRelated = getName(selected_related, line);
            }

            //actualizar stops
            int resp = tableMapper.updateStop(userId, stop, value, selected_related, tablenameTipo, tablenameTiempo, tablenameSelected, tablenameSelectedRelated);

            //fecha end
            Calendar cal = Calendar.getInstance();
            cal.setTime(stop.getDate());
            cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + 5);

            //lista de variables
            List<Variable> variableUpated = new ArrayList<>();
            if (tipo != null)
                variableUpated.add(tipo);
            variableUpated.add(tiempo);
            variableUpated.add(selected);
            if (selected_related != null)
                variableUpated.add(selected_related);

            //actualizar variables derivadas y mixtas
            variableUpated = updateDeriveds(stop.getDate(), cal.getTime(), line, variableUpated);
            variableUpated = updateMixVariables(stop.getDate(), cal.getTime(), line, variableUpated);

            //actualizar variabkes de memoria
            updateMemory(stop.getDate(), cal.getTime(), line, variableUpated, memories);

            //liberar memoria
            break_free_memory(memories);
        } catch (ValidationException e) {
            //liberar memoria
            break_free_memory(memories);
            throw e;
        } catch (Exception e) {
            break_free_memory(memories);
            throw new ValidationException(e.getMessage());
        }
    }

    private String getName(Variable variable, Line line) {
        if (variable.getType().equals(Variable.TYPE_DERIVED)) {
            return variableMapper.getDerivedById(variable.getId()).getTable().getName();
        } else {
            return CustomLineTable.getTableName(line, variableMapper.getContinuingById(variable.getId()).getIdFile());
        }
    }

    private void updateMemory(Date started, Date ended, Line line, List<Variable> variables, List<Memory> memories) {
        variables.sort(Comparator.comparingLong(Variable::getId));

        for (Memory m : memories) {
            Memory memory = memoryRedisService.getFileMemory(m.getFile().getName());
            if (memory.getTime() != null && (ended == null ||
                    (memory.getTime().getTime() >= started.getTime() && memory.getTime().getTime() < ended.getTime())
            )) {//verifica si esta en el rango
                char identifier_aux = '`';
                char identifier_der = 'a';
                char identifier_con = 'b';
                List<String> selects = new ArrayList<>();
                List<String> tables = new ArrayList<>();
                List<WhereStamen> wheres = new ArrayList<>();
                for (MemoryVariablesLine memoryVariablesLine : memory.getMemoryByLine().values()) {
                    for (Variable var : variables) {
                        identifier_aux += 1;
                        if (var.getType().equals(Variable.TYPE_DERIVED)) {
                            List<VariableDerived> variableDeriveds = memoryVariablesLine.getDerives();
                            for (VariableDerived varder : variableDeriveds) {
                                if (varder.getId() != var.getId())
                                    continue;
                                String tableName = "";
                                if (var.getClass().equals(VariableDerived.class) &&
                                        ((VariableDerived) var).getTable() != null) {
                                    tableName = ((VariableDerived) var).getTable().getName();
                                } else {
                                    tableName = variableMapper.getDerivedById(var.getId()).getTable().getName();
                                }
                                String table = new StringBuilder().append(tableName).append(" as ").append(identifier_der).toString();
                                int pos = tables.indexOf(table);
                                if (pos < 0) {
                                    table = new StringBuilder().append(tableName).append(" as ").append(identifier_aux).toString();
                                    pos = tables.indexOf(table);
                                    if (pos < 0)
                                        identifier_der = identifier_aux;
                                }
                                if (pos < 0) {
                                    tables.add(table);
                                    System.out.println(tables);

                                    wheres.addAll(getLimitsForCreate(identifier_der, started, ended, memory));
                                }

                                selects.add(new StringBuilder().append(identifier_der).append(".`").append(var.getId()).append("`").toString());
                            }
                        } else if (var.getType().equals(Variable.TYPE_CONTINUING)) {
                            List<VariableContinuing> variableContinuings = memoryVariablesLine.getContinuing();
                            for (VariableContinuing varcon : variableContinuings) {
                                if (varcon.getId() != var.getId())
                                    continue;
                                String table = new StringBuilder().append(CustomLineTable.getTableName(line, variableMapper.getContinuingById(var.getId()).getIdFile())).append(" as ").append(identifier_con).toString();
                                int pos = tables.indexOf(table);
                                if (pos < 0) {
                                    table = new StringBuilder().append(CustomLineTable.getTableName(line, variableMapper.getContinuingById(var.getId()).getIdFile())).append(" as ").append(identifier_aux).toString();
                                    pos = tables.indexOf(table);
                                    if (pos < 0)
                                        identifier_con = identifier_aux;
                                }
                                if (pos < 0) {
                                    tables.add(table);
                                    System.out.println(tables);
                                    wheres.addAll(getLimitsForCreate(identifier_con, started, ended, memory));

                                }
                                selects.addAll(Arrays.asList(new StringBuilder().append(identifier_con).append(".`").append(var.getId()).append("`").toString()));
                            }
                        }
                    }
                    if (tables.isEmpty() || selects.isEmpty() || wheres.isEmpty())
                        continue;
                    List<Map<String, Object>> results = tableMapper.getDynamicVarious(selects, wheres, tables, null, 0);
                    if (results.size() == 0)
                        continue;
                    Map<Integer, Double> x = new HashMap<>();

                    for (Map<String, Object> result : results) {
                        for (Map.Entry<String, Object> entry : result.entrySet()) {
                            System.out.println(entry.getKey() + "/" + entry.getValue());
                            String strvalue = (String) entry.getValue();
                            double val = Double.parseDouble(strvalue);
                            Integer key = Integer.parseInt(entry.getKey());
                            //x.put(key, val);
                            memoryVariablesLine.getData().put(key, val);
                        }
                    }
                    tables.clear();
                    selects.clear();
                    wheres.clear();
                    identifier_aux = '`';
                    identifier_con = 'b';
                    identifier_der = 'a';
                }
                memoryRedisService.updateMemory(memory);
            }
        }
    }

    private List<WhereStamen> getLimitsForCreate(char identifier_der, Date started, Date ended, Memory memory) {
        List<WhereStamen> wheres = new ArrayList<>();

        WhereStamen lowerLimit = new WhereStamen();
        lowerLimit.setField(new StringBuilder().append(identifier_der).append(".").append("`create`").toString());
        lowerLimit.setOperator(WhereStamen.Comparative.GreaterThanOrEqual);
        lowerLimit.setValue(started);
        wheres.add(lowerLimit);

        WhereStamen upperLimit = new WhereStamen();
        upperLimit.setField(new StringBuilder().append(identifier_der).append(".").append("`create`").toString());
        upperLimit.setOperator(WhereStamen.Comparative.LessThan);
        if (ended != null) {
            upperLimit.setValue(ended);
        } else {
            Date lastDataTime = memory.getTime();
            upperLimit.setValue(lastDataTime);
            upperLimit.setOperator(WhereStamen.Comparative.LessThanOrEqual);
        }
        upperLimit.setLogical(WhereStamen.LogicalOperator.And);
        wheres.add(upperLimit);

        return wheres;
    }

    private List<Memory> verify_memory_available(Line line) throws InterruptedException {
        if (!memoryRedisService.isAvailableMemory()) {
            throw new ValidationException("Mantenimiento");
        }

        //verificar memoria libre y ocupar o terminar
        List<Files> files = filesMapper.getListFilesByLine(line.getId());
        List<Memory> memories = new ArrayList<>();
        for (Files file : files) {
            Memory fileStatus = memoryRedisService.getFileStatus(file.getName());
            if (fileStatus == null) {
                throw new ValidationException("No se encontro información para el archivo " + file.getName());
            }
            for (int i = 0; i < 3; i++) {
                if (!memoryRedisService.isPlantStatusEnable(line.getPlant().getId()) || memoryRedisService.isFileIsProcessing(file.getName())) {
                    if (i == 2) {
                        throw new ValidationException("Archivo ocupado");
                    }
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        memories.forEach(m -> m.setProcessing(false));
                        throw e;
                    }
                } else {
                    //Indica que se va a procesar este archivo
                    memoryRedisService.updateMemoryStatusProcessingRedis(true, file.getName());
                    memories.add(fileStatus);
                    break;
                }
            }
        }
        return memories;
    }

    private void break_free_memory(List<Memory> memories) {
        if (memories != null)
            for (Memory mem : memories) {
                memoryRedisService.updateMemoryStatusProcessingRedis(false, mem.getFile().getName()); //***********> REDIS
            }
    }
}
