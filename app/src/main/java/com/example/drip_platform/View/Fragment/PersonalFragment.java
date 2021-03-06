package com.example.drip_platform.View.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.drip_platform.R;
import com.example.drip_platform.View.Activity.LoginActivity;

public class PersonalFragment extends Fragment implements View.OnClickListener{
    private Button SignOut_button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);

        SignOut_button = (Button) view.findViewById(R.id.signout_button);
        SignOut_button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {

        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.signout_button:
                signOut();
                break;
            default:
                break;
        }



    }
    private void signOut(){
        SharedPreferences pref = getActivity().getSharedPreferences("myActivityName", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isLogin", false);
        editor.commit();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
