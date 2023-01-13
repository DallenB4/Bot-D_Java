package glue_gunner4.botd.config;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mojang.datafixers.util.Pair;
import glue_gunner4.botd.server.BotdServer;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.util.ArrayList;

public class PlayersConfig {
    public static PlayersConfig PLAYERSCONFIG;
    public static final String CONFIG_FILE_NAME = "Players.json";
    //Config values
    public static ArrayList<Pair<String, String>> PLAYERS;


    public static void registerConfig() {
        PLAYERSCONFIG = new PlayersConfig();
    }

    private PlayersConfig() {
        File config = Configurd.getConfigFilePath(CONFIG_FILE_NAME).toFile();
        if (!config.exists()) {
            config = createConfigFile();
        }
        try {
            JsonReader reader = new JsonReader(new FileReader(config));

            PLAYERS = new ArrayList<>();
            reader.beginObject();
            while (reader.hasNext()) {
                JsonToken token = reader.peek();
                if (token.equals(JsonToken.END_OBJECT)) {
                    reader.endObject();
                    break;
                }
                Pair nextPair = new Pair(reader.nextName(), reader.nextString());
                PLAYERS.add(nextPair);
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
            writer.name("Steve").value("123456789");
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
