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
        Set<Map.Entry<String, String>> entries = config.getConfigProperties().entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }

    //static variable to set environment based on command line argument
    public static String environment;

    public static String getEnvironment() { return environment; }
}