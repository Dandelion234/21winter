package com.example.twthomeworkjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class LoginInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);
        if (getSupportActionBar() !=null){      //隐藏标题栏
            getSupportActionBar().hide();
        }
        EditText account=findViewById(R.id.loginAccount);
        EditText password=findViewById(R.id.loginPassword);
        Button loginIn=findViewById(R.id.loginIn);
        CheckBox checkBox=findViewById(R.id.check_box);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {    //显示密码
            if(isChecked)
            {
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                password.setSelection(password.getText().length());
            }else password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            password.setSelection(password.getText().length());
        });

        loginIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account.getText().toString().equals("123456") && password.getText().toString().equals("123456")){
                    Toast.makeText(LoginInActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent();
                    intent.putExtra("LoginIn","已登录");
                    setResult(RESULT_OK,intent);
                    finish();
                }
                else {
                    Toast.makeText(LoginInActivity.this,"用户名或密码不正确",Toast.LENGTH_SHORT).show();
                    password.setText("");
                }
            }
        });



    }

}