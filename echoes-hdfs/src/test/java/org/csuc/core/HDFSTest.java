package org.csuc.core;

import org.apache.hadoop.fs.Path;
import org.csuc.typesafe.HDFSConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class HDFSTest {

    private HDFS hdfs;
    private HDFSConfig hdfsConfig = HDFSConfig.config(null);

    private Path example = new Path("example");
    private File file;

    @Before
    public void setUp() throws Exception {
        hdfs = new HDFS(hdfsConfig);

        ClassLoader classLoader = getClass().getClassLoader();
        file = new File(classLoader.getResource("echoes-hdfs-defaults.conf").getFile());
    }

    @Test
    public void hdfs() throws IOException {
        hdfs.mkdir(hdfs.getFileSystem(), example);

        hdfs.write(
                hdfs.getFileSystem(),
                new Path(example, file.getName()),
                new FileInputStream(file),
                true
        );

        hdfs.get(
                hdfs.getFileSystem(),
                new Path(example, file.getName()),
                System.out
        );
    }

    @After
    public void tearDown() throws Exception {
        hdfs.delete(
                hdfs.getFileSystem(),
                new Path(hdfsConfig.getHome()));

        hdfs.close(hdfs.getFileSystem());
    }
}