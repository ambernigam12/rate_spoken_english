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
package com.cocubes.speech.statement;

import com.cocubes.speech.app_constants.ConstantsClass;
import com.cocubes.speech.helper.UtilityFunctions;
import com.cocubes.speech.logging.LoggingFunctions;

public class StatementClass {

    private final int statementId;
    private float totalWordCharacterLength;
    private int difficultWordCount;
    private int wordCount;
    private float avgLength;
    private String originalText;

    public StatementClass(int statementId, String originalText) {
        this.statementId = statementId;
        this.originalText = originalText;
    }

    private void setDifficultWordCountAndTotalWordLength(String[] wordInString) {
        for (String word : wordInString) {
            if (word.length() > 0) {
                if (UtilityFunctions.isDifficultWord(word)) {
                    difficultWordCount++;
                }
                totalWordCharacterLength += word.length();
            }
        }
    }

    public boolean setValues() {
        boolean rValue;
        String[] wordsInString = (originalText.replaceAll("\\.|\\,", " ")).replaceAll(" +", " ").trim().split(" ");
        wordCount = wordsInString.length;
        rValue = wordCount > 0;
        if (rValue) {
            setDifficultWordCountAndTotalWordLength(wordsInString);
            avgLength = totalWordCharacterLength * ConstantsClass.FLOAT_MULTIPLIER / wordCount;
        } else {
            LoggingFunctions.InsertError(String.format("No text in file:%s", statementId), "StatementClass", "setValues", ConstantsClass.NO_USERID_OR_STATEMENTID, statementId);
        }
        return rValue;
    }

    public int getStatementId() {
        return statementId;
    }

    public float getLength() {
        return totalWordCharacterLength;
    }

    public int getDifficultWordCount() {
        return difficultWordCount;
    }

    public int getWordCount() {
        return wordCount;
    }

    public String getOriginalText() {
        return originalText;
    }

    //Get String to write statement attributes to perUserPerStatementResult
    public StringBuilder getString() {
        return new StringBuilder().append(wordCount).append(",").append(avgLength).append(",").append(difficultWordCount);
    }

}
