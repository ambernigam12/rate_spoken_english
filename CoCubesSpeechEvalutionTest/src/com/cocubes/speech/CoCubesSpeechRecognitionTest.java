package com.cocubes.speech;

import com.cocubes.speech.app_constants.ConstantsClass;
import com.cocubes.speech.record.SoundRecorder;
import com.cocubes.speech.enums.StateTypeEnum;
import com.cocubes.speech.helper.UtilityFunctions;
import com.cocubes.speech.helper.UtilityPathFunction;
import com.cocubes.speech.logging.LoggingFunctions;
import com.cocubes.speech.statement.StatementClass;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class CoCubesSpeechRecognitionTest extends javax.swing.JFrame {

    public CoCubesSpeechRecognitionTest() {
        initComponents();
        statementLabel = questionStatementLabel;
        btnStart = startButton;
        isContinue = true;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        startButton = new javax.swing.JButton();
        timerLabel = new javax.swing.JLabel();
        questionStatementLabel = new javax.swing.JLabel();
        questionNumberLabel = new javax.swing.JLabel();
        headerLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Spoken English Evaluator");
        setResizable(false);

        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        timerLabel.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        timerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timerLabel.setText("-");
        timerLabel.setToolTipText("");

        questionStatementLabel.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        questionStatementLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        questionStatementLabel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Read the content in the box as the timer starts", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(153, 153, 153)));

        questionNumberLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        headerLabel.setText("Remaining Time");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(questionStatementLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(startButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(questionNumberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(309, 309, 309)
                        .addComponent(headerLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(332, 332, 332)
                        .addComponent(timerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(headerLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(timerLabel)
                .addGap(28, 28, 28)
                .addComponent(questionStatementLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(startButton)
                    .addComponent(questionNumberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        questionStatementLabel.getAccessibleContext().setAccessibleName("statementLabel");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private Timer timer;
    private static HashMap<Integer, StatementClass> statementMap = new HashMap<>();
    private int[] statementIdArray;
    private long userId;
    private int count = 0;
    private int timeLeft;
    private static int level = 0;
    private static StateTypeEnum state = null;
    private static JLabel statementLabel;
    private static JButton btnStart;
    private boolean isContinue;
    private static SoundRecorder objSoundRecorder = new SoundRecorder();
    private static UsersClass objUserClass = new UsersClass();
    private static boolean isIntonation;

    class TimerListener implements ActionListener {

        @Override
        //Record all user responses
        public void actionPerformed(ActionEvent evt) {
            if (isContinue) {
                if (timeLeft >= 0) {
                    timerLabel.setText("" + --timeLeft);
                }
                if (timeLeft < 0) {
                    switch (state) {
                        case Start:
                            if (timer != null) {
                                timer.stop();
                                timer.removeActionListener(this);
                                timer = null;
                            }
                            if (count < statementIdArray.length) {
                                questionNumberLabel.setText(String.format("Question #: %d/%d", count + 1, statementIdArray.length));
                                String statement = statementMap.get(statementIdArray[count]).getOriginalText();
                                questionStatementLabel.setText("<html>" + (char) (statement.charAt(0) & ~(1 << 5)) + statement.substring(1) + "</html>");
                                timeLeft = (((1 + (int) Math.ceil(statementMap.get(statementIdArray[count]).getWordCount() * ConstantsClass.PER_WORD_ALLOWED_SECONDS)) * (int) ConstantsClass.MILLISECOND_MULTIPLIER) + ConstantsClass.BUFFER_TIME_IN_MILLISECONDS) / (int) ConstantsClass.MILLISECOND_MULTIPLIER;
                                timerLabel.setText("" + timeLeft);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!objSoundRecorder.start(userId, statementMap.get(statementIdArray[count]).getStatementId())) {
                                            isContinue = false;
                                        }
                                    }
                                }).start();
                                timer = new Timer(ConstantsClass.TIMER_INITIALISER, (ActionListener) new TimerListener());
                                timer.start();
                                state = StateTypeEnum.Transition;
                            }
                            break;
                        case Transition:
                            questionStatementLabel.setText("");
                            timerLabel.setText("-");
                            objSoundRecorder.finish();
                            count++;
                            if (count >= statementIdArray.length) {
                                questionStatementLabel.setText("Evaluating your speech profile. Please wait...");
                                questionNumberLabel.setText("");
                                timer.stop();
                                timer = null;
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!SwingUtilities.isEventDispatchThread()) {
                                            SwingUtilities.invokeLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (objUserClass.updateUserClassMap(userId, statementIdArray)) {
                                                        level = objUserClass.Evaluate(statementMap, isIntonation);
                                                        questionStatementLabel.setText(level > ConstantsClass.CODE_FAIL_VALUE ? "Your English proficiency level is " + level : "Something went wrong!");
                                                    } else {
                                                        questionStatementLabel.setText("Something went wrong!");
                                                    }
                                                    startButton.setEnabled(true);
                                                }
                                            });
                                        }
                                    }
                                }).start();
                            }
                            state = StateTypeEnum.Start;
                            break;
                    }
                }
            } else if (timer != null) {
                timer.stop();
                timer = null;
            }
        }
    }

    //Fetch statemetnId list for test and start test
    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        count = 0;
        boolean rValue = false;
        String userIdString = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        if (UtilityFunctions.longTryParse(userIdString)) {
            userId = Long.parseLong(userIdString);
            String userDirectory = UtilityPathFunction.getUserDirectoryPath(userId);
            File fileUser = new File(userDirectory);
            rValue = fileUser.exists() || fileUser.mkdir();
            if (rValue) {
                String userSoundDirectory = UtilityPathFunction.getSounDirectoryPath(userId);
                File soundFile = new File(userSoundDirectory);
                rValue = soundFile.exists() || soundFile.mkdir();
                if (rValue) {
                    statementIdArray = UtilityFunctions.pickStatementSet();
                    if (statementIdArray.length > 0 && UtilityFunctions.createOrUpdateStatementHashMap(UtilityFunctions.convertArrayToList(statementIdArray), statementMap)) {
                        timer = new Timer(ConstantsClass.TIMER_INITIALISER, (ActionListener) new TimerListener());
                        state = StateTypeEnum.Start;
                        timer.start();
                        startButton.setEnabled(false);
                    } else {
                        LoggingFunctions.InsertError("Invalid Statement List", "CoCubesSpeechRecognitionTest", "startButtonActionPerformed", userId, ConstantsClass.NO_USERID_OR_STATEMENTID);
                    }
                } else {
                    LoggingFunctions.InsertError(String.format("Directory not created path:%s", userSoundDirectory), "CoCubesSpeechRecognitionTest", "startButtonActionPerformed", userId, ConstantsClass.NO_USERID_OR_STATEMENTID);
                }
            } else {
                LoggingFunctions.InsertError(String.format("Directory not created path:%s", userDirectory), "CoCubesSpeechRecognitionTest", "startButtonActionPerformed", userId, ConstantsClass.NO_USERID_OR_STATEMENTID);
            }
        } else {
            LoggingFunctions.InsertError(String.format("Invalid user file name:%s ", userIdString), "CoCubesSpeechRecognitionTest", "startButtonActionPerformed", ConstantsClass.NO_USERID_OR_STATEMENTID, ConstantsClass.NO_USERID_OR_STATEMENTID);
        }
        if (!rValue) {
            questionStatementLabel.setText("Something went wrong!");
        }
    }//GEN-LAST:event_startButtonActionPerformed

    //start the project
    public static void main(String args[]) {
        UtilityFunctions.createValidStatementIds();
        objUserClass.updateUserClassMapFormExistingUserDirectory(statementMap);
        isIntonation = false;
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                CoCubesSpeechRecognitionTest frame = new CoCubesSpeechRecognitionTest();
                frame.setVisible(true);
                if (!objSoundRecorder.isMicAvailable(null, null)) {
                    btnStart.setEnabled(false);
                    JOptionPane.showMessageDialog(frame, "Unable to find microphone, please check and restart found!", "Resource not found", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel headerLabel;
    private javax.swing.JLabel questionNumberLabel;
    private javax.swing.JLabel questionStatementLabel;
    private javax.swing.JButton startButton;
    private javax.swing.JLabel timerLabel;
    // End of variables declaration//GEN-END:variables
}
