package team12.cs4850.com.adventurecreator;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by siatk on 3/3/2018.
 */

public class ZEventAdapter extends RecyclerView.Adapter<ZEventAdapter.ZEventHolder> {

    private List<ZEvent> zEventList;

    public class ZEventHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView tvEventId, tvEventTitle, tvEventDescription;
        private ImageButton btnEdit, btnDelete;

        public ZEventHolder(View itemView) {
            super(itemView);
            tvEventId = itemView.findViewById(R.id.eventId);
            tvEventTitle = itemView.findViewById(R.id.eventTitle);
            tvEventDescription = itemView.findViewById(R.id.eventDescription);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View itemView) {
            Intent i = new Intent(itemView.getContext(), EditEventActivity.class);
            //ZEvent zEvent = zEventList.get(getLayoutPosition());
            i.putExtra("eventListPosition", getLayoutPosition());
            itemView.getContext().startActivity(i);
        }

        @Override
        public boolean onLongClick(View itemView) {
            //Toast.makeText(itemView.getContext(), "LONG CLICK position = " + getLayoutPosition(), Toast.LENGTH_SHORT).show();
            return true;
        }

    }

    public ZEventAdapter(List<ZEvent> zEventList) {
        this.zEventList = zEventList;
    }

    @Override
    public ZEventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_item, parent, false);
        return new ZEventHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ZEventHolder holder, int position) {
        ZEvent currEvent = zEventList.get(position);
        try {
            holder.tvEventId.setText(Integer.toString(currEvent.eventId));
            holder.tvEventTitle.setText(currEvent.title);
            holder.tvEventDescription.setText(currEvent.description);
        }
        catch (Exception ex) {
            String exceptString = ex.getMessage();
        }
    }

    @Override
    public int getItemCount() {
        return zEventList.size();
    }

    public void updateList(List<ZEvent> ZEventList) {
        this.zEventList = ZEventList;
        notifyDataSetChanged();
    }



}
