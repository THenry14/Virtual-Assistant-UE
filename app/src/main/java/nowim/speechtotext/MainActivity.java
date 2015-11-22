package nowim.speechtotext;
/*
Wojciech Szymczyk, Michał Czerwień
Virtual Personal Assistant, ver 0.3
Api level: 17

 */
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//import android.app.Activity;                          //probably will use it for working in widget-like fashion as messenger and in background
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;                 //now only for recognition, later on let's use speech.RecognizerResultsIntent or speech* to obtain and display the results of a search
import android.text.TextUtils;
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
    About about = new About();
    String phone = new String();
    String name = new String();
    String id = new String();
    String number = new String();
    String nameOfContact = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);      // view declared in xml, You can change the layout there

        RecognisedText = (TextView) findViewById(R.id.Text);
        about.AboutText = (TextView) findViewById(R.id.Text);
        ButtonToRecord = (ImageButton) findViewById(R.id.ButtonToRecord);

        ButtonToRecord.setOnClickListener(new View.OnClickListener() {      // creation of layout, found by ID

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);          // after pressing button, perform the action of recognition

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

                    recognise(text);
                    readContacts(nameOfContact);
                    makeCall(number);

                }
                break;
            }
        }
    }

    public String recognise( ArrayList text){

        String obj = (String) text.get(0);
        text.clear();
        text.add(obj);
        nameOfContact = TextUtils.join(" ", text);
        // RecognisedText.setText(nameOfContact + " View from recognised field" + "\n");  //FOR LOGGING

        return nameOfContact;
    }

    public String readContacts(String nameOfContact) {
        Map<String, String> book = new HashMap<String, String>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            book.put(name, phone);
                        }
                        pCur.close();
                    }
                }
            }
            number = book.get(nameOfContact);
            //RecognisedText.setText(number + " view from readContacts field" + "\n"); //FOR LOGGING

        return number;
        }

    public void makeCall (String number) {
        if(number != null) {
                String numberToCall = "tel:" + number.trim();
                Uri Call = Uri.parse(numberToCall);
                Intent callIntent = new Intent(Intent.ACTION_CALL, Call);
                startActivity(callIntent);
            }
        else {
            Toast t = Toast.makeText(getApplicationContext(),
                    "No such contact in your phonebook", // later can add voice saying this and asking if you can add contact
                    Toast.LENGTH_SHORT);
                t.show();
            }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                               // what happens after clicking the menu's option (settings)
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case R.id.action_scenarios:

                //possible scenarios here

                return true;

            case R.id.action_settings:

                //later on

                return true;

            case R.id.action_about:

                about.AboutText();

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