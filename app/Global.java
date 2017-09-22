import com.avaje.ebean.Ebean;
import models.User;
import play.Application;
import play.GlobalSettings;
import play.libs.Yaml;

import java.util.List;

/**
 * Created by sandeshpoudel on 22/09/2017.
 */
public class Global extends GlobalSettings {
    @Override
    public void onStart(Application app) {
        super.onStart(app);

        //check if the database is empty
        if(User.find.findRowCount() == 0){
            Ebean.save((List) Yaml.load("test-data.yml"));
        }
    }
}
