package com.davinci.crewup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private LinearLayout containerPlans;

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

        containerPlans = findViewById(R.id.containerPlans);
        mostrarPlanes();
    }

    // Crea una tarjeta por cada plan y la agrega al feed
    private void mostrarPlanes() {
        LayoutInflater inflater = getLayoutInflater();
        for (Plan plan : RepositorioPlanes.getPlanes()) {
            View tarjeta = inflater.inflate(R.layout.item_plan, containerPlans, false);
            completarTarjeta(tarjeta, plan);
            containerPlans.addView(tarjeta);
        }
    }

    // Carga los datos fijos del plan en la tarjeta
    private void completarTarjeta(View tarjeta, Plan plan) {
        ImageView imgPlan = tarjeta.findViewById(R.id.imgPlan);
        TextView txtTitulo = tarjeta.findViewById(R.id.txtPlanTitle);
        TextView txtLugarHora = tarjeta.findViewById(R.id.txtPlanPlaceTime);
        TextView txtCosto = tarjeta.findViewById(R.id.txtPlanCost);
        TextView txtOrganizador = tarjeta.findViewById(R.id.txtPlanHost);
        Button btnSolicitud = tarjeta.findViewById(R.id.btnRequestJoin);

        imgPlan.setImageResource(plan.getImagenRes());
        txtTitulo.setText(plan.getTitulo());
        txtLugarHora.setText(getString(R.string.place_time_format,
                plan.getUbicacion(), plan.getFechaHora()));
        txtOrganizador.setText(getString(R.string.hosted_by_format,
                plan.getOrganizador(), plan.getRatingOrganizador()));

        if (plan.esGratis()) {
            txtCosto.setText(R.string.free);
            txtCosto.setBackgroundResource(R.drawable.bg_chip_free);
            txtCosto.setTextColor(ContextCompat.getColor(this, R.color.crew_green));
        } else {
            txtCosto.setText(getString(R.string.cost_format, plan.getCosto()));
            txtCosto.setBackgroundResource(R.drawable.bg_chip_cost);
            txtCosto.setTextColor(ContextCompat.getColor(this, R.color.crew_teal_dark));
        }

        btnSolicitud.setOnClickListener(v -> {
            if (plan.getEstadoSolicitud() == EstadoSolicitud.PENDIENTE) {
                plan.setEstadoSolicitud(EstadoSolicitud.NINGUNA);
                Toast.makeText(this, R.string.toast_request_cancelled,
                        Toast.LENGTH_SHORT).show();
            } else {
                plan.setEstadoSolicitud(EstadoSolicitud.PENDIENTE);
                Toast.makeText(this, getString(R.string.toast_request_sent,
                        plan.getOrganizador()), Toast.LENGTH_SHORT).show();
            }
            actualizarEstado(tarjeta, plan);
        });

        actualizarEstado(tarjeta, plan);
    }

    // Refleja en la tarjeta el cupo y el estado de la solicitud
    private void actualizarEstado(View tarjeta, Plan plan) {
        TextView txtPersonas = tarjeta.findViewById(R.id.txtPlanPeople);
        TextView txtEstado = tarjeta.findViewById(R.id.txtPlanStatus);
        Button btnSolicitud = tarjeta.findViewById(R.id.btnRequestJoin);

        txtPersonas.setText(getString(R.string.people_format,
                plan.getParticipantes(), plan.getCapacidad()));

        boolean pendiente = plan.getEstadoSolicitud() == EstadoSolicitud.PENDIENTE;
        txtEstado.setVisibility(pendiente ? View.VISIBLE : View.GONE);

        if (pendiente) {
            btnSolicitud.setEnabled(true);
            btnSolicitud.setText(R.string.btn_cancel_request);
            btnSolicitud.setTextColor(ContextCompat.getColor(this, R.color.white));
            btnSolicitud.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.crew_orange));
        } else if (plan.estaCompleto()) {
            btnSolicitud.setEnabled(false);
            btnSolicitud.setText(R.string.btn_plan_full);
            btnSolicitud.setTextColor(ContextCompat.getColor(this, R.color.crew_gray));
            btnSolicitud.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.crew_gray_light));
        } else {
            btnSolicitud.setEnabled(true);
            btnSolicitud.setText(R.string.btn_request_join);
            btnSolicitud.setTextColor(ContextCompat.getColor(this, R.color.white));
            btnSolicitud.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.crew_teal));
        }
    }
}