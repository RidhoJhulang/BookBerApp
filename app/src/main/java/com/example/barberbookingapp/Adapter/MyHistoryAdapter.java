package com.example.barberbookingapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberbookingapp.Model.BookingInformation;
import com.example.barberbookingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyHistoryAdapter extends RecyclerView.Adapter<MyHistoryAdapter.MyViewHoder> {

    Context context;
    List<BookingInformation>bookingInformationList;

    public MyHistoryAdapter(Context context, List<BookingInformation> bookingInformationList) {
        this.context = context;
        this.bookingInformationList = bookingInformationList;
    }

    @NonNull
    @Override
    public MyViewHoder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_history, viewGroup, false);
        return new MyViewHoder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoder myViewHoder, int i) {

        myViewHoder.txt_salon_name.setText(bookingInformationList.get(i).getSalonName());
        myViewHoder.txt_salon_address.setText(bookingInformationList.get(i).getSalonAddress());
        myViewHoder.txt_booking_time.setText(bookingInformationList.get(i).getTime());
        myViewHoder.txt_booking_barber_text.setText(bookingInformationList.get(i).getBarberName());



    }

    @Override
    public int getItemCount() {
        return bookingInformationList.size();
    }

    public class MyViewHoder extends RecyclerView.ViewHolder {

        Unbinder unbinder;

//        @BindView(R.id.txt_booking_date)
//        TextView txt_booking_date;
        @BindView(R.id.txt_salon_name)
        TextView txt_salon_name;
        @BindView(R.id.txt_salon_address)
        TextView txt_salon_address;
        @BindView(R.id.txt_booking_time)
        TextView txt_booking_time;
        @BindView(R.id.txt_booking_barber_text)
        TextView txt_booking_barber_text;

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }
    }
}
