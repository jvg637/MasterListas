package org.jvilches.masterlistas;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class InicioSesionActivity extends AppCompatActivity {

    private static final int LOGIN_ACTIVITY = 10001;
    EditText usuario;
    EditText contraseña;
    private Button facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        extraeHash();

        usuario = (EditText) findViewById(R.id.usuario);
        contraseña = (EditText) findViewById(R.id.contraseña);

        facebook = (Button) findViewById(R.id.boton_facebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementaIndiceBloqueo();
            }
        });
    }

    public void loguearCheckbox(View v) {
        CheckBox recordarme = (CheckBox) findViewById(R.id.recordarme);
        String s = getString(R.string.recordar_datos_usuario) +
                (recordarme.isChecked() ? getString(android.R.string.yes) : getString(android.R.string.no));
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    public void showPassword(View v) {

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
        ActivityCompat.startActivityForResult(this, intent, LOGIN_ACTIVITY, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
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

    private ArrayList bloqueo;

    public void incrementaIndiceBloqueo() {
        bloqueo.add(null);
    }

    public void incrementaIndiceANR(View view) {
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void extraeHash () {
        String packageName = getPackageName();
        try {
            PackageInfo info = this.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyLog", "KeyHash:"+
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }
}
