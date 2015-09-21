package com.dipcore.gpsswitcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;

public class SwitchGPSDialog extends DialogFragment {

    RadioButton mstPatchedRadioButton;
    RadioButton universalRadioButton;
    RadioButton universalGNSSRadioButton;
    Spinner devSpinner;
    Spinner speedSpinner;

    public interface NoticeListener {
        public void onSwitchGPSDialogDialogSaveClick(DialogFragment dialog, String lib, String dev, String speed);
        public void onSwitchGPSDialogCancelClick(DialogFragment dialog);
    }

    NoticeListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (NoticeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement NoticeListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // View
        final View view = inflater.inflate(R.layout.switch_gps_dialog, null);
        mstPatchedRadioButton = (RadioButton)view.findViewById(R.id.mstPatchedRadioButton);
        universalRadioButton = (RadioButton)view.findViewById(R.id.universalRadioButton);
        universalGNSSRadioButton = (RadioButton)view.findViewById(R.id.universalGNSSRadioButton);
        devSpinner = (Spinner)view.findViewById(R.id.devSpinner);
        speedSpinner = (Spinner)view.findViewById(R.id.speedSpinner);


        // Device list
        File f = new File("/dev");
        ArrayList<String> names = new ArrayList<String>();
        for(String s : f.list()){
            if (s.contains("ttyUSB")){
                names.add(s);
            }
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, names);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        devSpinner.setAdapter(spinnerArrayAdapter);
        //devSpinner.setSelection(((ArrayAdapter<String>) devSpinner.getAdapter()).getPosition(mGPSStatus.dev));

        // Speed
        //speedSpinner.setSelection(((ArrayAdapter<String>) speedSpinner.getAdapter()).getPosition(mGPSStatus.speed));

        // Library type
        //universalRadioButton.setChecked(mGPSStatus.lib == "universal");
        //mstPatchedRadioButton.setChecked(mGPSStatus.lib == "mst_patched");

        // Build
        builder.setView(view);

        // Set title
        builder.setTitle(R.string.switch_gps_dialog_title);

        // Buttons
        builder.setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                String dev = "";
                String speed = "";
                String lib = "";

                if (devSpinner.getSelectedItem() != null)
                    dev = devSpinner.getSelectedItem().toString();

                if (speedSpinner.getSelectedItem() != null)
                    speed = speedSpinner.getSelectedItem().toString();

                if (mstPatchedRadioButton.isChecked()) {
                    lib = "mst_patched";
                }

                if (universalRadioButton.isChecked()) {
                    lib = "generic";
                }

                if (universalGNSSRadioButton.isChecked()) {
                    lib = "generic_gnss";
                }

                mListener.onSwitchGPSDialogDialogSaveClick(SwitchGPSDialog.this, lib, dev, speed);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mListener.onSwitchGPSDialogCancelClick(SwitchGPSDialog.this);
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();

    }

}
