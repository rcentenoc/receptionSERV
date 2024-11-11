package pe.mm.reception.security;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pe.mm.reception.security.dto.PlantSesisonDTO;
import pe.mm.reception.security.dto.UserCompanySessionDTO;
import pe.mm.reception.security.dto.UserSessionDTO;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;


@Component
public class TokenParser {

    public UserSessionDTO parseToken(String token) {
        UserSessionDTO u = null;

        try {
            String payload = token.split("\\.")[1];
            payload = new String(Base64.getDecoder().decode(payload));
            ObjectMapper mapper = new ObjectMapper();
            Map body = mapper.readValue(payload, Map.class);
            u = new UserSessionDTO();
            u.setName(body.get("sub").toString());

            u.setId(Integer.parseInt((String) body.get(Constants.SECURITY.CLAIM_USER_ID)));

            u.setRoles((String) body.get(Constants.SECURITY.CLAIM_ROLES));

            UserCompanySessionDTO userCompanyLoginDTO = new UserCompanySessionDTO();
            userCompanyLoginDTO.setId((int)body.get(Constants.SECURITY.CLAIM_COMPANY_ID));
            u.setUserCompany(userCompanyLoginDTO);

            PlantSesisonDTO plantDto  = new PlantSesisonDTO();
            if(body.get(Constants.SECURITY.CLAIM_PLANT_ID)!=null)
                plantDto.setId((int)body.get(Constants.SECURITY.CLAIM_PLANT_ID));
            u.setPlant(plantDto);

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return u;
    }
}
