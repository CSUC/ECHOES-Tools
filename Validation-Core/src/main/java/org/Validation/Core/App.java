package org.Validation.Core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

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
	private static Charset charset = StandardCharsets.UTF_8;
	private static String sch;
	private static int threads = 1;

	private static AtomicInteger providedCHO = new AtomicInteger(0);
	private static AtomicInteger aggregation = new AtomicInteger(0);
	private static AtomicInteger webResource = new AtomicInteger(0);
	private static AtomicInteger agent = new AtomicInteger(0);
	private static AtomicInteger place = new AtomicInteger(0);
	private static AtomicInteger timeSpan = new AtomicInteger(0);
	private static AtomicInteger Concept = new AtomicInteger(0);

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
				if (args[i].equals("--charset"))    charset = !Charset.isSupported((String) args[i + 1]) ? StandardCharsets.UTF_8 : Charset.forName((String) args[i + 1]);
				if (args[i].equals("--sch"))
					sch = args[i + 1];
				if (args[i].equals("--threads"))
					threads = Integer.parseInt(args[i + 1]);
			}
		if (Objects.isNull(file))
			throw new Exception("file must not be null");

		new ForkJoinPool(threads).submit(() -> {
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
											providedCHO.getAndIncrement();
										if (c.ifAggregation())
											aggregation.getAndIncrement();
										if (c.ifWebResource())
											webResource.getAndIncrement();
										if (c.ifAgent())
											agent.getAndIncrement();
										if (c.ifPlace())
											place.getAndIncrement();
										if (c.ifTimeSpan())
											timeSpan.getAndIncrement();
										if (c.ifConcept())
											Concept.getAndIncrement();
									});
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
										logger.info(
												String.format("%s isValid %s SCH", f.getFileName(), schUtil.isValid()));
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
		}).join();

		logger.info(String.format(
				"ProvidedCHO: %s WebResource: %s Agent: %s Place: %s TimeSpan: %s Concept: %s Aggregation: %s",
				providedCHO.get(), webResource.get(), agent.get(), place.get(), timeSpan.get(), Concept.get(),
				aggregation.get()));
	}
}