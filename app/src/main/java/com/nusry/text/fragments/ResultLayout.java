package com.nusry.text.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.nusry.text.MainActivity;
import com.nusry.text.Permission;
import com.nusry.text.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;


public class ResultLayout extends Fragment {

    private TextView label;
    private EditText resultHolder, edtFileName;
    private ImageView deleteBtn, saveBtn, shareBtn;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 100;
    private Permission permission;

    public ResultLayout() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result_layout, container, false);

        ((MainActivity)getActivity()).setTag_ResultLayout(getTag());
        permission = new Permission(getActivity());

        label = view.findViewById(R.id.tv_label);

        resultHolder = view.findViewById(R.id.edt_result_holder);
        resultHolder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    label.setVisibility(View.VISIBLE);
                }
                else {
                    label.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        deleteBtn = view.findViewById(R.id.btn_delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });




        //Share Button
        shareBtn = view.findViewById(R.id.btn_share);
        shareBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String string = resultHolder.getText().toString();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,string);
                intent.setType("text/plain");
                startActivity(intent);
            }
        });


        saveBtn = view.findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = resultHolder.getText().toString();
                if (!TextUtils.isEmpty(msg)){
                    permission.grantAccess(R.string.dialog_msg_to_save_text_file);
                    if (permission.isAccessGranted()){
                        saveConfirmationDialog(msg);
                    }
                }
            }
        });

        return view;
    }

    /**
     * A confirmation dialog which also sets the name of the file.
     * @param msg
     */
    private void saveConfirmationDialog(final String msg){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_save_confirmation);
        edtFileName = dialog.findViewById(R.id.edt_file_name);
        Button button = dialog.findViewById(R.id.btn_confirm_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = edtFileName.getText().toString();
                if (!TextUtils.isEmpty(fileName)){
                    dialog.dismiss();
                    saveInFile(msg, fileName);
                }
                else {
                    edtFileName.setError("file name cannot be empty");
                }
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    /**
     * Storing the .txt file publicly in a particular folder in external storage.
     * @param msg
     * @param fileName
     */
    private void saveInFile(String msg, String fileName) {
        try{
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"Text Scanner App");
            if (!dir.exists()) {
                Toast.makeText(getActivity(), "created", Toast.LENGTH_SHORT).show();
                dir.mkdirs();
            }
            if (dir.exists()){
                File file = new File(dir, fileName);
                if (!file.exists()){
                    file.createNewFile();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
                    writer.write(msg);
                    writer.close();
                    MediaScannerConnection.scanFile(getActivity(), new String[]{file.toString()}, null, null);
                    Toast.makeText(getActivity(), "Successfully Saved in Documents/Text Scanner App/"+fileName, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "File already exists\nUnable to save data", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getActivity(), "Unable to create directory", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Clearing editText
     */
    private void clearData() {
        resultHolder.setText("");
        label.setVisibility(View.VISIBLE);
    }

    /**
     * Displaying the text from the image into an editText
     * @param msg
     */
    public void setResult(String msg){
        label.setVisibility(View.INVISIBLE);
        resultHolder.setText(msg);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                permission.setAccessGranted(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
                return;
            }
        }
    }
}
