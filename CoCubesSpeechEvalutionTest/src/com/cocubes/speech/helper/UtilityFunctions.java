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
package com.cocubes.speech.helper;

import com.cocubes.speech.app_constants.ConstantsClass;
import com.cocubes.speech.logging.LoggingFunctions;
import com.cocubes.speech.statement.StatementClass;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class UtilityFunctions {

    private static List<Integer> validStatementIds = new ArrayList<>();

    //Create list of Valid Statement Ids
    public static boolean createValidStatementIds() {
        for (File statement : new File(UtilityPathFunction.getStatementDirectory()).listFiles()) {
            String statementId = statement.getName().replace(ConstantsClass.TEXT_FILE_EXTENSION, "").trim();
            if (intTryParse(statementId)) {
                validStatementIds.add(Integer.parseInt(statementId));
            } else {
                LoggingFunctions.InsertError(String.format("Invalid statement file path:%1s ", statement.getAbsolutePath()), "UtilityFunctions", "createValidStatementIds", ConstantsClass.NO_USERID_OR_STATEMENTID, ConstantsClass.NO_USERID_OR_STATEMENTID);
            }
        }
        return validStatementIds.size() > 0;
    }

    //Validate statement list created from user response list, i.e, list of sound files of a particular user
    public static boolean isValidStatementIdList(List<Integer> statementIdList) {
        boolean rValue = true;
        for (Integer statementId : statementIdList) {
            if (!validStatementIds.contains(statementId)) {
                LoggingFunctions.InsertError(String.format("Invalid statement Id:%1s ", statementId), "UtilityFunctions", "isValidStatementIdList", ConstantsClass.NO_USERID_OR_STATEMENTID, ConstantsClass.NO_USERID_OR_STATEMENTID);
                rValue = false;
                break;
            }
        }
        return rValue;
    }

    //Randomly pick a statement set for evaluation of current user
    public static int[] pickStatementSet() {
        return ConstantsClass.SET_OF_STATEMENTS[new Random().nextInt(ConstantsClass.NUMBER_OF_SETS)];
    }

    //Convert integer array to integer list
    public static List<Integer> convertArrayToList(int[] statementIdArray) {
        List<Integer> statementIdList = new ArrayList<>();
        for (int i = 0; i < statementIdArray.length; i++) {
            statementIdList.add(statementIdArray[i]);
        }
        return statementIdList;
    }

    //Populate statementMap with question statements
    public static boolean createOrUpdateStatementHashMap(List<Integer> statementIdList, HashMap<Integer, StatementClass> statementMap) {
        boolean rValue = isValidStatementIdList(statementIdList);
        for (int i = 0; rValue && i < statementIdList.size(); i++) {
            int statementId = statementIdList.get(i);
            if (!statementMap.containsKey(statementId)) {
                String text = readTextFromFile(statementId);
                if (text.length() > 0) {
                    StatementClass objStatement = new StatementClass(statementId, text);
                    rValue = objStatement.setValues();
                    if (rValue) {
                        statementMap.put(statementId, objStatement);
                    }
                } else {
                    rValue = false;
                    LoggingFunctions.InsertError(String.format("No text in file:%s", statementId), "UtilityFunctions", "creatOrUpdateStatementHashMap", ConstantsClass.NO_USERID_OR_STATEMENTID, statementId);
                }
            }
        }
        return rValue;
    }

    //Read question statements
    private static String readTextFromFile(int statementId) {
        String text = "";
        BufferedReader reader = null;
        try {
            String line;
            reader = new BufferedReader(new FileReader(UtilityPathFunction.getStatementFilePath(statementId)));
            while ((line = reader.readLine()) != null) {
                text += line;
            }
        } catch (Exception ex) {
            LoggingFunctions.InsertError(ex.getMessage(), "UtilityFunctions", "readTextFromFile", ConstantsClass.NO_USERID_OR_STATEMENTID, statementId);
            text = "";
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ex) {
                    text = "";
                    LoggingFunctions.InsertError(ex.getMessage(), "UtilityFunctions", "readTextFromFile", ConstantsClass.NO_USERID_OR_STATEMENTID, statementId);
                }
            }
        }
        return text;
    }
    
    //Check userId is valid, i.e, it is of long type 
    public static boolean longTryParse(String text) {
        boolean rValue = false;
        try {
            Long.parseLong(text);
            rValue = true;
        } catch (Exception ex) {
            LoggingFunctions.InsertError(String.format("Error message:%s UserId:%s", ex.getMessage(), text), "UtilityFunctions", "longTryParse", ConstantsClass.NO_USERID_OR_STATEMENTID, ConstantsClass.NO_USERID_OR_STATEMENTID);
        }
        return rValue;
    }

    //Check statementId is valid, i.e, it is of integer type 
    public static boolean intTryParse(String text) {
        boolean rValue = false;
        try {
            Integer.parseInt(text);
            rValue = true;
        } catch (Exception ex) {
            LoggingFunctions.InsertError(String.format("Error message:%s statementId:%s", ex.getMessage(), text), "UtilityFunctions", "intTryParse", ConstantsClass.NO_USERID_OR_STATEMENTID, ConstantsClass.NO_USERID_OR_STATEMENTID);
        }
        return rValue;
    }

    //Check whether word is difficult
    public static boolean isDifficultWord(String word) {
        return (word.length() >= 10);
    }

    //Check whether character is vowel
    public static boolean isVowel(char ch) {
        return "AEIOUaeiou".indexOf(ch) > -1;
    }

    public static int Minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

}
