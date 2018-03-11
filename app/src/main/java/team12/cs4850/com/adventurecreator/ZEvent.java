package team12.cs4850.com.adventurecreator;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siatk on 3/6/2018.
 */

//only leaf nodes can be deleted?


public class ZEvent {

    int eventId;                //1, 2, 3, 4.     0 = starting node.
    //String ZEventKey;           //for convenience - the entry Key in Firebase

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

/*    public ZEvent(String zEventKey, String title, String description) {
        ++eventIdCounter;
        eventId = eventIdCounter;           //start at 1.
        this.ZEventKey = zEventKey;
        this.description = description;
    }*/

    public ZEvent(String title, String description, int eventId) {
        this.eventId = eventId;           //start at 1.
        this.title = title;
        this.description = description;

        actions = new ArrayList<>();
        nextEventIds = new ArrayList<>();
    }

    @Exclude
    public int getTriggerWordsIndexFromChildEventId(int childEventId) {
        for (int i = 0; i < nextEventIds.size(); i++) {
            if (nextEventIds.get(i) == childEventId) {
                return i;
            }
        }
        return -1;
    }

    @Exclude
    public String getTriggerWordsFromChildEventId(int childEventId) {
        for (int i = 0; i < nextEventIds.size(); i++) {
            if (nextEventIds.get(i) == childEventId) {
                return actions.get(i);
            }
        }
        return null;
    }

}
