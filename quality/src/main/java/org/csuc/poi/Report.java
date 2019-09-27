package org.csuc.poi;

import com.mongodb.MongoClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.csuc.dao.QualityDetailsDAO;
import org.csuc.dao.entity.Error;
import org.csuc.dao.entity.QualityDetails;
import org.csuc.dao.entity.edm.*;
import org.csuc.dao.impl.QualityDetailsDAOImpl;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.FindOptions;

import java.io.FileOutputStream;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Report {

    private static Logger logger = LogManager.getLogger(Report.class);

    private SXSSFWorkbook workbook = new SXSSFWorkbook(100);

    private QualityDetailsDAO qualityDetailsDAO;

    public Report(String host, int port, String database) {
        Morphia morphia = new Morphia();
        morphia.getMapper().getOptions().setStoreNulls(false);
        morphia.getMapper().getOptions().setStoreEmpties(false);

        MongoClient mongo = new MongoClient(host, port);

        Datastore datastore = morphia.createDatastore(mongo, database);
        qualityDetailsDAO = new QualityDetailsDAOImpl(QualityDetails.class, datastore);

        workbook.setCompressTempFiles(true);
    }

    public Report(Datastore datastore) {
        qualityDetailsDAO = new QualityDetailsDAOImpl(QualityDetails.class, datastore);

        workbook.setCompressTempFiles(true);
    }

    /**
     *
     * @param objectId
     */
    public void create(String objectId) {
        logger.info("[Report] - {}", objectId);

        try {
            if (qualityDetailsDAO.countErrorsById(objectId) > 0) {
                //STEP1
                if (qualityDetailsDAO.countErrorsStep1ById(objectId) > 0) {
                    Sheet sheet = workbook.createSheet("1");

                    AtomicInteger atomicInteger = new AtomicInteger(0);

                    Row headerRow = sheet.createRow(atomicInteger.get());
                    for (int i = atomicInteger.get(); i < STEP1.values().length; i++) {
                        Cell cell = headerRow.createCell(i);
                        cell.setCellValue((STEP1.values()[i]).value());
                    }

                    qualityDetailsDAO.getStep1Errors(objectId).fetch(new FindOptions().batchSize(50).noCursorTimeout(true)).forEach(qualityDetails -> {
                        if (Objects.nonNull(qualityDetails.getSchema())) {

                            AtomicInteger length = new AtomicInteger(0);

                            Row row = sheet.createRow(atomicInteger.incrementAndGet());

                            row.createCell(length.getAndIncrement()).setCellValue(qualityDetails.getInput());

                            row.createCell(length.getAndIncrement()).setCellValue(qualityDetails.getSchema().getMessage());
                        }
                    });
                }
                //STEP2
                if (qualityDetailsDAO.countErrorsStep2ById(objectId) > 0) {
                    Sheet sheet = workbook.createSheet("2");

                    AtomicInteger atomicInteger = new AtomicInteger(0);

                    Row headerRow = sheet.createRow(atomicInteger.get());
                    for (int i = atomicInteger.get(); i < STEP2.values().length; i++) {
                        Cell cell = headerRow.createCell(i);
                        cell.setCellValue((STEP2.values()[i]).value());
                    }

                    qualityDetailsDAO.getStep2Errors(objectId).fetch(new FindOptions().batchSize(50).noCursorTimeout(true)).forEach(qualityDetails -> {
                        if (Objects.nonNull(qualityDetails.getSchematron()) && !qualityDetails.getSchematron().isEmpty()) {
                            qualityDetails.getSchematron().forEach(schematron -> {
                                AtomicInteger length = new AtomicInteger(0);

                                Row row = sheet.createRow(atomicInteger.incrementAndGet());

                                row.createCell(length.getAndIncrement()).setCellValue(qualityDetails.getInput());

                                row.createCell(length.getAndIncrement()).setCellValue(schematron.getTest());
                                row.createCell(length.getAndIncrement()).setCellValue(schematron.getMessage());
                            });
                        }
                    });
                }
                //STEP3
                if (qualityDetailsDAO.countErrorsStep3ById(objectId) > 0) {
                    Sheet sheet = workbook.createSheet("3");

                    AtomicInteger atomicInteger = new AtomicInteger(0);

                    Row headerRow = sheet.createRow(atomicInteger.get());
                    for (int i = atomicInteger.get(); i < STEP3.values().length; i++) {
                        Cell cell = headerRow.createCell(i);
                        cell.setCellValue((STEP3.values()[i]).value());
                    }

                    qualityDetailsDAO.getStep3Errors(objectId).fetch(new FindOptions().batchSize(50).noCursorTimeout(true)).forEach(qualityDetails -> {
                        if (Objects.nonNull(qualityDetails.getEdm())) {
                            Stream.of(
                                    qualityDetails.getEdm().getAgent(),
                                    qualityDetails.getEdm().getAggregation(),
                                    qualityDetails.getEdm().getConcept(),
                                    qualityDetails.getEdm().getPlace(),
                                    qualityDetails.getEdm().getProvidedCHO(),
                                    qualityDetails.getEdm().getTimeSpan(),
                                    qualityDetails.getEdm().getWebResource()
                            )
                                    .parallel()
                                    .filter(Objects::nonNull)
                                    .filter(f -> !f.isEmpty())
                                    .forEach(objects -> objects.forEach(o -> {
                                        if (o instanceof Agent) {
                                            if (Objects.nonNull(((Agent) o).getErrorList())) {
                                                ((Agent) o).getErrorList().forEach(error -> iterationRow(sheet, atomicInteger, qualityDetails.getInput(), error));
                                            }
                                        }
                                        if (o instanceof Aggregation) {
                                            if (Objects.nonNull(((Aggregation) o).getErrorList())) {
                                                ((Aggregation) o).getErrorList().forEach(error -> iterationRow(sheet, atomicInteger, qualityDetails.getInput(), error));
                                            }
                                        }
                                        if (o instanceof Concept) {
                                            if (Objects.nonNull(((Concept) o).getErrorList())) {
                                                ((Concept) o).getErrorList().forEach(error -> iterationRow(sheet, atomicInteger, qualityDetails.getInput(), error));
                                            }
                                        }
                                        if (o instanceof Place) {
                                            if (Objects.nonNull(((Place) o).getErrorList())) {
                                                ((Place) o).getErrorList().forEach(error -> iterationRow(sheet, atomicInteger, qualityDetails.getInput(), error));
                                            }
                                        }
                                        if (o instanceof TimeSpan) {
                                            if (Objects.nonNull(((TimeSpan) o).getErrorList())) {
                                                ((TimeSpan) o).getErrorList().forEach(error -> iterationRow(sheet, atomicInteger, qualityDetails.getInput(), error));
                                            }
                                        }
                                        if (o instanceof WebResource) {
                                            if (Objects.nonNull(((WebResource) o).getErrorList())) {
                                                ((WebResource) o).getErrorList().forEach(error -> iterationRow(sheet, atomicInteger, qualityDetails.getInput(), error));
                                            }
                                        }
                                        if (o instanceof ProvidedCHO) {
                                            if (Objects.nonNull(((ProvidedCHO) o).getErrorList())) {
                                                ((ProvidedCHO) o).getErrorList().forEach(error -> iterationRow(sheet, atomicInteger, qualityDetails.getInput(), error));
                                            }
                                        }
                                    }));
                        }
                    });
                }
            }

            workbook.write(new FileOutputStream(String.format("/tmp/%s.xlsx", objectId)));

            // dispose of temporary files backing this workbook on disk
            workbook.dispose();

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     *
     * @param sheet
     * @param iterSheet
     * @param input
     * @param error
     */
    private void iterationRow(Sheet sheet, AtomicInteger iterSheet, String input, Error error) {
        AtomicInteger length = new AtomicInteger(0);

        Row row = sheet.createRow(iterSheet.incrementAndGet());

        row.createCell(length.getAndIncrement()).setCellValue(input);

        row.createCell(length.getAndIncrement()).setCellValue(error.getType().getType());
        row.createCell(length.getAndIncrement()).setCellValue(error.getMetadata());
        row.createCell(length.getAndIncrement()).setCellValue(error.getValidationType().getType());
        row.createCell(length.getAndIncrement()).setCellValue(error.getLevelQuality().getLevel());
        row.createCell(length.getAndIncrement()).setCellValue(error.getMessage());
    }
}
