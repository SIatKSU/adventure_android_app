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

        private TextView tvAction, tvTitle, tvDescription;

        public ZChildEventHolder(View itemView) {
            super(itemView);
            tvAction = itemView.findViewById(R.id.tvAction);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View itemView) {
            Intent i = new Intent(itemView.getContext(), EditLinkToNextEventActivity.class);
            MyBaseActivity.isNewChildEvent = false;
            MyBaseActivity.currChildEvent = zChildEventList.get(getLayoutPosition());
            itemView.getContext().startActivity(i);
        }

        @Override
        public boolean onLongClick(View itemView) {
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
        try {
            String actionText = zEvent.nextActions.get(position);
            holder.tvAction.setText(actionText);

            String titleText = Integer.toString(thisEvent.eventId) + " " + thisEvent.title;
            holder.tvTitle.setText(titleText);
            holder.tvDescription.setText(thisEvent.description);
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
