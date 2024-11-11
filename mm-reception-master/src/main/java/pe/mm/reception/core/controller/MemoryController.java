package pe.mm.reception.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pe.mm.reception.common.NotFoundException;
import pe.mm.reception.common.Request;
import pe.mm.reception.common.Response;
import pe.mm.reception.core.dto.Memory;
import pe.mm.reception.core.dto.PlantStatusDTO;
import pe.mm.reception.core.model.CompanyBasic;
import pe.mm.reception.core.model.MemoryStatus;
import pe.mm.reception.core.model.Plant;
import pe.mm.reception.core.model.UserCompany;
import pe.mm.reception.core.service.MemoryRedisService;
import pe.mm.reception.core.service.PlantService;
import pe.mm.reception.core.service.UserCompanyService;
import pe.mm.reception.security.model.AuthenticatedUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class MemoryController {

    @Autowired
    private MemoryRedisService memoryRedisService;

    @Autowired
    private PlantService plantService;
    @Autowired
    private UserCompanyService userCompanyService;
    //@Autowired
    //UrlCatalogService urlCatalogService;
    /**
     * Recarga la data cargada en memoria de todas las compañías.
     * @return
     */
    @RequestMapping(value = "/memories/reloads",method = RequestMethod.POST)
    public ResponseEntity reload() {
        memoryRedisService.loadAllCompanyMemory();
        return ResponseEntity.ok("reloading");
    }

    /**
     * Recarga la data cargada en memoria de 1 compañía.
     * @param body UserCompany in json.
     * @return
     * @throws IOException si los datos del cliente son erróneos.
     */
    @RequestMapping(value = "/memories/companies/reloads",method = RequestMethod.POST)
    public ResponseEntity reloadCompany(@RequestBody String body) throws IOException, InterruptedException {
        UserCompany userCompany = new Request<UserCompany>(body, UserCompany.class).getRequestObject();
        UserCompany temp = userCompanyService.getUserCompany(userCompany.getId());
        if(temp == null)
            throw new NotFoundException(userCompany.getId());
        CompanyBasic cp = new CompanyBasic();
        cp.setId(temp.getId());
        cp.setDenomination(temp.getDenomination());
        cp.setStatus(temp.getStatus());
        memoryRedisService.loadACompany(cp);
        return Response.ok(userCompany,"reloading",null);
    }

    /**
     * Obtiene la memoria usada para la recepción de data.
     * @return toda la memoria.
     */
    @RequestMapping(value = "/memories/info",method = RequestMethod.GET)
    public ResponseEntity memoryStatus(){
        return ResponseEntity.ok(memoryRedisService.getInfo());
    }

    /**
     * Obtiene la memoria usada para la recepción de data.
     * @return toda la memoria.
     */
    @RequestMapping(value = "/memories/info/{key}",method = RequestMethod.GET)
    public ResponseEntity memoryStatusKey(@PathVariable("key") String key){
        return ResponseEntity.ok(memoryRedisService.getInfoOfKey(key));
    }

    /**
     *  Actualiza el estado de mantenimiento de una planta
     * @param body Plant con el nuevo estado
     * @return
     * @throws IOException si la data envía es errónea.
     */
    @RequestMapping(value = "/memories/companies/{idCompany}/plants/status",method = RequestMethod.POST)
    public ResponseEntity plantStatusUpdate( @RequestBody  PlantStatusDTO plantStatusDTO,@PathVariable("idCompany") int idCompany ) throws IOException{
        System.out.println("plant Status Update by idCompany ....");
        Plant plant = plantService.getPlant(plantStatusDTO.getPlant().getId());
        if(plant == null)
            throw new NotFoundException(plantStatusDTO.getPlant().getId());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int usermod=1;
        if(auth!=null){
            AuthenticatedUser userSession = (AuthenticatedUser) auth.getPrincipal();
            usermod =userSession.getId();
        }
        memoryRedisService.setStatusPlantRedis(plant,plantStatusDTO.getStatus(),usermod);
        return Response.ok(plantStatusDTO, "Cambio realizado",null);
    }

    /**
     *  Obtiene el estado de mantenimiento de una planta.
     * @param idPlant de la planta a consultar.
     * @return planta con el estado de mantenimiento.
     * @throws IOException si los datos del cliente son erróneos.
     */
    @RequestMapping(value = "/memories/companies/{idCompany}/plants/{idPlant}/status",method = RequestMethod.GET)
    public ResponseEntity getPlantStatus( @PathVariable("idPlant") int idPlant,@PathVariable("idCompany") int idCompany) throws IOException {
        System.out.println("get Status of one plant by idCompany ....");
        Plant plant = plantService.getPlant(idPlant);
        if(plant == null)
            throw new NotFoundException(idPlant);
        //PlantStatusDTO dto = memoryService.getStatusPlant(plant);
        PlantStatusDTO dto = memoryRedisService.getStatusPlant(plant);
        return Response.ok(dto,"Consultado correctamente",null);
    }

    /**
     * Obtiene el estado de mantenimiento de las plantas en memoria de una compañía.
     * @param idCompany id de la compañía consultada.
     * @return lista de plantas con sus estados.
     * @throws IOException si los datos del cliente son erróneos.
     */
    @RequestMapping(value = "/memories/companies/{idCompany}/status",method = RequestMethod.GET)
    public ResponseEntity getPlantsStatus( @PathVariable("idCompany") int idCompany) throws IOException {
        System.out.println("get Status of all plants by idCompany ....");
        UserCompany userCompany = userCompanyService.getUserCompany(idCompany);
        if(userCompany == null)
            throw new NotFoundException(idCompany);
        ArrayList<PlantStatusDTO> dtos = new ArrayList<>();
        plantService.getAllByCompany(idCompany).forEach(plant -> dtos.add(memoryRedisService.getStatusPlant(plant)));
        return Response.ok(dtos,"Consultado correctamente",null);
    }


    @GetMapping(value = "/memory/test/{idCompany}")
    public  ResponseEntity getMemoryTest( @PathVariable("idCompany") int idCompany) throws IOException {
        //List<VariableFixedBasic> fixedBasicList=memoryRedisService.getVariableFixedByIdCompany(idCompany);
        return Response.ok(memoryRedisService.getVariableFixedByIdCompany(idCompany),"list redis sample",null);
    }

    @GetMapping(value = "/memory/test/load")
    public  ResponseEntity getMemoryTestLoad() throws IOException {
        memoryRedisService.loadAllCompanyMemory();
        return Response.ok(null,"load redis sample",null);
    }
}
