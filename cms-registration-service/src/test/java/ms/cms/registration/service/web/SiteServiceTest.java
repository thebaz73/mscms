package ms.cms.registration.service.web;

import ms.cms.domain.CmsSite;
import ms.cms.domain.WorkflowType;
import ms.cms.registration.service.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@WebAppConfiguration
@IntegrationTest
public class SiteServiceTest extends AbstractServiceTest {

    @Before
    public void setUp() throws Exception {
        prepareEnvironment(true);
        prepareHttpClient(true);
    }

    @Test
    public void testCreateSite() throws Exception {
        RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory(client));
        // Prepare acceptable media type
        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);

        CmsSite cmsSite = new CmsSite();
        cmsSite.setName("Half Blood Blog");
        cmsSite.setAddress("www.half-blood.com");
        // Prepare header
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(acceptableMediaTypes);
        // Pass the new person and header
        HttpEntity<CmsSite> requestEntity = new HttpEntity<>(cmsSite, headers);

        ResponseEntity<Void> entity = template.exchange("http://localhost:9000/api/site/{userId}", HttpMethod.POST, requestEntity, Void.class, userId);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    public void testFindSite() throws Exception {
        registrationManager.createSite(userId, "Half Blood Blog", "www.half-blood.com", WorkflowType.SELF_APPROVAL_WF);
        RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory(client));
        // Prepare acceptable media type
        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
        // Prepare header
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(acceptableMediaTypes);
        // Pass the new person and header
        HttpEntity<CmsSite> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<CmsSite> entity = template.exchange("http://localhost:9000/api/site/{param}", HttpMethod.GET, requestEntity, CmsSite.class, userId);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    public void testEditSite() throws Exception {
        registrationManager.createSite(userId, "Half Blood Blog", "www.half-blood.com", WorkflowType.SELF_APPROVAL_WF);
        RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory(client));
        // Prepare acceptable media type
        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);

        CmsSite cmsSite = new CmsSite();
        cmsSite.setName("HB Blog");
        // Prepare header
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(acceptableMediaTypes);
        // Pass the new person and header
        HttpEntity<CmsSite> requestEntity = new HttpEntity<>(cmsSite, headers);

        ResponseEntity<Void> entity = template.exchange("http://localhost:9000/api/site/{id}", HttpMethod.PUT, requestEntity, Void.class, registrationManager.findSite("www.half-blood.com").getId());

        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    public void testDeleteSite() throws Exception {
        registrationManager.createSite(userId, "Half Blood Blog", "www.half-blood.com", WorkflowType.SELF_APPROVAL_WF);
        RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory(client));
        // Prepare acceptable media type
        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
        // Prepare header
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(acceptableMediaTypes);
        // Pass the new person and header
        HttpEntity<CmsSite> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Void> entity = template.exchange("http://localhost:9000/api/site/{id}", HttpMethod.DELETE, requestEntity, Void.class, registrationManager.findSite("www.half-blood.com").getId());

        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    public void testAddSiteAuthor() throws Exception {

    }

    @Test
    public void testRemoveSiteAuthor() throws Exception {

    }
}