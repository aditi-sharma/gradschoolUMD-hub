import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import java.util.Scanner;
import org.apache.hadoop.fs.*;



/**
 * Created by Aditi on 10/18/15.
 * This class is to generate multiple small files of defined chunks from one large file
 */

public class FileSplitter {

    public void generateSplits(String file, int chunk_size) throws IOException
    {
        FileSystem hdfs = FileSystem.get(new Configuration());
        Path filePath = new Path(file);
        int bytesRead= 0,totalBytes=0;
        int i=1;
        String string,pathName="/Users/Aditi/hadoop/file"+i+".txt";

       // Path dirPath = new Path("/Users/Aditi");
       //hdfs.mkdirs(dirPath);
        Path newFilePath= new Path(pathName);
        hdfs.createNewFile(newFilePath);
        FSDataOutputStream fsOutStream = hdfs.create(newFilePath,true);
        File File_length= new File(file);
        long length=File_length.length();
        System.out.println("File is of size"+length);
        BufferedReader reader= new BufferedReader(new InputStreamReader(hdfs.open(filePath)));
        string=reader.readLine();
        do
        {
           if (bytesRead<=chunk_size)
           {
               fsOutStream.writeBytes(string);
               bytesRead += string.getBytes().length;
           }

            else {
                fsOutStream.writeBytes(string);
                fsOutStream.close();
               bytesRead += string.getBytes().length;
               // hdfs.copyFromLocalFile("",);
                System.out.println("The file at" + pathName + "has been generated"+ "\t" + bytesRead);
                i++;
                totalBytes+=bytesRead;
                bytesRead = 0;
                pathName = "/Users/Aditi/hadoop/file"+i+".txt";
                newFilePath = new Path(pathName);
               hdfs.createNewFile(newFilePath);
                fsOutStream = hdfs.create(newFilePath,true);

            }

        }while ((string=reader.readLine())!=null);
        System.out.println("The file at" + pathName + "has been generated"+ "\t" + bytesRead);
        fsOutStream.close();
            hdfs.close();
            reader.close();
    }


    public static void main(String[] args) throws IOException
    {
        String file;
        int CHUNK_SIZE=1;
        Scanner in = new Scanner(System.in);

        System.out.println("Enter the file path (in HDFS) to be split");
        file = in.nextLine();

        System.out.println("Enter the CHUNK SIZE in mb");
        CHUNK_SIZE = in.nextInt();

        FileSplitter splitter = new FileSplitter();
        splitter.generateSplits(file, CHUNK_SIZE*1024*1024);

    }
}


