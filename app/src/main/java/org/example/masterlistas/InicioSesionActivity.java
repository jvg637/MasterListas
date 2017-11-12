package org.example.masterlistas;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.transition.Slide;
import android.transition.Transition;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class InicioSesionActivity extends AppCompatActivity {

    private static final int LOGIN_ACTIVITY = 10001;
    EditText usuario;
    EditText contraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        usuario = (EditText) findViewById(R.id.usuario);
        contraseña = (EditText) findViewById(R.id.contraseña);
    }

    public void loguearCheckbox(View v) {
        CheckBox recordarme = (CheckBox) findViewById(R.id.recordarme);
        String s = "Recordar datos de usuario: " +
                (recordarme.isChecked() ? "Sí" : "No");
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    public void mostrarContraseña(View v) {

        CheckBox mostrar = (CheckBox) findViewById(R.id.mostrar_contraseña);
        if (mostrar.isChecked()) {
            contraseña.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_NORMAL);
        } else {
            contraseña.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    public void acceder(View view) {
        Intent intent = new Intent(this, ListasActivity.class);
        // Override animation exit transition explode
//        setupWindowAnimations();
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }


    public void borrarCampos(View view) {

        usuario.setText("");
        contraseña.setText("");
        usuario.requestFocus();

    }

    private void setupWindowAnimations() {
        Intent intent = new Intent(this, ListasActivity.class);
        Transition slide = new Slide();
        slide.setDuration(300);
        getWindow().setExitTransition(slide);
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
//        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        intent.putExtra("usuario", usuario.getText().toString());
        intent.putExtra("contraseña", contraseña.getText().toString());
        ActivityCompat.startActivityForResult(this, intent, LOGIN_ACTIVITY,  ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOGIN_ACTIVITY && resultCode == RESULT_OK) {
            String user = data.getExtras().getString("usuario");
            String password = data.getExtras().getString("contraseña");

            contraseña.setText(password);
            usuario.setText(user);

            acceder(null);
        }
    }
}
