package ms.cms.authoring.service.web;

import ms.cms.authoring.common.business.AuthoringException;
import ms.cms.authoring.common.business.AuthoringManager;
import ms.cms.domain.CmsPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * PostService
 * Created by thebaz on 27/03/15.
 */
@RestController
public class PostService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthoringManager authoringManager;

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public void createPost(HttpServletResponse response,
                           @RequestParam(value = "siteId") String siteId,
                           @RequestParam(value = "name", defaultValue = "") String name,
                           @RequestParam(value = "title") String title,
                           @RequestParam(value = "uri", defaultValue = "") String uri,
                           @RequestParam(value = "summary", defaultValue = "") String summary,
                           @RequestParam(value = "content") String content) throws IOException {
        try {
            authoringManager.createPost(siteId, name, title, uri, summary, content);
        } catch (AuthoringException e) {
            String msg = String.format("Cannot create post. Reason: %s", e.toString());
            logger.info(msg);
            response.sendError(400, msg);
        }
    }

    @RequestMapping(value = "/post/{id}", method = RequestMethod.GET)
    public CmsPost findPost(HttpServletResponse response, @PathVariable("id") String id) throws IOException {
        try {
            return authoringManager.findPost(id);
        } catch (AuthoringException e) {
            String msg = String.format("Cannot find post. Reason: %s", e.toString());
            logger.info(msg);
            response.sendError(400, msg);
        }

        return null;
    }

    @RequestMapping(value = "/post/byUri", method = RequestMethod.GET)
    public CmsPost findPostByUri(HttpServletResponse response,
                                 @RequestParam(value = "siteId") String siteId,
                                 @RequestParam(value = "uri") String uri) throws IOException {
        try {
            return authoringManager.findPostByUri(siteId, uri);
        } catch (AuthoringException e) {
            String msg = String.format("Cannot find post. Reason: %s", e.toString());
            logger.info(msg);
            response.sendError(400, msg);
        }

        return null;
    }

    @RequestMapping(value = "/post/{id}", method = RequestMethod.PUT)
    public void editPost(HttpServletResponse response,
                         @PathVariable(value = "id") String id,
                         @RequestParam(value = "name", defaultValue = "") String name,
                         @RequestParam(value = "title") String title,
                         @RequestParam(value = "uri", defaultValue = "") String uri,
                         @RequestParam(value = "summary", defaultValue = "") String summary,
                         @RequestParam(value = "content") String content) throws IOException {
        try {
            authoringManager.editPost(id, name, title, uri, summary, content);
        } catch (AuthoringException e) {
            String msg = String.format("Cannot edit post. Reason: %s", e.toString());
            logger.info(msg);
            response.sendError(400, msg);
        }
    }

    @RequestMapping(value = "/post/{id}", method = RequestMethod.DELETE)
    public void deletePost(HttpServletResponse response,
                           @PathVariable(value = "id") String id) throws IOException {
        try {
            authoringManager.deletePost(id);
        } catch (AuthoringException e) {
            String msg = String.format("Cannot delete post. Reason: %s", e.toString());
            logger.info(msg);
            response.sendError(400, msg);
        }
    }
}