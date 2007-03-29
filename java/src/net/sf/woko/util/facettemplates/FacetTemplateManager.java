package net.sf.woko.util.facettemplates;

import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.core.io.Resource;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.IOException;

public class FacetTemplateManager {

    public List<FacetTemplate> getFacetTemplates(ServletContext ctx) {
        try {
            WebApplicationContext webAppCtx = WebApplicationContextUtils.getWebApplicationContext(ctx);
            Resource[] resources = webAppCtx.getResources("classpath*:net/sf/woko/util/facettemplates/*.properties");
            ArrayList<FacetTemplate> res = new ArrayList<FacetTemplate>();
            for(int i=0 ; i<resources.length ; i++) {
                String fileName = resources[i].getFilename();
                String name = fileName.substring(0, fileName.indexOf('.'));
                InputStream content = resources[i].getInputStream();
                FacetTemplate ft = new FacetTemplate(name, content);
                res.add(ft);
            }
            return res;
        } catch(IOException ioex) {
            ioex.printStackTrace();
            return null;
        }
    }

}
