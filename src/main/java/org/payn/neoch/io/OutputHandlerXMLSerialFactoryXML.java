package org.payn.neoch.io;

import org.payn.chsm.io.ReporterFactoryXML;
import org.payn.chsm.io.file.ReporterIntervalFactoryXML;

/**
 * Factory for a serial XML output handler
 * 
 * @author v78h241
 *
 */
public class OutputHandlerXMLSerialFactoryXML extends ReporterFactoryXML<ReporterXMLSerial> {

   @Override
   public void init() 
   {
      new ReporterIntervalFactoryXML(reporter, config).init();
   }

   @Override
   public ReporterXMLSerial newReporter() 
   {
      return new ReporterXMLSerial();
   }

}
