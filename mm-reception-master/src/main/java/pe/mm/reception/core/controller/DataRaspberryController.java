package pe.mm.reception.core.controller;

import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pe.mm.reception.common.Request;
import pe.mm.reception.common.ValidationException;
import pe.mm.reception.common.ValidationRaspberryException;
import pe.mm.reception.common.util.Dispar_Decode;
import pe.mm.reception.core.dto.DataSendDTO;
import pe.mm.reception.core.service.DataService;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by César Calle on 10/10/2016.
 */
@Controller
public class DataRaspberryController {

    @Autowired
    private DataService dataService;

    Logger logger = LoggerFactory.getLogger(DataRaspberryController.class);


    /**
     * Recibe data en formato JSON para procesar.
     * @param body Data a insertar en json.
     * @return <code>1</code> data aceptada
     * @throws IOException si hay un error al procesar
     */
    @RequestMapping(value = "/data/send-raspberry", method = RequestMethod.POST)
    public ResponseEntity insert(@RequestBody String body) throws IOException {
        Dispar_Decode codificador = new Dispar_Decode();
        body = codificador.Decodificar(body);
        DataSendDTO dataLineDTO = new Request<DataSendDTO>(body, DataSendDTO.class).getRequestObject();
        long initialTime = dataLineDTO.getTime().getTime();
        dataService.insert(dataLineDTO);
        return response(1, dataLineDTO.getCode(), initialTime);
    }


    /**
     * Devuelve un código de estado
     * @param code <code>0</code> data enviada errónea
     * <code>1</code> data aceptada
     * <code>2</code> error interno
     * @return ResponseEntity
     */
    private ResponseEntity response(int code){
        Map map = new HashMap();
        map.put("code", null);
        map.put("time", null);
        map.put("status", code);
        return ResponseEntity.ok(map);
    }

    /**
     * Devuelve un código de estado
     * @param code <code>0</code> data enviada errónea
     * <code>1</code> data aceptada
     * <code>2</code> error interno
     * @param  file
     * @param  time
     * @return ResponseEntity
     */
    private ResponseEntity response(int code, String file, Long time){
        Map map = new HashMap();
        map.put("status", code);
        map.put("code", file);
        map.put("time", time);
        return ResponseEntity.ok(map);
    }


    /**
     * Devuelve un mensaje para errores de validación.
     * @param req petición del cliente.
     * @param e excepción de validación.
     * @return <code>0</code> data enviada errónea.
     */
    @ExceptionHandler(value = ValidationRaspberryException.class)
    public ResponseEntity invalid(HttpServletRequest req, ValidationRaspberryException e){
        logger.error("Invalido "+e.getCode()+" ("+e.getTime().getTime()+"): " +(e.getMsg()==null?e.getMessage():e.getMsg().getMessage()), e);
        return response(0, e.getCode(), e.getTime().getTime());
    }

    /**
     * Devuelve un mensaje para errores de conexión a base de datos.
     * @param req petición del cliente.
     * @param e excepción de conexión a base de datos.
     * @return <code>2</code> error interno.
     */
    @ExceptionHandler(value = MyBatisSystemException.class)
    public ResponseEntity errorBD(HttpServletRequest req, MyBatisSystemException e){
        logger.error("Error BD: "+e.getMessage(), e);
        return response(2);
    }

    /**
     * Devuelve un mensaje para errores de sintaxis en consultas a base de datos.
     * @param req petición del cliente.
     * @param e excepción de sintaxis en consultas a base de datos.
     * @return <code>2</code> error interno.
     */
    @ExceptionHandler(value = BadSqlGrammarException.class)
    public ResponseEntity errorGramarBD(HttpServletRequest req, BadSqlGrammarException e)
    {
        logger.error("Error Sintaxis: "+e.getMessage(), e);
        return response(2);
    }

    /**
     * Devuelve un mensaje para errores de punteros a nulo.
     * @param req petición del cliente.
     * @param e excepción de puntero a nulo.
     * @return <code>2</code> error interno.
     */
    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity nullPointer(HttpServletRequest req, NullPointerException e){
        logger.error("Puntero nulo ", e);
        return response(2);
    }

    /**
     * Devuelve un mensaje para errores genéricos si no hay uno específico.
     * @param req petición del cliente.
     * @param e excepción genérica.
     * @return <code>0</code> data enviada errónea.
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        // Si existe algun otro Handler especifico que lo recepcione lo vuleve a lanzar
        if (AnnotationUtils.findAnnotation
                (e.getClass(), ResponseStatus.class) != null)
            throw e;
        logger.error("Otro error: "+ e.getMessage(), e);
        return response(0);
    }


}
