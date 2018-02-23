package com.example.primos;

import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity  implements FragmentOculto.TaskListener{

    private static final String TAG = MainActivity.class.getName();

    private EditText inputField, resultField;
    private Button primecheckbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputField = (EditText) findViewById(R.id.inputField);
        resultField = (EditText) findViewById(R.id.resultField);
        primecheckbutton = (Button) findViewById(R.id.primecheckbutton);


    }

    public void triggerPrimecheck(View v){
        long parameter = Long.parseLong(inputField.getText().toString());
        Bundle parametros = new Bundle();
        parametros.putLong("numComprobar", parameter);

        FragmentOculto fragment = FragmentOculto.newInstance(parametros);
        FragmentTransaction  fragmentTransaction = getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment, FragmentOculto.TAG);
        fragmentTransaction.commit();
    }


    @Override
    public void onPreExecute() {
        resultField.setText("");
        primecheckbutton.setEnabled(false);
    }

    @Override
    public void onProgressUpdate(double progreso) {
        resultField.setText(String.format("%.1f%% completado", progreso*100));
    }

    @Override
    public void onPostExecute(boolean resultado) {
        resultField.setText(resultado + "");
        primecheckbutton.setText("¿ES PRIMO?");
    }

    @Override
    public void onCancelled() {
        resultField.setText("Proceso cancelado");
        primecheckbutton.setEnabled(true);
    }
}
