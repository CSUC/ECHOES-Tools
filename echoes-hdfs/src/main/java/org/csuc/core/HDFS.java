package org.csuc.core;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.csuc.typesafe.HDFSConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;
import java.text.MessageFormat;

/**
 * @author amartinez
 */
public class HDFS implements Serializable {

    private static Logger logger = LogManager.getLogger(HDFS.class);

    private FileSystem fileSystem;

    public HDFS(HDFSConfig hdfsConfig) throws IOException {
        Configuration conf = new Configuration();

        conf.set("fs.defaultFS", hdfsConfig.getUri());
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());

        System.setProperty("HADOOP_USER_NAME", hdfsConfig.getUser());
        System.setProperty("hadoop.home.dir", hdfsConfig.getHome());

        fileSystem = FileSystem.get(URI.create(hdfsConfig.getUri()), conf);
    }

    public HDFS(String hdfsuri, String hdfsuser, String hdfshome) throws IOException {
        Configuration conf = new Configuration();

        conf.set("fs.defaultFS", hdfsuri);
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());

        System.setProperty("HADOOP_USER_NAME", hdfsuser);
        System.setProperty("hadoop.home.dir", hdfshome);

        fileSystem = FileSystem.get(URI.create(hdfsuri), conf);
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public static void mkdir(FileSystem fileSystem, Path path) throws IOException {
        if(!fileSystem.exists(new Path(System.getProperty("hadoop.home.dir"), path))) {
            // Create new Directory
            fileSystem.mkdirs(new Path(System.getProperty("hadoop.home.dir"), path));

            logger.info(MessageFormat.format("Path {0} created.", path));
        }
    }

    public static void write(FileSystem fileSystem, Path dest, InputStream inputStream, boolean overwrite) throws IOException {
        //Init output stream
        FSDataOutputStream outputStream = fileSystem.create(new Path(System.getProperty("hadoop.home.dir"), dest), overwrite);

        //Cassical output stream usage
        //Copy file from local to HDFS
        IOUtils.copyBytes(inputStream, outputStream, 4096, true);

        IOUtils.closeStream(inputStream);
        IOUtils.closeStream(outputStream);

        logger.info(MessageFormat.format("End Write file into hdfs: {0}/{1}", System.getProperty("hadoop.home.dir"), dest));
    }

    public static void get(FileSystem fileSystem, Path source, OutputStream outputStream) throws IOException {
        logger.info(MessageFormat.format("Read file into hdfs: {0}/{1}", System.getProperty("hadoop.home.dir"), source));
        //Init input stream
        FSDataInputStream inputStream = fileSystem.open(new Path(System.getProperty("hadoop.home.dir"), source));

        //Classical input stream usage
        IOUtils.copyBytes(inputStream, outputStream, 512, false);

        IOUtils.closeStream(inputStream);
        IOUtils.closeStream(outputStream);
    }

    public static void delete(FileSystem fileSystem, Path source) throws IOException {
        if(fileSystem.exists(new Path(System.getProperty("hadoop.home.dir"), source)))
            fileSystem.delete(new Path(System.getProperty("hadoop.home.dir"), source), true);
    }

    public static void close(FileSystem fileSystem) throws IOException {
        fileSystem.close();
    }
}
