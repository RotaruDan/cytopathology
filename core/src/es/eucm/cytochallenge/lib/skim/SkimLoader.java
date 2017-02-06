package es.eucm.cytochallenge.lib.skim;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;

/** {@link AssetLoader} for {@link Skin} instances. All {@link Texture} and {@link BitmapFont} instances will be loaded as
 * dependencies. Passing a {@link SkinParameter} allows the exact name of the texture associated with the skin to be specified.
 * Otherwise the skin texture is looked up just as with a call to {@link Skin#Skin(com.badlogic.gdx.files.FileHandle)}. A
 * {@link SkinParameter} also allows named resources to be set that will be added to the skin before loading the json file,
 * meaning that they can be referenced from inside the json file itself. This is useful for dynamic resources such as a BitmapFont
 * generated through FreeTypeFontGenerator.
 * @author Nathan Sweet */
public class SkimLoader extends AsynchronousAssetLoader<Skim, SkimLoader.SkinParameter> {

    private final String scale;

    public SkimLoader (FileHandleResolver resolver, String scale) {
        super(resolver);
        this.scale = scale;
    }

    @Override
    public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, SkimLoader.SkinParameter parameter) {
        Array<AssetDescriptor> deps = new Array();
        if (parameter == null || parameter.textureAtlasPath == null)
            deps.add(new AssetDescriptor(file.pathWithoutExtension() + ".atlas", TextureAtlas.class));
        else if (parameter.textureAtlasPath != null){
            deps.add(new AssetDescriptor(parameter.textureAtlasPath, TextureAtlas.class));
        }
        return deps;
    }

    @Override
    public void loadAsync (AssetManager manager, String fileName, FileHandle file, SkinParameter parameter) {
    }

    @Override
    public Skim loadSync (AssetManager manager, String fileName, FileHandle file, SkinParameter parameter) {
        String textureAtlasPath = file.pathWithoutExtension() + ".atlas";
        ObjectMap<String, Object> resources = null;
        if (parameter != null) {
            if (parameter.textureAtlasPath != null){
                textureAtlasPath = parameter.textureAtlasPath;
            }
            if (parameter.resources != null){
                resources = parameter.resources;
            }
        }
        TextureAtlas atlas = manager.get(textureAtlasPath, TextureAtlas.class);
        Skim skin = new Skim(atlas, scale);
        if (resources != null) {
            for (Entry<String, Object> entry : resources.entries()) {
                skin.add(entry.key, entry.value);
            }
        }

        skin.load(file.parent().parent().child("skin.json"));
        return skin;
    }

    static public class SkinParameter extends AssetLoaderParameters<Skim> {
        public final String textureAtlasPath;
        public final ObjectMap<String, Object> resources;

        public SkinParameter () {
            this(null, null);
        }

        public SkinParameter(ObjectMap<String, Object> resources){
            this(null, resources);
        }

        public SkinParameter (String textureAtlasPath) {
            this(textureAtlasPath, null);
        }

        public SkinParameter (String textureAtlasPath, ObjectMap<String, Object> resources) {
            this.textureAtlasPath = textureAtlasPath;
            this.resources = resources;
        }
    }
}
