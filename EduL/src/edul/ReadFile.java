package edul;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ReadFile {
	
	public static List<String> readFile(String path) throws IOException{
		List<String> file = Files.readAllLines(Paths.get(path));
		return file;
	}
	
}
