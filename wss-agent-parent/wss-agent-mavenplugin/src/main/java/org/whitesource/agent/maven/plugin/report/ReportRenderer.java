package org.whitesource.agent.maven.plugin.report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.xml.PrettyPrintXMLWriter;
import org.whitesource.agent.api.dispatch.ReportResult;
import org.whitesource.agent.maven.plugin.Constants;


/**
 * Class generates the White Source report XML file.
 * 
 * @author tom.shapira
 *
 */
public class ReportRenderer {

	/* --- Static members --- */

	private static final String ELEMENT_REPORT = "report";

	private static final String ELEMENT_LICENSE_HISTOGRAM = "LicenseHistogram";

	private static final String ELEMENT_LICENSE = "license";

	private static final String ELEMENT_NAME = "name";

	private static final String ELEMENT_COUNT = "count";
	
	private static final String ELEMENT_ALERTS = "Alerts";

	private static final String ELEMENT_NEWER_VERSION = "newerVersions";

	private static final String REPORT_NAME = "whitesource-report.xml";

	private static final String XML_FILE_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n";
	
	private static final String LOG_SEPERATOR = "                                                   ";

	private static final int LICENSE_NAME_LENGTH = 40;
	
	/* --- Static methods --- */

	public static void renderToXML(File outputDirectory, ReportResult result) throws IOException {
		File report = new File(outputDirectory, REPORT_NAME);
		if (report != null) {
			report.getParentFile().mkdirs();
			
			PrintWriter writer = new PrintWriter(report);
			PrettyPrintXMLWriter prettyWriter = new PrettyPrintXMLWriter(writer);
			prettyWriter.writeMarkup(XML_FILE_HEADER);
			prettyWriter.startElement(ELEMENT_REPORT);

			// license histogram
			prettyWriter.startElement(ELEMENT_LICENSE_HISTOGRAM);
			TreeMap<String, Integer> sortedHistogram = new TreeMap<String, Integer>(result.getLicenseDistribution());
			for (Entry<String, Integer> entry : sortedHistogram.entrySet()) {
				prettyWriter.startElement(ELEMENT_LICENSE);
				
				prettyWriter.startElement(ELEMENT_NAME);
				prettyWriter.writeText(entry.getKey());
				prettyWriter.endElement();
				
				prettyWriter.startElement(ELEMENT_COUNT);
				prettyWriter.writeText(String.valueOf(entry.getValue()));
				prettyWriter.endElement();
				
				prettyWriter.endElement();
			}
			prettyWriter.endElement();
			
			// alerts
			prettyWriter.startElement(ELEMENT_ALERTS);
			int numOfNewerVersions = result.getNumOfNewerVersions();
			if (numOfNewerVersions > 0) {
				prettyWriter.startElement(ELEMENT_NEWER_VERSION);
				prettyWriter.writeText(String.valueOf(numOfNewerVersions));
				prettyWriter.endElement();
			}
			prettyWriter.endElement();
			
			prettyWriter.endElement();
			writer.close();
		}
	}
	
	/**
	 * Logs the operation results. 
	 * 
	 * @param result
	 */
	public static void renderToLog(ReportResult result, Log log) {
		log.info("");

		Map<String, Integer> licenceDistribution = result.getLicenseDistribution();
		if (licenceDistribution.isEmpty()) {
			log.info(Constants.INFO_NO_RESULTS);
		} else {
			log.info(Constants.INFO_LICENSE_ANALYSIS);
			log.info("");
			TreeMap<String, Integer> sortedHistogram = new TreeMap<String, Integer>(result.getLicenseDistribution());
			for (Entry<String, Integer> entry : sortedHistogram.entrySet()) {
				log.info(formatLicenseResult(entry.getKey(), entry.getValue()));
			}

			int numOfNewerVersions = result.getNumOfNewerVersions();
			if (numOfNewerVersions > 0) {
				log.info("");
				log.info(MessageFormat.format(Constants.INFO_NEWER_VERSIONS_FORMAT, numOfNewerVersions));
			}
			log.info("");
			log.info(Constants.INFO_CONTACT);
			log.info("");
		}
	}
	
	/**
	 * Writes the license result to the log.
	 * 
	 * @param license Name of the license.
	 * @param appearances Number of libraries under license;
	 */
	private static String formatLicenseResult(final String license, Integer appearances) {
		String formattedLicense = license;
		
		// shorten name if necessary
		if (formattedLicense.length() > LICENSE_NAME_LENGTH) {
			formattedLicense = formattedLicense.substring(0, LICENSE_NAME_LENGTH) + "..";
		}

		StringBuilder sb = new StringBuilder(LOG_SEPERATOR);
		sb.replace(0, license.length(), license + " ");

		// align number to right
		String alignString = "  ";
		if (appearances > 9) {
			alignString = " ";
			if (appearances > 99) {
				alignString = "";
			}
		}
		sb.append(alignString + appearances);

		return sb.toString();
	}
	
	/* --- Constructors --- */
	
	/**
	 * Private default constructor
	 */
	private ReportRenderer() {
		// avoid instantiation
	}
}
