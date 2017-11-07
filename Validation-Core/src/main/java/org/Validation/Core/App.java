package org.Validation.Core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.Validation.Core.deserialize.Validate;
import org.Validation.Core.schematron.SchematronUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.europeana.corelib.definitions.jibx.RDF;

/**
 * @author amartinez
 *
 */
public class App {

	private static Logger logger = LogManager.getLogger("Validation-Core");

	private static String file;
	private static String charset = StandardCharsets.UTF_8.toString();
	private static String sch;

	private static AtomicInteger providedCHOSize = new AtomicInteger(0);
	private static AtomicInteger aggregationSize = new AtomicInteger(0);
	private static AtomicInteger webResourceSize = new AtomicInteger(0);
	private static AtomicInteger agentSize = new AtomicInteger(0);
	private static AtomicInteger placeSize = new AtomicInteger(0);
	private static AtomicInteger timeSpanSize = new AtomicInteger(0);
	private static AtomicInteger ConceptSize = new AtomicInteger(0);

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (Objects.nonNull(args) && args.length != 0)
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("--file"))
					file = args[i + 1];
				if (args[i].equals("--charset"))
					charset = args[i + 1];
				if (args[i].equals("--sch"))
					sch = args[i + 1];
			}
		if (Objects.isNull(file))
			throw new Exception("file must not be null");

		try {
			Files.walk(Paths.get(file)).filter(Files::isRegularFile).filter(f -> f.toString().endsWith(".xml"))
					.forEach(f -> {
						try {
							Validate validate = new Validate(new FileInputStream(f.toFile()), charset, RDF.class);

							logger.info(String.format("%s isValid %s XML", f.getFileName(), validate.isValid()));

							if (validate.isValid()) {
								RDF rdf = (RDF) validate.getElement();
								rdf.getChoiceList().forEach(c -> {
									if (c.ifProvidedCHO())
										providedCHOSize.getAndIncrement();
									if (c.ifAggregation())
										aggregationSize.getAndIncrement();
									if (c.ifWebResource())
										webResourceSize.getAndIncrement();
									if (c.ifAgent())
										agentSize.getAndIncrement();
									if (c.ifPlace())
										placeSize.getAndIncrement();
									if (c.ifTimeSpan())
										timeSpanSize.getAndIncrement();
									if (c.ifConcept())
										ConceptSize.getAndIncrement();
								});

								logger.info(String.format(
										"ProvidedCHO: %s WebResource: %s Agent: %s Place: %s TimeSpan: %s Concept: %s Aggregation: %s",
										providedCHOSize.get(), webResourceSize.get(), agentSize.get(), placeSize.get(),
										timeSpanSize.get(), ConceptSize.get(), aggregationSize.get()));

							} else {
								logger.info(validate.getError());
							}
						} catch (FileNotFoundException e) {
							logger.error(e);
						}
						if (Objects.nonNull(sch)) {
							SchematronUtil schUtil = new SchematronUtil(new File(sch), f.toFile());
							try {
								if (!schUtil.isValid()) {
									logger.info(String.format("%s isValid %s SCH", f.getFileName(), schUtil.isValid()));
									logger.info(schUtil.getSVRLFailedAssert());
								}
							} catch (Exception e) {
								logger.error(e);
							}
						}
					});
		} catch (IOException e) {
			logger.error(e);
		}
	}
}
