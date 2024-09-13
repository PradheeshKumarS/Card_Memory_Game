import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;



public class MatchCards {

    class Card{
        String cardname;
        ImageIcon image;

        Card(String s , ImageIcon i){
            this.cardname = s;
            this.image = i;
        }

        public String toString(){
            return this.cardname;
        }
    }

    String[] cardlist = {
        "darkness",
        "double",
        "fairy",
        "fighting",
        "fire",
        "grass",
        "lightning",
        "metal",
        "psychic",
        "water"
    };

    int rows = 4;
    int columns = 5;
    int cardWidth = 90;
    int cardHeight = 128;

    ArrayList<Card> cardset ; // create a deck of cards with cardnames and cardimageicons
    ImageIcon cardbackimageicon;

    int boardwidth = columns * cardWidth;
    int boardheight = rows * cardHeight;

    JFrame frame = new JFrame("Card Memory Game");
    JLabel textLabel = new JLabel();
    JPanel tPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel restartgamPanel = new JPanel();
    JButton restarButton = new JButton();

    int errorcount = 0;
    ArrayList<JButton> board; // keep track of all buttons in the board
    Timer hidecardTimer;
    boolean gameReady = false;
    JButton card1selected;
    JButton card2selected;

    MatchCards(){
        setupCards();
        shuffleCards();

        // frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        frame.setSize(boardwidth,boardheight);
        frame.setLocationRelativeTo(null); // sets the window in the centre of the screen
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textLabel.setFont(new Font("Arial", Font.PLAIN,20));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Errors : " + Integer.toString(errorcount));

        tPanel.setPreferredSize(new Dimension(boardwidth,30));
        tPanel.add(textLabel);
        frame.add(tPanel,BorderLayout.NORTH);

        //card game board
        board = new ArrayList<JButton>();
        boardPanel.setLayout(new GridLayout(rows,columns));
        for(int i = 0 ; i < cardset.size() ; i++){
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardWidth,cardHeight));
            tile.setOpaque(true);
            tile.setIcon(cardset.get(i).image);
            tile.setFocusable(false);
            tile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    if(!gameReady) return;
                    JButton tile = (JButton) e.getSource();
                    if(tile.getIcon() == cardbackimageicon){
                        if(card1selected == null){
                            card1selected = tile;
                            int index = board.indexOf(card1selected);
                            card1selected.setIcon(cardset.get(index).image);
                        }
                        else if(card2selected == null){
                            card2selected = tile;
                            int index = board.indexOf(card2selected);
                            card2selected.setIcon(cardset.get(index).image);

                            if (card1selected.getIcon() != card2selected.getIcon()){
                                errorcount += 1;
                                textLabel.setText("Errors : " + Integer.toString(errorcount));
                                hidecardTimer.start();
                            }
                            else{
                                card1selected = null;
                                card2selected = null;
                            }
                        }
                    }
                }
            });
            board.add(tile);
            boardPanel.add(tile);
        }
        frame.add(boardPanel);

        // restart game button
        restarButton.setFont(new Font("Arial" , Font.PLAIN , 16));
        restarButton.setText("Restart Game");
        restarButton.setPreferredSize(new Dimension(boardwidth,30));
        restarButton.setFocusable(false);
        restarButton.setEnabled(false);
        restarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if(!gameReady) return;

                gameReady = false;
                restarButton.setEnabled(false);
                card1selected = null;
                shuffleCards();

                // reassign buttons with new cards
                for (int i = 0 ; i < board.size() ; i++){
                    board.get(i).setIcon(cardset.get(i).image);
                }

                errorcount = 0;
                textLabel.setText("Errors : " + Integer.toString(errorcount));
                hidecardTimer.start();
            }
        });
        restartgamPanel.add(restarButton);
        frame.add(restartgamPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        // start game
        hidecardTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                hideCards();
            }
        });
        hidecardTimer.setRepeats(false);
        hidecardTimer.start();
    }

    void setupCards(){
        cardset = new ArrayList<Card>();
        for(String cardname : cardlist){
            // load each card image
            Image cardImg = new ImageIcon(getClass().getResource("./images/" + cardname + ".jpg")).getImage();
            ImageIcon cardImageIcon = new ImageIcon(cardImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));

            // create card object and add it in the list
            Card card = new Card(cardname,cardImageIcon);
            cardset.add(card);
        }
        cardset.addAll(cardset);

        // load the back card image
        Image cardbackimg = new ImageIcon(getClass().getResource("./images/back.jpg")).getImage();
        cardbackimageicon = new ImageIcon(cardbackimg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
    }

    void shuffleCards(){
        System.out.println(cardset);
        
        // shuffle
        for(int i = 0 ; i < cardset.size() ; i++){
            int j = (int) (Math.random() * cardset.size()); // get random index
            // swap

            Card temp = cardset.get(i);
            cardset.set(i, cardset.get(j));
            cardset.set(j , temp);
        }

        System.out.println(cardset);
    }

    void hideCards(){

        if (gameReady && card1selected != null && card2selected != null){
            card1selected.setIcon(cardbackimageicon);
            card1selected = null;
            card2selected.setIcon(cardbackimageicon);
            card2selected = null;
        }
        else{
            for(int i = 0 ; i < board.size() ; i++){
                board.get(i).setIcon(cardbackimageicon);
            }
            gameReady = true;
            restarButton.setEnabled(true);
        }
        
    }
}