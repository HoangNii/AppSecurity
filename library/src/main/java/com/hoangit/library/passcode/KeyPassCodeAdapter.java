package com.hoangit.library.passcode;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hoangit.library.R;

import java.util.ArrayList;

public abstract class KeyPassCodeAdapter extends RecyclerView.Adapter<KeyPassCodeAdapter.ViewHolder> {

    private Context context;

    private ArrayList<Key> keys;

    protected KeyPassCodeAdapter(Context context, ArrayList<Key> keys) {
        this.context = context;
        this.keys = keys;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_key_input,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Key key = keys.get(position);
        holder.tvKey.setText(key.getNumber());
        holder.tvText.setText(key.getTvNumber());
        if(key.getNumber().equals("10")){
            holder.itemView.setVisibility(View.GONE);
        }else {
            holder.itemView.setVisibility(View.VISIBLE);
        }

        if(key.getNumber().equals("0")){
            holder.tvText.setVisibility(View.GONE);
        }else {
            holder.tvText.setVisibility(View.VISIBLE);
        }
        if(key.getNumber().equals("11")){
            holder.tvKey.setVisibility(View.GONE);
            holder.item.setCardBackgroundColor(Color.TRANSPARENT);
        }else {
            holder.tvKey.setVisibility(View.VISIBLE);
            holder.item.setCardBackgroundColor(Color.parseColor("#60C9C9C9"));
        }


    }


    @Override
    public int getItemCount() {
        return keys.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView item;
        private TextView tvKey,tvText;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            tvKey = itemView.findViewById(R.id.tvKey);
            tvText = itemView.findViewById(R.id.tvTextKey);
            item.getLayoutParams().height = (int) (context.getResources().getDisplayMetrics().widthPixels/5.5);
            item.getLayoutParams().width = (int) (context.getResources().getDisplayMetrics().widthPixels/5.5);
            item.setRadius((float) ((context.getResources().getDisplayMetrics().widthPixels/5.5)/2));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OnItemClick(keys.get(getAdapterPosition()).getNumber());
                }
            });
            itemView.setOnTouchListener(new OnTouch());
        }
    }

    public abstract void OnItemClick(String wl);
}
