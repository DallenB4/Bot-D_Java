package glue_gunner4.botd.config;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mojang.datafixers.util.Pair;
import glue_gunner4.botd.server.BotdServer;
import net.dv8tion.jda.api.entities.emoji.Emoji;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EmojisConfig implements Configurd{
    public static final String CONFIG_FILE_NAME = "Emojis.json";
    //Config values
    private static ArrayList<Pair<String, String>> EMOJIS;

    public static String getEmojiFromName(String name) {
        name = name.toLowerCase();
        for (Pair<String, String> pair : EMOJIS) {
            if (pair.getFirst().equals(name)) {
                return "<:" + name + ":" + pair.getSecond() + ">";
            }
        }
        BotdServer.logError("Failed to get emoji \"" + name + "\"");
        return "<:no_entry_sign:>";
    }

    public static void registerConfig() {
        File config = Configurd.getConfigFilePath(CONFIG_FILE_NAME).toFile();
        if (!config.exists()) {
            config = createConfigFile();
        }
        try {
            JsonReader reader = new JsonReader(new FileReader(config));

            EMOJIS = new ArrayList<>();
            reader.beginObject();
            while (reader.hasNext()) {
                JsonToken token = reader.peek();
                if (token.equals(JsonToken.END_OBJECT)) {
                    reader.endObject();
                    break;
                }
                Pair<String, String> nextPair = new Pair<>(reader.nextName(), reader.nextString());
                EMOJIS.add(nextPair);
            }

        } catch (Exception e) {
            BotdServer.logError("Unable to load config!");
            e.printStackTrace();
        }

    }

    private static File createConfigFile() {
        try {
            File file = Configurd.getConfigFilePath(CONFIG_FILE_NAME).toFile();
            file.createNewFile();

            JsonWriter writer = new JsonWriter(new FileWriter(file));
            writer.setIndent("    ");
            writer.beginObject();
            writer.name("smiley").value("123456789");
            writer.endObject();
            writer.close();

            return file;
        } catch (IOException e) {
            BotdServer.logError("Unable to create config!");
            e.printStackTrace();
        }
        return null;
    }
}
