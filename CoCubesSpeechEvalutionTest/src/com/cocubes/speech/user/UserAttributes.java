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
import java.util.HashMap;
import java.util.Map;

public class UserAttributes {

    private float shortSilentPauseTime;
    private float longSilentPauseTime;
    private float shortFilledPauseTime;
    private float longFilledPauseTime;
    private int shortSilentPauseCount;
    private int longSilentPauseCount;
    private int shortFilledPauseCount;
    private int longFilledPauseCount;
    private float totalTimeWithoutPauses;
    private float totalTimeWithPauses;
    private int wordCountCandidateText;
    private int syllableCount;
    private float exactMatchCount;
    private float insertionCount;
    private float accuracyScore;
    private float avgConfidence;
    private double rmsConfidence;
    private float numberOfWordsPerLongSilentPause;
    private float numberOfWordsPerLongFilledPause;
    private float numberOfWordsPerShortSilentPause;
    private float numberOfWordsPerShortFilledPause;
    private float longSilentPauseDeduction;
    private float longFilledPauseDeduction;
    private float shortSilentPauseDeduction;
    private float shortFilledPauseDeduction;
    private float pausesScore;
    private float fluencyScore;
    private float rateOfSpeech;
    private float articulationRate;
    private float avgSyllableDuration;
    private float wordsPerSecond;
    private float wordsPerSecondScore;
    private float articulationRateScore;
    private float intonationScore;
    private float score;
    private int proficiencyLevel;

    //Evaluate user attributes on the basics of normal and transformed
    public void Evaluate(HashMap<Integer, UserStatementClass> userStatementMap, boolean isNormal, boolean isIntonation) {
        for (Map.Entry<Integer, UserStatementClass> entry : userStatementMap.entrySet()) {
            UserStatementClass objUserStatement = entry.getValue();
            UserStatementAttributes objUserStatementAttributes = isNormal ? objUserStatement.getUserStatementAttributesNormal() : objUserStatement.getUserStatementAttributesTransformed();
            //
            shortSilentPauseTime += objUserStatementAttributes.getShortSilentPauseTime();
            shortSilentPauseCount += objUserStatementAttributes.getShortSilentPauseCount();
            shortFilledPauseTime += objUserStatementAttributes.getShortFilledPauseTime();
            shortFilledPauseCount += objUserStatementAttributes.getShortFilledPauseCount();
            longSilentPauseTime += objUserStatementAttributes.getLongSilentPauseTime();
            longSilentPauseCount += objUserStatementAttributes.getLongSilentPauseCount();
            longFilledPauseTime += objUserStatementAttributes.getLongFilledPauseTime();
            longFilledPauseCount += objUserStatementAttributes.getLongFilledPauseCount();
            totalTimeWithPauses += objUserStatementAttributes.getTotalTimeWithPauses();
            totalTimeWithoutPauses += objUserStatementAttributes.getTotalTimeWithoutPauses();
            wordCountCandidateText += objUserStatementAttributes.getWordCountCandidateText();
            syllableCount += objUserStatementAttributes.getSyllableCount();
            exactMatchCount += objUserStatementAttributes.getExactMatchCount();
            insertionCount += objUserStatementAttributes.getInsertionCount();
            avgConfidence += objUserStatementAttributes.getSumOfConfidence();
            rmsConfidence += objUserStatementAttributes.getSumOfSquareConfidence();
            if(isIntonation)
                intonationScore += objUserStatementAttributes.getIntonationScore();
        }
        rateOfSpeech = totalTimeWithPauses > 0 ? syllableCount * 100.f / totalTimeWithPauses : 0;
        avgSyllableDuration = syllableCount > 0 ? totalTimeWithoutPauses / syllableCount : 0;

        numberOfWordsPerShortSilentPause = shortSilentPauseCount > 0 ? ((float) wordCountCandidateText) / (shortSilentPauseCount) : wordCountCandidateText;
        numberOfWordsPerShortFilledPause = shortFilledPauseCount > 0 ? ((float) wordCountCandidateText) / (shortFilledPauseCount) : wordCountCandidateText;
        numberOfWordsPerLongSilentPause = longSilentPauseCount > 0 ? ((float) wordCountCandidateText) / (longSilentPauseCount) : wordCountCandidateText;
        numberOfWordsPerLongFilledPause = longFilledPauseCount > 0 ? ((float) wordCountCandidateText) / (longFilledPauseCount) : wordCountCandidateText;

        wordsPerSecond = totalTimeWithPauses > 0 ? wordCountCandidateText * ConstantsClass.MILLISECOND_MULTIPLIER / totalTimeWithPauses : 0;
        articulationRate = totalTimeWithoutPauses > 0 ? syllableCount * ConstantsClass.MILLISECOND_MULTIPLIER / totalTimeWithoutPauses : 0;
        intonationScore = isIntonation && !userStatementMap.isEmpty() ? intonationScore / userStatementMap.size() : 0;
    }

    public boolean setScore(int wordCountOriginalText, boolean isIntotnation) {
        boolean rValue = wordCountOriginalText > 0;
        if (rValue) {
            setPausesScore();
            setWordsPerSecondScore();
            setArticulationRateScore();
            fluencyScore = ((ConstantsClass.WEIGHT_PAUSE_SCORE * pausesScore) + (ConstantsClass.WEIGHT_WORDS_PER_SECOND_SCORE * wordsPerSecondScore) + (ConstantsClass.WEIGHT_ARTICULATION_RATE_SCORE * articulationRateScore)) / 100;
            accuracyScore = Math.max(0, Math.min(100, 100.f * (exactMatchCount * ConstantsClass.FLOAT_MULTIPLIER - insertionCount) / wordCountOriginalText));
            avgConfidence = avgConfidence * ConstantsClass.FLOAT_MULTIPLIER / wordCountOriginalText;
            rmsConfidence /= wordCountOriginalText * ConstantsClass.FLOAT_MULTIPLIER;
            rmsConfidence = Math.sqrt(rmsConfidence);
            setOverAllScore(isIntotnation);
            //Set level
            setProficiencyLevel();
        }
        return rValue;
    }

    private void setWordsPerSecondScore() {
        if (wordsPerSecond >= ConstantsClass.WORDS_PER_SECOND_VALID_LOWER_LIMIT && wordsPerSecond <= ConstantsClass.WORDS_PER_SECOND_VALID_UPPER_LIMIT) {
            wordsPerSecondScore = 100;
        } else if (wordsPerSecond <= ConstantsClass.WORDS_PER_SECOND_INVALID_LOWER_LIMIT || wordsPerSecond >= ConstantsClass.WORDS_PER_SECOND_INVALID_UPPER_LIMIT) {
            wordsPerSecondScore = 0;
        } else if (wordsPerSecond < ConstantsClass.WORDS_PER_SECOND_VALID_LOWER_LIMIT) {
            wordsPerSecondScore = (float) (ConstantsClass.WORDS_PER_SECOND_LESS_THAN_VALID_LOWER_LIMIT_EQUATION_COEFFICIENT_X_SQUARE * Math.pow(wordsPerSecond, 2) + ConstantsClass.WORDS_PER_SECOND_LESS_THAN_VALID_LOWER_LIMIT_EQUATION_COEFFICIENT_X * wordsPerSecond + 1.f * ConstantsClass.WORDS_PER_SECOND_LESS_THAN_VALID_LOWER_LIMIT_EQUATION_CONSTANT);
        } else {
            wordsPerSecondScore = (float) (ConstantsClass.WORDS_PER_SECOND_GREATER_THAN_VALID_UPPER_LIMIT_EQUATION_COEFFICIENT_X_SQUARE * Math.pow(wordsPerSecond, 2) + ConstantsClass.WORDS_PER_SECOND_GREATER_THAN_VALID_UPPER_LIMIT_EQUATION_COEFFICIENT_X * wordsPerSecond + 1.f * ConstantsClass.WORDS_PER_SECOND_GREATER_THAN_VALID_UPPER_LIMIT_EQUATION_CONSTANT);
        }
        wordsPerSecondScore = Math.min(100, Math.max(0, wordsPerSecondScore));
    }

    private void setArticulationRateScore() {
        if (articulationRate >= ConstantsClass.ARTICULATION_RATE_VALID_LOWER_LIMIT && articulationRate <= ConstantsClass.ARTICULATION_RATE_VALID_UPPER_LIMIT) { //Note: This range of Indian Speaker, Navite speaker avg articulation rate is 5.5 
            articulationRateScore = 100;
        } else if (articulationRate <= ConstantsClass.ARTICULATION_RATE_INVALID_LOWER_LIMIT || articulationRate >= ConstantsClass.ARTICULATION_RATE_INVALID_UPPER_LIMIT) {
            articulationRateScore = 0;
        } else if (articulationRate < ConstantsClass.ARTICULATION_RATE_VALID_LOWER_LIMIT) {
            articulationRateScore = (float) (ConstantsClass.ARTICULATION_RATE_LESS_THAN_VALID_LOWER_LIMIT_EQUATION_COEFFICIENT_X_SQUARE * Math.pow(articulationRate, 2) + ConstantsClass.ARTICULATION_RATE_LESS_THAN_VALID_LOWER_LIMIT_EQUATION_COEFFICIENT_X * articulationRate + 1.f * ConstantsClass.ARTICULATION_RATE_LESS_THAN_VALID_LOWER_LIMIT_EQUATION_CONSTANT);
        } else {
            articulationRateScore = (float) (ConstantsClass.ARTICULATION_RATE_GREATER_THAN_VALID_UPPER_LIMIT_EQUATION_COEFFICIENT_X_SQUARE * Math.pow(articulationRate, 2) + ConstantsClass.ARTICULATION_RATE_GREATER_THAN_VALID_UPPER_LIMIT_EQUATION_COEFFICIENT_X * articulationRate + 1.f * ConstantsClass.ARTICULATION_RATE_GREATER_THAN_VALID_UPPER_LIMIT_EQUATION_CONSTANT);
        }
        articulationRateScore = Math.min(100, Math.max(0, articulationRateScore));
    }

    private void setPausesScore() {
        float pausesScoreDeduction;

        //LongSilentPause:
        if (numberOfWordsPerLongSilentPause < ConstantsClass.NUMBER_OF_WORDS_PER_LONG_PAUSE_VALID_UPPER_LIMIT) {
            longSilentPauseDeduction = ConstantsClass.DAMPING_FACTOR_LONG_PAUSE * (float) (100 - Math.min(100, Math.max(0, (ConstantsClass.LONG_PAUSE_EQUATION_COEFFICIENT_X_SQUARE * Math.pow(numberOfWordsPerLongSilentPause, 2) + ConstantsClass.LONG_PAUSE_EQUATION_COEFFICIENT_X * numberOfWordsPerLongSilentPause + 1.f * ConstantsClass.LONG_PAUSE_EQUATION_CONSTANT))));
        }
        //LongFilledPause:
        if (numberOfWordsPerLongFilledPause < ConstantsClass.NUMBER_OF_WORDS_PER_LONG_PAUSE_VALID_UPPER_LIMIT) {
            longFilledPauseDeduction = ConstantsClass.DAMPING_FACTOR_LONG_PAUSE * (float) (100 - Math.min(100, Math.max(0, (ConstantsClass.LONG_PAUSE_EQUATION_COEFFICIENT_X_SQUARE * Math.pow(numberOfWordsPerLongFilledPause, 2) + ConstantsClass.LONG_PAUSE_EQUATION_COEFFICIENT_X * numberOfWordsPerLongFilledPause + 1.f * ConstantsClass.LONG_PAUSE_EQUATION_CONSTANT))));
        }
        //ShortSilentPause:
        if (numberOfWordsPerShortSilentPause < ConstantsClass.NUMBER_OF_WORDS_PER_SHORT_SILENT_PAUSE_VALID_UPPER_LIMIT) {
            shortSilentPauseDeduction = ConstantsClass.DAMPING_FACTOR_SHORT_SILENT_PAUSE * (float) (100 - Math.min(100, Math.max(0, (ConstantsClass.SHORT_SILENT_PAUSE_EQUATION_COEFFICIENT_X_SQUARE * Math.pow(numberOfWordsPerShortSilentPause, 2) + ConstantsClass.SHORT_SILENT_PAUSE_EQUATION_COEFFICIENT_X * numberOfWordsPerShortSilentPause + 1.f * ConstantsClass.SHORT_SILENT_PAUSE_EQUATION_CONSTANT))));
        }
        //ShortFilledPause:
        if (numberOfWordsPerShortFilledPause < ConstantsClass.NUMBER_OF_WORDS_PER_SHORT_FILLED_PAUSE_VALID_UPPER_LIMIT) {
            shortFilledPauseDeduction = ConstantsClass.DAMPING_FACTOR_SHORT_FILLED_PAUSE * (float) (100 - Math.min(100, Math.max(0, (ConstantsClass.SHORT_FILLED_PAUSE_EQUATION_COEFFICIENT_X_SQUARE * Math.pow(numberOfWordsPerShortFilledPause, 2) + ConstantsClass.SHORT_FILLED_PAUSE_EQUATION_COEFFICIENT_X * numberOfWordsPerShortFilledPause + 1.f * ConstantsClass.SHORT_FILLED_PAUSE_EQUATION_CONSTANT))));
        }

        // INFLATIO should be multiplied with pausesScoreDeduction because if multiplied with individual deduction then its weight will cap its value 
        pausesScoreDeduction = (float) (ConstantsClass.WEIGHT_LONG_SILENT_PAUSE * longSilentPauseDeduction + ConstantsClass.WEIGHT_LONG_FILLED_PAUSE * longFilledPauseDeduction + ConstantsClass.WEIGHT_SHORT_SILENT_PAUSE * shortSilentPauseDeduction + ConstantsClass.WEIGHT_SHORT_FILLED_PAUSE * shortFilledPauseDeduction);
        pausesScore = Math.min(100, Math.max(0, 100.f - ConstantsClass.INFLATIO * pausesScoreDeduction));
    }

    private void setOverAllScore(boolean isIntotnation) {
        float evaluationPercent = 1;
        if (accuracyScore < ConstantsClass.THRESHOLD_MIN_ACCURACY_SCORE) {
            evaluationPercent *= ConstantsClass.SCORING_CAP_ACCURACY_SCORE;
        }
        if (fluencyScore < ConstantsClass.THRESHOLD_MIN_FLUENCY_SCORE) {
            evaluationPercent *= ConstantsClass.SCORING_CAP_FLUENCY_SCORE;
        }
        if (avgConfidence < ConstantsClass.THRESHOLD_MIN_AVG_CONFIDENCE_SCORE) {
            evaluationPercent *= ConstantsClass.SCORING_CAP_AVG_CONFIDENCE_SCORE;
        }
        score = isIntotnation ? Math.min(100, Math.max(0, evaluationPercent * (((ConstantsClass.WEIGHT_ACCURACY_SCORE_WITH_INTONATION * accuracyScore) + (ConstantsClass.WEIGHT_FLUENCY_SCORE_WITH_INTONATION * fluencyScore) + (ConstantsClass.WEIGHT_AVG_CONFIDENCE_SCORE_WITH_INTONATION * avgConfidence) + (ConstantsClass.WEIGHT_INTONATION * intonationScore)) / 100))) : Math.min(100, Math.max(0, evaluationPercent * (((ConstantsClass.WEIGHT_ACCURACY_SCORE * accuracyScore) + (ConstantsClass.WEIGHT_FLUENCY_SCORE * fluencyScore) + (ConstantsClass.WEIGHT_AVG_CONFIDENCE_SCORE * avgConfidence)) / 100)));
    }

    private boolean isProficiencyLevelCorrect(int level) {
        switch (level) {
            case 6:
                if (score < ConstantsClass.MIN_OVER_ALL_SCORE_FOR_LEVEL_SIX || accuracyScore < ConstantsClass.MIN_ACCURACY_SCORE_FOR_LEVEL_SIX || fluencyScore < ConstantsClass.MIN_FLUENCY_SCORE_FOR_LEVEL_SIX || avgConfidence < ConstantsClass.MIN_AVG_CONFIDENCY_FOR_LEVEL_SIX || longSilentPauseCount > ConstantsClass.MIN_LONG_PAUSE_COUNT_FOR_LEVEL_SIX || longFilledPauseCount > ConstantsClass.MIN_LONG_PAUSE_COUNT_FOR_LEVEL_SIX) {
                    return false;
                }
                break;
            case 5:
                if (score < ConstantsClass.MIN_OVER_ALL_SCORE_FOR_LEVEL_FIVE || accuracyScore < ConstantsClass.MIN_ACCURACY_SCORE_FOR_LEVEL_FIVE || fluencyScore < ConstantsClass.MIN_FLUENCY_SCORE_FOR_LEVEL_FIVE || avgConfidence < ConstantsClass.MIN_AVG_CONFIDENCY_FOR_LEVEL_FIVE) {
                    return false;
                }
                break;
            case 4:
                if (score < ConstantsClass.MIN_OVER_ALL_SCORE_FOR_LEVEL_FOUR || accuracyScore < ConstantsClass.MIN_ACCURACY_SCORE_FOR_LEVEL_FOUR || fluencyScore < ConstantsClass.MIN_FLUENCY_SCORE_FOR_LEVEL_FOUR || avgConfidence < ConstantsClass.MIN_AVG_CONFIDENCY_FOR_LEVEL_FOUR) {
                    return false;
                }
                break;
            case 3:
                if (score < ConstantsClass.MIN_OVER_ALL_SCORE_FOR_LEVEL_THREE || accuracyScore < ConstantsClass.MIN_ACCURACY_SCORE_FOR_LEVEL_THREE || fluencyScore < ConstantsClass.MIN_FLUENCY_SCORE_FOR_LEVEL_THREE || avgConfidence < ConstantsClass.MIN_AVG_CONFIDENCY_FOR_LEVEL_THREE) {
                    return false;
                }
                break;
            case 2:
                if (score < ConstantsClass.MIN_OVER_ALL_SCORE_FOR_LEVEL_TWO || accuracyScore < ConstantsClass.MIN_ACCURACY_SCORE_FOR_LEVEL_TWO || fluencyScore < ConstantsClass.MIN_FLUENCY_SCORE_FOR_LEVEL_TWO || avgConfidence < ConstantsClass.MIN_AVG_CONFIDENCY_FOR_LEVEL_TWO) {
                    return false;
                }
                break;
        }
        return true;
    }

    private void setProficiencyLevel() {
        int level = ConstantsClass.HIGHEST_PROFICIENCY_LEVEL;
        while (level > 1 && !isProficiencyLevelCorrect(level)) {
            level--;
        }
        proficiencyLevel = level;
    }

    public int getProficiencyLevel() {
        return proficiencyLevel;
    }

    public float getScore() {
        return score;
    }

    //Get user attributes to Result
    public StringBuilder getString() {
        return new StringBuilder().append(totalTimeWithoutPauses).append(",").append(totalTimeWithPauses).append(",").append(wordCountCandidateText).append(",").append(shortSilentPauseTime).append(",").append(shortSilentPauseCount)
                .append(",").append(longSilentPauseTime).append(",").append(longSilentPauseCount).append(",").append(shortFilledPauseTime).append(",").append(shortFilledPauseCount).append(",").append(longFilledPauseTime)
                .append(",").append(longFilledPauseCount).append(",").append(rateOfSpeech).append(",").append(articulationRate).append(",").append(avgSyllableDuration).append(",").append(wordsPerSecond).append(",").append(wordsPerSecondScore)
                .append(",").append(articulationRateScore).append(",").append((100 - longSilentPauseDeduction)).append(",").append((100 - longFilledPauseDeduction)).append(",").append((100 - shortSilentPauseDeduction))
                .append(",").append((100 - shortFilledPauseDeduction)).append(",").append(avgConfidence).append(",").append(rmsConfidence).append(",").append(pausesScore).append(",").append(accuracyScore).append(",").append(fluencyScore).append(",").append(intonationScore);
    }

}
