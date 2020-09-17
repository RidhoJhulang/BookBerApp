package com.example.barberbookingapp.Interface;

import com.example.barberbookingapp.Database.CartItem;

import java.util.List;

public interface ICartItemLoadListener {
    void onGetAllItemCartLoadSuccess(List<CartItem> cartItemList);
}
