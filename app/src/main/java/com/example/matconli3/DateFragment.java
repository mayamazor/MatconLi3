//package com.example.matconli3;
//
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//import androidx.fragment.app.Fragment;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.DatePicker;
//
//public class DateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
//
//    public DateFragment() {
//        // Required empty public constructor
//    }
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        DatePickerDialog picker = new DatePickerDialog(getContext(), this, 2021, 1, 1);
//        return picker;
//    }
//
//    @Override
//    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//        Log.d("TAG", "" + year +"/" + month + "/" + dayOfMonth);
//    }
//}