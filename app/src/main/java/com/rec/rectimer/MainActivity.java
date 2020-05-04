package com.rec.rectimer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TimePickerFragment.TimePickerListener {

    private TextView txtTimer;
    private TextView txtData;
    private Runnable runnable;
    private Handler handler = new Handler();
    private TextView txtAlarm;
    private TextView btnIO;
    private TextToSpeech txtfala;
    private Switch btnSwIO;
    private static final String ARQUIVO_PREFERENCIA = "ArquivoPreferencia";

    private String novHora = "0";
    private String novMin = "0";
    private String novOnOff = "false";

    private  String[] osMeses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
    private  String[] osDiaSemana = {"Domingo", "Segunda", "Terca", "Quarta", "Quinta", "Sexta", "Sábado"};

    // ACTYVITIS LIFE
    // TRABALHAR COM SINTETIZADOR
    // TRABALHAR BANCOS DE DADOS
    // TRABALHAR COM ACCESSOSO DE ARQUIVOS
    //

    // ALARMA FUNCIONANDO EM 2 PLANO CILO DE VIDA ACTIVITY
    // POR O DIA NA DATA
    // ANDROID SINTETIZADOR
    // Lista de Alarmas programables
    // Descargar Bancos de voces e alarmas
    // Criar Rutinas (Tomar Bnhao, Café da manhá, )


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // INICIO
        txtTimer = findViewById(R.id.txtTimer);
        txtData = findViewById(R.id.txtData);
        btnIO = findViewById(R.id.btnIO);
        btnSwIO = findViewById(R.id.btnSwIO);
        AtualizarHora();

        // TIME PIKER
        txtAlarm = findViewById(R.id.txtAlarm);
        txtAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setCancelable(false);
                timePickerFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        // TTS
        txtfala = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    //txtfala.setLanguage(Locale.US);
                    txtfala.setLanguage(Locale.getDefault());
                }
            }
        });

        // RECUPERA VALOR SALVO AL INICIO
        SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
        novOnOff = preferences.getString("onOff", "false");
        novHora = preferences.getString("hour", "0");
        novMin = preferences.getString("minute", "0");

        txtAlarm.setText(novHora + " : " + novMin);

        //Toast.makeText(getApplicationContext(), novOnOff, Toast.LENGTH_SHORT).show();

        if ( novOnOff.equals("true") ) {
            btnSwIO.setChecked(true);
            btnIO.setText("ON");
            btnIO.setTextColor(Color.parseColor("#00FF00"));
            txtAlarm.setTextColor(Color.parseColor("#00FF00"));
        }
        else{
            btnSwIO.setChecked(false);
            btnIO.setText("OFF");
            btnIO.setTextColor(Color.parseColor("#FF0000"));
            txtAlarm.setTextColor(Color.parseColor("#FF0000"));
        }

        //===================================================================================
        btnSwIO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), novOnOff + " novOnOff", Toast.LENGTH_SHORT).show();

                // SALVANDO DADOS
                SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
                SharedPreferences.Editor editor = preferences.edit();

                if ( btnSwIO.isChecked() ) {
                    novOnOff = "true";
                    btnIO.setText("ON");
                    btnIO.setTextColor(Color.parseColor("#00FF00"));
                    btnSwIO.setChecked(true);
                    editor.putString("onOff", "true");
                    editor.commit();
                    txtAlarm.setTextColor(Color.parseColor("#00FF00"));

                }else{
                    btnIO.setText("OFF");
                    novOnOff = "false";
                    btnIO.setTextColor(Color.parseColor("#FF0000"));
                    btnSwIO.setChecked(false);
                    editor.putString("onOff", "false");
                    editor.commit();
                    txtAlarm.setTextColor(Color.parseColor("#FF0000"));
                }
            }
        });
        //===================================================================================
    }


    private void AtualizarHora() {
        final Calendar calendar = Calendar.getInstance();
        final Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        runnable = new Runnable() {
            @Override
            public void run() {

                calendar.setTimeInMillis(System.currentTimeMillis());

                //Toast.makeText(getApplicationContext(), calendar.get(Calendar.DAY_OF_WEEK) + " DAY_OF_WEEK", Toast.LENGTH_SHORT).show();

                String tiempo = String.format("%02d:%02d:%02d",
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        calendar.get(Calendar.SECOND));
                txtTimer.setText(tiempo);

                String data = String.format("%s , %02d / %s / %02d",
                        osDiaSemana[calendar.get(Calendar.DAY_OF_WEEK)-1],
                        calendar.get(Calendar.DATE),
                        osMeses[calendar.get(Calendar.MONTH)],
                        calendar.get(Calendar.YEAR));
                txtData.setText(data);

                

                long agora = SystemClock.uptimeMillis();
                long proximo = agora + (1000 - (agora % 1000));
                handler.postAtTime(runnable, proximo);

                //velSalvo();

                // =========================================================================================================================
                // RECUPERAR DADOS
//                SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
//                if ( preferences.contains("hour") && preferences.contains("minute") ){
//                    String hour = preferences.getString("hour", "");
//                    String minute = preferences.getString("minute", "");
//
//                    txtAlarm.setText(hour + " : " + minute);
//                    //txtAlarm.setTextColor(Color.parseColor("#0000FF"));
//                }
//                else{
//                    txtAlarm.setText("Paila");
//                }

                // =========================================================================================================================
                // IF WOW

                //Toast.makeText(getApplicationContext(), novHora + " " + novMin + " " + novOnOff, Toast.LENGTH_SHORT).show();
//                System.out.println( calendar.get(Calendar.MINUTE) + " +++" );
//                System.out.println( Integer.parseInt(novMin) + " ---" );
//                if ( calendar.get(Calendar.MINUTE) != Integer.parseInt(novMin) ){
//                    Toast.makeText(getApplicationContext(), novHora + " " + novMin + " " + novOnOff, Toast.LENGTH_SHORT).show();
//                }



                String txtHoraMinuto = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
                String txtAlarmHoraMinuto = novHora + ":" + novMin;

                //System.out.println(txtHoraMinuto+" txtHoraMinuto");
                //System.out.println(txtAlarmHoraMinuto+" txtAlarmHoraMinuto");

                if ( novOnOff.equals("true") && !txtHoraMinuto.equals(txtAlarmHoraMinuto) ) {
                    //Toast.makeText(getApplicationContext(), novHora + " *** " + novMin + " *** " + novOnOff, Toast.LENGTH_SHORT).show();
                    if (calendar.get(Calendar.SECOND) % 30 == 0) {
                        vib.vibrate(100);
                        if (calendar.get(Calendar.MINUTE) == 0) {
                            falaTxt(calendar.get(Calendar.HOUR_OF_DAY) + " horas");
                        } else if (calendar.get(Calendar.SECOND) == 30) {
                            falaTxt(calendar.get(Calendar.HOUR_OF_DAY) + " horas " + calendar.get(Calendar.MINUTE) + " minutos e meio ");
                        } else {
                            falaTxt(calendar.get(Calendar.HOUR_OF_DAY) + " horas e " + calendar.get(Calendar.MINUTE));
                        }

                    }
                }

                else if( novOnOff.equals("true") && txtHoraMinuto.equals(txtAlarmHoraMinuto) ){
                    if (calendar.get(Calendar.SECOND) % 5 == 0) {
                        vib.vibrate(1000);
                        if (calendar.get(Calendar.MINUTE) == 0) {
                            falaTxt(calendar.get(Calendar.HOUR_OF_DAY) + " horas");
                        } else {
                            falaTxt(calendar.get(Calendar.HOUR_OF_DAY) + " horas e " + calendar.get(Calendar.MINUTE));
                        }
                    }
                }

                // =========================================================================================================================
            }
        };
        runnable.run();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {

        // =========================================================================================================================
        // SALVANDO DADOS
        SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
        SharedPreferences.Editor editor = preferences.edit();

//        if (editAlarm.getText().toString().equals("")) {
//            Toast.makeText(getApplicationContext(), "Falta Alarma", Toast.LENGTH_LONG).show();
//        } else {
        //String alarm = editAlarm.getText().toString();
        editor.putString("hour", String.valueOf(hour));
        editor.putString("minute", String.valueOf(minute));
        editor.putString("onOff", "true");
        editor.commit();

        // MOSTRA QUE SALVA
        btnIO.setText("ON");
        btnIO.setTextColor(Color.parseColor("#00FF00"));
        novOnOff = "true";
        btnSwIO.setChecked(true);
        txtAlarm.setText(hour + " : " + minute);
        txtAlarm.setTextColor(Color.parseColor("#00FF00"));

        novHora = String.valueOf(hour);
        novMin = String.valueOf(minute);
        novOnOff = "true";

        // FALA QUE SALVOU
        falaTxt("Eu vou encher seu saco até " + hour + " horas e " + minute);
        //}
        // =========================================================================================================================
    }



    private void falaTxt(String falartxt) {
        String toSpeak = falartxt;
        //Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
        txtfala.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void onPause() {
        if (txtfala != null) {
            txtfala.stop();
            txtfala.shutdown();
        }
        super.onPause();
    }

}
