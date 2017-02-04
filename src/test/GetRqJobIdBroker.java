package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GetRqJobIdBroker {
	//to process job_unread.txt transferred from desktop
	public static void main(String[] args) throws IOException{
		final String path = "/opt/tests/";
		String broker_job_id = args[0];
				
				//to change job_unread.txt to job_read.txt
				File unread_file = new File(path + "job_unread.txt");
				if(!unread_file.exists()){
					return;
				}
				else{
					BufferedReader read_br = null;
					read_br = new BufferedReader(new FileReader(path + "job_read.txt"));
					String sCurrentLine;
					String redqueen_job_id;
					//String broker_job_id;
					while ((sCurrentLine = read_br.readLine()) != null) {
						if (sCurrentLine.contains(broker_job_id)){
							redqueen_job_id = sCurrentLine.split(":")[0];
							//to store the data into database
							break;
						}
				}
				}
	}

}
