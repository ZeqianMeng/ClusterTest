package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class BrokerAccounting {
	public static void main(String[] args) throws IOException {
		// to execute fetch_file_broker.sh to get accounting data from cluster
		// it will copy job_duration.txt to /opt/AHE3/
		String command = "/opt/AHE3/fetch_file_broker.sh";
		Runtime rt = Runtime.getRuntime();
	    rt.exec(command);
	    
	    // to read the file and update ontologies
	    final String path = "/opt/AHE3/";
		File job_file = new File(path + "job_duration.txt");
		
		if(job_file.exists()){
			Reader reader = new FileReader(job_file);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String line = bufferedReader.readLine();
			if(!line.isEmpty()){
				String duration = line.split(":")[1];
				//Ont
			}
		}
	    
	}

}
