/**
 * Created by Aditi on 11/5/15.
 */
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;


import java.io.IOException;

public class ItemCategoriesTFIDF {

    public static class ItemProfilesMapper extends Mapper<Text, Text, Text, Text> {

        /* This map function merely generates item ID and its categories for clustering */
        public void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            String input = value.toString();
            String[] split = input.split("#");
            int len =split.length;
            Text category = new Text();
            category.set((split[len-1].trim()));
            if (!(category.toString().isEmpty()))
            context.write(key,category);
        }
    }


    public static class ItemProfilesReducer extends Reducer <Text, Text, Text, Text>

    {
        /*The reduce fucntion does not perform any function for now, but will be used for clustering for the Iterable categories of every item*/
        public void reduce(Text key, Text value, Context context) throws IOException, InterruptedException
        {
            context.write(key, value);
        }

    }
}