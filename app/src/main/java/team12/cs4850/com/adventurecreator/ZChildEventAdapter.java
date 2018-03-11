package team12.cs4850.com.adventurecreator;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siatk on 3/3/2018.
 */

public class ZChildEventAdapter extends RecyclerView.Adapter<ZChildEventAdapter.ZChildEventHolder> {

    private ZEvent zEvent;
    private List<ZEvent> zChildEventList;

    public class ZChildEventHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView tvText1, tvText2, tvText3;

        public ZChildEventHolder(View itemView) {
            super(itemView);
            tvText1 = itemView.findViewById(R.id.tvText1);
            tvText2 = itemView.findViewById(R.id.tvText2);
            tvText3 = itemView.findViewById(R.id.tvText3);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View itemView) {
            Intent i = new Intent(itemView.getContext(), EditLinkToNextEventActivity.class);
            //i.putExtra("eventId", zEventList.get(getLayoutPosition()).eventId);
            MyBaseActivity.isNewChildEvent = false;
            MyBaseActivity.currChildEvent = zChildEventList.get(getLayoutPosition());
            itemView.getContext().startActivity(i);
        }

        @Override
        public boolean onLongClick(View itemView) {
            //Toast.makeText(itemView.getContext(), "LONG CLICK position = " + getLayoutPosition(), Toast.LENGTH_SHORT).show();
            return true;
        }

    }

    public ZChildEventAdapter(ZEvent thisEvent) {
        this.zEvent = thisEvent;
        this.zChildEventList = new ArrayList<>();

        ZEvent tempEvent;

        if (thisEvent.nextEventIds == null) {
            thisEvent.nextActions = new ArrayList<>();
            thisEvent.nextEventIds = new ArrayList<>();
        }

        for (Integer id : thisEvent.nextEventIds) {
            //find the event matching nextEventsId, and add it to the childEvents array
            tempEvent = MyBaseActivity.currAdventure.getEventFromEventListUsingEventId(id);
            if (tempEvent != null) {
                zChildEventList.add(tempEvent);
            }
        }

        //this.zEventList = zEventList;
    }

    @Override
    public ZChildEventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.childevent_list_item, parent, false);
        return new ZChildEventHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ZChildEventHolder holder, int position) {
        ZEvent thisEvent = zChildEventList.get(position);
        //thisEvent.eventId
        try {
            String text1 = "\"" + zEvent.nextActions.get(position) + "\" -> "
                    + Integer.toString(thisEvent.eventId) + " " + thisEvent.title;
            holder.tvText1.setText(text1);
            String text2 = MyBaseActivity.eventTypes.get(thisEvent.eventType)
                    + ".  " + thisEvent.description;
            holder.tvText2.setText(text2);
        }
        catch (Exception ex) {
            String exceptString = ex.getMessage();
        }
    }

    @Override
    public int getItemCount() {
        return zChildEventList.size();
    }

    public void updateList(ZEvent zEvent) {
        this.zEvent = zEvent;
        notifyDataSetChanged();
    }



}
