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
 * Generates the Circus files of the environments that have been translated form
 * a program.
 * 
 * @author Matt Luckcuck
 * 
 */
public class CircusGenerator
{
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
	private static final String SAFELET_APP_TEMPLATE_FTL = "SafeletApp-Template.ftl";
	private static final String MISSION_APP_TEMPLATE_FTL = "MissionApp-Template.ftl";
	private static final String MISSION_SEQUENCER_APP_TEMPLATE_FTL = "MissionSequencerApp-Template.ftl";
	private static final String HANDLER_APP_TEMPLATE_FTL = "HandlerApp-Template.ftl";
	private static final String PROCESS_ID = "ProcessID";
	private static final String APP_CIRCUS = "App.circus";
	private static final String TEMPLATE_DIRECTORY = "/home/matt/Documents/Translation/test/templates";
	private static final String OUTPUT_DIRECTORY = "/home/matt/Documents/Translation/test/output/";
	private static final String CUSTOM_CHANNELS_TEMPLATE_FTL = "CustomChannels-Templat.ftl";

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
		} catch (IOException e)
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
		new File(OUTPUT_DIRECTORY + programName).mkdirs();

		File file = new File(OUTPUT_DIRECTORY + programName + FILE_DELIMITER + fileName);

		FileOutputStream fop = null;
		try
		{
			fop = new FileOutputStream(file);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		/* Merge data-model with template */
		Writer out = new OutputStreamWriter(fop);
		try
		{
			temp.process(root, out);

			// out.close();
		} catch (TemplateException e1)
		{
			e1.printStackTrace();
		} catch (IOException e2)
		{
			e2.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	private void translateIDFiles()
	{
		System.out.println("+++ Generating ID Files +++");
		Map Ids = programEnv.getMissionIdsMap();

		translateCommon(Ids, MISSION_IDS_TEMPLATE_FTL, MISSION_IDS_CIRCUS);

		Ids = programEnv.getSchedulableIdsMap();

		translateCommon(Ids, SCHEDULABLE_IDS_TEMPLATE_FTL,
				SCHEDULABLE_IDS_CIRCUS);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void generateReport()
	{
		System.out.println("+++ Generating Report +++");
		Map root = programEnv.geNetworkMap();
		root.put("programName", programName);

		translateCommon(root, REPORT_TEMPLATE_FTL, programName + REPORT_TEX);
	}

	@SuppressWarnings("rawtypes")
	private void translateNetwork()
	{
		System.out.println("+++ Translating Network +++ ");
		/* Create a data-model */
		Map root = programEnv.geNetworkMap();

		translateCommon(root, NETWORK_TEMPLATE_FTL, NETWORK_CIRCUS);
	}

	@SuppressWarnings("rawtypes")
	private void translateSafelet()
	{
		System.out.println("+++ Translating Safelet +++ ");
		/* Create a data-model */
		Map root = programEnv.getSafelet().toMap();

		procName = (String) root.get(PROCESS_ID);

		translateCommon(root, SAFELET_APP_TEMPLATE_FTL, procName + APP_CIRCUS);
	}

	@SuppressWarnings("rawtypes")
	private void translateTopLevelMissionSequencers()
	{
		System.out.println("+++ Translating Top Level Mission Sequencers +++");

		Map tlms;
		
		for (TopLevelMissionSequencerEnv tlmsEnv : programEnv
				.getTopLevelMissionSequencers())
		{
			/* Create a data-model */
			tlms = tlmsEnv.toMap();

			procName = (String) tlms.get(PROCESS_ID);

			translateCommon(tlms, MISSION_SEQUENCER_APP_TEMPLATE_FTL, procName
					+ APP_CIRCUS);

			tlms = tlmsEnv.getClassEnv().toMap();
			if(!tlms.isEmpty())
			{
			translateCommon(tlms, CLASS_TEMPLATE_FTL, procName
					+ CLASS_CIRCUS);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void translateMissions()
	{
		System.out.println("+++ Translating Missions +++");
		Map missionMap;
		
		for (MissionEnv mEnv : programEnv.getMissions())
		{
			/* Create a data-model */
			missionMap = mEnv.toMap();

			procName = (String) missionMap.get(PROCESS_ID);

			translateCommon(missionMap, MISSION_APP_TEMPLATE_FTL, procName
					+ APP_CIRCUS);
			
			System.out.println("/// mission map " + missionMap.toString());
			translateCommon(missionMap, CUSTOM_CHANNELS_TEMPLATE_FTL, procName + "MethChan.circus");
			
			missionMap = mEnv.getClassEnv().toMap();
			if(!missionMap.isEmpty())
			{
				translateCommon(missionMap, CLASS_TEMPLATE_FTL, procName
					+ CLASS_CIRCUS);
			}
			
		}
	}

	@SuppressWarnings("rawtypes")
	private void translateNestedMissionSequencers()
	{
		System.out.println("+++ Translating Nested Mission Sequencers +++");

		Map sms;
		
		for (NestedMissionSequencerEnv smsEnv : programEnv
				.getNestedMissionSequencers())
		{
			/* Create a data-model */
			sms = smsEnv.toMap();

			procName = (String) sms.get(PROCESS_ID);

			translateCommon(sms, MISSION_SEQUENCER_APP_TEMPLATE_FTL, procName
					+ APP_CIRCUS);
			
			sms = smsEnv.getClassEnv().toMap();
			if(!sms.isEmpty())
			{
				translateCommon(sms, CLASS_TEMPLATE_FTL, procName
					+ CLASS_CIRCUS);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void translateManagedThreads()
	{
		System.out.println("+++ Translating Managed Threads +++ ");
		
		Map mtMap;

		ArrayList<ManagedThreadEnv> mts = programEnv.getManagedThreads();
		for (ManagedThreadEnv mtEnv : mts)
		{
			/* Create a data-model */
			mtMap = mtEnv.toMap();

			System.out.println("/// params = " + mtMap.get("Parameters").toString());
			
			procName = mtEnv.getName().toString();
			translateCommon(mtMap, "ManagedThreadApp-Template.ftl", procName
					+ APP_CIRCUS);

			mtMap = mtEnv.getClassEnv().toMap();
			if(!mtMap.isEmpty())
			{
				translateCommon(mtMap, CLASS_TEMPLATE_FTL, procName
					+ CLASS_CIRCUS);
			}
			

		}
	}

	@SuppressWarnings("rawtypes")
	private void translateOneShotEventHandlers()
	{
		System.out.println("+++ Translating One Shot Event Handlers +++");

		Map osehMap;
		
		for (OneShotEventHandlerEnv osehEnv : programEnv
				.getOneShotEventHandlers())
		{
			/* Create a data-model */
			osehMap = osehEnv.toMap();

			procName = (String) osehMap.get(PROCESS_ID);
			translateCommon(osehMap, HANDLER_APP_TEMPLATE_FTL, procName
					+ APP_CIRCUS);
			
			osehMap = osehEnv.getClassEnv().toMap();
			if(!osehMap.isEmpty())
			{
				translateCommon(osehMap, CLASS_TEMPLATE_FTL, procName
					+ CLASS_CIRCUS);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void translatePeriodicEventHandlers()
	{
		System.out.println("+++ Translating Periodic Event Handlers +++");

		Map pehhMap;
		
		for (PeriodicEventHandlerEnv pehEnv : programEnv
				.getPeriodicEventHandlers())
		{
			/* Create a data-model */
			pehhMap = pehEnv.toMap();

			procName = (String) pehhMap.get(PROCESS_ID);
			translateCommon(pehhMap, HANDLER_APP_TEMPLATE_FTL, procName
					+ APP_CIRCUS);
			
			pehhMap = pehEnv.getClassEnv().toMap();
			if(!pehhMap.isEmpty())
			{
				translateCommon(pehhMap, CLASS_TEMPLATE_FTL, procName
					+ CLASS_CIRCUS);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void translateAperiodicEventHandlers()
	{
		System.out.println("+++ Translating One Shot Event Handlers +++");

		Map apehMap;
		
		for (AperiodicEventHandlerEnv apehEnv : programEnv
				.getAperiodicEventHandlers())
		{
			/* Create a data-model */
			apehMap = apehEnv.toMap();

			procName = (String) apehMap.get(PROCESS_ID);
			translateCommon(apehMap, HANDLER_APP_TEMPLATE_FTL, procName
					+ APP_CIRCUS);
			
			apehMap = apehEnv.getClassEnv().toMap();
			if(!apehMap.isEmpty())
			{
				translateCommon(apehMap, CLASS_TEMPLATE_FTL, procName
					+ CLASS_CIRCUS);
			}
		}
	}

}