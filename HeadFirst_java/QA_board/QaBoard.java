package QA_board;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class QaBoard {
    private JFrame myFrame;
    private JTextArea question;
    private JTextArea answer;
    ArrayList<QuizCard> cardList;

    public static void main(String[] args){
        QaBoard board = new QaBoard();
        board.go();
    } 

    private void go(){
        myFrame = new JFrame("Q & a board");
        JPanel mainPanel = new JPanel();
        Font bigFont = new Font("sanserif", Font.BOLD, 24);
        question = new JTextArea(6,20);
        question.setLineWrap(true);
        question.setWrapStyleWord(true);
        question.setFont(bigFont);

        JScrollPane qScroller = new JScrollPane(question);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); 

        answer = new JTextArea(6,20);
        answer.setLineWrap(true);
        answer.setWrapStyleWord(true);
        answer.setFont(bigFont);
        
        JScrollPane aScroller = new JScrollPane(answer);
        aScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        aScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); 
        
        JButton nextButton = new JButton("Next Card");
        cardList = new ArrayList<QuizCard>();

        JLabel qLabel = new JLabel("Question");
        JLabel aLabel = new JLabel("Answer");

        mainPanel.add(qLabel);
        mainPanel.add(qScroller);
        mainPanel.add(aLabel);
        mainPanel.add(aScroller);
        mainPanel.add(nextButton);
        nextButton.addActionListener(new NextCardListener());

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        newMenuItem.addActionListener(new NewMenuListener());

        saveMenuItem.addActionListener(new SaveMenuListener());
        fileMenu.add(newMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);

        myFrame.setJMenuBar(menuBar);
        myFrame.getContentPane().add(BorderLayout.CENTER,mainPanel);
        myFrame.setSize(500,600);
        myFrame.setVisible(true);
    }

    public class NextCardListener implements ActionListener{
        public void actionPerformed(ActionEvent ev){
            QuizCard card = new QuizCard(question.getText(), answer.getText());
            System.out.println(question.getText());
            System.out.println(answer.getText() + "\n");
            cardList.add(card);
            clearCard();
        }
    }
     public class SaveMenuListener implements ActionListener{
        public void actionPerformed(ActionEvent ev){
            QuizCard card = new QuizCard(question.getText(), answer.getText());
            cardList.add(card);
            
            JFileChooser fileSave = new JFileChooser();
            fileSave.showSaveDialog(myFrame);
            saveFile(fileSave.getSelectedFile());
        }
    }
    public class NewMenuListener implements ActionListener{
        public void actionPerformed(ActionEvent ev){
          cardList.clear();
          clearCard();
        }
    }
    private void clearCard(){
        question.setText("");
        answer.setText("");
        question.requestFocus();
    }
    private void saveFile(File file){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for(QuizCard card: cardList){
                writer.write(card.getQuestion() + "/");
                writer.write(card.getAnswer() + "\n");
            }
            writer.close();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Could't write the CardList out");
            e.printStackTrace();
        }
    }
    private class QuizCard{
        private String question;
        private String answer;

        public QuizCard(String ques, String answ){
            question = ques;
            answer = answ;
        }

        public String getQuestion(){
            return question;
        }

        public String getAnswer(){
            return answer;
        }    }

}
