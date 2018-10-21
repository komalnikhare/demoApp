package com.arrk.starwars;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arrk.starwars.dbConfig.DatabaseCreator;
import com.arrk.starwars.models.Character;
import com.arrk.starwars.services.StarWarService;
import com.arrk.starwars.utils.Utils;

import java.util.List;

public class CharatersListActivity extends AppCompatActivity implements CharacterListAdapter.ItemClickListener{

    private RecyclerView mRecyclerView;
    private CharacterListAdapter mAdapter;
    private TextView mTxtNoRecord;
    private Context mContext;
    private DatabaseCreator dbCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charaters_list);

        mContext = this;
        dbCreator = DatabaseCreator.getInstance(getApplicationContext());
        mRecyclerView = findViewById(R.id.character_list);
        mTxtNoRecord = findViewById(R.id.txt_norecord);

        mAdapter = new CharacterListAdapter(mContext, CharatersListActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.invalidate();
        getLocalData();
        getCharacters();
    }

    private void getLocalData(){
        dbCreator.getCharacters().observe(this, new Observer<List<Character>>() {
            @Override
            public void onChanged(@Nullable List<Character> characters) {
                if (characters !=null){
                    mAdapter.setList(characters);
                    mTxtNoRecord.setVisibility(View.GONE);
                }else {
                    mTxtNoRecord.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * Call service to get data
     */
    private void getCharacters(){
        if (!Utils.isNetworkAvailable(mContext)){
            Toast.makeText(mContext, R.string.no_internet, Toast.LENGTH_LONG).show();
            return;
        }
        new StarWarService(mContext).getStarCharacterList(mContext, mHandler);
    }

    /**
     * Handle service response and manage UI
     */
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 0){
                List<Character> mCharacters = (List<Character>)msg.obj;

                mTxtNoRecord.setVisibility(View.GONE);
            }else {
                mTxtNoRecord.setVisibility(View.VISIBLE);
                String error = (String)msg.obj;
                showDialog(error);
            }
        }
    };


    @Override
    public void onItemClick(Object object) {

        Character mCharacter = (Character)object;
        Intent intent = new Intent(mContext, CharacterDetailActivity.class);
        intent.putExtra("details", mCharacter);
        startActivity(intent);

    }

    /**
     * Error dialog
     * @param msg
     */
    private void showDialog(String msg){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("Error");
        builder.setMessage(msg);

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getCharacters();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
