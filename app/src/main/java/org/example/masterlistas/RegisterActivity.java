package org.example.masterlistas;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by jvg63 on 05/11/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    EditText contraseña;
    EditText usuario;
    CheckBox mostrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usuario = (EditText) findViewById(R.id.usuario);
        contraseña = (EditText) findViewById(R.id.contraseña);
        mostrar = (CheckBox) findViewById(R.id.mostrar_contraseña);

        usuario.setText(getIntent().getExtras().getString("usuario", ""));
        contraseña.setText(getIntent().getExtras().getString("contraseña", ""));

    }

    public void mostrarContraseña(View v) {

        if (mostrar.isChecked()) {
            contraseña.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_NORMAL);
        } else {
            contraseña.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }


    public void acceder(View view) {
        Intent intent = new Intent();
        intent.putExtra("usuario", usuario.getText().toString());
        intent.putExtra("contraseña", contraseña.getText().toString());
        // Override animation exit transition explode
//        setupWindowAnimations();
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
//        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void borrarCampos(View view) {
        usuario.setText("");
        contraseña.setText("");
        usuario.requestFocus();

    }
}
