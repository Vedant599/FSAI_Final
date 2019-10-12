package com.example.helpdroid1223;

import androidx.annotation.NonNull;


import androidx.appcompat.app.AppCompatActivity;



import android.app.ProgressDialog;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;

import android.widget.EditText;

import android.widget.TextView;

import android.widget.Toast;



import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.FirebaseDatabase;



public class Login extends AppCompatActivity {



    private String FN;

    private EditText Email;

    private EditText Password;

    private Button LoginBtn;

    private TextView Info;

    private TextView Register;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    private int counter=5;

    private TextView forgotPassword;

    private FirebaseDatabase firebaseDatabase;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        //Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
        if (user != null) {
            Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Login.this, NavigationDrawerActivity.class));
            //finish();

        }

        Email = (EditText) findViewById(R.id.et_email);

        Password = (EditText) findViewById(R.id.et_password);

        LoginBtn = (Button) findViewById(R.id.btn_login);

        Info = (TextView) findViewById(R.id.tv_attempts);

        Register = (TextView) findViewById(R.id.tv_register);

        forgotPassword = (TextView)findViewById(R.id.tv_forgotPass);



        Info.setText("Number of attempts remaining are: 5");

        firebaseAuth=FirebaseAuth.getInstance();

//Firebase functions..



//progress dialogue

        progressDialog = new ProgressDialog(this);



        LoginBtn.setOnClickListener(new View.OnClickListener() { //Method to do something when a button or a text is clicked...

            @Override

            public void onClick(View view) {

                if (check()) { //Check the methods first get the text and then convert it to string...

                    validate(Email.getText().toString(), Password.getText().toString());

                }



            }

        });



        Register.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                Intent intent = new Intent(Login.this, Register.class);

                startActivity(intent);

//Login_Student.this.finish();

            }

        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                startActivity(new Intent(Login.this, Forgot_Password.class));

//Login_Student.this.finish();

            }

        });

    }



    private boolean check() {

        boolean result = false;

        String userName = Email.getText().toString();

        String userPassword = Password.getText().toString();

        if (userName.isEmpty()) {

            Toast.makeText(this, "Please enter the user name", Toast.LENGTH_SHORT).show();

            return result;

        }

        if (userPassword.isEmpty()) {

            Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show();

            return result;

        }

        result = true;

        return result;

    }



    public void validate(String userEmail, String userPassword) {

        progressDialog.setMessage("Please wait while the verification is done");

        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override

            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    progressDialog.dismiss();
                    startActivity(new Intent(Login.this, NavigationDrawerActivity.class));

//finish();

/*Toast.makeText(Login_Student.this, "Successful Login", Toast.LENGTH_SHORT).show();

startActivity(new Intent(Login_Student.this, Student_Home_page.class));*/

                    checkEmailVerification();

                } else {

                    Toast.makeText(Login.this, "Unsuccessful", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();

                    counter--;

                    Info.setText("Number of attempts remaining are: "+counter);

                    if(counter==0){

                        LoginBtn.setEnabled(false);

                    }

                }

            }

        });

    }



    public void checkEmailVerification(){

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Boolean check = firebaseUser.isEmailVerified();



        if(check){

//finish();

    //        Intent intent = new Intent(Login.this, UserHomePage.class);

//Here FN is my string...

            //startActivity(intent);

        }else{

            Toast.makeText(this,"Please verify the email",Toast.LENGTH_SHORT).show();;

            firebaseAuth.signOut();

        }

    }

}
