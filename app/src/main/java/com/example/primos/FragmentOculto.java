package com.example.primos;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Indogroup02 on 23/02/2018.
 */

public class FragmentOculto extends Fragment {

    static interface TaskListener {
        void onPreExecute();
        void onProgressUpdate(double progreso);
        void onPostExecute(boolean resultado);
        void onCancelled();
    }

    public static final String TAG = FragmentOculto.class.getName();
    private TaskListener taskListener;
    private MyAsyncTask myAsyncTask;
    private long numComprobar;


    public static FragmentOculto newInstance(Bundle argumentos){
        FragmentOculto f = new FragmentOculto();
        if(argumentos != null){
            f.setArguments(argumentos);
        }
        return f;
    }

    @Override
    public void onAttach(Activity actividad){
        super.onAttach(actividad);
        try{
            this.taskListener= (TaskListener)actividad;
        }catch(ClassCastException ex){
            Log.e(TAG, "El Activity debe implementar la interfaz TaskListener");
        }
    }

    @Override public void onDetach() {
        this.taskListener = null;
        super.onDetach();
    }


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle parameters = this.getArguments();
        if(parameters != null)
        this.numComprobar = parameters.getLong("numComprobar", 0);
        myAsyncTask=new MyAsyncTask();
        myAsyncTask.execute(this.numComprobar);
    }


    private class MyAsyncTask extends AsyncTask<Long, Double, Boolean> {

        @Override
        protected void onPreExecute(){
            Log.d(TAG, "Thread " + Thread.currentThread().getId() + ": onPreExecute()");
            taskListener.onPreExecute();
            Log.d(TAG, "Estado de onPreExecute() " + myAsyncTask.getStatus().toString());

        }

        @Override
        protected Boolean doInBackground(Long... n){
            if(isCancelled()){
                return null;
            }


            Log.d(TAG, "Thread " + Thread.currentThread().getId() + ": Comienza doInBackground");
            long numComprobar = n[0];
            if(numComprobar < 2 || numComprobar % 2 == 0)
                return false;
            double limite = Math.sqrt(numComprobar) + 0.0001;
            double progreso = 0;
            for(long factor = 3; factor < limite && !isCancelled(); factor += 2){
                if(numComprobar % factor == 0){
                    return false;
                }
                if (factor > limite * progreso / 100){
                    publishProgress(progreso / 100);
                    progreso += 5;
                }
            }
            Log.d(TAG, "Thread " + Thread.currentThread().getId() + ": Finaliza doInBackground");
            Log.d(TAG, "Estado de doInBackground " + myAsyncTask.getStatus().toString());
            return true;
        }

        @Override
        protected void onProgressUpdate(Double... progress){
            Log.d(TAG, "Thread " + Thread.currentThread().getId() + ": onProgressUpdate()");
            taskListener.onProgressUpdate(progress[0]);
            Log.d(TAG, "Estado de onProgressUpdate() " + myAsyncTask.getStatus().toString());

        }

        @Override
        protected void onPostExecute(Boolean isPrime){
            taskListener.onPostExecute(isPrime);
            Log.d(TAG, "Estado de onPostExecute() " + myAsyncTask.getStatus().toString());
        }

        @Override
        protected  void onCancelled(){
            Log.d(TAG, "Thread " + Thread.currentThread().getId() + ": onCancelled");
            super.onCancelled();
            taskListener.onCancelled();
            Log.d(TAG, "Estado de onCancelled() " + myAsyncTask.getStatus().toString());
        }




    }



}
