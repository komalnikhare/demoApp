package com.arrk.starwars;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.arrk.starwars.models.Character;

public class CharacterDetailActivity extends AppCompatActivity {

    private TextView mTxtName, mTxtHeight,mTxtMass,mTxtDate;
    private Character mCharacter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_detail);

        mTxtName = findViewById(R.id.txt_name_value);
        mTxtHeight = findViewById(R.id.txt_height_value);
        mTxtMass = findViewById(R.id.txt_mass_value);
        mTxtDate = findViewById(R.id.txt_date_value);

        if (getIntent()!=null){
            mCharacter = getIntent().getParcelableExtra("details");

            mTxtName.setText(mCharacter.getName());
            mTxtHeight.setText(mCharacter.getHeight());
            mTxtMass.setText(mCharacter.getMass()+"kg");
            mTxtDate.setText(mCharacter.getCreated());
        }
    }
}
