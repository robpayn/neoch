package org.payn.neoch.io;

import java.io.File;
import java.util.HashMap;

import org.payn.chsm.io.ReporterFactoryXML;
import org.payn.chsm.io.file.ReporterFileSystemFactoryXML;

/**
 * Factory for dependencies reporter
 * 
 * @author v78h241
 *
 */
public class ReporterDependenciesFactoryXML extends ReporterFactoryXML<ReporterDependencies> {

   @Override
   public void init() throws Exception 
   {
      new ReporterFileSystemFactoryXML(reporter, config).init();
   }

   @Override
   public ReporterDependencies newReporter(File workingDir, HashMap<String, String> argMap) 
   {
      return new ReporterDependencies(workingDir, argMap);
   }

}
