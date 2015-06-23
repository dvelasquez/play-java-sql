package security.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Danilo Velasquez on 23-06-2015.
 * Generic Role model class
 */
public class RoleDTO {
    @SerializedName("id")
    private transient Integer PRF_Codigo;                                           //ID SHOULD NEVER BE EXPOSED TO THE FRONT-END!!!
    @SerializedName("token")
    private String PRF_Token;                                                       //GUID, Token can be safely be exposed in api's or Front End
    @SerializedName("name")
    private String PRF_Nombre;                                                      //Role Name

    public Integer getPRF_Codigo() {
        return PRF_Codigo;
    }

    public void setPRF_Codigo(Integer PRF_Codigo) {
        this.PRF_Codigo = PRF_Codigo;
    }

    public String getPRF_Token() {
        return PRF_Token;
    }

    public void setPRF_Token(String PRF_Token) {
        this.PRF_Token = PRF_Token;
    }

    public String getPRF_Nombre() {
        return PRF_Nombre;
    }

    public void setPRF_Nombre(String PRF_Nombre) {
        this.PRF_Nombre = PRF_Nombre;
    }
}
