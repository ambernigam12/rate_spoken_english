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
package com.cocubes.speech.record;

import com.cocubes.speech.app_constants.ConstantsClass;
import com.cocubes.speech.helper.UtilityPathFunction;
import com.cocubes.speech.logging.LoggingFunctions;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class SoundRecorder {

    private AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    private TargetDataLine line;
    private String soundFileFullName;

    //Set audio format
    public AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
        return format;
    }

    //Open TaragetDataLine and start recording
    public boolean start(long userId, int statementId) {
        boolean rValue = false;

        if (line != null) {
            LoggingFunctions.InsertError("Unexpected null for line", "SoundRecorder", "start", userId, statementId);
            soundFileFullName = null;
            finish();
        }
        //About to create file name
        soundFileFullName = UtilityPathFunction.getSounFilePath(userId, statementId);

        File wavFile = new File(soundFileFullName);
        AudioFormat format = getAudioFormat();
        try {
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, getAudioFormat());
            if (isMicAvailable(format, info)) {
                if (AudioSystem.isLineSupported(info)) {
                    if (!AudioSystem.isLineSupported(info)) {
                        LoggingFunctions.InsertError("Line not supported", "SoundRecorder", "start", userId, statementId);
                    }
                    line = (TargetDataLine) AudioSystem.getLine(info);
                    line.open(format);
                    line.start();
                    AudioInputStream ais = new AudioInputStream(line);

                    AudioSystem.write(ais, fileType, wavFile);
                    rValue = true;
                }
            }
        } catch (LineUnavailableException | IOException ex) {
            if (wavFile.exists()) {
                wavFile.delete();
                soundFileFullName = null;
            }
            if (line != null) {
                finish();
            }
            LoggingFunctions.InsertError(ex.getMessage(), "SoundRecorder", "start", userId, statementId);
        }
        return rValue;
    }

    //Close TargetDataLine and finish recording
    public void finish() {
        if (line != null) {
            line.stop();
            line.close();
            line = null;
        }
    }

    //Check mic is availabe for recording
    public boolean isMicAvailable(AudioFormat format, DataLine.Info info) {
        boolean rValue = false;
        boolean isLocalConnection = false;
        try {
            if (info == null || format == null) {
                format = getAudioFormat();
                info = new DataLine.Info(TargetDataLine.class, format);
                isLocalConnection = true;
            }
            rValue = AudioSystem.isLineSupported(info);
        } catch (Exception e) {
            LoggingFunctions.InsertError(e.getMessage(), "SoundRecorder", "isMicAvailable", ConstantsClass.NO_USERID_OR_STATEMENTID, ConstantsClass.NO_USERID_OR_STATEMENTID);
        } finally {
            if (isLocalConnection && line != null) {
                finish();
            }
        }
        return rValue;
    }

}
