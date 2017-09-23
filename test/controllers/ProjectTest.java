package controllers;

import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;
import controllers.*;
import controllers.routes;
import models.Project;
import org.junit.Before;
import org.junit.Test;
import play.libs.Yaml;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.List;

import static com.thoughtworks.selenium.SeleneseTestBase.assertEquals;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static play.test.Helpers.*;

/**
 * Created by sandeshpoudel on 23/09/2017.
 */
public class ProjectTest extends WithApplication {
    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase(),fakeGlobal()));
        Ebean.save((List) Yaml.load("test-data.yml"));
    }

    @Test
    public void newProject(){
        Result result = callAction(
                controllers.routes.ref.Projects.add(),
                fakeRequest().withSession("email","bob@example.com")
                .withFormUrlEncodedBody(ImmutableMap.of("group","Some Group"))
         );

        assertEquals(200,status(result));
        Project project = Project.find.where()
                .eq("folder","Some Group").findUnique();
        assertNotNull(project);
        assertEquals("New project",project.name);
        assertEquals(1,project.members.size());
        assertEquals("bob@example.com",project.members.get(0).email);

    }

    @Test
    public void renameProject(){
        Long id = Project.find.where()
                .eq("members.email","bob@example.com")
                .eq("name","Private").findUnique().id;
        Result result = callAction(
                controllers.routes.ref.Projects.rename(id),
                        fakeRequest().withSession("email","bob@example.com")
                        .withFormUrlEncodedBody(ImmutableMap.of("name","New name"))
                        );
        assertEquals(200,status(result));
        assertEquals("New name",Project.find.byId(id).name);


    }

    @Test
    public void renameProjectForbidden(){
        Long id = Project.find.where()
                .eq("members.email","bob@example.com")
                .eq("name","Private").findUnique().id;
        Result result = callAction(
                controllers.routes.ref.Projects.rename(id),
                fakeRequest().withSession("email","jeff@example.com")
                    .withFormUrlEncodedBody(ImmutableMap.of("name","New name"))
        );
        assertEquals(403,status(result));
        assertEquals("Private",Project.find.byId(id).name);
    }
}
