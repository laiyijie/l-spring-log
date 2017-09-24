package me.laiyijie.spring.log;

import java.util.List;

/**
 * Created by admin on 2017-05-19.
 */
public class LogConfig {
    public LogConfig(List<String> fileNames) {
        if (fileNames != null) {
            fileNames.add("log4j2.xml");
        } else {
            System.setProperty("log4j.configurationFile", "log4j2.xml");
            return;
        }
        System.setProperty("log4j.configurationFile", String.join(",", fileNames));
    }
}
