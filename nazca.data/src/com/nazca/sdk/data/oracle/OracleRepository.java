package com.nazca.sdk.data.oracle;

import com.nazca.sdk.data.DefaultEntityRepository;

import java.sql.ResultSet;
import java.util.ArrayList;

public class OracleRepository extends DefaultEntityRepository {

    public OracleRepository(ArrayList<ResultSet> rawData) {
        super(rawData);
    }


}
