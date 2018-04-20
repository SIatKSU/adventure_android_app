package team12.cs4850.com.adventurecreator;

import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by siatk on 3/3/2018.
 */

public class ZAdventureAdapter extends RecyclerView.Adapter<ZAdventureAdapter.ZAdventureHolder> {

    private List<ZAdventure> zAdventureList;
    int selected_position;
    private int selectedColor;

    public class ZAdventureHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView mName, mDescription, mDate;
        private CardView adventureCardView;

        public ZAdventureHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.text1);
            mDescription = itemView.findViewById(R.id.text2);
            mDate = itemView.findViewById(R.id.text3);
            adventureCardView = itemView.findViewById(R.id.adventureCardView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            selectedColor = ContextCompat.getColor(itemView.getContext(), R.color.selectedAdventureColor);
        }

        @Override
        public void onClick(View itemView) {
/*            Intent i = new Intent(itemView.getContext(), EditAdventureActivity.class);
            ZAdventure zAdventure = zAdventureList.get(getLayoutPosition());
            i.putExtra("zAdventure", (Parcelable) zAdventure);
            itemView.getContext().startActivity(i);*/

            // Below line is just like a safety check, because sometimes holder could be null,
            // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

            // Updating old as well as new positions
            notifyItemChanged(selected_position);   //update old position
            selected_position = getAdapterPosition();
            notifyItemChanged(selected_position);   //update new position
        }

        @Override
        public boolean onLongClick(View itemView) {
            //Toast.makeText(itemView.getContext(), "LONG CLICK position = " + getLayoutPosition(), Toast.LENGTH_SHORT).show();
            return true;
        }

    }

    public ZAdventureAdapter(List<ZAdventure> zAdventureList) {
        this.zAdventureList = zAdventureList;
        this.selected_position = zAdventureList.size() - 1;       //-1 is RecyclerView.NO_POSITION
    }

    @Override
    public ZAdventureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adventure_list_item, parent, false);
        return new ZAdventureHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ZAdventureHolder holder, int position) {
        ZAdventure currAdventure = zAdventureList.get(position);
        holder.mName.setText(currAdventure.adventureName);
        holder.mDescription.setText(currAdventure.adventureDescription);
        Date dateModified = new Date(currAdventure.dateCreated);
        SimpleDateFormat spf= new SimpleDateFormat("MMM dd yyyy");
        holder.mDate.setText(spf.format(dateModified));

        //holder.itemView.setBackgroundColor(selected_position == position ? selectedColor : Color.TRANSPARENT);
        holder.adventureCardView.setCardBackgroundColor(selected_position == position ? selectedColor : Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return zAdventureList.size();
    }

    public void updateList(List<ZAdventure> zAdventureList) {
        this.zAdventureList = zAdventureList;
        this.selected_position = zAdventureList.size() - 1;       //-1 is RecyclerView.NO_POSITION
        notifyDataSetChanged();
    }



}
