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

import com.cocubes.speech.helper.UtilityPathFunction;
import com.cocubes.speech.statement.StatementClass;
import com.cocubes.speech.logging.LoggingFunctions;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import java.util.HashMap;

public class UserStatementClass {

    private final int statementId;
    private UserStatementAttributes userStatementAttributesNormal;
    private UserStatementAttributes userStatementAttributesTransformed;

    public UserStatementClass(int statementId) {
        this.statementId = statementId;
        userStatementAttributesNormal = new UserStatementAttributes();
        userStatementAttributesTransformed = new UserStatementAttributes();
    }

    public int getStatementId() {
        return statementId;
    }

    //Call UserStatementAttributes methods predictStatementFromReconizer, to predict each response, and setValues, to evaluate each response after prediction, for normal and transformed. 
    public boolean predictStatement(HashMap<Integer, StatementClass> statementMap, Configuration configuration,
        long userId, String perUserPerStatementResult) {
        boolean rValue = false;
        try {
            StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
            rValue = userStatementAttributesNormal.predictStatementFromRecognizer(userId, statementId, recognizer, UtilityPathFunction.getPredictedTextResultFilePathNormal(userId, statementId), UtilityPathFunction.getTimeStampFilePathNormal(userId, statementId), true);
            rValue = rValue && userStatementAttributesNormal.setValues(userId, statementId, statementMap.get(statementId).getOriginalText(), statementMap.get(statementId).getWordCount(),UtilityPathFunction.getPredictedAmplitudeFilePathNormal(userId, statementId));
            rValue = rValue && userStatementAttributesTransformed.predictStatementFromRecognizer(userId, statementId, recognizer, UtilityPathFunction.getPredictedTextResultFilePathTransformed(userId, statementId), UtilityPathFunction.getTimeStampFilePathTransformed(userId, statementId), false);
            rValue = rValue && userStatementAttributesTransformed.setValues(userId, statementId, statementMap.get(statementId).getOriginalText(), statementMap.get(statementId).getWordCount(),UtilityPathFunction.getPredictedAmplitudeFilePathTransformed(userId, statementId));
            rValue = rValue && writePerStatementResult(userId, statementMap, perUserPerStatementResult);
        } catch (Exception ex) {
            LoggingFunctions.InsertError(ex.getMessage(), "UserStatementClass", "predictStatement", userId, statementId);
        }
        return rValue;
    }

    //Write per statement attributes to perUserPerStatementResult
    private boolean writePerStatementResult(long userId, HashMap<Integer, StatementClass> statementMap, String perUserPerStatementResult) {
        return LoggingFunctions.writeIntoFile((new StringBuilder().append(userId).append(",").append(statementId).append(",").append(statementMap.get(statementId).getString())
                .append(",").append(userStatementAttributesNormal.getString()).append(",").append(userStatementAttributesTransformed.getString())).toString()
                , perUserPerStatementResult, true);
    }

    public UserStatementAttributes getUserStatementAttributesNormal() {
        return userStatementAttributesNormal;
    }

    public UserStatementAttributes getUserStatementAttributesTransformed() {
        return userStatementAttributesTransformed;
    }

}
