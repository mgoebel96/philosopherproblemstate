package de.netempire.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ResultLogger {

    private ResultLogger() {
        throw new IllegalStateException("Logger class");
    }

    public static synchronized void log(String printText){
        System.out.println(printText);

        try {
            Logger logger = Logger.getLogger("MyLog");
            // This block configure the logger with handler and formatter
            FileHandler fh = new FileHandler("C:/dev/philosopherproblemwithoutsemaphore/files/MyLogFile.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            // the following statement is used to log any messages
            logger.info(printText);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }
}