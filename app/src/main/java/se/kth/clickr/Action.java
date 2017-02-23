package se.kth.clickr;

/**
 * Created by ElectricCookie on 24.08.2016.
 */
public class Action {
    public String type;
    public Object payload;

    public Action(String type,Object payload){
        this.type = type;
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public Object getPayload() {
        return payload;
    }
}
