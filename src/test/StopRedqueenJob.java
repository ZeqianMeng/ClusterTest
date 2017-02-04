package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class StopRedqueenJob {
	//user or manager to stop redqueen job
	//if its manager to stop, no need to get duration from redqueen
	public static void main(String[] args) throws IOException{
		String broker_job_id = args[0];
		final String path = "/home/MSC11/mengz/Desktop/tests/";
		//to process job_unread.txt for redqueen_job_id
		File unread_file = new File(path + "job_unread.txt");
		String redqueen_job_id = null;
		if(!unread_file.exists()){
			return;
		}
		else{
			BufferedReader unread_br = new BufferedReader(new FileReader(path + "job_unread.txt"));
			String sCurrentLine;
			
			while ((sCurrentLine = unread_br.readLine()) != null) {
				System.out.println(sCurrentLine);
				if(sCurrentLine.contains(broker_job_id)){
					redqueen_job_id = sCurrentLine.split(":")[0];
					break;
				}
			}
		}
		
		//to execute the scripts with redqueen_job_id as input
		Runtime rt = Runtime.getRuntime();
		String command = "/home/MSC11/mengz/Desktop/tests/stop_job_desktop.sh " + redqueen_job_id;
		rt.exec(command);
		
		//to get stopped job duration
	}

}
