package userInterface;

import communication.ICommunication;
import communication.SinglePlayerCommunication;
import game.GameExecutor;
import game.IUIExecutor;
import game.ShipGrid;
import helpers.CollideHelper;
import models.Orientation;
import models.Ship;
import models.ShipType;
import models.ShotType;

import java.util.Collection;

public class SeaBattleGame implements ISeaBattleGame {

    private GameExecutor player0;
    private GameExecutor player1;


    public void setGUIExecutor(IUIExecutor GUIExecutor) {
        player1.setGUIExecutor( GUIExecutor );
        player0.setGUIExecutor( GUIExecutor );
    }

    public SeaBattleGame(){
        SinglePlayerCommunication communication0 = new SinglePlayerCommunication();
        player0 = new GameExecutor(communication0);

        SinglePlayerCommunication communication1 = new SinglePlayerCommunication();
        communication1.setOtherPlayer(player0);
        player1 = new GameExecutor(communication1);

        communication0.setOtherPlayer(player1);
    }

    public GameExecutor getPlayer(int playerNr){
        GameExecutor player;
        if (playerNr == 0) {
            player = player0;
        } else if(playerNr == 1){
            player = player1;
        } else {
            throw new IllegalArgumentException("Playernumber cannot be more then 1 or be negative");
        }
        return player;
    }

    public int registerPlayer(String name, ISeaBattleGUI application, boolean singlePlayerMode) {
        return 0;
    }

    public boolean placeShipsAutomatically(int playerNr) {
        return false;
    }

    public boolean placeShip(int playerNr, ShipType shipType, int bowX, int bowY, boolean horizontal) {
        GameExecutor player = getPlayer(playerNr);
        Ship ship = new Ship(shipType);
        ship.setX(bowX);
        ship.setY(bowY);
        if (horizontal) {
            ship.setOrientation(Orientation.Horizontal);
        } else {
            ship.setOrientation(Orientation.Vertical);
        }
        player.PlaceBoat(ship);

        return true;
    }

    public boolean removeShip(int playerNr, int posX, int posY) {
        GameExecutor player = getPlayer(playerNr);

        Ship ship = (new CollideHelper()).getShip(posX, posY, player.GetLocalGrid());
        if(ship != null){
            player.GetLocalGrid().removeShip(ship);
        }

        return ship != null;
    }

    public boolean removeAllShips(int playerNr) {
        GameExecutor player = getPlayer(playerNr);
        player.GetLocalGrid().removeAllShips();

        return true;
    }

    public boolean notifyWhenReady(int playerNr) {
        return false;
    }

    public ShotType fireShotPlayer(int playerNr, int posX, int posY) {
        return null;
    }

    public ShotType fireShotOpponent(int playerNr) {
        return null;
    }

    public boolean startNewGame(int playerNr) {
        return false;
    }
}