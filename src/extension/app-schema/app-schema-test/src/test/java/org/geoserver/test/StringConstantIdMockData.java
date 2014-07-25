/*
 * Copyright (c) 2001 - 2013 OpenPlans - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.test;

import org.geoserver.data.test.MockData;

/**
 * Mock data for testing GEOT-4567: using String constants in idExpression with joining.
 * 
 * Inspired by {@link MockData}.
 * 
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 */

public class StringConstantIdMockData extends AbstractAppSchemaMockData {

    /**
     * @see org.geoserver.test.AbstractAppSchemaMockData#addContent()
     */
    @Override
    public void addContent() {
        addFeatureType(GSML_PREFIX, "MappedFeature", "MappedFeatureStringConstantId.xml",
                "MappedFeaturePropertyfile.properties", "GeologicUnit.properties");
    }

}
