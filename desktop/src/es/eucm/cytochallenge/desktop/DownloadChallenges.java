package es.eucm.cytochallenge.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.backends.lwjgl.LwjglNet;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DownloadChallenges {

    public static final String API_PATH = "http://cytopathology.e-ucm.es/";
    public static final String GET_CHALLENGES_PATH = API_PATH + "challenges";
    public static final String GET_CHALLENGES_UPLOAD_PATH = API_PATH + "uploads/";
    public static final String CHALLENGE_ZIP_NAME = "challenge.zip";


    public static Array<String> IDS = new Array<String>();
    public static FileHandle TMP_FOLDER, CHALLENGES_FOLDER;
    public static int PROCESSED = 0;

    public static void main(String[] arg) {
        Gdx.net = new LwjglNet();
        Gdx.files = new LwjglFiles();

        TMP_FOLDER = Gdx.files.absolute("./tmp");
        CHALLENGES_FOLDER = Gdx.files.absolute("./android/assets/challenges");

        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        final Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url(GET_CHALLENGES_PATH).build();

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String challengesStr = httpResponse.getResultAsString();
                System.out.println(challengesStr);

                JsonReader reader = new JsonReader();

                JsonValue value = reader.parse(challengesStr);

                JsonValue challengeInfo;

                int i = 0;
                do {
                    challengeInfo = value.get(i);

                    if (challengeInfo != null) {

                        String id = challengeInfo.getString("_id");

                        if (id != null) {
                            System.out.println("challenge = " + id);
                            IDS.add(id);
                            downloadChallenge(id);
                        }
                    }
                    ++i;
                } while (challengeInfo != null);
            }

            @Override
            public void failed(Throwable t) {
                System.out.println("t = " + t);
            }

            @Override
            public void cancelled() {
                System.out.println("cancelled");
            }
        });
        do {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while ((IDS.size == 0) || IDS.size != PROCESSED);

        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        FileHandle jsonChallenges = CHALLENGES_FOLDER.child("challenges.json");
        Array<String> currentChallenges = json.fromJson(Array.class, jsonChallenges);

        for (int i = 0; i < IDS.size; i++) {
            String id = IDS.get(i);
            if(id != null && !currentChallenges.contains(id, false)) {
                currentChallenges.add(id);
            }
        }
        String[] totalChallenges = new String[currentChallenges.size];
        for (int i = 0; i < currentChallenges.size; i++) {
            totalChallenges[i] = currentChallenges.get(i);
        }

        json.toJson(currentChallenges, String[].class, jsonChallenges);
        System.out.println(json.prettyPrint(currentChallenges));

        // Cleaning
        TMP_FOLDER.deleteDirectory();
    }

    public static void downloadChallenge(final String id) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        final Net.HttpRequest httpRequest = requestBuilder.newRequest()
                .method(Net.HttpMethods.GET).url(GET_CHALLENGES_UPLOAD_PATH + id + "/" + CHALLENGE_ZIP_NAME).build();

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                InputStream input = httpResponse.getResultAsStream();
                FileHandle outputZip = TMP_FOLDER.child(id + "/" + CHALLENGE_ZIP_NAME);
                outputZip.parent().mkdirs();
                OutputStream output = outputZip.write(false);

                byte data[] = new byte[2048];
                try {
                    int count;
                    do {
                        count = input.read(data);

                        if (count != -1) {
                            output.write(data, 0, count);
                        }

                    } while (count != -1);
                    output.flush();
                    StreamUtils.closeQuietly(output);
                    StreamUtils.closeQuietly(input);
                    unzipChallenge(outputZip, id);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failed(Throwable t) {
                System.out.println("t = " + t);
            }

            @Override
            public void cancelled() {
                System.out.println("cancelled");
            }
        });
    }

    public static void unzipChallenge(FileHandle sourceZip, String id) {

        FileHandle outputFolder = sourceZip.parent().child("unzipped");
        outputFolder.mkdirs();
        ZipUtils.unzip(sourceZip, outputFolder);
        copyChallengeToAssets(outputFolder, id);
    }

    public static void copyChallengeToAssets(FileHandle challengeDir, String id) {
        challengeDir.copyTo(CHALLENGES_FOLDER.child(id));
        PROCESSED++;
    }
}
