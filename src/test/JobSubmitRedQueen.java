package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class JobSubmitRedQueen {
	public static void main(String[] args) throws IOException{
		//need to get input as broker_job_id, also for job submission parameters
		String numjobs = args[0];
		String nefold = args[1];
		String broker_job_id = args[2];
		int service_level = Integer.parseInt(args[3]);
		String redqueen_job_id = null;
		File wd = new File("/bin");
		System.out.println(wd);
		Process proc = null;
		
		try {
		   proc = Runtime.getRuntime().exec("/bin/bash", null, wd);
		}
		catch (IOException e) {
		   e.printStackTrace();
		}
		
		BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
		
		final String path = "/mnt/iusers01/zk01/mbaxjzm3/";
		File unread_file = new File(path + "job_unread.txt");
		if(!unread_file.exists()){
			unread_file.createNewFile();
		}
		//write redqueen_job_id and broker_job_id to job_unread.txt
		FileWriter fileWritter = new FileWriter(unread_file.getName(),true);
        PrintWriter pw = new PrintWriter(fileWritter);
		
		if (service_level == 1){
			if (proc != null) {
				   
				   out.println("cd /mnt/iusers01/zk01/mbaxjzm3/scratch/serial");
				   out.println("./submitjob.sh" + " " + numjobs + " 5 7 " + nefold);
				   try {
				      String line;
				      while ((line = in.readLine()) != null) {
				    	  System.out.println("******** here");
				         System.out.println(line);
				         if(line.contains("job")){
				        	 System.out.println("********" + line);
				        		redqueen_job_id = line.substring(15, 20);
				        		pw.println(redqueen_job_id + ":" + broker_job_id + ":s");
				        		System.out.println("job id is: " + redqueen_job_id);
				        		break;
				        	}
				      }
				      //proc.waitFor();
				      in.close();
				      out.close();
				      pw.close();
				      //proc.destroy();
				   }
				   catch (Exception e) {
				      e.printStackTrace();
				   }
			//command = "/bin/cd /mnt/iusers01/zk01/mbaxjzm3/scratch/serial && ./submitjob.sh" + " " + numjobs + " 5 7 " + nefold;
		}
		}
		if (service_level > 1){
			
			out.println("cd /mnt/iusers01/zk01/mbaxjzm3/scratch/parallel");
			   out.println("./submitjob.sh" + " " + numjobs + " 6.0 6.1 " + nefold);
			   try {
			      String line;
			      while ((line = in.readLine()) != null) {
			         System.out.println(line);
			         if(line.contains("job")){
			        		redqueen_job_id = line.substring(15, 20);
			        		System.out.println("job id is: " + redqueen_job_id);
			        		pw.println(redqueen_job_id + ":" + broker_job_id + ":p");
			        		break;
			        	}
			      }
			      //proc.waitFor();
			      in.close();
			      out.close();
			      pw.close();
			      //proc.destroy();
			   }
			   catch (Exception e) {
			      e.printStackTrace();
			   }
			//command = "/bin/cd /mnt/iusers01/zk01/mbaxjzm3/scratch/serial && ./submitjob.sh" + " " + numjobs + " 6.0 6.1 " + nefold;
		}
	}

}
