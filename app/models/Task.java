package models;

import play.api.mvc.AnyContentAsJson;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.List;

/**
 * Created by sandeshpoudel on 22/09/2017.
 */
@Entity
public class Task extends Model {
    @Id
    public Long id;
    public String title;
    public boolean done = false;
    public String folder;
    public Date dueDate;
    @ManyToOne
    public User assignedTo;
    @ManyToOne
    public Project project;

    public static Model.Finder<Long,Task> find = new Finder<Long, Task>(Long.class,Task.class);

    public static List<Task> findToDoInvolving(String user){
        return find.fetch("project").where()
                .eq("done",false)
                .eq("project.members.email",user)
                .findList();
    }

    public static Task create(Task task, Long project, String folder) {
        task.project = Project.find.ref(project);
        task.folder = folder;
        task.save();
        return  task;
    }






















}
