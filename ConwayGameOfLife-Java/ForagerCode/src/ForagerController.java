import javax.swing.*;
import javax.swing.Timer;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public class ForagerController extends Observable {

    ForagerInterface model;
    int[][] resourcesBoard;
    HashMap<Integer, Pair<Integer, Integer>> playerPositions;
    int playerLocationX;
    int playerLocationY;
    int resources;
    int round = 1;
    int adminId;
    Timer timer;
    int playerId;
    String genString;
    int genPosition;
    boolean isPlaying = false;
    boolean sType = false;
    boolean isFinished = false;



    public ForagerController(ForagerInterface model) throws RemoteException {
        this.model = model;
        model.getPlayerPositions(-1);
        this.playerId = hashCode();

        //changed starting location to random space on the board
        Random gen = new Random();
        this.playerLocationX = gen.nextInt(32);
        this.playerLocationY = gen.nextInt(32);

        model.sendPlayerPosition(playerId, playerLocationX, playerLocationY);
        this.resources = 0;
        this.round = 1;
        this.adminId = model.getAdminId();
        timer = new Timer(100, event -> update());
        timer.start();
    }

    public void update() {
        try {
            if(model.isFinishedGame()){
                isFinished =true;
                timer.stop();
            }
            playerPositions = model.getPlayerPositions(-1);
            Pair<Integer, Integer> playerPos = playerPositions.get(playerId);
            this.playerLocationY = playerPos.getSecond();
            this.playerLocationX = playerPos.getFirst();
            resourcesBoard = model.getResources();

            if (this.adminId == playerId){
                if(this.sType == true && model.getIsPlaying() == false){
                    //System.out.println("Change Position");
                    model.setGenType(genPosition);
                    //System.out.println("Starting");
                    model.setStart(sType);
                    model.newRound(genPosition, this.round);
                }
                this.sType = false;
                model.setStart(false);

            }
            round = model.getRound();

            setChanged();
            notifyObservers();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyObservers() {
        if (hasChanged()) {
            notifyObservers(new ArrayList<Object>(Arrays.asList(resourcesBoard, playerPositions, resources, round, genString, isFinished)));
        }
    }

    public int[][] getBoard() {
        try {
            return model.getResources();
        } catch (RemoteException e) {
            return new int[32][32];
        }
    }

    public void handleMoveCommand(Character key) {
        try {
            int newX = playerLocationX;
            int newY = playerLocationY;
            switch (key) {
                case 'w':
                    newY--;
                    break;
                case 's':
                    newY++;
                    break;
                case 'a':
                    newX--;
                    break;
                case 'd':
                    newX++;
                    break;
                case 'e':
                    this.resources += model.gatherResource(playerId);
                    return;
                default:
                    break;
            }
            //if there is no player at that location
            if(!model.getPlayerPositions(this.playerId).containsValue(new Pair<>(newX, newY))) {
                model.sendPlayerPosition(this.playerId, newX, newY);
            }
        } catch (RemoteException ignored) { }
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        try {
            Remote obj = Naming.lookup("//localhost/ForagerServer");
            ForagerInterface foragerServer = (ForagerInterface) obj;
            ForagerController controller = new ForagerController(foragerServer);
            ForagerController controller2 = new ForagerController(foragerServer);
            ForagerView view = new ForagerView(controller);
            ForagerView view2 = new ForagerView(controller2);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
