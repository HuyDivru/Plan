package com.example.plantext;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateFragment extends BottomSheetDialogFragment {
    CircleImageView image;
    TextInputEditText inputDescription;
    AppCompatButton upload;


    public CreateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_create, container, false);
        image=view.findViewById(R.id.profile_image);
        inputDescription=view.findViewById(R.id.input_description);
        upload=view.findViewById(R.id.button_upload);
        return view;
    }
}