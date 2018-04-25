package com.example.administrator.bmob;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.BreakIterator;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

public class MainActivity extends AppCompatActivity {
    private EditText etPhone;
    private EditText etNumber;
    private Button btnCountdown;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mContext= this;
//        Log.e(TAG, "onStart 方法调用");
//// SMS初始化
//        BmobSMS.initialize(, "还记得吗？这里填刚才拿到的Application ID");
//        init();
       BmobSMS.initialize(MainActivity.this,"38718e796dfb9633cefec9d7ff69b0c5");
        btnCountdown=(Button)findViewById(R.id.btnCountdown);
        btnSend=(Button)findViewById(R.id.btnSend);
        etNumber=(EditText)findViewById(R.id.etNumber);
        etPhone=(EditText)findViewById(R.id.etPhone);
        btnCountdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// 将按钮设置为不可用状态
                btnCountdown.setEnabled(false);
// 启动倒计时的服务
          //      startService(MainActivity.this);
// 通过requestSMSCode方式给绑定手机号的该用户发送指定短信模板的短信验证码
                BmobSMS.requestSMSCode(MainActivity.this, etPhone.getText().toString(), "天才", new RequestSMSCodeListener() {
                    @Override
                    public void done(Integer smsId, BmobException ex) {
                        if (ex == null) {//验证码发送成功
                            btnCountdown.setClickable(false);
                            btnCountdown.setBackgroundColor(Color.GRAY);
                            Toast.makeText(MainActivity.this,"验证码已发送，尽快使用",Toast.LENGTH_SHORT).show();
                            Log.e("bmob", "短信id：" + smsId);//用于查询本次短信发送详情
                            /**
                             * 倒计时1分钟操作
                             * 说明：
                             * new CountDownTimer(60000, 1000),第一个参数为倒计时总时间，第二个参数为倒计时的间隔时间
                             * 单位都为ms，其中必须要实现onTick()和onFinish()两个方法，onTick()方法为当倒计时在进行中时，
                             * 所做的操作，它的参数millisUntilFinished为距离倒计时结束时的时间，以此题为例，总倒计时长
                             * 为60000ms,倒计时的间隔时间为1000ms，然后59000ms、58000ms、57000ms...该方法的参数
                             * millisUntilFinished就等于这些每秒变化的数，然后除以1000，把单位变成秒，显示在textView
                             * 或Button上，则实现倒计时的效果，onFinish()方法为倒计时结束时要做的操作，此题可以很好的
                             * 说明该方法的用法，最后要注意的是当new CountDownTimer(60000, 1000)之后，一定要调用start()
                             * 方法把该倒计时操作启动起来，不调用start()方法的话，是不会进行倒计时操作的
                             */
                            new CountDownTimer(60000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    btnCountdown.setBackgroundResource(R.drawable.button_shape01);
                                    btnCountdown.setText(millisUntilFinished / 1000 + "秒");
                                }

                                @Override
                                public void onFinish() {
                                     btnCountdown.setClickable(true);
                                    btnCountdown.setBackgroundResource(R.drawable.button_shape02);
                                    btnCountdown.setText("重新发送");
                                }
                            }.start();
                            Log.e("MESSAGE:", "4");
                        } else {
                            Toast.makeText(MainActivity.this, "验证码发送失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
                        }

                });
            }
        });

//            private void startService(MainActivity mainActivity) {
//            }
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = etNumber.getText().toString();
                if (!TextUtils.isEmpty(number)){
 // 通过verifySmsCode方式可验证该短信验证码
                            BmobSMS.verifySmsCode(MainActivity.this,etPhone.getText().toString(), number, new VerifySMSCodeListener() {
                                @Override
                                public void done(BmobException ex) {
                                    if(ex==null){//短信验证码已验证成功
                                        Log.e("bmob", "验证通过");
                                    }else{
                                        Log.e("bmob", "验证失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
                                    }
                                }
                            });
                }
            }
        });

    }
   }




