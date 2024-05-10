import java.util.ArrayList;
import java.util.Random;

public class AI {
    /*
     * This class is responsible for the AI of the Battleship game. It will be able to play the game
     * and make decisions based on the current state of the game.
     */
    // array of randomly generated names for the bot (puns on the word "bot" and words of that nature)
    private static final String[] botNames = {"Botimus Prime", "Botman", "Botzilla"};
    private ArrayList<ArrayList<String>> aiShipCoordArray = new ArrayList<>();
    private ArrayList<ArrayList<String>> aiShipCoordArrayCopy = new ArrayList<>();
    private ArrayList<ArrayList<String>> playerShipCoordArray = new ArrayList<>();

    private boolean[][] board;
//    boolean lastHit;
    private String name;
    Coord lastHit;
    Coord firstHit;

    ArrayList<String> hitCoords = new ArrayList<String>();

    Random rand = new Random();
    ArrayList<Coord> answers = new ArrayList<Coord>();

    private double percentHit = 0.0;
    private double percentIfHit = 0.0;
    private ArrayList<String> moves = new ArrayList<String>();

    // call constructor then call getBoard to get the board, then set board to opponent's board
    public AI(String difficultyLevel) {
        board = new boolean[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = false;
            }
        }
        switch (difficultyLevel) {
            case "easy":
                percentHit = 0.15;
                percentIfHit = 0.6;
                break;
            case "medium":
                // medium bot
                percentHit = 0.3;
                percentIfHit = 0.70;

                break;
            case "impossible":
                // impossible bot
                percentHit = 0.95;
                percentIfHit = 0.95;

                break;
            default:
                break;
        }
        // generate a random name for the bot
        int nameIndex = rand.nextInt(botNames.length);
        name = botNames[nameIndex];

        // place the bot's ships
        placeShips();
        // return the bot's board
        //print the board
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                if (board[i][j]){
                    System.out.print("1 ");
                }
                else {
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
    }
    public ArrayList<String> pickRandomMove() {
        moves.clear();
        attemptHit();
        if(!playerShipCoordArray.isEmpty())
            moves.add(returnMiss().toString());
        return moves;
    }

    private void attemptHit() {
        while (!playerShipCoordArray.isEmpty() && rand.nextDouble() < percentHit) {
            int randomMoveOuter = rand.nextInt(playerShipCoordArray.size());
            ArrayList<String> innerArr = playerShipCoordArray.get(randomMoveOuter);

            while (!innerArr.isEmpty() && rand.nextDouble() < percentIfHit) {
                int randomMoveInner = rand.nextInt(innerArr.size());
                String move = innerArr.get(randomMoveInner);
                moves.add(move);
                hitCoords.add(move);
                innerArr.remove(randomMoveInner);
            }

            // If the current inner array is empty after the hits, remove it from the playerShipCoordArray
            if (innerArr.isEmpty()) {
                playerShipCoordArray.remove(randomMoveOuter);
            }

            // Reduce the chance to continue hitting different inner arrays after a successful series of hits
            percentHit = percentIfHit;
        }
    }
    public Coord returnMiss(){
       Coord randomMove = new Coord(rand.nextInt(0, 10), rand.nextInt(0, 10));
         while (hitCoords.contains(randomMove.toString())){
              randomMove = new Coord(rand.nextInt(0, 10), rand.nextInt(0, 10));
         }
         return randomMove;
    }
    private void placeShips() {
        // place the bot's ships randomly
        //TODO: actually place the ships (i think i did this)
        for (int i = 2; i <= 5; i++){
            placeRandomly(i);
        }
        placeRandomly(3);
    }
    private void placeShip(int row, int column, int shipSize, int orientation) {
        // make a move based on the last move
        ArrayList<String> shipCoords = new ArrayList<String>();
        String coord = "";
        if (orientation == 0) {
            for (int i = 0; i < shipSize; i++) {
                board[row + i][column] = true;
                coord = "{" + Integer.toString(row + i) + ", "+ Integer.toString(column) + "}";
                shipCoords.add(coord);
            }
        }
        else {
            for (int i = 0; i < shipSize; i++) {
                board[row][column + i] = true;
                coord = "{" + Integer.toString(row) + ", "+ Integer.toString(column + i) + "}";
                shipCoords.add(coord);
            }
        }
        aiShipCoordArray.add(shipCoords);;
    }
    private void placeRandomly(int shipSize){
        boolean placed = false;
        int orientation = 0; // 0 = horizontal, 1 = vertical
        int randomRow = 0;
        int randomColumn = 0;
        while (!placed){
            orientation = rand.nextInt(2);
            randomRow = rand.nextInt(11 - shipSize);
            randomColumn = rand.nextInt(11 - shipSize);
            if (orientation == 0){
                for (int j = 0; j < shipSize; j++){
                    if (board[randomRow + j][randomColumn]){
                        placed = false;
                        break;
                    }
                    else {
                        placed = true;
                    }
                }
            }
            else {
                for (int j = 0; j < shipSize; j++){
                    if (board[randomRow][randomColumn + j]){
                        placed = false;
                        break;
                    }
                    else {
                        placed = true;
                    }
                }
            }
            if (placed){
                placeShip(randomRow, randomColumn, shipSize, orientation);
            }
        }

    }
    public boolean[][] getBoard() {
        return board;
    }
    public void setBoard(boolean[][] board) {
        this.board = board;
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                if (board[i][j]){
                    answers.add(new Coord(i, j));
                }
            }
        }
    }
    public String getName() {
        return name;
    }
    public ArrayList<ArrayList<String>> getAiShipCoordArray(){
            return aiShipCoordArray;
        }
    public void setPlayerShipCoordArray(ArrayList<ArrayList<String>> playerShipCoordArray){
        this.playerShipCoordArray = playerShipCoordArray;
        aiShipCoordArrayCopy = new ArrayList<>();
        for (ArrayList<String> coordList : playerShipCoordArray) {
            aiShipCoordArrayCopy.add(new ArrayList<>(coordList));
            hitCoords.addAll(coordList);
        }
    }
}
