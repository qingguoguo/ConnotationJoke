package com.qingguoguo.connotationjoke;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.connotationjoke.qingguoguo.baselibrary.ioc.CheckNet;
import com.connotationjoke.qingguoguo.baselibrary.ioc.OnClick;
import com.connotationjoke.qingguoguo.baselibrary.ioc.ViewById;
import com.connotationjoke.qingguoguo.baselibrary.ioc.ViewUtils;


public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.test_tv)
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        mTextView.setText("哈哈哈,注入成功");
    }

    @CheckNet
    @OnClick({R.id.test_tv,R.id.test_iv})
    private void testOnclick() {
        Toast.makeText(this,"哈哈哈,注入事件成功",Toast.LENGTH_SHORT).show();
    }
}
