package project.afinal.anuchar.mobilemarket;


import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class Tab3Fragment extends Fragment {
    private static final String TAG = "Tab3Fragment";

    HttpClient hc;
    HttpPost hp;
    HttpResponse hr;
    String url = "", data = "";
    BufferedReader br;

    GridView display, compare;

    String allinfo = "";

    ArrayList<HashMap<String, String>> fill_data, fill_compare;
    HashMap<String, String> myMap, mapCompare, read_data;

    SimpleAdapter myAdapter, compareAdapter;

    static final String KEY_TITLE = "title";
    static final String KEY_URLTOIMAGE = "urlToImage";
    static final String KEY_ANNOUNCED = "announced";
    static final String KEY_ONE = "one";
    static final String KEY_TWO = "two";
    static final String KEY_THREE = "three";
    static final String KEY_FOUR = "four";

    int SwapCheck = 0;

    EditText edtSearch;
    Spinner spnSort;
    Button btnCompare, btnClear;

    int countCompare = 0, checkPHP = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3_fragment, container, false);
        checkPHP = 0;
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        countCompare = 0;
        compareAdapter = null;
        fill_compare = new ArrayList<HashMap<String, String>>();

        btnCompare = (Button) view.findViewById(R.id.btnCompare);
        btnCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (compare.getCount() == 2) {
                    startCompare();
                } else {
                    Toast.makeText(getContext(), "เลือกมือถือที่ต้องการเปรียบเทียบ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnClear = (Button) view.findViewById(R.id.btnClearCompare);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (compare.getCount() != 0) {
                    countCompare = 0;
                    fill_compare = new ArrayList<HashMap<String, String>>();
                    mapCompare.clear();
                    compare.setAdapter(null);
                } else {
                    Toast.makeText(getContext(), "ไม่มีรายการเปรียบเทียบ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        display = (GridView) view.findViewById(R.id.grdDisplay);
        display.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onClickCard(i);
            }

        });

        compare = (GridView) view.findViewById(R.id.grdCompare);
        compare.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                fill_compare.remove(i);
                gridMobileAdapter adapterCompare = new gridMobileAdapter((Activity) getContext(), fill_compare);
                compare.setAdapter(adapterCompare);
                countCompare -= 1;
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
                        checkPHP = 1;
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
            if (checkPHP == 0)
                allinfo = data;

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
                    myMap.put(KEY_ANNOUNCED, "" + detail[3]);
                    myMap.put(KEY_ONE, "" + detail[2] + ":" + detail[3] + ":" + detail[4] + ":" + detail[5] + ":" + detail[6] + ":" +
                            detail[7] + ":" + detail[8] + ":" + detail[9] + ":" + detail[10] + ":" + detail[11] + ":"
                            + detail[12] + ":" + detail[13]);
                    myMap.put(KEY_TWO, "" + detail[14] + ":" + detail[15] + ":" + detail[16] + ":" + detail[17] + ":" + detail[18]);
                    myMap.put(KEY_THREE, "" + detail[19] + ":" + detail[20] + ":" + detail[21]);
                    myMap.put(KEY_FOUR, "" + detail[22] + ":" + detail[23] + ":" + detail[24] + ":" + detail[25] + ":" + detail[26]);
                    fill_data.add(myMap);
                    j++;
                }

                gridMobileAdapter adapter = new gridMobileAdapter((Activity) getContext(), fill_data);
                display.setAdapter(adapter);
            } else {
                Toast.makeText(getContext(), "ไม่พบผลลัพธ์", Toast.LENGTH_SHORT).show();
                display.setAdapter(null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void useCompare(String imgUrl, String name, String announc, String one, String two, String three, String four) {
        if (countCompare < 2) {

            mapCompare = new HashMap<String, String>();
            mapCompare.put(KEY_URLTOIMAGE, "" + imgUrl);
            mapCompare.put(KEY_TITLE, "" + name);
            mapCompare.put(KEY_ANNOUNCED, "" + announc);
            mapCompare.put(KEY_ONE, "" + one);
            mapCompare.put(KEY_TWO, "" + two);
            mapCompare.put(KEY_THREE, "" + three);
            mapCompare.put(KEY_FOUR, "" + four);

            fill_compare.add(mapCompare);

            countCompare += 1;

            gridMobileAdapter adapterCompare = new gridMobileAdapter((Activity) getContext(), fill_compare);
            compare.setAdapter(adapterCompare);
        } else {
            Toast.makeText(getContext(), "ช่องเปรียบเทียบเต็ม", Toast.LENGTH_SHORT).show();
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
        checkPHP = 1;
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

    public void onClickCard(int id) {
        String resURL = fill_data.get(+id).get(KEY_URLTOIMAGE);
        String resTITLE = fill_data.get(+id).get(KEY_TITLE);
        String resANNOUNCED = fill_data.get(+id).get(KEY_ANNOUNCED);
        String resONE = fill_data.get(+id).get(KEY_ONE);
        String resTWO = fill_data.get(+id).get(KEY_TWO);
        String resTHREE = fill_data.get(+id).get(KEY_THREE);
        String resFOUR = fill_data.get(+id).get(KEY_FOUR);

        useCompare(resURL, resTITLE, resANNOUNCED, resONE, resTWO, resTHREE, resFOUR);
    }

    public void startCompare() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        View cView = getLayoutInflater().inflate(R.layout.dialog_divice_compare, null);
        SwapCheck = 0;

        final TextView name = (TextView) cView.findViewById(R.id.txtName);

        final Button btnSwap = (Button) cView.findViewById(R.id.btnSwap);
        btnSwap.setText("สลับ " + fill_compare.get(+1).get(KEY_TITLE));
        name.setText(fill_compare.get(+0).get(KEY_TITLE));

        final TextView announced = (TextView) cView.findViewById(R.id.txtAnnounced);
        final TextView dimensions = (TextView) cView.findViewById(R.id.txtDimensions);
        final TextView screen = (TextView) cView.findViewById(R.id.txtScreen);
        final TextView weight = (TextView) cView.findViewById(R.id.txtWeight);
        final TextView os = (TextView) cView.findViewById(R.id.txtOs);
        final TextView resolution = (TextView) cView.findViewById(R.id.txtResolution);
        final TextView cpu = (TextView) cView.findViewById(R.id.txtCPU);
        final TextView gpu = (TextView) cView.findViewById(R.id.txtGPU);
        final TextView ram = (TextView) cView.findViewById(R.id.txtRAM);
        final TextView rom = (TextView) cView.findViewById(R.id.txtROM);
        final TextView exrom = (TextView) cView.findViewById(R.id.txtEXROM);
        final TextView technology = (TextView) cView.findViewById(R.id.txtTechnology);
        final TextView bluetooth = (TextView) cView.findViewById(R.id.txtBluetooth);
        final TextView wifi = (TextView) cView.findViewById(R.id.txtWIFI);
        final TextView nfc = (TextView) cView.findViewById(R.id.txtNFC);
        final TextView gps = (TextView) cView.findViewById(R.id.txtGPS);
        final TextView fcam = (TextView) cView.findViewById(R.id.txtFcam);
        final TextView bcam = (TextView) cView.findViewById(R.id.txtBcam);
        final TextView vedio = (TextView) cView.findViewById(R.id.txtVideo);
        final TextView battery = (TextView) cView.findViewById(R.id.txtBattery);
        final TextView color = (TextView) cView.findViewById(R.id.txtColor);
        final TextView duosim = (TextView) cView.findViewById(R.id.txtDuosim);
        final TextView waterproof = (TextView) cView.findViewById(R.id.txtWaterproof);

        String[] one, two, three, four;
        one = fill_compare.get(+0).get(KEY_ONE).split(":");
        two = fill_compare.get(+0).get(KEY_TWO).split(":");
        three = fill_compare.get(+0).get(KEY_THREE).split(":");
        four = fill_compare.get(+0).get(KEY_FOUR).split(":");

        name.setText(one[0]);
        announced.setText(one[1]);
        dimensions.setText(one[2]);
        screen.setText(one[3]);
        weight.setText(one[4]);
        os.setText(one[5]);
        resolution.setText(one[6]);
        cpu.setText(one[7]);
        gpu.setText(one[8]);
        ram.setText(one[9]);
        rom.setText(one[10]);
        exrom.setText(one[11]);
        technology.setText(two[0]);
        bluetooth.setText(two[1]);
        if (two[2].equals("1"))
            wifi.setText("รองรับ");
        else
            wifi.setText("ไม่รองรับ");
        if (two[3].equals("1"))
            nfc.setText("รองรับ");
        else
            nfc.setText("ไม่รองรับ");
        if (two[4].equals("1"))
            gps.setText("รองรับ");
        else
            gps.setText("ไม่รองรับ");
        fcam.setText(three[0]);
        bcam.setText(three[1]);
        vedio.setText(three[2]);
        battery.setText(four[0]);
        color.setText(four[1]);
        if (four[2].equals("1"))
            duosim.setText("รองรับ");
        else
            duosim.setText("ไม่รองรับ");
        if (four[3].equals("1"))
            waterproof.setText("รองรับ");
        else
            waterproof.setText("ไม่รองรับ");

        btnSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SwapCheck == 0) {
                    SwapCheck = 1;
                    btnSwap.setText("สลับ " + fill_compare.get(+0).get(KEY_TITLE));

                    String[] one, two, three, four;
                    one = fill_compare.get(+1).get(KEY_ONE).split(":");
                    two = fill_compare.get(+1).get(KEY_TWO).split(":");
                    three = fill_compare.get(+1).get(KEY_THREE).split(":");
                    four = fill_compare.get(+1).get(KEY_FOUR).split(":");


                    name.setText(one[0]);
                    announced.setText(one[1]);
                    dimensions.setText(one[2]);
                    screen.setText(one[3]);
                    weight.setText(one[4]);
                    os.setText(one[5]);
                    resolution.setText(one[6]);
                    cpu.setText(one[7]);
                    gpu.setText(one[8]);
                    ram.setText(one[9]);
                    rom.setText(one[10]);
                    exrom.setText(one[11]);
                    technology.setText(two[0]);
                    bluetooth.setText(two[1]);
                    if (two[2].equals("1"))
                        wifi.setText("รองรับ");
                    else
                        wifi.setText("ไม่รองรับ");
                    if (two[3].equals("1"))
                        nfc.setText("รองรับ");
                    else
                        nfc.setText("ไม่รองรับ");
                    if (two[4].equals("1"))
                        gps.setText("รองรับ");
                    else
                        gps.setText("ไม่รองรับ");
                    fcam.setText(three[0]);
                    bcam.setText(three[1]);
                    vedio.setText(three[2]);
                    battery.setText(four[0]);
                    color.setText(four[1]);
                    if (four[2].equals("1"))
                        duosim.setText("รองรับ");
                    else
                        duosim.setText("ไม่รองรับ");
                    if (four[3].equals("1"))
                        waterproof.setText("รองรับ");
                    else
                        waterproof.setText("ไม่รองรับ");

                } else {
                    SwapCheck = 0;
                    btnSwap.setText("สลับ " + fill_compare.get(+1).get(KEY_TITLE));

                    String[] one, two, three, four;
                    one = fill_compare.get(+0).get(KEY_ONE).split(":");
                    two = fill_compare.get(+0).get(KEY_TWO).split(":");
                    three = fill_compare.get(+0).get(KEY_THREE).split(":");
                    four = fill_compare.get(+0).get(KEY_FOUR).split(":");

                    name.setText(one[0]);
                    announced.setText(one[1]);
                    dimensions.setText(one[2]);
                    screen.setText(one[3]);
                    weight.setText(one[4]);
                    os.setText(one[5]);
                    resolution.setText(one[6]);
                    cpu.setText(one[7]);
                    gpu.setText(one[8]);
                    ram.setText(one[9]);
                    rom.setText(one[10]);
                    exrom.setText(one[11]);
                    technology.setText(two[0]);
                    bluetooth.setText(two[1]);
                    if (two[2].equals("1"))
                        wifi.setText("รองรับ");
                    else
                        wifi.setText("ไม่รองรับ");
                    if (two[3].equals("1"))
                        nfc.setText("รองรับ");
                    else
                        nfc.setText("ไม่รองรับ");
                    if (two[4].equals("1"))
                        gps.setText("รองรับ");
                    else
                        gps.setText("ไม่รองรับ");
                    fcam.setText(three[0]);
                    bcam.setText(three[1]);
                    vedio.setText(three[2]);
                    battery.setText(four[0]);
                    color.setText(four[1]);
                    if (four[2].equals("1"))
                        duosim.setText("รองรับ");
                    else
                        duosim.setText("ไม่รองรับ");
                    if (four[3].equals("1"))
                        waterproof.setText("รองรับ");
                    else
                        waterproof.setText("ไม่รองรับ");
                }
            }
        });

        mBuilder.setView(cView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }
}
