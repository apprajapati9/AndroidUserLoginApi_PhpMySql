package www.apprajapati.AndroidLogin;

import android.content.Entity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajay on 7/1/2016.
 */
public class RegisterActivity extends AppCompatActivity implements Constant_ServerUrl {

    private EditText mUsername;
    private EditText mPassword;
    private EditText mEmail;
    protected Button signup;
    private String addedUsername;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);


        mUsername = (EditText) findViewById(R.id.edittext_username);
        mPassword = (EditText) findViewById(R.id.edittext_password);
        mEmail = (EditText) findViewById(R.id.edittext_email);

        signup = (Button) findViewById(R.id.sign_up);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addedUsername = mUsername.getText().toString();
                String enteredPassword = mPassword.getText().toString();
                String enteredEmail = mEmail.getText().toString();

                if (addedUsername.equals("") || enteredPassword.equals("") || enteredEmail.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Username or password or email must be filled", Toast.LENGTH_LONG).show();
                    return;
                }
                if (addedUsername.length() <= 1 || enteredPassword.length() <= 1) {
                    Toast.makeText(RegisterActivity.this, "Username or password length must be greater than one", Toast.LENGTH_LONG).show();
                    return;
                }
                // request authentication with remote server4

                AsyncRegister registration = new AsyncRegister();
                registration.execute(baseUrl, addedUsername, enteredPassword, enteredEmail);

            }
        });
    }

    private class AsyncRegister extends AsyncTask<String, Void, Integer>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);

            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(params[0]);
            String jsonResult = "";
            int answer = 0;
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("username", params[1]));
                nameValuePairs.add(new BasicNameValuePair("password", params[2]));
                nameValuePairs.add(new BasicNameValuePair("email", params[3]));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpClient.execute(httpPost);
              //  jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
                jsonResult = EntityUtils.toString(response.getEntity());
                JSONObject myObject = new JSONObject(jsonResult); //converting it into JSON
                answer = Integer.parseInt(String.valueOf(myObject.getInt("success")));
                System.out.println("Returned_registration_jsonresult:" + jsonResult.toString());
                System.out.print("value_of_ans" + answer);

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            System.out.println("Resulted Value: " + result);
            if(result.equals("") || result == null){
                Toast.makeText(RegisterActivity.this, "Server connection failed", Toast.LENGTH_LONG).show();
                return;
            }
           // int jsonResult = returnParsedJsonObject(result);
            if(result == 0){
                Toast.makeText(RegisterActivity.this, "Invalid username or password or email", Toast.LENGTH_LONG).show();
                return;
            }
            if(result == 1){
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("USERNAME", addedUsername);
                intent.putExtra("MESSAGE", "You have successfully Registered! Welcome");
                startActivity(intent);
            }
        }
//        private StringBuilder inputStreamToString(InputStream is) {
//            String rLine = "";
//            StringBuilder answer = new StringBuilder();
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//            try {
//                while ((rLine = br.readLine()) != null) {
//                    answer.append(rLine);
//                }
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            return answer;
//        }
    }
//    private int returnParsedJsonObject(String result){
//
//        JSONObject resultObject = null;
//        int returnedResult = 0;
//        try {
//            resultObject = new JSONObject(result);
//            returnedResult = resultObject.getInt("success");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return returnedResult;
//    }

}
