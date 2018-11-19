package org.csuc.merge;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.util.FormatType;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author amartinez
 */
public class Merge {

    private static Logger logger = LogManager.getLogger(Merge.class);

    private double w = 0.002;
    private int max_files = 1;
    private Lang format = Lang.TURTLE;

    private Path temporal = Files.createTempDirectory(UUID.randomUUID().toString());

    public Merge() throws Exception {
    }

    public Merge(FormatType formatType) throws Exception {
        this.format = formatType.lang();
    }

    public Merge(double weight) throws Exception {
        this.w = weight;
    }

    public Merge(double weight, FormatType formatType) throws Exception {
        this.w = weight;
        this.format = formatType.lang();
    }

    public Merge(int max_files) throws Exception {
        this.max_files = max_files;
    }

    public Merge(int max_files, FormatType formatType) throws Exception {
        this.max_files = max_files;
        this.format = formatType.lang();
    }

    public Merge(double weight, int max_files) throws Exception {
        this.w = weight;
        this.max_files = max_files;
    }

    public Merge(double weight, int max_files, FormatType formatType) throws Exception {
        this.w = weight;
        this.max_files = max_files;
        this.format = formatType.lang();
    }

    public void execute(Path origin) throws IOException {
        AtomicInteger iter = new AtomicInteger(0);

        Stream<Path> stream = Files.walk(origin).filter(Files::isRegularFile);
        List<Path> object = stream.collect(Collectors.toList());

        int objectSize = object.size();

        if(objectSize > max_files){
            double weight = Math.pow(w / objectSize, ((double) 1 / 5));

            int partition =
                    rounded((rounded(objectSize * weight) == 1)
                            ? (max_files == 1 ? objectSize : max_files)
                            : rounded(objectSize * weight));

            Collection<List<Path>> data = partition(object, partition);

            showinfo(objectSize, weight, partition, data.size());

            Model union = ModelFactory.createDefaultModel();

            data.forEach(col->{
                col.forEach(p->{
                    iter.incrementAndGet();
                    union.add(RDFDataMgr.loadModel(p.toString()));

                    logger.debug("[UNION] {} de {} - {}", iter.get(), objectSize, p);

                    if (origin.equals(temporal)) {
                        logger.debug("[DELETE] - {}", p);
                        p.toFile().delete();
                    }
                });
                try {
                    write(union, temporal);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            if(data.size() > max_files)    execute(temporal);
        }
    }

    /**
     * @param union
     * @param path
     * @throws IOException
     */
    private void write(Model union, Path path) throws IOException {
        String f = MessageFormat.format("{0}/{1}.{2}", path, UUID.randomUUID().toString(), format.getFileExtensions().get(0));
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f, true), 1024)) {
            RDFDataMgr.write(out, union, format);
        }
        logger.debug("[CREATE] {}", f);
        //reset model
        if (Objects.nonNull(union)) union.removeAll();
    }


    /**
     * @param list
     * @param N
     * @param <T>
     * @return
     */
    private <T> Collection<List<T>> equalsPartition(List<T> list, int N) {
        final AtomicInteger counter = new AtomicInteger(0);

        return list.stream()
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() % N))
                .values();
    }

    /**
     * @param list
     * @param N
     * @param <T>
     * @return
     */
    private <T> Collection<List<T>> partition(List<T> list, int N) {
        final AtomicInteger counter = new AtomicInteger(0);

        return list.stream()
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / N))
                .values();
    }

    /**
     * @param value
     * @return
     */
    private static int rounded(double value) {
        return (int) Math.ceil(value);
    }

    public void deleteTemporal() throws IOException {
        Files.walk(temporal)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .peek(p-> logger.info("[DELETE] - {}", p))
                .forEach(File::delete);
    }

    public Path getTemporal() {
        return temporal;
    }

    private void showinfo(int size, double weight, int partition, int result){
        logger.info("/*************************************************** INFO ITERATION ***************************************************/");
        logger.info("data size:      {}", size);
        logger.info("weight:         {}", weight);
        logger.info("partitions:     {}", partition);
        logger.info("result:         {}", result);
        logger.info("/**********************************************************************************************************************/");
    }
}
