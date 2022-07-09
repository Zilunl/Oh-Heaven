package oh_heaven.game;

import ch.aplu.jcardgame.Deck;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropertiesLoader {
    public static final String DEFAULT_DIRECTORY_PATH =  "properties/";
    public static Properties properties;
    public PropertiesLoader(String propertiesFile) {
        this.properties = loadPropertiesFile(propertiesFile);
        //DEFAULT_DIRECTORY_PATH = "properties/";
    }

    private static Properties loadPropertiesFile(String propertiesFile) {
        if (propertiesFile == null) {
            try (InputStream input = new FileInputStream( DEFAULT_DIRECTORY_PATH + "runmode.properties")) {

                Properties prop = new Properties();

                // load a properties file
                prop.load(input);

                propertiesFile = DEFAULT_DIRECTORY_PATH + prop.getProperty("current_mode");
                System.out.println(propertiesFile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        try (InputStream input = new FileInputStream(propertiesFile)) {

            Properties prop = new Properties();
            prop.load(input);

            return prop;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static int loadSeed(){
        return Integer.parseInt(properties.getProperty("seed"));
    }

    public static int loadNbStartCards(){
        return Integer.parseInt(properties.getProperty("nbStartCards"));
    }

    public static boolean loadEnforeRules(){
        return Boolean.parseBoolean(properties.getProperty("enforceRules"));
    }

    public static int loadRounds(){
        return Integer.parseInt(properties.getProperty("rounds"));
    }

    public static AllPlayers loadAPlayer(int player){
        AllPlayers all = new AllPlayers(player);
        for (int i=0;i<player;i++){
            String playerType =  properties.getProperty("players."+i);
            if(playerType.equals("human")){
                all.addPlayer(new Human());
            }
            else if(playerType.equals("random")){
                all.addPlayer(new RandomNPC());
            }
            else if(playerType.equals("legal")){
                all.addPlayer(new LegalNPC());
            }
            else if(playerType.equals("smart")){
                all.addPlayer(new SmartNPC());
            }
        }
        return all;
    }


}