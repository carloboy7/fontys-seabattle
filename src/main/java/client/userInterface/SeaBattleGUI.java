package client.userInterface;

import game.IUIExecutor;
import models.*;

import static models.HitType.Collided;
import static models.HitType.Miss;

public class SeaBattleGUI implements IUIExecutor {

    private ISeaBattleEnhancedGUI battleGUI;
    public SeaBattleGUI(ISeaBattleEnhancedGUI gui){
        battleGUI = gui;
    }

    @Override
    public void placeTempShipLocal(ShipType shipType, int bowX, int bowY, boolean horizontal) {

    }

    @Override
    public void placeShipLocal(Ship ship) {
        for (int i = 0; i < ship.getLength(); i++) {
            if(ship.getOrientation() == Orientation.Horizontal) {
                battleGUI.showSquarePlayer(0, ship.getX() + i, ship.getY(), SquareState.SHIP);
            } else {
                battleGUI.showSquarePlayer(0, ship.getX(), ship.getY() + i, SquareState.SHIP);
            }
        }
    }

    @Override
    public void removeShipLocal(Ship ship) {
        for (int i = 0; i < ship.getLength(); i++) {
            if(ship.getOrientation() == Orientation.Horizontal) {
                battleGUI.showSquarePlayer(0, ship.getX() + i, ship.getY(), SquareState.WATER);
            } else {
                battleGUI.showSquarePlayer(0, ship.getX(), ship.getY() + i, SquareState.WATER);
            }
        }
    }

    @Override
    public void fireShotLocal(Hit hit) {
        System.out.println("LOCAL");
        HitType hitType = hit.getHitType();
        if (hitType == Miss) {
            battleGUI.showSquarePlayer(0, hit.getX(), hit.getY(), SquareState.SHOTMISSED);
        }
        else if (hitType == Collided) {
            battleGUI.showSquarePlayer(0, hit.getX(), hit.getY(), hit.getSunk()? SquareState.SHIPSUNK : SquareState.SHOTHIT);
        }
    }

    @Override
    public void fireShotOpponent(Hit hit) {
        System.out.println("OPPONENT");
        HitType hitType = hit.getHitType();
        if (hitType == Miss) {
            battleGUI.showSquareOpponent(0, hit.getX(), hit.getY(), SquareState.SHOTMISSED);
            battleGUI.opponentFiresShot(0, ShotType.MISSED);
        }
        else if (hitType == Collided) {
            battleGUI.showSquareOpponent(0, hit.getX(), hit.getY(), hit.getSunk()? SquareState.SHIPSUNK : SquareState.SHOTHIT);
            battleGUI.opponentFiresShot(0, ShotType.HIT);
        }

    }

    @Override
    public void fireTempShotOpponent(int posX, int posY) {

    }

    @Override
    public void removeTempShotOpponent() {

    }

    @Override
    public void removeTempShipLocal(int posX, int posY) {

    }

    //tegenstander is geregistreerd, plaats nu al je boten
    @Override
    public void gameReady(String opponentName) {
        resetUI();
    }

    //tegenstander en jij zijn klaar met boten plaatsen, begin met vuren
    public void fireReady(){

    }

    public void resetUI(){
        for (int i = 0; i < 10; i++){
            for (int j = 0; j <10; j++){
                battleGUI.showSquareOpponent(0, i, j, SquareState.WATER);
                battleGUI.showSquarePlayer(0, i, j, SquareState.WATER);
            }
        }
    }

    @Override
    public void gameEnded(String winner) {


        resetUI();

        battleGUI.showMessage(winner + " heeft gewonnen!");
        battleGUI.gameEnded();

    }
}