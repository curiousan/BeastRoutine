package controllers;

import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;
import controllers.*;
import controllers.routes;
import org.junit.Before;
import org.junit.Test;
import play.libs.Yaml;
import play.test.WithApplication;

import javax.xml.transform.Result;
import java.util.List;

import static com.thoughtworks.selenium.SeleneseTestBase.assertEquals;
import static play.test.Helpers.*;

/**
 * Created by sandeshpoudel on 23/09/2017.
 */
public class LoginTest  extends WithApplication{
    @Before
    public void setUp(){
        start(fakeApplication(inMemoryDatabase(),fakeGlobal()));
        Ebean.save((List) Yaml.load("test-data.yml"));
    }

    @Test
    public void authenticateSuccess(){
        play.mvc.Result result = callAction(
                controllers.routes.ref.Application.authenticate(),
                fakeRequest().withFormUrlEncodedBody(ImmutableMap.of(
                        "email","bob@example.com",
                        "password","secret"
                ))
        );
        assertEquals(303,status(result));
        assertEquals("bob@example.com",session(result).get("email"));
    }

    @Test
    public void authenticated(){
        play.mvc.Result result = callAction(
                controllers.routes.ref.Application.index(),
                fakeRequest().withSession("email","bob@example.com")

        );
        assertEquals(200,status(result));
    }

    @Test
    public void notAuthenticated(){
        play.mvc.Result result = callAction(
                controllers.routes.ref.Application.index(),
                fakeRequest()
        );
        assertEquals(303, status(result));
        assertEquals("/login",header("Location",result));
    }
}
