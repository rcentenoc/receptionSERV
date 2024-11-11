package pe.mm.reception.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.mm.reception.common.message.ErrorMessage;
import pe.mm.reception.common.message.Message;
import pe.mm.reception.common.message.SuccessMessage;
import pe.mm.reception.core.model.User;

public class Response <T> {

    private User user;
    private Message message;
    private T response;
    private Pager pager;
    private int Status;
    private String token;

    public String getToken() {
        return token;
    }

    public Response<T> setToken(String token) {
        this.token = token;
        return this;
    }

    public Pager getPager() {
        return pager;
    }

    public Response<T> setPager(Pager pager) {
        this.pager = pager;
        return this;
    }

    public Response() {
    }

    public User getUser() {
        return user;
    }

    public Response<T> setUser(User user) {
        this.user = user;return  this;
    }

    public Message getMessage() {
        return message;
    }

    public Response<T> setMessage(Message message) {
        this.message = message;return  this;
    }

    public T getResponse() {
        return response;
    }

    public Response<T> setResponse(T response) {
        this.response = response;return  this;
    }

    public int getStatus() {
        return Status;
    }

    public Response<T> setStatus(int status) {
        Status = status; return this;
    }

    public static ResponseEntity<?> ok(Object object, Pager pager){
        return ok(object,"OK", pager);
    }

    public static ResponseEntity<?> ok(Object object, String msg, Pager pager){
        return constructor(object,new SuccessMessage(msg), pager, HttpStatus.OK);
    }

    public static ResponseEntity<?> inserted(Object object, String msg){
        return constructor(object,new SuccessMessage(msg), null,HttpStatus.CREATED);
    }

    public static ResponseEntity<?> updated(Object object, String msg){
        return constructor(object,new SuccessMessage(msg), null,HttpStatus.ACCEPTED);
    }

    public static ResponseEntity<?> deleted(Object object, String msg){
        return constructor(object,new SuccessMessage(msg), null,HttpStatus.NO_CONTENT);
    }

    public static ResponseEntity<?> notModified(Object object, String msg){
        return constructor(object,new ErrorMessage(msg), null,HttpStatus.NOT_MODIFIED);
    }

    public static ResponseEntity<?> invalid(Object object, String msg){
        return constructor(object,new ErrorMessage(msg), null, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> notAutenticade(Object object, String msg){
        return constructor(object,new ErrorMessage(msg), null,HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<?> notPermission(Object object, String msg){
        return constructor(object,new ErrorMessage(msg), null,HttpStatus.FORBIDDEN);
    }

    public static ResponseEntity<?> notFound(Object object, String msg){
        return constructor(object,new ErrorMessage(msg), null,HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<?> internalError(Object object, String msg) {
        return constructor(object, new ErrorMessage(msg), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<?> unauthorized(Object object, String msg) {
        return constructor(object, new ErrorMessage(msg), null, HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<?> conflict(Object object, String msg) {
        return constructor(object, new ErrorMessage(msg), null, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    private static ResponseEntity<?> constructor(Object object, Message msg, Pager pager, HttpStatus status)
    {
        Response <?> response = new Response()
                .setMessage(msg)
                .setResponse(object)
                .setPager(pager)
                .setStatus(status.value());

        ResponseEntity responseEntity = new ResponseEntity<Response>(response,null, status);

        return responseEntity;
    }


    public static class Pager {

        private int totalElements;
        private int startIndex;
        private int sizePage;

        public Pager(int totalElements, int startIndex, int sizePage) {
            this.totalElements = totalElements;
            this.startIndex = startIndex;
            this.sizePage = sizePage;
        }

        public int getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

        public int getStartIndex() {
            return startIndex;
        }

        public void setStartIndex(int startIndex) {
            this.startIndex = startIndex;
        }

        public int getSizePage() {
            return sizePage;
        }

        public void setSizePage(int sizePage) {
            this.sizePage = sizePage;
        }

    }
}
