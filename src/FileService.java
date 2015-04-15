import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileService {
	public void createFile(String fileName) {
		File f = new File(fileName);
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.toString());
		}
	}

	public void writeToFile(String fileName, boolean append, String content) {
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(
					new FileWriter(fileName, append)));
			writer.println(content);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void readFromFile(String fileName) {
		BufferedReader reader = null;
		String curLine = "";

		try {
			reader = new BufferedReader(new FileReader(fileName));
			while ((curLine = reader.readLine()) != null) {
				System.out.println(curLine);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static ArrayList<String> readCardList() {
		BufferedReader reader = null;
		String curLine = "";
		ArrayList<String>cardNames = new ArrayList<String>();

		try {
			reader = new BufferedReader(new FileReader("C:\\Users\\jms_m_000\\AppData\\Local\\Cockatrice\\cards.xml"));
			while ((curLine = reader.readLine()) != null) {
				if(curLine.contains("<name>")){
					cardNames.add(curLine.substring(curLine.indexOf('<')
							+ "name> ".length(),
							curLine.length() - "</name>".length()));
				}				
			}
			
			return cardNames;
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}

	public static ArrayList<String> readSetList() {
		BufferedReader reader = null;
		String curLine = "";
		ArrayList<String>cardNames = new ArrayList<String>();
		String lastLine = "";

		try {
			reader = new BufferedReader(new FileReader("C:\\Users\\jms_m_000\\AppData\\Local\\Cockatrice\\cards.xml"));
			while ((curLine = reader.readLine()) != null) {
				if (lastLine.contains("<set muId=") && !curLine.contains("<set muId=")) {
					cardNames.add(lastLine.substring(
									lastLine.indexOf(">") + 1,
									lastLine.indexOf(">") + 4));
					System.out.println(lastLine.substring(
									lastLine.indexOf(">") + 1,
									lastLine.indexOf(">") + 4));
				}
				
				lastLine = curLine;
			}
			
			return cardNames;
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
}
