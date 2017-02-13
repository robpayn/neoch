package org.payn.neoch.io;

import org.payn.chsm.io.OutputHandlerFactoryXML;
import org.payn.chsm.io.file.OutputHandlerFileSystemFactoryXML;

/**
 * Factory for dependencies output handler
 * 
 * @author v78h241
 *
 */
public class OutputHandlerDependenciesFactoryXML extends OutputHandlerFactoryXML<OutputHandlerDependencies> {

   @Override
   public void init() 
   {
      new OutputHandlerFileSystemFactoryXML(outputHandler, config).init();
   }

   @Override
   public OutputHandlerDependencies newOutputHandler() 
   {
      return new OutputHandlerDependencies();
   }

}
