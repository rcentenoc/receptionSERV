package pe.mm.reception.security;

public class Constants {
    public static class SECURITY{
        public static final long TIME_TOKEN_EXPIRATION=900000;
        public static final String TOKEN_KEY_GEN="siger-key-v1";
        public static final String CLAIM_USER_ID="userId";
        public static final String CLAIM_ROLES="ROLES";
        public static final String TOKEN_HEADER="token";
        public static final String START_TOKEN ="sigert-";
        public static final String CLAIM_COMPANY_ID ="companyId";
        public static final String CLAIM_PLANT_ID ="plantId";
        public static final String ROLES_SEPARATOR =",";
    }
}