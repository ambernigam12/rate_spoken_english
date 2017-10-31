package createtranscription;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class CreateTranscription {

    private static String sourceFile = "input_corpus" + File.separator + "corpus.txt";
    private static String transcriptionFile = "output_corpus" + File.separator + "sample.transcription";
    private static String soundFilePath = "input_sound_files";
    private static String renamedSoundFilePath = "output_sound_files" + File.separator;
    private static String fileidsFile = "output_corpus" + File.separator + "sample.fileids";
    private static String extension = ".wav";
    private static String start = "<s> ";
    private static String end = " </s> ";
    private static String bracketOpen = " (";
    private static String bracketClose = ")";

    public static void RenameSoundFiles() {
        String directoryName = soundFilePath;
        File[] fileList = new File(soundFilePath).listFiles();
        Arrays.sort(fileList);
        char ch = 'a';
        for (File soundFile : fileList) {
            File[] soundFileList = soundFile.listFiles();
            Arrays.sort(soundFileList);
            int count = 1;
            String name = "" + ch;
            directoryName = directoryName + File.separator;
            for (File file : soundFileList) {
                file.renameTo(new File(renamedSoundFilePath + name + count + ".wav"));
                count++;
            }
            ch++;
        }
    }

    public static void main(String[] args) throws IOException {
        RenameSoundFiles();
        char ch = 'a';
        int numberOfSoundFolders = new File(soundFilePath).listFiles().length;
        File file = new File(sourceFile);
        StringBuilder stringTranscription = new StringBuilder();
        StringBuilder stringFileids = new StringBuilder();
        for (int i = 0; i < numberOfSoundFolders; i++) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                int count = 1;
                BufferedReader reader = new BufferedReader(new FileReader(file.getPath()));
                String str;
                while ((str = reader.readLine()) != null) {
                    String soundFileName = "" + ch + count;
                    str = str.trim();
                    stringTranscription.append(start.concat(str.concat(end.concat(bracketOpen.concat(soundFileName.concat(extension.concat(bracketClose))))))).append(System.lineSeparator());
                    stringFileids.append(soundFileName).append(System.lineSeparator());
                    count++;
                }
            }
            ch++;
        }
        try {
            BufferedWriter writerTranscription = new BufferedWriter(new FileWriter(transcriptionFile));
            BufferedWriter writerFileids = new BufferedWriter(new FileWriter(fileidsFile));
            writerTranscription.write(stringTranscription.toString());
            writerFileids.write(stringFileids.toString());
            writerTranscription.close();
            writerFileids.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

}
