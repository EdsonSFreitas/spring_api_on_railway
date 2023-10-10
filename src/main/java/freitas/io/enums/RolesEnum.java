package freitas.io.enums;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 10/10/2023
 * {@code @project} api
 */

public enum RolesEnum {
    ROLE_USER(1),
    ROLE_ADMIN(2),
    ROLE_SUPER(3);

    private final int code;

    private RolesEnum(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static RolesEnum valueOf(int code){
        for(RolesEnum value : RolesEnum.values()){
            if(value.getCode() == code){
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid Role code");
    }
}