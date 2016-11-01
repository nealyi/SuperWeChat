package com.nealyi.superwechat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nealyi.superwechat.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btn_register, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class));
                break;
            case R.id.btn_login:
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                break;
        }
    }
}
