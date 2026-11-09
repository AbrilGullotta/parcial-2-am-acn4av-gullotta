package com.davinci.crewup;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button btnRequestJoin;
    private TextView txtPlanStatus;
    private boolean solicitudEnviada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnRequestJoin = findViewById(R.id.btnRequestJoin);
        txtPlanStatus = findViewById(R.id.txtPlanStatus);

        btnRequestJoin.setOnClickListener(v -> alternarSolicitud());
    }

    // Envía o cancela la solicitud para unirse al plan
    private void alternarSolicitud() {
        solicitudEnviada = !solicitudEnviada;

        if (solicitudEnviada) {
            btnRequestJoin.setText(R.string.btn_cancel_request);
            btnRequestJoin.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.crew_orange));
            txtPlanStatus.setVisibility(View.VISIBLE);
            Toast.makeText(this, getString(R.string.toast_request_sent, "Emma"),
                    Toast.LENGTH_SHORT).show();
        } else {
            btnRequestJoin.setText(R.string.btn_request_join);
            btnRequestJoin.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.crew_teal));
            txtPlanStatus.setVisibility(View.GONE);
            Toast.makeText(this, R.string.toast_request_cancelled,
                    Toast.LENGTH_SHORT).show();
        }
    }
}