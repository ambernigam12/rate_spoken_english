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
import com.cocubes.speech.word.WordClass;
import com.cocubes.speech.enums.OperationTypeEnum;
import com.cocubes.speech.enums.PauseTypeEnum;
import com.cocubes.speech.helper.UtilityFunctions;
import com.cocubes.speech.helper.UtilityPathFunction;
import com.cocubes.speech.logging.LoggingFunctions;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.decoder.adaptation.Stats;
import edu.cmu.sphinx.decoder.adaptation.Transform;
import edu.cmu.sphinx.result.WordResult;
import static edu.cmu.sphinx.util.LogMath.getLogMath;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class UserStatementAttributes {

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
    private List<WordClass> listWordDataWithoutPauses;
    private List<WordClass> listWordDataWithPauses;
    private String candidateText;
    private int exactMatchCount;
    private int insertionCount;
    private float accuracy;
    private float avgConfidence;
    private float rmsConfidence;
    private float sumOfConfidence;
    private float sumOfSquareConfidence;
    private float avgScore;
    private float rmsScore;
    private float sumOfScore;
    private float sumOfSquareScore;
    private float[] rmsAmplitudeArray;
    private float intonationScore;

    public boolean setValues(long userId, int statementId, String originalText, int wordCount, String amplitudeFilePath) {
        boolean rValue = true;
        if (setListWordDataWithPauses()) {
            rValue = setPauses(originalText, userId, statementId);
            rValue = rValue && pausesEvaluation(userId, statementId);
            rValue = rValue && setAccuracy(originalText, wordCount, userId, statementId);
            rValue = rValue && setIntonationScore(userId, statementId, amplitudeFilePath);
            if (rValue) {
                setSyllableCount();
                wordCountCandidateText = (candidateText.replaceAll("\\.|\\,", " ")).replaceAll(" +", " ").trim().split(" ").length;
            } else {
                LoggingFunctions.InsertError("User statement values not set!", "UserStatementAttributes", "setValues", userId, statementId);
            }
        }

        return rValue;
    }

    //Predict statement from sound file 
    public boolean predictStatementFromRecognizer(long userId, int statementId, StreamSpeechRecognizer recognizer, String textResultFilePath, String timeStampFilePath, boolean isNormal) throws Exception {
        boolean rValue = false;
        InputStream stream = null;
        try {
            String soundFile = UtilityPathFunction.getSounFilePath(userId, statementId);
            stream = new FileInputStream(soundFile);
            stream.skip(44);
            SpeechResult result;
            String strTimeStamp = "";
            candidateText = "";
            listWordDataWithoutPauses = new ArrayList<>();
            listWordDataWithPauses = new ArrayList<>();
            if (!isNormal) {
                // get status per sentance
                // Live adaptation to speaker with speaker profiles
                // stream = new FileInputStream(soundFile);
                //stream.skip(44);
                // Stats class is used to collect speaker-specific data
                Stats stats = recognizer.createStats(1);
                recognizer.startRecognition(stream);
                while ((result = recognizer.getResult()) != null) {
                    stats.collect(result);
                }
                recognizer.stopRecognition();
                Transform transform = stats.createTransform();
                recognizer.setTransform(transform);
                // Decode again with updated transform
                stream = new FileInputStream(soundFile);
                stream.skip(44);
            }
            recognizer.startRecognition(stream);
            while ((result = recognizer.getResult()) != null) {
                candidateText += result.getHypothesis() + " ";
                for (WordResult r : result.getWords()) {
                    float startTime = r.getTimeFrame().getStart();
                    float endtime = r.getTimeFrame().getEnd();
                    WordClass word = new WordClass(r.getWord().getSpelling(), r, startTime, endtime, endtime - startTime);
                    if (!r.getWord().isFiller()) {
                        listWordDataWithoutPauses.add(word);
                    }
                    listWordDataWithPauses.add(word);
                    strTimeStamp += r.toString() + System.lineSeparator();
                }
            }
            recognizer.stopRecognition();
            rValue = LoggingFunctions.writeIntoFile(candidateText, textResultFilePath, false);
            rValue = rValue && LoggingFunctions.writeIntoFile(strTimeStamp, timeStampFilePath, false);
        } catch (IOException ex) {
            LoggingFunctions.InsertError(ex.getMessage(), "UserStatementAttributes", "predictStatementFromRecognizer", userId, statementId);
            Logger.getLogger(UserStatementAttributes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception ex) {
                    rValue = false;
                    LoggingFunctions.InsertError(ex.getMessage(), "UserStatementAttributes", "predictStatementFromRecognizer", userId, statementId);
                }
            }
        }
        return rValue;
    }

    private void setSyllableCount() {
        String trimmedCandidateText = candidateText.trim();
        if (trimmedCandidateText.length() > 0) {
            boolean isPreviousVowel = false;
            for (String word : trimmedCandidateText.replaceAll(" +", " ").split(" ")) {
                int tempCount = 0;
                for (int i = 0; i < word.length(); i++) {
                    boolean isCurrentVowel = UtilityFunctions.isVowel(word.charAt(i));
                    if (!isPreviousVowel && isCurrentVowel && i != 0) {
                        tempCount++;
                    }
                    isPreviousVowel = isCurrentVowel;
                }
                syllableCount += tempCount > 0 ? tempCount : 1;
            }
        }
    }

    private boolean setRmsAmplitudeArray(long userId, int statementId) {
        // somePathName is a pre-existing string whose value was
        // based on a user selection.
        boolean rValue = false;
        float[] samples = new float[ConstantsClass.BUFFER_BYTE_SIZE / 2];
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(UtilityPathFunction.getSounFilePath(userId, statementId))));
            AudioFormat audioFormat = audioInputStream.getFormat();
            int bytesPerFrame
                    = audioFormat.getFrameSize();
            int arraySize = (int) Math.ceil(audioInputStream.getFrameLength() / audioFormat.getFrameRate()) * ConstantsClass.ARRAY_SIZE_MULTIPLIER + 1;
            rmsAmplitudeArray = new float[arraySize];
            int counter = 0;
            if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
                // some audio formats may have unspecified frame size
                // in that case we may read any amount of bytes
                bytesPerFrame = 1;
            }
            // Set an arbitrary buffer size of 1024 frames.
            byte[] buf = new byte[ConstantsClass.BUFFER_SIZE * bytesPerFrame];
            int b;
            // Try to read numBytes bytes from the file.
            while ((b = audioInputStream.read(buf)) != -1) {
                // Here, do something useful with the audio data that's 
                // now in the audioBytes array...
                for (int i = 0, s = 0; i < b;) {
                    int sample = 0;
                    //Combine two bytes to make a short
                    sample |= buf[i++] & 0xFF; // (reverse these two lines
                    sample |= buf[i++] << 8;   //  if the format is big endian)
                    // normalize to range of +/-1.0f
                    samples[s++] = sample / ConstantsClass.MAX_SHORT_VALUE;
                }
                float rms = 0f;
                for (float sample : samples) {
                    rms += sample * sample;
                }
                rms = (float) Math.sqrt(rms / samples.length);
                if (counter < arraySize) {
                    rmsAmplitudeArray[counter++] = rms;
                }
            }
            rValue = true;
        } catch (UnsupportedAudioFileException | IOException e) {
            LoggingFunctions.InsertError(e.getMessage(), "UserStatementAttributes", "setRmsAmplitudeArray", userId, statementId);
        } finally {
            if (audioInputStream != null) {
                try {
                    audioInputStream.close();
                } catch (Exception ex) {
                    rValue = false;
                    LoggingFunctions.InsertError(ex.getMessage(), "UserStatementAttributes", "setRmsAmplitudeArray", userId, statementId);
                }
            }
        }
        return rValue;
    }

    //Set amplitude of each word spoken in response statemnt and return mean amplitude of the statement 
    private float meanAmplitudeOfStatement(long userId, int statementId, String amplitudeFilePath) {
        float mean = ConstantsClass.CODE_FAIL_VALUE;
        if (setRmsAmplitudeArray(userId, statementId) && listWordDataWithoutPauses != null && rmsAmplitudeArray != null && rmsAmplitudeArray.length > 0) {
            mean = 0;
            if (!listWordDataWithoutPauses.isEmpty()) {
                StringBuilder amplitudeDetails = new StringBuilder();
                amplitudeDetails.append(ConstantsClass.AMPLITUDE_HEADER_STRING).append(System.lineSeparator());
                for (WordClass word : listWordDataWithoutPauses) {
                    int startIndex = (int) (word.getStartTimeOfUtterance() / ConstantsClass.INDEX_MULTIPLIER);
                    int endIndex = (int) Math.ceil(word.getEndTimeOfUtterance() / ConstantsClass.INDEX_MULTIPLIER);
                    float amplitude = 0;
                    for (int i = startIndex; i < endIndex && mean != ConstantsClass.CODE_FAIL_VALUE; i++) {
                        if (i < rmsAmplitudeArray.length) {
                            amplitude += rmsAmplitudeArray[i];
                        } else {
                            mean = ConstantsClass.CODE_FAIL_VALUE;
                            LoggingFunctions.InsertError(String.format("Range of calculated index %s and %s is greater than rmsAmplitudeArray length %s", startIndex, endIndex, rmsAmplitudeArray.length), "UserStatementAttributes", "findAmlitudeOfEachWord", userId, statementId);
                            break;
                        }
                    }
                    if (mean != ConstantsClass.CODE_FAIL_VALUE) {
                        mean += amplitude;
                        word.setAmplitude(amplitude);
                        amplitudeDetails.append(word.getWord()).append(",").append(word.getStartTimeOfUtterance()).append(",").append(word.getEndTimeOfUtterance()).append(",").append(word.getDurationOfUtterance()).append(",").append(startIndex).append(",").append(endIndex).append(",").append(rmsAmplitudeArray.length).append(",").append(amplitude).append(System.lineSeparator());
                    }
                }
                mean = mean != ConstantsClass.CODE_FAIL_VALUE && LoggingFunctions.writeIntoFile(amplitudeDetails.toString(), amplitudeFilePath, false) ? mean / listWordDataWithoutPauses.size() : ConstantsClass.CODE_FAIL_VALUE;
            }
        }
        return mean;
    }

    private boolean setIntonationScore(long userId, int statementId, String amplitudeFilePath) {
        float mean = meanAmplitudeOfStatement(userId, statementId, amplitudeFilePath);
        boolean rValue = false;
        if (mean != ConstantsClass.CODE_FAIL_VALUE) {
            float numberOfWordsPerIntonation = 0;
            int listSize = listWordDataWithoutPauses.size();
            if (listWordDataWithoutPauses != null && listSize > 2) {
                int intonationCount = 0;
                float previousAmplitude = listWordDataWithoutPauses.get(0).getAmplitude();
                float currentAmplitude = listWordDataWithoutPauses.get(1).getAmplitude();
                float nextAmplitude;
                for (int i = 2; i < listSize; i++) {
                    nextAmplitude = listWordDataWithoutPauses.get(i).getAmplitude();
                    if ((previousAmplitude < mean && currentAmplitude > mean && nextAmplitude < mean) || (previousAmplitude > mean && currentAmplitude < mean && nextAmplitude > mean)) {
                        intonationCount++;
                    }
                    previousAmplitude = currentAmplitude;
                    currentAmplitude = nextAmplitude;
                }
                numberOfWordsPerIntonation = intonationCount > 0 ? listSize * ConstantsClass.FLOAT_MULTIPLIER / intonationCount : intonationCount;
            }
            intonationScore = numberOfWordsPerIntonation > 0 ? Math.min(100, Math.max(0, ConstantsClass.AMPLITUDE_EQUATION_COEFFICIENT_X_SQUARE * numberOfWordsPerIntonation * numberOfWordsPerIntonation + ConstantsClass.AMPLITUDE_EQUATION_COEFFICIENT_X * numberOfWordsPerIntonation + ConstantsClass.AMPLITUDE_EQUATION_CONSTANT)) : 0;
            rValue = true;
        }
        return rValue;
    }

    public boolean setListWordDataWithPauses() {
        listWordDataWithPauses = mergePauses(listWordDataWithPauses);
        return listWordDataWithPauses.size() > 0;
    }

    //Merge continuous pauses of each type, i.e, silent and filled
    private List<WordClass> mergePauses(List<WordClass> responseWords) {
        List<WordClass> responseWordsMerged = new ArrayList<>();
        if (responseWords.size() > 0) {
            WordClass objectPreviousWord = responseWords.get(0);
            WordClass objectCurrentWord = null;
            String previousWord;
            String currentWord;
            for (int i = 1; i < responseWords.size(); i++) {
                objectCurrentWord = responseWords.get(i);
                if (objectPreviousWord.getWordResult().isFiller() && objectCurrentWord.getWordResult().isFiller()) {
                    previousWord = objectPreviousWord.getWord();
                    currentWord = objectCurrentWord.getWord();
                    if (previousWord.equals(currentWord) && previousWord.equals(ConstantsClass.SILENT_PAUSE)) {
                        objectPreviousWord.setDurationOfUtterance(objectPreviousWord.getDurationOfUtterance() + objectCurrentWord.getDurationOfUtterance());
                        objectPreviousWord.setEndTimeOfUtterance(objectCurrentWord.getEndTimeOfUtterance());
                    } else if (!previousWord.equals(ConstantsClass.SILENT_PAUSE) && !currentWord.equals(ConstantsClass.SILENT_PAUSE)) {
                        objectPreviousWord.setWord(ConstantsClass.FILLED_PAUSE);
                        objectCurrentWord.setWord(ConstantsClass.FILLED_PAUSE);
                        objectPreviousWord.setDurationOfUtterance(objectPreviousWord.getDurationOfUtterance() + objectCurrentWord.getDurationOfUtterance());
                        objectPreviousWord.setEndTimeOfUtterance(objectCurrentWord.getEndTimeOfUtterance());
                    } else {
                        if (!previousWord.equals(ConstantsClass.SILENT_PAUSE)) {
                            objectPreviousWord.setWord(ConstantsClass.FILLED_PAUSE);
                        }
                        responseWordsMerged.add(objectPreviousWord);
                        objectPreviousWord = objectCurrentWord;
                    }
                } else {
                    responseWordsMerged.add(objectPreviousWord);
                    objectPreviousWord = objectCurrentWord;
                }
            }
            if (objectCurrentWord != null && !(objectPreviousWord.getWord().equals(objectCurrentWord.getWord()))) {
                responseWordsMerged.add(objectCurrentWord);
            } else {
                responseWordsMerged.add(objectPreviousWord);
            }
        }
        return responseWordsMerged;
    }

    private boolean setPauses(String originalText, long userId, int statementId) {
        return ComputeCorrectWordCount(originalText, listWordDataWithPauses, false, userId, statementId);
    }

    private boolean setAccuracy(String originalText, int wordCount, long userId, int statementId) {
        boolean rValue = false;
        if (wordCount > 0) {
            if (ComputeCorrectWordCount(originalText.replaceAll("\\.|\\,", ""), listWordDataWithoutPauses, true, userId, statementId)) {
                accuracy = Math.max(0, Math.min(100, 100.f * (((exactMatchCount * 1.f - insertionCount) / wordCount))));
                rValue = true;
            }
        } else {
            LoggingFunctions.InsertError("Accuracy not set!", "UserStatementAttributes", "setAccuracy", userId, statementId);
        }
        return rValue;
    }

    private boolean isEmptyOrSilence(String questionWord) {
        return questionWord.trim().length() == 0;
    }

    //Levenshtein distance
    private boolean ComputeCorrectWordCount(String question, List<WordClass> responseWords, boolean isAccuracyCalculation, long userId, int statementId) {
        List<WordClass> questionWords = new ArrayList<>();
        for (String questionWordsArray : question.replaceAll(" +", " ").split("\\W")) {
            questionWords.add(new WordClass(questionWordsArray, null, 0, 0, 0));
        }
        int[][] distance = new int[responseWords.size() + 1][questionWords.size() + 1];
        for (int i = 0; i <= responseWords.size(); i++) {
            distance[i][0] = i;
        }
        for (int j = 1; j <= questionWords.size(); j++) {
            distance[0][j] = j;
        }
        for (int i = 1; i <= responseWords.size(); i++) {
            String responseCurrentWord = responseWords.get(i - 1).getWord();
            for (int j = 1; j <= questionWords.size(); j++) {
                String questionCurrentWord = questionWords.get(j - 1).getWord();
                if (isEmptyOrSilence(questionCurrentWord)) {
                    questionCurrentWord = ConstantsClass.SILENT_PAUSE;
                }
                if (responseCurrentWord.trim().equals(ConstantsClass.SILENT_PAUSE) && !questionCurrentWord.equals(ConstantsClass.SILENT_PAUSE)) {
                    distance[i][j] = UtilityFunctions.Minimum(distance[i - 1][j],
                            distance[i][j - 1], distance[i - 1][j - 1]);
                } else {
                    distance[i][j] = UtilityFunctions.Minimum(distance[i - 1][j] + 1,
                            distance[i][j - 1] + 1,
                            distance[i - 1][j - 1] + ((responseCurrentWord.equals(questionCurrentWord) == false) ? 1 : 0));
                }
            }
        }
        return questionWords.size() > 0 ? (isAccuracyCalculation ? SegregateExactMatchCount(distance, questionWords, responseWords, userId, statementId) : validatePauses(distance, questionWords, responseWords)) : true;
    }

    //Levenshtein distance
    private int GetDistance(int i, int j, int[][] editDistance) {
        if (i < 0 || j < 0 || i > editDistance.length || j > editDistance[0].length) {
            return Integer.MAX_VALUE;
        }
        return editDistance[i][j];
    }

    //Levenshtein distance
    private boolean AreWordsMerged(int i, int j, List<WordClass> questionWords, List<WordClass> responseWords) {
        boolean areWordsMerged = false;
        int skippedWordCount = 0;
        if (i >= 0 && j >= 0 && j < questionWords.size() && i < responseWords.size()) {
            String mergedPassageWords = "";
            while (j < questionWords.size() && mergedPassageWords.length() <= responseWords.get(i).getWord().length()) {
                skippedWordCount++;
                mergedPassageWords = mergedPassageWords + questionWords.get(j++);
                if (mergedPassageWords.equals(responseWords.get(i).getWord()) && skippedWordCount > 1) {
                    areWordsMerged = true;
                    break;
                }
            }
        }
        return areWordsMerged;
    }

    //Set exactMatchCount and avgConfidence for each statement
    private boolean SegregateExactMatchCount(int[][] editDistances, List<WordClass> listQuestionWords, List<WordClass> listResponseWords, long userId, int statementId) {

        boolean rValue = true;
        int i = listResponseWords.size(), j = listQuestionWords.size();
        int substitutionCount = 0;

        while (i >= 0 && j >= 0) {
            OperationTypeEnum operation = null;
            boolean isSubmissionWord = true;
            int initialI = i;
            int initialJ = j;
            int initialDistance = editDistances[i][j];
            int upperCellDistance = GetDistance(i - 1, j, editDistances);
            int leftCellDistance = GetDistance(i, j - 1, editDistances);
            int diagonalCellDistance = GetDistance(i - 1, j - 1, editDistances);
            if (i > 0 && j > 0 && (diagonalCellDistance <= upperCellDistance && diagonalCellDistance <= leftCellDistance)) {
                String questionWord = listQuestionWords.get(j - 1).getWord();
                String responseWord = listResponseWords.get(i - 1).getWord();
                if (diagonalCellDistance < initialDistance) {
                    operation = OperationTypeEnum.Substitution;
                    substitutionCount++;
                } else if (responseWord.equals(questionWord) || (responseWord.equals(ConstantsClass.SILENT_PAUSE) && isEmptyOrSilence(questionWord))) {
                    exactMatchCount++;
                    float confidence = Math.round(getLogMath().logToLinear((float) listResponseWords.get(i - 1).getWordResult().getConfidence()) * 10) * 10; // constant
                    sumOfConfidence += confidence;
                    sumOfScore += listResponseWords.get(i - 1).getWordResult().getScore();
                    sumOfSquareConfidence += Math.pow(confidence, 2);
                    sumOfSquareScore += Math.pow(listResponseWords.get(i - 1).getWordResult().getScore(), 2);
                }
                i--;
                j--;
            } else if (j > 0 && leftCellDistance <= upperCellDistance && leftCellDistance <= diagonalCellDistance) {
                operation = OperationTypeEnum.Deletion;
                j--;
                isSubmissionWord = false;
            } else if (i > 0) {
                if (!listResponseWords.get(i - 1).getWordResult().isFiller()) {
                    operation = OperationTypeEnum.Insertion;
                    insertionCount++;
                }
                i--;
            }
            if (i >= 0 && j >= 0 && operation == OperationTypeEnum.Deletion && substitutionCount > 0 && AreWordsMerged(i, j, listQuestionWords, listResponseWords)) {
                substitutionCount--;
            }
            if ((isSubmissionWord && initialI > 0 ? listResponseWords.get(initialI - 1) : initialJ > 0 ? listQuestionWords.get(initialJ - 1) : null) != null) { // check condition
            } else {
                break;
            }
        }
        int wordCount = listQuestionWords.size();
        if (wordCount > 0) {
            avgConfidence = sumOfConfidence / wordCount;
            avgScore = sumOfScore / wordCount;
            rmsConfidence = sumOfSquareConfidence / wordCount;
            rmsConfidence = (float) Math.sqrt(rmsConfidence);
            rmsScore = sumOfSquareScore / wordCount;
            rmsScore = (float) Math.sqrt(rmsScore);

        } else {
            exactMatchCount = ConstantsClass.CODE_FAIL_VALUE;
            rValue = false;
            LoggingFunctions.InsertError(String.format("No words in statement: %s", statementId), "UserStatementAttributes", "SegregateExactMatchCount", userId, statementId);
        }
        return rValue;
    }

    //Set valid pauses
    private boolean validatePauses(int[][] editDistances, List<WordClass> listQuestionWords, List<WordClass> listResponseWords) {
        boolean rValue = true;
        int i = listResponseWords.size(), j = listQuestionWords.size();
        int substitutionCount = 0;
        while (i >= 0 && j >= 0) {
            OperationTypeEnum operation = null;
            boolean isSubmissionWord = true;
            int initialI = i;
            int initialJ = j;
            int initialDistance = editDistances[i][j];
            int upperCellDistance = GetDistance(i - 1, j, editDistances);
            int leftCellDistance = GetDistance(i, j - 1, editDistances);
            int diagonalCellDistance = GetDistance(i - 1, j - 1, editDistances);
            if (i > 0 && j > 0 && (diagonalCellDistance <= upperCellDistance && diagonalCellDistance <= leftCellDistance)) {
                String questionWord = listQuestionWords.get(j - 1).getWord();
                String responseWord = listResponseWords.get(i - 1).getWord();
                if (diagonalCellDistance < initialDistance) {
                    operation = OperationTypeEnum.Substitution;
                    substitutionCount++;
                } else if (responseWord.equals(questionWord) || (responseWord.equals(ConstantsClass.SILENT_PAUSE) && isEmptyOrSilence(questionWord))) {
                    if (responseWord.equals(ConstantsClass.SILENT_PAUSE)) {
                        if (listResponseWords.get(i - 1).getDurationOfUtterance() > ConstantsClass.VALID_SILENT_PAUSE_DURATION) {
                            listResponseWords.get(i - 1).setDurationOfUtterance(listResponseWords.get(i - 1).getDurationOfUtterance() - ConstantsClass.VALID_SILENT_PAUSE_DURATION);
                        } else {
                            listResponseWords.get(i - 1).setIsValidPause(true);
                        }
                    }
                }
                i--;
                j--;
            } else if (j > 0 && leftCellDistance <= upperCellDistance && leftCellDistance <= diagonalCellDistance) {
                operation = OperationTypeEnum.Deletion;
                j--;
                isSubmissionWord = false;
            } else if (i > 0) {
                if (!listResponseWords.get(i - 1).getWordResult().isFiller()) {
                    operation = OperationTypeEnum.Insertion;
                }
                i--;
            }
            if (i >= 0 && j >= 0 && operation == OperationTypeEnum.Deletion && substitutionCount > 0 && AreWordsMerged(i, j, listQuestionWords, listResponseWords)) {
                substitutionCount--;
            }
            if ((isSubmissionWord && initialI > 0 ? listResponseWords.get(initialI - 1) : initialJ > 0 ? listQuestionWords.get(initialJ - 1) : null) == null) {
                break;
            }
        }
        return rValue;
    }

    //Set total duration of utterance with pauses and witout pauses, total duration of each type of invalid pauses and count of each type of invalid pauses.
    private boolean pausesEvaluation(long userId, int statementId) {
        boolean rValue = false;
        float tempTotalTimeWithPause = 0;
        float tempShortSilentPausesTime = 0;
        float tempShortFilledPausesTime = 0;
        float tempLongSilentPausesTime = 0;
        float tempLongFilledPausesTime = 0;
        boolean firstWordFound = false;
        List<WordClass> listWord = listWordDataWithPauses;
        try {
            for (WordClass result : listWord) {
                if (!result.getWordResult().isFiller()) {
                    if (!firstWordFound) {
                        tempShortSilentPausesTime = tempShortSilentPausesTime > ConstantsClass.VALID_INITIAL_PAUSE ? tempShortSilentPausesTime - ConstantsClass.VALID_INITIAL_PAUSE : 0;
                        tempShortFilledPausesTime = tempShortFilledPausesTime > ConstantsClass.VALID_INITIAL_PAUSE ? tempShortFilledPausesTime - ConstantsClass.VALID_INITIAL_PAUSE : 0;
                        tempLongSilentPausesTime = tempLongSilentPausesTime > ConstantsClass.VALID_INITIAL_PAUSE ? tempLongSilentPausesTime - ConstantsClass.VALID_INITIAL_PAUSE : 0;
                        tempLongFilledPausesTime = tempLongFilledPausesTime > ConstantsClass.VALID_INITIAL_PAUSE ? tempLongFilledPausesTime - ConstantsClass.VALID_INITIAL_PAUSE : 0;
                        tempTotalTimeWithPause = tempTotalTimeWithPause > ConstantsClass.VALID_INITIAL_PAUSE ? tempTotalTimeWithPause - ConstantsClass.VALID_INITIAL_PAUSE : 0;
                    }
                    firstWordFound = true;
                    totalTimeWithoutPauses += result.getDurationOfUtterance();
                    shortSilentPauseTime += tempShortSilentPausesTime;
                    shortFilledPauseTime += tempShortFilledPausesTime;
                    longSilentPauseTime += tempLongSilentPausesTime;
                    longFilledPauseTime += tempLongFilledPausesTime;
                    totalTimeWithPauses += tempTotalTimeWithPause;
                    tempShortSilentPausesTime = 0;
                    tempShortFilledPausesTime = 0;
                    tempLongSilentPausesTime = 0;
                    tempLongFilledPausesTime = 0;
                    tempTotalTimeWithPause = 0;
                } else {
                    String word = result.getWord();
                    float durationOfUtterance = result.getDurationOfUtterance();
                    tempTotalTimeWithPause += durationOfUtterance;
                    PauseTypeEnum type = null;
                    if (!result.getIsValidPause()) {
                        if (word.equals(ConstantsClass.SILENT_PAUSE)) {
                            type = durationOfUtterance >= ConstantsClass.QUANTUM_LONG_PAUSE ? PauseTypeEnum.LongSilentPause : PauseTypeEnum.ShortSilentPause;
                        } else if (word.equals(ConstantsClass.FILLED_PAUSE)) {
                            type = durationOfUtterance >= ConstantsClass.QUANTUM_LONG_PAUSE ? PauseTypeEnum.LongFilledPause : PauseTypeEnum.ShortFilledPause;
                        }
                    }
                    if (type != null) {
                        switch (type) {
                            case ShortSilentPause:
                                tempShortSilentPausesTime += durationOfUtterance;
                                break;
                            case LongSilentPause:
                                tempLongSilentPausesTime += durationOfUtterance;
                                break;
                            case ShortFilledPause:
                                tempShortFilledPausesTime += durationOfUtterance;
                                break;
                            case LongFilledPause:
                                tempLongFilledPausesTime += durationOfUtterance;
                                break;
                        }
                    }
                }
            }
            if (!firstWordFound) {
                float quantumTimeForJustSilence = (ConstantsClass.QUANTUM_LONG_PAUSE * ConstantsClass.QUANTUM_EQUIVALENT_TO_NO_RESPONSE);
                longFilledPauseTime += quantumTimeForJustSilence;
                totalTimeWithPauses += quantumTimeForJustSilence;
            }
            totalTimeWithPauses += totalTimeWithoutPauses;
            shortSilentPauseCount = shortSilentPauseTime >= ConstantsClass.QUANTUM_SHORT_SILENT_PAUSE ? Math.round(shortSilentPauseTime / ConstantsClass.QUANTUM_SHORT_SILENT_PAUSE) : 0;
            longSilentPauseCount = longSilentPauseTime >= ConstantsClass.QUANTUM_LONG_PAUSE ? Math.round(longSilentPauseTime / ConstantsClass.QUANTUM_LONG_PAUSE) : 0;
            shortFilledPauseCount = shortFilledPauseTime >= ConstantsClass.QUANTUM_SHORT_FILLED_PAUSE ? Math.round(shortFilledPauseTime / ConstantsClass.QUANTUM_SHORT_FILLED_PAUSE) : 0;
            longFilledPauseCount = longFilledPauseTime >= ConstantsClass.QUANTUM_LONG_PAUSE ? Math.round(longFilledPauseTime / ConstantsClass.QUANTUM_LONG_PAUSE) : 0;
            rValue = true;
        } catch (Exception ex) {
            LoggingFunctions.InsertError(ex.getMessage(), "UserStatementAttributes", "pausesEvaluation", userId, statementId);
        }
        return rValue;
    }

    public float getShortSilentPauseTime() {
        return shortSilentPauseTime;
    }

    public float getLongSilentPauseTime() {
        return longSilentPauseTime;
    }

    public float getShortFilledPauseTime() {
        return shortFilledPauseTime;
    }

    public float getLongFilledPauseTime() {
        return longFilledPauseTime;
    }

    public float getShortSilentPauseCount() {
        return shortSilentPauseCount;
    }

    public float getLongSilentPauseCount() {
        return longSilentPauseCount;
    }

    public float getShortFilledPauseCount() {
        return shortFilledPauseCount;
    }

    public float getLongFilledPauseCount() {
        return longFilledPauseCount;
    }

    public float getTotalTimeWithoutPauses() {
        return totalTimeWithoutPauses;
    }

    public float getTotalTimeWithPauses() {
        return totalTimeWithPauses;
    }

    public float getWordCountCandidateText() {
        return wordCountCandidateText;
    }

    public float getSyllableCount() {
        return syllableCount;
    }

    public int getExactMatchCount() {
        return exactMatchCount;
    }

    public int getInsertionCount() {
        return insertionCount;
    }

    public float getAvgConfidence() {
        return avgConfidence;
    }

    public float getRMSConfidence() {
        return rmsConfidence;
    }

    public float getAvgScore() {
        return avgScore;
    }

    public float getRMSScore() {
        return rmsScore;
    }

    public float getSumOfConfidence() {
        return sumOfConfidence;
    }

    public float getSumOfSquareConfidence() {
        return sumOfSquareConfidence;
    }

    //Get String of per statement attributes
    public StringBuilder getString() {
        return new StringBuilder().append(accuracy).append(",").append(totalTimeWithoutPauses).append(",").append(totalTimeWithPauses).append(",").append(wordCountCandidateText).append(",").append(shortSilentPauseTime).append(",").append(shortSilentPauseCount)
                .append(",").append(longSilentPauseTime).append(",").append(longSilentPauseCount).append(",").append(shortFilledPauseTime).append(",").append(shortFilledPauseCount).append(",").append(longFilledPauseTime)
                .append(",").append(longFilledPauseCount).append(",").append(avgConfidence).append(",").append(avgScore).append(",").append(rmsConfidence).append(",").append(rmsScore).append(",").append(intonationScore);

    }

    public float getIntonationScore() {
        return intonationScore;
    }

}
