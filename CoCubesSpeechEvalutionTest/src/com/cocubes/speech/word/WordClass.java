package com.cocubes.speech.word;

import edu.cmu.sphinx.result.WordResult;

public class WordClass {

    private String word;
    private float durationOfUtterance;
    private float startTimeOfUtterance;
    private float endTimeOfUtterance;
    private WordResult wordResult;
    private boolean isValidPause;
    private float amplitude;

    public WordClass(String word, WordResult result, float startTime, float endTime, float duration) {
        this.word = word;
        this.wordResult = result;
        this.startTimeOfUtterance = startTime;
        this.endTimeOfUtterance = endTime;
        this.durationOfUtterance = duration;
        this.isValidPause = false;
        this.amplitude = 0;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public float getStartTimeOfUtterance() {
        return startTimeOfUtterance;
    }

    public float getDurationOfUtterance() {
        return durationOfUtterance;
    }
    
    public void setDurationOfUtterance(float duration) {
        durationOfUtterance = duration;
    }

    public boolean getIsValidPause() {
        return isValidPause;
    }

    public void setIsValidPause(boolean value) {
        isValidPause = value;
    }

    public WordResult getWordResult() {
        return wordResult;
    }

    public void setStartTimeOfUtterance(float startTimeOfUtterance) {
        this.startTimeOfUtterance = startTimeOfUtterance;
    }

    public float getEndTimeOfUtterance() {
        return endTimeOfUtterance;
    }

    public void setEndTimeOfUtterance(float endTimeOfUtterance) {
        this.endTimeOfUtterance = endTimeOfUtterance;
    }

    public float getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(float amplitude) {
        this.amplitude = amplitude;
    }
}
