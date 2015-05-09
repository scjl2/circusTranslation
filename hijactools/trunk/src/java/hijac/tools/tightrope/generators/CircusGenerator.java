package hijac.tools.tightrope.generators;

import hijac.tools.application.TightRopeTest;
import hijac.tools.tightrope.environments.ManagedThreadEnv;
import hijac.tools.tightrope.environments.MissionEnv;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.environments.TopLevelMissionSequencerEnv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;


import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class CircusGenerator
{
	private freemarker.template.Configuration cfg;
	private ProgramEnv programEnv;
	private String fileLocation = "/home/matt/Documents/Translation/test/output/";
	
	
	public CircusGenerator(ProgramEnv programEnv)
	{
	    /* You should do this ONLY ONCE in the whole application life-cycle:        */    
    
        /* Create and adjust the configuration singleton */
        cfg = new freemarker.template.Configuration();

        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        
        try
		{
			cfg.setDirectoryForTemplateLoading(new File("/home/matt/Documents/Translation/test/templates"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
        
        this.programEnv = programEnv;
	}
	
	
	public void translate() throws IOException,
				FileNotFoundException
		{
			System.out.println("+++ Translating +++");                                                                     
			
	        
	        
	        
	translateSafelet();
	        
	    	   
	translateTopLevelMissionSequencers();
	        
	translateMissions();
	    
	    
	 translateManagedThreads();
	
	    
		}


	private void translateCommon(Map root, String template, String fileName)
	{
		/* Get the template (uses cache internally) */
		freemarker.template.Template temp = null;
		try
		{
			temp = cfg.getTemplate(template);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
		/* Variables for writing output to a file */
		File file = new File(fileLocation+fileName);
		FileOutputStream fop = null;
		try
		{
			fop = new FileOutputStream(file);
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
			
		
		/* Merge data-model with template */
		Writer out = new OutputStreamWriter(fop);
		try
		{
			temp.process(root, out);
		} catch (TemplateException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (IOException e2)
		{
			e2.printStackTrace();
		}
		
		// Note: Depending on what `out` is, you may need to call `out.close()`.
		// This is usually the case for file output, but not for servlet output.
	}


	private void translateSafelet() 
	{
		//*****************************************Safelet++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		        
		        /* Create a data-model */
		        Map root = programEnv.getSafelet().toMap();		        
		
		        translateCommon(root, "SafeletApp-Template.ftl", "SafeletApp.circus");
	}


	private void translateTopLevelMissionSequencers() 
	{
		//********************************************Top Level MS +++++++++++++++++++++++++++++++++++++++++++++++
	    for(TopLevelMissionSequencerEnv tlmsEnv : programEnv.getTopLevelMissionSequencers())
	    {
	        /* Create a data-model */
	        Map tlms = tlmsEnv.toMap();
	        
//	        String procName = (String) tlms.get("MissionSequencerName");
	        translateCommon(tlms, "MissionSequencerApp-Template.ftl", "TopLevelMissionSequencerApp.circus");
	
//	        /* Get the template (uses cache internally) */
//	        freemarker.template.Template temp2 = cfg.getTemplate("MissionSequencerApp-Template.ftl");
//	
//	        
//	        
//	        /* Variables for writing output to a file */
//	//        String procName = (String) tlms.get("MissionSequencerName");
//	        String procName = "TopLevelMissionSequencerApp";
//	        File file2 = new File("/home/matt/Documents/Translation/test/output/"+procName+".circus");
//	        FileOutputStream fop2 = new FileOutputStream(file2);
//			
//				
//	        
//	        /* Merge data-model with template */
//	        Writer out3 = new OutputStreamWriter(fop2);
//	        try
//			{
//				temp2.process(tlms, out3);
//			} catch (TemplateException e1)
//			{
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//	        // Note: Depending on what `out` is, you may need to call `out.close()`.
//	        // This is usually the case for file output, but not for servlet output.
//	        
	        
	    }
	}


	private void translateMissions() 
	{
		// ***************************************** Missions ++++++++++++++++++++++++++++++++++++++++++++++++++++       
	    for(MissionEnv mEnv : programEnv.getMissions())
	    {
	        /* Create a data-model */
	        Map missionMap = mEnv.toMap();

	        String procName = "MissionApp.circus";
	        translateCommon(missionMap, "MissionApp-Template.ftl", procName);
	
//	        /* Get the template (uses cache internally) */
//	        freemarker.template.Template temp3 = cfg.getTemplate("MissionApp-Template.ftl");
//	
//	        
//	        
//	        /* Variables for writing output to a file */
//	//        String procName = (String) tlms.get("MissionSequencerName");
//	        String procName = "MissionApp";
//	        File file3 = new File("/home/matt/Documents/Translation/test/output/"+procName+".circus");
//	        FileOutputStream fop3 = new FileOutputStream(file3);
//			
//				
//	        
//	        /* Merge data-model with template */
//	        Writer out3 = new OutputStreamWriter(fop3);
//	        try
//			{
//				temp3.process(missionMap, out3);
//			} catch (TemplateException e1)
//			{
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//	        // Note: Depending on what `out` is, you may need to call `out.close()`.
//	        // This is usually the case for file output, but not for servlet output.
//	        
	        
	    }
	}


	private void translateManagedThreads() 		
	{
		// ***************************************** Managed Threads ++++++++++++++++++++++++++++++++++++++++++++++++++++       
		    ArrayList<ManagedThreadEnv> mts = programEnv.getManagedThreads();
		    for(ManagedThreadEnv mtEnv : mts )
		    {
		        /* Create a data-model */
		        Map mtMap = mtEnv.toMap();

		        String procName = mtEnv.getName().toString();
		        translateCommon(mtMap, "ManagedThreadApp-Template.ftl", procName+"App.circus");
		        
		
//		        /* Get the template (uses cache internally) */
//		        freemarker.template.Template temp4 = cfg.getTemplate("ManagedThreadApp-Template.ftl");
//		
//		        
//		        
//		        /* Variables for writing output to a file */
//		//        String procName = (String) tlms.get("MissionSequencerName");
//		        String procName = mtEnv.getName().toString();
//		        File file4 = new File("/home/matt/Documents/Translation/test/output/"+procName+"App.circus");
//		        FileOutputStream fop4 = new FileOutputStream(file4);
//				
//					
//		        
//		        /* Merge data-model with template */
//		        Writer out4 = new OutputStreamWriter(fop4);
//		        try
//				{
//					temp4.process(mtMap, out4);
//				} catch (TemplateException e1)
//				{
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//		        // Note: Depending on what `out` is, you may need to call `out.close()`.
//		        // This is usually the case for file output, but not for servlet output.
//		        
		        
		    }
	}
	
}