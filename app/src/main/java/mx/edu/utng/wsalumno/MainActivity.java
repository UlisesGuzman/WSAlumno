package mx.edu.utng.wsalumno;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etMatricula;
    private EditText etNotas;
    private EditText etFechaAlta;
    private EditText etGrupo;
    private EditText etGeneracion;
    private EditText etNombre;
    private ToggleButton tbType;

    private Button btGuardar;
    private Button btListar;

    private Alumno alumno = new Alumno();

    final String NAMESPACE = "http://ws.utng.edu.mx";
    final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    static String URL = "http://192.168.24.24:8080/WSAlumno/services/AlumnoWS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startComponentes();
    }

    private void startComponentes() {
        etMatricula = (EditText) findViewById(R.id.txt_matricula);
        etNotas = (EditText) findViewById(R.id.txt_notas);
        etFechaAlta = (EditText) findViewById(R.id.txt_fecha);
        etGrupo = (EditText) findViewById(R.id.txt_grupo);
        etGeneracion = (EditText) findViewById(R.id.txt_generacion);
        etNombre = (EditText) findViewById(R.id.txt_nombre);
        tbType = (ToggleButton) findViewById(R.id.toogle_categoria);
        btGuardar = (Button) findViewById(R.id.btn_guardar);
        btListar = (Button) findViewById(R.id.btn_listar);
        btGuardar.setOnClickListener(this);
        btListar.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alumnos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == btGuardar.getId()) {
                obtenerDatos();
                try {
                    if (getIntent().getExtras().getString("accion")
                            .equals("modificar")) {
                        TaskWSUpdate tarea = new TaskWSUpdate();
                        tarea.execute();

                    }

                } catch (Exception e) {
                    alumno.setProperty(0,0);
                    //Cuando no se haya mandado una accion por defecto es insertar.
                       TaskWSInsert alumno = new TaskWSInsert();
                       alumno.execute();

                }

        }
        if (btListar.getId() == v.getId()) {
            startActivity(new Intent(MainActivity.this, ListAlumnos.class));
        }
    }//fin conClick

    private void cleanEditTex() {
        etMatricula.setText("");
        etNotas.setText("");
        etFechaAlta.setText("");
        etNotas.setText("");
        tbType.setChecked(false);
        etGrupo.setText("");
        etGeneracion.setText("");
        etNombre.setText("");
    }


    private class TaskWSInsert extends AsyncTask<String, Integer, Boolean> {
      /*  public TaskWSInsert() {

            alumno = new Alumno();

             alumno.setProperty(0, 0);
            obtenerDatos();
        }*/

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = true;
            final String METHOD_NAME = "addAlumno";
            final String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;

            SoapObject request =
                    new SoapObject(NAMESPACE, METHOD_NAME);

            PropertyInfo info = new PropertyInfo();
            info.setName("alumno");
            info.setValue(alumno);
            info.setType(alumno.getClass());
            request.addProperty(info);
            envelope.setOutputSoapObject(request);
            envelope.addMapping(NAMESPACE, "Alumno", Alumno.class);

            HttpTransportSE transporte = new HttpTransportSE(URL);
            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapPrimitive response =
                        (SoapPrimitive) envelope.getResponse();
                String res = response.toString();
                if (!res.equals("1")) {
                    result = true;
                }

            } catch (Exception e) {
                Log.e("Error ", e.getMessage());
                result = false;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                cleanEditTex();
                Toast.makeText(getApplicationContext(),
                        "Registro exitoso.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Error al insertar.",
                        Toast.LENGTH_SHORT).show();

            }
        }
    }//fin tarea insertar

    private class TaskWSUpdate extends
            AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean result = true;

            final String METHOD_NAME = "editAlumno";
            final String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            alumno = new Alumno();
            alumno.setProperty(0, getIntent().getExtras().getString("valor0"));
            obtenerDatos();

            PropertyInfo info = new PropertyInfo();
            info.setName("alumno");
            info.setValue(alumno);
            info.setType(alumno.getClass());

            request.addProperty(info);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.setOutputSoapObject(request);

            envelope.addMapping(NAMESPACE, "Alumno", alumno.getClass());

            MarshalFloat mf = new MarshalFloat();
            mf.register(envelope);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION, envelope);

                SoapPrimitive resultado_xml = (SoapPrimitive) envelope
                        .getResponse();
                String res = resultado_xml.toString();

                if (!res.equals("1")) {
                    result = false;
                }

            } catch (HttpResponseException e) {
                Log.e("Error HTTP", e.toString());
            } catch (IOException e) {
                Log.e("Error IO", e.toString());
            } catch (XmlPullParserException e) {
                Log.e("Error XmlPullParser", e.toString());
            }

            return result;

        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                Toast.makeText(getApplicationContext(), "Actualizado OK",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error al actualizar",
                        Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void obtenerDatos(){
        alumno.setProperty(1, etMatricula.getText().toString());
        alumno.setProperty(2, etNotas.getText().toString());
        alumno.setProperty(3, etFechaAlta.getText().toString());
        if(tbType.isChecked()){
            alumno.setProperty(4,true);
        }else{
            alumno.setProperty(4,false);
        }
        alumno.setProperty(5, etGrupo.getText().toString());
        alumno.setProperty(6, Integer.parseInt(etGeneracion.getText().toString()));
        alumno.setProperty(7, etNombre.getText().toString());


        Log.i("Alumno",alumno.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle datosRegreso = this.getIntent().getExtras();
        try {

            etMatricula.setText(datosRegreso.getString("valor1"));
            etNotas.setText(datosRegreso.getString("valor2"));
            etFechaAlta.setText(datosRegreso.getString("valor3"));
            if (datosRegreso.getString("valor4").equals("1")) {
                tbType.setChecked(false);
            } else {
                tbType.setChecked(true);
            }
            etGrupo.setText(datosRegreso.getString("valor5"));
            etGeneracion.setText(datosRegreso.getString("valor6"));
            etNombre.setText(datosRegreso.getString("valor7"));


        } catch (Exception e) {
            Log.e("Error al Regargar", e.toString());
        }

    }

}