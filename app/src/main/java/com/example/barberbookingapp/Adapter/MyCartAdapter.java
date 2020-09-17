package com.example.barberbookingapp.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.barberbookingapp.Database.CartDatabase;
import com.example.barberbookingapp.Database.CartItem;
import com.example.barberbookingapp.Database.DatabaseUtils;
import com.example.barberbookingapp.Interface.ICartItemUpdateListener;
import com.example.barberbookingapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyViewHolder> {

    Context context;
    List<CartItem> cartItemList;
    CartDatabase cartDatabase;
    ICartItemUpdateListener iCartItemUpdateListener;

    public MyCartAdapter(Context context, List<CartItem> cartItemList, ICartItemUpdateListener iCartItemUpdateListener) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.iCartItemUpdateListener = iCartItemUpdateListener;
        this.cartDatabase = CartDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(
                R.layout.layout_cart_item,viewGroup,false
        );
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        Picasso.get().load(cartItemList.get(i).getProductImage()).into(myViewHolder.img_product);
        myViewHolder.txt_cart_name.setText(new StringBuilder(cartItemList.get(i).getProductName()));
        myViewHolder.txt_cart_price.setText(new StringBuilder("$").append(cartItemList.get(i).getProductPrice()));
        myViewHolder.txt_quantity.setText(new StringBuilder(String.valueOf(cartItemList.get(i).getProductQuantity())));

        myViewHolder.setListener((view, pos, isDecrease) -> {
            if (isDecrease)
            {
                if (cartItemList.get(pos).getProductQuantity() > 0)
                {
                    cartItemList.get(pos)
                            .setProductQuantity(cartItemList
                                    .get(pos)
                                    .getProductQuantity() - 1);
                    DatabaseUtils.updateCart(cartDatabase, cartItemList.get(pos));
                    myViewHolder.txt_quantity.setText(new StringBuilder(String.valueOf(cartItemList.get(i).getProductQuantity())));

                }
              else if(cartItemList.get(pos).getProductQuantity()==0)
                {
                    DatabaseUtils.deleteCart(cartDatabase, cartItemList.get(pos));
                    cartItemList.remove(pos);
                    notifyItemRemoved(pos);
                }
            }
            else
            {
                if (cartItemList.get(pos).getProductQuantity() < 99)
                {
                    cartItemList.get(pos)
                            .setProductQuantity(cartItemList
                                    .get(pos)
                                    .getProductQuantity() + 1);
                    DatabaseUtils.updateCart(cartDatabase, cartItemList.get(pos));
                    myViewHolder.txt_quantity.setText(new StringBuilder(String.valueOf(cartItemList.get(i).getProductQuantity())));
                }
            }
            iCartItemUpdateListener.onCartItemUpdateSuccess();
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    interface IImageButtonListener {
        void onImageButtonClick(View view, int pos, boolean isDecrease);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_cart_name, txt_cart_price, txt_quantity;
        ImageView img_decrease, img_increase, img_product;

        IImageButtonListener listener;

        public void setListener(IImageButtonListener listener) {
            this.listener = listener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_cart_name = (TextView) itemView.findViewById(R.id.txt_cart_name);
            txt_cart_price = (TextView) itemView.findViewById(R.id.txt_cart_price);
            txt_quantity = (TextView) itemView.findViewById(R.id.txt_cart_quantity);

            img_decrease = (ImageView) itemView.findViewById(R.id.img_decrease);
            img_increase = (ImageView) itemView.findViewById(R.id.img_increase);
            img_product = (ImageView) itemView.findViewById(R.id.cart_img);

            img_decrease.setOnClickListener(v -> listener.onImageButtonClick(v,getAdapterPosition(),true));

            img_increase.setOnClickListener(v -> listener.onImageButtonClick(v,getAdapterPosition(),false));
        }
    }
}
