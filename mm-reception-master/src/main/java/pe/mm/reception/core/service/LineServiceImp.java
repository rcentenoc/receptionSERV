package pe.mm.reception.core.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.mm.reception.common.Request;
import pe.mm.reception.core.dto.LineEssentialDTO;
import pe.mm.reception.core.mapper.LineMapper;
import pe.mm.reception.core.model.Line;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

/**
 * Created by rodrigo on 15/09/2016.
 */
@Service
public class LineServiceImp implements LineService {

    @Autowired
    private LineMapper lineMapper;

    @Override
    public List<Line> getAllByPlant(int idPlant){
        List<Line> lines=lineMapper.getAllByDefault(idPlant,null,null);
        return lines;
    }

    @Override
    public List<Line> getAllByPlant(int idPlant, Request.Pager pager) {
        return lineMapper.getAllByDefault(idPlant,pager.getStartIndex(), pager.getPageSize());
    }

    @Override
    public Line getLine(int id) {
        return lineMapper.getById(id);
    }

    @Override
    public Line insert(Line line, int idUser) {
        String key = new BigInteger(60, new SecureRandom()).toString(32);
        line.setLineCode(key);
        lineMapper.insert(line,idUser);
        return line;
    }

    @Override
    public Line update(Line line, int idUser) {
        lineMapper.update(line,idUser);
        return line;
    }

    @Override
    public void delete(int id, int idUser) {
        lineMapper.softDeleted(id,2,idUser);
    }

    @Override
    public boolean verifySimilarName(Line line) {
        List list=lineMapper.getAllByField("name",line.getName(),line.getPlant().getId());
        return list.size()>0;
    }

    public List<Line> getLineStatusCero(int idPlant) {
        List<Line> lines=lineMapper.getAllByField("status_tables","0",idPlant);
        return lines;
    }

    @Override
    public List<LineEssentialDTO> getAllByPlantSimple(int idPlant){
        List<LineEssentialDTO> lines=lineMapper.getAllByPlantSimple(idPlant,null,null);
        return lines;
    }
}
