package com.arsenicbismuth.syncedform;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ScrollingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    // Contains every component IDs inside content_scrolling.xml
    private static ArrayList<View> components = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewGroup main = findViewById(R.id.lay_main);
        components = getChildren(main);
    }

    private ArrayList<View> getChildren(View v) {

        // Anything with child(s) is a ViewGroup, end recursion if not
        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        // Loop inside current group, and compile results from every child
        ViewGroup vg = (ViewGroup) v;
        Log.i("ChildCount",String.valueOf(vg.getChildCount()));
        for (int i = 0; i < vg.getChildCount(); i++) {

            View child = vg.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getChildren(child));   // Recursion

            result.addAll(viewArrayList);
        }

        // Return to parent
        return result;
    }

    private Map<String, String> getData() {
        // Collect all input data
        Map<String, String> result = new HashMap<>();

        for (View comp : components) {
            // Get every EditText's tag & text.
            if (comp instanceof EditText) {
                result.put(comp.getTag().toString(), ((EditText) comp).getText().toString());
            }
        }
        return result;
    }

    // Assigned to the fab (floating action button) onClick parameter.
    public void postSheet(final View view) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://script.google.com/macros/s/AKfycbyZmIKdI4HMb0boa7mXF3LeaEadp-NCNJBOvspt1uMO5sAe5Hc/exec";

        // Collect all data to send
        final Map<String, String> allData = getData();
        allData.putAll(allRadio);  // Combine with radio data
        allData.putAll(allCheck);  // Combine with check data

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Snackbar.make(view, "Response: " + response, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(view, "No response", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
                return allData;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    // Class variables for storing radioButton states
    private Map<String, String> allRadio = new HashMap<>();
    private Map<String, String> allCheck = new HashMap<>();

    // Assigned to every RadioButton onClick parameter.
    public void onRadioClicked(View view) {
        // Check if button currently checked
        boolean checked = ((RadioButton) view).isChecked();

        // Tag naming format: group_pick. Ex: sex_female
        String[] tag = view.getTag().toString().split("_");
        String group = tag[0];
        String pick = tag[1];

        // Put all data
        if(checked) {
            allRadio.put(group, pick);
        }
    }

    // Assigned to every check box onClick parameter.
    public void onCheckClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        // Applies for every check, each must contains tag
        if (checked) {
            allCheck.put(view.getTag().toString(), "v");
        } else {
            allCheck.put(view.getTag().toString(), "");
        }
    }

    EditText picked;
    // Assigned to every date EditText onClick parameter.
    public void showDateDialog(View view) {
        picked = (EditText) view;   // Store the dialog to be picked

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
        // If done picking date
        String date = d + "/" + (m+1) + "/" + y;
        picked.setText(date);
    }
}
