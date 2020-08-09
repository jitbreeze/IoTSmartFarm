package com.example.smartfarmhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.Exclude;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

//김지수 작성
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    ArrayList<CardItem> items = new ArrayList<CardItem>();
    Context context;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ViewHolder holder, View view, int position);
    }

    public CardAdapter(ArrayList<CardItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void addItem(CardItem item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public CardItem getItem(int position) {
        return items.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView diaryDate;
        TextView diaryText;
        ImageView diaryPic;
        OnItemClickListener listener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            diaryDate = itemView.findViewById(R.id.diaryDate);
            diaryText = itemView.findViewById(R.id.diaryText);
            diaryPic = itemView.findViewById(R.id.diaryPic);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }
    }

    @NonNull
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ViewHolder holder, int position) {
        holder.diaryDate.setText(items.get(position).getDate());
        holder.diaryText.setText(items.get(position).getContent());
        Picasso.with(context).load(items.get(position).getImage())
                             .placeholder(R.drawable.ic_loading).fit()
                             .centerCrop()
                             .into(holder.diaryPic);
        holder.setOnItemClickListener(listener);
    }
}

//김지수 작성
class CardItem {
    public String date;
    public String content;
    public String image;
    public long timestamp;
    private String mkey;
    public String uid;

    public CardItem() {}

    public CardItem(String date, String content, String image, long timestamp, String uid) {
        this.date = date;
        this.content = content;
        this.image = image;
        this.timestamp = timestamp;
        this.uid = uid;
    }
    public String getDate() { return date; }
    public String getContent() {
        return content;
    }
    public String getImage() {
        return image;
    }
    @Exclude
    public String getkey() {return mkey;}
    @Exclude
    public void setkey(String key) { this.mkey = key; }

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
}
