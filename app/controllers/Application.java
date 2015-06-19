package controllers;



import models.MarcaDBO;
import play.*;
import play.libs.Json;
import play.mvc.*;
import sdk.data.DataService;
import sdk.data.EntityRepository;
import sdk.data.EntityTable;
import sdk.data.collections.DataParameterCollection;

import static sdk.adapter.Adapter.ExecuteQuery;

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
