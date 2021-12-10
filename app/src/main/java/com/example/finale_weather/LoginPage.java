package com.example.finale_weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class LoginPage extends AppCompatActivity {
    TextInputEditText NewUser,NewPassword,NewConfirmPassword;
    TextInputEditText oldUser,oldPassword;
    Button SignInButton,SignUpButton;
    TextView goToSignInTV,goToSignUpTV;
    RelativeLayout signUpRL,signInRL;
    MyDBHelper myDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        NewUser=findViewById(R.id.idNewUserName);
        NewPassword=findViewById(R.id.idNewPassword);
        NewConfirmPassword=findViewById(R.id.idNewConfirmPassword);
        SignUpButton=findViewById(R.id.idsignupButton);
        goToSignInTV=findViewById(R.id.mainTVGoToSignIn);
        oldUser=findViewById(R.id.idAlreadyUserName);
        oldPassword=findViewById(R.id.idAlreadyUserPassword);
        SignInButton=findViewById(R.id.idsigninButton);
        goToSignUpTV=findViewById(R.id.mainTVGoToSignUp);
        signInRL=findViewById(R.id.mainRLSignIn);
        signUpRL=findViewById(R.id.mainRLSignUp);
        myDBHelper=new MyDBHelper(this);

        goToSignInTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpRL.setVisibility(View.GONE);
                signInRL.setVisibility(View.VISIBLE);
            }
        });
        goToSignUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInRL.setVisibility(View.GONE);
                signUpRL.setVisibility(View.VISIBLE);
            }
        });


    }
    public static boolean EmailValidation(String s)
    {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(s.matches(emailPattern))return true;
        return false;
    }

    public void setSignUpButton(View view)
    {

        String user=NewUser.getText().toString();
        String password=NewPassword.getText().toString();
        String confirmPassword=NewConfirmPassword.getText().toString();
        if (user.equals("")||password.equals("")||confirmPassword.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Please Enter All Fields", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(EmailValidation(user)) {
                if (password.equals(confirmPassword)) {

                    boolean checkDB = myDBHelper.checkUsername(user);
                    if (checkDB) {
                        Toast.makeText(getApplicationContext(), "User already exists! Please Sign in", Toast.LENGTH_SHORT).show();
                    } else {
                        NewUser.setText("");
                        NewPassword.setText("");
                        NewConfirmPassword.setText("");
                        oldUser.setText("");
                        oldPassword.setText("");

                        boolean insertingData = myDBHelper.insertData(user, password);
                        Toast.makeText(getApplicationContext(), "Registered Successfully!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginPage.this, MainActivity.class);
                        startActivity(intent);

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Passwords are not Matched", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Invalid Email Address", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setSignInButton(View view)
    {
        String user=oldUser.getText().toString();
        String password=oldPassword.getText().toString();

        if (user.equals("")||password.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Please Enter All Fields", Toast.LENGTH_SHORT).show();
        }
        else
        {
            boolean checkDB=myDBHelper.checkBothField(user,password);
            if(checkDB)
            {
                NewUser.setText("");
                NewPassword.setText("");
                NewConfirmPassword.setText("");
                oldUser.setText("");
                oldPassword.setText("");

                Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Email or Password is wrong!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}