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

/**
 * Created by Dew on 3/3/2018.
 */
class gridMobileAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;

    public gridMobileAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
        GridMobileViewHolder holder = null;
        if (convertView == null) {
            holder = new GridMobileViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.grid_layout, parent, false);
            holder.img1 = (ImageView) convertView.findViewById(R.id.img1);
            holder.txt1 = (TextView) convertView.findViewById(R.id.txt1);
            holder.txt2 = (TextView) convertView.findViewById(R.id.txt2);
            holder.txt3 = (TextView) convertView.findViewById(R.id.txt3);
            holder.txt4 = (TextView) convertView.findViewById(R.id.txt4);
            holder.txt5 = (TextView) convertView.findViewById(R.id.txt5);
            holder.txt6 = (TextView) convertView.findViewById(R.id.txt6);

            convertView.setTag(holder);
        } else {
            holder = (GridMobileViewHolder) convertView.getTag();
        }

        holder.img1.setId(position);
        holder.txt1.setId(position);
        holder.txt2.setId(position);
        holder.txt3.setId(position);
        holder.txt4.setId(position);
        holder.txt5.setId(position);
        holder.txt6.setId(position);

        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);

        try{
            holder.txt1.setText(song.get(Tab3Fragment.KEY_TITLE));
            holder.txt2.setText(song.get(Tab3Fragment.KEY_ANNOUNCED));
            holder.txt3.setText(song.get(Tab3Fragment.KEY_ONE));
            holder.txt4.setText(song.get(Tab3Fragment.KEY_TWO));
            holder.txt5.setText(song.get(Tab3Fragment.KEY_THREE));
            holder.txt6.setText(song.get(Tab3Fragment.KEY_FOUR));


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

class GridMobileViewHolder {
    ImageView img1;
    TextView txt1, txt2, txt3, txt4, txt5, txt6;
}