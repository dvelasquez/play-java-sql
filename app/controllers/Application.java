package controllers;



import com.valentys.sdk.data.DataService;
import com.valentys.sdk.data.EntityRepository;
import com.valentys.sdk.data.EntityTable;
import com.valentys.sdk.data.collections.DataParameterCollection;
import models.MarcaDBO;
import play.libs.Json;
import play.mvc.*;

import static com.valentys.sdk.adapter.Adapter.ExecuteQuery;

public class Application extends Controller {

    public Result index() throws Exception {
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
