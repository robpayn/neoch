package org.payn.neoch.io.xmltools;

import org.payn.chsm.Behavior;
import org.payn.chsm.io.xml.ElementBehavior;
import org.w3c.dom.Element;

/**
 * XML element representing a behavior
 * 
 * @author rob payn
 *
 */
public class ElementBehaviorMatrix extends ElementBehavior {

   /**
    * Construct a new instance based on the provided XML element
    * 
    * @param element
    *       XML element associated with the behavior element
    */
   public ElementBehaviorMatrix(Element element) 
   {
      super(element);
   }

   /**
    * Override the super method to check for behavior matrix type
    */
   @Override
   public void configureBehaviorElement(Behavior behavior, boolean install) 
   {
      setName(behavior.getSimpleName());
      if (!install)
      {
         setInstall(Boolean.toString(install));
      }
      setResourceName(behavior.getResource().getName());
   }

}
