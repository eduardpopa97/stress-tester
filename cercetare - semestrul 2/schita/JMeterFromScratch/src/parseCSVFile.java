import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class parseCSVFile {

	public static void main(String[] argv) {
		try (BufferedReader br = Files.newBufferedReader(Paths.get("src//file1.csv"))) {

		    // CSV file delimiter
		    String DELIMITER = ",";

		    // read the file line by line
		    String line;
		    while ((line = br.readLine()) != null) {

		        // convert line into columns
		        String[] columns = line.split(DELIMITER);

		        // print all columns
		        System.out.println(columns[14]);
		    }

		} catch (IOException ex) {
		    ex.printStackTrace();
		}
	}
}
