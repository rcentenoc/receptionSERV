package pe.mm.reception.core.service;


import pe.mm.reception.core.dto.BatchDTO;
import pe.mm.reception.core.dto.StopDTO;
import pe.mm.reception.core.model.Line;

import java.util.Date;

public interface ManagmentDataService {
	BatchDTO createBatch(BatchDTO newBatchDto, Line line, Integer userId);
	void cleanProduction(Date started, Date ended, Line line, Integer userId);
	void updateStop(Line line, Integer userId, StopDTO stop);
}
