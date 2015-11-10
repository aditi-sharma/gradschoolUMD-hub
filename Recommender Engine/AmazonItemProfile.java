/**
 * Created by Aditi on 10/31/15.
 */
import java.io.Serializable;
import java.util.*;

public class AmazonItemProfile implements Serializable{
    public String ID;
    public String title;
    public String ASIN;
    public int salesrank;
    public double avgRating;
    public Vector<String> productCategories=new Vector<String>();

    public void setID(String ID){
        this.ID=ID;
    }

    public String getID(){
        return ID;
    }


    public String toString()
    {
        /*StringBuilder sb=new StringBuilder("******************ITEM*************************");
        sb.append("\nitemID:"+ID + ":");
        sb.append("\n***************************************************");
        sb.append("\nTitle:"+title);
        sb.append("\nASIN:"+ASIN);
        sb.append("\nSalesRank of the item:"+salesrank);
        sb.append("\nAverage Rating:"+avgRating);
        sb.append("\nCategories of item:"+productCategories);
        sb.append("\n**************************************************\n\n");
        return sb.toString();*/

        StringBuilder sb=new StringBuilder(ID+":");
       // sb.append(ID + ":");
        //sb.append("\n***************************************************");
        sb.append(title +"#");
        sb.append(ASIN +"#");
        sb.append(salesrank +"#");
        sb.append(avgRating +"#");
        sb.append(productCategories +"\n");
        //sb.append("\n**************************************************\n\n");
        return sb.toString();
    }
}
