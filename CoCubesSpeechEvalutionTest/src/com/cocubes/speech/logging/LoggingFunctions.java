package com.cocubes.speech.logging;

import com.cocubes.speech.helper.UtilityPathFunction;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggingFunctions {

    public static boolean InsertLog(String content) {
        return writeIntoFile(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + " : " + content, UtilityPathFunction.getLogFilePath(), true);
    }

    public static boolean InsertError(String content, String className, String functionName, long userId, int statementId) {
        return writeIntoFile("\"" + content + "\"" + "," + functionName + "," + className + "," + userId + "," + statementId + "," + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), UtilityPathFunction.getErrorLogFilePath(), true);
    }

    public static boolean InsertTrackerLog(int statementsEvaluatedCount) {
        return writeIntoFile(String.format("%-15d", statementsEvaluatedCount), UtilityPathFunction.getTrackerLogFile(), false);
    }

    public static boolean writeIntoFile(String content, String filePath, boolean isAppend) {
        boolean rValue = false;
        try {
            content += System.lineSeparator();
            File file = new File(filePath);
            file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), isAppend);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            fw.close();
            rValue = true;
        } catch (IOException ex) {
            File logDirectory = new File(UtilityPathFunction.getLogFilePath()).getParentFile();
            if (!logDirectory.exists()) {
                logDirectory.mkdir();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage()+" : "+content.trim());
        }
        return rValue;
    }

}
