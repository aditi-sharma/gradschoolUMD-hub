
import java.util.*;
public class AmazonCustomerProfile {

	private String ID;
	private Vector<AmazonProductProfile> PurchasedProductList;
	
	public AmazonCustomerProfile(String ID)
	{
		this.ID=ID;
		PurchasedProductList=new Vector<AmazonProductProfile>();
	}
	public String GetID()
	{
		return ID;
	}
	public  Vector<AmazonProductProfile> GetProductsPurchased()
	{
		return this.PurchasedProductList;
	}
	
	public String toString()
	{
		StringBuilder sb=new StringBuilder("******************Customer*************************");
		sb.append("\nCustomerID:"+ID);
		//sb.append("\n******************Products******************");
		sb.append("\n***************************************************");
		for(AmazonProductProfile prodProfile:PurchasedProductList)
		{
			sb.append("\nProductID:"+prodProfile.ID);
			sb.append("\nTitle:"+prodProfile.Title);
			sb.append("\nRatings:"+prodProfile.Rating);
		}
		sb.append("\n**************************************************\n\n");		
		return sb.toString();
	}
	public void AddProductProfile(AmazonProductProfile product)
	{
		PurchasedProductList.add(product);
	}
	
}
