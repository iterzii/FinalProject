package project.afinal.anuchar.mobilemarket;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by User on 2/28/2017.
 */

public class Tab2Fragment extends Fragment {
    private static final String TAG = "Tab2Fragment";

    HttpClient hc;
    HttpPost hp;
    HttpResponse hr;
    String url = "", data = "";
    BufferedReader br;

    ListView display;

    ArrayList<HashMap<String, String>> fill_data;
    HashMap<String, String> myMap, read_data;

    SimpleAdapter myAdapter;

    static final String KEY_TITLE = "title";
    static final String KEY_URLTOIMAGE = "urlToImage";
    static final String KEY_LAZADA = "lazada";
    static final String KEY_SUPERTSTORE = "supertstore";
    static final String KEY_TOPVALUE = "TOPVALUE";
//    static final String KEY_LINK1 = "link1";
//    static final String KEY_LINK2 = "link2";
//    static final String KEY_LINK3 = "link3";

    EditText edtSearch;
    Spinner spnSort;
    String allInfo = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab2_fragment, container, false);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        display = (ListView) view.findViewById(R.id.lstDisplay);
        display.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onClickCard(i, allInfo);
            }
        });

        url = "http://anuchar.000webhostapp.com/PROJECTFINAL/ALLDEVICEDETAIL.php?order=announced%20desc";
        useListview(url);
        edtSearch = (EditText) view.findViewById(R.id.edtSearch);
        edtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (edtSearch.getText().toString().equals("")) {
                        url = "http://anuchar.000webhostapp.com/PROJECTFINAL/ALLDEVICEDETAIL.php?order=announced%20desc";
                        useListview(url);
                        hideKeyboard(getActivity());
                    } else {
                        getSpinner(edtSearch.getText().toString(), spnSort.getSelectedItemPosition());
                        hideKeyboard(getActivity());
                    }
                    return true;
                }
                return false;
            }
        });

        spnSort = (Spinner) view.findViewById(R.id.spnSort);
        spnSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text1 = edtSearch.getText().toString();
                switch (position) {
                    case 0:
                        getSpinner(text1, position);
                        break;
                    case 1:
                        getSpinner(text1, position);
                        break;
                    case 2:
                        getSpinner(text1, position);
                        break;
                    case 3:
                        getSpinner(text1, position);
                        break;
                    case 4:
                        getSpinner(text1, position);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }

    public void useListview(String url) {
        try {
            hc = new DefaultHttpClient();
            hp = new HttpPost(url);
            hr = hc.execute(hp);
            br = new BufferedReader(new InputStreamReader(hr.getEntity().getContent()));
            data = br.readLine();
            allInfo = data;

            if (!data.equals("0 results")) {
                String[] mobileSplit = data.split(";");
                myAdapter = null;
                fill_data = new ArrayList<HashMap<String, String>>();
                int j = 0;
                while (j < mobileSplit.length) {
                    String[] detail = mobileSplit[j].split("#");
                    String url2 = "https://www.techxcite.com" + detail[26];
                    myMap = new HashMap<String, String>();
                    myMap.put(KEY_URLTOIMAGE, "" + url2);
                    myMap.put(KEY_TITLE, "" + detail[2]);

                    if (!detail[28].equals("0"))
                        myMap.put(KEY_LAZADA, "" + detail[28]);
                    else
                        myMap.put(KEY_LAZADA, "ไม่พบ");

                    if (!detail[29].equals("0"))
                        myMap.put(KEY_SUPERTSTORE, "" + detail[29]);
                    else
                        myMap.put(KEY_SUPERTSTORE, "ไม่มีสินค้า");

                    if (!detail[30].equals("0"))
                        myMap.put(KEY_TOPVALUE, "" + detail[30]);
                    else
                        myMap.put(KEY_TOPVALUE, "ไม่พบ");

//                    if (!detail[31].equals(""))
//                        myMap.put(KEY_LINK1, detail[31]);
//                    else
//                        myMap.put(KEY_LINK1, "https://www.google.com");
//
//                    if (!detail[32].equals(""))
//                        myMap.put(KEY_LINK2, detail[32]);
//                    else
//                        myMap.put(KEY_LINK2, "https://www.google.com");
//
//                    if (!detail[33].equals(""))
//                        myMap.put(KEY_LINK3, detail[33]);
//                    else
//                        myMap.put(KEY_LINK3, "https://www.google.com");

                    fill_data.add(myMap);
                    j++;
                }

                ListMobileAdapter adapter = new ListMobileAdapter((Activity) getContext(), fill_data);
                display.setAdapter(adapter);
            } else {
                Toast.makeText(getContext(), "ไม่พบผลลัพธ์", Toast.LENGTH_SHORT).show();
                display.setAdapter(null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void getSpinner(String txt, int position) {
        if (txt.equals("")) {
            switch (position) {
                case 0:
                    url = "http://anuchar.000webhostapp.com/PROJECTFINAL/ALLDEVICEDETAIL.php?order=announced%20desc";
                    useListview(url);
                    break;
                case 1:
                    url = "http://anuchar.000webhostapp.com/PROJECTFINAL/ALLDEVICEDETAIL.php?order=name";
                    useListview(url);
                    break;
                case 2:
                    url = "http://anuchar.000webhostapp.com/PROJECTFINAL/ALLDEVICEDETAIL.php?order=name%20desc";
                    useListview(url);
                    break;
                case 3:
                    url = "http://anuchar.000webhostapp.com/PROJECTFINAL/ALLDEVICEDETAIL.php?order=announced%20desc";
                    useListview(url);
                    break;
                case 4:
                    url = "http://anuchar.000webhostapp.com/PROJECTFINAL/ALLDEVICEDETAIL.php?order=announced%20desc";
                    useListview(url);
                    break;
            }
        } else {
            switch (position) {
                case 0:
                    url = "http://anuchar.000webhostapp.com/PROJECTFINAL/SEARCHDEVICEDETAIL.php?search="
                            + txt + "&order=announced%20desc";
                    useListview(url);
                    break;
                case 1:
                    url = "http://anuchar.000webhostapp.com/PROJECTFINAL/SEARCHDEVICEDETAIL.php?search="
                            + txt + "&order=name";
                    useListview(url);
                    break;
                case 2:
                    url = "http://anuchar.000webhostapp.com/PROJECTFINAL/SEARCHDEVICEDETAIL.php?search="
                            + txt + "&order=name%20desc";
                    useListview(url);
                    break;
                case 3:
                    url = "http://anuchar.000webhostapp.com/PROJECTFINAL/SEARCHDEVICEDETAIL.php?search="
                            + txt + "&order=announced%20desc";
                    useListview(url);
                    break;
                case 4:
                    url = "http://anuchar.000webhostapp.com/PROJECTFINAL/SEARCHDEVICEDETAIL.php?search="
                            + txt + "&order=announced%20desc";
                    useListview(url);
                    break;
            }
        }
    }

    public void onClickCard(int id, String allInfo) {
        String res = fill_data.get(+id).get(KEY_TITLE);
        String[] device = allInfo.split(";");

        String[] detail = device[id].split("#");

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        View dView = getLayoutInflater().inflate(R.layout.dialog_device_detail, null);

        final TextView name = (TextView) dView.findViewById(R.id.txtName);
        name.setText(res);
        final TextView announced = (TextView) dView.findViewById(R.id.txtAnnounced);
        announced.setText(detail[3]);
        final TextView dimensions = (TextView) dView.findViewById(R.id.txtDimensions);
        dimensions.setText(detail[4]);
        final TextView screen = (TextView) dView.findViewById(R.id.txtScreen);
        screen.setText(detail[5]);
        final TextView weight = (TextView) dView.findViewById(R.id.txtWeight);
        weight.setText(detail[6]);
        final TextView os = (TextView) dView.findViewById(R.id.txtOs);
        os.setText(detail[7]);
        final TextView resolution = (TextView) dView.findViewById(R.id.txtResolution);
        resolution.setText(detail[8]);
        final TextView cpu = (TextView) dView.findViewById(R.id.txtCPU);
        cpu.setText(detail[9]);
        final TextView gpu = (TextView) dView.findViewById(R.id.txtGPU);
        gpu.setText(detail[10]);
        final TextView ram = (TextView) dView.findViewById(R.id.txtRAM);
        ram.setText(detail[11]);
        final TextView rom = (TextView) dView.findViewById(R.id.txtROM);
        rom.setText(detail[12]);
        final TextView exrom = (TextView) dView.findViewById(R.id.txtEXROM);
        exrom.setText(detail[13]);
        final TextView technology = (TextView) dView.findViewById(R.id.txtTechnology);
        technology.setText(detail[14]);
        final TextView bluetooth = (TextView) dView.findViewById(R.id.txtBluetooth);
        bluetooth.setText(detail[15]);
        final TextView wifi = (TextView) dView.findViewById(R.id.txtWIFI);
        if (detail[16].equals("1"))
            wifi.setText("รองรับ");
        else
            wifi.setText("ไม่รองรับ");
        final TextView nfc = (TextView) dView.findViewById(R.id.txtNFC);
        if (detail[17].equals("1"))
            nfc.setText("รองรับ");
        else
            nfc.setText("ไม่รองรับ");
        final TextView gps = (TextView) dView.findViewById(R.id.txtGPS);
        if (detail[18].equals("1"))
            gps.setText("รองรับ");
        else
            gps.setText("ไม่รองรับ");
        final TextView fcam = (TextView) dView.findViewById(R.id.txtFcam);
        fcam.setText(detail[19]);
        final TextView bcam = (TextView) dView.findViewById(R.id.txtBcam);
        bcam.setText(detail[20]);
        final TextView vedio = (TextView) dView.findViewById(R.id.txtVideo);
        vedio.setText(detail[21]);
        final TextView battery = (TextView) dView.findViewById(R.id.txtBattery);
        battery.setText(detail[22]);
        final TextView color = (TextView) dView.findViewById(R.id.txtColor);
        color.setText(detail[23]);
        final TextView duosim = (TextView) dView.findViewById(R.id.txtDuosim);
        if (detail[24].equals("1"))
            duosim.setText("รองรับ");
        else
            duosim.setText("ไม่รองรับ");
        final TextView waterproof = (TextView) dView.findViewById(R.id.txtWaterproof);
        if (detail[25].equals("1"))
            waterproof.setText("รองรับ");
        else
            waterproof.setText("ไม่รองรับ");

        final ImageView imgView = (ImageView) dView.findViewById(R.id.imgView1);
        Picasso.with(getContext()).load("https://www.techxcite.com"+detail[27]).into(imgView);

        int pLazada, pSupert, pTopvalue;

        final String urlVisit;
        if(detail[28].equals("0"))
            pLazada = 999999;
        else
            pLazada = Integer.parseInt(detail[28]);

        if(detail[29].equals("0"))
            pSupert = 999999;
        else
            pSupert = Integer.parseInt(detail[29]);
        if(detail[30].equals("0"))
            pTopvalue = 999999;
        else
            pTopvalue = Integer.parseInt(detail[30]);


        if(pLazada < pSupert && pLazada < pTopvalue){
            urlVisit = detail[31];
        }else if(pSupert < pLazada && pSupert < pTopvalue){
            urlVisit = detail[32];
        }else if(pTopvalue < pLazada && pTopvalue < pSupert ){
            urlVisit = detail[33];
        }else{
            urlVisit = detail[31];
        }

        final ImageButton imbtnVisit = (ImageButton) dView.findViewById(R.id.ImbtnVisit);
        imbtnVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlVisit));
                startActivity(browserIntent);
            }
        });

        mBuilder.setView(dView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

}
