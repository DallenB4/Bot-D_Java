package glue_gunner4.botd.config;

import glue_gunner4.botd.server.BotdServer;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class BotdConfig {
    public static BotdConfig BOTDCONFIG;
    public static final String CONFIG_FILE_NAME = "Bot'D.properties";

    //Config variables/getters
    private boolean shouldReplace = true;
    public boolean getShouldReplace() {return shouldReplace;}

    private String token;
    public String getToken() {
        return token;
    }

    private String guild;
    public String getGuild() {
        return guild;
    }

    private String channel;
    public String getChannel() {
        return channel;
    }


    public static void registerConfig() {
        BotdConfig.BOTDCONFIG = new BotdConfig();
    }

    private BotdConfig() {
        File config = Configurd.getConfigFilePath(CONFIG_FILE_NAME).toFile();
        if (!config.exists()) {
            config = createConfigFile();
        }
        try {
            Properties prop = new Properties();
            FileInputStream stream = new FileInputStream(config);
            prop.load(stream);

            //Read properties
            shouldReplace = Boolean.parseBoolean(prop.getProperty("shouldReplace"));
            token = prop.getProperty("token");
            guild = prop.getProperty("guild");
            channel = prop.getProperty("channel");
        } catch (Exception e) {
            BotdServer.logError("Unable to load config!");
            e.printStackTrace();
        }

    }

    private static File createConfigFile() {
        try {
            File file = Configurd.getConfigFilePath(CONFIG_FILE_NAME).toFile();
            file.createNewFile();
            FileOutputStream stream = new FileOutputStream(file);

            Properties prop = new Properties();
            //Set properties
            prop.setProperty("shouldReplace", "true");
            prop.setProperty("token", "<INSERT TOKEN HERE!>");
            prop.setProperty("guild", "<INSERT GUILD ID HERE!>");
            prop.setProperty("channel", "<INSERT CHANNEL ID HERE!>");

            prop.store(stream, null);
            stream.close();
            return file;
        } catch (IOException e) {
            BotdServer.logError("Unable to create config!");
            e.printStackTrace();
        }
        return null;
    }
}
