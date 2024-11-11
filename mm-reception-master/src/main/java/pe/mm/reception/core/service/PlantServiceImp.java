package pe.mm.reception.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.mm.reception.common.Request;
import pe.mm.reception.core.dto.PlantEssentialDTO;
import pe.mm.reception.core.mapper.PlantMapper;
import pe.mm.reception.core.model.Plant;

import java.util.ArrayList;
import java.util.List;
@Service
public class PlantServiceImp implements PlantService {

    @Autowired
    private PlantMapper plantMapper;

    @Override
    public List<Plant> getAllByCompany(int idCompany, Request.Pager pager) {
        return plantMapper.getAllByCompany(idCompany,pager.getStartIndex(), pager.getPageSize());
    }

    @Override
    public List<Plant> getAllByCompany(int idCompany) {
        return plantMapper.getAllByCompany(idCompany,null, null);
    }

    @Override
    public Plant getPlant(int id) {
        return plantMapper.getById(id);
    }

    @Override
    public Plant insert(Plant plant, int idUser) {
        plantMapper.insert(plant,idUser);
        return plant;
    }

    @Override
    public Plant update(Plant plant, int idUser) {
        plantMapper.update(plant,idUser);
        return plant;
    }

    @Override
    public void delete(int id, int idUser) {
        plantMapper.softDeleted(id,2,idUser);
    }

    @Override
    public boolean verifySimilarName(Plant plant) {
        List list = plantMapper.getAllByField("name", plant.getName(),plant.getUserCompany().getId());
        return list.size()>0;
    }

    @Override
    public boolean verifySimilarNameDistinctMe(Plant plant) {
        List<Plant> list = plantMapper.getAllByField("name", plant.getName(),plant.getUserCompany().getId());
        if(list.size()==1)
            return list.get(0).getId()!=plant.getId();
        return list.size()>0;
    }

    @Override
    public void createTablesForPlant(int id) {
        plantMapper.createTableForPlant(id);
    }

    @Override
    public boolean existTable(String name){
        List list =plantMapper.existNameTable(name);
        if(list!=null){
            return list.size()!=0;
        }
        return false;
    }

    @Override
    public List<PlantEssentialDTO> getAllByCompanyEssential(int idCompany,Request.Pager pager) {
        List<Plant>plantsList;
        if(pager==null)plantsList=plantMapper.getAllByCompany(idCompany,null, null);
        else plantsList=plantMapper.getAllByCompany(idCompany,pager.getStartIndex(), pager.getPageSize());

        List<PlantEssentialDTO>plantEssentialDTOList = new ArrayList<PlantEssentialDTO>();
        for(Plant plantEssencial: plantsList){
            PlantEssentialDTO plantEssentialDTO =new PlantEssentialDTO();
            plantEssentialDTO.setId(plantEssencial.getId());
            plantEssentialDTO.setName(plantEssencial.getName());
            plantEssentialDTO.setInformation(plantEssencial.getInformation());
            plantEssentialDTO.setTimeZone(plantEssencial.getTimeZone());
            plantEssentialDTO.setCreate(plantEssencial.getCreate());
            plantEssentialDTO.setUpdate(plantEssencial.getUpdate());
            plantEssentialDTOList.add(plantEssentialDTO);
        }
        return plantEssentialDTOList;
    }


}