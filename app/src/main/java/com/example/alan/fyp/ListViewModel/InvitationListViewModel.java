package com.example.alan.fyp.ListViewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.example.alan.fyp.BR;
import com.example.alan.fyp.R;
import com.example.alan.fyp.viewModel.ConViewModel;
import com.example.alan.fyp.viewModel.InvitationViewModel;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by wadealanchan on 30/1/2018.
 */

public class InvitationListViewModel {


    public final ObservableList<InvitationViewModel> items = new ObservableArrayList<InvitationViewModel>();
    public final ItemBinding<InvitationViewModel> itemBinding = ItemBinding.of(BR.invitation, R.layout.list_item_invitation);
}
