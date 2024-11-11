package pe.mm.reception.configuration;


import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pe.mm.reception.common.InternalException;
import pe.mm.reception.common.NotFoundException;
import pe.mm.reception.common.Response;
import pe.mm.reception.common.ValidationException;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by César Calle on 10/08/2016.
 * Manejo de excepciones a nivel de aplicación.
 */
@ControllerAdvice
class ControllerException {

	Logger logger = LoggerFactory.getLogger(ControllerException.class);
    /**
     * Devuelve un mensaje para errores genéricos si no hay uno específico.
     * @param req petición del cliente.
     * @param e excepción genérica.
     * @return mensaje de error.
     * @throws Exception excepción con un captor asignado.
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity
    defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        // Si existe algun otro Handler especifico que lo recepcione lo vuleve a lanzar
        if (AnnotationUtils.findAnnotation
                (e.getClass(), ResponseStatus.class) != null)
            throw e;
        logger.error(e.getMessage(), e);
        return Response.invalid(null, e.getMessage());
    }

    /**
     * Devuelve un mensaje para errores de validación.
     * @param req petición del cliente.
     * @param e excepción de validación.
     * @return mensaje de error.
     */
    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity invalid(HttpServletRequest req, ValidationException e){
    	logger.error(e.getMessage(), e);
        return Response.invalid(null,e.getMsg().getMessage());
    }

    /**
     * Devuelve un mensaje para errores de no encontrado
     * @param req petición del cliente.
     * @param e excepción de no encontrado.
     * @return mensaje de error.
     */
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity notFound(HttpServletRequest req, NotFoundException e){
    	logger.error(e.getMessage(), e);
        return Response.notFound(null,e.getMsg().getMessage());
    }

    /**
     * Devuelve un mensaje para errores internos del servidor
     * @param req petición del cliente.
     * @param e excepción interna del servidor.
     * @return mensaje de error.
     */
    @ExceptionHandler(value = InternalException.class)
    public ResponseEntity internalError(HttpServletRequest req, NotFoundException e){
    	logger.error(e.getMessage(), e);
        return Response.internalError(null,e.getMsg().getMessage());
    }

    /**
     * Devuelve un mensaje para errores de conexión a base de datos.
     * @param req petición del cliente.
     * @param e excepción de conexión a base de datos.
     * @return mensaje de error.
     */
    @ExceptionHandler(value = MyBatisSystemException.class)
    public ResponseEntity errorBD(HttpServletRequest req, MyBatisSystemException e){
    	logger.error(e.getMessage(), e);
        return Response.internalError(e.getLocalizedMessage(), "Error de Base de Datos");
    }

    /**
     * Devuelve un mensaje para errores de sintaxis en consultas a base de datos.
     * @param req petición del cliente.
     * @param e excepción de sintaxis en consultas a base de datos.
     * @return mensaje de error.
     */
    @ExceptionHandler(value = BadSqlGrammarException.class)
    public ResponseEntity errorGramarBD(HttpServletRequest req, BadSqlGrammarException e){
    	logger.error(e.getMessage(), e);
        return Response.internalError(e.getMessage(), "Error de consulta");
    }

    /**
     * Devuelve un mensaje para errores de autentificación
     * @param req petición del cliente.
     * @param e excepción de autentificación.
     * @return mensaje de error.
     */
    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity errorAuthentication(HttpServletRequest req, AuthenticationException e){
        return Response.unauthorized(e.getMessage(), "No Autorizado");
    }
    
}