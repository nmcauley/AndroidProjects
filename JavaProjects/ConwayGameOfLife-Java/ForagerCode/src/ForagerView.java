import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class ForagerView implements Observer, ActionListener{
    private ForagerController controller;
    private BoardView viewPanel;
    private JPanel infoPanel;
    private JLabel resourcesLabel;
    private JLabel roundLabel;
    private JLabel genLabel;
    //private JLabel timerLabel;
    private JButton resourcesGen50;
    private JButton resourcesGen65;
    private JButton resourcesGen80;
    private JButton gameStart;
    private int adminId;
    private boolean initialized;


    public ForagerView(ForagerController controller) {
        this.controller = controller;
        controller.addObserver(this);
        this.adminId = controller.adminId;
        initialized = false;
        initializeView();
    }

    public void initializeView(){
        System.out.println("Initializing View");
        // Creating Main Window
        int width = 700;
        int height = 700;
        JFrame mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(width, height);
        mainFrame.setResizable(false);

        // Creating Game Window
        viewPanel = new BoardView(controller.getBoard(), controller.playerId);
        mainFrame.add(viewPanel, BorderLayout.CENTER);


        // Creating info panel
        this.infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout());
        infoPanel.setPreferredSize(new Dimension(width, 100));
        //init resourceLabel and roundLabel
        resourcesLabel = new JLabel("Resources: 0");
        roundLabel = new JLabel("Round: 1");
        genLabel = new JLabel("Gen Type: Null");
        //resourcesLabel.setBounds(10,10,100,15);
        //roundLabel.setBounds(10,110,100,15);
        //genLabel.setBounds(10,210,100,15);

        //Adding resourseL and roundL to jFrame
        infoPanel.add(resourcesLabel);
        infoPanel.add(roundLabel);

        //Check for adminId with playerId
        if(adminId == controller.playerId ){
            infoPanel.add(genLabel);
            //init genConfigButtons and startButton
            resourcesGen50 = new JButton("%50 / %50");
            resourcesGen50.addActionListener(this::actionPerformed);
            //resourcesGen50.setBounds(10,10,50,50);
            resourcesGen65 = new JButton("%65 / %35");
            resourcesGen65.addActionListener(this::actionPerformed);
            //resourcesGen65.setBounds(110,10,50,50);
            resourcesGen80 = new JButton("%80 / %20");
            resourcesGen80.addActionListener(this::actionPerformed);
            //resourcesGen80.setBounds(210,10,50,50);
            gameStart = new JButton("Start");
            gameStart.addActionListener(this::actionPerformed);
            //gameStart.setBounds(310,10,50,50);

            //add genConfigButtons
            infoPanel.add(resourcesGen50);
            infoPanel.add(resourcesGen65);
            infoPanel.add(resourcesGen80);
            infoPanel.add(gameStart);

        }
        mainFrame.add(infoPanel, BorderLayout.PAGE_END);

        setKeyBindings();
        mainFrame.setVisible(true);
        System.out.println("Finished initializing view.");
        initialized = true;
    }

    public void displayGame(int[][] board, HashMap<Integer, Pair<Integer, Integer>> playerPositions, int resources, int round, String genType, boolean isFinished) {
        System.out.println("Calling Display Game");
        viewPanel.setBoard(board);
        viewPanel.setPlayerPositions(playerPositions);
        resourcesLabel.setText("Resources: " + resources);
        roundLabel.setText("Round: " + round);
        genLabel.setText("Gen Type: " + genType);

        //finished game
        if(isFinished){
            roundLabel.setText("GAME OVER");
        }
        viewPanel.repaint();
    }

    @Override
    public void update(Observable observable, Object o) {
        if (initialized) {
            System.out.println("Calling View Update");
            ArrayList<Object> data = (ArrayList<Object>) o;
            displayGame((int[][]) data.get(0), (HashMap<Integer, Pair<Integer, Integer>>) data.get(1), (int) data.get(2), (int) data.get(3), (String) data.get(4), (boolean) data.get(5));
        }
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resourcesGen50){
            controller.genPosition = 1;
            controller.genString = "50/50";
        } else if (e.getSource() == resourcesGen65){
            controller.genPosition = 2;
            controller.genString = "65/35";
        } else if (e.getSource() == resourcesGen80){
            controller.genPosition = 3;
            controller.genString = "80/20";
        } else if (e.getSource() == gameStart){
            controller.sType = true;
        }
    }

    private class BoardView extends JPanel {
        private int[][] board;
        private int cellWidth, cellHeight;
        private HashMap<Integer, Pair<Integer, Integer>> playerPositions;
        private int ourPlayer;

        public BoardView(int[][] board, int ourPlayer) {
            super();
            this.board = board;
            this.playerPositions = new HashMap<>();
            this.ourPlayer = ourPlayer;
            cellWidth = cellHeight = 1;
        }

        public void setBoard(int[][] board) {
            this.board = board;
        }

        public void setPlayerPositions(HashMap<Integer, Pair<Integer, Integer>> playerPositions) {
            this.playerPositions = playerPositions;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            cellWidth = getWidth() / board[0].length;
            cellHeight = getHeight() / board.length;

            for (int r = 0; r < board[0].length; r++) {
                for (int c = 0; c < board.length; c++) {
                    int rb = Math.min(255, (8 - board[r][c]) * 32);
                    g2.setColor(new Color(rb,  255, rb));
                    g2.fillRect(c * cellWidth, r * cellHeight, cellWidth, cellHeight);

                    g2.setColor(Color.BLACK);
                    g2.drawRect(c * cellWidth, r * cellHeight, cellWidth, cellHeight);
                }
            }

            for (Integer playerID : playerPositions.keySet()) {
                if (playerID != ourPlayer) {
                    PlayerInterface player = new RedPlayer(new RegularPlayer());
                    Pair<Integer, Integer> position = playerPositions.get(playerID);
                    g2.setColor(player.getColor());
                    g2.drawString(player.getSymbol(), position.getFirst() * cellWidth, (position.getSecond() + 1) * cellHeight);
                }
            }

            if (playerPositions.containsKey(ourPlayer)) {
                PlayerInterface player = new BluePlayer(new MainPlayer());
                Pair<Integer, Integer> playerPosition = playerPositions.get(ourPlayer);
                g2.setColor(player.getColor());
                g2.drawString(player.getSymbol(), playerPosition.getFirst() * cellWidth, (playerPosition.getSecond() + 1) * cellHeight);
            }
            g2.dispose();
        }
    }

    private void setKeyBindings()  {
        ActionMap actionMap = viewPanel.getActionMap();
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = viewPanel.getInputMap(condition);

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "w");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "s");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "a");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "d");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "e");

        actionMap.put("w", new KeyAction("w"));
        actionMap.put("s", new KeyAction("s"));
        actionMap.put("a", new KeyAction("a"));
        actionMap.put("d", new KeyAction("d"));
        actionMap.put("e", new KeyAction("e"));
    }

    private class KeyAction extends AbstractAction {
        public KeyAction(String actionCommand) {
            putValue(ACTION_COMMAND_KEY, actionCommand);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvt) {
            controller.handleMoveCommand(actionEvt.getActionCommand().charAt(0));
        }
    }
}

interface PlayerInterface {
    String getSymbol();
    Color getColor();
}

class RegularPlayer implements PlayerInterface {
    public String getSymbol() {
        return "⦿";
    }

    public Color getColor() {
        return new Color(0, 0, 0);
    }
}

class MainPlayer implements PlayerInterface {
    public String getSymbol() {
        return "✛";
    }

    public Color getColor() {
        return new Color(0, 0, 0);
    }
}

class BluePlayer implements PlayerInterface {
    PlayerInterface player;
    public BluePlayer(PlayerInterface player) {
        this.player = player;
    }

    public String getSymbol() {
        return player.getSymbol();
    }

    public Color getColor() {
        return new Color(0, 0, 255);
    }
}

class RedPlayer implements PlayerInterface {
    PlayerInterface player;
    public RedPlayer(PlayerInterface player) {
        this.player = player;
    }

    public String getSymbol() {
        return player.getSymbol();
    }

    public Color getColor() {
        return new Color(255, 0, 0);
    }
}
