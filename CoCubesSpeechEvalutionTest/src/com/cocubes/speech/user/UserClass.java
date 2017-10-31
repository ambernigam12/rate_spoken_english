/*
 * /*region CoCubes Copyright Details
 *
 *  // /////////////////////////////////////////////////////////////////////////////////////////////////////
 *  //
 *  //
 *  //  All rights reserved by CoCubes.com
 *  //
 *  //
 *  //  (c) Copyright 2008-2017 CoCubes Technologies Pvt. Ltd.,
 *  //    http://www.cocubes.com/
 *  //
 *  //
 *  // ////////////////////////////////////////////////////////////////////////////////////////////////////////
 *
 */
package com.cocubes.speech.user;

import com.cocubes.speech.app_constants.ConstantsClass;
import com.cocubes.speech.helper.UtilityPathFunction;
import com.cocubes.speech.logging.LoggingFunctions;
import com.cocubes.speech.statement.StatementClass;
import edu.cmu.sphinx.api.Configuration;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UserClass {

    private final long userId;
    private final HashMap<Integer, UserStatementClass> userStatementMap;
    private final UserAttributes userAttributesNormal;
    private final UserAttributes userAttributesTransformed;
    private int wordCountOriginalText;
    private float avgWordLength;
    private int difficultWordCout;

    public UserClass(long userId) {
        this.userId = userId;
        userStatementMap = new HashMap<>();
        userAttributesNormal = new UserAttributes();
        userAttributesTransformed = new UserAttributes();
    }

    //Create directories, call PredictionInstance threads for predicting and evaluating each user respone and set all attributes
    public boolean setValues(HashMap<Integer, StatementClass> statementMap, List<Integer> statementIdList, Configuration configuration, String perUserPerStatementResult, boolean isIntonation) {
        boolean rValue;
        String startTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        // <editor-fold desc="Creating Directory">
        File filePredictionDirectory = new File(UtilityPathFunction.getPredictionDirectoryPath(userId));
        rValue = filePredictionDirectory.exists() || filePredictionDirectory.mkdir();
        if (rValue) {
            File fileTimeStampDirectory = new File(UtilityPathFunction.getTimeStampDirectoryPath(userId));
            rValue = fileTimeStampDirectory.exists() || fileTimeStampDirectory.mkdir();
            if (rValue) {
                File fileTimeStampDirectoryNormal = new File(UtilityPathFunction.getTimeStampDirectoryPathNormal(userId));
                rValue = fileTimeStampDirectoryNormal.exists() || fileTimeStampDirectoryNormal.mkdir();
                if (rValue) {
                    File fileTimeStampDirectoryTransformed = new File(UtilityPathFunction.getTimeStampDirectoryPathTransformed(userId));
                    rValue = fileTimeStampDirectoryTransformed.exists() || fileTimeStampDirectoryTransformed.mkdir();
                    if (rValue) {
                        File filePredictedTextDirectory = new File(UtilityPathFunction.getPredictedTextResultDirectoryPath(userId));
                        rValue = filePredictedTextDirectory.exists() || filePredictedTextDirectory.mkdir();
                        if (rValue) {
                            File filePredictedTextDirectoryNormal = new File(UtilityPathFunction.getPredictedTextResultDirectoryPathNormal(userId));
                            rValue = filePredictedTextDirectoryNormal.exists() || filePredictedTextDirectoryNormal.mkdir();
                            if (rValue) {
                                File filePredictedTextDirectoryTransformed = new File(UtilityPathFunction.getPredictedTextResultDirectoryPathTransformed(userId));
                                rValue = filePredictedTextDirectoryTransformed.exists() || filePredictedTextDirectoryTransformed.mkdir();
                                if (!rValue) {
                                    LoggingFunctions.InsertError(String.format("Directory not created path:%1s", filePredictedTextDirectoryTransformed), "UserClass", "setValues", userId, ConstantsClass.NO_USERID_OR_STATEMENTID);
                                }
                            } else {
                                LoggingFunctions.InsertError(String.format("Directory not created path:%1s", filePredictedTextDirectoryNormal), "UserClass", "setValues", userId, ConstantsClass.NO_USERID_OR_STATEMENTID);
                            }
                        } else {
                            LoggingFunctions.InsertError(String.format("Directory not created path:%1s", filePredictedTextDirectory), "UserClass", "setValues", userId, ConstantsClass.NO_USERID_OR_STATEMENTID);
                        }
                    } else {
                        LoggingFunctions.InsertError(String.format("Directory not created path:%1s", fileTimeStampDirectoryTransformed), "UserClass", "setValues", userId, ConstantsClass.NO_USERID_OR_STATEMENTID);
                    }
                } else {
                    LoggingFunctions.InsertError(String.format("Directory not created path:%1s", fileTimeStampDirectoryNormal), "UserClass", "setValues", userId, ConstantsClass.NO_USERID_OR_STATEMENTID);
                }
            } else {
                LoggingFunctions.InsertError(String.format("Directory not created path:%1s", fileTimeStampDirectory), "UserClass", "setValues", userId, ConstantsClass.NO_USERID_OR_STATEMENTID);
            }
        } else {
            LoggingFunctions.InsertError(String.format("Directory not created path:%1s", filePredictionDirectory), "UserClass", "setValues", userId, ConstantsClass.NO_USERID_OR_STATEMENTID);
        }
        //amplitude
        if (rValue) {
            File filePredictedAmplitudeDirectory = new File(UtilityPathFunction.getPredictedAmplitudeDirectoryPath(userId));
            rValue = filePredictedAmplitudeDirectory.exists() || filePredictedAmplitudeDirectory.mkdir();
            if (rValue) {
                File filePredictedAmplitudeDirectoryNormal = new File(UtilityPathFunction.getPredictedAmplitudeDirectoryPathNormal(userId));
                rValue = filePredictedAmplitudeDirectoryNormal.exists() || filePredictedAmplitudeDirectoryNormal.mkdir();
                if (rValue) {
                    File filePredictedAmplitudeDirectoryTransformed = new File(UtilityPathFunction.getPredictedAmplitudeDirectoryPathTransformed(userId));
                    rValue = filePredictedAmplitudeDirectoryTransformed.exists() || filePredictedAmplitudeDirectoryTransformed.mkdir();
                    if (!rValue) {
                        LoggingFunctions.InsertError(String.format("Directory not created path:%1s", filePredictedAmplitudeDirectoryTransformed), "UserClass", "setValues", userId, ConstantsClass.NO_USERID_OR_STATEMENTID);
                    }
                } else {
                    LoggingFunctions.InsertError(String.format("Directory not created path:%1s", filePredictedAmplitudeDirectoryNormal), "UserClass", "setValues", userId, ConstantsClass.NO_USERID_OR_STATEMENTID);
                }
            } else {
                LoggingFunctions.InsertError(String.format("Directory not created path:%1s", filePredictedAmplitudeDirectory), "UserClass", "setValues", userId, ConstantsClass.NO_USERID_OR_STATEMENTID);
            }
        }

        // </editor-fold>
        if (rValue) {
            try {
                PredictionInstance[] predictionThread = new PredictionInstance[ConstantsClass.NUMBER_OF_THREADS];
                for (int i = 0; i < statementIdList.size();) {
                    int j = 0;
                    while (rValue && j < ConstantsClass.NUMBER_OF_THREADS && i < statementIdList.size()) {
                        int statementId = statementIdList.get(i);
                        LoggingFunctions.InsertTrackerLog(statementId);
                        predictionThread[j] = new PredictionInstance(statementMap, statementId, new UserStatementClass(statementId), userId, configuration, userStatementMap, perUserPerStatementResult);
                        predictionThread[j].start();
                        j++;
                        i++;
                    }

                    for (int k = 0; k < j; k++) {
                        predictionThread[k].join();
                    }
                }
            } catch (NumberFormatException | InterruptedException ex) {
                rValue = false;
                LoggingFunctions.InsertError(ex.getMessage(), "UserClass", "setValues", userId, ConstantsClass.NO_USERID_OR_STATEMENTID);
            }

            if (rValue) {
                for (UserStatementClass userStatement : userStatementMap.values()) {
                    int statementId = userStatement.getStatementId();
                    difficultWordCout += statementMap.get(statementId).getDifficultWordCount();
                    wordCountOriginalText += statementMap.get(statementId).getWordCount();
                    avgWordLength += statementMap.get(statementId).getLength();
                }
                rValue = wordCountOriginalText > 0;
                if (rValue) {
                    avgWordLength = avgWordLength / wordCountOriginalText;
                    userAttributesNormal.Evaluate(userStatementMap, true, isIntonation);
                    rValue = userAttributesNormal.setScore(wordCountOriginalText, isIntonation);
                    userAttributesTransformed.Evaluate(userStatementMap, false, isIntonation);
                    rValue = rValue && userAttributesTransformed.setScore(wordCountOriginalText, isIntonation);
                } else {
                    LoggingFunctions.InsertError("wordCountOriginalText = 0", "UserClass", "setValues", userId, ConstantsClass.NO_USERID_OR_STATEMENTID);
                }
                String endTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
                LoggingFunctions.InsertLog("User: " + userId + " Start Time: " + startTime + " End Time: " + endTime);
            }
        }
        return rValue;
    }

    public UserAttributes getUserAttributesNormal() {
        return userAttributesNormal;
    }

    public UserAttributes getUserAttributesTransformed() {
        return userAttributesTransformed;
    }

    //Get String to write user attributes to Result
    public String getString() {
        return (new StringBuilder().append(userId).append(",").append(wordCountOriginalText).append(",").append(avgWordLength).append(",").append(difficultWordCout)
                .append(",").append(userAttributesNormal.getString()).append(",").append(userAttributesTransformed.getString()).append(",").append(userAttributesNormal.getScore())
                .append(",").append(userAttributesTransformed.getScore()).append(",").append(userAttributesNormal.getProficiencyLevel())).toString();
    }

}
