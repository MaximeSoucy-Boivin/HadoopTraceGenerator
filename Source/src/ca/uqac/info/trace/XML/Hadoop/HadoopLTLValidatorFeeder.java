package ca.uqac.info.trace.XML.Hadoop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
 
import javax.xml.parsers.ParserConfigurationException;

//For the setConditionsFile fonction
//Example from: http://examples.javacodegeeks.com/core-java/xml/dom/parse-xml-file-with-dom/
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class HadoopLTLValidatorFeeder extends MessageFeeder{

	protected static final Random m_random = new Random();
	
	private Writer writer = null;
	private File FileIO = null;
	private File FileIOFinal = null;
	private int SizeMaxIO = 0;
	private int SizeFile = 0;
	private List<VariableConditions> listConditions = new LinkedList<VariableConditions>();
	private int depthTrace = 0;
	private String typeTrace = "";
	private int DepthVar = 0;
	private int DepthValue = 0;
	private boolean FileGenerated = false;
	private String StringLetters = "abcdefghijklmnopqrstuwxyz";
	private int tracelength = 0;
	
	public HadoopLTLValidatorFeeder()
	{
		super();
	}
	
	public void SetOutputFile(String path)
	{
		try 
    	{
    		//Create the file and open it
    		FileIO = new File(path+"TEMP");
    		FileOutputStream is = new FileOutputStream(FileIO);
    		OutputStreamWriter osw = new OutputStreamWriter(is); 
    		writer = new BufferedWriter(osw);
    		
    		//Create the temp file to write the final file with the tracelength
    		FileIOFinal = new File(path);
    		
    		//Write the begining of the file
    		writer.write("<Trace>\n");
    		writer.write(" \n");
    		writer.write("<TraceLength>\n");
    		writer.write(" \n");
    	} 
		catch (IOException e) 
    	{
    		 System.err.println("Problem to open the output File");
        }
	}
	
	private void closeFile()
	{
		//Close the OutpPut file
		try 
		{
			writer.close();
		} catch (IOException e) 
		{
			 System.err.println("Problem to close the output File");
		}
	}
	
	 public void setSizeOutputFile(int size)
	 {
		 SizeMaxIO = size;
	 }
	 
	 public int getSizeOutputFile()
	 {
		 return SizeMaxIO;
	 }
	 
	 public void setConditionsFile(String path) throws FileNotFoundException, SAXException, IOException
	 {
		 //Read the conditions and create VariableCondition Variables to help the analyse
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		 dbf.setValidating(false);
		 DocumentBuilder db;
		 
		try 
		{
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(new FileInputStream(new File(path)));
			
			//Get each conditions variables
			NodeList itemsList = doc.getElementsByTagName("var");  
			int itemsCount = itemsList.getLength();
			int itemValueCount = 0;
			
			//Treats each variable
			for (int i = 0; i<itemsCount; i++) 
			{
				VariableConditions Var = new VariableConditions();
				
				Node itemNode = itemsList.item(i);
				Element item = (Element) itemNode;
				 
			    //Get the name of the variable
				Node itemNameNode = item.getElementsByTagName("name").item(0);
				String itemName = itemNameNode.getFirstChild().getNodeValue();
				Var.setVarName(itemName);   
				
				//Get all of the value of the variable
				NodeList ListValue = item.getElementsByTagName("value");
				itemValueCount= ListValue.getLength();
				
				//Set all of the value in the object
				for (int j =0; j<itemValueCount; j++)
				{
					Node itemValueNode = item.getElementsByTagName("value").item(j);
					String itemRes = itemValueNode.getFirstChild().getNodeValue();
					Var.addValueVar(itemRes);
				}
				//Add the VariableCondition in the list
				listConditions.add(Var);
			}

		} 
		catch (ParserConfigurationException e) 
		{
			System.out.println("Parsing error for the Conditions File");
			e.printStackTrace();
		}
	 }
	 
	 public void setDepthTrace(int depth)
	 {
		  depthTrace = depth;
	 }
	 
	 public void setTypeTrace(String type)
	 {
		 typeTrace = type;
	 }
	 
	 public void setDepthVar(int depth)
	 {
		 DepthVar = depth;
	 }
	 
	 public void setDepthValue(int depth)
	 {
		 DepthValue = depth;
	 }
	  
	 public boolean getFileGenerated()
	 {
		  return FileGenerated;
	 }

	 private void setFileGenerated(boolean value)
	 {
		 FileGenerated = value;
	 }
	 
	 private String ValueVariable()
	 {
		 String value ="";
		 Random m_RandValue = new Random();
		 int rand = m_RandValue.nextInt(DepthValue);
		 
		 if(typeTrace.equals("Numbers"))
		 {
			 value = String.valueOf(rand);
		 }
		 else if(typeTrace.equals("Letters"))
		 {
			 if(rand >= 25)
			 {
				 while(rand >=25)
				 {
					 rand = rand - 24;
				 }
			 }
			 value =  StringLetters.substring(rand, rand+1);
		 }
		 else if(typeTrace.equals("Both"))
		 {
			 m_RandValue = new Random();
			 int  test = m_RandValue.nextInt(100);
			 
			 if(test < 51) // Numbers Choice
			 {
				 value = String.valueOf(rand);
			 }
			 else // Letters Choices
			 {
				 if(rand >= 25)
				 {
					 while(rand >=25)
					 {
						 rand = rand - 24;
					 }
				 }
				 value =  StringLetters.substring(rand, rand+1);
			 }
		 }
		 else // Default --> Numbers
		 {
			 value = String.valueOf(rand);
		 }
		 
		 return value;
	 }
	 
	 private void UpdateSizeFile(int addSize)
	 {
		 SizeFile += addSize;
	 }
	 
	 private int getSizeFile()
	 {
		 return SizeFile;
	 }
	 
	 private boolean FileFinish()
	 {
		 boolean Finish = false;
		 
		 //The current size of the file
		 int sizeFile = getSizeFile();
		 
		 //The maximum wanted by the user
		 int sizeMax = getSizeOutputFile();
		 
		 if(sizeMax < sizeFile)
		 {
			Finish = true;
			 
			//Set the end of the generation
			setFileGenerated(Finish);
		 }
		 return Finish;
	 }
	 
	 private void writeTraceLength()
	 {
		 String line = "";
		 try 
		 {
			//Create the scanner to read until the good place
	    	Scanner scan = new Scanner(FileIO);
	    	
	    	//Create the file and open it
    		FileOutputStream is = new FileOutputStream(FileIOFinal);
    		OutputStreamWriter osw = new OutputStreamWriter(is); 
    		Writer writerTemp = new BufferedWriter(osw);
	    	
    		//Read and write the lines until the trace length tag
    		line = scan.next();
    		
	    	while(line.equals("<TraceLength>") == false)
	    	{
	            if(line.equals("<Trace>"))
	            {
	            	line +="\n";
	            }
	    		writerTemp.write(line+"\n");
	    		line = scan.next();
	    	}
	
	    	//Change the line tracelength
	    	line = "<TraceLength>";
	    	line+= tracelength;
	    	line += "</TraceLength>\n";
	    	writerTemp.write(line+"\n");
	    	
	    	//re-write the rest of the file
	    	while(scan.hasNext())
	    	{
	    		line = scan.next();
	    		
	    		if(line.contains("<Event>"))
	    		{
	    			line +="\n";
	    			writerTemp.write(line+"\n");
	    		}
	    		else if (line.equals("</Trace>"))
	    		{
	    			writerTemp.write(line);
	    		}
	    		else //Variables
	    		{
	    			writerTemp.write(line+"\n");
	    		}
	    	}
	    	
	    	//Close all files
	    	scan.close();
	    	writerTemp.close();
	    	
	    	//delete the old file
	    	FileIO.delete();
	    	
		}
		 catch (IOException e) 
		{
			System.out.println("Error during the writing of the trace length and the new file");
			e.printStackTrace();
		}
	 }
	 
	@Override
	public String next()
	{
		boolean Finish = FileFinish();
		
		//Means the end of the generation
		if(Finish == true)
		{
			try 
			{
				//Write the end of the file
				writer.write("</Trace>");
				
				//Close the output file
				closeFile();
				
				// Add the tracelength to the file 
				writeTraceLength();
			} 
			catch (IOException e) 
			{
				System.out.println("Writing error for the end of the Output File");
				e.printStackTrace();
				
			}
			return "The generation of the Trace is over !!! ";
		}
		
		boolean firstVar = true;
		boolean ConditionFound = false;
		String XmlEvent = "<Event>";//\n";
		
		//add the tracelength
		XmlEvent += "<TL>";
		XmlEvent += tracelength;
		XmlEvent += "</TL>";
		
		int test = m_random.nextInt(100);
		
		for(int i =0; i< depthTrace; i++)
		{
			if( test <=51)// Create a random numbers of variable
			{
				int name = m_random.nextInt(DepthVar);
				String VarName = null;
				if(firstVar == true)
				{
					VarName = "<p" + name + ">";
					firstVar = false;
				}
				else
					VarName = ",<p" + name + ">";
					
				XmlEvent += VarName;
				
				//Check if their are some conditions on the variables
				if(listConditions.size() == 0)
				{
					String Value = ValueVariable();
					XmlEvent += Value;
					XmlEvent += "</p" + name + ">";
				}
				else
				{
					VariableConditions Cond = new VariableConditions();
					for(int j = 0; j< listConditions.size(); j++)
					{
						//Get the name of the current variable in the condition list
						Cond = listConditions.get(j);
						String nameCond = Cond.getVarName();
						
						if(nameCond.equals("p"+ name))
						{
							//Get the value(s) of the variable
							List<String> listValue = new LinkedList<String>();
							listValue = Cond.getListValue();
							
							if(listValue.size() == 1)
							{
								XmlEvent += listValue.get(0).toString();
								
							}
							else
							{
								//Chooses the value at random from the list of choices
								Random m_RandValue = new Random();
								int  sizeList = m_RandValue.nextInt(listValue.size());
								XmlEvent += listValue.get(sizeList).toString();
							}
							
							//Close the variable
							XmlEvent += "</p" + name + ">";//\n
							ConditionFound = true;
							
							//Stop the for --> Because condition found
							j = listConditions.size();
							
						}// if(nameCond.equals(VarName))
					
					}//for listConditions
					
					//Make sure that every variable has a value
					if(ConditionFound != true)
					{
						String Value = ValueVariable();
						XmlEvent += Value;
						XmlEvent += "</p" + name + ">";///\n
					}
					
				}// else listConditions.size() == 0
			}// end test <= 51
			test = m_random.nextInt(100);
			ConditionFound = false;
		}
		XmlEvent +="</Event>\n";
		XmlEvent += " \n";
		
		//Update the current size of the file
		int eventLenght = XmlEvent.length();
		UpdateSizeFile(eventLenght);
		
		//Write the new event in the Output File
		try 
		{
			writer.write(XmlEvent);
		} catch (IOException e) 
		{
			System.out.println("Writing error for the XML Trace Generated");
			e.printStackTrace();
		}
		
		//update the trace length
		tracelength++;
		
		return XmlEvent;
	}
}
