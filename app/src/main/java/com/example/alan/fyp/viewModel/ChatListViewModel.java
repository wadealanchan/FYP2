package com.example.alan.fyp.viewModel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.example.alan.fyp.BR;
import com.example.alan.fyp.R;

import me.tatarka.bindingcollectionadapter2.BindingListViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by wadealanchan on 10/12/2017.
 */

public class ChatListViewModel extends ViewModel {


    public final ObservableList<ChatViewModel> items = new ObservableArrayList<ChatViewModel>();
    public final ItemBinding<ChatViewModel> itemBinding = ItemBinding.of(BR.chat, R.layout.list_item);




}
