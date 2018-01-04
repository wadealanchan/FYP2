package com.example.alan.fyp.ListViewModel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.example.alan.fyp.BR;
import com.example.alan.fyp.R;
import com.example.alan.fyp.viewModel.Con_MessageViewModel;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by wadealanchan on 10/12/2017.
 */

public class ChatListViewModel2 extends ViewModel {


    public final ObservableList<Con_MessageViewModel> items = new ObservableArrayList<Con_MessageViewModel>();
    public final ItemBinding<Con_MessageViewModel> itemBinding = ItemBinding.of(BR.chat, R.layout.list_item2);




}
