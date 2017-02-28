package com.ccnode.codegenerator.freemarker;

import com.ccnode.codegenerator.log.Log;
import com.ccnode.codegenerator.log.LogFactory;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * @Author bruce.ge
 * @Date 2017/1/12
 * @Description
 */
public class TemplateUtil {

    private static Log log = LogFactory.getLogger(TemplateUtil.class);
    private static Configuration configuration;


    static {
        configuration = new Configuration(Configuration.getVersion());
        try {
            configuration.setDirectoryForTemplateLoading(new File(TemplateUtil.class.getClassLoader().getResource("templates").getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
    }

    public static String processToString(String templateName, Map<String, Object> root) {
        try {
            Template template = configuration.getTemplate(templateName);
            StringWriter out = new StringWriter();
            template.process(root, out);
            return out.toString();
        } catch (Exception e) {
            log.error("template process catch exception",e);
            throw new RuntimeException("process freemarker template catch exception", e);
        }
    }
}
