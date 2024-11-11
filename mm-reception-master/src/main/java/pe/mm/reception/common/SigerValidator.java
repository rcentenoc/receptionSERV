package pe.mm.reception.common;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;

import java.util.List;

public class SigerValidator {
    public static String UPDATE = "update";
    public static String INSERT = "insert";

    public static void validate(Object object, String profile) throws ValidationException{
        Validator validator = new Validator();
        validator.disableAllProfiles();
        validator.enableProfile(profile);
        List<ConstraintViolation> constraints = validator.validate(object);
        if(constraints.size()>0)
            throw new ValidationException(constraints);
    }
}
