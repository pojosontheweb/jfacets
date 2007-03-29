package net.sf.woko.util.facettemplates;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

public class FacetTemplate {

    private String name;
    private String code;
    private String comment;

    public FacetTemplate(String name, InputStream codeIs) throws IOException {
        this.name = name;
        Properties props = new Properties();
        props.load(codeIs);
        this.code = props.getProperty("code");
        this.comment = props.getProperty("comment");
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getComment() {
        return comment;
    }
}
