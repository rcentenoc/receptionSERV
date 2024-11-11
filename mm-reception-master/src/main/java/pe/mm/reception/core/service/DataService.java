package pe.mm.reception.core.service;

import pe.mm.reception.core.dto.DataSendDTO;

public interface DataService {
    void insert(DataSendDTO dataLineDTO);

    // insert sirve para insertar un dato en la base de datos por medio de un DTO
    String greetingsUser(String gretting);
    // greetingsUser sirve para saludar al usuario con un mensaje
}
