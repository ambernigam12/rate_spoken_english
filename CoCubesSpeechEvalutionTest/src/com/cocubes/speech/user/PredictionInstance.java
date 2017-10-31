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

import com.cocubes.speech.statement.StatementClass;
import edu.cmu.sphinx.api.Configuration;
import java.util.HashMap;

public class PredictionInstance extends Thread {

    HashMap<Integer, StatementClass> statementMap;
    int statementId;
    UserStatementClass objUserStatement;
    long userId;
    Configuration configuration;
    HashMap<Integer, UserStatementClass> userStatementMap;
    String perUserPerStatementResult;

    public PredictionInstance(HashMap<Integer, StatementClass> statementMap, int statementId, UserStatementClass objUserStatement,
            long userId,
            Configuration configuration,
            HashMap<Integer, UserStatementClass> userStatementMap,
            String perUserPerStatementResult) {
        this.statementMap = statementMap;
        this.statementId = statementId;
        this.objUserStatement = objUserStatement;
        this.userId = userId;
        this.configuration = configuration;
        this.userStatementMap = userStatementMap;
        this.perUserPerStatementResult = perUserPerStatementResult;
    }

    @Override
    public void run() {
        objUserStatement.predictStatement(statementMap, configuration, userId, perUserPerStatementResult);
        userStatementMap.put(statementId, objUserStatement);
    }

}
