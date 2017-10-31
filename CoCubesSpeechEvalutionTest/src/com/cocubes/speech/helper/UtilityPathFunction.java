package com.cocubes.speech.helper;

import com.cocubes.speech.app_constants.ConstantsClass;
import java.io.File;

public class UtilityPathFunction {
    
    static final String STATEMENT_DIRECTORY = "statements";
    static final String USER_DIRECTORY_NAME = "candidates";
    static final String PER_USER_RESULT = "PerUser.csv";
    static final String PER_USER_PER_STATEMENT_RESULT = "PerUserPerStatement.csv";
    static final String RESULT_DIRECTORY = "results";
    static final String PREDICTION_DIRECTORY = "prediction";
    static final String TIME_STAMP_DIRECTORY = "time_stamp";
    static final String NORMAL = "normal";
    static final String TRANSFORMED = "transformed";
    static final String PREDICTED_TEXT_RESULT_DIRECTORY = "text_result";
    static final String PREDICTED_AMPLITUDE_DIRECTORY = "amplitude";
    static final String SOUND_DIRECTORY_NAME = "sound_files";
    static final String LOG_DIRECTORY = "log";
    static final String ERROR_LOG_FILE = "SpeechEvaluation_ErrorLog.csv";
    static final String LOG_FILE = "SpeechEvaluation.log";
    static final String TRACKER_LOG_FILE = "SpeechEvaluation.slg";
    static final String MODEL_DIRECTORY = "models";
    static final String ACOUSTIC_MODEL_DIRECTORY = "en-new";
    static final String DICTIONARY = "en.dict";
    static final String LANGUAGE_MODEL = "eng.lm";

    public static String getAcousticModelPath(){
            return String.format("%s%s%s",MODEL_DIRECTORY,File.separator,ACOUSTIC_MODEL_DIRECTORY);
    }
    
    public static String getDictionaryPath(){
            return String.format("%s%s%s",MODEL_DIRECTORY,File.separator,DICTIONARY);
    }
    
    public static String getLanguageModelPath(){
            return String.format("%s%s%s",MODEL_DIRECTORY,File.separator,LANGUAGE_MODEL);
    }
    
    public static String getErrorLogFilePath() {
        return String.format("%s%s%s",LOG_DIRECTORY,File.separator,ERROR_LOG_FILE);
    }
    
    public static String getLogFilePath() {
        return String.format("%s%s%s",LOG_DIRECTORY,File.separator,LOG_FILE);
    }
    
    public static String getTrackerLogFile() {
        return String.format("%s%s%s",LOG_DIRECTORY,File.separator,TRACKER_LOG_FILE);
    }
    
    public static String getUserDirectory(){
            return USER_DIRECTORY_NAME;
    }
    
    public static String getStatementDirectory() {
        return STATEMENT_DIRECTORY;
    }
    
    public static String getStatementFilePath(int statementId) {
        return String.format("%s%s%d%s",STATEMENT_DIRECTORY,File.separator,statementId,ConstantsClass.TEXT_FILE_EXTENSION);
    }
    
    public static String getSounDirectoryPath(long userId) {
        return String.format("%s%s%d%s%s",USER_DIRECTORY_NAME,File.separator,userId,File.separator,SOUND_DIRECTORY_NAME);
    }
    
    public static String getSounFilePath(long userId, int statementId) {
        return String.format("%s%s%d%s%s%s%d%s",USER_DIRECTORY_NAME,File.separator,userId,File.separator,SOUND_DIRECTORY_NAME,File.separator,statementId,ConstantsClass.SOUND_FILE_EXTENSION);
    }
    
    public static String getPerUserResultPath() {
        return String.format("%s%s%s",RESULT_DIRECTORY,File.separator,PER_USER_RESULT);
    }
    
    public static String getPerUserPerStatementResultPath() {
        return String.format("%s%s%s",RESULT_DIRECTORY,File.separator,PER_USER_PER_STATEMENT_RESULT);
    }
    
    public static String getUserDirectoryPath(long userId) {
        return String.format("%s%s%d",USER_DIRECTORY_NAME,File.separator,userId);
    }
    
    public static String getPredictionDirectoryPath(long userId) {
        return String.format("%s%s%d%s%s",USER_DIRECTORY_NAME,File.separator,userId,File.separator,PREDICTION_DIRECTORY);
    }
    
    public static String getTimeStampDirectoryPath(long userId) {
        return String.format("%s%s%d%s%s%s%s",USER_DIRECTORY_NAME,File.separator,userId,File.separator,PREDICTION_DIRECTORY,File.separator,TIME_STAMP_DIRECTORY);
    }
    
    public static String getTimeStampDirectoryPathNormal(long userId) {
        return String.format("%s%s%d%s%s%s%s%s%s",USER_DIRECTORY_NAME,File.separator,userId,File.separator,PREDICTION_DIRECTORY,File.separator,TIME_STAMP_DIRECTORY,File.separator,NORMAL);
    }
    
    public static String getTimeStampFilePathNormal(long userId, int statementId) {
        return String.format("%s%s%d%s%s%s%s%s%s%s%d%s",USER_DIRECTORY_NAME,File.separator,userId,File.separator,PREDICTION_DIRECTORY,File.separator,TIME_STAMP_DIRECTORY,File.separator,NORMAL,File.separator,statementId,ConstantsClass.TEXT_FILE_EXTENSION);
    }
    
    public static String getTimeStampDirectoryPathTransformed(long userId) {
        return String.format("%s%s%d%s%s%s%s%s%s",USER_DIRECTORY_NAME,File.separator,userId,File.separator,PREDICTION_DIRECTORY,File.separator,TIME_STAMP_DIRECTORY,File.separator,TRANSFORMED);
    }
    
    public static String getTimeStampFilePathTransformed(long userId, int statementId) {
        return String.format("%s%s%d%s%s%s%s%s%s%s%d%s",USER_DIRECTORY_NAME,File.separator,userId,File.separator,PREDICTION_DIRECTORY,File.separator,TIME_STAMP_DIRECTORY,File.separator,TRANSFORMED,File.separator,statementId,ConstantsClass.TEXT_FILE_EXTENSION);
    }
    
    public static String getPredictedTextResultDirectoryPath(long userId) {
        return String.format("%s%s%d%s%s%s%s",USER_DIRECTORY_NAME,File.separator,userId,File.separator,PREDICTION_DIRECTORY,File.separator,PREDICTED_TEXT_RESULT_DIRECTORY);
    }
    
    public static String getPredictedTextResultDirectoryPathNormal(long userId) {
        return String.format("%s%s%d%s%s%s%s%s%s",USER_DIRECTORY_NAME,File.separator,userId,File.separator,PREDICTION_DIRECTORY,File.separator,PREDICTED_TEXT_RESULT_DIRECTORY,File.separator,NORMAL);
    }
    
    public static String getPredictedTextResultDirectoryPathTransformed(long userId) {
        return String.format("%s%s%d%s%s%s%s%s%s",USER_DIRECTORY_NAME,File.separator,userId,File.separator,PREDICTION_DIRECTORY,File.separator,PREDICTED_TEXT_RESULT_DIRECTORY,File.separator,TRANSFORMED);
    }
    
    public static String getPredictedTextResultFilePathNormal(long userId, int statementId) {
        return String.format("%s%s%d%s%s%s%s%s%s%s%d%s",USER_DIRECTORY_NAME,File.separator,userId,File.separator,PREDICTION_DIRECTORY,File.separator,PREDICTED_TEXT_RESULT_DIRECTORY,File.separator,NORMAL,File.separator,statementId,ConstantsClass.TEXT_FILE_EXTENSION);
    }
    
    public static String getPredictedTextResultFilePathTransformed(long userId, int statementId) {
        return String.format("%s%s%d%s%s%s%s%s%s%s%d%s",USER_DIRECTORY_NAME,File.separator,userId,File.separator,PREDICTION_DIRECTORY,File.separator,PREDICTED_TEXT_RESULT_DIRECTORY,File.separator,TRANSFORMED,File.separator,statementId,ConstantsClass.TEXT_FILE_EXTENSION);
    }
    
    public static String getPredictedAmplitudeDirectoryPath(long userId) {
        return String.format("%s%s%d%s%s%s%s",USER_DIRECTORY_NAME,File.separator,userId,File.separator,PREDICTION_DIRECTORY,File.separator,PREDICTED_AMPLITUDE_DIRECTORY);
    }
    
    public static String getPredictedAmplitudeDirectoryPathNormal(long userId) {
        return String.format("%s%s%d%s%s%s%s%s%s",USER_DIRECTORY_NAME,File.separator,userId,File.separator,PREDICTION_DIRECTORY,File.separator,PREDICTED_AMPLITUDE_DIRECTORY,File.separator,NORMAL);
    }
    
    public static String getPredictedAmplitudeDirectoryPathTransformed(long userId) {
        return String.format("%s%s%d%s%s%s%s%s%s",USER_DIRECTORY_NAME,File.separator,userId,File.separator,PREDICTION_DIRECTORY,File.separator,PREDICTED_AMPLITUDE_DIRECTORY,File.separator,TRANSFORMED);
    }
    
    public static String getPredictedAmplitudeFilePathNormal(long userId, int statementId) {
        return String.format("%s%s%d%s%s%s%s%s%s%s%d%s",USER_DIRECTORY_NAME,File.separator,userId,File.separator,PREDICTION_DIRECTORY,File.separator,PREDICTED_AMPLITUDE_DIRECTORY,File.separator,NORMAL,File.separator,statementId,ConstantsClass.TEXT_FILE_EXTENSION);
    }
    
    public static String getPredictedAmplitudeFilePathTransformed(long userId, int statementId) {
        return String.format("%s%s%d%s%s%s%s%s%s%s%d%s",USER_DIRECTORY_NAME,File.separator,userId,File.separator,PREDICTION_DIRECTORY,File.separator,PREDICTED_AMPLITUDE_DIRECTORY,File.separator,TRANSFORMED,File.separator,statementId,ConstantsClass.TEXT_FILE_EXTENSION);
    }
    
    public static String getRenameFilePath(long userId) {
        return String.format("%s%s%d%s",USER_DIRECTORY_NAME,File.separator,userId,ConstantsClass.USER_PROCESSED);
    }
    
}
