package Sound_player;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MusicGui extends JFrame{

    public static void main(String[] args){
        MusicGui prog = new MusicGui();
    }
    Sequencer sequencer;
    Sequence sequence;
    Track track;
    JPanel mainPanel; 
    ArrayList<JCheckBox> checkBoxList;
    String[] instrumentNames = {
        "Bass Drum", "Closed Hi-Hat", "Open Hi-Hat",
        "Ascoutic Snare", "Crash Cymbal", "Hand Clap",
        "High Tom","Hi Bongo", "Maracas", "Whistle",
        "Low Conga","Cowbell", "Vibraslap", "Low-mid Tom",
        "High Agogo", "Open Hi Conga"
    };
    int[] instruments = { 35, 42, 46,38, 49, 39, 50, 60, 70, 72,
                           64, 56, 58, 47, 67, 63 }; 

    public MusicGui(){
        setUpGui();
    }

    // Setup Gui
    private void initGui(){
        setContentPane(mainPanel);
        setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        setBounds(30,30, 300, 300);
        pack();
        setResizable(false);
        setVisible(true);
    }
    private void setUpGui(){

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0,4));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10, 10));

    // Box button
        Box buttonBox = new Box(BoxLayout.Y_AXIS);

        JButton start = new JButton("Start");
        start.addActionListener(new MyStartListener());
        buttonBox.add(start);
        buttonBox.add(Box.createVerticalStrut(5));

        JButton stop = new JButton("Stop");
        stop.addActionListener(new MyStopListener());
        buttonBox.add(stop);
        buttonBox.add(Box.createVerticalStrut(5));

        JButton upTempo = new JButton("Tempo Up");
        upTempo.addActionListener(new MyUpTempoListener());
        buttonBox.add(upTempo);
        buttonBox.add(Box.createVerticalStrut(5));

        JButton downTempo = new JButton("Tempo down");
        downTempo.addActionListener(new MyDownTempoListener());
        buttonBox.add(downTempo);
        buttonBox.add(Box.createVerticalStrut(5));

        mainPanel.add(BorderLayout.EAST, buttonBox);

    // Name box
        Box nameBox = new Box(BoxLayout.Y_AXIS);
        for(String name : instrumentNames){
            nameBox.add(Box.createVerticalStrut(5));
            nameBox.add(new JLabel(name));
        }
        mainPanel.add(BorderLayout.WEST, nameBox);

    // checkBoxList
        checkBoxList = new ArrayList<JCheckBox>();
        JPanel gridCheckBox = new JPanel();
        mainPanel.add(BorderLayout.CENTER, gridCheckBox);
        gridCheckBox.setLayout(new GridLayout(16, 16,2, 1));
        for(int i = 0; i < 256; i++){
            JCheckBox checkBox = new JCheckBox();
            checkBox.setSelected(false);
            checkBoxList.add(checkBox);
            gridCheckBox.add(checkBox);
        }

        setUpMidi();
        initGui();
    }

    // Midi setup
    public void setUpMidi(){
        try{
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ, 4);
            track = sequence.createTrack();
            sequencer.setTempoInBPM(120); 
        } catch(Exception e) { e.printStackTrace(); }
    }
    public void makeTrack(int[] list){
        for(int i = 0; i< 16; i++){
            int key = list[i];
            if(key != 0) {
                track.add(makeEvent(144,9, key,100, i));
                track.add(makeEvent(128,9, key,100, i+1));

            }
        }
    }

    public static MidiEvent makeEvent(int comd, int chan, int one, int two, int tick){
        MidiEvent event = null;
        try {
            ShortMessage mes = new ShortMessage();
            mes.setMessage(comd, chan, one, two);
            event = new MidiEvent(mes, tick);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getStackTrace());
        }
        return event;
    }

   private void buildTrackAndStart(){
    int[] trackList = null;

    sequence.deleteTrack(track);
    track = sequence.createTrack();

    for(int i = 0; i< 16; i++){
        trackList = new int[16];
        int key = instruments[i];
        for(int j = 0; j < 16; j++){
            JCheckBox jc = (JCheckBox) checkBoxList.get(j + 16 * i);
            if(jc.isSelected()){
                trackList[j] = key;
            } else {
                trackList[j] = 0;
            }
        }
        makeTrack(trackList);
        track.add(makeEvent(176, 1, 127, 0, 16));
    }

    track.add(makeEvent(192, 9, 1, 0, 15));
    try {
        sequencer.setSequence(sequence);
        sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);
        sequencer.start();
        sequencer.setTempoInBPM(120);
    } catch (Exception e) {
        // TODO: handle exception
        e.printStackTrace();
    }
   }

    // Handle event
    public class MyStartListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
           buildTrackAndStart();
            System.out.println("start");

        }
    }
    public class MyStopListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            System.out.println("stop");
            sequencer.stop();
        }
    }
    
    public class MyUpTempoListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            float tempoFactor = sequencer.getTempoFactor();
            System.out.println("up");
            sequencer.setTempoFactor((float)(tempoFactor * 1.03));
        }
    }

     public class MyDownTempoListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            float tempoFactor = sequencer.getTempoFactor();
                        System.out.println("down");
            sequencer.setTempoFactor((float)(tempoFactor * .97));
        }
    }
}