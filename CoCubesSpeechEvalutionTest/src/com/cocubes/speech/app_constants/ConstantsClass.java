package com.cocubes.speech.app_constants;

public class ConstantsClass {
    
    public static final String PER_USER_RESLUT_HEADER_STRING = "Uersname,WordCountOriginalText,AvgWordLength,DifficultWordCount,TotalTimeWithoutPausesNormal,TotalTimeWithPausesNormal,WordCountCandidateTextNormal,ShortSilentPauseTimeNormal,ShortSilentPauseCountNormal,LongSilentPauseTimeNormal,LongSilentPauseCountNormal,ShortFilledPauseTimeNormal,ShortFilledPauseCountNormal,LongFilledPauseTimeNormal,LongFilledPauseCountNormal,RateOfSpeechNormal(#Syllables/TotalTimeWithPauses),ArticulationRateNormal(#Syllables/TotalTimeWithoutPauses),AvgSyllableDurationNormal,WordPerSecondNormal,WordsPerSecondScoreNormal,AritculationRateScoreNormal,LongSilentPausesScoreNormal,LongFilledPausesScoreNormal,ShortSilentPausesScoreNormal,ShortFilledPausesScoreNormal,AverageConfidenceNormal,RMSOfConfidenceNormal,PauseScoreNormal,AccuracyScoreNormal,FluencyScoreNormal,IntonationScoreNormal,TotalTimeWithoutPausesTransformed,TotalTimeWithPausesTransformed,WordCountCandidateTextTransfromed,ShortSilentPauseTimeTransformed,ShortSilentPauseCountTransformed,LongSilentPauseTimeTransformed,LongSilentPauseCountTransformed,ShortFilledPauseTimeTransformed,ShortFilledPauseCountTransformed,LongFilledPauseTimeTransformed,LongFilledPauseCountTransformed,RateOfSpeechTransformed(#Syllables/TotalTimeWithPauses),ArticulationRateTransformed(#Syllables/TotalTimeWithoutPauses),AvgSyllableDurationTransformed,WordPerSecondTransformed,WordsPerSecondScoreTransformed,AritculationRateScoreTransformed,LongSilentPausesScoreTransformed,LongFilledPausesScoreTransformed,ShortSilentPausesScoreTransformed,ShortFilledPausesScoreTransformed,AverageConfidenceTransformed,RMSOfConfidenceTransformed,PauseScoreTransformed,AccuracyScoreTransformed,FluencyScoreTransformed,IntonationScoreTransformed,ScoreNormal,ScoreTransformed,ProficiencyLevel";
    public static final  String PER_STATEMENT_RESULT_HEADER_STRING = "User Name,File Name,WordCountOriginalText,AvgWordLength,DifficultWordCount,NormalAccuarcyPercent,TotalTimeWithoutPausesNormal,TotalTimeWithPausesNormal,WordCountCandidateTextNormal,ShortSilentPauseTimeNormal,ShortSilentPauseCountNormal,LongSilentPauseTimeNormal,LongSilentPauseCountNormal,ShortFilledPauseTimeNormal,ShortFilledPauseCountNormal,LongFilledPauseTimeNormal,LongFilledPauseCountNormal,AverageConfidenceNormal,AverageScoreNormal,RMSConfidenceNormal,RMSScoreNormal,IntonationScoreNormal,TransformAccuarcyPercent,TotalTimeWithoutPausesTransformed,TotalTimeWithPausesTransformed,WordCountCandidateTextTransformeded,ShortSilentPauseTimeTransformed,ShortSilentPauseCountTransformed,LongSilentPauseTimeTransformed,LongSilentPauseCountTransformed,ShortFilledPauseTimeTransformed,ShortFilledPauseCountTransformed,LongFilledPauseTimeTransformed,LongFilledPauseCountTransformed,AverageConfidenceTransformeded,AverageScoreTransformeded,RMSConfidenceTransformeded,RMSScoreTransformeded,IntonationScoreTransformed";
    public static final  String AMPLITUDE_HEADER_STRING ="Word,StartTime,EndTime,Duration,StartIndex,EndIndex,ArrayLength,Amplitude";   
    public static final int CODE_FAIL_VALUE = -1;
    public static final int NO_USERID_OR_STATEMENTID = -1;

    // <editor-fold desc="Paths">
    public static final String SOUND_FILE_EXTENSION = ".wav";
    public static final String TEXT_FILE_EXTENSION = ".txt";
    public static final String USER_PROCESSED = "_processed";

    // </editor-fold>

    
    public static final int BUFFER_BYTE_SIZE = 2048;
    public static final int BUFFER_SIZE = 1024;
    public static final int ARRAY_SIZE_MULTIPLIER = 16;//
    public static final int INDEX_MULTIPLIER = 62;//
    public static final float MAX_SHORT_VALUE = 32768f;
    
    
    // <editor-fold desc="Thread">
    private static final int PROCESSOR_COUNT = Runtime.getRuntime().availableProcessors();
    public static final int NUMBER_OF_THREADS = PROCESSOR_COUNT - 1;
    // </editor-fold>

    // <editor-fold desc="Time">
    public static final float PER_WORD_ALLOWED_SECONDS = 0.4f;
    public static final int BUFFER_TIME_IN_MILLISECONDS = 2000;
    public static final int TIMER_INITIALISER = 1000;
    // </editor-fold>

    // <editor-fold desc="Multiplier">
    public static final float MILLISECOND_MULTIPLIER = 1000;
    public static final float FLOAT_MULTIPLIER = 1.f;
    // </editor-fold>

    // <editor-fold desc="Statement Constants">
    public static final int[][] SET_OF_STATEMENTS = {{76, 41, 82, 349, 96, 178, 195, 219, 306, 429, 241, 237, 321, 89, 147, 284, 149, 150, 276, 298},
    {58, 101, 14, 50, 259, 392, 257, 321, 140, 268, 363, 237, 297, 93, 143, 276, 179, 253, 383, 150},
    {414, 218, 184, 410, 182, 439, 167, 268, 267, 285, 363, 220, 225, 262, 159, 226, 287, 133, 232, 141},
    {448, 406, 430, 7, 291, 128, 321, 290, 301, 316, 306, 216, 305, 179, 274, 153, 147, 277, 315, 312},
    {286, 448, 121, 132, 129, 254, 197, 69, 151, 201, 212, 316, 209, 300, 193, 287, 277, 89, 226, 189},
    {41, 109, 447, 311, 238, 63, 212, 197, 209, 336, 268, 202, 319, 159, 187, 153, 320, 231, 304, 403},
    {109, 125, 139, 183, 49, 154, 321, 201, 431, 241, 217, 237, 209, 179, 269, 320, 381, 89, 222, 141},
    {163, 178, 122, 295, 414, 67, 195, 220, 217, 167, 92, 151, 219, 136, 133, 302, 307, 251, 235, 284},
    {218, 264, 376, 395, 230, 57, 216, 228, 325, 201, 191, 221, 306, 116, 143, 153, 287, 147, 383, 222},
    {68, 317, 254, 112, 51, 33, 301, 319, 69, 202, 237, 283, 299, 224, 251, 274, 205, 232, 171, 266},
    {455, 426, 447, 294, 60, 82, 305, 299, 336, 220, 237, 301, 228, 269, 110, 226, 266, 244, 315, 147},
    {81, 56, 106, 27, 161, 100, 283, 299, 219, 237, 285, 191, 69, 213, 87, 339, 160, 196, 170, 159},
    {392, 154, 418, 13, 371, 435, 225, 267, 285, 167, 169, 257, 321, 223, 143, 79, 234, 304, 315, 302},
    {59, 29, 352, 95, 423, 51, 191, 225, 92, 197, 257, 305, 297, 44, 300, 446, 205, 116, 215, 249},
    {455, 375, 4, 73, 271, 165, 169, 217, 301, 195, 306, 290, 297, 133, 89, 243, 266, 298, 288, 173},
    {188, 361, 242, 47, 430, 369, 241, 268, 290, 431, 299, 429, 169, 117, 446, 262, 432, 150, 265, 137},
    {51, 148, 194, 373, 408, 450, 283, 285, 202, 219, 169, 290, 140, 65, 287, 253, 403, 339, 171, 116},
    {203, 303, 26, 413, 120, 438, 202, 325, 321, 431, 69, 228, 285, 231, 39, 193, 390, 312, 149, 234},
    {218, 85, 385, 345, 176, 245, 167, 217, 325, 316, 299, 216, 268, 204, 304, 243, 215, 273, 262, 240},
    {295, 14, 73, 437, 32, 343, 319, 290, 212, 219, 283, 336, 429, 224, 240, 173, 179, 449, 153, 274}
    };

    
    public static final int NUMBER_OF_SETS = 20;
    public static final int NUMBER_OF_STATEMENTS = 20;
    // </editor-fold>

    // <editor-fold desc="Pauses Related Constant">
    public static final float VALID_INITIAL_PAUSE = 2000;
    public static final float QUANTUM_LONG_PAUSE = 500;
    public static final float QUANTUM_SHORT_SILENT_PAUSE = 200;
    public static final float QUANTUM_SHORT_FILLED_PAUSE = 100;
    public static final String SILENT_PAUSE = "<sil>";
    public static final String FILLED_PAUSE = "FilledPauses";
    public static final float QUANTUM_EQUIVALENT_TO_NO_RESPONSE = 2;
    public static final float VALID_SILENT_PAUSE_DURATION = 1000;
    public static final float INFLATIO = 4.f;
    public static final float LONG_PAUSE_EQUATION_COEFFICIENT_X_SQUARE = -0.003f;
    public static final float LONG_PAUSE_EQUATION_COEFFICIENT_X = 1.245f;
    public static final float LONG_PAUSE_EQUATION_CONSTANT = -10.49f;
    public static final float SHORT_SILENT_PAUSE_EQUATION_COEFFICIENT_X_SQUARE = -0.053f;
    public static final float SHORT_SILENT_PAUSE_EQUATION_COEFFICIENT_X = 4.927f;
    public static final float SHORT_SILENT_PAUSE_EQUATION_CONSTANT = -12.29f;
    public static final float SHORT_FILLED_PAUSE_EQUATION_COEFFICIENT_X_SQUARE = -0.013f;
    public static final float SHORT_FILLED_PAUSE_EQUATION_COEFFICIENT_X = 2.688f;
    public static final float SHORT_FILLED_PAUSE_EQUATION_CONSTANT = -5.542f;
    public static final int NUMBER_OF_WORDS_PER_LONG_PAUSE_VALID_UPPER_LIMIT = 150;
    public static final int NUMBER_OF_WORDS_PER_SHORT_SILENT_PAUSE_VALID_UPPER_LIMIT = 50;
    public static final int NUMBER_OF_WORDS_PER_SHORT_FILLED_PAUSE_VALID_UPPER_LIMIT = 100;
    public static final float WEIGHT_LONG_SILENT_PAUSE = 0.28f;
    public static final float WEIGHT_LONG_FILLED_PAUSE = 0.32f;
    public static final float WEIGHT_SHORT_SILENT_PAUSE = 0.15f;
    public static final float WEIGHT_SHORT_FILLED_PAUSE = 0.25f;
    public static final float DAMPING_FACTOR_LONG_PAUSE = 1.f;
    public static final float DAMPING_FACTOR_SHORT_SILENT_PAUSE = 1.f;
    public static final float DAMPING_FACTOR_SHORT_FILLED_PAUSE = 1.f;
    // </editor-fold>

    // <editor-fold desc="Words pre second Related Constants">
    public static final float WORDS_PER_SECOND_VALID_LOWER_LIMIT = 2.5f;
    public static final float WORDS_PER_SECOND_VALID_UPPER_LIMIT = 3.25f;
    public static final float WORDS_PER_SECOND_INVALID_LOWER_LIMIT = 0.75f;
    public static final float WORDS_PER_SECOND_INVALID_UPPER_LIMIT = 5.25f;
    public static final float WORDS_PER_SECOND_LESS_THAN_VALID_LOWER_LIMIT_EQUATION_COEFFICIENT_X_SQUARE = -26.19f;
    public static final float WORDS_PER_SECOND_LESS_THAN_VALID_LOWER_LIMIT_EQUATION_COEFFICIENT_X = 144.4f;
    public static final float WORDS_PER_SECOND_LESS_THAN_VALID_LOWER_LIMIT_EQUATION_CONSTANT = -96.28f;
    public static final float WORDS_PER_SECOND_GREATER_THAN_VALID_UPPER_LIMIT_EQUATION_COEFFICIENT_X_SQUARE = -19.02f;
    public static final float WORDS_PER_SECOND_GREATER_THAN_VALID_UPPER_LIMIT_EQUATION_COEFFICIENT_X = 132.9f;
    public static final float WORDS_PER_SECOND_GREATER_THAN_VALID_UPPER_LIMIT_EQUATION_CONSTANT = -133.9f;
    // </editor-fold>

    // <editor-fold desc="ARTICULATION RATE Related Constants">
    public static final float ARTICULATION_RATE_VALID_LOWER_LIMIT = 4.5f;
    public static final float ARTICULATION_RATE_VALID_UPPER_LIMIT = 5.5f;
    public static final float ARTICULATION_RATE_INVALID_LOWER_LIMIT = 0.75f;
    public static final float ARTICULATION_RATE_INVALID_UPPER_LIMIT = 8.25f;
    public static final float ARTICULATION_RATE_LESS_THAN_VALID_LOWER_LIMIT_EQUATION_COEFFICIENT_X_SQUARE = 0.266f;
    public static final float ARTICULATION_RATE_LESS_THAN_VALID_LOWER_LIMIT_EQUATION_COEFFICIENT_X = 27.93f;
    public static final float ARTICULATION_RATE_LESS_THAN_VALID_LOWER_LIMIT_EQUATION_CONSTANT = -24.30f;
    public static final float ARTICULATION_RATE_GREATER_THAN_VALID_UPPER_LIMIT_EQUATION_COEFFICIENT_X_SQUARE = -2.701f;
    public static final float ARTICULATION_RATE_GREATER_THAN_VALID_UPPER_LIMIT_EQUATION_COEFFICIENT_X = -4.059f;
    public static final float ARTICULATION_RATE_GREATER_THAN_VALID_UPPER_LIMIT_EQUATION_CONSTANT = 210.2f;
    // </editor-fold>

    // <editor-fold desc="OverAll Score Related Constants">
    public static final float WEIGHT_WORDS_PER_SECOND_SCORE = 20;
    public static final float WEIGHT_ARTICULATION_RATE_SCORE = 30;
    public static final float WEIGHT_PAUSE_SCORE = 50;
    public static final float WEIGHT_ACCURACY_SCORE = 30.f;
    public static final float WEIGHT_FLUENCY_SCORE = 40.f;
    public static final float WEIGHT_AVG_CONFIDENCE_SCORE = 30.f;
    public static final float SCORING_CAP_ACCURACY_SCORE = 0.2f;
    public static final float SCORING_CAP_FLUENCY_SCORE = 0.3f;
    public static final float SCORING_CAP_AVG_CONFIDENCE_SCORE = 0.2f;
    public static final float THRESHOLD_MIN_ACCURACY_SCORE = 20;
    public static final float THRESHOLD_MIN_FLUENCY_SCORE = 20;
    public static final float THRESHOLD_MIN_AVG_CONFIDENCE_SCORE = 20;
    public static final float WEIGHT_ACCURACY_SCORE_WITH_INTONATION = 27.f;
    public static final float WEIGHT_FLUENCY_SCORE_WITH_INTONATION = 37.f;
    public static final float WEIGHT_AVG_CONFIDENCE_SCORE_WITH_INTONATION = 26.f;
    public static final float WEIGHT_INTONATION = 10.f;
    // </editor-fold>

    // <editor-fold desc="PROFICIENCY Related Constants">
    public static final int HIGHEST_PROFICIENCY_LEVEL = 6;
    public static final float MIN_OVER_ALL_SCORE_FOR_LEVEL_SIX = 90;
    public static final float MIN_ACCURACY_SCORE_FOR_LEVEL_SIX = 85;
    public static final float MIN_FLUENCY_SCORE_FOR_LEVEL_SIX = 95;
    public static final float MIN_AVG_CONFIDENCY_FOR_LEVEL_SIX = 85;
    public static final float MIN_LONG_PAUSE_COUNT_FOR_LEVEL_SIX = 0;
    public static final float MIN_OVER_ALL_SCORE_FOR_LEVEL_FIVE = 85;
    public static final float MIN_ACCURACY_SCORE_FOR_LEVEL_FIVE = 80;
    public static final float MIN_FLUENCY_SCORE_FOR_LEVEL_FIVE = 90;
    public static final float MIN_AVG_CONFIDENCY_FOR_LEVEL_FIVE = 80;
    public static final float MIN_OVER_ALL_SCORE_FOR_LEVEL_FOUR = 75;
    public static final float MIN_ACCURACY_SCORE_FOR_LEVEL_FOUR = 60;
    public static final float MIN_FLUENCY_SCORE_FOR_LEVEL_FOUR = 85;
    public static final float MIN_AVG_CONFIDENCY_FOR_LEVEL_FOUR = 65;
    public static final float MIN_OVER_ALL_SCORE_FOR_LEVEL_THREE = 60;
    public static final float MIN_ACCURACY_SCORE_FOR_LEVEL_THREE = 50;
    public static final float MIN_FLUENCY_SCORE_FOR_LEVEL_THREE = 80;
    public static final float MIN_AVG_CONFIDENCY_FOR_LEVEL_THREE = 55;
    public static final float MIN_OVER_ALL_SCORE_FOR_LEVEL_TWO = 50;
    public static final float MIN_ACCURACY_SCORE_FOR_LEVEL_TWO = 35;
    public static final float MIN_FLUENCY_SCORE_FOR_LEVEL_TWO = 75;
    public static final float MIN_AVG_CONFIDENCY_FOR_LEVEL_TWO = 45;
    // </editor-fold>
    
     // <editor-fold desc="Amplitude Constants">
    public static final float AMPLITUDE_EQUATION_COEFFICIENT_X_SQUARE = -3.072f;
    public static final float AMPLITUDE_EQUATION_COEFFICIENT_X = 15.898f;
    public static final float AMPLITUDE_EQUATION_CONSTANT = 81.005f;
    // </editor-fold>
}
