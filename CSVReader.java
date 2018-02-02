import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CSVReader {
    String filename;
    double vmax;
    File file;
    public CSVReader(String filename) {
	this.filename = filename;
	file = new File(filename);
    }

    public CSVReader(File file) {
	this.file = file;
    }

    public double[][] parseCSV() {
	
	List<List<String>> lines = new ArrayList<>();
	Scanner inputStream;
	
	try{
	    inputStream = new Scanner(file);
	    while(inputStream.hasNextLine()){
		String line= inputStream.nextLine();
		String[] values = line.split(",");
		lines.add(Arrays.asList(values));
	    }
	    
	    inputStream.close();
	    
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	double [][] profile = new double[lines.size()][2];
	for(int j = 0; j < lines.size(); j++) {
	    List<String> line = lines.get(j);
	    for (int i = 0; i < 2; i++) {
		profile[j][i] = Double.parseDouble(line.get(i));
	    }
	    
	}
	return profile;
    }

    public static void main(String[] args) {
	String filename = args[0];
	CSVReader reader = new CSVReader(filename);
	reader.parseCSV();
    }

}
