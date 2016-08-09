package hijac.tools.tightrope.generators;

import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import hijac.tools.application.TightRope;
import hijac.tools.tightrope.environments.AperiodicEventHandlerEnv;
import hijac.tools.tightrope.environments.ClassEnv;
import hijac.tools.tightrope.environments.ManagedThreadEnv;
import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.environments.MissionEnv;
import hijac.tools.tightrope.environments.NestedMissionSequencerEnv;
import hijac.tools.tightrope.environments.NonParadigmEnv;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.environments.OneShotEventHandlerEnv;
import hijac.tools.tightrope.environments.PeriodicEventHandlerEnv;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.environments.SafeletEnv;
import hijac.tools.tightrope.environments.TopLevelMissionSequencerEnv;
import hijac.tools.tightrope.utils.Debugger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates the Circus files of the environments that have been translated form
 * a program.
 * 
 * @author Matt Luckcuck
 * 
 */
public class CircusGenerator
{
	private static final String NETWORK_PROGRAM = "NetworkProgram.circus";
	private static final String NETWORK_PROGRAM_TEMPLATE_FTL = "NetworkProgram-Template.ftl";
	private static final String NETWORK_LOCKING = "NetworkLocking.circus";
	private static final String NETWORK_LOCKING_TEMPLATE_FTL = "NetworkLocking-Template.ftl";
	private static final String NETWORK_METHOD_CALL_BINDER = "NetworkMethodCallBinder.circus";
	private static final String NETWORK_METHOD_CALL_BINDER_TEMPLATE_FTL = "NetworkMethodCallBinder-Template.ftl";
	private static final String NETWORK_CHAN = "NetworkChan.circus";
	private static final String NETWORK_CHAN_TEMPLATE_FTL = "NetworkChan-Template.ftl";
	private static final String ID_TYPE = "IDType";
	private static final String CLASS_CIRCUS = "Class.circus";
	private static final String CLASS_TEMPLATE_FTL = "Class-Template.ftl";
	private static final String FILE_DELIMITER = "/";
	private static final String SCHEDULABLE_IDS_CIRCUS = "SchedulableIds.circus";
	private static final String SCHEDULABLE_IDS_TEMPLATE_FTL = "SchedulableIds-Template.ftl";
	private static final String MISSION_IDS_CIRCUS = "MissionIds.circus";
	private static final String MISSION_IDS_TEMPLATE_FTL = "MissionIds-Template.ftl";
	private static final String REPORT_TEX = "-Report.tex";
	private static final String REPORT_TEMPLATE_FTL = "Report-Template.ftl";
	private static final String NETWORK_CIRCUS = "Network.circus";
	private static final String NETWORK_TEMPLATE_FTL = "Network-Template.ftl";
	private static final String NON_PARADIGM_APP_TEMPLATE_FTL = "NonParadigmApp-Template.ftl";
	private static final String SAFELET_APP_TEMPLATE_FTL = "SafeletApp-Template.ftl";
	private static final String MISSION_APP_TEMPLATE_FTL = "MissionApp-Template.ftl";
	private static final String MISSION_SEQUENCER_APP_TEMPLATE_FTL = "MissionSequencerApp-Template.ftl";
	private static final String HANDLER_APP_TEMPLATE_FTL = "HandlerApp-Template.ftl";
	private static final String PROCESS_Name = "ProcessName";
	private static final String APP_CIRCUS = "App.circus";
	private static final String TEMPLATE_DIRECTORY = "../templates";
	private static final String OUTPUT_DIRECTORY = "../output/";
	private static final String CUSTOM_CHANNELS_TEMPLATE_FTL = "CustomChannels-Templat.ftl";
	private static final String THREAD_IDS_TEMPLATE_FTL = "ThreadIds-Template.ftl";
	private static final String THREAD_IDS_CIRCUS = "ThreadIds.circus";
	private static final String OBJECT_IDS_TEMPLATE_FTL = "ObjectIds-Template.ftl";
	private static final String OBJECT_IDS_CIRCUS = "ObjectIds.circus";

	private freemarker.template.Configuration cfg;
	private ProgramEnv programEnv;
	private String programName;

	private String procName;

	/**
	 * 
	 * @param programName
	 *            The name of the program being translated
	 * @param programEnv
	 *            The environment of the program that is being translated
	 */
	@SuppressWarnings("deprecation")
	public CircusGenerator(String programName, ProgramEnv programEnv)
	{
		/* You should do this ONLY ONCE in the whole application life-cycle: */

		/* Create and adjust the configuration singleton */
		cfg = new freemarker.template.Configuration();

		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

		try
		{
			cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_DIRECTORY));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		this.programEnv = programEnv;

		this.programName = programName;

	}

	/**
	 * This method triggers the translation of the program, each type of object
	 * is translated to Circus and output to file.
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void translate() throws IOException, FileNotFoundException
	{
		System.out.println("+++ Translating +++");
		System.out.println();
			
		translateNetwork();
		
		translateNonParadigmObjects();

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
	 * Used by all the translate methods. This processes a given Map with a
	 * given template and outputs it with a given filename
	 * 
	 * @param root
	 *            Map of data
	 * @param template
	 *            The name of the template
	 * @param fileName
	 *            The file name to output the translated file to
	 */	@SuppressWarnings("rawtypes")
	private void translateCommon(Map root, String template, String fileName)
	{
		/* Get the template (uses cache internally) */
		freemarker.template.Template temp = null;
		try
		{
			temp = cfg.getTemplate(template);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		/* Variables for writing output to a file */
		new File(OUTPUT_DIRECTORY + programName).mkdirs();

		File file = new File(OUTPUT_DIRECTORY + programName + FILE_DELIMITER + fileName);

		FileOutputStream fop = null;
		try
		{
			fop = new FileOutputStream(file);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		/* Merge data-model with template */
		Writer out = new OutputStreamWriter(fop);
		try
		{
			temp.process(root, out);

			// out.close();
		}
		catch (TemplateException e1)
		{
			e1.printStackTrace();
		}
		catch (IOException e2)
		{
			e2.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	private void translateClass(ClassEnv classEnv)
	{
		Map classMap;
		if (classEnv != null)
		{
			if (!classEnv.isEmpty())
			{
				classMap = classEnv.toMap();

				translateCommon(classMap, CLASS_TEMPLATE_FTL, procName + CLASS_CIRCUS);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void translateCustomChannels(Map chanMap, ObjectEnv objEnv)
	{
		if (!objEnv.getMeths().isEmpty() && !objEnv.getSyncMeths().isEmpty())
		{
			objEnv.getMeths().remove("handleAsyncEvent");
			//TODO Calculate ID TYPE
			chanMap.put(ID_TYPE, "SchedulableID");
			translateCommon(chanMap, CUSTOM_CHANNELS_TEMPLATE_FTL, procName
					+ "MethChan.circus");
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void translateIDFiles()
	{
		System.out.println("+++ Generating ID Files +++");
		// Ian Duncan Smith?
		Map iDs = programEnv.getMissionIdsMap();

		translateCommon(iDs, MISSION_IDS_TEMPLATE_FTL, MISSION_IDS_CIRCUS);

		iDs = programEnv.getSchedulableIdsMap();

		translateCommon(iDs, SCHEDULABLE_IDS_TEMPLATE_FTL, SCHEDULABLE_IDS_CIRCUS);

		iDs = new HashMap();
		iDs.put("Threads", programEnv.getThreadIdsMap());

		translateCommon(iDs, THREAD_IDS_TEMPLATE_FTL, THREAD_IDS_CIRCUS);

		iDs = programEnv.getObjectIdsMap();

		translateCommon(iDs, OBJECT_IDS_TEMPLATE_FTL, OBJECT_IDS_CIRCUS);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void generateReport()
	{
		System.out.println("+++ Generating Report +++");
		Map root = programEnv.geNetworkMap();
		root.put("ProgramName", programName);
		root.put("Version", TightRope.getVersion());

		translateCommon(root, REPORT_TEMPLATE_FTL, programName + REPORT_TEX);
	}

	@SuppressWarnings("rawtypes")
	private void translateNetwork()
	{
		System.out.println("+++ Translating Network +++ ");
		/* Create a data-model */
		Map root = programEnv.geNetworkMap();
		
		System.out.println("+++ Translating Network Channels +++");
		translateCommon(root, NETWORK_CHAN_TEMPLATE_FTL, NETWORK_CHAN);
		
		System.out.println("+++ Translating Method Call Binder +++");	
		translateCommon(root, NETWORK_METHOD_CALL_BINDER_TEMPLATE_FTL, NETWORK_METHOD_CALL_BINDER);
		
		Debugger.log (root.get("MethodCallBindings").toString());
		
		translateCommon(root, "MethodCallBindingChannels.ftl", "MethodCallBindingChannels.circus");
		
		System.out.println("+++ Translating Locking +++");	
		translateCommon(root, NETWORK_LOCKING_TEMPLATE_FTL, NETWORK_LOCKING);
		
		System.out.println("+++ Translating Program +++");	
		translateCommon(root, NETWORK_PROGRAM_TEMPLATE_FTL, NETWORK_PROGRAM);
		

		translateCommon(root, NETWORK_TEMPLATE_FTL, NETWORK_CIRCUS);
	}

	@SuppressWarnings("rawtypes")
  private void translateNonParadigmObjects()
  {
	  System.out.println("+++ Translating Non-Paradigm Objects +++ ");
    /* Create a data-model */
   for (NonParadigmEnv npe : programEnv.getNonParadigmObjectEnvs())
   {     
     Map npeMap = npe.toMap();
     
     procName = (String) npeMap.get(PROCESS_Name);
     
     translateCommon(npeMap, NON_PARADIGM_APP_TEMPLATE_FTL, procName + APP_CIRCUS);
     
     translateCustomChannels(npeMap, npe);
     
     ClassEnv classEnv = npe.getClassEnv();
     translateClass(classEnv);
   }
  

      

    // Custom Channels
    // if (!safelet.getMeths().isEmpty() ||
    // !safelet.getSyncMeths().isEmpty())
    // {
    // root.put("IDType", "");
    // translateCommon(root, CUSTOM_CHANNELS_TEMPLATE_FTL, procName
    // + "MethChan.circus");
    // }
    

   
    
  }

  @SuppressWarnings({ "rawtypes" })
	private void translateSafelet()
	{
		System.out.println("+++ Translating Safelet +++ ");
		/* Create a data-model */
		SafeletEnv safelet = programEnv.getSafelet();

		Map sMap = safelet.toMap();

		procName = (String) sMap.get(PROCESS_Name);

		translateCommon(sMap, SAFELET_APP_TEMPLATE_FTL, procName + APP_CIRCUS);

		// Custom Channels
		// if (!safelet.getMeths().isEmpty() ||
		// !safelet.getSyncMeths().isEmpty())
		// {
		// root.put("IDType", "");
		// translateCommon(root, CUSTOM_CHANNELS_TEMPLATE_FTL, procName
		// + "MethChan.circus");
		// }
		translateCustomChannels(sMap, safelet);

		ClassEnv classEnv = safelet.getClassEnv();
		translateClass(classEnv);
	}

	@SuppressWarnings({ "rawtypes" })
	private void translateTopLevelMissionSequencers()
	{
		System.out.println("+++ Translating Top Level Mission Sequencers +++");

		Map tlmsMap;

		for (TopLevelMissionSequencerEnv tlmsEnv : programEnv
				.getTopLevelMissionSequencers())
		{
			/* Create a data-model */
			tlmsMap = tlmsEnv.toMap();

			procName = (String) tlmsMap.get(PROCESS_Name);

			translateCommon(tlmsMap, MISSION_SEQUENCER_APP_TEMPLATE_FTL, procName
					+ APP_CIRCUS);

			// Custom Channels
			// if (!tlmsEnv.getMeths().isEmpty()
			// || !tlmsEnv.getSyncMeths().isEmpty())
			// {
			// tlms.put("IDType", "SchedulableID");
			// translateCommon(tlms, CUSTOM_CHANNELS_TEMPLATE_FTL, procName
			// + "MethChan.circus");
			// }
			translateCustomChannels(tlmsMap, tlmsEnv);

			ClassEnv classEnv = tlmsEnv.getClassEnv();
			translateClass(classEnv);
		}
	}

	@SuppressWarnings({ "rawtypes" })
	private void translateMissions()
	{
		System.out.println("+++ Translating Missions +++");
		Map missionMap;

		for (MissionEnv mEnv : programEnv.getMissions())
		{
			Debugger.log(mEnv.getName());

			/* Create a data-model */
			missionMap = mEnv.toMap();

			procName = (String) missionMap.get(PROCESS_Name);

			// Application Process
			translateCommon(missionMap, MISSION_APP_TEMPLATE_FTL, procName + APP_CIRCUS);

			// Custom Channels
			// if (!mEnv.getMeths().isEmpty() || !mEnv.getSyncMeths().isEmpty())
			// {
			// missionMap.put("IDType", "MissionID");
			// translateCommon(missionMap, CUSTOM_CHANNELS_TEMPLATE_FTL,
			// procName + "MethChan.circus");
			// }
			translateCustomChannels(missionMap, mEnv);

			// Class
			ClassEnv classEnv = mEnv.getClassEnv();
			translateClass(classEnv);

		}
	}

	@SuppressWarnings({ "rawtypes" })
	private void translateNestedMissionSequencers()
	{
		System.out.println("+++ Translating Nested Mission Sequencers +++");

		Map smsMap;

		for (NestedMissionSequencerEnv smsEnv : programEnv.getNestedMissionSequencers())
		{
			/* Create a data-model */
			smsMap = smsEnv.toMap();

			procName = (String) smsMap.get(PROCESS_Name);

			translateCommon(smsMap, MISSION_SEQUENCER_APP_TEMPLATE_FTL, procName
					+ APP_CIRCUS);

			// Custom Channels
			// if (!smsEnv.getMeths().isEmpty()
			// || !smsEnv.getSyncMeths().isEmpty())
			// {
			// sms.put("IDType", "SchedulableID");
			// translateCommon(sms, CUSTOM_CHANNELS_TEMPLATE_FTL, procName
			// + "MethChan.circus");
			// }
			translateCustomChannels(smsMap, smsEnv);

			ClassEnv classEnv = smsEnv.getClassEnv();
			translateClass(classEnv);
		}
	}

	@SuppressWarnings({ "rawtypes" })
	private void translateManagedThreads()
	{
		System.out.println("+++ Translating Managed Threads +++ ");

		Map mtMap;

		ArrayList<ManagedThreadEnv> mts = programEnv.getManagedThreads();
		for (ManagedThreadEnv mtEnv : mts)
		{
			/* Create a data-model */
			mtMap = mtEnv.toMap();
			Debugger.log("!// managed threads parents = "
					+ mtEnv.getParents().toString());
			procName = mtEnv.getName().toString();
			translateCommon(mtMap, "ManagedThreadApp-Template.ftl", procName + APP_CIRCUS);

			// Custom Channels
			// if (!mtEnv.getMeths().isEmpty() ||
			// !mtEnv.getSyncMeths().isEmpty())
			// {
			// mtEnv.getMeths().remove("run");
			// mtMap.put("IDType", "SchedulableID");
			// translateCommon(mtMap, CUSTOM_CHANNELS_TEMPLATE_FTL, procName
			// + "MethChan.circus");
			// }
			translateCustomChannels(mtMap, mtEnv);

			ClassEnv classEnv = mtEnv.getClassEnv();
			// if (classEnv != null)
			// {
			// if (!classEnv.getMeths().isEmpty()
			// || !classEnv.getSyncMeths().isEmpty())
			// {
			// mtMap = classEnv.toMap();
			// translateCommon(mtMap, CLASS_TEMPLATE_FTL, procName
			// + CLASS_CIRCUS);
			// }
			// }
			translateClass(classEnv);

		}
	}

	@SuppressWarnings({ "rawtypes" })
	private void translateOneShotEventHandlers()
	{
		System.out.println("+++ Translating One Shot Event Handlers +++");

		Map osehMap;

		for (OneShotEventHandlerEnv osehEnv : programEnv.getOneShotEventHandlers())
		{
			/* Create a data-model */
			osehMap = osehEnv.toMap();

			procName = (String) osehMap.get(PROCESS_Name);
			translateCommon(osehMap, HANDLER_APP_TEMPLATE_FTL, procName + APP_CIRCUS);

			// Custom Channels
			// if (!osehEnv.getMeths().isEmpty()
			// || !osehEnv.getSyncMeths().isEmpty())
			// {
			// osehEnv.getMeths().remove("handleAsyncEvent");
			// osehMap.put("IDType", "SchedulableID");
			// translateCommon(osehMap, CUSTOM_CHANNELS_TEMPLATE_FTL, procName
			// + "MethChan.circus");
			// }
			translateCustomChannels(osehMap, osehEnv);

			ClassEnv classEnv = osehEnv.getClassEnv();
			translateClass(classEnv);
		}
	}

	@SuppressWarnings({ "rawtypes" })
	private void translatePeriodicEventHandlers()
	{
		System.out.println("+++ Translating Periodic Event Handlers +++");

		Map pehMap;

		for (PeriodicEventHandlerEnv pehEnv : programEnv.getPeriodicEventHandlers())
		{
			/* Create a data-model */
			pehMap = pehEnv.toMap();

			procName = (String) pehMap.get(PROCESS_Name);
			translateCommon(pehMap, HANDLER_APP_TEMPLATE_FTL, procName + APP_CIRCUS);

			// Custom Channels
			// if (!pehEnv.getMeths().isEmpty()
			// || !pehEnv.getSyncMeths().isEmpty())
			// {
			// pehEnv.getMeths().remove("handleAsyncEvent");
			// pehMap.put("IDType", "SchedulableID");
			// translateCommon(pehMap, CUSTOM_CHANNELS_TEMPLATE_FTL, procName
			// + "MethChan.circus");
			// }
			translateCustomChannels(pehMap, pehEnv);

			ClassEnv classEnv = pehEnv.getClassEnv();
			translateClass(classEnv);
		}
	}

	@SuppressWarnings({ "rawtypes" })
	private void translateAperiodicEventHandlers()
	{
		System.out.println("+++ Translating Aperiodic Event Handlers +++");

		Map apehMap;

		for (AperiodicEventHandlerEnv apehEnv : programEnv.getAperiodicEventHandlers())
		{
			/* Create a data-model */
			apehMap = apehEnv.toMap();

			procName = (String) apehMap.get(PROCESS_Name);
			translateCommon(apehMap, HANDLER_APP_TEMPLATE_FTL, procName + APP_CIRCUS);

			// Custom Channels
			translateCustomChannels(apehMap, apehEnv);

			ClassEnv classEnv = apehEnv.getClassEnv();
			translateClass(classEnv);
		}
	}

}