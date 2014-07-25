package org.geoserver.wcs2_0;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.geotools.data.AbstractDataStoreFactory;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;

public class MultiDimDataStoreFactory extends AbstractDataStoreFactory {

	@Override
	public DataStore createDataStore(Map<String, Serializable> params)
			throws IOException {		
	    return new MultiDimDataStore((String)params.get("ParentLocation"));
	}

	@Override
	public DataStore createNewDataStore(Map<String, Serializable> params)
			throws IOException {
		throw new UnsupportedOperationException("Can not create a new dummy data store");
	}

	@Override
	public String getDescription() {
		return "Data store using the dummy catalog as backend.";
	}

	@Override
	public Param[] getParametersInfo() {
		return new DataStoreFactorySpi.Param[] {new Param("ParentLocation")};
	}


}
