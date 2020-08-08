package com.isaiahvaris;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ConfigParser {
    private String filename; //name of file to be read

    /*map to store key/value pairs gotten from reading file
    LinkedHashMap to preserve order of values*/
    private Map<String, String> configProperties = new LinkedHashMap<>();

    /*This constructor is used to directly specify config file / environment to work on*/
    public ConfigParser(String filename) {
        //filname must be a valid config file
        if (!"config.txt".equals(filename) && !"config.txt.dev".equals(filename)
                && !"config.txt.staging".equals(filename)) {
            System.err.println(filename + " is not a valid config file.\n" +
                    "Please specify:\n config.txt for production, config.txt.dev for development,\n" +
                    "or config.txt.staging for staging environment config file");
        } else {
            this.filename = filename;
            readConfigFile();
        }
    }

    /*empty constructor using pre-defined environment to determine file,
    read file and populate map with readConfigFIle method*/
    public ConfigParser() {
        switch(Main.getEnvironment()) {
            case "development":
                setFilename("config.txt.dev");
                break;
            case "staging":
                setFilename("config.txt.staging");
                break;
            default: //default production environment
                setFilename("config.txt");
        }
        readConfigFile();
    }

    //getters and setters
    public String getFilename() { return filename; }

    public void setFilename(String filename) { this.filename = filename; }

    public Map<String, String> getConfigProperties() { return configProperties; }

    //method to read file and get key/value pairs
    public void readConfigFile() {
        //get pworking directory and filepath when running from...
        File file = new File("./");
        String path = file.getAbsolutePath().replace(".", "");
        String fileLocation = "";
        if (path.endsWith("isaiahvaris/")) {//...command line
            fileLocation = path;
        } else {
            fileLocation = "src/com/isaiahvaris/";//...Main
        }

        String textLine;

        /*try-with-resources block handles exception and automatically closes connection
         * BufferedReader makes it easy to read each line of file
         * relative path ensures file can be found even if working directory changes*/
        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileLocation + getFilename()))) {
            //loop through each line till you get to the end of the file
            while ((textLine = br.readLine()) != null) {
                if(textLine.contains("=")) { //indicates key/value pair we need
                    String[] textLineArray = textLine.split("="); //get key/value pair
                    if ("name".equals(textLineArray[0]) || "port".equals(textLineArray[0]) || "context-url".equals(textLineArray[0])) {
                        //set application name "name/port/context-url" to pre-defined recognizable key value
                        textLineArray[0] = "application." + textLineArray[0];
                    } //putIfAbsent to ensure already set value of application name doesn't get overridden
                    configProperties.putIfAbsent(textLineArray[0], textLineArray[1]);
                }
            }
        } catch (IOException e) {//catch possible FileNotFound or EOF exceptions
            e.printStackTrace();
        }
    }

    //Method to get value corresponding to key from map
    public String get(String key) {
        return configProperties.get(key);
    }
}