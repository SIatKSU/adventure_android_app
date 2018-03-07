package team12.cs4850.com.adventurecreator;

import java.util.List;

/**
 * Created by siatk on 3/6/2018.
 */

//only leaf nodes can be deleted?


public class ZEvent {

    static int eventCounter;

    String ZEventKey;
    String eventId;  //could be 1,2,3,4,5, etc
    String description;

    String prevEventKey;          //to go back

    List<String> actions;         //e.g. "go left", "go right", "go center"
    List<String> nextEventKeys;    //e.g. leftNode, rightNode, centerNode

    public ZEvent () {
    }

    public ZEvent(String ZEventKey, String eventId, String description) {
        this.ZEventKey = ZEventKey;
        this.eventId = eventId;
        this.description = description;
    }
}
