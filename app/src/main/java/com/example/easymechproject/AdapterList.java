package com.example.easymechproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdapterList extends BaseAdapter{

    private  Context context;
    ArrayList<Mechanics> mechanics;

    public AdapterList(Context context, ArrayList<Mechanics> mechanics) {
        this.context = context;
        this.mechanics = mechanics;
    }

    @Override
    public int getCount() {
        return mechanics.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        view = LayoutInflater.from(context).inflate(R.layout.listview_layout, parent, false);

        TextView title = view.findViewById(R.id.titles);
        TextView describe = view.findViewById(R.id.description);
        ImageView image = view.findViewById(R.id.images);

        title.setText(mechanics.get(position).getTitle());
        describe.setText(mechanics.get(position).getDescription());
        image.setImageResource(mechanics.get(position).getImages());


        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(position==0){
                    Intent int2 = new Intent(context,Mechanic_Profile_Options.class);
                    context.startActivity(int2);
                }
            }
        });

        return view;
    }

}
