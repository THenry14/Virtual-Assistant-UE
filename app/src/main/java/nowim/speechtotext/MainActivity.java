package nowim.speechtotext;


/*
Wojciech Szymczyk, Michał Czerwień
Virtual Personal Assistant, ver 0.1
Api level: 17


 */
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
//import android.app.Activity;                          //probably will use it for working in widget-like fashion as messenger and in background
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;                 //now only for recognition, later on let's use speech.RecognizerResultsIntent or speech* to obtain and display the results of a search
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    protected static final int RESULT_SPEECH = 1;

    private ImageButton ButtonToRecord;                 // initialize button
    private TextView RecognisedText;                    // initialize text area

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);      // view declared in xml, You can change the layout there

        RecognisedText = (TextView) findViewById(R.id.RecognisedText);
        ButtonToRecord = (ImageButton) findViewById(R.id.ButtonToRecord);

        ButtonToRecord.setOnClickListener(new View.OnClickListener() {      // creation of layout, found by ID

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);          // after pressing button, perform the action of recognition

                //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {                                                       // if speech recognition not supported, exception is being thrown
                    startActivityForResult(intent, RESULT_SPEECH);
                    RecognisedText.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Device doesn't support speech recognition",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                             // we can probably get rid of this, but for now we have a simple menu with settings, which does nothing. Here may be exit button implemented
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {     // result of the activity
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {                                                          // in our case speech recognition is chosen all the time since RESULT_SPEECH=1
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {                          // the speech was recognised and is not silence

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    RecognisedText.setText(text.get(0));
                }
                break;
            }

        }
    }

    private void AboutText(){

        TextView AboutTXT;

        AboutTXT = (TextView) findViewById(R.id.AboutTXT);

        AboutTXT.setText("Virtual Personal Assistant,\n ver 0.1\n\n " +
                "Wojciech Szymczyk,\n Michał Czerwień\n" );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                               // what happens after clicking the menu's option (settings)
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //all commented for now, cuz I try to do it different way

       /* int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        else if (id == R.id.action_about) {
            return true;
        }

        else if (id == R.id.action_exit) {
            return true;
        }

        return super.onOptionsItemSelected(item); */

        switch (item.getItemId()) {

            case R.id.action_settings:

                // DO SOMETHING HERE

                return true;

            case R.id.action_about:

                AboutText();

                return true;

            case R.id.action_exit:

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}