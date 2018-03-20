package communication;

import models.Fire;
import models.Hit;
import models.HitType;

public class FirePackage extends CommunicationPackage {
    public FirePackage(Fire fire) {
        super(CommunicationAction.PlaceBoat, serialize(fire));
    }
    private static String serialize(Fire fire){
        return fire.getX() +":"+ fire.getY();
    }
    public static Fire unserialize(String data){
        String[] split = data.split(":");
        return new Fire(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }
}
