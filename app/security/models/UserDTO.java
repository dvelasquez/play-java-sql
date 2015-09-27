package security.models;

import com.google.gson.annotations.SerializedName;
import com.nazca.sdk.data.EntityTable;


/**
 * Created by Danilo Velasquez on 23-06-2015.
 * Generic User Data Class
 */
public class UserDTO {
    @SerializedName("id")
    private transient Integer USU_Codigo;                                       //ID SHOULD NEVER BE EXPOSED TO THE FRONT-END!!!
    @SerializedName("token")
    private String USU_Token;                                                   //GUID, Token can be safely be exposed in api's or Front End
    @SerializedName("name")
    private String USU_Nombre;                                                  //Name of the User
    @SerializedName("username")
    private String USU_Identificador;                                           //Username
    @SerializedName("password")
    private String USU_Contrasena;                                              //Password
    @SerializedName("email")
    private String USU_Correo;                                                  //User Email.

    private EntityTable<RoleDTO> roles;                                         //List with user roles.

    public Integer getUSU_Codigo() {
        return USU_Codigo;
    }

    public void setUSU_Codigo(Integer USU_Codigo) {
        this.USU_Codigo = USU_Codigo;
    }

    public String getUSU_Token() {
        return USU_Token;
    }

    public void setUSU_Token(String USU_Token) {
        this.USU_Token = USU_Token;
    }

    public String getUSU_Nombre() {
        return USU_Nombre;
    }

    public void setUSU_Nombre(String USU_Nombre) {
        this.USU_Nombre = USU_Nombre;
    }

    public EntityTable<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(EntityTable<RoleDTO> roles) {
        this.roles = roles;
    }

    public String getUSU_Contrasena() {
        return USU_Contrasena;
    }

    public void setUSU_Contrasena(String USU_Contrasena) {
        this.USU_Contrasena = USU_Contrasena;
    }

    public String getUSU_Correo() {
        return USU_Correo;
    }

    public void setUSU_Correo(String USU_Correo) {
        this.USU_Correo = USU_Correo;
    }

    public String getUSU_Identificador() {
        return USU_Identificador;
    }

    public void setUSU_Identificador(String USU_Identificador) {
        this.USU_Identificador = USU_Identificador;
    }
}
