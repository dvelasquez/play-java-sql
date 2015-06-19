package models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dvelasquez on 03-02-15.
 */
public class MarcaDBO {

    @SerializedName("codigo")
    private transient Integer MARC_Codigo;
    @SerializedName("token")
    private String MARC_Token;
    @SerializedName("nombre")
    private String MARC_Nombre;
    @SerializedName("activa")
    private Boolean MARC_Activa;

    public Integer getMARC_Codigo() {
        return MARC_Codigo;
    }

    public void setMARC_Codigo(Integer MARC_Codigo) {
        this.MARC_Codigo = MARC_Codigo;
    }

    public String getMARC_Token() {
        return MARC_Token;
    }

    public void setMARC_Token(String MARC_Token) {
        this.MARC_Token = MARC_Token;
    }

    public String getMARC_Nombre() {
        return MARC_Nombre;
    }

    public void setMARC_Nombre(String MARC_Nombre) {
        this.MARC_Nombre = MARC_Nombre;
    }

    public Boolean getMARC_Activa() {
        return MARC_Activa;
    }

    public void setMARC_Activa(Boolean MARC_Activa) {
        this.MARC_Activa = MARC_Activa;
    }
}
