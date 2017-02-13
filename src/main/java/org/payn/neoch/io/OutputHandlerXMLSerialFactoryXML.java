package org.payn.neoch.io;

import org.payn.chsm.io.OutputHandlerFactoryXML;
import org.payn.chsm.io.file.OutputHandlerIntervalFactoryXML;

/**
 * Factory for a serial XML output handler
 * 
 * @author v78h241
 *
 */
public class OutputHandlerXMLSerialFactoryXML extends OutputHandlerFactoryXML<OutputHandlerXMLSerial> {

   @Override
   public void init() 
   {
      new OutputHandlerIntervalFactoryXML(outputHandler, config).init();
   }

   @Override
   public OutputHandlerXMLSerial newOutputHandler() 
   {
      return new OutputHandlerXMLSerial();
   }

}
