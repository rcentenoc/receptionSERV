package pe.mm.reception.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pe.mm.reception.common.RoleEnum;
import pe.mm.reception.core.dto.LineEssentialDTO;
import pe.mm.reception.core.dto.LineSessionDTO;
import pe.mm.reception.core.mapper.LineSessionMapper;
import pe.mm.reception.core.model.Rol;
import pe.mm.reception.security.model.AuthenticatedUser;

import java.util.ArrayList;
import java.util.List;

@Service
public class LineSessionServiceImpl implements LineSessionService {
	@Autowired
	private LineSessionMapper lineSessionMapper;

	@Override
	public List<LineSessionDTO> list(List<LineEssentialDTO> lines) {
		List<LineSessionDTO> lineSessionDTOS = new ArrayList<>();
		lineSessionDTOS = lineSessionMapper.list(lines,0);
		return lineSessionDTOS;
	}

	@Override
	public List<LineSessionDTO> listByLine(int idLine) {
		List<LineSessionDTO> lineSessionDTOS = new ArrayList<>();
		lineSessionDTOS = lineSessionMapper.list(null,idLine);
		return lineSessionDTOS;
	}

	@Override
	public int block(int idLine,int idUser,int idRole,int usermod) {
		int result;
		List<LineSessionDTO> list = new ArrayList<>();
		if(idRole == RoleEnum.ROLE_OPERARIO.getCode()){
			list = lineSessionMapper.is_enabled(idLine, RoleEnum.ROLE_OPERARIO.getCode());//trae a operarios en la linea
		}

		if(list.isEmpty()) {//linea sin operarios
			list = lineSessionMapper.in_session(0,idUser);//verificar session en todas las lineas
			if(list.isEmpty())//no inicio sesion?
				result = lineSessionMapper.block(idLine, idUser, idRole, usermod);//no estas en sesion y agregamos
			else{
				lineSessionMapper.unblock(0,idLine,idUser);//cierra sesion en todas las linea
				result = lineSessionMapper.block(idLine, idUser, idRole, usermod);//agregamos session
			}
		}
		else{
			result = 0;//no disponible
			for (LineSessionDTO i : list) {
				if(i.getId_user() == idUser){
					result = 2;//ocupada pero x el mismo x eso no se inserta en la tabla
					break;
				}
			}
		}

		return result;
	}

	@Override
	public int getRol(int idUser) {
		List<Rol> s = lineSessionMapper.getRol(idUser);
		if(s.isEmpty()){
			return 0;
		}else
			return s.get(0).getId();
	}

	@Override
	public int unblock(int id,int idLine,int usermod) {
		int result = lineSessionMapper.unblock(id,idLine,usermod);

		return result;
	}

	@Override
	public int check_availability(int idLine,int idUser,int idRole) {
		int result;
		List<LineSessionDTO> list = new ArrayList<>();
		if(idRole == RoleEnum.ROLE_OPERARIO.getCode()) {
			list = lineSessionMapper.is_enabled(idLine,RoleEnum.ROLE_OPERARIO.getCode());//trae a operarios en la linea
		}
		if(list.isEmpty())
			result = 1;//no hay operario -> linea disponible
		else {
			result = 0;//hay otro operario -> linea no disponible
			for (LineSessionDTO i : list) {
				if(i.getId_user() == idUser){
					result = 2;//tu eres el operario -> linea disponible
					break;
				}
			}
		}

		if(result == 1){
			list = lineSessionMapper.in_session(idLine,idUser);
			if(list.isEmpty())
				result = 0;//no estas en sesion -> linea no disponible
		}else if(result == 0){
			lineSessionMapper.unblock(0,idLine,idUser);//cierra sesion en todas las linea
		}

		return result;
	}

	@Override
	public int check_line_availability(int idLine) {

		//get user date start
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		int idUser = 1,idRole=1;

		if(auth!=null){
			AuthenticatedUser userSession = (AuthenticatedUser) auth.getPrincipal();
			idUser =userSession.getId();
			idRole = this.getRol(idUser);
		}
		//get user date end

		int result;
		List<LineSessionDTO> list = new ArrayList<>();
		if(idRole == RoleEnum.ROLE_OPERARIO.getCode()) {
			list = lineSessionMapper.is_enabled(idLine,RoleEnum.ROLE_OPERARIO.getCode());//trae a operarios en la linea
		}
		if(list.isEmpty())
			result = 1;//no hay operario -> linea disponible
		else {
			result = 0;//hay otro operario -> linea no disponible
			for (LineSessionDTO i : list) {
				if(i.getId_user() == idUser){
					result = 2;//tu eres el operario -> linea disponible
					break;
				}
			}
		}

		if(result == 1){
			list = lineSessionMapper.in_session(idLine,idUser);
			if(list.isEmpty())
				result = 0;//no estas en sesion -> linea no disponible
		}else if(result == 0){
			lineSessionMapper.unblock(0,idLine,idUser);//cierra sesion en todas las linea
		}

		return result;
	}
}
