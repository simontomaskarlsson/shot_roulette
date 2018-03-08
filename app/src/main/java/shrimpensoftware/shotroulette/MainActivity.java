package shrimpensoftware.shotroulette;

import android.icu.util.Calendar;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;
import java.util.Random;

import java.sql.Time;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    //Doubles (integers)
    Double numRows, numColumns, calcNumber;
    //Buttons
    Button startButton, calcButton, backButton;
    //Text fields
    TextView shotNumberText;
    //Other
    ViewFlipper viewFlipper;
    EditText calcNumberText, numRowsText, numColumnsText;
    MediaPlayer shot;
    MediaPlayer slurp;
    private Handler mHandler = new Handler();

    //random sniff
    Random randomGenerator = new Random();
    Integer sniff;
    Integer count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Music
        shot = MediaPlayer.create(this, R.raw.shot);
        slurp = MediaPlayer.create(this, R.raw.slurp);

        //strings
        numRowsText = (EditText) findViewById(R.id.numRowsText);
        numColumnsText = (EditText) findViewById(R.id.numColumnsText);
        calcNumberText = (EditText) findViewById(R.id.calcNumber);
        //Buttons
        startButton = (Button) findViewById(R.id.startButton);
        calcButton = (Button) findViewById(R.id.calcButton);
        backButton = (Button) findViewById(R.id.backButton);
        //Text fields
        shotNumberText = (TextView) findViewById(R.id.shotNumberText);
        //Viewflipper
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);




        // Button listeners
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRoulette();
                slurp.start();
                sniff = randomGenerator.nextInt((int)Math.floor((numColumns*numRows/2)));
            }
        });

        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateShotNumber();
                shot.start();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showNext();
            }
        });
    }


    //Functions
    public void startRoulette() {
        numRows = Math.ceil(Double.parseDouble(numRowsText.getText().toString()));
        numColumns = Math.ceil(Double.parseDouble(numColumnsText.getText().toString()));
        viewFlipper.showNext();
    }

    public void calculateShotNumber() {
        if (calcNumberText.getText().toString().matches("")) {
            this.shotNumberText.setText(":*(");
        }
        else if (count == sniff) {
            this.shotNumberText.setText("Congratulations, you got a SNIFF!");
            sniff = randomGenerator.nextInt((int)(Math.floor(numColumns*numRows/2)));
            count = 0;
        }
        else{
            this.shotNumberText.setText("Calculating...");
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    count++;
                    //Variables
                    calcNumber = Math.ceil(Double.parseDouble(calcNumberText.getText().toString()));

                    //Algorithm
                    Double rowResult = Math.sqrt(calcNumber*calcNumber*21) + numColumns/2;
                    Double columnResult = Math.sqrt(Math.abs(calcNumber)) + numRows/2;

                    //Quality checks
                    if (rowResult == 0) {
                        rowResult = 1.0;
                    }
                    if (columnResult == 0) {
                        columnResult = 1.0;
                    }

                    //Get something within playing field
                    rowResult = Math.ceil(rowResult);
                    columnResult = Math.floor(columnResult);
                    //Modulus rowResult
                    Double left = rowResult;
                    Double mod = 0.0;
                    while (left >= 0) {
                        mod = left;
                        left -= numRows;
                    }
                    rowResult = mod+1;

                    //Modulus columnResult
                    left = columnResult;
                    mod = 0.0;
                    while (left >= 0) {
                        mod = left;
                        left -= numColumns;
                    }
                    columnResult = mod+1;

                    //Set the text
                    String strRow = rowResult.toString();
                    strRow = strRow.substring(0, strRow.length() - 2);
                    String strColumn = columnResult.toString();
                    strColumn = strColumn.substring(0, strColumn.length() - 2);
                    String resultString = strRow + " x " + strColumn;
                    shotNumberText.setText(resultString);
                }
            }, 7000);
        }
    }
}

