package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClusterAccounting {
	
	public static void main(String[] args) throws IOException {
		final String file_path = "/opt/test/";
		//final String job_unread = "job_unread.txt";
		File job_unread = new File(file_path + "job_unread.txt");
		//if there is job complete data not been processed
		if(job_unread.exists() && !job_unread.isDirectory()) { 
			//to process the data
			//to rename file_unread to file_read
			File file_read = new File(file_path + "job_read.txt");
			File file_broker = new File(file_path + "job_duration.txt");
			
			if(!file_broker.exists()){
				file_broker.createNewFile();
    		}
			FileWriter fileWritter = new FileWriter(file_broker.getName(),true);
	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			
			if(job_unread.renameTo(file_read)){
				BufferedReader br = new BufferedReader(new FileReader(file_read));
				String old_record;
			    while ((old_record = br.readLine()) != null) {
			    	String[] ids = old_record.split(":");
			    	String redqueen_job_id = ids[0];
			    	String broker_job_id = ids[1];
			    	String job_type = ids[2];
			    	String duration;
			    	String new_record = null;
			    	if(job_type.equalsIgnoreCase("s")){
			    	    duration = processSerialData(redqueen_job_id);
			    	    new_record = broker_job_id + ":" + duration;
			    	}
			    	if(job_type.equalsIgnoreCase("p")){
			    		duration = processParallelData(redqueen_job_id);
			    	    new_record = broker_job_id + ":" + duration;
			    	}
			    	bufferWritter.write(new_record);
			    }
			    bufferWritter.close();
			    br.close();
			    file_read.delete();
			}
			else{
				System.out.println("Rename file failed");
			}
		}
	}
	
	public static String processSerialData(String redqueen_job_id) throws IOException{
		String duration = null;
		String command = "qacct -j " + redqueen_job_id;
		Runtime rt = Runtime.getRuntime();
	    Process pr = rt.exec(command);
        //Process pr = rt.exec("ls /opt/test/ && sudo chmod 777 -R /opt/test/");
    
        String s = null;
    
        BufferedReader stdInput = new BufferedReader(new
            InputStreamReader(pr.getInputStream()));
    
        System.out.println("Here is the standard output of the command:\n");
        while ((s = stdInput.readLine()) != null && s.contains("cpu")) {
            System.out.println(s);
            duration = s.split(":")[1];
            break;
        }
        return duration;
	}
	
	public static String processParallelData(String redqueen_job_id) throws IOException{
		long duration = 0;
		String command = "qacct -j " + redqueen_job_id;
		Runtime rt = Runtime.getRuntime();
	    Process pr = rt.exec(command);
        //Process pr = rt.exec("ls /opt/test/ && sudo chmod 777 -R /opt/test/");
    
        String s = null;
    
        BufferedReader stdInput = new BufferedReader(new
            InputStreamReader(pr.getInputStream()));
    
        System.out.println("Here is the standard output of the command:\n");
        while ((s = stdInput.readLine()) != null && s.contains("cpu")) {
            System.out.println(s);
            duration = duration + Long.parseLong(s.split(":")[1]);
        }
        return Long.toString(duration);
	}

}
