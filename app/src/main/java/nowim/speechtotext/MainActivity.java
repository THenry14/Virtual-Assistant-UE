package nowim.speechtotext;
/*
Wojciech Szymczyk, Michał Czerwień
Virtual Personal Assistant, ver 0.2
Api level: 17


 */
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
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
    About about = new About();
    // Methods read = new Methods();
    public TextView textDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);      // view declared in xml, You can change the layout there

        RecognisedText = (TextView) findViewById(R.id.Text);
        ButtonToRecord = (ImageButton) findViewById(R.id.ButtonToRecord);
        about.AboutText = (TextView) findViewById(R.id.Text);
        //read.
        textDetail = (TextView) findViewById(R.id.Text);


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
                    RecognisedText.setText("");
                    RecognisedText.setText(text.get(0));
                }
                break;
            }

        }
    }

    public void readContacts () {
        textDetail.setText("");
        StringBuffer sb = new StringBuffer();
        sb.append("......Contact Details.....");
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        String phone = null;


        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    sb.append("\n Contact Name:" + name);
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        sb.append("\n Phone number:" + phone);
                    }
                    pCur.close();
                }
                sb.append("\n........................................");
            }
            textDetail.setText(sb);

        }
        //coś tu sie odpierdoliło.
        // public void makeCall () {

        //    if (RecognisedText.getText().toString().contains(name)) {
        //        Intent callIntent = new Intent(Intent.ACTION_CALL);
        //        callIntent.setData(Uri.parse(phone));
        //        startActivity(callIntent);
    }
    //}
//}



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                               // what happens after clicking the menu's option (settings)
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case R.id.action_settings:

                //FOR TEST PURPOSES FOR NOW !!!!!! LATER ON WILL IMPLEMENT SETTINGS MENU

                //read.
                readContacts();

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