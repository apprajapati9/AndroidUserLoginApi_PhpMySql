package www.apprajapati.AndroidLogin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Constant_ServerUrl {

    //Views for Login
    private EditText userName;
    private EditText mPassword;

    private String enteredUserName; //to store the UserName

    //Server Url, RESTAPI url where we can get store/use login details
    //private final String baseUrl = "http://10.0.2.2/android_login_api/"; //androidLogin - Apache Webserver folder with RESTAPI set up
   // private final String baseUrl = "http://28c78290.ngrok.io/androidLogin/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize the views of EditText
        userName = (EditText) findViewById(R.id.username_login);
        mPassword = (EditText) findViewById(R.id.password_login);

        //initializing buttons
        Button loginButton = (Button) findViewById(R.id.login_button);
        Button registerButton = (Button) findViewById(R.id.register_button);

        //Validation
        assert loginButton != null;
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //getting both values entered in Username and password
                enteredUserName = userName.getText().toString();
                String enteredPassword = mPassword.getText().toString();

                if(enteredUserName.equals("") || enteredPassword.equals("")){
                    Toast.makeText(MainActivity.this, "Username or Password Must not be Empty.",Toast.LENGTH_LONG).show();
                    return;
                }
                if(enteredUserName.length()<=1||enteredPassword.length()<=1){
                    Toast.makeText(MainActivity.this, "Username or Password Must be greater than four character.",Toast.LENGTH_LONG).show();
                    return;
                }
                //if everything works out... check out the details entered by user using Server
                AsyncTaskLogin loginTask = new AsyncTaskLogin();
                loginTask.execute(baseUrl,enteredUserName, enteredPassword); //PASSING THREE necessary values to check

            }
        });
        assert registerButton != null;
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });





        //REMOVE comment to enable FloatingActionButton
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//    fab.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
//        }
//    });
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


//    Params, the type of the parameters sent to the task upon execution.
//    Progress, the type of the progress units published during the background computation.
//    Result, the type of the result of the background computation.
    //In our case, this three parameters are , String =Params , Void =Progress , Result= String
    private class AsyncTaskLogin extends AsyncTask<String, Void, Integer> {


        //This is the main method in AsyncTask,  you must implement this, because this is where
        // it creates a new thread to work in background. You cannot do UI task on this thread.
        @Override
        protected Integer doInBackground(String... params) {

            //HttpParams =
            HttpParams httpParams = new BasicHttpParams();
            //BasicHttpParams = Default implementation of HttpParams interface.
            //This class represents a collection of HTTP protocol parameters.
            // Protocol parameters may be linked together to form a hierarchy.
            // If a particular parameter value has not been explicitly defined in the collection itself,
            // its value will be drawn from the parent collection of parameters.

            // HttpConnectionParams An adaptor for accessing connection parameters in HttpParams.
            HttpConnectionParams.setConnectionTimeout(httpParams,5000);
            HttpConnectionParams.setSoTimeout(httpParams,5000);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            //Interface for an HTTP client. HTTP clients encapsulate a smorgasbord of objects required to execute HTTP
            // requests while handling cookies, authentication, connection management, and other features.
            // Thread safety of HTTP clients depends on the implementation and configuration of the specific client.

            HttpPost httpPost = new HttpPost(params[0]); //Params[0] = baseUrl of our API
            String jsonResult = "";
            int answer = 0;
            try{
                List<NameValuePair> nameValuePairs = new ArrayList<>(2); //2 is capacity of ArrayList
                nameValuePairs.add(new BasicNameValuePair("username",params[1]));
                nameValuePairs.add(new BasicNameValuePair("password",params[2]));

                httpPost.setEntity(new UrlEncodedFormEntity( nameValuePairs));

                HttpResponse response = httpClient.execute(httpPost);
               // jsonResult = inputStreamToString(response.getEntity().getContent()).
                 //       toString(); //got response and converted into string and passed it to build string
                jsonResult = EntityUtils.toString(response.getEntity());
                Log.d("json_result_doinba:",jsonResult);
              //  jsonResult = EntityUtils.toString(response.getEntity());
                JSONObject myObject = new JSONObject(jsonResult);
                answer = Integer.parseInt(String.valueOf(myObject.getInt("success")));
                Log.d("answer:", String.valueOf(answer));


            }catch (ClientProtocolException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return answer;
        }


        //After getting the result you might want to redirect to another screen or getting somewhere.
        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);


            if(result == 0){
                Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
            }

            //If json result Value is 1, then it means RestApi sent "success" - SO LET THE USER LOGIN
            //if success then let the user go in LoginActivity..
            if(result == 1){
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("USERNAME", enteredUserName);
                intent.putExtra("MESSAGE", "You have successfully Logged In.");
                startActivity(intent);
            }
        }

    }

    //returning success keyword as JSON parsed
//    private int returnParsedJsonObject(String result) {
//
//       // JSONObject resultObj = null;
//        int returnedResult = 0;
//        try{
//            JSONObject resultObj = new JSONObject(result);
//            returnedResult = resultObj.getInt("success");
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
//        return returnedResult;
//    }


}
