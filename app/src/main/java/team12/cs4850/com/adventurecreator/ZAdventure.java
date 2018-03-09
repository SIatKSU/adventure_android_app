package team12.cs4850.com.adventurecreator;

import java.util.List;

/**
 * Created by siatk on 3/3/2018.
 */

//storage class for an Adventure in Firebase
public class ZAdventure {
    public String userid;
    public String adventureKey; //for convenience - the String under which it is stored in Firebase
    public String adventureName;
    public String adventureDescription;
    public long dateModified;    //long currDate = System.currentTimeMillis();     //use mDate = Date(currDate) to get it back.
    public int adventureType;

    public List<ZEvent> events = null;
    public int eventCounter = 0;


    public ZAdventure() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public ZAdventure(String userid, String adventureName, String adventureDescription, String adventureKey, int adventureType) {
        this.userid = userid;
        this.adventureKey = adventureKey;
        this.adventureName = adventureName;
        this.adventureDescription = adventureDescription;
        this.dateModified = System.currentTimeMillis();
        this.adventureType = adventureType;
    }

    public ZEvent AddNewEvent(String title, String description) {
            ++eventCounter;  //start at one
            return new ZEvent (title, description, eventCounter);
    }

}
