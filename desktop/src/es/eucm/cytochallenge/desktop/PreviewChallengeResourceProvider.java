package es.eucm.cytochallenge.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.*;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import es.eucm.cytochallenge.model.Challenge;
import es.eucm.cytochallenge.model.PreviewConfig;
import es.eucm.cytochallenge.utils.ChallengeResourceProvider;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PreviewChallengeResourceProvider implements ChallengeResourceProvider {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static String authorization = "";
    private PreviewConfig previewConfig;
    private Json json;

    public PreviewChallengeResourceProvider() {
        this.json = new Json();

        String username = System.getenv(USERNAME);
        String password = System.getenv(PASSWORD);
        String auth = username + ":" + password;
        authorization = "Basic " + Base64.encode(auth.getBytes());
    }

    @Override
    public String getCurrentChallengeId() {
        return previewConfig.getChallengeId();
    }

    @Override
    public void getTexture(final String imagePath, final ResourceProvidedCallback<Texture> callback) {

        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        final Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url(previewConfig.getImagesHost()
                + getCurrentChallengeId() + "/" + imagePath.replace(" ", "%"))
                .header("Authorization", authorization).build();

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {

            private int download(byte[] out, InputStream inputStream) {

                try {
                    int readBytes = 0;
                    while (true) {
                        int length = inputStream.read(out, readBytes, out.length - readBytes);
                        if (length == -1) break;
                        readBytes += length;
                    }
                    return readBytes;
                } catch (Exception ex) {
                    System.out.println("ex = " + ex);
                    return 0;
                } finally {
                    StreamUtils.closeQuietly(inputStream);
                }
            }

            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {

                byte[] bytes = new byte[4000 * 1024]; // assuming the content is not bigger than 200kb.
                int numBytes = download(bytes, httpResponse.getResultAsStream());
                if (numBytes != 0) {
                    // load the pixmap
                    final Pixmap pixmap = new Pixmap(bytes, 0, numBytes);

                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            callback.loaded(new Texture(pixmap));
                        }
                    });
                }

            }

            @Override
            public void failed(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void cancelled() {

            }
        });

    }

    @Override
    public void getChallenge(String jsonPath, final ResourceProvidedCallback<Challenge> callback) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        final Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url(previewConfig.getChallengeHost() + getCurrentChallengeId() + "/")
                .header("Authorization", authorization).build();

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
