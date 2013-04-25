package simulator;
import java.io.*;

public class Reader {
    
    private final String filepath;
	
    /* The index positions of each data field in the csv file */
    
    private BufferedReader reader;
    private BufferedReader sizeReader;
    private int size = 0;
    private int progress = 0;
    
    public Reader(String path) throws IOException, FileNotFoundException {
    	this.filepath = path;
        reader = new BufferedReader( new FileReader(path) );
        
        this.size = determineFileSize();
        
    }
    private int determineFileSize() throws IOException {
        sizeReader = new BufferedReader( new FileReader(filepath) );
        int numLines = 0;
        for (; sizeReader.readLine() != null; numLines++) {
        }
		return numLines;
    }
    
    public String readLine() throws IOException {
    	progress++;
    	return reader.readLine();
    }
    
    public int getFileSize() {
    	return size;
    }
    
    public int getProgress() {
    	return progress;
    }
    
    public String getFilePath() {
    	return this.filepath;
    }

    

    
}