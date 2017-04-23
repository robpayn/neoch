package org.payn.neoch.io.reporters;

import java.io.File;
import java.util.HashMap;

import org.payn.chsm.io.reporters.ReporterFactoryXML;
import org.payn.chsm.io.reporters.ReporterIntervalFactoryXML;

/**
 * Factory for a serial XML reporter
 * 
 * @author v78h241
 *
 */
public class ReporterXMLSerialFactoryXML extends ReporterFactoryXML<ReporterXMLSerial> {

   @Override
   public void init() throws Exception 
   {
      new ReporterIntervalFactoryXML(reporter, config).init();
   }

   @Override
   public ReporterXMLSerial newReporter(File workingDir, HashMap<String, String> argMap) 
   {
      return new ReporterXMLSerial(workingDir, argMap);
   }

}
