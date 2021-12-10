package com.example.finale_weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<WeatherRecyclerViewModal> weatherRecyclerViewModalArrayList;

    public RecyclerViewAdapter(Context context, ArrayList<WeatherRecyclerViewModal> weatherRecyclerViewModalArrayList) {
        this.context = context;
        this.weatherRecyclerViewModalArrayList = weatherRecyclerViewModalArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recyclerview_item,parent,false);
        ViewHolder obj=new ViewHolder(view);
        return obj;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        WeatherRecyclerViewModal myModal=weatherRecyclerViewModalArrayList.get(position);
        holder.tempTV.setText(myModal.getTemp()+"°c");
        Picasso.get().load("http:".concat(myModal.getIcon())).into(holder.condition);
        holder.windTV.setText(myModal.getWindSpeed()+"km/hr");
        SimpleDateFormat getdate=new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat timeFormat=new SimpleDateFormat("hh:mm aa");
        try
        {
            Date finalDate= getdate.parse(myModal.getTime());
            holder.timeTV.setText(timeFormat.format(finalDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }



    }

    @Override
    public int getItemCount() {
        return weatherRecyclerViewModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tempTV,timeTV,windTV;
        ImageView condition;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            windTV=itemView.findViewById(R.id.idTVWindSpeed);
            tempTV=itemView.findViewById(R.id.idTVTemp);
            timeTV=itemView.findViewById(R.id.idTVTime);
            condition=itemView.findViewById(R.id.idTVImage);
        }
    }
}
