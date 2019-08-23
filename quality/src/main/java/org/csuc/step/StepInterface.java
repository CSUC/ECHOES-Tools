package org.csuc.step;

import java.net.URL;
import java.nio.file.Path;

public interface StepInterface<T> {

    T quality(Path path) throws Exception;

    T quality(URL url) throws Exception;
}
