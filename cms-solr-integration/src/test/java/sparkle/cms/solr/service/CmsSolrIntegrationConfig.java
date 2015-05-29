package sparkle.cms.solr.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.data.solr.server.SolrServerFactory;
import org.springframework.data.solr.server.support.EmbeddedSolrServerFactoryBean;

import javax.annotation.Resource;

/**
 * SparkleSolrConfig
 * Created by bazzoni on 29/05/2015.
 */
@Configuration
@EnableSolrRepositories(basePackages = {"sparkle.cms.solr.data"})
@PropertySource("application.properties")
public class CmsSolrIntegrationConfig {
    private static final String PROPERTY_NAME_SOLR_SOLR_HOME = "solr.solr.home";

    @Resource
    private Environment environment;

    @Bean
    public SolrTemplate solrTemplate() {
        return new SolrTemplate(solrServerFactory());
    }

    private SolrServerFactory solrServerFactory() {
        EmbeddedSolrServerFactoryBean factory = new EmbeddedSolrServerFactoryBean();
        factory.setSolrHome(environment.getRequiredProperty(PROPERTY_NAME_SOLR_SOLR_HOME));

        return factory;
    }
}
