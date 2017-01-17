package es.eucm.cytochallenge.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.backends.lwjgl.LwjglNet;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.*;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import es.eucm.cytochallenge.model.Difficulty;
import es.eucm.cytochallenge.model.course.Course;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class DownloadChallenges {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String API_PATH = "http://cytopathology.e-ucm.es/";
    public static final String GET_CHALLENGES_PATH = API_PATH + "api/challenges";
    public static final String GET_COURSES_PATH = API_PATH + "api/courses";
    public static final String GET_CHALLENGES_UPLOAD_PATH = API_PATH + "uploads/";
    public static final String CHALLENGE_ZIP_NAME = "challenge.zip";

    public static final Array<Course> courses = new Array<Course>();

    private static String authorization;
    public static Array<String> IDS = new Array<String>();
    public static FileHandle TMP_FOLDER, CHALLENGES_FOLDER;
    public static int PROCESSED = 0;
    public static Map<String, Array<Difficulty>> difficulties = new HashMap<String, Array<Difficulty>>();

    public static void main(String[] arg) {

        String username = System.getenv(USERNAME);
        String password = System.getenv(PASSWORD);
        String auth = username + ":" + password;
        authorization = "Basic " + Base64.encode(auth.getBytes());
        System.out.println("authorization = " + authorization);
        System.out.println("auth = " + auth);

        Gdx.net = new LwjglNet();
        Gdx.files = new LwjglFiles();

        TMP_FOLDER = Gdx.files.absolute("./tmp");
        CHALLENGES_FOLDER = Gdx.files.absolute("./android/assets/challenges");

        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        final Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url(GET_COURSES_PATH)
                .header("Authorization", authorization).build();

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String coursesStr = httpResponse.getResultAsString();
                System.out.println(coursesStr);

                JsonReader reader = new JsonReader();

                JsonValue value = reader.parse(coursesStr);

                JsonValue courseInfo;

                courses.clear();
                int i = 0;
                do {
                    courseInfo = value.get(i);

                    if (courseInfo != null) {

                        String id = courseInfo.getString("_id");

                        if (id != null) {
                            System.out.println("courseInfo = " + id);

                            Course course = new Course();
                            course.setCourseId(id);
                            course.setName(courseInfo.getString("name"));
                            course.setTimePerChallenge(courseInfo.getInt("timePerChallenge", 0));

                            courses.add(course);
                        }
                    }
                    ++i;
                } while (courseInfo != null);
                downloadChallenges();
            }

            @Override
            public void failed(Throwable t) {
                System.out.println("Failed to get courses, t = " + t);
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
        Array<String> currentChallenges = null;
        if (jsonChallenges.exists()) {
            currentChallenges = json.fromJson(Array.class, jsonChallenges);
        } else {
            currentChallenges = new Array<String>();
        }

        for (int i = 0; i < IDS.size; i++) {
            String id = IDS.get(i);
            if (id != null && !currentChallenges.contains(id, false)) {
                currentChallenges.add(id);
            }
        }
        String[] totalChallenges = new String[currentChallenges.size];
        for (int i = 0; i < currentChallenges.size; i++) {
            totalChallenges[i] = currentChallenges.get(i);
        }

        json.toJson(currentChallenges, String[].class, jsonChallenges);

        for (int i = 0; i < courses.size; ++i) {
            String courseId = courses.get(i).getCourseId();
            Array<Difficulty> courseDiffs = difficulties.get(courseId);
            if (courseDiffs != null) {
                courses.get(i).getEstimatedDifficulty(courseDiffs);
            }
        }

        FileHandle coursesFile = CHALLENGES_FOLDER.child("courses.json");
        json.toJson(courses, Array.class, coursesFile);

        // Cleaning
        TMP_FOLDER.deleteDirectory();
    }

    public static void downloadChallenges() {

        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        final Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url(GET_CHALLENGES_PATH)
                .header("Authorization", authorization).build();

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
                            IDS.add(id);

                            String courseId = challengeInfo.getString("_course");


                            if (challengeInfo.hasChild("challengeFile")) {
                                JsonValue challengeFile = challengeInfo.getChild("challengeFile");
                                while (!challengeFile.name().equals("difficulty")) {
                                    challengeFile = challengeFile.next();
                                }

                                String challengeDifficulty = challengeFile.asString();
                                Difficulty diff;
                                if (challengeDifficulty.equals("EASY")) {
                                    diff = Difficulty.EASY;
                                } else if (challengeDifficulty.equals("MEDIUM")) {
                                    diff = Difficulty.MEDIUM;
                                } else {
                                    diff = Difficulty.HARD;
                                }

                                Array<Difficulty> courseDifficulties = difficulties.get(courseId);
                                if (courseDifficulties == null) {
                                    courseDifficulties = new Array<Difficulty>();
                                    difficulties.put(courseId, courseDifficulties);
                                }
                                courseDifficulties.add(diff);

                            }

                            for (int j = 0; j < courses.size; j++) {
                                Course course = courses.get(j);
                                if (course.getCourseId().equals(courseId)) {
                                    Array<String> courseChallenges = course.getChallenges();
                                    if (courseChallenges == null) {
                                        courseChallenges = new Array<String>();
                                        course.setChallenges(courseChallenges);
                                    }

                                    if (!courseChallenges.contains(id, false)) {
                                        courseChallenges.add(id);
                                    }
                                }
                            }

                            downloadChallenge(id);
                        }
                    }
                    ++i;
                } while (challengeInfo != null);
            }

            @Override
            public void failed(Throwable t) {
                System.out.println("Failed to get challenges, t = " + t);
            }

            @Override
            public void cancelled() {
                System.out.println("cancelled");
            }
        });

    }

    public static void downloadChallenge(final String id) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        final Net.HttpRequest httpRequest = requestBuilder.newRequest()
                .method(Net.HttpMethods.GET).url(GET_CHALLENGES_UPLOAD_PATH + id + "/" + CHALLENGE_ZIP_NAME)
                .header("Authorization", authorization).build();

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
