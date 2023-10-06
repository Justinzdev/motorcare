package com.example.myproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.myproject.model.WorkingLists;

import java.util.List;

public class WorkingListsAdapter extends BaseAdapter {
    private List<WorkingLists> caseList;

    private OnButtonClickListener buttonClickListener;
    private Context context;

    public WorkingListsAdapter(Context context, List<WorkingLists> caseList) {
        this.context = context;
        this.caseList = caseList;
    }

    @Override
    public int getCount() {
        return caseList.size();
    }

    @Override
    public Object getItem(int position) {
        return caseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface OnButtonClickListener {
        void onButtonClickConfirm(int position);
        void onButtonClickCancel(int position);
    }
    public void setButtonClickListener(OnButtonClickListener listener) {
        this.buttonClickListener = listener;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.working_list_items, null);
        }

        TextView textTitle = view.findViewById(R.id.workingTitle);
        TextView textDescription = view.findViewById(R.id.workingDescription);
        TextView textUsername = view.findViewById(R.id.workingUsername);
        TextView textPhone = view.findViewById(R.id.workingPhone);
        TextView textLat = view.findViewById(R.id.workingLat);
        TextView textLng = view.findViewById(R.id.workingLng);
        Button confirmButton = view.findViewById(R.id.confirmButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        WorkingLists currentCase = caseList.get(position);
        textTitle.setText(currentCase.getTitle());
        textDescription.setText(currentCase.getDescription());
        textUsername.setText("ชื่อลูกค้า: " + currentCase.getUsername());
        textPhone.setText("เบอร์โทรศัพท์: " + currentCase.getPhone());
        textLat.setText(currentCase.getLat());
        textLng.setText(currentCase.getLng());

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonClickListener != null) {
                    buttonClickListener.onButtonClickConfirm(position);
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonClickListener != null) {
                    buttonClickListener.onButtonClickCancel(position);
                }
            }
        });

        return view;
    }
}
