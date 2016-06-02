package es.eucm.cytochallenge.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.*;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import es.eucm.cytochallenge.model.Challenge;
import es.eucm.cytochallenge.model.PreviewConfig;
import es.eucm.cytochallenge.utils.ChallengeResourceProvider;


public class GWTPreviewChallengeResourceProvider implements ChallengeResourceProvider {

    private final HtmlLauncher launcher;
    private PreviewConfig previewConfig;
    private Json json;

    public GWTPreviewChallengeResourceProvider(HtmlLauncher launcher) {
        this.json = new Json();
        this.launcher = launcher;
    }


    @Override
    public void getTexture(final String imagePath, final ResourceProvidedCallback<Texture> callback) {
        final RootPanel root = RootPanel.get("embed-html");
        final String url = previewConfig.getImagesHost() + imagePath;
        final Image img = new Image(url);
        img.getElement().setAttribute("crossOrigin", "anonymous");
        img.addLoadHandler(new LoadHandler() {

            @Override
            public void onLoad(LoadEvent event) {
                launcher.getPreloader().images.put(url, ImageElement.as(img.getElement()));
                callback.loaded(new Texture(new Pixmap(Gdx.files.internal(url))));
                root.remove(img);
            }
        });
        root.add(img);
    }

    @Override
    public void getChallenge(String jsonPath, final ResourceProvidedCallback<Challenge> callback) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        final Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url(previewConfig.getChallengeHost()).build();

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {

            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String challengesStr = httpResponse.getResultAsString();
                JsonReader reader = new JsonReader();

                JsonValue value = reader.parse(challengesStr);

                JsonValue challenge = value.get("challengeFile");

                if (challenge != null) {
                    callback.loaded(json.fromJson(Challenge.class, challenge.toJson(JsonWriter.OutputType.json)));
                } else {
                    callback.failed();
                }
            }

            @Override
            public void failed(Throwable t) {
            }

            @Override
            public void cancelled() {

            }
        });
    }

    public void setPreviewConfig(PreviewConfig previewConfig) {
        this.previewConfig = previewConfig;
    }
}
