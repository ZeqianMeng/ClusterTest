package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class CheckJobComplete {
	//this is placed in redqueen
	public static void main(String[] args) throws IOException{
		String redqueen_job_id = args[0];
		
		String command = "qstat";
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(command);
		BufferedReader stdInput = new BufferedReader(new
	            InputStreamReader(pr.getInputStream()));
	
		
		String s;
		/*final String path = "/mnt/iusers01/zk01/mbaxjzm3/data/test/";
		File job_file = new File(path + "redqueen_job_id.txt");
		
		if(!job_file.exists()){
			job_file.createNewFile();
		}
		
		FileWriter fileWritter = new FileWriter(job_file.getName(),true);
        PrintWriter pw = new PrintWriter(fileWritter);*/
		//boolean complete = false;
		while ((s = stdInput.readLine()) != null){
			if(s.contains(redqueen_job_id)){
				//job not complete yet
				//complete = false;
				
				//if not complete, create a file with redqueen_job_id as file name
		        //pw.println("1");
		        //pw.close();
		        //to kill the job in the queue
		        String kill_command = "qdel " + redqueen_job_id;
		        rt.exec(kill_command);
		        
		        
		        final String path = "/mnt/iusers01/zk01/mbaxjzm3/data/test/";
				File job_file = new File(path + "redqueen_job_id.txt");
				
				if(!job_file.exists()){
					job_file.createNewFile();
				}
				
				//FileWriter fileWritter = new FileWriter(job_file.getName(),true);
		        //scp the file to desktop
		        String scp_to_desktop = "/mnt/iusers01/zk01/mbaxjzm3/data/test/ScpToDesktop.sh " + redqueen_job_id;
		        rt.exec(scp_to_desktop);
		        break;
			}
		}
		
	}

}
