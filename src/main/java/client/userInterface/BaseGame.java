package client.userInterface;


import gameLogic.IGameExecutor;
import gameLogic.IUIExecutor;
import gameLogic.exceptions.BoatInvalidException;
import gameLogic.exceptions.FireInvalidException;
import gameLogic.exceptions.PlayerNotTurnException;
import gameLogic.exceptions.PlayerStartException;
import helpers.CollideHelper;
import models.*;

import java.util.Collection;

public abstract class BaseGame implements ISeaBattleGame{

    protected IGameExecutor localPlayer;
    protected ISeaBattleEnhancedGUI enhancedGUI;

    public void setEnhancedGUI(ISeaBattleEnhancedGUI enhancedGUI) {
        this.enhancedGUI = enhancedGUI;
    }

    public abstract void setGUIExecutor(IUIExecutor GUIExecutor);

    public int registerPlayer(String name, ISeaBattleGUI application, boolean singlePlayerMode) {
        return 0;
    }

    public abstract IGameExecutor getPlayer(int playerNr);

    @Override
    public boolean placeShipsAutomatically(int playerNr) {
        removeAllShips(playerNr);
        placeShip(playerNr, ShipType.AIRCRAFTCARRIER, 0,0, true);
        placeShip(playerNr, ShipType.BATTLESHIP, 0,1, true);
        placeShip(playerNr, ShipType.CRUISER, 0,2, true);
        placeShip(playerNr, ShipType.MINESWEEPER, 0,3, true);
        placeShip(playerNr, ShipType.SUBMARINE, 0,4, true);

        return true;
    }

    @Override
    public boolean placeShip(int playerNr, ShipType shipType, int bowX, int bowY, boolean horizontal) {
        IGameExecutor player = getPlayer(playerNr);
        Ship ship = new Ship(shipType);
        ship.setX(bowX);
        ship.setY(bowY);
        if (horizontal) {
            ship.setOrientation(Orientation.Horizontal);
        } else {
            ship.setOrientation(Orientation.Vertical);
        }
        try {
            player.PlaceShip(ship);
        } catch (Exception e){
            enhancedGUI.showMessage(e.getMessage());
        }

        return true;
    }

    @Override
    public boolean removeShip(int playerNr, int posX, int posY) {
        IGameExecutor player = getPlayer(playerNr);
        return removeShip(playerNr, (new CollideHelper()).getShip(posX, posY, player.GetLocalGrid()));
    }

    private boolean removeShip(int playerNr, Ship ship) {
        IGameExecutor player = getPlayer(playerNr);
        if(ship != null){
            try {
                player.RemoveShip(ship);
            } catch (PlayerStartException e) {
                e.printStackTrace();
            } catch (BoatInvalidException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public boolean removeAllShips(int playerNr) {
        IGameExecutor player = getPlayer(playerNr);

        Ship[] array = new Ship[player.GetLocalGrid().getShips().size()];
        int i = 0;
        for (Ship ship:  player.GetLocalGrid().getShips()) {
            array[i++] = ship;
        }

        for(Ship ship: array){
            removeShip(playerNr,ship);
        }

        return true;
    }

    @Override
    public boolean notifyWhenReady(int playerNr) {
        IGameExecutor player = getPlayer(playerNr);
        Collection<Ship> ships = player.GetLocalGrid().getShips();


        //todo Zet de check als alle ships wel geplaats zijn, in GameExecutor

        if (ships.size() == 5){
            try {
                player.RequestFireState();
            } catch (FireInvalidException e) {
                e.printStackTrace();
            } catch (PlayerStartException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ShotType fireShotPlayer(int playerNr, int posX, int posY) {
        IGameExecutor player = getPlayer(playerNr);
        try {
            player.FireOnGridOpponent(new Fire(posX, posY));
        } catch (PlayerStartException e) {
            e.printStackTrace();
        } catch (PlayerNotTurnException e) {
            enhancedGUI.showMessage("Its not your turn to shoot yet");
        }
        return ShotType.MISSED;
    }

    @Override
    public ShotType fireShotOpponent(int playerNr) {
        return null;
    }

    @Override
    public boolean startNewGame(int playerNr) {
        removeAllShips(0);

        return true;
    }
}
