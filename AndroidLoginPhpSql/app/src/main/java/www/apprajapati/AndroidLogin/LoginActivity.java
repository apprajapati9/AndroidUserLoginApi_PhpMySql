package www.apprajapati.AndroidLogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Ajay on 7/1/2016.
 */
public class LoginActivity extends AppCompatActivity {

    private TextView userText;
    private TextView messageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_acitivity);

        userText = (TextView) findViewById(R.id.login_user) ;
        messageView = (TextView) findViewById(R.id.message);

        //getting an Intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        //getting the value passed while logged in inside Intent
        String loggedInUser = bundle.getString("USERNAME");
        String message = bundle.getString("MESSAGE");
        String upperCase = capitalizeFirstCharacter(loggedInUser);
        userText.setText(upperCase);
        messageView.setText(message);

    }
    private String capitalizeFirstCharacter(String textInput){
        String input = textInput.toLowerCase();
        String output = input.substring(0, 1).toUpperCase() + input.substring(1);
        return output;
    }
}
