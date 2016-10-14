package es.eucm.cytochallenge.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.user.client.Window;
import es.eucm.cytochallenge.CytoChallenge;
import es.eucm.cytochallenge.model.PreviewConfig;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(1024, 552);
    }

    @Override
    public ApplicationListener createApplicationListener() {


        return new CytoChallenge(null, new HtmlResolver()) {
            @Override
            public void create() {
                String challenge = Window.Location.getParameter("challenge");
                /*
                Gdx.app.setLogLevel(LOG_DEBUG);
                Gdx.app.log("HOST", "Host " + Window.Location.getHost());
                Gdx.app.log("HOST", "getProtocol " + Window.Location.getProtocol());
                Gdx.app.log("HOST", "getHash " + Window.Location.getHash());
                Gdx.app.log("HOST", "getHostName " + Window.Location.getHostName());
                Gdx.app.log("HOST", "getHref " + Window.Location.getHref());

                Gdx.app.log("HOST", "getPath " + Window.Location.getPath());

                Gdx.app.log("HOST", "getPort " + Window.Location.getPort());

                Gdx.app.log("HOST", "getProtocol " + Window.Location.getProtocol());
                Gdx.app.log("HOST", "getQueryString " + Window.Location.getQueryString());
                Gdx.app.log("HOST", "getParameter " + challenge);
                */

                boolean preview = challenge != null && challenge.length() == 24;
                GWTPreviewChallengeResourceProvider challengeResourceProvider = null;
                if (preview) {

                    challengeResourceProvider = new GWTPreviewChallengeResourceProvider(HtmlLauncher.this);
                    PreviewConfig prevConfig = new PreviewConfig();


                    String protocol = Window.Location.getProtocol();
                    if (!protocol.endsWith("/")) {
                        protocol += "//";
                    }
                    String origin = protocol + Window.Location.getHost();
                    prevConfig.setImagesHost(origin + "/uploads/");
                    prevConfig.setChallengeHost(origin + "/challenges/");
                    prevConfig.setChallengeId(challenge);

                    challengeResourceProvider.setPreviewConfig(prevConfig);
                }

                setResourceProvider(challengeResourceProvider);

                super.create();
            }
        };
    }
}