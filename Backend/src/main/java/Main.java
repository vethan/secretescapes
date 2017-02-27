import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mongodb.BasicDBObject;
import com.secretescapes.codingChallenge.Server;
import com.secretescapes.codingChallenge.modules.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        List<Module> moduleList = new ArrayList<>();
        moduleList.add(new FongoModule());
        moduleList.add(new ConsumerModule());
        moduleList.add(new GsonModule());
        moduleList.add(new PersistenceModule());
        moduleList.add(new GreenmailModule());
        Injector injector = Guice.createInjector(moduleList);
        Server server = injector.getInstance(Server.class);
        server.start();
    }
}
