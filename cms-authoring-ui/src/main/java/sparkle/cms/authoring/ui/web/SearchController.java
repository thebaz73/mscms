package sparkle.cms.authoring.ui.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sparkle.cms.authoring.common.business.AuthoringManager;
import sparkle.cms.plugin.mgmt.search.SparkleDocument;

import java.util.Date;
import java.util.List;

/**
 * SearchController
 * Created by thebaz on 12/04/15.
 */
@Controller(value = "searchController")
public class SearchController {
    @Autowired
    private AuthoringManager authoringManager;

    @RequestMapping(value = {"/search"}, params = {"query"}, method = RequestMethod.GET)
    public String search(ModelMap model, String query) {
        model.put("date", new Date());
        model.put("query", query);

        List<? extends SparkleDocument> docs = authoringManager.searchContent(query);
        model.put("docs", docs);
        return "search";
    }
}
