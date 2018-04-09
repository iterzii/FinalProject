package project.afinal.anuchar.mobilemarket;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

class ListMobileAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;

    public ListMobileAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
    }
    public int getCount() {
        return data.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ListMobileViewHolder holder = null;

        if (convertView == null) {
            holder = new ListMobileViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.cardview_row, parent, false);
            holder.img1 = (ImageView) convertView.findViewById(R.id.img1);
            holder.txt1 = (TextView) convertView.findViewById(R.id.txt1);
            holder.onLineShop4 = (TextView) convertView.findViewById(R.id.onLineShop4);
            holder.onLineShop5 = (TextView) convertView.findViewById(R.id.onLineShop5);
            holder.onLineShop6 = (TextView) convertView.findViewById(R.id.onLineShop6);
//            holder.txtShop13 = (TextView) convertView.findViewById(R.id.txtShop13);
//            holder.txtShop14 = (TextView) convertView.findViewById(R.id.txtShop14);
//            holder.txtShop15 = (TextView) convertView.findViewById(R.id.txtShop15);

            convertView.setTag(holder);
        } else {
            holder = (ListMobileViewHolder) convertView.getTag();
        }
        holder.img1.setId(position);
        holder.txt1.setId(position);
        holder.onLineShop4.setId(position);
        holder.onLineShop5.setId(position);
        holder.onLineShop6.setId(position);
//        holder.txtShop13.setId(position);
//        holder.txtShop14.setId(position);
//        holder.txtShop15.setId(position);



        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);

        try{
            holder.txt1.setText(song.get(Tab2Fragment.KEY_TITLE));
            holder.onLineShop4.setText(song.get(Tab2Fragment.KEY_LAZADA));
            holder.onLineShop5.setText(song.get(Tab2Fragment.KEY_SUPERTSTORE));
            holder.onLineShop6.setText(song.get(Tab2Fragment.KEY_TOPVALUE));
//            holder.txtShop13.setText(song.get(Tab2Fragment.KEY_LINK1));
//            holder.txtShop14.setText(song.get(Tab2Fragment.KEY_LINK2));
//            holder.txtShop15.setText(song.get(Tab2Fragment.KEY_LINK3));



            if(song.get(Tab1Fragment.KEY_URLTOIMAGE).toString().length() < 5)
            {
                holder.img1.setVisibility(View.GONE);
            }else{
                Picasso.with(activity)
                        .load(song.get(Tab1Fragment.KEY_URLTOIMAGE).toString())
                        .resize(290, 290)
                        .into(holder.img1);
            }
        }catch(Exception e) {}
        return convertView;
    }
}

class ListMobileViewHolder {
    ImageView img1;
    TextView txt1,onLineShop4,onLineShop5,onLineShop6,txtShop13,txtShop14,txtShop15;
}