package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.restlet.data.Form;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class ClusterClient {
	public static void main(String[] args) throws IOException {
		//final String path = "/home/MSC11/mengz/Desktop/tests/";
		//File temp_file = new File(path + "temp.txt");
		//temp_file.createNewFile();
		
		//-------start BrokerAccounting
		// to execute fetch_file_broker.sh to get accounting data from cluster
		// it will copy job_duration.txt to /opt/AHE3/
		/*String command = "/opt/test/fetch_file_broker.sh";
		Runtime rt = Runtime.getRuntime();
	    rt.exec(command);
	    
	    // to read the file and update ontologies
	    final String path = "/opt/test/";
		File job_file = new File(path + "job_duration.txt");
		
		long start = System.currentTimeMillis();
		System.out.println("sophia " + start);
        long end = start + 20*1000; // 60 seconds * 1000 ms/sec
        System.out.println("hi     " + end);
        while (System.currentTimeMillis() < end)
            {
            // run
        	int i = 0;
        	    System.out.println("hello" + start + " " + end + " " + i);
        	    i++;
            }
        System.out.println("lasse  " + System.currentTimeMillis());
		
		if(job_file.exists()){
			Reader reader = new FileReader(job_file);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String line;
			while((line = bufferedReader.readLine()) != null){
				long broker_job_id = Long.parseLong(line.split(":")[0]);
				long duration = Long.parseLong(line.split(":")[1]);
				System.out.println("*********** " + broker_job_id + "**** " + duration);
				//to get contract_id of the broker_job_id
				long contract_id = NegotiationDB.getConFromJob(broker_job_id);
				String state = NegotiationDB.getContractStatus(contract_id);
		   		  if(state.equalsIgnoreCase(NegState.Contracted.toString())){
				      OntUpdate.mPolicyShareCompleteReduce(contract_id, duration);
		   		  }
			}
		}*/
		
		//-------end BrokerAccounting
		
		
		//Cluster file writing test
	/*	System.out.println("1    hi I am here");
		final String file_path = "/mnt/iusers01/zk01/mbaxjzm3/data/test/";
		File test = new File(file_path + "test.txt");
		FileWriter fileWritter = new FileWriter(test.getName(),true);
        PrintWriter pw = new PrintWriter(fileWritter);
        pw.println("hello");
        pw.close();*/
		
	/*	final String file_path = "/mnt/iusers01/zk01/mbaxjzm3/data/test/";
		File job_unread = new File(file_path + "job_unread.txt");
		if(!job_unread.exists()){
			return;
		}
		else{
			job_unread.delete();
		}*/
		
		//-------start ClusterAccounting
		System.out.println("1    hi I am here");
		final String file_path = "/mnt/iusers01/zk01/mbaxjzm3/data/test/";
		//final String job_unread = "job_unread.txt";
		File job_unread = new File(file_path + "job_unread.txt");
		//if there is job complete data not been processed
		if(job_unread.exists()) { 
			//to process the data
			//to rename file_unread to file_read
			File file_read = new File(file_path + "job_read.txt");
			File file_broker = new File(file_path + "job_duration.txt");
			System.out.println("2    hi I am here");
			if(!file_broker.exists()){
				file_broker.createNewFile();
    		}
			FileWriter fileWritter = new FileWriter(file_broker.getName(),true);
			PrintWriter pw = new PrintWriter(fileWritter);
	        //BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			
			if(job_unread.renameTo(file_read)){
				String command_1 = "chmod 777 /mnt/iusers01/zk01/mbaxjzm3/data/test/job_read.txt";
				//String command = "ls /opt/";
			    Runtime rt = Runtime.getRuntime();
			    rt.exec(command_1);
			    
			    String command_2 = "chmod 777 /mnt/iusers01/zk01/mbaxjzm3/data/test/job_duration.txt";
				//String command = "ls /opt/";
			    //Runtime rt_2 = Runtime.getRuntime();
			    rt.exec(command_2);
			    
				BufferedReader br = new BufferedReader(new FileReader(file_read));
				String old_record;
				String duration = null;
				String cluster_command;
			    while ((old_record = br.readLine()) != null) {
			    	String[] ids = old_record.split(":");
			    	String redqueen_job_id = ids[0];
			    	//System.out.println("------ redqueen job id: " + redqueen_job_id);
			    	String broker_job_id = ids[1];
			    	//System.out.println("++++++ broker job id: " + broker_job_id);
			    	String job_type = ids[2];
			    	//System.out.println("====== job type: " + job_type);
			    	//String duration;
			    	String new_record = null;
			    	if(job_type.equalsIgnoreCase("s")){
			    		cluster_command = "qacct -j " + redqueen_job_id;
			    	    Process pr = rt.exec(cluster_command);
			    	    //Process pr = rt.exec("ls /opt/test/ && sudo chmod 777 -R /opt/test/");

			    	    String s = null;

			    	    BufferedReader stdInput = new BufferedReader(new
			    	        InputStreamReader(pr.getInputStream()));

			    	    System.out.println("Here is the standard output of the command:\n");
			    	    while ((s = stdInput.readLine()) != null){
			    	    //while ((s = stdInput.readLine()) != null && s.contains("cpu")) {
			    	    	if(s.contains("cpu")){
			    	            System.out.println(s);
			    	            duration = s.substring(13, 19);
			    	            new_record = broker_job_id + ":" + duration;
					    	    pw.println(new_record);
			    	            break;
			    	    	}
			    	    }
			    	    //duration = processSerialData(redqueen_job_id);
			    	}
			    	if(job_type.equalsIgnoreCase("p")){
			    		double pallel_duration = 0;
			    		String command = "qacct -j " + redqueen_job_id;
			    	    Process pr = rt.exec(command);
			    	    //Process pr = rt.exec("ls /opt/test/ && sudo chmod 777 -R /opt/test/");

			    	    String s = null;

			    	    BufferedReader stdInput = new BufferedReader(new
			    	        InputStreamReader(pr.getInputStream()));

			    	    System.out.println("Here is the standard output of the command:\n");
			    	    while ((s = stdInput.readLine()) != null){
			    	    	if(s.contains("cpu")){
			    	    //while ((s = stdInput.readLine()) != null && s.contains("cpu")) {
			    	            System.out.println(s);
			    	            pallel_duration = pallel_duration + Double.parseDouble(s.substring(13, 19));
			    	            new_record = broker_job_id + ":" + Double.toString(pallel_duration);
					    	    pw.println(new_record);
			    	    	}
			    	    }
			    		//duration = processParallelData(redqueen_job_id);

			    	}
			    	//pw.println(new_record);
			    	//bufferWritter.write(new_record);
			    	//bufferWritter.newLine();
			    }
			    pw.close();
			    //bufferWritter.close();
			    br.close();
			    //file_read.delete();
			}
			else{
				System.out.println("Rename file failed");
			}
		}
	}
	
	//-------start ClusterAccounting
/*public static String processSerialData(String redqueen_job_id) throws IOException{
	String duration = null;
	String command = "qacct -j " + redqueen_job_id;
	Runtime rt = Runtime.getRuntime();
    Process pr = rt.exec(command);
    //Process pr = rt.exec("ls /opt/test/ && sudo chmod 777 -R /opt/test/");

    String s = null;

    BufferedReader stdInput = new BufferedReader(new
        InputStreamReader(pr.getInputStream()));

    System.out.println("Here is the standard output of the command:\n");
    while ((s = stdInput.readLine()) != null){
    //while ((s = stdInput.readLine()) != null && s.contains("cpu")) {
    	if(s.contains("cpu")){
            System.out.println(s);
            duration = s.substring(13, 19);
            break;
    	}
    }
    return duration;
}

public static String processParallelData(String redqueen_job_id) throws IOException{
	double duration = 0;
	String command = "qacct -j " + redqueen_job_id;
	Runtime rt = Runtime.getRuntime();
    Process pr = rt.exec(command);
    //Process pr = rt.exec("ls /opt/test/ && sudo chmod 777 -R /opt/test/");

    String s = null;

    BufferedReader stdInput = new BufferedReader(new
        InputStreamReader(pr.getInputStream()));

    System.out.println("Here is the standard output of the command:\n");
    while ((s = stdInput.readLine()) != null){
    	if(s.contains("cpu")){
    //while ((s = stdInput.readLine()) != null && s.contains("cpu")) {
            System.out.println(s);
            duration = duration + Double.parseDouble(s.substring(13, 19));
    	}
    }
    return Double.toString(duration);
}*/
//-------emd ClusterAccounting
	//-------end ClusterAccounting
		/*String redqueen_job_id = args[0];
	String duration = null;
	String command = "qacct -j " + redqueen_job_id;
	Runtime rt = Runtime.getRuntime();
    Process pr = rt.exec(command);
    //Process pr = rt.exec("ls /opt/test/ && sudo chmod 777 -R /opt/test/");

    String s = null;

    BufferedReader stdInput = new BufferedReader(new
        InputStreamReader(pr.getInputStream()));

    System.out.println("Here is the standard output of the command:\n");
    while ((s = stdInput.readLine()) != null){
    //while ((s = stdInput.readLine()) != null && s.contains("cpu")) {
    	if(s.contains("cpu")){
            System.out.println(s);
            duration = s.substring(13, 19);
            System.out.println(duration);
            break;
    	}
    }*/
	
		//------start CheckJobComplete
       /* String redqueen_job_id = args[0];
		
		String command = "qstat";
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(command);
		BufferedReader stdInput = new BufferedReader(new
	            InputStreamReader(pr.getInputStream()));
	
		
		String s;
		final String path = "/mnt/iusers01/zk01/mbaxjzm3/data/test/";
		File job_file = new File(path + "redqueen_job_id.txt");
		
		if(!job_file.exists()){
			job_file.createNewFile();
		}
		
		FileWriter fileWritter = new FileWriter(job_file.getName(),true);
        PrintWriter pw = new PrintWriter(fileWritter);
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
		}*/
		//------end CheckJobComplete
		
		//String command = null;
		/*String numjobs = args[0];
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
		}*/

		/*Process pr = rt.exec(command);
		BufferedReader stdInput = new BufferedReader(new
	            InputStreamReader(pr.getInputStream()));
		
		String s;
		String redqueen_job_id = null;
		while ((s = stdInput.readLine()) != null){
        	//String s = stdInput.readLine();
        	System.out.println(s);
        	if(s.equalsIgnoreCase("job")){
        		redqueen_job_id = s.substring(15, 20);
        		System.out.println("job id is: " + redqueen_job_id);
        	}
        	//pw.println(s);
		}
		stdInput.close();*/
		
		//write to job_unread.txt file
		//final String path = "/home/MSC11/mengz/Desktop/tests/";
/*		
        if(service_level == 1){
            pw.println(redqueen_job_id + ":" + broker_job_id + ":s");
        }
        else if (service_level > 1){
        	pw.println(redqueen_job_id + ":" + broker_job_id + ":p");
        }
        pw.close();*/
	
		/*Form form = new Form(); 
		ClientResource resource = new ClientResource("http://ec2-52-208-119-247.eu-west-1.compute.amazonaws.com:8080/steerservice/neg");

		form.add("username", "Sofia");
		form.add("group", "ManGroup");
		form.add("appname","WaterSteering");
		form.add("start_time","asap");
		
		try {
	        resource.post(form).write(System.out);
			//resource.get().write(System.out);
 
		} catch (ResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//long id = Long.parseLong(args[0]);
		//long id = 99999;
		//createEntry(id);
		//System.out.println("done");
		
		/*final String path = "/mnt/iusers01/zk01/mbaxjzm3/data/test/";
		
		//to change job_unread.txt to job_read.txt
		File temp_file = new File(path + "temp.txt");
		temp_file.createNewFile();
		
		FileWriter fileWritter = new FileWriter(temp_file.getName(),true);
		PrintWriter pw = new PrintWriter(fileWritter);
		
		String command;
		Runtime rt = Runtime.getRuntime();
		command = "qacct -j 74514";

		Process pr = rt.exec(command);
		BufferedReader stdInput = new BufferedReader(new
	            InputStreamReader(pr.getInputStream()));
		
		String s;
		while ((s = stdInput.readLine()) != null){
        	//String s = stdInput.readLine();
        	System.out.println(s);
        	pw.println(s);
		}
		stdInput.close();
		pw.close();*/
		//bufferWritter.close();
		//String redqueen_job_id = "74514";
		
	    
        //Process pr = rt.exec("ls /opt/test/ && sudo chmod 777 -R /opt/test/");
        
	
	/*public static void createEntry(long job_id) throws IOException{
		// to generate complete time stamp
		Calendar cal1 = Calendar.getInstance();
	    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd");
	    Calendar cal2 = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
   	    String stamp = sdf1.format(cal1.getTime()) + "T" + sdf.format(cal2.getTime()) + "+7:00";
   	    System.out.println(stamp);
   	    
   	    String content = job_id + ";" + stamp;
   	    
   	    //to create an entry in the CompleteJobs file
   	    //final String file_path = "/Users/zeqianmeng/Desktop/CompleteJobs.txt";
   	    final String file_path = "/opt/test/CompleteJobs.txt";
   	    File file = new File(file_path);
        //BufferedReader br = null;
        if(!file.exists()){
        	//to create a file
        	file.createNewFile();
        }
        //to add an entry
        Writer writer = new FileWriter(file, true);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        bufferedWriter.write(content);
        bufferedWriter.newLine();
        //Files.write(Paths.get("myfile.txt"), "the text".getBytes(), StandardOpenOption.APPEND);
        if(bufferedWriter != null) bufferedWriter.close();

	}*/

}
