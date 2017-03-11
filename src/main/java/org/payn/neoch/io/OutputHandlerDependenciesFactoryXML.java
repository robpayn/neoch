package org.payn.neoch.io;

import org.payn.chsm.io.ReporterFactoryXML;
import org.payn.chsm.io.file.ReporterFileSystemFactoryXML;

/**
 * Factory for dependencies output handler
 * 
 * @author v78h241
 *
 */
public class OutputHandlerDependenciesFactoryXML extends ReporterFactoryXML<ReporterDependencies> {

   @Override
   public void init() 
   {
      new ReporterFileSystemFactoryXML(reporter, config).init();
   }

   @Override
   public ReporterDependencies newReporter() 
   {
      return new ReporterDependencies();
   }

}
