package pe.mm.reception.core.service;

import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.mm.reception.common.ValidationException;
import pe.mm.reception.core.dto.*;
import pe.mm.reception.core.mapper.*;
import pe.mm.reception.core.model.*;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class MemoryRedisServiceImpl implements MemoryRedisService {

    @Autowired
    private UserCompanyService userCompanyService;

    @Autowired
    private VariableMapper variableMapper;

    @Autowired
    private PlantStatusMapper plantStatusMapper;

    @Autowired
    private FilesMapper filesMapper;

    @Autowired
    private LineMapper lineMapper;

    @Autowired
    private TableMapper tableMapper;

    @Autowired
    private PlantService plantService;

    @Autowired
    RedissonClient redissonClient;

    RMap<String, Integer> mapAllFiles = null;

    // RMap<String, Integer> mapCompanies=null;
    @PostConstruct
    private void postConstruct() {
        this.mapAllFiles = redissonClient.getMap("mapAllFiles");
    }

    private RLock getMemoryMaintenanceLock() {
        return redissonClient.getLock("memoryMaintenance");
    }

    @Override
    public void loadAllCompanyMemory() {
        if (!this.isAvailableMemory())
            throw new ValidationException("La memoria esta en manteniento");

        getMemoryMaintenanceLock().tryLock();
        try {
            List<CompanyBasic> companies = userCompanyService.getAllCompany();
            for (CompanyBasic u : companies) {
                loadACompany(u);
            }
        } catch (Exception e) {
            throw new ValidationException(e);
        } finally {
            try {
                getMemoryMaintenanceLock().unlock();
            } catch (Exception e) {

            }
        }

    }

    @Override
    public Map<Integer, Double> getVariableFixedByIdCompany(int idCompany) {
        RMap<Integer, Double> mapFixed = redissonClient.getMap("mapFixed:" + idCompany);
        return mapFixed;
    }

    @Override
    public void loadACompany(CompanyBasic u) throws InterruptedException {
        System.out.println("load company id " + u.getId());
        List<Files> files = filesMapper.getAllByCompany(u.getId(), null, null);
        List<Files> filesOcuped = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<Files> filesInUse = new ArrayList<>();
            files.forEach(f -> {
                if (this.isFileIsProcessing(f.getName())) {
                    filesInUse.add(f);
                } else {
                    filesOcuped.add(f);
                    this.updateMemoryStatusProcessingRedis(true, f.getName());

                }
            });
            files = filesInUse;
            if (files.size() == 0) {
                break;
            } else {
                Thread.sleep(100);
            }
        }
        if (files.size() > 0) {
            filesOcuped.forEach(files1 -> this.updateMemoryStatusProcessingRedis(false, files1.getName()));
            throw new ValidationException("No se pudo liberar todos los archivos");
        }

        RMap<Integer, Double> mapFixed = redissonClient.getMap("mapFixed:" + u.getId());
        mapFixed.delete();
        variableMapper.getAllFixed(u.getId()).forEach(v -> {
            mapFixed.put(v.getId(), v.getValue());
            System.out.println("variable Id:  " + v.getId());
        });

        List<MemoryForVariablesMixed> memoryForMixes = variableMapper.getAllDerivedMixByCompany(u.getId());
        memoryForMixes.forEach(memoryForMix -> {
            String table = memoryForMix.getTableName().getName();
            List<Map<String, Object>> list = tableMapper
                    .getAllFieldByOtherField("(select max(`create`) from " + table + ")", "`create`", table);
            if (list.size() > 0)
                memoryForMix.setTime((Date) list.get(list.size() - 1).get("create"));
        });

        plantService.getAllByCompany(u.getId()).forEach(plant -> {

            RMap<String, Object> memoryPlantStatus = redissonClient.getMap("plantStatus:" + plant.getId());

            PlantStatus plantStatus = plantStatusMapper.getStatusByPlant(plant);

            if (plantStatus == null) {
                plantStatus = new PlantStatus();
                plantStatus.setPlant(plant);
                plantStatus.setStatus(1);
                plantStatusMapper.insertStatus(plantStatus, 1);

            }
            memoryPlantStatus.put("name", plant.getName());
            memoryPlantStatus.put("status", plantStatus.getStatus());
            memoryPlantStatus.put("timeZone", plant.getTimeZone());
            memoryPlantStatus.put("companyId", plant.getUserCompany().getId());

            for (Files f : filesMapper.getAllByPlant(plant.getId(), null, null)) {
                RMap<String, Object> memoryFileStatus = redissonClient.getMap("fileStatus:" + f.getName());
                memoryFileStatus.put("id", f.getId());
                memoryFileStatus.put("plantId", plant.getId());
                if (f.getSecondName() != null) {
                    memoryFileStatus.put("secondNameFile", f.getSecondName());
                }
                memoryFileStatus.put("processing", false);
                memoryFileStatus.put("totalCount", 0);
                memoryFileStatus.put("sendFrecuency", f.getSendFrequency());

                List<Integer> idLinesNoTables = new ArrayList<>();

                // Se arma la estrucutura de variables continuas por linea en cada archivo

                Memory memory = new Memory();
                memory.setMemoryByLine(new HashMap<>());
                memory.setFile(f);
                memory.setIdToColumnName(new HashMap<>());
                variableMapper.getAllContinuingByFile(f.getName()).forEach(v -> {

                    Line line = v.getDevice().getLocation().getLine();

                    if (!memory.getMemoryByLine().containsKey(line.getId())
                            && !idLinesNoTables.contains(line.getId())) {
                        line = lineMapper.getById(line.getId());
                        if (line.getStatusTables() == 1) {
                            MemoryVariablesLine memoryVariablesLine = new MemoryVariablesLine();
                            memoryVariablesLine.setLine(line);
                            memoryVariablesLine.setDerives(new ArrayList<>());
                            memoryVariablesLine.setContinuing(new ArrayList<>());
                            memoryVariablesLine.setData(new HashMap<>());
                            memory.getMemoryByLine().put(line.getId(), memoryVariablesLine);
                        } else {
                            idLinesNoTables.add(line.getId());
                        }
                    }
                    if (!idLinesNoTables.contains(line.getId())) {
                        memory.getMemoryByLine().get(line.getId()).getContinuing().add(v);
                        memory.getIdToColumnName().put(v.getId(), memory.convertToFormat(v.getColumn()));
                        memory.setTotalCount(memory.getTotalCount() + 1);
                    }

                });
                // aca añado cosa fea
                memory.getMemoryByLine().forEach((i, memoryVariableLine) -> {
                    memoryVariableLine.getDerives().addAll(variableMapper.getAllDerivedByLineFile(i, f.getId()));
                    String tableName = CustomLineTable.getTableName(memoryVariableLine.getLine(), f.getId());
                    setParentsToMixes(tableName, memory, memoryVariableLine, memoryForMixes);
                    memoryVariableLine.getData()
                            .putAll(getLastValues(tableName, memoryVariableLine.getContinuing(), memory));
                    if (memoryVariableLine.getDerives().size() > 0) {
                        tableName = memoryVariableLine.getDerives().get(0).getTable().getName();
                        setParentsToMixes(tableName, memory, memoryVariableLine, memoryForMixes);
                        memoryVariableLine.getData().putAll(getLastValues(tableName,
                                memoryVariableLine.getDerives(), memory));
                    }
                });
                RMap<String, Object> memoryByFile = redissonClient.getMap("memoryByFile:" + f.getName());
                memoryByFile.put("time", memory.getTime() == null ? 0 : memory.getTime().getTime());
                memoryByFile.put("memoryByLine", memory.getMemoryByLine());
                memoryByFile.put("idToColumnName", memory.getIdToColumnName());
                memoryFileStatus.put("totalCount", memory.getTotalCount());

                RSemaphore s = redissonClient.getSemaphore("fileSemaphore:" + f.getName());
                s.delete();
                s.trySetPermits(1);
                System.out.println("fileName: " + f.getName());
                System.out.println("plantId: " + plant.getId());
            }
        });
        /**/
        redissonClient.getList("memoryMix:" + u.getId()).delete();
        memoryForMixes.forEach(memoryForVariablesMixed -> {
            if (memoryForVariablesMixed.getMemory1() == null)
                for (MemoryForVariablesMixed otherMemoryForVariablesMixed : memoryForMixes)
                    if (otherMemoryForVariablesMixed.getTableName().getName()
                            .equals(memoryForVariablesMixed.getTableName().getParent1())) {
                        memoryForVariablesMixed.setMemory1(
                                "memoryMix:" + u.getId() + ":" + otherMemoryForVariablesMixed.getIdTableName());
                        break;
                    }
            if (memoryForVariablesMixed.getMemory2() == null)
                for (MemoryForVariablesMixed otherMemoryForVariablesMixed : memoryForMixes)
                    if (otherMemoryForVariablesMixed.getTableName().getName()
                            .equals(memoryForVariablesMixed.getTableName().getParent2())) {
                        memoryForVariablesMixed.setMemory2(
                                "memoryMix:" + u.getId() + ":" + otherMemoryForVariablesMixed.getIdTableName());
                        break;
                    }
            RMap<String, Object> mapMixtasR = redissonClient
                    .getMap("memoryMix:" + u.getId() + ":" + memoryForVariablesMixed.getIdTableName());
            mapMixtasR.put("time",
                    memoryForVariablesMixed.getTime() == null ? 0 : memoryForVariablesMixed.getTime().getTime());
            mapMixtasR.put("mem", memoryForVariablesMixed);
        });

    }

    public Map<String, MemoryForVariablesMixed> getMemoriesMixByIdCompany(int idCompany) {
        Map<String, MemoryForVariablesMixed> map = new HashMap<>();
        redissonClient.getKeys().getKeysByPattern("memoryMix:" + idCompany + ":*").forEach(fileKey -> {
            RMap<String, Object> mapMixtasR = redissonClient.getMap(fileKey);
            if (mapMixtasR != null) {
                MemoryForVariablesMixed memoryForVariablesMixed = (MemoryForVariablesMixed) mapMixtasR.get("mem");
                map.put(fileKey, memoryForVariablesMixed);
            }
        });
        return map;
    }

    public Date getDateOfMemoryMix(String key) {
        Long date = (Long) redissonClient.getMap(key).get("time");
        return (date == null || date == 0) ? null : new Date(date);
    }

    private void setParentsToMixes(String tableName, Memory memory, MemoryVariablesLine memoryVariableLine,
            List<MemoryForVariablesMixed> memoryForMixes) {
        final String finalTableName = tableName;
        memoryForMixes.forEach(memoryForMix -> {
            if (memoryForMix.getMemory1() == null && memoryForMix.getTableName().getParent1().equals(finalTableName)) {
                memoryForMix.setMemory1("memoryByFile:" + memory.getFile().getName());
            }
            if (memoryForMix.getMemory2() == null && memoryForMix.getTableName().getParent2().equals(finalTableName)) {
                memoryForMix.setMemory2("memoryByFile:" + memory.getFile().getName());
            }
        });
    }

    private Map<Integer, Double> getLastValues(String tableName, List variables, Memory memory) {
        List<Map<String, Object>> list = tableMapper
                .getAllFieldByOtherField("(select max(`create`) from " + tableName + ")", "`create`", tableName);
        Map<Integer, Double> map = new HashMap<>();
        if (list != null && list.size() > 0) {
            variables.forEach(v -> {
                int id = ((Variable) v).getId();
                if (list.get(list.size() - 1).containsKey(id + ""))
                    map.put(id, Double.valueOf((String) list.get(list.size() - 1).get(id + "")));
            });
            if (memory.getTime() == null) {
                memory.setTime((Date) list.get(list.size() - 1).get("create"));
            } else if (memory.getTime().getTime() < ((Date) list.get(list.size() - 1).get("create")).getTime())
                memory.setTime((Date) list.get(list.size() - 1).get("create"));
        }
        return map;
    }

    @Override
    public boolean isAvailableMemory() {
        if (this.getMemoryMaintenanceLock().isLocked())
            return false;
        else {
            // inUse++;
            // mantainance=true;
            return true;
            /*
             * if (memoryMap.size() == 0) {
             * reload();
             * return false;
             * } else {
             * inUse++;
             * return true;
             * }/
             **/
        }
    }

    @Override
    public Memory getFileStatus(String code) {
        Memory memory = new Memory();
        RMap<String, Object> memoryFileStatus = redissonClient.getMap("fileStatus:" + code);
        if (memoryFileStatus.get("id") == null)
            return null;
        memory.setFile(new Files());
        memory.getFile().setId((Integer) memoryFileStatus.get("id"));
        memory.getFile().setName(code);
        memory.getFile().setSecondName((String) memoryFileStatus.get("secondNameFile"));
        memory.getFile().setSendFrequency((Integer) memoryFileStatus.get("sendFrecuency"));
        memory.getFile().setPlant(new Plant());
        memory.getFile().getPlant().setId((Integer) memoryFileStatus.get("plantId"));
        memory.setProcessing((Boolean) memoryFileStatus.get("processing"));
        memory.setTotalCount((Integer) memoryFileStatus.get("totalCount"));
        return memory;
    }

    @Override
    public Memory getFileMemory(String code) {
        Memory memory = getFileStatus(code);
        RMap<String, Object> memoryFileStatus = redissonClient.getMap("memoryByFile:" + code);
        Long time = (Long) memoryFileStatus.get("time");
        if (time == null)
            return null;
        memory.setTime(time == 0 ? null : new Date(time));
        memory.setIdToColumnName((Map<Integer, String>) memoryFileStatus.get("idToColumnName"));
        memory.setMemoryByLine((Map<Integer, MemoryVariablesLine>) memoryFileStatus.get("memoryByLine"));
        return memory;
    }

    @Override
    public boolean isPlantStatusEnable(Integer plantId) {
        RMap<String, Object> memoryPlantStatus = redissonClient.getMap("plantStatus:" + plantId);
        if (memoryPlantStatus.get("status") == null)
            return false;
        return (Integer) memoryPlantStatus.get("status") == 1;
    }

    @Override
    public boolean isFileIsProcessing(String code) {
        RSemaphore s = redissonClient.getSemaphore("fileSemaphore:" + code);
        return s.tryAcquire();
    }

    @Override
    public void updateMemoryStatusProcessingRedis(Boolean status, String code) {
        RMap<String, Object> memoryFileStatus = redissonClient.getMap("fileStatus:" + code);
        // RMap ayuda a manejar los datos de Redis como si fueran un Map
        // memoryFileStatus es un Map que contiene los datos de un archivo en Redis
        // code es el nombre del archivo
        if (memoryFileStatus.get("id") == null)
            return;
        // ESTA COndicion nos muestra si el archivo esta en uso o no
        memoryFileStatus.put("status", status);
        // status es un booleano que nos dice si el archivo esta en uso o no
        // memoryFileStatus.put("status",status) actualiza el valor de status en el Map
        // memoryFileStatus
        if (!status) {
            // si status es falso
            // quiere decir que el archivo no esta en uso
            RSemaphore s = redissonClient.getSemaphore("fileSemaphore:" + code);
            // s es un Semaphore que nos ayuda a manejar los datos de Redis como si fueran
            // un Semaphore
            // s es un Semaphore que contiene los datos de un archivo en Redis
            s.releaseAsync();
            // s.releaseAsync() libera el Semaphore s
        }
    }

    @Override
    public Map<Integer, Double> getFixedByCompany(UserCompany userCompany) {
        RMap<Integer, Double> memory = redissonClient.getMap("mapFixed:" + userCompany.getId());
        Map<Integer, Double> fixed = new HashMap<>();
        memory.forEach(fixed::put);
        return fixed;
    }

    @Override
    public void updateMemory(Memory fileMemory) {
        RMap<String, Object> memoryByFile = redissonClient.getMap("memoryByFile:" + fileMemory.getFile().getName());
        memoryByFile.put("time", fileMemory.getTime() == null ? 0 : fileMemory.getTime().getTime());
        memoryByFile.put("memoryByLine", fileMemory.getMemoryByLine());
    }

    @Override
    public Map getInfo() {
        Map<String, Object> root = new HashMap<>();
        root.put("mantenimiento", isAvailableMemory());
        ArrayList<String> fileStatus = new ArrayList();
        redissonClient.getKeys().getKeysByPattern("fileStatus:*").forEach(f -> {
            fileStatus.add(f);
        });
        root.put("fileStatus", fileStatus);
        ArrayList<String> plantStatus = new ArrayList();
        redissonClient.getKeys().getKeysByPattern("plantStatus:*").forEach(f -> {
            plantStatus.add(f);
        });
        root.put("plantStatus", plantStatus);
        return root;
    }

    @Override
    public Object getInfoOfKey(String key) {
        RType type = redissonClient.getKeys().getType(key);
        if (type == null) {
            throw new ValidationException("No se encontró el key: " + key);
        } else if (type == RType.MAP)
            return redissonClient.getMap(key);
        else
            return redissonClient.getSemaphore(key).availablePermits();
    }

    @Override
    public void setStatusPlantRedis(Plant plant, Integer status, int usermod) {

        RMap<String, Object> memoryPlantStatus = redissonClient.getMap("plantStatus:" + plant.getId());
        Integer statusBefore = (Integer) memoryPlantStatus.get("status");
        if (status.equals(statusBefore)) {
            throw new ValidationException("El nuevo estado es el mismo que el actual");
        }

        PlantStatus plantStatus = plantStatusMapper.getStatusByPlant(plant);
        if (plantStatus == null) {
            plantStatus = new PlantStatus();
            plantStatus.setPlant(plant);
            plantStatus.setStatus(0);
            plantStatusMapper.insertStatus(plantStatus, usermod);
            plantStatus = plantStatusMapper.getStatusByPlant(plant);
        }
        plantStatus.setPlant(plant);
        plantStatus.setStatus(status);
        plantStatusMapper.updateStatus(plantStatus, usermod);

        memoryPlantStatus.put("name", plantStatus.getPlant().getName());
        memoryPlantStatus.put("status", plantStatus.getStatus());
        memoryPlantStatus.put("timeZone", plantStatus.getPlant().getTimeZone());
        memoryPlantStatus.put("companyId", plant.getUserCompany().getId());

        StatusLog statusLog = plantStatusMapper.getLastLog(plantStatus);
        if (status == 0 && (statusLog == null || statusLog.getEnd() != null)) {
            statusLog = new StatusLog();
            statusLog.setPlantStatus(plantStatus);
            statusLog.setStart(new Date());
            plantStatusMapper.insertStatusLog(statusLog, usermod);
        } else {
            if (status == 1 && statusLog != null) {
                statusLog.setEnd(new Date());
                plantStatusMapper.updateStatusLog(statusLog, usermod);
            }
        }
    }

    @Override
    public PlantStatusDTO getStatusPlant(Plant plant) {
        RMap<String, Object> memoryPlantStatus = redissonClient.getMap("plantStatus:" + plant.getId());
        PlantStatusDTO dto = new PlantStatusDTO();
        dto.setPlant(new PlantEssentialDTO(plant));
        Integer status = (Integer) memoryPlantStatus.get("status");
        if (status == null) {
            status = 0;
        }
        dto.setStatus(status);
        return dto;
    }
}
