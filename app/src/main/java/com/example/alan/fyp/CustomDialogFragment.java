package com.example.alan.fyp;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alan.fyp.databinding.SignupformBinding;
import com.example.alan.fyp.viewModel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomDialogFragment extends DialogFragment{


    private static final String TAG = "CustomDialogF";
    private CustomDialogListener mListener;

    public UserViewModel userViewModel = new UserViewModel();

    private EditText register_username,register_email,register_pw;
    public String email ;
    public String password;
    public String name;
    public CustomDialogFragment() {
        // Required empty public constructor

    }
    public String getusername(){
        return  name;
    }

    public void setusername(String Name)
    {
        this.name=Name;
    }

    public String getuseremail(){
        return  email;
    }

    public void setuseremail(String Email)
    {
        this.email=Email;
    }

    public String getuserpw()
    {
        return password;
    }

    public void setuserpassword(String pw)
    {
        this.password=pw;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CustomDialogListener) {
            mListener = (CustomDialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CustomDialogListener");
        }
    }


//    public static CustomDialogFragment newInstance(String email,String pw) {
//        CustomDialogFragment C_Dialog = new CustomDialogFragment();
//        Bundle args = new Bundle();
//        args.putString("email", email);
//        args.putString("pw", pw);
//        C_Dialog.setArguments(args);
//        return C_Dialog;
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

//        SignupformBinding binding = DataBindingUtil.inflate(inflater,
//                R.layout.signupform,
//                null,
//                false);
//        View customView = binding.getRoot();
        View customView = inflater.inflate(R.layout.signupform, null);
        //binding.setUser(userViewModel);

        register_username = (EditText) customView.findViewById(R.id.field_name);
        register_email = (EditText) customView.findViewById(R.id.field_email);
        register_pw = (EditText) customView.findViewById(R.id.field_pw);

        Toolbar mytoolbar = (Toolbar) customView.findViewById(R.id.toolbar);

        mytoolbar.setNavigationIcon(R.drawable.ic_close);
        mytoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogFragment.this.getDialog().cancel();
                        if(mListener!=null){
                            mListener.onDialogNegative(null);
                        }
            }
        });

        mytoolbar.inflateMenu(R.menu.menu_main);
        mytoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tb_done:
                        if(mListener!=null) {
                           if(validateForm()) {
                               //String message = email;
                               mListener.onDialogPositive(name ,email, password);
                           }
                        }
                        return true;

                }

                return true;
        }

    });

        builder.setView(customView);
                // Add action buttons
//                .setPositiveButton(R.string.createac, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        // sign in the user ...
//                        if(mListener!=null){
//                            if(validateForm()) {
//                                String message = String.format("username %s password %s",username,password);
//                                Log.v(TAG, message);
//
//                                mListener.onDialogPositive(message);
//                            }
//                        }
//                    }
//                });
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                       CustomDialogFragment.this.getDialog().cancel();
//                        if(mListener!=null){
//                            mListener.onDialogNegative(null);
//                        }
//                    }
//                });




        return builder.create();



    }

    private boolean validateForm() {
        name=register_username.getText().toString().trim();
        email= register_email.getText().toString().trim();
        password= register_pw.getText().toString().trim();
        //email = userViewModel.email.get();
        //password = userViewModel.password.get();

        if(name.isEmpty())return false;
        if(email.isEmpty())return false;
        if(password.isEmpty())return false;
        return true;
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }





    }
