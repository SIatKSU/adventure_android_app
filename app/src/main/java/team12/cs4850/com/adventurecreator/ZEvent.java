package team12.cs4850.com.adventurecreator;

import java.util.List;

/**
 * Created by siatk on 3/6/2018.
 */

//only leaf nodes can be deleted?


public class ZEvent {

    static int eventIdCounter;
    int eventId;                //1, 2, 3, 4.     0 = starting node.
    String ZEventKey;           //for convenience - the entry Key in Firebase

    String title;           //optional event title

    String description;
    int eventType = 0;          //default, basic ZEvent

    String prevEventKey;          //to go back

    List<String> actions;         //e.g. "go left", "go right", "go center"
    List<Integer> nextEventIds;    //e.g. leftNode, rightNode, centerNode


    //int level = 0;    // level only makes sense in the context of TreeAdventure -
                        // where you can only add to layer immediately below current level


    public ZEvent () {
    }

    public ZEvent(String zEventKey, String description) {
        ++eventIdCounter;
        eventId = eventIdCounter;           //start at 1.
        this.ZEventKey = zEventKey;
        this.description = description;
    }
}
