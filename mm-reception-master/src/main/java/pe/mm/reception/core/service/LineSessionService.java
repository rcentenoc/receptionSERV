package pe.mm.reception.core.service;

import pe.mm.reception.core.dto.LineEssentialDTO;
import pe.mm.reception.core.dto.LineSessionDTO;

import java.util.List;

public interface LineSessionService {
	List<LineSessionDTO> list(List<LineEssentialDTO> lines);
	List<LineSessionDTO> listByLine(int idLine);
	int block(int idLine, int idUser, int idRole, int usermod);
	int getRol(int idUser);
	int unblock(int id, int idLine, int usermod);
	int check_availability(int idLine, int isUser, int idRole);
	int check_line_availability(int idLine);
//	List<SonTDO> getAllTreeWhitoutTags(int idLine, int idVariable, int idParent);
//	List<SonChildrenDTO> getAllTreeHierarchyWhitoutTags(int idLine, int idVariable, int idParent);
//	int setParent(int newparent, int id, int usermod);
//	int deleteVariable(int id, int usermod);
//	int insertNode(SonTDO nodo, int usermod);
//	int updateNode(SonTDO nodo, int usermod, int idVariable, int idNode);
}
