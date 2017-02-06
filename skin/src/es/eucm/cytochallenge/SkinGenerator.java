package es.eucm.cytochallenge;

import com.badlogic.gdx.graphics.Texture;
import es.eucm.maven.plugins.piel.GenerateSkins;

import java.io.File;
import java.util.Properties;

public class SkinGenerator {
    public static void main(String[] arg) {

        Properties ttfs = new Properties();
        ttfs.put("./skin/skins-raw/Roboto-Regular.ttf", 16);
        new GenerateSkins(new File("./skin/skins-raw/images"),
                new File("./skin/skins-raw/svg"),
                new File("./skin/skins-raw/9patch"),
                new File("./skin/skins-raw/png-temp"),
                new String[]{
                        "1.0", "1.5", "2.0", "2.5", "3.0", "4.0"
                }, ttfs, Texture.TextureFilter.Linear, 1024, 2048, "skin", new File("./android/assets/skin"))
                .execute();
    }
}

