package local.apache.orc;
import org.apache.orc.*;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.vector.LongColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;

public class GenerateORCFile {

	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();
		Path path = new Path("/Users/drice/orctest/t11.orc"); 
		
		TypeDescription schema = TypeDescription.fromString("struct<x:int,y:int>");
		Writer writer = OrcFile.createWriter(path,
		                  OrcFile.writerOptions(conf)
		                         .setSchema(schema));

		VectorizedRowBatch batch = schema.createRowBatch();
		LongColumnVector x = (LongColumnVector) batch.cols[0];
		LongColumnVector y = (LongColumnVector) batch.cols[1];
		for(int r=0; r < 10000; ++r) {
		  int row = batch.size++;
		  x.vector[row] = r;
		  y.vector[row] = r * 3;
		  // If the batch is full, write it out and start over.
		  if (batch.size == batch.getMaxSize()) {
			  writer.addRowBatch(batch);
		    batch.reset();
		  }
		}
		writer.close();
	}

}






