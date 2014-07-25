/*
 * Copyright (c) 2001 - 2013 OpenPlans - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;

import com.mockrunner.mock.web.MockHttpServletResponse;

/**
 * Test for SF0 CSV outputFormat in App-schema {@link BoreholeViewMockData}
 * 
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 */
public class CSVOutputFormatTest extends AbstractAppSchemaTestSupport {

    @Override
    protected BoreholeViewMockData createTestData() {
        return new BoreholeViewMockData();
    }

    /**
     * Tests full request with CSV outputFormat.
     * 
     */
    @Test
    public void testFullRequest() throws Exception {

        MockHttpServletResponse resp = getAsServletResponse("wfs?service=WFS&version=1.1.0&request=GetFeature&typename=gsmlp:BoreholeView&outputFormat=csv");

        // check the mime type
        assertEquals("text/csv", resp.getContentType());

        // check the content disposition
        assertEquals("attachment; filename=BoreholeView.csv", resp.getHeader("Content-Disposition"));

        // read the response back with a parser that can handle escaping, newlines and what not
        List<String[]> lines = readLines(resp.getOutputStreamContent());

        // we should have one header line and then all the features in that feature type
        assertEquals(3, lines.size());        

        // check the header
        String[] header = new String[] { "gml:id", "gsmlp:identifier", "gsmlp:name",
                "gsmlp:drillingMethod", "gsmlp:driller", "gsmlp:drillStartDate",
                "gsmlp:startPoint", "gsmlp:inclinationType", "gsmlp:boreholeMaterialCustodian",
                "gsmlp:boreholeLength_m", "gsmlp:elevation_m", "gsmlp:elevation_srs",
                "gsmlp:specification_uri", "gsmlp:metadata_uri", "gsmlp:shape" };        
        
        assertTrue(Arrays.asList(lines.get(0)).containsAll(Arrays.asList(header)));
        
        // check each line has the expected number of elements (num of att + 1 for the id)
        int headerCount = lines.get(0).length;
        assertEquals(headerCount, lines.get(1).length);
        assertEquals(headerCount, lines.get(2).length);
    }

    /**
     * Tests CSV outputFormat with filters.
     * 
     */
    @Test
    public void testFilter() throws Exception {
        String IDENTIFIER = "borehole.GA.17338";
        MockHttpServletResponse resp = getAsServletResponse("wfs?service=WFS&request=GetFeature&version=1.1.0&typename=gsmlp:BoreholeView&outputFormat=csv&filter=<Filter><PropertyIsEqualTo><PropertyName>gsmlp:identifier</PropertyName><Literal>"
                + IDENTIFIER + "</Literal></PropertyIsEqualTo></Filter>");

        // check the mime type
        assertEquals("text/csv", resp.getContentType());

        // check the content disposition
        assertEquals("attachment; filename=BoreholeView.csv", resp.getHeader("Content-Disposition"));

        // read the response back with a parser that can handle escaping, newlines and what not
        List<String[]> lines = readLines(resp.getOutputStreamContent());

        // we should have one header line and then all the features in that feature type
        assertEquals(2, lines.size());

        int identifierIndex = Arrays.asList(lines.get(0)).indexOf("gsmlp:identifier");
        assertEquals(IDENTIFIER, lines.get(1)[identifierIndex]);
        
        // check the header
        String[] header = new String[] { "gml:id", "gsmlp:identifier", "gsmlp:name",
                "gsmlp:drillingMethod", "gsmlp:driller", "gsmlp:drillStartDate",
                "gsmlp:startPoint", "gsmlp:inclinationType", "gsmlp:boreholeMaterialCustodian",
                "gsmlp:boreholeLength_m", "gsmlp:elevation_m", "gsmlp:elevation_srs",
                "gsmlp:specification_uri", "gsmlp:metadata_uri", "gsmlp:shape" };        
        
        assertTrue(Arrays.asList(lines.get(0)).containsAll(Arrays.asList(header)));
        
        // check each line has the expected number of elements (num of att + 1 for the id)
        int headerCount = lines.get(0).length;
        assertEquals(headerCount, lines.get(1).length);
    }

//TODO: requires a patch in WFS GetFeature.class
//    /**
//     * Tests CSV outputFormat with property selections. 
//     * 
//     */
//    @Test
//    public void testPropertyName() throws Exception {
//        MockHttpServletResponse resp = getAsServletResponse("wfs?service=WFS&version=1.1.0&request=GetFeature&typename=gsmlp:BoreholeView&outputFormat=csv&propertyName=gsmlp:identifier,gsmlp:name");
//        // check the mime type
//        assertEquals("text/csv", resp.getContentType());
//
//        // check the content disposition
//        assertEquals("attachment; filename=BoreholeView.csv", resp.getHeader("Content-Disposition"));
//
//        // read the response back with a parser that can handle escaping, newlines and what not
//        List<String[]> lines = readLines(resp.getOutputStreamContent());
//
//        // we should have one header line and then all the features in that feature type
//        assertEquals(3, lines.size());
//
//        for (String[] line : lines) {
//            // check each line has the expected number of elements (num of att + 1 for the id)
//            assertEquals(3, line.length);
//        }
//
//        // check the header
//        String[] header = new String[] { "gml:id", "gsmlp:identifier", "gsmlp:name" };
//        assertEquals(Arrays.toString(header), Arrays.toString(lines.get(0)));
//
//    }

    /**
     * Convenience to read the csv content . Copied from {@link org.geoserver.wfs.response.CSVOutputFormatTest}
     * 
     * @param csvContent
     * @return
     * @throws IOException
     */
    private List<String[]> readLines(String csvContent) throws IOException {
        CSVReader reader = new CSVReader(new StringReader(csvContent));

        List<String[]> result = new ArrayList<String[]>();
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            result.add(nextLine);
        }
        return result;
    }

}
