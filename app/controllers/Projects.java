package controllers;

import models.Project;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.projects.group;
import views.html.projects.item;

import java.util.ArrayList;

import static play.data.Form.form;

/**
 * Created by sandeshpoudel on 23/09/2017.
 */
@Security.Authenticated(Secured.class)
public class Projects extends Controller {
    public static Result add() {
        Project newProject = Project.create(
                "New project",
                form().bindFromRequest().get("group"),
                request().username()
        );
        return ok(item.render(newProject));
    }

    public static Result rename(Long project) {
        if(Secured.isMemberOf(project)) {
            return ok(
                    Project.rename(
                            project,
                            form().bindFromRequest().get("name")
                    )
            );
        } else {
            return forbidden();
        }
    }

    public static Result delete(Long project){
        if(Secured.isMemberOf(project)) {
            Project.find.ref(project).delete();
            return  ok();
        }else {
            return forbidden();
        }
    }

    public static Result addGroup() {
        return ok(
                group.render("New group",new ArrayList<Project>())
        );
    }
}
