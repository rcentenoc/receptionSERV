package pe.mm.reception.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pe.mm.reception.common.InternalException;
import pe.mm.reception.common.ValidationRaspberryException;
import pe.mm.reception.common.util.VariableTypeEnum;
import pe.mm.reception.core.dto.*;
import pe.mm.reception.core.mapper.*;
import pe.mm.reception.core.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.*;

@Service
public class DataServiceImpl implements DataService {

    Logger logger = LoggerFactory.getLogger(DataService.class);

    @Autowired
    private VariableMapper variableMapper;

    @Autowired
    private MemoryRedisService memoryRedisService;

    @Autowired
    private VariableDerivedUtilService variableDerivedUtil;

    @Autowired
    private TableMapper tableMapper;

    @Autowired
    private TariffStructureMapper tariffStructureMapper;

    @Autowired
    private PeriodMapper periodMapper;

    @Autowired
    private CalendaryMapper calendaryMapper;
    @Autowired
    private DepletionMapper depletionMapper;

    @Autowired
    private BatchMapper batchMapper;

    /*
     * @Autowired
     * private MemoryService memoryService;
     * 
     * @Autowired
     * private VariableDerivedUtilService variableDerivedUtil;
     * /
     **/

    @Autowired
    private ProductMapper productMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public synchronized void insert(DataSendDTO dataDTO) {
        // WriteLog.logger.write("logInsert.txt","Inicio insercion");
        // Verifica que la memoria haya sido cargada y no este en mantenimiento
        // if (!memoryService.start())
        Date dataDTODate = dataDTO.getTime();

        executeValidations(memoryRedisService, dataDTO.getCode(), dataDTODate, dataDTO.getData().size(),
                dataDTO.getTime());
        // executeValidation sirve para verificar que la memoria haya sido cargada y no
        // este en mantenimiento
        // MEDIANTE la estructura de executeValidations(el redis, el codigo del archivo,
        // la fecha del dato insertado, el tamaño del dato insertado, la fecha del dato
        // insertado)

        // Indica que se va a procesar este archivo
        // memory.setProcessing(true);
        memoryRedisService.updateMemoryStatusProcessingRedis(true, dataDTO.getCode()); // ***********> REDIS
        // memoryRedisService sirve para actualizar el estado de la memoria en redis
        // mediante la estructura de updateMemoryStatusProcessingRedis(true, el codigo
        // del archivo)

        Memory redisMemoryByFileStatus = memoryRedisService.getFileStatus(dataDTO.getCode()); // ***********> REDIS
        // redisMemoryByFileStatus traera la informacion del archivo mediante el codigo
        // del archivo
        Files file = redisMemoryByFileStatus.getFile();
        // file traera la informacion del archivo mediante la informacion de la memoria
        // del archivo
        Memory fileMemory = memoryRedisService.getFileMemory(dataDTO.getCode());
        // fileMemory traera la informacion de la memoria del archivo mediante el codigo
        // del archivo

        /**
         * Si la fecha del dato ingresado es menor a la que actualmente se maneja no se
         * inserta, pero se deja pasar
         */
        if (isDataAlreadyProcessed(fileMemory, dataDTO)) {
            memoryRedisService.updateMemoryStatusProcessingRedis(false, dataDTO.getCode()); // ***********> REDIS
            System.out.println("isDataAlreadyProcessed is: true");
            return;
        }

        dataDTO.setTime(aproximarAlMinutoInferior(dataDTO.getTime(), file.getSendFrequency()));

        try {

            executeDataNormalization(fileMemory, dataDTO, file.getSendFrequency());

            // Objeto que guarda la información con la data continua y derivada separada por
            // liena
            Map<Integer, Map<Integer, Double>> mapDataSavedByLine = saveDataContinuousAndDerivedByLine(fileMemory,
                    dataDTO, file);

            // se guarda la información a la memoria de la fila insertada luego de que todo
            // haya sido procesado
            mapDataSavedByLine.forEach((i, j) -> fileMemory.getMemoryByLine().get(i).setData(j));

            fileMemory.setTime(dataDTO.getTime());
            memoryRedisService.updateMemory(fileMemory);

            // WriteLog.logger.write("logInsert.txt","fin");
            // memoryService.end();

            memoryRedisService.updateMemoryStatusProcessingRedis(false, dataDTO.getCode()); // ***********> REDIS
        } catch (Exception e) {
            // memoryService.end();

            memoryRedisService.updateMemoryStatusProcessingRedis(false, dataDTO.getCode()); // ***********> REDIS
            throw new ValidationRaspberryException(e, dataDTO.getCode(), dataDTODate);
        }
    }

    private Map<Integer, Map<Integer, Double>> saveDataContinuousAndDerivedByLine(Memory fileMemory,
            DataSendDTO dataDTO, Files memorFilesByStatus) {

        Map<Integer, Map<Integer, Double>> mapDataByLine = new HashMap<>();

        for (MemoryVariablesLine memoryVariablesLine : fileMemory.getMemoryByLine().values()) {

            System.out.println("loop memoryVariablesLine....");
            Line line = memoryVariablesLine.getLine();
            // WriteLog.logger.write("logInsert.txt","procesamiento continuas de linea: " +
            // line.getName() + ", file_id" + file.getName() + " en la fecha: " +
            // dataDTO.getTime().toString());
            String tableName = CustomLineTable.getTableName(line, memorFilesByStatus.getId());
            String idData = tableMapper.getFieldByCreate("id", dataDTO.getTime(), tableName);

            // objeto con la data continua para ser insertada
            Map<Integer, Double> mapDataMerged = new HashMap<>();
            mappingMemoryContinousKeyAndInputContinousValue(memoryVariablesLine.getContinuing(), dataDTO, mapDataMerged,
                    fileMemory.getIdToColumnName());

            Long timeInitila = dataDTO.getTime().getTime();
            if (idData != null) {
                tableMapper.update(tableName,
                        mapDataMerged,
                        "id",
                        idData);

                logger.info("Line update from: " + idData + " of " + tableName + " at " + timeInitila + " from "
                        + dataDTO.getCode());
            } else {
                List headers = new ArrayList();
                List values = new ArrayList();
                headers.add("create");
                headers.addAll(mapDataMerged.keySet());

                values.add(dataDTO.getTime());
                values.addAll(mapDataMerged.values());

                tableMapper.insert(tableName, headers, values);
                idData = tableMapper.getFieldByCreate("id", dataDTO.getTime(), tableName);
            }

            String secondNameFile = fileMemory.getFile().getSecondName();
            boolean isProduction = isSecondFileNameForProduction(secondNameFile);
            /// Inserta los indíces
            insertIndex(idData, line, memorFilesByStatus, dataDTO.getTime(), fileMemory.getIdToColumnName(),
                    mapDataMerged, memoryVariablesLine, isProduction);
            // Data continua mezclada con la fija
            Map<Integer, Double> mapDataWithFixed = new HashMap<>();
            mapDataWithFixed.putAll(mapDataMerged);
            mapDataWithFixed.putAll(memoryRedisService.getFixedByCompany(line.getPlant().getUserCompany()));
            // retorna solo la data mixta
            Map<Integer, Double> mapDerived = insertDerived(idData, dataDTO.getTime(), memoryVariablesLine,
                    mapDataWithFixed);

            // se mezcla la data continua mas data derivada tiempo execution
            mapDataMerged.putAll(mapDerived);

            // restaurar el valor de la parada en bd desde redis (anterior valor)
            restorePreviousStopValue(memoryVariablesLine, mapDerived, mapDataMerged, tableName, idData);

            mapDataByLine.put(line.getId(), mapDataMerged);
        }
        return mapDataByLine;
    }

    private boolean isDataAlreadyProcessed(Memory fileMemory, DataSendDTO dataDTO) {
        return fileMemory.getTime() != null && fileMemory.getTime().getTime() >= dataDTO.getTime().getTime();
    }

    private void executeDataNormalization(Memory fileMemory, DataSendDTO dataDTO, int sendFrequency) {

        if (fileMemory.getTime() != null) {
            Long diferenceTime = (dataDTO.getTime().getTime() - fileMemory.getTime().getTime()) / 1000;
            /**
             * Si se esta enviando un dato, dejando huecos se debe normalizar las acumuladas
             */
            if (diferenceTime > sendFrequency) {
                insertMissing(fileMemory, dataDTO);
            } else {
                System.out.println("WARNING: time insert is less than file memory time");
            }
        } else {
            System.out.println("WARNING: fileMemory is null");
        }
    }

    private void executeValidations(MemoryRedisService memoryRedisService, String codeFile, Date dataInsertDate,
            int dataInsertSize, Date dateTime) {

        if (!memoryRedisService.isAvailableMemory()) // REVISAR EN OTRO CASO
            throw new ValidationRaspberryException("Mantenimiento", codeFile, dataInsertDate);

        Memory fileStatus = memoryRedisService.getFileStatus(codeFile); // ***********> REDIS

        if (fileStatus == null) {
            // memoryService.end();
            throw new ValidationRaspberryException("No se encontro información para el archivo", codeFile,
                    dataInsertDate);
        } else if (!memoryRedisService.isPlantStatusEnable(fileStatus.getFile().getPlant().getId())) {
            throw new ValidationRaspberryException("Planta en mantenimiento", codeFile, dataInsertDate);
        } else if (!memoryRedisService.isFileIsProcessing(codeFile)) {
            throw new ValidationRaspberryException("Archivo ocupado", codeFile, dataInsertDate);
        }

        if (dataInsertSize != fileStatus.getTotalCount()) {
            // memoryService.end();
            // memory.setProcessing(false);
            memoryRedisService.updateMemoryStatusProcessingRedis(false, codeFile); // ***********> REDIS

            throw new ValidationRaspberryException("No se tiene la misma cantidad de datos que los almacenados",
                    codeFile, dataInsertDate);
        }
    }

    /*
     * Restaurar valores de paradas anteriores desde redis a mysql
     * 
     * @param memoryVariablesLine
     * 
     * @param mapDerived
     * 
     * @param mapData
     * 
     * @param tableName
     * 
     * @param idData
     */
    private void restorePreviousStopValue(MemoryVariablesLine memoryVariablesLine, Map<Integer, Double> mapDerived,
            Map<Integer, Double> mapData, String tableName, String idData) {
        // Map de actualizaciones de valores de paradas
        Map stopValuesRestored = new HashMap();
        memoryVariablesLine.getDerives().stream().filter(derived ->
        // Es parada una parada?
        (derived.getGenericType() == 11 || derived.getGenericType() == 12)).forEach(derived -> {

            // Carga de redis (el id de la variable relacionada de la derivada) o (el id de
            // la variable tipo parada)
            int stopKey = derived.getRelatedIdVariable();
            // Carga de redis el valor del tipo de parada anterior con el id de la parada
            Double lastValueStop = memoryVariablesLine.getData().get(stopKey);
            // Carga de mysql el valor de la parada actual
            Double currentStopValue = mapDerived.get(derived.getId());
            // Carga de mysql el valor del tipo de parada
            Double currentStopTypeValue = mapData.get(stopKey);
            // Es una parada? && el valor del tipo de parada es igual a zero? && el valor
            // del tipo parada anterior no es zero?
            if (currentStopValue > 0 && currentStopTypeValue == 0 && lastValueStop != 0) {
                // Se agrega la parada anterior al map de actualizaciones de valores de paradas
                stopValuesRestored.put(stopKey, lastValueStop);
            }
        });
        if (stopValuesRestored.size() > 0) {
            // Se actualiza mysql
            tableMapper.update(tableName, stopValuesRestored, "id", idData);
            // Se actualiza redis
            mapData.putAll(stopValuesRestored);
        }
    }

    private Date aproximarAlMinutoInferior(Date inputTime, int frecuencyMemory) {
        Calendar date = Calendar.getInstance();
        date.setTime(inputTime);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        int min = date.get(Calendar.MINUTE);
        date.set(Calendar.MINUTE, min - (min % (frecuencyMemory / 60)));
        return date.getTime();
    }

    private Map<String, Integer> getPositionDTO(List<String> head) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (int i = 0; i < head.size(); i++) {
            map.put(head.get(i), i);
        }
        return map;
    }

    private boolean isSecondFileNameForProduction(String secondFileName) {
        boolean isProduction = false;
        if (secondFileName != null) {
            isProduction = secondFileName.contains("_PROD_");
        }
        return isProduction;
    }

    /**
     * Calcula la data faltante
     * 
     * @param memory
     * @param dataDTO
     */
    private void insertMissing(Memory redisMemoryFile, DataSendDTO dataDTO) {

        Map<String, Integer> positionInDTO = getPositionDTO(dataDTO.getHead());
        String secondNameFile = redisMemoryFile.getFile().getSecondName();
        boolean isProduction = isSecondFileNameForProduction(secondNameFile);

        Files file = redisMemoryFile.getFile();
        if (file.getSendFrequency() == 0)
            throw new InternalException("No puede ser 0 la frecuencia de envío");

        int count = ((int) (((dataDTO.getTime().getTime() - redisMemoryFile.getTime().getTime()) / 1000)))
                / file.getSendFrequency();

        Map<Integer, Double> addForVariable = new HashMap<>();

        for (MemoryVariablesLine memoryVariablesLine : redisMemoryFile.getMemoryByLine().values()) {
            if (memoryVariablesLine.getDerives().size() > 0) {
                for (VariableDerived derived : memoryVariablesLine.getDerives()) {
                    VariableDerivedDTO dto = new VariableDerivedDTO(derived);
                    FormulaDerivedDTO formula = dto.getFormula();
                    Double valueDerived = null, value1, value2 = null;

                    if (formula.getType() == 2) {
                        int i = positionInDTO.get(redisMemoryFile.getIdToColumnName().get(formula.getVar1().getId()));
                        value1 = Double.valueOf(dataDTO.getData().get(i));
                        value2 = getValue(formula.getVar2().getId(), memoryVariablesLine.getData());
                        if (value2 == null)
                            valueDerived = new Double(0);
                        else {
                            if (value1 < value2)
                                valueDerived = value1;
                            else
                                valueDerived = variableDerivedUtil.operar(value1, formula.getOperation(), value2);
                            valueDerived = valueDerived / count;
                        }
                        valueDerived = round(valueDerived, 4);
                        addForVariable.put(formula.getVar2().getId(), valueDerived);
                    }

                }
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(redisMemoryFile.getTime());
        calendar.add(Calendar.SECOND, file.getSendFrequency());
        for (; calendar.getTime().getTime() < dataDTO.getTime().getTime(); calendar.add(Calendar.SECOND,
                file.getSendFrequency())) {
            // Objeto que guarda la información con la data continua y derivada separada por
            // liena
            Map<Integer, Map<Integer, Double>> mapDataByLine = new HashMap<>();
            for (MemoryVariablesLine memoryVariablesLine : redisMemoryFile.getMemoryByLine().values()) {
                Line line = memoryVariablesLine.getLine();
                String tableName = CustomLineTable.getTableName(line, file.getId());
                String idData;
                // objeto con la data continua para ser insertada
                Map<Integer, Double> mapDataWith0 = new HashMap<>();
                memoryVariablesLine.getContinuing().stream().filter(v -> v.getStatus() == 1).forEach(v -> {
                    if (addForVariable.containsKey(v.getId())) {
                        mapDataWith0.put(v.getId(),
                                getValue(v.getId(), memoryVariablesLine.getData()) + addForVariable.get(v.getId()));
                    } else {
                        mapDataWith0.put(v.getId(), 0d);
                    }
                });
                List headers = new ArrayList();
                List values = new ArrayList();
                headers.add("create");
                headers.addAll(mapDataWith0.keySet());
                values.add(calendar.getTime());
                values.addAll(mapDataWith0.values());
                tableMapper.insert(tableName, headers, values);
                idData = tableMapper.getFieldByCreate("id", calendar.getTime(), tableName);

                // Inserta los indíces
                // WriteLog.logger.write("logInsert.txt","inicio de indexación");
                List<TariffStructure> tarifs = tariffStructureMapper.getAllByPlant(line.getPlant().getId());
                String tableNameIndex = CustomLineTable.getTableIndexName(line, file.getId());
                Map map = new HashMap();
                map.put("data_id", idData);
                for (TariffStructure t : tarifs) {
                    Integer id = periodMapper.getIdByDateRange(line.getPlant().getId(),
                            t.getId(),
                            calendar.getTime());
                    if (id != null) {
                        map.put(t.getId(), id);
                    }
                }
                Integer idTurn = null;
                Calendary calendary = calendaryMapper.getTurnRangeTimeByDate(line, calendar.getTime());
                if (calendary != null) {
                    idTurn = calendary.getTurn().getId();
                    Depletion depletionTurn = depletionMapper.getByCalendary(calendary);
                    if (depletionTurn == null && isProduction) {
                        depletionTurn = new Depletion();
                        depletionTurn.setCalendaryId(calendary.getId());
                        depletionTurn.setCalendary(calendary);
                        depletionTurn.setQuantity(0.0);
                        depletionMapper.insert(depletionTurn, 1);
                    }
                }

                map.put("turn", idTurn);
                // TODO: change batch
                Variable batchVariable = variableMapper.getBatchVariable(line.getId());
                if (batchVariable != null) {// Si la data actual de la linea actual tiene asignada una variable de lote
                    VariableContinuing varTemp = variableMapper.getContinuingById(batchVariable.getId());
                    if (varTemp.getIdFile() == file.getId()) {// Si se esta leyendo el archivo donde esta la variable
                        tableName = CustomLineTable.getTableName(line, varTemp.getIdFile());
                        String idBefore = tableMapper.getFieldByBeforeCreate("id", calendar.getTime(), tableName);// id
                                                                                                                  // del
                                                                                                                  // dato
                                                                                                                  // insertado
                                                                                                                  // anteriormente
                        Double idLote = null;
                        if (idBefore != null) {// si hay un dato anterior
                            idLote = getIndexById("batch", idBefore, tableNameIndex);// se obtiene el id del lote
                                                                                     // producido para el dato anterior
                        }
                        map.put("batch", idLote);
                    }
                } else {
                    insertIndexBacthByDefault(idData, map, line, file, calendar.getTime(), tableNameIndex,
                            mapDataWith0);
                }
                String idIndex = tableMapper.getFieldByOtherField("data_id", idData, "data_id", tableNameIndex);
                if (idIndex != null) {
                    tableMapper.update(tableNameIndex, map, "data_id", idIndex);
                } else {
                    tableMapper.insert(tableNameIndex,
                            new ArrayList(map.keySet()),
                            new ArrayList(map.entrySet()));
                }
                // Data continua mezclada con la fija
                Map<Integer, Double> mapDataWithFixed = new HashMap<>();
                mapDataWithFixed.putAll(mapDataWith0);
                mapDataWithFixed.putAll(memoryRedisService.getFixedByCompany(line.getPlant().getUserCompany()));
                // retorna solo la data mixta
                // WriteLog.logger.write("logInsert.txt","inicio de derivadas");
                Map<Integer, Double> mapDerived = new HashMap<>();
                if (memoryVariablesLine.getDerives().size() != 0) {
                    String tableNameDerived = memoryVariablesLine.getDerives().get(0).getTable().getName();
                    String id = tableMapper.getFieldByCreate("id", calendar.getTime(), tableNameDerived);
                    memoryVariablesLine.getDerives().forEach(d -> {
                        VariableDerivedDTO dto = new VariableDerivedDTO(d);
                        FormulaDerivedDTO formula = dto.getFormula();
                        Double valueDerived = null, value1, value2 = null;

                        switch (formula.getType()) {
                            case 1:
                                value1 = getValue(formula.getVar1().getId(), mapDataWithFixed);
                                value2 = getValue(formula.getVar2().getId(), mapDataWithFixed);
                                valueDerived = variableDerivedUtil.operar(value1, formula.getOperation(), value2);
                                break;
                            case 2:
                                value1 = getValue(formula.getVar1().getId(), mapDataWithFixed);
                                value2 = getValue(formula.getVar2().getId(), memoryVariablesLine.getData());
                                if (value2 == null)
                                    valueDerived = new Double(0);
                                else {
                                    if (value1 < value2)
                                        valueDerived = value1;
                                    else
                                        valueDerived = variableDerivedUtil.operar(value1, formula.getOperation(),
                                                value2);
                                }
                                break;
                            case 3:
                                value1 = getValue(formula.getVar1().getId(), mapDataWithFixed);
                                value2 = getValue(formula.getVar2().getId(), mapDataWithFixed);
                                int operador = variableDerivedUtil.comparar(value1, formula.getOperation(), value2)
                                        ? formula.getIF().getThen()
                                        : formula.getIF().getELSE();
                                if (operador == 11) {// valor
                                    valueDerived = formula.getIF().getValue();
                                } else if (operador == 12) {// operacion
                                    valueDerived = getValue(formula.getIF().getVariable().getId(), mapDataWithFixed);
                                } else {
                                    valueDerived = variableDerivedUtil.operar(value1, operador, value2);
                                }
                                break;
                            case 4:
                                value1 = getValue(formula.getVar1().getId(), mapDataWithFixed);
                                String tableIndexName = d.getTable().getIndex();
                                if (formula.getOption() == 1)// velociadad nominarl
                                    value2 = getIndexById("rated_speed", idData, tableIndexName);
                                else if (formula.getOption() == 2)// factor de conversion;
                                    value2 = getIndexById("measure_per_unit", idData, tableIndexName);
                                valueDerived = variableDerivedUtil.operar(value1, formula.getOperation(), value2);
                                break;
                        }
                        valueDerived = round(valueDerived, 4);
                        mapDerived.put(d.getId(), valueDerived);
                        mapDataWithFixed.put(d.getId(), valueDerived);
                    });

                    if (id != null) {
                        tableMapper.update(tableNameDerived,
                                map, "id",
                                idData);
                    } else {
                        List headersDerived = new ArrayList();
                        headersDerived.add("id");
                        headersDerived.add("create");
                        headersDerived.addAll(mapDerived.keySet());
                        List valuesDerived = new ArrayList();
                        valuesDerived.add(idData);
                        valuesDerived.add(calendar.getTime());
                        valuesDerived.addAll(mapDerived.values());
                        tableMapper.insert(tableNameDerived, headersDerived, valuesDerived);
                    }
                }
                mapDataWith0.putAll(mapDerived);
                mapDataByLine.put(line.getId(), mapDataWith0);
            }
            mapDataByLine.forEach((i, j) -> redisMemoryFile.getMemoryByLine().get(i).setData(j));
            redisMemoryFile.setTime(calendar.getTime());
        }

    }

    private void mappingMemoryContinousKeyAndInputContinousValue(List<VariableContinuing> variableContinuingsInMemory,
            DataSendDTO dataDTO, Map<Integer, Double> map,
            Map<Integer, String> idToColumnName) {

        Map<String, Integer> positionInDTO = getPositionDTO(dataDTO.getHead());

        for (int i = 0; i < variableContinuingsInMemory.size(); i++) {
            Variable memoryContinuousVar = variableContinuingsInMemory.get(i);
            if (memoryContinuousVar == null) {
                throw new RuntimeException("Variable nula en la posicion " + i + " del archivo " + dataDTO.getCode());
            }
            if (memoryContinuousVar.getStatus() == 1) {
                if (!idToColumnName.containsKey(memoryContinuousVar.getId())) {
                    throw new RuntimeException("Columnname no se econtró con el id de variable "
                            + memoryContinuousVar.getId() + " del archivo " + dataDTO.getCode());
                }
                String columnName = idToColumnName.get(memoryContinuousVar.getId());
                if (!positionInDTO.containsKey(columnName)) {
                    throw new RuntimeException("No se econtró con el nombre de variable " + columnName + " del archivo "
                            + dataDTO.getCode());
                }
                int pos = positionInDTO.get(columnName);
                if (dataDTO.getData().get(pos) == null)
                    throw new RuntimeException("data nula en la posicion " + i + " del archivo " + dataDTO.getCode());
                map.put(memoryContinuousVar.getId(), Double.valueOf(dataDTO.getData().get(pos)));
            }
        }
    }

    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private Double getValue(int idVariable, Map<Integer, Double> map) {
        if (map.containsKey(idVariable))
            return map.get(idVariable);
        else
            return 0d;
    }

    private Double getIndexById(String idIndex, String idData, String tableIndex) {
        String value = "";
        value = tableMapper.getFieldByOtherField(idIndex, idData, "data_id", tableIndex);
        return value == null ? new Double(0) : Double.parseDouble(value);

    }

    /**
     * Calcula las derivadas
     * 
     * @param idData
     * @param time
     * @param memoryVariablesLine
     * @param mapData
     * @return
     */
    private Map<Integer, Double> insertDerived(String idData, Date time, MemoryVariablesLine memoryVariablesLine,
            Map<Integer, Double> mapData) {
        // WriteLog.logger.write("logInsert.txt","inicio de derivadas");
        Map<Integer, Double> map = new HashMap<>();
        if (memoryVariablesLine.getDerives().size() == 0)
            return map;
        String tableNameDerived = memoryVariablesLine.getDerives().get(0).getTable().getName();
        String id = tableMapper.getFieldByCreate("id", time, tableNameDerived);
        memoryVariablesLine.getDerives().forEach(d -> {
            VariableDerivedDTO dto = new VariableDerivedDTO(d);
            FormulaDerivedDTO formula = dto.getFormula();
            Double valueDerived = null, value1 = null, value2 = null;
            switch (formula.getType()) {
                case 1:
                    value1 = getValue(formula.getVar1().getId(), mapData);
                    value2 = getValue(formula.getVar2().getId(), mapData);
                    valueDerived = variableDerivedUtil.operar(value1, formula.getOperation(), value2);
                    break;
                case 2:
                    value1 = getValue(formula.getVar1().getId(), mapData);
                    value2 = getValue(formula.getVar2().getId(), memoryVariablesLine.getData());
                    if (value2 == null)
                        valueDerived = new Double(0);
                    else {
                        if (value1 < value2)
                            valueDerived = value1;
                        else
                            valueDerived = variableDerivedUtil.operar(value1, formula.getOperation(), value2);
                    }
                    if (dto.getId() == 1545) {
                        logger.info("Variable 1545 (" + time.getTime() + ")" + ", Valor redis: "
                                + ((value2 == null) ? "null" : value2) + ", Valor entrante: " + value1 + ", Resultado: "
                                + valueDerived);
                    }

                    break;
                case 3:
                    value1 = getValue(formula.getVar1().getId(), mapData);
                    value2 = getValue(formula.getVar2().getId(), mapData);
                    int operador = variableDerivedUtil.comparar(value1, formula.getOperation(), value2)
                            ? formula.getIF().getThen()
                            : formula.getIF().getELSE();
                    if (operador == 11) {// valor
                        valueDerived = formula.getIF().getValue();
                    } else if (operador == 12) {// operacion
                        valueDerived = getValue(formula.getIF().getVariable().getId(), mapData);
                    } else {
                        valueDerived = variableDerivedUtil.operar(value1, operador, value2);
                    }
                    break;
                case 4:
                    value1 = getValue(formula.getVar1().getId(), mapData);
                    String tableIndexName = d.getTable().getIndex();
                    if (formula.getOption() == 1)// velociadad nominarl
                        value2 = getIndexById("rated_speed", idData, tableIndexName);
                    else if (formula.getOption() == 2)// factor de conversion;
                        value2 = getIndexById("measure_per_unit", idData, tableIndexName);
                    valueDerived = variableDerivedUtil.operar(value1, formula.getOperation(), value2);
                    break;
            }
            valueDerived = round(valueDerived, 4);
            map.put(d.getId(), valueDerived);
            mapData.put(d.getId(), valueDerived);
        });

        boolean isUpdate = false;
        if (id != null) {
            tableMapper.update(tableNameDerived,
                    map, "id",
                    idData);
            isUpdate = true;
        } else {
            List header = new ArrayList();
            header.add("id");
            header.add("create");
            header.addAll(map.keySet());
            List values = new ArrayList();
            values.add(idData);
            values.add(time);
            values.addAll(map.values());
            tableMapper.insert(tableNameDerived, header, values);
        }

        return map;
    }

    /**
     * Inserta los valores para la tabla index
     * 
     * @param idData
     * @param line
     * @param file
     * @param time
     * @param idToColumnName
     * @param mapData
     * @param memoryVariablesLine
     */
    private void insertIndex(String idData, Line line, Files file, Date time, Map<Integer, String> idToColumnName,
            Map<Integer, Double> mapData, MemoryVariablesLine memoryVariablesLine, boolean isProduction) {
        // WriteLog.logger.write("logInsert.txt","inicio de indexación");
        List<TariffStructure> tarifs = tariffStructureMapper.getAllByPlant(line.getPlant().getId());
        String tableNameIndex = CustomLineTable.getTableIndexName(line, file.getId());
        Map map = new HashMap();
        map.put("data_id", idData);
        for (TariffStructure t : tarifs) {
            Integer id = periodMapper.getIdByDateRange(line.getPlant().getId(),
                    t.getId(),
                    time);
            if (id != null) {
                map.put(t.getId(), id);
            }
        }
        Integer idTurn = null;

        Calendary calendary = calendaryMapper.getTurnRangeTimeByDate(line, time);
        if (calendary != null) {
            idTurn = calendary.getTurn().getId();
            Depletion depletionTurn = depletionMapper.getByCalendary(calendary);
            if (depletionTurn == null && isProduction) {
                depletionTurn = new Depletion();
                depletionTurn.setCalendaryId(calendary.getId());
                depletionTurn.setCalendary(calendary);
                depletionTurn.setQuantity(0.0);

                depletionMapper.insert(depletionTurn, 1);

            }
        }

        map.put("turn", idTurn);

        // TODO: change batch
        Variable batchVariable = variableMapper.getBatchVariable(line.getId());
        if (batchVariable != null) {// Si la data actual de la linea actual tiene asignada una variable de lote
            VariableContinuing varTemp = variableMapper.getContinuingById(batchVariable.getId());
            if (varTemp.getIdFile() == file.getId()) {// Si se esta leyendo el archivo donde esta la variable
                String tableName = CustomLineTable.getTableName(line, varTemp.getIdFile());
                String idBefore = tableMapper.getFieldByBeforeCreate("id", time, tableName);// id del dato insertado
                                                                                            // anteriormente
                Double idLote = null;
                if (idBefore != null) {// si hay un dato anterior
                    idLote = getIndexById("batch", idBefore, tableNameIndex);// se obtiene el id del lote producido para
                                                                             // el dato anterior
                    if (idLote != null && idLote != 0) {// si existe un lote previo
                        Double tiempoAnterior = getValue(batchVariable.getId(), memoryVariablesLine.getData());
                        Double tiempoAhora = getValue(batchVariable.getId(), mapData);
                        // logger.debug("Variable Id: "+batchVariable.getId()+", Tiempo Anterior:
                        // "+tiempoAnterior+", Tiempo Ahora"+tiempoAhora );
                        if (tiempoAhora < tiempoAnterior) {// Si el tiempo del lote actual a disminuido hubo cambio de
                                                           // lote
                            Batch batchAnterior = batchMapper.getById(idLote.intValue());
                            if (batchAnterior != null) {
                                batchAnterior.setEnded(time);// Se termina el lote anterior
                                batchMapper.update(batchAnterior, 1);
                            }
                            // logger.debug("Fin de lote ID: "+idLote+", LineId: "+line.getId()+" Time:
                            // "+time);
                            if (tiempoAhora > 0) { // Si el tiempo disponible del lote es mayor a 0 se crea un nuevo
                                                   // lote
                                Batch batch = new Batch();
                                batch.setStarted(time);
                                batch.setLine(line);
                                batchMapper.insert(batch, 1);
                                // logger.debug("Nuevo-lote-ID: "+idLote+", Line-Id: "+line.getId()+" Time:
                                // "+time);
                                idLote = new Double(batch.getId());
                            }
                        }
                    } else {
                        Double tiempoAhora = getValue(batchVariable.getId(), mapData);
                        if (tiempoAhora > 0) { // Si el tiempo disponible del lote es mayor a 0 se crea un nuevo lote
                            Batch batch = new Batch();
                            batch.setStarted(time);
                            batch.setLine(line);
                            batchMapper.insert(batch, 1);
                            idLote = new Double(batch.getId());
                            // logger.debug("Nuevo lote-ID: "+idLote+", Line Id: "+line.getId()+" Time:
                            // "+time);
                        }
                    }
                } else {// Si no hay un dato anterior se crea un lote que inicie ahora
                    Batch batch = new Batch();
                    batch.setStarted(time);
                    batch.setLine(line);
                    batchMapper.insert(batch, 1);
                    idLote = new Double(batch.getId());
                    // logger.debug("Nuevo lote ID: "+idLote+", Line Id: "+line.getId()+" Time:
                    // "+time);
                }
                map.put("batch", idLote);

                List<Variable> variables = productMapper.getAllCharacteristicsByLine(line.getId());
                Map<Variable, Double> charact = new HashMap<>();
                for (Variable var : variables) {
                    Double value = mapData.get(var.getId());
                    charact.put(var, value);
                }
                String tableProduct = CustomLineTable.getTableProductName(line);
                Product product = productMapper.getByCharacteristics(charact, tableProduct);
                if (product != null) {
                    map.put("rated_speed", product.getRatedSpeed());
                    map.put("measure_per_unit", product.getMeasurePerUnit());
                    map.put("product_id", product.getId());
                }
            }
        } else {

            insertIndexBacthByDefault(idData, map, line, file, time, tableNameIndex, mapData);
        }
        String idIndex = tableMapper.getFieldByOtherField("data_id", idData, "data_id", tableNameIndex);
        if (idIndex != null) {
            tableMapper.update(tableNameIndex, map, "data_id", idIndex);
        } else {
            tableMapper.insert(tableNameIndex,
                    new ArrayList(map.keySet()),
                    new ArrayList(map.entrySet()));
        }
    }

    /**
     * Insert los indices de acuerdo la configuración de lotes por defecto de la
     * tablet
     * 
     * @param idData
     * @param map
     * @param line
     * @param file
     * @param time
     * @param tableNameIndex
     */
    // TODO: change batch
    private void insertIndexBacthByDefault(String idData, Map map, Line line, Files file, Date time,
            String tableNameIndex, Map mapData) {
        List<Variable> variables = variableMapper.getVariablesByTypeFile(line.getId(),
                VariableTypeEnum.CONTEO_UNITARIO.getCode());
        if (variables.size() > 0) {
            int fileIdProduct = 0;
            if (variables.get(0).getType().equals(Variable.TYPE_CONTINUING)) {
                fileIdProduct = variableMapper.getContinuingById(variables.get(0).getId()).getIdFile();
            } else if (variables.get(0).getType().equals(Variable.TYPE_DERIVED)) {
                fileIdProduct = variableMapper.getDerivedById(variables.get(0).getId()).getIdFile();
            }
            if (fileIdProduct == file.getId()) {
                String tableName = CustomLineTable.getTableName(line, file.getId());
                String idBefore = tableMapper.getFieldByBeforeCreate("id", time, tableName);// id del dato insertado
                                                                                            // anteriormente
                Map mapUpdateContinuing = new HashMap();

                Double idLote = null;
                if (idBefore != null) {// si hay un dato anterior
                    idLote = getIndexById("batch", idBefore, tableNameIndex);// se obtiene el id del lote producido para
                                                                             // el dato anterior
                    if (idLote != null && idLote != 0) {// si existe un lote previo
                        // logger.debug("Variable Id: " + variables.get(0).getId());
                        Batch batchAnterior = batchMapper.getById(idLote.intValue());
                        if (batchAnterior != null
                                && (batchAnterior.getEnded() == null || batchAnterior.getEnded().after(time))) {
                            idLote = new Double(batchAnterior.getId());
                            Double idBeforeProduct = getIndexById("product_id", idBefore, tableNameIndex);
                            if (idBeforeProduct != null && idBeforeProduct != 0) {
                                String tableProduct = CustomLineTable.getTableProductName(line);
                                Map product = productMapper.getProductFeatureById(tableProduct,
                                        idBeforeProduct.intValue());
                                if (product != null) {
                                    map.put("product_id", product.get("id"));
                                    map.put("rated_speed", product.get("rated_speed"));
                                    map.put("measure_per_unit", product.get("measure_per_unit"));
                                    product.keySet().forEach(key -> {
                                        if (!key.equals("measure_per_unit") && !key.equals("user_id")
                                                && !key.equals("rated_speed")) {
                                            if (isNumeric(key.toString())) {
                                                mapUpdateContinuing.put(key, product.get(key));
                                            }
                                        }
                                    });
                                }

                            }
                        } else {
                            Batch batch = new Batch();
                            batch.setStarted(time);
                            batch.setLine(line);
                            batchMapper.insert(batch, 1);
                            idLote = new Double(batch.getId());
                            // logger.debug("Nuevo lote ID: " + idLote + ", Line Id: " + line.getId() + "
                            // Time: " + time);
                        }
                    } else {
                        Batch batch = new Batch();
                        batch.setStarted(time);
                        batch.setLine(line);
                        batchMapper.insert(batch, 1);
                        idLote = new Double(batch.getId());
                        // logger.debug("Nuevo lote ID: " + idLote + ", Line Id: " + line.getId() + "
                        // Time: " + time);
                    }
                } else {// Si no hay un dato anterior se crea un lote que inicie ahora
                    Batch batch = new Batch();
                    batch.setStarted(time);
                    batch.setLine(line);
                    batchMapper.insert(batch, 1);
                    idLote = new Double(batch.getId());
                    // logger.debug("Nuevo lote ID: "+idLote+", Line Id: "+line.getId()+" Time:
                    // "+time);
                }
                map.put("batch", idLote);
                if (!map.containsKey("product_id")) {
                    String tableProduct = CustomLineTable.getTableProductName(line);
                    Map productDefault = productMapper.getProductFeatureDefaultByLine(tableProduct, line.getId());
                    if (productDefault != null) {
                        map.put("product_id", productDefault.get("id"));
                        map.put("rated_speed", productDefault.get("rated_speed"));
                        map.put("measure_per_unit", productDefault.get("measure_per_unit"));
                        productDefault.keySet().forEach(key -> {
                            if (!key.equals("measure_per_unit") && !key.equals("user_id")
                                    && !key.equals("rated_speed")) {
                                if (isNumeric(key.toString())) {
                                    mapUpdateContinuing.put(key, productDefault.get(key));
                                }
                            }
                        });
                    }
                }

                List<Variable> v = variableMapper.getVariablesByTypeFile(line.getId(),
                        VariableTypeEnum.T_DISPONIBLE.getCode());
                if (v != null) {
                    v.forEach(variable -> {
                        mapUpdateContinuing.put(variable.getId(), new Double(file.getSendFrequency()));
                    });
                }
                if (mapUpdateContinuing.size() > 0) {
                    tableMapper.update(tableName,
                            mapUpdateContinuing, "id",
                            idData);
                    mapData.putAll(mapUpdateContinuing);
                }
            }

        }

    }

    public static boolean isNumeric(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }

    @Override
    public String greetingsUser(String gretting) {
        String hello = "Hello ";
        return hello.concat(gretting);
    }

}
