package pe.mm.reception.core.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pe.mm.reception.common.NotFoundException;
import pe.mm.reception.common.Response;
import pe.mm.reception.common.SigerValidator;
import pe.mm.reception.common.ValidationException;
import pe.mm.reception.core.dto.BatchDTO;
import pe.mm.reception.core.dto.StopDTO;
import pe.mm.reception.core.dto.WithoutProduction;
import pe.mm.reception.core.model.Line;
import pe.mm.reception.core.service.LineService;
import pe.mm.reception.core.service.LineSessionService;
import pe.mm.reception.core.service.ManagmentDataService;
import pe.mm.reception.security.model.AuthenticatedUser;

import java.util.Iterator;
import java.util.List;

@Controller
public class ManagmentDataController {
	
	@Autowired
	private ManagmentDataService mangmentDataSevice;
	

	 @Autowired
	 LineService lineService;

	 @Autowired
	 LineSessionService lineSessionService;
	
	/**
	 * Actualiza un lote existente, para aumentarle o disminuirle el tiempo de duración
	 * @param batch lote a actualizar
	 * @return
	 */
	/*@RequestMapping(value = "data/batch", method = RequestMethod.PUT)
	public ResponseEntity updateBatch(@RequestBody BatchDTO batch) {
		mangmentDataSevice.updateBatch(batch);
		return Response.inserted(batch, "El batch fue actualizado");
	}*/

	/**
	 * Crea un nuevo lote, cortando el lote anterior
	 * @param newBatch nuevo lote
	 * @return
	 */
	@RequestMapping(value = "/companies/{idCompany}/plants/{idPlant}/batch", method = RequestMethod.POST)
	@ApiOperation( 
	    value = "Crea un nuevo lote", 
	    notes = "Asigna a la data recibida en el rango de tiempo el lote y producto recibido"
    )
	@ApiResponses({
		@ApiResponse(code = 404, message = "No se encontró el id"),
		@ApiResponse(code = 400, message = "Acceso prohibido o Error de validación"),
		@ApiResponse(code = 201, message = "El lote fue creado exitosamente")
	})
	public ResponseEntity<BatchDTO> newBatch(@PathVariable("idCompany") int idCompany,
                                             @PathVariable("idPlant") int idPlant,
                                             @RequestBody BatchDTO newBatch) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int usermod=1;
        if(auth!=null){
            AuthenticatedUser userSession = (AuthenticatedUser) auth.getPrincipal();
            usermod =userSession.getId();
        }
		SigerValidator.validate(newBatch, SigerValidator.INSERT);
		if(newBatch.getEnded()!=null && !newBatch.getEnded().after(newBatch.getStarted())) {
			throw new ValidationException("La fecha de fin debe ser mayor que la de inicio");
		}

        Line line  = lineService.getLine(newBatch.getLineId());
        if(line==null)
            throw new NotFoundException(newBatch.getLineId());
        if(line.getPlant()!=null && line.getPlant().getId()!=idPlant )
            throw new ValidationException("Los identificadores son diferentes ");
        if(line.getPlant().getUserCompany()!=null && line.getPlant().getUserCompany().getId()!=idCompany )
            throw new ValidationException("Los identificadores son diferentes ");
        //Verifica que sea de tipo producción
		if (line.getType() != 1)
			throw new ValidationException("No es una línea de producción");

		//verify line session start
		String msg;
		int result = lineSessionService.check_line_availability(newBatch.getLineId());
		if(result==0){// no disponible?
			msg = "La linea no se encuentra disponible";
			return (ResponseEntity<BatchDTO>) Response.conflict(result,msg);
		}
		//verify line session end
		
		newBatch = mangmentDataSevice.createBatch(newBatch,line, usermod);
		return (ResponseEntity<BatchDTO>) Response.inserted(newBatch, "El lote fue creado exitosamente");
	}
	
	/**
	 * 
	 * @param idCompany
	 * @param idPlant
	 * @param newBatch
	 * @return
	 */
	@RequestMapping(value = "/companies/{idCompany}/plants/{idPlant}/lines/{idLine}/withouProduction", method = RequestMethod.POST)
	@ApiOperation( 
	    value = "Limpia un rango sin producción", 
	    notes = "Al rango del tiempo consultado le quita la producción y los lotes generados"
    )
	@ApiResponses({
		@ApiResponse(code = 404, message = "No se encontró el id"),
		@ApiResponse(code = 400, message = "Acceso prohibido o Error de validación"),
		@ApiResponse(code = 201, message = "El rango fue limpiado exitosamente")
	})
	public ResponseEntity<WithoutProduction> withoutBatch(@PathVariable("idCompany") int idCompany,
														  @PathVariable("idPlant") int idPlant, @PathVariable("idLine") int idLine,
														  @RequestBody WithoutProduction newBatch) {

		//verify line session start
		String msg;
		int result = lineSessionService.check_line_availability(idLine);
		if(result==0){// no disponible?
			msg = "La linea no se encuentra disponible";
			return (ResponseEntity<WithoutProduction>) Response.conflict(result,msg);
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int usermod=1;
        if(auth!=null){
            AuthenticatedUser userSession = (AuthenticatedUser) auth.getPrincipal();
            usermod =userSession.getId();
        }
		SigerValidator.validate(newBatch, SigerValidator.INSERT);

		if(newBatch.getEnded()==null) {
			throw new ValidationException("La fecha de fin es obligatoria");
		} else if(!newBatch.getEnded().after(newBatch.getStarted())) {
			throw new ValidationException("La fecha de fin debe ser mayor que la de inicio");
		}
        
        Line line  = lineService.getLine(idLine);
        if(line==null)
            throw new NotFoundException(idLine);
        if(line.getPlant()!=null && line.getPlant().getId()!=idPlant )
            throw new ValidationException("Los identificadores son diferentes ");
        if(line.getPlant().getUserCompany()!=null && line.getPlant().getUserCompany().getId()!=idCompany )
            throw new ValidationException("Los identificadores son diferentes ");
        //Verifica que sea de tipo producción
		if (line.getType() != 1)
			throw new ValidationException("No es una línea de producción");

        
		
		mangmentDataSevice.cleanProduction(newBatch.getStarted(), newBatch.getEnded(),line, usermod);
		return (ResponseEntity<WithoutProduction>) Response.inserted(newBatch, "El lote fue creado exitosamente");
	}

	/**
	 *
	 * @param idCompany
	 * @param idPlant
	 * @param idLine
	 * @param stop
	 * @return
	 */
	@RequestMapping(value = "/companies/{idCompany}/plants/{idPlant}/lines/{idLine}/reasignstops", method = RequestMethod.POST)
	@ApiOperation(
			value = "Editar parada",
			notes = "Editar el tipo de parada"
	)
	@ApiResponses({
			@ApiResponse(code = 404, message = "No se encontró el id"),
			@ApiResponse(code = 400, message = "Acceso prohibido o Error de validación"),
			@ApiResponse(code = 201, message = "El rango fue limpiado exitosamente")
	})
	public ResponseEntity reasignStops(@PathVariable("idCompany") int idCompany, @PathVariable("idPlant") int idPlant,
                                       @PathVariable("idLine") int idLine, @RequestBody StopDTO stop) {

		//verify line session start
		String msg;
		int result = lineSessionService.check_line_availability(idLine);
		if(result==0){// no disponible?
			msg = "La linea no se encuentra disponible";
			return Response.conflict(result,msg);
		}
		//verify line session end
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		int usermod=1;
		if(auth!=null){
			AuthenticatedUser userSession = (AuthenticatedUser) auth.getPrincipal();
			usermod =userSession.getId();
		}
		Line line  = lineService.getLine(idLine);
		if(line==null)
			throw new NotFoundException(idLine);
		if(line.getPlant()!=null && line.getPlant().getId()!=idPlant )
			throw new ValidationException("Los identificadores son diferentes ");
		if(line.getPlant().getUserCompany()!=null && line.getPlant().getUserCompany().getId()!=idCompany )
			throw new ValidationException("Los identificadores son diferentes ");
		//Verifica que sea de tipo producción
		if (line.getType() != 1)
			throw new ValidationException("No es una línea de producción");


		mangmentDataSevice.updateStop(line,usermod,stop);

		return Response.inserted(stop, "La parada ha sido actualizada");
	}

	/**
	 *
	 * @param idCompany
	 * @param idPlant
	 * @param idLine
	 * @param stops
	 * @return
	 */
	@RequestMapping(value = "/companies/{idCompany}/plants/{idPlant}/lines/{idLine}/reasignsectionstops", method = RequestMethod.POST)
	@ApiOperation(
			value = "Editar seccion de parada",
			notes = "Editar el tipo de seccion de parada"
	)
	@ApiResponses({
			@ApiResponse(code = 404, message = "No se encontró el id"),
			@ApiResponse(code = 400, message = "Acceso prohibido o Error de validación"),
			@ApiResponse(code = 201, message = "El rango fue limpiado exitosamente")
	})
	public ResponseEntity reasignSectionStops(@PathVariable("idCompany") int idCompany, @PathVariable("idPlant") int idPlant,
									   @PathVariable("idLine") int idLine, @RequestBody List<StopDTO> stops) {

		//verify line session start
		String msg;
		int result = lineSessionService.check_line_availability(idLine);
		if(result==0){// no disponible?
			msg = "La linea no se encuentra disponible";
			return Response.conflict(result,msg);
		}
		//verify line session end
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		int usermod=1;
		if(auth!=null){
			AuthenticatedUser userSession = (AuthenticatedUser) auth.getPrincipal();
			usermod =userSession.getId();
		}
		Line line  = lineService.getLine(idLine);
		if(line==null)
			throw new NotFoundException(idLine);
		if(line.getPlant()!=null && line.getPlant().getId()!=idPlant )
			throw new ValidationException("Los identificadores son diferentes ");
		if(line.getPlant().getUserCompany()!=null && line.getPlant().getUserCompany().getId()!=idCompany )
			throw new ValidationException("Los identificadores son diferentes ");
		//Verifica que sea de tipo producción
		if (line.getType() != 1)
			throw new ValidationException("No es una línea de producción");

		int stopsSuccess = 0;
		try{
			Iterator<StopDTO> it = stops.iterator();
			// mientras al iterador queda proximo stop
			while(it.hasNext()) {
				StopDTO stop = it.next();
				reasignStops(idCompany, idPlant, idLine, stop);
				stopsSuccess++ ;
			}
		} catch (Exception e) {
			throw new ValidationException("Solo se hicieron " + stopsSuccess + " paradas ");
		}

		return Response.inserted(stops, "La sección de paradas ha sido actualizada");
	}
}
