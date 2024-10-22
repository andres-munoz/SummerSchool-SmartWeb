package es.uca.cursoia.conversational.assistants.redmine;

import com.taskadapter.redmineapi.ProjectManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.IssueCategory;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.internal.Transport;
import dev.langchain4j.agent.tool.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RedmineAssistantTool {


    private static final int OBJECTS_PER_PAGE = 10;
    public static String REDMINE_URL;
    public static String REDMINE_PROJECT;
    public static String REDMINE_API_KEY;

    Logger logger = LoggerFactory.getLogger(this.getClass());


    @Value("${redmine.api-key}")
    public void setRedmineApiKey(String key) {
        REDMINE_API_KEY = key;
    }

    @Value("${redmine.url}")
    public void setRedmineUri(String url) {
        REDMINE_URL = url;
    }

    @Value("${redmine.project-name}")
    public void setRedmineProject(String project) {
        REDMINE_PROJECT = project;
    }

    /**
     * Create a new issue in the Redmine project
     *
     * @param subject     Issue subject
     * @param description Issue description
     */
    @Tool
    public List<Issue> getIssues() {
        RedmineManager mgr = RedmineManagerFactory.createWithApiKey(REDMINE_URL, REDMINE_API_KEY);
        mgr.setObjectsPerPage(OBJECTS_PER_PAGE);

        List<Issue> issues = new ArrayList<>();
        try {
            issues = mgr.getIssueManager().getIssues(REDMINE_PROJECT, null);
            logger.info("Redmine Issues retrieved: " + issues.size());

        } catch (RedmineException e) {
            logger.error("Error getting issues from Redmine", e);
        }
        return issues;
    }


    /**
     * Get the list of Redmine projects
     *
     * @return List of Redmine projects
     */
    @Tool
    List<Project> getProjects() {
        RedmineManager mgr = RedmineManagerFactory.createWithApiKey(REDMINE_URL, REDMINE_API_KEY);
        mgr.setObjectsPerPage(OBJECTS_PER_PAGE);

        List<Project> projects = new ArrayList<>();
        try {
            projects = mgr.getProjectManager().getProjects();
            logger.info("Redmine Projects retrieved: " + projects.size());

        } catch (RedmineException e) {
            logger.error("Error getting projects from Redmine", e);
        }
        return projects.subList(0, 5);
    }

    /**
     * Create a new issue in the Redmine project
     *
     * @param subject     Issue subject
     * @param description Issue description
     */
    @Tool
    public Issue createIssue(String subject, String description) {
        RedmineManager mgr = RedmineManagerFactory.createWithApiKey(REDMINE_URL, REDMINE_API_KEY);
        mgr.setObjectsPerPage(OBJECTS_PER_PAGE);

        Issue issue = null;
        try {

            Transport transport = mgr.getTransport();
            IssueCategory cat = new IssueCategory(transport).setId(673);
            ProjectManager projectManager = mgr.getProjectManager();
            Project project = projectManager.getProjectByKey(REDMINE_PROJECT);

            issue = new Issue(mgr.getTransport(), project.getId())
                    .setSubject(subject)
                    .setDescription(description)
                    .setProjectId(project.getId())
                    .create();


        } catch (RedmineException e) {
            logger.error("Error creating issue in Redmine", e);
        }
        return issue;
    }
}
