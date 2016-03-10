package es.eucm.cytochallenge;

import com.badlogic.gdx.graphics.Texture;
import es.eucm.maven.plugins.piel.GenerateSkins;

import java.util.Properties;

public class SkinGenerator {
    public static void main(String[] arg) {

        Properties ttfs = new Properties();
        ttfs.put("./skin/skins-raw/Roboto-Regular.ttf", 24);
        new GenerateSkins("./skin/skins-raw/images",
                "./skin/skins-raw/svg",
                "./skin/skins-raw/9patch",
                "./skin/skins-raw/png-temp",
                new String[]{
                        "1.0", "2.0"
                }, ttfs, Texture.TextureFilter.Linear, 1024, "skin", "./android/assets/skin")
                .execute();
    }
}

