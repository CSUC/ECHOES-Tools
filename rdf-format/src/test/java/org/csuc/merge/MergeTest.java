package org.csuc.merge;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class MergeTest {

    Path path;

    @Before
    public void setUp() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        path = new File(classLoader.getResource("ttl").getFile()).toPath();
    }

    @Test
    public void execute() throws Exception {
//        Merge merge1 = new Merge();
//        merge1.execute(path);
//        merge1.deleteTemporal();

        Merge merge2 = new Merge(3);
        merge2.execute(path);
        merge2.deleteTemporal();


    }
}