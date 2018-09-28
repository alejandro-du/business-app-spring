package com.example.issues.issues;

import com.example.issues.projects.Project;
import com.vaadin.flow.server.VaadinSession;

public class Session {

    public static final String PROJECT = "project";

    public static final String USER_ID = "user_id";

    public static Project getProject() {
        return (Project) VaadinSession.getCurrent().getAttribute(PROJECT);
    }

    public static void setProject(Project project) {
        VaadinSession.getCurrent().setAttribute(PROJECT, project);
    }

    public static Long getUserId() {
        return (Long) VaadinSession.getCurrent().getAttribute(USER_ID);
    }

}
