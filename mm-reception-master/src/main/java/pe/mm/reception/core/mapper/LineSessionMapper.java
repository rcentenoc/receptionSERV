package pe.mm.reception.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import pe.mm.reception.core.dto.LineEssentialDTO;
import pe.mm.reception.core.dto.LineSessionDTO;
import pe.mm.reception.core.model.Rol;

import java.util.List;

@Mapper
public interface LineSessionMapper {
    List<LineSessionDTO> list(@Param("lines") List<LineEssentialDTO> lines, @Param("idLine") int idLine);
    int block(@Param("line_id") int line_id, @Param("user_id") int user_id, @Param("role_id") int role_id, @Param("usermod") int usermod);
    List<Rol> getRol(@Param("user_id") int user_id);
    List<LineSessionDTO> is_enabled(@Param("line_id") int line_id, @Param("role_id") int role_id);
    List<LineSessionDTO> in_session(@Param("line_id") int line_id, @Param("user_id") int user_id);
    int unblock(@Param("id") int id, @Param("idLine") int idLine, @Param("usermod") int usermod);
}
