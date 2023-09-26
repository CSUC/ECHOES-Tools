package org.edm.transformations.formats.custom;

import eu.europeana.corelib.definitions.jibx.RDF;
import nl.mindbus.a2a.A2AType;
import org.apache.commons.lang3.RegExUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.custom.multirecords.Metadata;
import org.csuc.deserialize.JaxbUnmarshal;
import org.csuc.deserialize.JibxUnMarshall;
import org.csuc.serialize.JibxMarshall;
import org.csuc.util.FormatType;
import org.edm.transformations.formats.EDM;
import org.edm.transformations.formats.a2a.A2A2EDM;
import org.edm.transformations.formats.dc.DC2EDM;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author amartinez
 */
public class CUSTOM2EDM extends RDF implements EDM {

    private static Logger logger = LogManager.getLogger(org.edm.transformations.formats.custom.CUSTOM2EDM.class);

    private String path;
    private Map<String, String> properties;
    private Metadata metadata;

    public CUSTOM2EDM(String path, Metadata metadata, Map<String, String> properties) {
        this.path = path;
        this.properties = properties;
        this.metadata = metadata;
    }

    @Override
    public void transformation(OutputStream out, Map<String, String> xsltProperties) throws Exception {
        throw new IllegalArgumentException("transformation is not valid for CUSTOM2EDM!");
    }

    @Override
    public void transformation(String xslt, OutputStream out, Map<String, String> xsltProperties) throws Exception {
        throw new IllegalArgumentException("transformation is not valid for CUSTOM2EDM!");
    }

    @Override
    public void transformation(String xslt) throws Exception {
        throw new IllegalArgumentException("transformation is not valid for CUSTOM2EDM!");
    }

    @Override
    public void creation() {
        metadata.getRecords().getRecord().forEach(record -> {
            try {
                JaxbUnmarshal jaxbUnmarshal = new JaxbUnmarshal(record.getAny(), new Class[]{OaiDcType.class, A2AType.class});

                if (jaxbUnmarshal.getObject() instanceof OaiDcType) {
                    OaiDcType oaiDcType = (OaiDcType) jaxbUnmarshal.getObject();
                    DC2EDM dc2EDM = new DC2EDM(UUID.randomUUID().toString(), oaiDcType, properties);

                    dc2EDM.creation();
                } else if (jaxbUnmarshal.getObject() instanceof A2AType) {
                    A2AType a2AType = (A2AType) jaxbUnmarshal.getObject();
                    A2A2EDM a2A2EDM = new A2A2EDM(UUID.randomUUID().toString(), a2AType, properties);

                    a2A2EDM.creation();
                } else {
                    throw new Exception("invalid schema");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void creation(FormatType formatType) throws IOException {
        metadata.getRecords().getRecord().forEach(record -> {
            try {
                JaxbUnmarshal jaxbUnmarshal = new JaxbUnmarshal(record.getAny(), new Class[]{OaiDcType.class, A2AType.class,});

                if (jaxbUnmarshal.getObject() instanceof OaiDcType) {
                    OaiDcType oaiDcType = (OaiDcType) jaxbUnmarshal.getObject();
                    DC2EDM dc2EDM = new DC2EDM(UUID.randomUUID().toString(), oaiDcType, properties);

                    dc2EDM.creation(formatType);
                } else if (jaxbUnmarshal.getObject() instanceof A2AType) {
                    A2AType a2AType = (A2AType) jaxbUnmarshal.getObject();
                    A2A2EDM a2A2EDM = new A2A2EDM(UUID.randomUUID().toString(), a2AType, properties);

                    a2A2EDM.creation(formatType);
                } else {
                    throw new Exception("invalid schema");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void creation(Charset encoding, boolean alone, OutputStream outs) {
        metadata.getRecords().getRecord().forEach(record -> {
            Path filename = Paths.get(
                    String.format("%s/%s.rdf",
                            path,
                            RegExUtils.replaceAll(
                                    UUID.randomUUID().toString(),
                                    "[^a-zA-Z0-9.-]",
                                    "_"
                            )
                    )
            );
            try {
                JaxbUnmarshal jaxbUnmarshal = new JaxbUnmarshal(record.getAny(), new Class[]{OaiDcType.class, A2AType.class});

                if (jaxbUnmarshal.getObject() instanceof OaiDcType) {
                    OaiDcType oaiDcType = (OaiDcType) jaxbUnmarshal.getObject();
                    DC2EDM dc2EDM = new DC2EDM(UUID.randomUUID().toString(), oaiDcType, properties);

                    dc2EDM.creation(encoding, alone, new FileOutputStream(filename.toFile()));
                } else if (jaxbUnmarshal.getObject() instanceof A2AType) {
                    A2AType a2AType = (A2AType) jaxbUnmarshal.getObject();
                    A2A2EDM a2A2EDM = new A2A2EDM(UUID.randomUUID().toString(), a2AType, properties);

                    a2A2EDM.creation(encoding, alone, new FileOutputStream(filename.toFile()));
                } else {
                    throw new Exception("invalid schema");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void creation(Charset encoding, boolean alone, OutputStream outs, FormatType formatType) throws Exception {
        metadata.getRecords().getRecord().forEach(record -> {
            Path filename = Paths.get(
                    String.format("%s/%s.rdf",
                            path,
                            RegExUtils.replaceAll(
                                    UUID.randomUUID().toString(),
                                    "[^a-zA-Z0-9.-]",
                                    "_"
                            )
                    )
            );
            try {
                JaxbUnmarshal jaxbUnmarshal = new JaxbUnmarshal(record.getAny(), new Class[]{OaiDcType.class, A2AType.class});

                if (jaxbUnmarshal.getObject() instanceof OaiDcType) {
                    OaiDcType oaiDcType = (OaiDcType) jaxbUnmarshal.getObject();
                    DC2EDM dc2EDM = new DC2EDM(UUID.randomUUID().toString(), oaiDcType, properties);

                    dc2EDM.creation(encoding, alone, new FileOutputStream(filename.toFile()), formatType);
                } else if (jaxbUnmarshal.getObject() instanceof A2AType) {
                    A2AType a2AType = (A2AType) jaxbUnmarshal.getObject();
                    A2A2EDM a2A2EDM = new A2A2EDM(UUID.randomUUID().toString(), a2AType, properties);

                    a2A2EDM.creation(encoding, alone, new FileOutputStream(filename.toFile()), formatType);
                } else {
                    throw new Exception("invalid schema");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void creation(Charset encoding, boolean alone, Writer writer) {
        if (!Objects.equals(this, new RDF()))
            JibxMarshall.marshall(this, encoding.toString(), alone, writer, RDF.class, -1);
    }

    @Override
    public JibxUnMarshall validateSchema(InputStream ins, Charset enc, Class<?> classType) {
        return new JibxUnMarshall(ins, enc, classType);
    }

    @Override
    public JibxUnMarshall validateSchema(InputStream ins, String name, Charset enc, Class<?> classType) {
        return new JibxUnMarshall(ins, name, enc, classType);
    }

    @Override
    public JibxUnMarshall validateSchema(Reader rdr, Class<?> classType) {
        return new JibxUnMarshall(rdr, classType);
    }

    @Override
    public JibxUnMarshall validateSchema(Reader rdr, String name, Class<?> classType) {
        return new JibxUnMarshall(rdr, name, classType);
    }

    @Override
    public void modify(RDF rdf) {

    }
}

