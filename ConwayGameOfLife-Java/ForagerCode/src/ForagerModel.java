import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import javax.swing.Timer;

public class ForagerModel extends UnicastRemoteObject implements ForagerInterface {
    private final HashMap<Integer, Pair<Integer, Integer>> playerPositions;
    private final HashMap<Integer, Move> playerMoves;
    private int[][] resources;
    private Timer timer;
    private Timer finalTimer;
    private final static double growthParam = .009;
    public int adminId;
    public int round = 0;
    public boolean startConfig = false;
    public boolean isPlaying = false;
    public boolean[] genType = {false, false, false};
    public String genString;
    public final int size;
    public int steps = 0;

    public boolean finishedGame = false;
    public ForagerModel(int size) throws RemoteException {
        super();
        //gameclock: finishes after 5 minutes
        //Each Round Lasts 5 Mins
        finalTimer = new Timer(125000, e -> {
            finishedGame = true;
        });

        playerPositions = new HashMap<>();
        playerMoves = new HashMap<>();
        resources = new int[size][size];
        this.size = size;
    }

    @Override
    public void newRound(int genPosition, int round){


        //Cases for diffrent reourse gen types
        if (genType[0]){
            //50/50
            System.out.println("Generating 50/50");



            Random r = new Random();
            //Cluster 1
            this.resources[5][5] = r.nextInt(8);
            this.resources[6][7] = r.nextInt(8);
            this.resources[7][9] = r.nextInt(8);
            this.resources[8][6] = r.nextInt(8);
            this.resources[5][6] = r.nextInt(8);
            this.resources[5][7] = r.nextInt(8);
            this.resources[5][8] = r.nextInt(8);
            this.resources[7][5] = r.nextInt(8);
            this.resources[7][9] = r.nextInt(8);
            this.resources[8][5] = r.nextInt(8);
            this.resources[6][7] = r.nextInt(8);
            this.resources[6][9] = r.nextInt(8);
            this.resources[9][7] = r.nextInt(8);
            this.resources[6][5] = r.nextInt(8);
            this.resources[8][7] = r.nextInt(8);
            this.resources[9][9] = r.nextInt(8);

            //Cluster 2
            this.resources[25][25] = r.nextInt(8);
            this.resources[26][27] = r.nextInt(8);
            this.resources[27][29] = r.nextInt(8);
            this.resources[28][26] = r.nextInt(8);
            this.resources[25][26] = r.nextInt(8);
            this.resources[25][27] = r.nextInt(8);
            this.resources[25][28] = r.nextInt(8);
            this.resources[27][25] = r.nextInt(8);
            this.resources[27][29] = r.nextInt(8);
            this.resources[28][25] = r.nextInt(8);
            this.resources[26][27] = r.nextInt(8);
            this.resources[26][29] = r.nextInt(8);
            this.resources[29][27] = r.nextInt(8);
            this.resources[26][25] = r.nextInt(8);
            this.resources[28][27] = r.nextInt(8);
            this.resources[29][29] = r.nextInt(8);



        } else if (genType[1]){
            //65/35
            System.out.println("Generating 65/35");
            Random r = new Random();
            //Cluster 1
            this.resources[5][5] = r.nextInt(8);
            this.resources[6][7] = r.nextInt(8);
            this.resources[7][9] = r.nextInt(8);
            this.resources[8][6] = r.nextInt(8);
            this.resources[5][6] = r.nextInt(8);
            this.resources[5][7] = r.nextInt(8);
            this.resources[5][8] = r.nextInt(8);
            this.resources[7][5] = r.nextInt(8);
            this.resources[7][9] = r.nextInt(8);
            this.resources[8][5] = r.nextInt(8);
            this.resources[6][7] = r.nextInt(8);
            this.resources[6][9] = r.nextInt(8);
            this.resources[9][7] = r.nextInt(8);
            this.resources[6][5] = r.nextInt(8);
            this.resources[8][7] = r.nextInt(8);
            this.resources[9][9] = r.nextInt(8);
            this.resources[4][5] = r.nextInt(8);
            this.resources[6][4] = r.nextInt(8);
            this.resources[8][9] = r.nextInt(8);
            this.resources[4][4] = r.nextInt(8);
            this.resources[4][7] = r.nextInt(8);
            this.resources[6][8] = r.nextInt(8);

            //Cluster 2
            this.resources[25][25] = r.nextInt(8);
            this.resources[26][27] = r.nextInt(8);
            this.resources[27][29] = r.nextInt(8);
            this.resources[28][26] = r.nextInt(8);
            this.resources[25][26] = r.nextInt(8);
            this.resources[25][27] = r.nextInt(8);
            this.resources[25][28] = r.nextInt(8);
            this.resources[27][25] = r.nextInt(8);
            this.resources[27][29] = r.nextInt(8);
            this.resources[28][25] = r.nextInt(8);



        } else if (genType[2]){
            //80/20
            System.out.println("Generating 80/20");
            Random r = new Random();
            //Cluster 1
            this.resources[5][5] = r.nextInt(8);
            this.resources[6][7] = r.nextInt(8);
            this.resources[7][9] = r.nextInt(8);
            this.resources[8][6] = r.nextInt(8);
            this.resources[5][6] = r.nextInt(8);
            this.resources[5][7] = r.nextInt(8);
            this.resources[5][8] = r.nextInt(8);



            //Cluster 2
            this.resources[25][25] = r.nextInt(8);
            this.resources[26][27] = r.nextInt(8);
            this.resources[27][29] = r.nextInt(8);
            this.resources[28][26] = r.nextInt(8);
            this.resources[25][26] = r.nextInt(8);
            this.resources[25][27] = r.nextInt(8);
            this.resources[25][28] = r.nextInt(8);
            this.resources[27][25] = r.nextInt(8);
            this.resources[27][29] = r.nextInt(8);
            this.resources[28][25] = r.nextInt(8);
            this.resources[26][27] = r.nextInt(8);
            this.resources[26][29] = r.nextInt(8);
            this.resources[29][27] = r.nextInt(8);
            this.resources[26][25] = r.nextInt(8);
            this.resources[28][27] = r.nextInt(8);
            this.resources[29][29] = r.nextInt(8);
            this.resources[24][25] = r.nextInt(8);
            this.resources[26][24] = r.nextInt(8);
            this.resources[28][29] = r.nextInt(8);
            this.resources[24][24] = r.nextInt(8);
            this.resources[24][27] = r.nextInt(8);
            this.resources[26][28] = r.nextInt(8);
            this.resources[27][28] = r.nextInt(8);
            this.resources[27][26] = r.nextInt(8);
            this.resources[26][25] = r.nextInt(8);


        } else {
            Random r = new Random();
            System.out.println("Random");
            for (int i = 0; i < size; i++) {
                int cell = r.nextInt(size * size);
                resources[cell / size][cell % size] = r.nextInt(8);
            }
        }

        timer = new Timer(100, event -> {
            try {
                steps++;
                step();
                if(isPlaying){
                    this.startConfig = false;
                    if(steps == 2500){
                        timer.stop();
                        this.isPlaying = false;
                        steps = 0;
                        resources = new int[size][size];
                        timer.restart();
                    }
                }
            } catch (RemoteException ignored) { }
        });
        finalTimer.start();
        timer.start();
        this.round += 1;
        this.isPlaying = true;

    }

    @Override
    public HashMap<Integer, Pair<Integer, Integer>> getPlayerPositions(int except) throws RemoteException {
        if (except == -1) {
            return playerPositions;
        }

        HashMap<Integer, Pair<Integer, Integer>> result = new HashMap<>();

        for (Integer playerID : playerPositions.keySet()) {
            if (playerID != except) {
                result.put(playerID, playerPositions.get(playerID));
            }
        }

        return result;
    }

    @Override
    public void sendPlayerPosition(int playerID, int positionX, int positionY) throws RemoteException {

        //Adding to admin id if hashmap is empty
        if (playerPositions.size() == 0){
            adminId = playerID;
        }

        // If the player does not exist in the hashmap, assume it is a new player and give it that position.
        if (!playerPositions.containsKey(playerID)) {
            playerPositions.put(playerID, new Pair<>(positionX, positionY));
            return;
        }

        int bound = resources.length;
        // If the player has already made a move and it hasn't been processed yet or if it is out of bounds, do nothing.
        if (playerMoves.containsKey(playerID) || !(positionX >= 0 && positionX < bound && positionY >= 0 && positionY < bound)) {
            return;
        }

        Pair<Integer, Integer> currentPos = playerPositions.get(playerID);

        // Check if move is valid (only one square at a time.
        if (Math.abs(positionX - currentPos.getFirst()) + Math.abs(positionY - currentPos.getSecond()) > 1) {
            return;
        }

        int dx = positionX - currentPos.getFirst();
        int dy = positionY - currentPos.getSecond();

        // If move is valid, then add it to the queue.
        playerMoves.put(playerID, new Move(dx, dy));
    }

    @Override
    public synchronized int gatherResource(int forPlayer) throws RemoteException {
        // Can't make this server authoritative because player resource data is stored client side and
        // there exists no method that updates player resource.

        // We assume that the client has sent the player position first before attempting to gather resources.
        if (!playerPositions.containsKey(forPlayer)) {
            return 0;
        }

        Pair<Integer, Integer> currentPos = playerPositions.get(forPlayer);
        int col = currentPos.getFirst();
        int row = currentPos.getSecond();

        if (resources[row][col] > 0) {
            resources[row][col]--;
            return 1;
        }

        return 0;
    }

    @Override
    public void updateResources() throws RemoteException {
        Random rand = new Random();
        LinkedList<int[]> toIncrease = new LinkedList<>();
        for (int r = 0; r < resources.length; r++) {
            for (int c = 0; c < resources[0].length; c++) {
                double threshold = calculateThreshold(r, c);
                if (rand.nextDouble() < threshold) {
                    toIncrease.add(new int[]{r, c});
                }
            }
        }

        for (int[] cell : toIncrease) {
            int row = cell[0];
            int col = cell[1];
            resources[row][col] = Math.min(resources[row][col] + 1, 7);
        }
    }

    @Override
    public int[][] getResources() throws RemoteException {
        return resources;
    }

    @Override
    public int getRound() throws RemoteException {
        return round;
    }

    @Override
    public int getAdminId() throws RemoteException {
        return adminId;
    }

    @Override
    public String getTime() throws RemoteException {
        return timer.toString();
    }

    @Override
    public boolean getIsPlaying() throws RemoteException {
        return isPlaying;
    }

    @Override
    public boolean isFinishedGame(){
        return finishedGame;
    }

    @Override
    public void setGenType(int position) throws RemoteException {
        if (position == 1){
            this.genType[0] = true;
            this.genString = "50/50";
            System.out.println("50/50");

            this.genType[1] = false;
            this.genType[2] = false;

        } else if (position == 2){
            this.genType[0] = false;
            this.genType[1] = true;
            this.genString = "65/35";
            System.out.println("65/35");
            this.genType[2] = false;

        } else if (position == 3){
            this.genType[0] = false;
            this.genType[1] = false;
            this.genString = "80/20";
            System.out.println("80/20");
            this.genType[2] = true;

        } else {
            this.genType[0] = false;
            this.genType[1] = false;
            this.genType[2] = false;
            this.genString = "Random";
            System.out.println("Random");
        }
    }

    @Override
    public void setStart(Boolean sType) throws RemoteException {
        this.startConfig = sType;
    }



    private double calculateThreshold(int r, int c) {
        int activeNeighbors = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                try {
                    if ((i != 0 || j != 0) && resources[r + i][c + j] > 0) {
                        activeNeighbors++;
                    }
                } catch (IndexOutOfBoundsException ignored) {
                }
            }
        }

        return growthParam * activeNeighbors / 8;
    }

    private void processMoves() {
        for (int playerID : playerMoves.keySet()) {
            Move move = playerMoves.get(playerID);
            playerPositions.replace(playerID, move.applyMove(playerPositions.get(playerID)));
            playerMoves.remove(playerID);
        }
    }

    private void step() throws RemoteException {
        updateResources();
        processMoves();
    }

    static class Move {
        private final int dx, dy;

        public Move(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public Pair<Integer, Integer> applyMove(Pair<Integer, Integer> position) {
            return new Pair<>(position.getFirst() + dx, position.getSecond() + dy);
        }
    }

    public static void main(String[] args) {
        System.setSecurityManager(new SecurityManager());

        try {
            ForagerInterface foragerServer = new ForagerModel(32);
            Naming.rebind("/ForagerServer", foragerServer);
        } catch (Exception e) {
            System.out.println("Failed to create model in RMI.");
            e.printStackTrace();
        }
    }
}
