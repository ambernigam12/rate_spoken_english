Training Flow

Files needed:
1. Corpus = newline separated statements without any punctuations (all chars in lower case)
2. Sound files
3. '.dic' Dictionary file, '.lm' ngram file, and acoustic model files

Steps:
Transform through intermediate tool:
Corpus + Sound Files -> Transcripts (in the desired format) + Fields file + Renaming and storing of sound files under a single folder

Training through bw +  map_adapt:
Transcript + Fields File + Sound Files + Dictionary -> Accoustic model

Finally, copy:
Copy accoustic model, dictionary, n-gram files to the respective location in the main project

Detailed Steps:
 1. Steps to create lm file and corresconding dictionary

 1.1. Create corpus.txt a text file containing all sentences. 
 a) The format of corpus.txt file - Each sentence should be in new line and should not have punctuation symbols (, . " " etc).
 1.2. Goto link [http://www.speech.cs.cmu.edu/tools/lmtool-new.html], upload this text file to 'Upload a sentence corpus file:'
 1.3. Click on COMPILE KNOWLEDGE BASE button, download the obtained files
 1.4. Obtained '.lm' file and '.dic' contains text in the capital case, convert this text into the small case


 2. Steps to train Acoustic Model

[Link: http://cmusphinx.sourceforge.net/wiki/tutorialam]

[Steps to create sample.transcription and sample.fileids file]
 2.1. Copy corpus.txt to 'Training_Module/CreateTranscription/input_corpus'
 2.2. Copy training sound folder corresponding to each trainner to Training_Module/CreateTranscription/input_sound_files NOTE: Each folder should contain sound files corresponding to all statements in the corpus.txt and the sequence of sound files should be same as the statement in corpus.txt
 2.3. Open netbeans project CreateTranscription and run it
 2.4. Copy '.dic' file obtained in 'Steps: 1.4' to Training_Module/TrainingAcousticModel and rename it as 'en.dict'
 2.5. Move all sound files from 'Training_Module/CreateTranscription/output_sound_files' to 'Training_Module/TrainingAcousticModel'
 2.6. Move sample.transcription and sample.fileids from 'CreateTranscription/sample_directory' to TrainingAcousticModel
 2.7. Goto directory TrainingAcousticModel in terminal and execute following commands to get model

sphinx_fe -argfile en-model/feat.params \
   -samprate 16000 -c sample.fileids -di . -do . \
   -ei wav -eo mfc -mswav yes

./bw \
   -hmmdir en-model \
   -moddeffn en-model/mdef \
   -ts2cbfn .cont. \
   -feat 1s_c_d_dd \
   -lda en-model/feature_transform\
   -cmn current \
   -agc none \
   -dictfn en.dict \
   -ctlfn sample.fileids \
   -lsnfn sample.transcription \
   -accumdir .

./map_adapt \
    -moddeffn en-model/mdef \
    -ts2cbfn .cont. \
    -meanfn en-model/means \
    -varfn en-model/variances \
    -mixwfn en-model/mixture_weights \
    -tmatfn en-model/transition_matrices \
    -accumdir . \
    -mapmeanfn en-new/means \
    -mapvarfn en-new/variances \
    -mapmixwfn en-new/mixture_weights \
    -maptmatfn en-new/transition_matrices

 2.8. Copy 'en-new' folder, 'en.dict' file to 'CoCubesSpeechEvalutionTest/models'
 2.9. Copy '.lm' file obtain in 'Steps: 1.4' to 'CoCubesSpeechEvalutionTest/models' and rename it to 'eng.lm'

NOTE: If Sound file are not in 16Khz format then convert them using following steps:

1. First install ffmpeg command: sudo apt-get install ffmpeg

2. Goto sound directory, then run following command: 

3. for f in *.wav; do ffmpeg -i "$f" -acodec pcm_s16le -ac 1 -ar 16000 "t${f%.wav}.wav"; done

