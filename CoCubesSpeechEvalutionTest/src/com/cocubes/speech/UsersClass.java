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
package com.cocubes.speech;

import com.cocubes.speech.helper.UtilityFunctions;
import com.cocubes.speech.app_constants.ConstantsClass;
import com.cocubes.speech.helper.UtilityPathFunction;
import com.cocubes.speech.logging.LoggingFunctions;
import com.cocubes.speech.statement.StatementClass;
import com.cocubes.speech.user.UserClass;
import edu.cmu.sphinx.api.Configuration;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersClass {

    private HashMap<Long, UserClass> userClassMap;
    private HashMap<Long, List<Integer>> statementIdMap;

    UsersClass() {
        userClassMap = new HashMap<>();
        statementIdMap = new HashMap<>();
    }

    //Validate and Add current user to userClassMap for evaluation
    public boolean updateUserClassMap(long userId, int[] statementIdArray) {
        boolean rValue = true;
        userClassMap.put(userId, null);
        List<Integer> statementIdList = statementIdMapFunction(userId);
        statementIdMap.put(userId, statementIdList);
        if (statementIdList != null && statementIdList.size() >= statementIdArray.length) {
            for (int i = 0; i < statementIdArray.length; i++) {
                if (!statementIdList.contains(statementIdArray[i])) {
                    rValue = false;
                    LoggingFunctions.InsertError(String.format("Sound file for statement Id:%s not present", statementIdArray[i]), "UsersClass", "updateUserClassMapFormExistingUserDirectory", userId, ConstantsClass.NO_USERID_OR_STATEMENTID);
                    break;
                }
            }
        }
        return rValue;
    }

    //Populate userClassMap with existing not evaluated users
    public void updateUserClassMapFormExistingUserDirectory(HashMap<Integer, StatementClass> statementMap) {
        for (File userFile : new File(UtilityPathFunction.getUserDirectory()).listFiles()) {
            String userFileName = userFile.getName();
            if (!userFileName.contains(ConstantsClass.USER_PROCESSED)) {
                if (UtilityFunctions.longTryParse(userFileName)) {
                    long userId = Long.parseLong(userFileName);
                    userClassMap.put(userId, null);
                    List<Integer> statementIdList = statementIdMapFunction(userId);
                    statementIdList = statementIdList != null && UtilityFunctions.createOrUpdateStatementHashMap(statementIdList, statementMap) ? statementIdList : null;
                    statementIdMap.put(userId, statementIdList);
                } else {
                    LoggingFunctions.InsertError(String.format("Invalid user directory path:%1s ", userFile.getAbsolutePath()), "UsersClass", "updateUserClassMapFormExistingUserDirectory", ConstantsClass.NO_USERID_OR_STATEMENTID, ConstantsClass.NO_USERID_OR_STATEMENTID);
                }
            }
        }
    }

    //Populate statementIdMap with list of question statements 
    private List<Integer> statementIdMapFunction(long userId) {
        List<Integer> statementIdList = null;
        File[] soundFileList = new File(UtilityPathFunction.getSounDirectoryPath(userId)).listFiles();
        if (soundFileList.length >= ConstantsClass.NUMBER_OF_STATEMENTS) {
            statementIdList = new ArrayList<>();
            for (File currentFile : soundFileList) {
                String statementId = currentFile.getName().replace(ConstantsClass.SOUND_FILE_EXTENSION, "").trim();
                if (UtilityFunctions.intTryParse(statementId)) {
                    statementIdList.add(Integer.parseInt(statementId));
                } else {
                    LoggingFunctions.InsertError(String.format("Invalid sound file path:%1s ", currentFile.getAbsolutePath()), "UsersClass", "statementIdMapFunction", userId, ConstantsClass.NO_USERID_OR_STATEMENTID);
                }
            }
        } else {
            LoggingFunctions.InsertError(String.format("Number of sound file != %d for user:%d ", ConstantsClass.NUMBER_OF_STATEMENTS, userId), "UsersClass", "statementIdMapFunction", userId, ConstantsClass.NO_USERID_OR_STATEMENTID);
        }
        return statementIdList != null && statementIdList.size() >= ConstantsClass.NUMBER_OF_STATEMENTS ? statementIdList : null;
    }

    //Call EvaluateUser method for all user in userClassMap
    public int Evaluate(HashMap<Integer, StatementClass> statementMap, boolean isIntonation) {
        Configuration configuration = new Configuration();
        // Load model from the jar
        configuration
                .setAcousticModelPath(UtilityPathFunction.getAcousticModelPath());
        // You can also load model from folderoise
        // configuration.setAcousticModelPath("file:en-us");
        configuration
                .setDictionaryPath(UtilityPathFunction.getDictionaryPath());
        configuration
                .setLanguageModelPath(UtilityPathFunction.getLanguageModelPath());
        //Start logging
        LoggingFunctions.InsertLog("Start logging");
        int englishLevel = 0;
        String result = UtilityPathFunction.getPerUserResultPath();
        if (!new File(result).exists()) {
            LoggingFunctions.writeIntoFile(ConstantsClass.PER_USER_RESLUT_HEADER_STRING, result, false);
        }
        String perUserPerStatementResult = UtilityPathFunction.getPerUserPerStatementResultPath();
        if (!new File(perUserPerStatementResult).exists()) {
           LoggingFunctions.writeIntoFile(ConstantsClass.PER_STATEMENT_RESULT_HEADER_STRING, perUserPerStatementResult, false);
        }
        for (Map.Entry<Long, List<Integer>> entry : statementIdMap.entrySet()) {
            long userId = entry.getKey();
            if (userClassMap.get(userId) == null) {
                if (entry.getValue() != null) {
                    if (UtilityFunctions.isValidStatementIdList(entry.getValue())) {
                        LoggingFunctions.InsertLog(String.format("Start user:%d", userId));
                        UserClass objUserClass = EvaluateUser(userId, entry.getValue(), statementMap, configuration, result, perUserPerStatementResult, isIntonation);
                        if (objUserClass != null) {
                            userClassMap.put(userId, objUserClass);
                            englishLevel = objUserClass.getUserAttributesNormal().getProficiencyLevel();
                        } else {
                            englishLevel = ConstantsClass.CODE_FAIL_VALUE;
                            break;
                        }
                        LoggingFunctions.InsertLog(String.format("End user:%d", userId));
                    } else {
                        LoggingFunctions.InsertError(String.format("Invalid Statement list for user:%s", userId), "UsersClass", "Evaluate", userId, ConstantsClass.NO_USERID_OR_STATEMENTID);
                    }
                } else {
                    LoggingFunctions.InsertError(String.format("Statement list empty for user:%s", userId), "UsersClass", "Evaluate", userId, ConstantsClass.NO_USERID_OR_STATEMENTID);
                }
            }
        }
        //End logging
        LoggingFunctions.InsertLog(String.format("Tool run : %s", englishLevel > ConstantsClass.CODE_FAIL_VALUE ? "successful" : "failed"));
        return englishLevel;
    }

    //Evaluate 1 user of userClassMap and write to result
    private UserClass EvaluateUser(long userId, List<Integer> statementId, HashMap<Integer, StatementClass> statementMap, Configuration configuration, String result, String perUserPerStatementResult, boolean isIntonation) {
        boolean rValue = true;
        UserClass objUserClass = new UserClass(userId);
        rValue = rValue && objUserClass.setValues(statementMap, statementId, configuration, perUserPerStatementResult, isIntonation);
        rValue = rValue && LoggingFunctions.writeIntoFile(objUserClass.getString(), result, true);
        if (rValue) {
          if (!new File(UtilityPathFunction.getUserDirectoryPath(userId)).renameTo(new File(UtilityPathFunction.getRenameFilePath(userId)))) {
               rValue = false;
                LoggingFunctions.InsertError(String.format("User directory: %s not renamed", userId), "UsersClass", "EvaluateProficiency", userId, ConstantsClass.NO_USERID_OR_STATEMENTID);
            }
        }
        return rValue ? objUserClass : null;
    }
}
