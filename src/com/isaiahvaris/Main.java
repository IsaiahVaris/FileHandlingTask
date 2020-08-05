package com.isaiahvaris;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static void main(String[] args) {
		//Set environment for execution based on command line argument
        if(args.length == 0) {//no argument means default production environment
            environment = "production";
        } //Argument must be either staging or development only...
        else if ((!"development".equals(args[0]) && !"staging".equals(args[0])) || args.length > 1) {
            //...if not alert user and return
            System.err.println("Please specify either \"staging\" or \"development\" environment " +
                    "or use default production environment");
            return;
        } else {//argument is either staging or development
            environment = args[0];
        }

        //instance of ConfigParser
        ConfigParser config = new ConfigParser();

        //get value using key
        System.out.println(config.get("dbname"));
        System.out.println(config.get("application.name"));
        System.out.println();

        //Print out full list of key/value pairs for config file
        Set<Map.Entry<String, String>> entries = config.configProperties.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }

    //static variable to set environment based on command line argument
    public static String environment;

    static class ConfigParser {
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
                return;
            } else {
                this.filename = filename;
                readConfigFile();
            }
        }

        /*empty constructor using pre-defined environment to determine file,
        read file and populate map with readConfigFIle method*/
        public ConfigParser() {
            switch(environment) {
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

        public String getFilename() { return filename; }

        public void setFilename(String filename) { this.filename = filename; }

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
                            //set application name "name" to pre-defined recognizable key value
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
}
