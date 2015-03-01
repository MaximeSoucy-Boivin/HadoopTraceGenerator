/*
        HadoopTraceGenerator, a XML trace generator for Hadoop LTLValidator
        Copyright (C) 2013 Maxime Soucy-Boivin

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.info.trace.XML.Hadoop;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.xml.sax.SAXException;

public abstract class MessageFeeder implements Iterator<String>
{
  @Override
  public boolean hasNext()
  {
    return true;
  }

  @Override
  public final void remove()
  {
    // Do nothing
  }
  
 
  /**
   * Sets up the Output File 
   * @param path who contains the tree of the folder and the name of it
   */
  public void SetOutputFile(String path)
  {
	  
  }
  
  /**
   * Sets up the maximum size of the Output File
   * @param size who contains the size in bytes
   */
  public void setSizeOutputFile(int size)
  {
	  
  }
  
  /**
   * Sets up the conditions file to analyse if there is one
   * @param path who contains the tree of the folder and the name of it
   */
  public void setConditionsFile(String path)throws FileNotFoundException, SAXException, IOException
  {
	  
  }
  
  /**
   * Sets up the maximum depth of the trace 
   * @param depth who contains the maximum depth of the traces
   */
  public void setDepthTrace(int depth)
  {
	  
  }
  
  /**
   * Sets up the type of the trace 
   * @param type who contains the type of the value of the trace ( Numbers, Letters or Both )
   */
  public void setTypeTrace(String type)
  {
	  
  }
  
  /**
   * Sets up the maximum depth of the variables
   * @param depth who contains the maximum depth of the variables
   */
  public void setDepthVar(int depth)
  {
	  
  }
  
  /**
   * Sets up the maximum depth of the values of the variables
   * @param depth who contains the maximum depth of values of the variables of the traces
   */
  public void setDepthValue(int depth)
  {
	  
  }
  
  /**
   * Gets the state of the generation of the traces file (True == Done, False == in progress)
   * @return the current state of the generation 
   */
  public boolean getFileGenerated()
  {
	  return false;
  }

}
