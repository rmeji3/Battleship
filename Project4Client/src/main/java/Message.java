import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
    static final long serialVersionUID = 42L;

    private String message = null;
    private String sender = null;
    private String receiver = null;
    private String command = null;

    private String avatar = null;

    private boolean b;

    private boolean[][] matrix = null;
    private int number;

    private ArrayList<String> stringArrayList = null;

    private ArrayList<Coord> movesList = null;

    private ArrayList<ArrayList<String>> shipCoordArray;

    public Message(String command){
        this.command = command;
    }

    public Message(String command, String message){
        this.message = message;
        this.command = command;
    }

    public Message(String command, String message, String avatar){
        this.message = message;
        this.command = command;
        this.avatar = avatar;
    }
    public Message(String command, String message, int amount){
        this.message = message;
        this.command = command;
        this.number = amount;
    }

    public Message(String command, boolean[][] matrix, ArrayList<ArrayList<String>> shipCoordArray){
        this.matrix = matrix;
        this.command = command;
        this.shipCoordArray = shipCoordArray;
    }

    public Message(String command, boolean[][] matrix, ArrayList<ArrayList<String>> shipCoordArray, String sender, String receiver){
        this.matrix = matrix;
        this.command = command;
        this.sender = sender;
        this.receiver = receiver;
        this.shipCoordArray = shipCoordArray;
    }

    public Message(String command, ArrayList<String> stringArrayList){
        this.command = command;
        this.stringArrayList = stringArrayList;
    }

    public Message(String command, int number, boolean b){
        this.command = command;
        this.number = number;
        this.b = b;
    }

    public Message(String command, String message, String sender, String receiver){
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.command = command;
    }

    public Message(ArrayList<String> stringArrayList){
        this.stringArrayList = stringArrayList;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getCommand(){
        return command;
    }

    public ArrayList<String> getStringArrayList() {
        return stringArrayList;
    }

    public int getNumber() {
        return number;
    }

    public boolean[][] getMatrix() {
        return matrix;
    }

    public ArrayList<ArrayList<String>> getShipCoordArray(){return shipCoordArray;}

    public ArrayList<Coord> getMovesList() {
        return movesList;
    }

    public String getAvatar(){return avatar;}
    public boolean isB() {
        return b;
    }
}
