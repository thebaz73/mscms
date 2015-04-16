package ms.cms.authoring.ui.web;

import ms.cms.authoring.common.business.AuthoringException;
import ms.cms.authoring.common.business.AuthoringManager;
import ms.cms.authoring.common.business.ContentManager;
import ms.cms.authoring.ui.domain.ContentData;
import ms.cms.domain.CmsContent;
import ms.cms.domain.CmsSite;
import ms.cms.domain.CmsUser;
import ms.cms.registration.common.business.RegistrationException;
import ms.cms.registration.common.business.RegistrationManager;
import ms.cms.registration.common.business.SiteManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ms.cms.utils.UserUtils.isAuthor;
import static ms.cms.utils.UserUtils.isWebmaster;

/**
 * UserController
 * Created by thebaz on 12/04/15.
 */
@Controller(value = "contentController")
public class ContentController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RegistrationManager registrationManager;
    @Autowired
    private AuthoringManager authoringManager;
    @Autowired
    private SiteManager siteManager;
    @Autowired
    private ContentManager contentManager;

    @ModelAttribute("allSites")
    public List<CmsSite> allSites(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            CmsUser cmsUser = registrationManager.findUser(request.getRemoteUser());
            if (isWebmaster(cmsUser)) {
                return siteManager.findAllSites(cmsUser);
            } else if (isAuthor(cmsUser)) {
                return siteManager.findAuthoredSites(cmsUser);
            }
        } catch (RegistrationException e) {
            String msg = String.format("Cannot manage sites. Reason: %s", e.getMessage());
            logger.info(msg, e);
            response.sendError(400, msg);
        }
        return null;
    }

    @ModelAttribute("allContents")
    public Page<CmsContent> allContents(HttpServletRequest request, HttpServletResponse response,
                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) throws IOException {
        try {
            CmsUser cmsUser = registrationManager.findUser(request.getRemoteUser());
            Pageable pageable = new PageRequest(page, pageSize, Sort.Direction.ASC, "name");
            if (isWebmaster(cmsUser)) {
                return contentManager.findAllContents(cmsUser, pageable);
            } else if (isAuthor(cmsUser)) {
                return contentManager.findAuthoredContents(cmsUser, pageable);
            }
        } catch (RegistrationException e) {
            String msg = String.format("Cannot manage contents. Reason: %s", e.getMessage());
            logger.info(msg, e);
            response.sendError(400, msg);
        }
        return null;
    }

    @RequestMapping({"/contents"})
    public String show(ModelMap model) {
        model.put("contentData", new ContentData());
        model.put("mode", "add");
        return "contents";
    }

    @RequestMapping(value = {"/contents"}, method = RequestMethod.POST)
    public String addContent(HttpServletResponse response, @ModelAttribute("contentData") ContentData contentData,
                             final BindingResult bindingResult, final ModelMap model) throws IOException {
        if (bindingResult.hasErrors()) {
            return "contents";
        }
        try {
            authoringManager.createContent(contentData.getSiteId(), "", contentData.getTitle(), "", contentData.getSummary(), contentData.getContent());
            model.clear();
        } catch (AuthoringException e) {
            String msg = String.format("Cannot author contents. Reason: %s", e.getMessage());
            logger.info(msg, e);
            response.sendError(400, msg);
        }
        return "redirect:/contents";
    }

    @RequestMapping(value = {"/contents/{contentId}"}, method = RequestMethod.GET)
    public String editMode(HttpServletResponse response, ModelMap model, @PathVariable("contentId") String contentId) throws IOException {
        try {
            CmsContent cmsContent = authoringManager.findContent(contentId);
            ContentData contentData = new ContentData();
            contentData.setSiteId(cmsContent.getSiteId());
            contentData.setTitle(cmsContent.getTitle());
            contentData.setSummary(cmsContent.getSummary());
            contentData.setContent(cmsContent.getContent());
            model.put("contentData", contentData);
            model.put("mode", "edit");
        } catch (AuthoringException e) {
            String msg = String.format("Cannot author contents. Reason: %s", e.getMessage());
            logger.info(msg, e);
            response.sendError(400, msg);
        }
        return "contents";
    }

    @RequestMapping(value = {"/contents/{contentId}"}, method = RequestMethod.DELETE)
    public String delete(HttpServletResponse response, @PathVariable("contentId") String contentId) throws IOException {
        try {
            authoringManager.deleteContent(contentId);
        } catch (AuthoringException e) {
            String msg = String.format("Cannot author contents. Reason: %s", e.getMessage());
            logger.info(msg, e);
            response.sendError(400, msg);
        }
        return "redirect:/contents";
    }
}