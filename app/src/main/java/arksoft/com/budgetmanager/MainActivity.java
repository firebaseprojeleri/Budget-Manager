package arksoft.com.budgetmanager;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import Adapter.CustomAdapter;
import FireBase.FireBaseHelper;
import Model.Income;

public class MainActivity extends AppCompatActivity {
    DatabaseReference db;
    FireBaseHelper helper;
    FloatingActionButton fab;
    CustomAdapter adapter;
    ListView lv;
    EditText nameEditTxt, propTxt, descTxt;
    List<Income> incomelist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_content);

        //Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        lv=(ListView)findViewById(R.id.lv);

        db= FirebaseDatabase.getInstance().getReference();
        //isa
        helper=new FireBaseHelper(db);
        adapter = new CustomAdapter(MainActivity.this, helper.getIncomes());
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayInput();
            }
        });

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Income doc =dataSnapshot.getValue(Income.class);
                incomelist.add(doc);
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void displayInput() {
        final Dialog d=new Dialog(this);

        d.setTitle("Gelir Girin");

        d.setContentView(R.layout.inputlayout);

        nameEditTxt = (EditText) d.findViewById(R.id.nameEditText);
        propTxt = (EditText) d.findViewById(R.id.propellantEditText);
        descTxt = (EditText) d.findViewById(R.id.descEditText);
        Button saveBtn = (Button) d.findViewById(R.id.saveBtn);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditTxt.getText().toString();
                String type = propTxt.getText().toString();
                String  amount= String.valueOf(descTxt.getText().toString());

                Income income=new Income(name,type,amount);




                if (name != null && name.length() > 0) {
                    //THEN SAVE
                    if (helper.save(income)) {

                        adapter.notifyDataSetChanged();
                        //IF SAVED CLEAR EDITXT
                        d.cancel();
                        nameEditTxt.setText("");
                       propTxt.setText("");
                        descTxt.setText("");


                    }
                } else {
                    Toast.makeText(MainActivity.this, "Name Must Not Be Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

        d.show();




    }
}
