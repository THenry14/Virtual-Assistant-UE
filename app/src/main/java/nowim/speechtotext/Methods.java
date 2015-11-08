package nowim.speechtotext;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Methods extends AppCompatActivity {

    //public TextView textDetail;

    public static void meth(){

    }
/*  

    Stefan, te metody przeniesione do pliku głównego powinny być właśnie tu - jednak tak jak mówiłem, null pointer exception się wywala

    public void readContacts () {
        textDetail.setText("");
        StringBuffer sb = new StringBuffer();
        sb.append("......Contact Details.....");
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        String phone = null;


        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur .getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (Integer.parseInt(cur.getString(cur .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    sb.append("\n Contact Name:" + name);
                    Cursor pCur = cr.query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
                    while (pCur.moveToNext()) {
                        phone = pCur .getString(pCur .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        sb.append("\n Phone number:" + phone);
                    }
                    pCur.close();}
                sb.append("\n........................................"); } textDetail.setText(sb);

        }


    }

*/

 /*   public void makeCall () {
        String personToCall = RecognisedText;

                if (RecognisedText.getText().toString().contains(name)) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse(phone));
                    startActivity(callIntent)
                }
    }
*/
}
