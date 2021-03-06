package com.tripleM.sendmeal_lab01;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tripleM.sendmeal_lab01.model.CuentaBancaria;
import com.tripleM.sendmeal_lab01.model.Tarjeta;
import com.tripleM.sendmeal_lab01.model.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    EditText etNombre,etPassword,etPassword2, etEmail, etNumeroTarjeta, etCCV,etMes,etAnio,etCBU,etAlias;
    Button btnRegistrar;
    RadioGroup rg1;
    RadioButton rb1,rb2;
    Switch sCargaInicial;
    CheckBox cbAcepto;
    SeekBar sbMonto;
   TextView textView;

    boolean esCredito;
    int monto=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNombre = findViewById(R.id.nombre);
        etPassword = findViewById(R.id.contrasenia);
        etPassword2 = findViewById(R.id.contrasenia2);
        etEmail = findViewById(R.id.email);
        etNumeroTarjeta = findViewById(R.id.nrotarjeta);
        etCCV = findViewById(R.id.ccv);
        etMes = findViewById(R.id.fecha_mes);
        etAnio = findViewById(R.id.fecha_anio);
        etCBU = findViewById(R.id.cbu);
        etAlias = findViewById(R.id.cbualias);
        btnRegistrar = findViewById(R.id.registrar);
        rg1 = findViewById(R.id.rg1);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        sCargaInicial = findViewById(R.id.carga_inicial);
        sbMonto = findViewById(R.id.barra);
        cbAcepto = findViewById(R.id.term_cond);
        textView = findViewById(R.id.textView);

        etCCV.setEnabled(false);
        etMes.setEnabled(false);
        etAnio.setEnabled(false);
        sbMonto.setVisibility(View.GONE);
        btnRegistrar.setEnabled(false);
        textView.setVisibility(View.GONE);

        RadioGroup.OnCheckedChangeListener radioListenerRG1 = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb1:
                        rb2.setEnabled(false);
                        break;
                    case R.id.rb2:
                        rb1.setEnabled(false);
                        break;
                }
            }
        };

        etNumeroTarjeta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                if(cs.length()!=0){
                    etCCV.setEnabled(true);
                    etMes.setEnabled(true);
                    etAnio.setEnabled(true);
                }else{
                    etCCV.setEnabled(false);
                    etCCV.setText("");
                    etMes.setEnabled(false);
                    etMes.setText("");
                    etAnio.setEnabled(false);
                    etAnio.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sCargaInicial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(sCargaInicial.isChecked()){
                    sbMonto.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }
                else{
                    sbMonto.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                    monto=0;
                    sbMonto.setProgress(0);
                    textView.setText("Carga inicial: $0");
                }
            }
        });

        sbMonto.setMax(1500);
        sbMonto.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                monto=progress;
                textView.setText("Carga inicial: $" + progress);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        cbAcepto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cbAcepto.isChecked()){
                    btnRegistrar.setEnabled(true);
                }else{
                    btnRegistrar.setEnabled(false);
                }
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String mensaje = "";
                String mensaje_final = new String();
                String mensaje_exitoso = "Datos cargados exitosamente.";

                if(etNombre.getText().toString().length()==0) mensaje += "El nombre está vacío. \n";
                if(etPassword.getText().toString().length()==0) mensaje += "La contraseña está vacía. \n";
                else if (etPassword2.getText().toString().compareTo(etPassword.getText().toString())!=0) mensaje += "Las contraseñas no coinciden. \n";
                if(etEmail.getText().toString().length()==0) mensaje += "El e-mail está vacío. \n";
                else if(!condicionEmail(etEmail.getText().toString())) mensaje += "El e-mail debe contener un @ seguido de al menos 3 letras. \n";
                if(rg1.getCheckedRadioButtonId()==-1) mensaje += "Seleccione el tipo de tarjeta. \n";
                if(etNumeroTarjeta.getText().toString().length()!=16) mensaje += "El número de tarjeta está incompleto. \n";
                if(etCCV.getText().toString().length()!=3) mensaje += "El CCV no es correcto. \n";
                if(etCBU.getText().toString().length()>0){
                    if(etCBU.getText().toString().length()!=22) mensaje += "El CBU es incorrecto. \n";
                    if(etAlias.getText().toString().length()==0) mensaje += "El Alias es incorrecto. \n";
                }

                if(etMes.getText().toString().length()==0||etAnio.getText().toString().length()==0) mensaje += "La fecha no está completa. \n";
                else {
                    int mes = Integer.parseInt(etMes.getText().toString());
                    int anio = Integer.parseInt(etAnio.getText().toString());

                    if (!(mes >= 1 && mes <= 12) || !(anio >= 2020 && anio < 2100))
                        mensaje += "La fecha ingresada no es correcta. \n";
                    else {
                        try {
                            if (!validarFecha(mes, anio))
                                mensaje += "El vencimiento de la tarjeta no es válido (3 meses al día actual). \n";
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                 }

                if(mensaje.length()==0){

                    mensaje_final=mensaje_exitoso;
                    Date ingresada=new Date();
                     int mes = Integer.parseInt(etMes.getText().toString());
                    int anio = Integer.parseInt(etAnio.getText().toString());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        ingresada = dateFormat.parse(anio+"-"+mes+"-1");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(rb1.isChecked()) esCredito=false;
                    else esCredito=true;

                    Tarjeta tarjeta =new Tarjeta(etNumeroTarjeta.getText().toString(),etCCV.getText().toString(),ingresada,esCredito);
                    System.out.println(tarjeta);

                    CuentaBancaria cuenta = new CuentaBancaria(etCBU.getText().toString(),etAlias.getText().toString());
                    System.out.println(cuenta);

                    Usuario user = new Usuario(1, etNombre.getText().toString(), etPassword.getText().toString(), etEmail.getText().toString(), Double.valueOf(monto),tarjeta,cuenta);
                    System.out.println(user);
                }
                else mensaje_final=mensaje.substring(0,mensaje.length()-1);

                Toast toast1 = Toast.makeText(getApplicationContext(), mensaje_final, Toast.LENGTH_SHORT);
                toast1.show();



            }
        });
    }

    public boolean validarFecha(int mes, int anio) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date ingresada = dateFormat.parse(anio+"-"+mes+"-1");
        Date vencimiento = Calendar.getInstance().getTime();
        Calendar actual = Calendar.getInstance();
        actual.setTime(vencimiento);
        actual.add(Calendar.MONTH,3);
        vencimiento=actual.getTime();

        if(ingresada.before(vencimiento)) return false;
        else return true;
    };

    public boolean condicionEmail(String email){
        email=email.toLowerCase();
        if(!email.contains("@")) return false;
        else {
            for (int i = 0; i < email.length(); i++){
                if(email.charAt(i)=='@'&&(email.length()-i)>=3){
                    if(email.charAt(i+1)>='a'&&email.charAt(i+1)<='z'&&
                            email.charAt(i+2)>='a'&&email.charAt(i+2)<='z'&&
                            email.charAt(i+3)>='a'&&email.charAt(i+3)<='z') return true;
                }
            }
        }
        return false;
    }
    //Posible implementacion para activar el boton registrar
    public void activarRegistrar(EditText etNombre,EditText etPassword,EditText etPassword2,EditText  etEmail, EditText  etCCV,
                                 EditText etMes,EditText etAnio, CheckBox cbAcepto,Button registrar, RadioGroup rg1) {

        if(etNombre.getText().toString().length()!=0&&etPassword.getText().toString().length()!=0
                &&etPassword2.getText().toString().length()!=0&&etEmail.getText().toString().length()!=0
                &&etCCV.getText().toString().length()!=0&&etMes.getText().toString().length()>0
                &&etAnio.getText().toString().length()==4&&cbAcepto.isChecked()&&rg1.getCheckedRadioButtonId()!=-1){
            registrar.setEnabled(true);
        }else{
            registrar.setEnabled(false);
        }

    }
}