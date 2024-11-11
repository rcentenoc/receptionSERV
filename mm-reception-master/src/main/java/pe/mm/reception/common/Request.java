package pe.mm.reception.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class Request <T> {

    private T requestObject;

    public Request(String jsonObject, Class<T> clase ) throws IOException {
        ObjectMapper om = new ObjectMapper();
        om.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);//accept arrays
        requestObject = (T) om.readValue(jsonObject, clase);
    }

    public Request(String jsonObject, TypeReference<T> typeReference) throws IOException {
        ObjectMapper om = new ObjectMapper();
        om.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);//accept arrays
        requestObject = om.readValue(jsonObject, typeReference);
    }

    public T getRequestObject(){
        return requestObject;
    }

    public static Pager getPagination(HttpServletRequest context){
        Integer start = getIntegerFromServletRequest(context, Pager.STARTINDEX);
        Integer size = getIntegerFromServletRequest(context, Pager.PAGESIZE);
        if(start==null|| size==null){
            return null;
        }
        return new Pager(start,size);
    }

    private static Integer getIntegerFromServletRequest(HttpServletRequest context, String name){
        String parameter = context.getParameter(name);
        if(parameter==null || parameter.isEmpty())
            return null;
        try {
            return Integer.parseInt(parameter);
        } catch (NumberFormatException e) {
            return null;
        }
    }


    public static class Pager {
        private static String STARTINDEX = "start";
        private static String PAGESIZE = "size";
        private int startIndex;
        private int pageSize;

        public Pager(int startIndex, int pageSize) {
            this.startIndex = startIndex;
            this.pageSize = pageSize;
        }

        public int getStartIndex() {
            return startIndex;
        }

        public void setStartIndex(int startIndex) {
            this.startIndex = startIndex;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
    }

}
