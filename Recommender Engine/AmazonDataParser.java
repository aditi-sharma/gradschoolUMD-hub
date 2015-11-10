import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.io.*;

public class AmazonDataParser {
   
   private BufferedReader reader=null;
   private  String inputLine=null;
   private String  filePath=null;
   private Map<String, AmazonCustomerProfile> CustomerProfiles=null;
	private Map<String, AmazonItemProfile> ItemProfiles=null;
   private String currentProductID=null;
   private String currentProductTitle=null;
	private String currentProductASIN=null;
	private int currentProductsalesrank=0;
	private Vector<String> currentProductCategories=new Vector<String>();




   public AmazonDataParser(String filePath)
   {
	   this.filePath=filePath;	  
	   this.CustomerProfiles= new  HashMap<String, AmazonCustomerProfile>();
		this.ItemProfiles= new HashMap<String, AmazonItemProfile>();
   }
   
   public void parse()
   {
	   try
	   {
		   if(reader!=null) reader.close();
		   reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)), 1024*100);
    	   inputLine=reader.readLine();
    	   while(inputLine!=null)
    	   {
    		   if(inputLine.startsWith("Id:"))
    		   {
    			   this.currentProductID = extractProductID(inputLine);
    			   inputLine=processInputLines(reader);
    		   }
    		   else
    		   {
    			   inputLine=reader.readLine();
    		   }
    				   
    		   //System.out.println(inputLine);
    	   }
    	   
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace();
	   }
       finally{
	    	try
	    	{
	    		if(reader!=null) reader.close();
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    	}
    	
       }
	   
   }
   
	private  String processInputLines(BufferedReader reader)throws IOException
	{
		String line=reader.readLine();
		System.out.println("\n****[Start]*****");
		while(line!=null && !line.startsWith("Id:"))
		{
			System.out.println(line);
			parseLine(line); //Important parses each line
			line=reader.readLine();
		}
		System.out.println("\n****[END]****");
		return line;
	}
	
	private void parseLine(String input)
	{

		//Implement regular expression to parse and build customer profile
		input=input.trim();
		if(input.startsWith("title:")) 
		{	
			this.currentProductTitle=extractProductTitle(input);
			
		}
		if(input.startsWith("ASIN:"))
		{
			this.currentProductASIN=extractProductASIN(input);

		}

		if (input.startsWith("salesrank:"))
		{
			this.currentProductsalesrank=extractProductsalesrank(input);
		}

		if(input.contains("cutomer:") ) extractCustomerProfile(input);

		if (input.startsWith("|"))
		{
			input=input.substring(1,input.length());
			if (currentProductCategories.isEmpty())
			this.currentProductCategories=extractProductCategories(input);
			else
			{
				currentProductCategories.addAll(extractProductCategories(input));
			}


		}

		if (input.startsWith("reviews:")) {
			extractItemProfiles(input);

		}
	}
	
	private String extractProductID(String input)
	{
		String extractedText=null;
		if(input!=null && input.startsWith("Id:"))
		{
			int pos=input.indexOf(':');
			extractedText=input.substring(pos+1);
			if(extractedText!=null)
			{
				extractedText=extractedText.trim();
			}
		}
		return extractedText;
	}
	private String extractProductTitle(String input)
	{
		String extractedText=null;
		if(input==null) return null;
		input=input.trim();
		if(input.startsWith("title:"))
		{
			int pos=input.indexOf(':');
			extractedText=input.substring(pos+1);
			if(extractedText!=null)
			{
				extractedText=extractedText.trim();
			}
		}
		return extractedText;
	}

	private String extractProductASIN(String input)
	{
		String extractedText=null;
		if(input==null) return null;
		input=input.trim();
		if(input.startsWith("ASIN:"))
		{
			int pos=input.indexOf(':');
			extractedText=input.substring(pos+1);
			if(extractedText!=null)
			{
				extractedText=extractedText.trim();
			}
		}
		return extractedText;
	}

	private int extractProductsalesrank(String input)
	{
		String extractedText=null;
		int salesrank=0;
		if(input==null) return 0;
		input=input.trim();
		if(input.startsWith("salesrank:"))
		{
			int pos=input.indexOf(':');
			extractedText=input.substring(pos+1);
			if(extractedText!=null)
			{
				extractedText=extractedText.trim();
			}
			 salesrank=Integer.parseInt(extractedText);
		}
		return salesrank;
	}

	private Vector<String> extractProductCategories(String input)
	{
		Vector<String> productCategory= new Vector<String>();
		if (input!=null)
		{
			input=input.trim();
			input=input.replaceAll("\\[.*?\\] ?", "");
			input=input.replaceAll("\\(.*?\\) ?","");
			input=input.replaceAll(",","");
			input=input.replaceAll(" ","");
			String[] splitString=input.split("\\|");
			int length=splitString.length;
			int i=0;
			while(i<length)
			{
				if (!currentProductCategories.contains(splitString[i]))
				productCategory.add(productCategory.size(), splitString[i].trim());
				i++;
			}

		}
		return productCategory;
	}


	private void extractItemProfiles(String input)
	{
		AmazonItemProfile itemProfile=new AmazonItemProfile();
		if (input!=null)
		{
			input=input.trim();
			if (input.contains("avg rating:"))
			{
				int pos= input.indexOf("avg rating:");
				String extractedText=input.substring(pos+12);
				if (extractedText!=null)
				{
					extractedText=extractedText.trim();
				}

				try {
					itemProfile.avgRating = Double.parseDouble(extractedText);
					//System.out.println(itemProfile.avgRating);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					itemProfile.avgRating=-1;
				}
			}

			itemProfile.ID=currentProductID ;
			itemProfile.ASIN=currentProductASIN;
			itemProfile.salesrank=currentProductsalesrank;
			itemProfile.title=currentProductTitle;
			itemProfile.productCategories.addAll(currentProductCategories);
			ItemProfiles.put(currentProductID, itemProfile);
			currentProductCategories.clear();

		}

	}

	private AmazonCustomerProfile extractCustomerProfile(String input)
	{
		//String extractedText=null;
		AmazonCustomerProfile custProfile=null;
		if(input!=null)
		{
			input=input.trim();
			if(input.contains("cutomer:"))
			{
				/*int pos=input.indexOf("cutomer:");
				extractedText=input.substring(pos+1);
				if(extractedText!=null)
				{
					extractedText.trim();
				}*/
				
				//test.s
				String[]splitString=input.split(".*cutomer:|\\s+rating:|\\s+votes:|\\shelpful:");
			    //Must contain 5 characters
				if(splitString.length==5)
				{

					String customerID=splitString[1].trim();
					if(CustomerProfiles.containsKey(customerID))
					{
						custProfile=CustomerProfiles.get(customerID);
					}
					else
					{
						custProfile=new AmazonCustomerProfile(customerID);
						CustomerProfiles.put(customerID, custProfile);
						
					}
					AmazonProductProfile product=new AmazonProductProfile();
					product.ID=currentProductID;
					product.Title=currentProductTitle;
					try
					{
						product.Rating=Integer.parseInt(splitString[2].trim());
					}
					catch(Exception e)
					{
						product.Rating=-1;
					}
					custProfile.AddProductProfile(product);					
				}
				
				
			}
		}
		return custProfile;
	}

	public void printItemsProfile() throws IOException {

		PrintWriter out= new PrintWriter(new BufferedWriter(new FileWriter("temp.txt")));
		/*Set s=ItemProfiles.entrySet();
		Iterator it = s.iterator();
		while(it.hasNext())
		{
			Map.Entry mEntry = (Map.Entry) it.next();
			String k= (String) mEntry.getKey();
			out.writeChars(k + "\n");
			//out.writeObject(mEntry.getValue());
		}
		out.flush();
		out.close();
*/
		for(String key:ItemProfiles.keySet())
		{
			//out.writeChars(key.trim().toString() + ":");
			out.write(ItemProfiles.get(key).toString() + "\n");
			//System.out.println(ItemProfiles.get(key));
		}
		out.flush();
		out.close();

	}
	
	public void printCustomersProfile()
	{
		for(String key:CustomerProfiles.keySet())
		{
			AmazonCustomerProfile  profile=CustomerProfiles.get(key);
			System.out.println(profile.toString());
		}
	}
	

}
