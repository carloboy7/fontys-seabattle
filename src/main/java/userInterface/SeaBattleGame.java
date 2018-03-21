package userInterface;

import communication.SinglePlayerCommunication;
import game.GameExecutor;
import game.IUIExecutor;
import helpers.CollideHelper;
import models.*;

import java.util.Collection;
import java.util.Random;

public class SeaBattleGame implements ISeaBattleGame {

    private GameExecutor localPlayer;
    private GameExecutor aiPlayer;


    public void setGUIExecutor(IUIExecutor GUIExecutor) {
        aiPlayer.setGUIExecutor( new EmptySeaBattleGUI() );
        localPlayer.setGUIExecutor( GUIExecutor );
    }

    public SeaBattleGame(){
        SinglePlayerCommunication communication0 = new SinglePlayerCommunication();
        localPlayer = new GameExecutor(communication0);

        SinglePlayerCommunication communication1 = new SinglePlayerCommunication();
        communication1.setOtherPlayer(localPlayer);
        aiPlayer = new GameExecutor(communication1);

        communication0.setOtherPlayer(aiPlayer);
        localPlayer.setGridSize(10,10);
        aiPlayer.setGridSize(10,10);
    }

    public GameExecutor getPlayer(int playerNr){
        GameExecutor player;
        if (playerNr == 0) {
            player = localPlayer;
        } else if(playerNr == 1){
            player = aiPlayer;
        } else {
            throw new IllegalArgumentException("Playernumber cannot be more then 1 or be negative");
        }
        return player;
    }

    public int registerPlayer(String name, ISeaBattleGUI application, boolean singlePlayerMode) {
        return 0;
    }

    public boolean placeShipsAutomatically(int playerNr) {
        removeAllShips(playerNr);
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
        GameExecutor player = getPlayer(playerNr);
        Collection<Ship> ships = player.GetLocalGrid().getShips();
        if (ships.size() == 5){
            return true;
        } else {
            return false;
        }
    }

    public ShotType fireShotPlayer(int playerNr, int posX, int posY) {
        GameExecutor player = getPlayer(playerNr);
        player.FireOpponent(new Fire(posX, posY));
        return ShotType.MISSED;
    }

    /**
     * Let the opponent fire a shot at the player's square.
     * This method is used in the single-player mode.
     * A shot is fired by the opponent using some AI strategy.
     * The result of the shot will be one of the following:
     * MISSED  - No ship was hit
     * HIT     - A ship was hit
     * SUNK    - A ship was sunk
     * ALLSUNK - All ships are sunk
     * @param playerNr identification of the player for which the opponent
     *                 will fire a shot
     * @return result of the shot
     */
    public ShotType fireShotOpponent(int playerNr) {
        aiPlayer.FireOpponent(new Fire(new Random(10).nextInt(), new Random(10).nextInt()));
        return ShotType.MISSED;
    }

    public boolean startNewGame(int playerNr) {
        return false;
    }
}
