package es.eucm.cytochallenge.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import es.eucm.cytochallenge.CytoChallenge;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(1024, 552);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new CytoChallenge();
        }
}