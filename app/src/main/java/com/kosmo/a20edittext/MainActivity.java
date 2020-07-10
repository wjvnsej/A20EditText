package com.kosmo.a20edittext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    //Logcat을 사용하기 위한 태그 설정
    private static final String TAG = "KOSMO61";

    TextView textView1;
    EditText etId;
    EditText etPwd;
    EditText etYear;
    EditText etMonth;
    EditText etDay;

    String sId;
    String sPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = findViewById(R.id.textView1);
        etId = findViewById(R.id.etId);
        etPwd = findViewById(R.id.etPwd);
        etYear = findViewById(R.id.etYear);
        etMonth = findViewById(R.id.etMonth);
        etDay = findViewById(R.id.etDay);

        //패스워드 입력시 글자수 체크
        etPwd.addTextChangedListener(watcher);
    }

    /*
    TextWatcher
        : 안드로이드에서 문자를 입력할 때 마다 변화된 값을 보여줘야 하는 경우 사용하는 클래스
        Ex) 한국 통화를 입력시 달러단위로 변경되어 출력
            금액을 입력하면 배송비와 합산되어 실시간으로 출력
     */
    TextWatcher watcher = new TextWatcher() {

        String beforeText;

        // 1. 텍스트가 바뀌기 전 동작할 내용
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // String 으로 변경 후 로그캣에 출력
            beforeText = s.toString();
            Log.d(TAG, "beforeTextChanged : " + beforeText);
        }

        // 2. 텍스트 변경 중
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            byte[] bytes = null;
            try {
                //한글처리를 위한 케릭셋 설정
                //bytes = str.toString().getBytes("KSC5601");
                bytes = s.toString().getBytes("8859_1");
                //문자열의 길이를 확인
                int strCount = bytes.length;
                //길이를 텍스트뷰에 출력
                textView1.setText(strCount + " / 8바이트");
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        // 3. 텍스트가 변경된 이후
        /*
        setSelection()메소드
            1. 매개변수가 1개일 때
                : 커서의 위치를 index만큼 이동시킨다. index가 0이면 문자열의 제일 처음으로 이동한다.
            2. 매개변수가 2개일 때
                : 커서의 위치를 start에서 stop까지 위치시킨다.
                즉, 해당 index를 블럭(드래그로 선택영역 지정)으로 설정한다.
                만약 지정한 index에 문자열이 없는 경우 예외가 발생한다.
         */
        @Override
        public void afterTextChanged(Editable s) {
            String str = s.toString();
            Log.d(TAG, "afterTextChanged : " + str);
            try {
                byte[] strBytes = str.getBytes("8859_1");
                if(strBytes.length > 8) {
                    etPwd.setText(beforeText);
                    /*
                    9글자가 되는 순간 텍스트가 블럭으로 지정되어
                    다시 처음부터 입력할 수 있게 된다.
                     */
                    etPwd.setSelection(beforeText.length() - 9, beforeText.length() - 1);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    //키보드 내리기 버튼
    public void onKeydownClicked(View v) {
        //IME Hide
        InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    //로그인 버튼
    public void onLoginClicked(View v) {

        //아이디, 패스워드에 입력된 내용을 가져옴
        sId = etId.getText().toString();
        sPwd = etPwd.getText().toString();

        //아이디는 입력값이 있는지 확인
        if(sId.length() < 3) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("알림")
                    .setMessage("아이디를 입력해 주세요")
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("닫기", null)
                    .show();
            etId.requestFocus();
            return;
        }
        else if(sPwd.length() < 5) {
            //패스워드는 입력값이 5자 미만이면 경고창 띄움
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("알림")
                    .setMessage("비밀번호를 정확히 입력해 주세요")
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("닫기", null)
                    .show();
            etPwd.requestFocus();
            return;
        }
    }
}
























