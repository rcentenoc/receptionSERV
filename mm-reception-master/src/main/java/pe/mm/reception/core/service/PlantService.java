package pe.mm.reception.core.service;

import pe.mm.reception.common.Request;
import pe.mm.reception.core.dto.PlantEssentialDTO;
import pe.mm.reception.core.model.Plant;

import java.util.List;

public interface PlantService {
    List<Plant> getAllByCompany(int idCompany, Request.Pager pager);
    List<Plant> getAllByCompany(int idCompany);
    Plant getPlant(int id);
    Plant insert(Plant plant, int idUser);
    Plant update(Plant plant, int idUser);
    void delete(int id, int idUser);
    boolean verifySimilarName(Plant plant);
    boolean verifySimilarNameDistinctMe(Plant plant);
    void createTablesForPlant(int id);
    boolean existTable(String name);
    List<PlantEssentialDTO> getAllByCompanyEssential(int idCompany, Request.Pager pager);
}
