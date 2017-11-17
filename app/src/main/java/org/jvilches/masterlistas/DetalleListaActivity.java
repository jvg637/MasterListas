package org.jvilches.masterlistas;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

public class DetalleListaActivity extends AppCompatActivity {

    private MaterialSheetFab materialSheetFab;
    private int statusBarColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_lista);

        int numeroLista = (int) getIntent().getExtras().getSerializable(
                "numeroLista");
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.textWhite));
        toolbar.setTitle("");

        ImageView imageView = (ImageView) findViewById(R.id.imagen);
        if (numeroLista == 0) {
            toolbar.setTitle(getString(R.string.trabajo));
            imageView.setImageResource(R.drawable.trabajo);
        } else {
            toolbar.setTitle(getString(R.string.personal));
            imageView.setImageResource(R.drawable.casa);
        }


        // MateriaSheetFab
        Fab fab = (Fab) findViewById(R.id.fab);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.dim_overlay);
        int sheetColor = getResources().getColor(R.color.colorPrimary);
        int fabColor = getResources().getColor(R.color.colorAccent);

        // Initialize material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay,
                sheetColor, fabColor);

        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                // Called when the material sheet's "show" animation starts.
                // Save current status bar color

                statusBarColor = getStatusBarColor();
                // Set darker status bar color to match the dim overlay
                setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark2));
            }

            @Override
            public void onSheetShown() {
                // Called when the material sheet's "show" animation ends.
            }

            @Override
            public void onHideSheet() {
                // Called when the material sheet's "hide" animation starts.
                setStatusBarColor(statusBarColor);
            }

            public void onSheetHidden() {
                // Called when the material sheet's "hide" animation ends.
            }
        });
//        materialSheetFab.showFab(translationX, translationY);
//        Transition shared_lista_enter = TransitionInflater.from(this)
//                .inflateTransition(R.transition.transition_curva);
//        getWindow().setSharedElementEnterTransition(shared_lista_enter);
    }

    @Override
    public void onBackPressed() {
        if (materialSheetFab.isSheetVisible()) materialSheetFab.hideSheet();
        else super.onBackPressed();
    }

    private int getStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return getWindow().getStatusBarColor();
        return 0;
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(color);
    }

    public void accionFab(View view) {
        Toast.makeText(this, getString(R.string.he_elegido) + ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
    } }