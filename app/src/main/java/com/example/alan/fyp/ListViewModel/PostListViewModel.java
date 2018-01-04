package com.example.alan.fyp.ListViewModel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.example.alan.fyp.BR;
import com.example.alan.fyp.R;
import com.example.alan.fyp.model.Post;
import com.example.alan.fyp.viewModel.PostViewModel;


import me.tatarka.bindingcollectionadapter2.BindingListViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by wadealanchan on 13/11/2017.
 */

public class PostListViewModel extends ViewModel{

    public final ObservableList<PostViewModel> items = new ObservableArrayList<PostViewModel>();
    public final ItemBinding<PostViewModel> itemBinding = ItemBinding.of(BR.post, R.layout.list_post);



    public final BindingListViewAdapter.ItemIds<Object> itemIds = new BindingListViewAdapter.ItemIds<Object>() {
        @Override
        public long getItemId(int position, Object item) {
            return position;
        }
    };
}
