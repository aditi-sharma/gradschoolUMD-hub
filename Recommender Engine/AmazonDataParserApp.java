/*
 * 
 * Id:   1
ASIN: 0827229534
  title: Patterns of Preaching: A Sermon Sampler
  group: Book
  salesrank: 396585
  similar: 5  0804215715  156101074X  0687023955  0687074231  082721619X
  categories: 2
   |Books[283155]|Subjects[1000]|Religion & Spirituality[22]|Christianity[12290]|Clergy[12360]|Preaching[12368]
   |Books[283155]|Subjects[1000]|Religion & Spirituality[22]|Christianity[12290]|Clergy[12360]|Sermons[12370]
  reviews: total: 2  downloaded: 2  avg rating: 5
    2000-7-28  cutomer: A2JW67OY8U6HHK  rating: 5  votes:  10  helpful:   9
    2003-12-14  cutomer: A2VE83MZF98ITY  rating: 5  votes:   6  helpful:   5


*/

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


import java.io.IOException;

public class AmazonDataParserApp {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		String filePath="am1.txt";
		AmazonDataParser amDP=new AmazonDataParser(filePath);
		amDP.parse();
		
		/*String test="2002-9-24  cutomer: A1I7GHG2XNYO3J  rating: 5  votes:   2  helpful:   1";
		//test.s
		String[]splitString=test.split(".*cutomer:|\\s+rating:|\\s+votes:|\\shelpful:");
	    //Must contain 5 characters
		if(splitString.length==5)
		{
			
		}*/
		//amDP.printCustomersProfile();
		//amDP.printItemsProfile();
		Configuration conf = new Configuration();
		conf.set("mapreduce.input.keyvaluelinerecordreader.key.value.separator", ":");
		conf.set("mapreduce.output.textoutputformat.separator", ":");

		Job job = new Job(conf, "TFIDF");
		job.setInputFormatClass(KeyValueTextInputFormat.class);
		job.setMapperClass(ItemCategoriesTFIDF.ItemProfilesMapper.class);
		job.setReducerClass(ItemCategoriesTFIDF.ItemProfilesReducer.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		//job.setOutputFormatClass(TextOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.waitForCompletion(true);
	}

}
