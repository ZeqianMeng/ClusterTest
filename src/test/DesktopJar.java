package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DesktopJar {
	
	public static void main(String[] args) {

		long id = Long.parseLong(args[0]);
		String response = getId(id);
		System.out.println(response);
		//long id = getId(55555);
	}
	
	/*public static String getId(long contract_id){
		//to use contract_id to process info in file
		return "hello" + contract_id;
	}*/
	
	public static String getId(long contract_id){
		//to use contract_id to process info in file
		
		final String file = "/home/MSC11/mengz/Desktop/tests/CompleteJobs.txt";
		
		BufferedReader br = null;
		String comp_time = "";

		try {

			String sCurrentLine;
			
			br = new BufferedReader(new FileReader(file));
			long temp_id;

			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				temp_id = Long.parseLong(sCurrentLine.split(":")[0]);
				if(temp_id == contract_id){
					comp_time = sCurrentLine.split(":")[1];
					break;
				}
				//output = output + sCurrentLine;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return comp_time;
	}

}
