import java.util.ArrayList;

public class GameLogic {
    private boolean[][] playerBoard;
    private boolean[][] opponentBoard;
//    private ArrayList<String> playerShipCoordArray = new ArrayList<>();
//    private ArrayList<String> opponentShipCoordArray = new ArrayList<>();
    private ArrayList<ArrayList<String>> playerShipCoordArray;
    private ArrayList<ArrayList<String>> opponentShipCoordArray;

    private ArrayList<ArrayList<String>> playerShipCoordArrayCopy;
    private ArrayList<ArrayList<String>> opponentShipCoordArrayCopy;
    private int playerHits = 0;
    private int opponentHits = 0;

    private ArrayList<String> lastShipSunk;

    private ArrayList<String> lastShipSunk2;

    GameLogic(boolean[][] playerBoard, boolean[][] opponentBoard){
        placeShips(playerBoard, opponentBoard);
    }
    public void placeShips(boolean[][] playerBoard, boolean[][] opponentBoard){
        this.playerBoard = playerBoard;
        this.opponentBoard = opponentBoard;

    }
    public boolean makePlayerMove(Coord coord) {
        if (opponentBoard[coord.row][coord.col]){
            playerHits++;
            return true;
        }
        return false;
    }
    public boolean makeOpponentMove(Coord coord){
        if (playerBoard[coord.row][coord.col]) {
            opponentHits++;
            return true;
        }
        return false;
    }
    public boolean checkOpponentShipSunk(Coord coord){
        int index = 0;

        for(int i = 0; i < opponentShipCoordArray.size(); i++ ){
//            System.out.println("curr array: " + opponentShipCoordArray.get(i));
//            System.out.println("curr copy: " + opponentShipCoordArrayCopy.get(i));
            if(opponentShipCoordArray.get(i).contains(coord.toString())){
//                System.out.println("contains: " + coord.toString());
                opponentShipCoordArray.get(i).remove(coord.toString());
                if(opponentShipCoordArray.get(i).isEmpty()){
                    lastShipSunk = opponentShipCoordArrayCopy.get(index);
//                    System.out.println("last: " + lastShipSunk);
                    return true;
                }
            }
            index++;
        }
        return false;
    }
    public boolean checkPlayerShipSunkAnimation(Coord coord){
        int index = 0;
        for(int i = 0; i < playerShipCoordArray.size(); i++){
            if(playerShipCoordArray.get(i).contains(coord.toString())){
                playerShipCoordArray.get(i).remove(coord.toString());
                if(playerShipCoordArray.get(i).isEmpty()){
                    lastShipSunk2 = playerShipCoordArrayCopy.get(index);
                    playerShipCoordArray.remove(index);
                    return true;
                }
            }
            index++;
        }
        return false;
    }
    public String checkGameOver(){
//        System.out.println("playerhits: " + playerHits);
//        System.out.println("opHits: " + opponentHits);
        if (playerHits == 17)
            return "player";
        else if (opponentHits == 17)
            return "opponent";
        return "none";
    }
    public void setShipCoordArrays(ArrayList<ArrayList<String>> playerShipCoordArray, ArrayList<ArrayList<String>> opponentShipCoordArray){
        this.playerShipCoordArray = playerShipCoordArray;
        this.opponentShipCoordArray = opponentShipCoordArray;
        opponentShipCoordArrayCopy = new ArrayList<>();
        for (ArrayList<String> coordList : opponentShipCoordArray) {
            opponentShipCoordArrayCopy.add(new ArrayList<>(coordList));
        }
        playerShipCoordArrayCopy = new ArrayList<>();
        for (ArrayList<String> coordList : playerShipCoordArray) {
            playerShipCoordArrayCopy.add(new ArrayList<>(coordList));
        }
//        System.out.println("duping");
//        System.out.println(opponentShipCoordArrayCopy.get(0));
    }

    public ArrayList<String> getLastShipSunk(){
//        System.out.println("last ship sunk: " + lastShipSunk);
        return new ArrayList<>(lastShipSunk);
    }
    public ArrayList<String> getLastShipSunk2(){
//        System.out.println("last ship sunk: " + lastShipSunk2);
        return new ArrayList<>(lastShipSunk2);
    }

    public void setOpponentBoard(boolean[][] opponentBoard){
        this.opponentBoard = opponentBoard;
    }
    public void setPlayerBoard(boolean[][] playerBoard){
        this.playerBoard = playerBoard;
    }
    public void setYourShipCoordArray(ArrayList<ArrayList<String>> yourShipCoordArray){
        this.playerShipCoordArray = yourShipCoordArray;
        playerShipCoordArrayCopy = new ArrayList<>();
        for (ArrayList<String> coordList : yourShipCoordArray) {
            playerShipCoordArrayCopy.add(new ArrayList<>(coordList));
        }
    }
    public void setOpponentShipCoordArray(ArrayList<ArrayList<String>> opponentShipCoordArray){
        this.opponentShipCoordArray = opponentShipCoordArray;
        opponentShipCoordArrayCopy = new ArrayList<>();
        for (ArrayList<String> coordList : opponentShipCoordArray) {
            opponentShipCoordArrayCopy.add(new ArrayList<>(coordList));
        }
    }
}
