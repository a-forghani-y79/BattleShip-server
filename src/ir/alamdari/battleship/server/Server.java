package ir.alamdari.battleship.server;

import ir.alamdari.battleship.model.Battle;
import ir.alamdari.battleship.model.Player;
import ir.alamdari.battleship.model.Ship;
import ir.alamdari.battleship.model.comminucations.Request;
import ir.alamdari.battleship.model.comminucations.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    static ServerSocket serverSocket;

    static Player playerOne = null;

    static Player playerTwo = null;

    static int[][] playerOneArea;

    static int[][] playerTwoArea;

    static Battle battle;

    static int turn = 1;


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int port = 9876;
        serverSocket = new ServerSocket(port);
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;

        Socket socket;
        while (true) {

            socket = serverSocket.accept();
            socket.setKeepAlive(true);

            objectInputStream =
                    new ObjectInputStream(socket.getInputStream());

            Request request = (Request) objectInputStream.readObject();


            Response response = getResponseForRequest(request);

            objectOutputStream =
                    new ObjectOutputStream(socket.getOutputStream());

            objectOutputStream.writeObject(response);


            objectOutputStream.close();
            objectInputStream.close();
        }

//        System.out.println("ir.alamdari.battleship.server shutdown");
//        serverSocket.close();


    }

    private static Response getResponseForRequest(Request request) {

        Response response
                = new Response();
        response.setTo(request.getFrom());
        response.setResponseType(request.getRequestType());
        switch (request.getRequestType().getCode()) {
            case 1:
                response.setData(true);
                break;
            case 2:
                if (battle == null)
                    battle = new Battle();


                List<Ship> ships = new ArrayList<>();
                ships.add(new Ship(1, 4, "Ship F4", "#FF5733"));
                ships.add(new Ship(2, 3, "Ship F3", "#05FF2B"));
                ships.add(new Ship(3, 5, "Ship F5", "#057BFF"));
                ships.add(new Ship(4, 2, "Ship F2", "#9305FF"));
                ships.add(new Ship(5, 6, "Ship F6", "#FF05B1"));

                battle.setShips(ships);
                response.setData(ships);

                break;
            case 3:
                response.setData(false);
                if (playerOne == null) {
                    playerOne = request.getFrom();
                    playerOneArea = (int[][]) request.getData();
                    response.setData(playerOne != null && playerOneArea != null);
                    if (battle == null)
                        battle = new Battle();

                    battle.setPlayerOne(playerOne);
                    battle.setPlayerOneArea(playerOneArea);
                } else if (playerTwo == null) {
                    playerTwo = request.getFrom();
                    playerTwoArea = (int[][]) request.getData();
                    response.setData(playerTwo != null && playerTwoArea != null);
                    battle.setPlayerTwo(playerTwo);
                    battle.setPlayerTwoArea(playerTwoArea);
                }
                break;
            case 4:
                response.setData(playerOne != null && playerTwo != null);
                break;

            case 5:
                //IS_MY_TURN(5),
                response.setData(false);
                if (request.getFrom().getName().equals(playerOne.getName()) && turn == 1)
                    response.setData(true);
                else if (request.getFrom().getName().equals(playerTwo.getName()) && turn == 2)
                    response.setData(true);
                break;
            case 6:
                //GET_WINNER(6),
                response.setData(getWinner(battle));
                break;
            case 7:

                //GET_LAST_STATE_BATTLE(7)
                response.setData(battle);
                break;
            case 8:
                //SHOOT(8)
                int[] xy = (int[]) request.getData();
                shoots(request.getFrom(), xy[0], xy[1], battle);
                response.setData(request.getData());
                break;
            default:
                response.setData(null);
        }

        return response;

    }

    private static void shoots(Player shooter, int x, int y, Battle battle) {
        if (battle.getPlayerOne().getName().equals(shooter.getName())) {
            if (battle.getPlayerTwoArea()[x][y] != 0) {
                battle.getPlayerTwoArea()[x][y] = -1;
                turn = 1;
            } else {
                battle.getPlayerTwoArea()[x][y] = -2;

                turn = 2;
            }
        } else if (battle.getPlayerTwo().getName().equals(shooter.getName()))
            if (battle.getPlayerOneArea()[x][y] != 0) {
                battle.getPlayerOneArea()[x][y] = -1;
                turn = 2;
            } else {
                battle.getPlayerOneArea()[x][y] = -2;
                turn = 1;
            }

    }
    private static void printArray(int[][] array) {
        System.out.println("-------------------");
        for (int[] ints : array) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
        System.out.println("-------------------");
    }
    private static Player getWinner(Battle battle){
        int countUpperZeroOne =0;
        for (int[] ints : battle.getPlayerOneArea()) {
            for (int anInt : ints) {
                if (anInt>0)
                    countUpperZeroOne++;
            }
        }
        int countUpperZeroTwo =0;
        for (int[] ints : battle.getPlayerTwoArea()) {
            for (int anInt : ints) {
                if (anInt>0)
                    countUpperZeroTwo++;
            }
        }

        if (countUpperZeroOne==0)
            return battle.getPlayerTwo();

        if (countUpperZeroTwo==0)
            return battle.getPlayerOne();

        return null;
    }

}
