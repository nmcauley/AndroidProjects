package com.example.settingsapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {
    View view;

    public interface myFragmentInterface{
        public void settingChange(int setting);
    }
    myFragmentInterface activityCommunicator;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            activityCommunicator = (myFragmentInterface) getActivity();
        }catch (Exception e){
            throw new ClassCastException("Activity must implement myFragmentInterface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.category_layout, container, false);

        // sets the values for list and then adapts using simple list adapter and then sets it to the fragment.
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("Battery");
        categories.add("Storage");
        categories.add("Software Update");
        categories.add("Volume");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
                android.R.layout.simple_list_item_1, categories);

        ListView listView = (ListView)view.findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);

        // set a listener for each setting.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // obtains setting category at the specified location and retrieves it from the array
                // transmits that through the communicator
                activityCommunicator.settingChange(position);
            }
        });

        return view;
    }
}
