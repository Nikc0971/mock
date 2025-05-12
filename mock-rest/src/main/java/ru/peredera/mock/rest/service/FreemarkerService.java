package ru.peredera.mock.rest.service;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

@Service
public class FreemarkerService {

    private final Configuration configuration;

    public FreemarkerService() {
        this.configuration = new Configuration(Configuration.VERSION_2_3_33);
        this.configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_33));
    }

    public String render(String templateString, Map<String, Object> params) throws TemplateException, IOException {
        var template = new Template("simpleTemplateName", new StringReader(templateString), configuration);
        var out = new StringWriter();

        template.process(params, out);

        return out.toString();
    }
}
