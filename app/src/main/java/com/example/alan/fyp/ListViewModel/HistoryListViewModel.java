package com.example.alan.fyp.ListViewModel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.example.alan.fyp.BR;
import com.example.alan.fyp.R;
import com.example.alan.fyp.viewModel.ConViewModel;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by wadealanchan on 4/1/2018.
 */

public class HistoryListViewModel extends ViewModel{

    public final ObservableList<ConViewModel> items = new ObservableArrayList<ConViewModel>();
    public final ItemBinding<ConViewModel> itemBinding = ItemBinding.of(BR.conversation, R.layout.list_history_conversation);



}

