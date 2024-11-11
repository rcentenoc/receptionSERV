package pe.mm.reception.core.service;


import pe.mm.reception.core.dto.Memory;
import pe.mm.reception.core.dto.PlantStatusDTO;
import pe.mm.reception.core.model.CompanyBasic;
import pe.mm.reception.core.model.MemoryForVariablesMixed;
import pe.mm.reception.core.model.Plant;
import pe.mm.reception.core.model.UserCompany;

import java.util.Date;
import java.util.Map;

public interface MemoryRedisService {

    void loadAllCompanyMemory();

    Map<Integer,Double> getVariableFixedByIdCompany(int idCompany);
    Map<String, MemoryForVariablesMixed> getMemoriesMixByIdCompany(int idCompany);
    Date getDateOfMemoryMix(String idMap);

    void loadACompany(CompanyBasic u) throws InterruptedException;

    boolean isAvailableMemory();

    Memory getFileStatus(String code);

    Memory getFileMemory(String code);

    boolean isPlantStatusEnable(Integer plantId);

    boolean isFileIsProcessing(String code);

    void updateMemoryStatusProcessingRedis(Boolean status, String fileName);

    Map<Integer, Double> getFixedByCompany(UserCompany userCompany);

    void updateMemory(Memory fileMemory);

    Map getInfo();

    Object getInfoOfKey(String key);

    void setStatusPlantRedis(Plant plant, Integer status, int usermod);

    PlantStatusDTO getStatusPlant(Plant plant);
}
