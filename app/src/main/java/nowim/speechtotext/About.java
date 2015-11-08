package nowim.speechtotext;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class About extends AppCompatActivity{

    public TextView AboutText;

    public void AboutText(){
        AboutText.setText(" ");
        AboutText.setText("Virtual Personal Assistant,\n ver 0.2\n\n " +
                "Wojciech Szymczyk,\n Michał Czerwień\n" );
    }

}