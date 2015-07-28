package hijac.tools.tightrope.generators;

import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import hijac.tools.tightrope.environments.AperiodicEventHandlerEnv;
import hijac.tools.tightrope.environments.ManagedThreadEnv;
import hijac.tools.tightrope.environments.MissionEnv;
import hijac.tools.tightrope.environments.NestedMissionSequencerEnv;
import hijac.tools.tightrope.environments.OneShotEventHandlerEnv;
import hijac.tools.tightrope.environments.PeriodicEventHandlerEnv;
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

/**
 * Generates the Circus files of the environments that have been translated form a program.
 * 
 * @author Matt Luckcuck
 *
 */
public class CircusGenerator
{
	private static final String TEMPLATE_DIRECTORY = "/home/matt/Documents/Translation/test/templates";
	private freemarker.template.Configuration cfg;
	private ProgramEnv programEnv;
	private static final String OUTPUT_DIRECTORY = "/home/matt/Documents/Translation/test/output/";
	
	private String programName;
	
	/**
	 *
	 * @param programName The name of the program being translated
	 * @param programEnv The environment of the program that is being translated
	 */
	public CircusGenerator(String programName, ProgramEnv programEnv)
	{
		/* You should do this ONLY ONCE in the whole application life-cycle: */

		/* Create and adjust the configuration singleton */
		cfg = new freemarker.template.Configuration();

		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

		try
		{
			cfg.setDirectoryForTemplateLoading(new File(
					TEMPLATE_DIRECTORY));
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		this.programEnv = programEnv;
					
	
		this.programName = programName;
		
	}

	/**
	 * This method triggers the translation of the program, each type of object is translated to Circus and output to file.
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void translate() throws IOException, FileNotFoundException
	{
		System.out.println("+++ Translating +++");
		System.out.println();

		translateNetwork();

		translateSafelet();

		translateTopLevelMissionSequencers();

		translateMissions();

		translateManagedThreads();
		
		translateNestedMissionSequencers();
		
		translateOneShotEventHandlers();
		
		translateAperiodicEventHandlers();
		
		translatePeriodicEventHandlers();
		
		translateIDFiles();
		
		generateReport();

	}

	/**
	 * Used by all the translate methods. This processes a given Map with a given template and outputs it with a given filename
	 * @param root Map of data
	 * @param template The name of the template
	 * @param fileName The file name to output the translated file to
	 */
	@SuppressWarnings("rawtypes")
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
			new File(OUTPUT_DIRECTORY +  programName).mkdirs();
			
			File file = new File(OUTPUT_DIRECTORY +  programName+ "/"+ fileName);
			
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
				
	//			out.close();
			} catch (TemplateException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e2)
			{
				e2.printStackTrace();
			}
	
		
			// Note: Depending on what `out` is, you may need to call `out.close()`.
			// This is usually the case for file output, but not for servlet output.
		}

	@SuppressWarnings("rawtypes")
	private void translateIDFiles()
	{
		System.out.println("+++ Generating ID Files +++");
		Map Ids = programEnv.getMissionIdsMap();
		
		translateCommon(Ids, "MissionIds-Template.ftl", "MissionIds.circus");
		
		Ids = programEnv.getSchedulableIdsMap();
		
		translateCommon(Ids, "SchedulableIds-Template.ftl", "SchedulableIds.circus");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void generateReport()
	{
		System.out.println("+++ Generating Report +++");
		Map root = programEnv.geNetworkMap();
		root.put("programName", programName);
		
		translateCommon(root, "Report-Template.ftl", programName+"-Report.tex");
	}

	@SuppressWarnings("rawtypes")
	private void translateNetwork()
	{
		System.out.println("+++ Translating Network +++ ");
		/* Create a data-model */
		Map root = programEnv.geNetworkMap();

		translateCommon(root, "Network-Template.ftl", "Network.circus");

		System.out.println("Network = " + root);

	}

	@SuppressWarnings("rawtypes")
	private void translateSafelet()
	{
		// *****************************************Safelet++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		System.out.println("+++ Translating Safelet +++ ");
		/* Create a data-model */
		Map root = programEnv.getSafelet().toMap();

		String name = (String) root.get("ProcessID");
		
		translateCommon(root, "SafeletApp-Template.ftl", name+"App.circus");
	}

	@SuppressWarnings("rawtypes")
	private void translateTopLevelMissionSequencers()
	{
		System.out.println("+++ Translating Top Level Mission Sequencers +++");
		// ********************************************Top Level MS
		// +++++++++++++++++++++++++++++++++++++++++++++++
		for (TopLevelMissionSequencerEnv tlmsEnv : programEnv
				.getTopLevelMissionSequencers())
		{
			/* Create a data-model */
			Map tlms = tlmsEnv.toMap();

			
			String name = (String) tlms.get("ProcessID");
			// String procName = (String) tlms.get("MissionSequencerName");
			translateCommon(tlms, "MissionSequencerApp-Template.ftl",
					name+"App.circus");

			// /* Get the template (uses cache internally) */
			// freemarker.template.Template temp2 =
			// cfg.getTemplate("MissionSequencerApp-Template.ftl");
			//
			//
			//
			// /* Variables for writing output to a file */
			// // String procName = (String) tlms.get("MissionSequencerName");
			// String procName = "TopLevelMissionSequencerApp";
			// File file2 = new
			// File("/home/matt/Documents/Translation/test/output/"+procName+".circus");
			// FileOutputStream fop2 = new FileOutputStream(file2);
			//
			//
			//
			// /* Merge data-model with template */
			// Writer out3 = new OutputStreamWriter(fop2);
			// try
			// {
			// temp2.process(tlms, out3);
			// } catch (TemplateException e1)
			// {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			// // Note: Depending on what `out` is, you may need to call
			// `out.close()`.
			// // This is usually the case for file output, but not for servlet
			// output.
			//

		}
	}

	@SuppressWarnings("rawtypes")
	private void translateMissions()
	{
		System.out.println("+++ Translating Missions +++");
		// ***************************************** Missions
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++
		for (MissionEnv mEnv : programEnv.getMissions())
		{
			/* Create a data-model */
			Map missionMap = mEnv.toMap();

			String name = (String) missionMap.get("ProcessID");
			
			String procName = name+"App.circus";
			translateCommon(missionMap, "MissionApp-Template.ftl", procName);

			// /* Get the template (uses cache internally) */
			// freemarker.template.Template temp3 =
			// cfg.getTemplate("MissionApp-Template.ftl");
			//
			//
			//
			// /* Variables for writing output to a file */
			// // String procName = (String) tlms.get("MissionSequencerName");
			// String procName = "MissionApp";
			// File file3 = new
			// File("/home/matt/Documents/Translation/test/output/"+procName+".circus");
			// FileOutputStream fop3 = new FileOutputStream(file3);
			//
			//
			//
			// /* Merge data-model with template */
			// Writer out3 = new OutputStreamWriter(fop3);
			// try
			// {
			// temp3.process(missionMap, out3);
			// } catch (TemplateException e1)
			// {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			// // Note: Depending on what `out` is, you may need to call
			// `out.close()`.
			// // This is usually the case for file output, but not for servlet
			// output.
			//

		}
	}

	@SuppressWarnings("rawtypes")
	private void translateNestedMissionSequencers()
	{
		System.out.println("+++ Translating Nested Mission Sequencers +++");
		// ********************************************Top Level MS
		// +++++++++++++++++++++++++++++++++++++++++++++++
		for (NestedMissionSequencerEnv smsEnv : programEnv
				.getNestedMissionSequencers())
		{
			/* Create a data-model */
			Map tlms = smsEnv.toMap();
			
			String name = (String) tlms.get("ProcessID");
			// String procName = (String) tlms.get("MissionSequencerName");
			translateCommon(tlms, "MissionSequencerApp-Template.ftl",
					name+"App.circus");
		}		
	}

	@SuppressWarnings("rawtypes")
	private void translateManagedThreads()
	{
		System.out.println("+++ Translating Managed Threads +++ ");
		// ***************************************** Managed Threads
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++
		ArrayList<ManagedThreadEnv> mts = programEnv.getManagedThreads();
		for (ManagedThreadEnv mtEnv : mts)
		{
			/* Create a data-model */
			Map mtMap = mtEnv.toMap();

			String procName = mtEnv.getName().toString();
			translateCommon(mtMap, "ManagedThreadApp-Template.ftl", procName
					+ "App.circus");
			
			mtMap = mtEnv.getClassEnv().toMap();
			translateCommon(mtMap, "Class-Template.ftl", procName
					+ "Class.circus");

			// /* Get the template (uses cache internally) */
			// freemarker.template.Template temp4 =
			// cfg.getTemplate("ManagedThreadApp-Template.ftl");
			//
			//
			//
			// /* Variables for writing output to a file */
			// // String procName = (String) tlms.get("MissionSequencerName");
			// String procName = mtEnv.getName().toString();
			// File file4 = new
			// File("/home/matt/Documents/Translation/test/output/"+procName+"App.circus");
			// FileOutputStream fop4 = new FileOutputStream(file4);
			//
			//
			//
			// /* Merge data-model with template */
			// Writer out4 = new OutputStreamWriter(fop4);
			// try
			// {
			// temp4.process(mtMap, out4);
			// } catch (TemplateException e1)
			// {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			// // Note: Depending on what `out` is, you may need to call
			// `out.close()`.
			// // This is usually the case for file output, but not for servlet
			// output.
			//

		}
	}
	
	@SuppressWarnings("rawtypes")
	private void translateOneShotEventHandlers()
	{
		System.out.println("+++ Translating One Shot Event Handlers +++");
		// ********************************************Top Level MS
		// +++++++++++++++++++++++++++++++++++++++++++++++
		for (OneShotEventHandlerEnv osevEnv : programEnv
				.getOneShotEventHandlers())
		{
			/* Create a data-model */
			Map osehMap = osevEnv.toMap();
			
			String name = (String) osehMap.get("ProcessID");
			translateCommon(osehMap, "HandlerApp-Template.ftl",
					name+"App.circus");
		}		
	}
	
	@SuppressWarnings("rawtypes")
	private void translatePeriodicEventHandlers()
	{
		System.out.println("+++ Translating Periodic Event Handlers +++");
		// ********************************************Top Level MS
		// +++++++++++++++++++++++++++++++++++++++++++++++
		for (PeriodicEventHandlerEnv pehEnv : programEnv
				.getPeriodicEventHandlers())
		{
			/* Create a data-model */
			Map pehhMap = pehEnv.toMap();
			
			String name = (String) pehhMap.get("ProcessID");
			translateCommon(pehhMap, "HandlerApp-Template.ftl",
					name+"App.circus");
		}		
	}
	
	@SuppressWarnings("rawtypes")
	private void translateAperiodicEventHandlers()
	{
		System.out.println("+++ Translating One Shot Event Handlers +++");
		// ********************************************Top Level MS
		// +++++++++++++++++++++++++++++++++++++++++++++++
		for (AperiodicEventHandlerEnv apehEnv : programEnv
				.getAperiodicEventHandlers())
		{
			/* Create a data-model */
			Map apehMap = apehEnv.toMap();
			
			String name = (String) apehMap.get("ProcessID");
			translateCommon(apehMap, "HandlerApp-Template.ftl",
					name+"App.circus");
		}		
	}

}