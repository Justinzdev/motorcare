package com.example.myproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.myproject.model.NotificationLists;

import java.util.List;

public class NotificationAdapter extends BaseAdapter {
    private List<NotificationLists> caseList;

    private Context context;

    public NotificationAdapter(Context context, List<NotificationLists> caseList) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.notification_list_items, null);
        }

        TextView textTitle = view.findViewById(R.id.notiTitle);
        TextView textDescription = view.findViewById(R.id.notiDescription);

        NotificationLists currentCase = caseList.get(position);
        textTitle.setText(currentCase.getTitle());
        textDescription.setText(currentCase.getDescription());

        return view;
    }
}
