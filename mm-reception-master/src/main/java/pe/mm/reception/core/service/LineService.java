package pe.mm.reception.core.service;

import pe.mm.reception.common.Request;
import pe.mm.reception.core.dto.LineEssentialDTO;
import pe.mm.reception.core.model.Line;

import java.util.List;

/**
 * Created by rodrigo on 15/09/2016.
 */

public interface LineService {
    List<Line> getAllByPlant(int idPlant);
    List<Line> getAllByPlant(int idPlant, Request.Pager pager);
    Line getLine(int id);
    Line insert(Line line, int idUser);
    Line update(Line line, int idUser);
    void delete(int id, int idUser);
    boolean verifySimilarName(Line line);
    List<Line> getLineStatusCero(int idPlant);
    List<LineEssentialDTO> getAllByPlantSimple(int idPlant);
}
