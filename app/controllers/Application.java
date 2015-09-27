package controllers;

/**
 * Created by Danilo Velasquez on 23-06-2015.
 */

import com.nazca.sdk.data.DataService;
import com.nazca.sdk.data.EntityRepository;
import com.nazca.sdk.data.EntityTable;
import com.nazca.sdk.data.collections.DataParameterCollection;
import models.MarcaDBO;
import play.libs.Json;
import play.mvc.*;
import security.RestSecurity;

import static com.nazca.sdk.adapter.Adapter.ExecuteQuery;

public class Application extends Controller {

    public Result index() throws Exception {
        EntityTable<MarcaDBO> marcas = getMarca();
        return ok(Json.toJson(marcas));
    }

    @RestSecurity.JWTAuth
    public Result authGet() throws Exception {
        EntityTable<MarcaDBO> marcas = getMarca();
        return ok(Json.toJson(marcas));
    }

    public EntityTable<MarcaDBO> getMarca() throws Exception {
        try(DataParameterCollection collection = new DataParameterCollection()){
            EntityRepository repo = ExecuteQuery(new DataService("PA_ADV_GET_Marca"));
            return repo.GetModel(MarcaDBO.class);
        }
    }

}
