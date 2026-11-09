package com.davinci.crewup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private LinearLayout containerCards;

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

        containerCards = findViewById(R.id.containerCards);
    }

    // onResume se ejecuta al abrir la pantalla y también al volver desde otra,
    // así las tarjetas siempre muestran el estado actualizado
    @Override
    protected void onResume() {
        super.onResume();
        mostrarPlanes();
    }

    private void mostrarPlanes() {
        containerCards.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();
        for (Plan plan : RepositorioPlanes.getPlanes()) {
            View tarjeta = inflater.inflate(R.layout.item_plan, containerCards, false);
            completarTarjeta(tarjeta, plan);
            containerCards.addView(tarjeta);
        }
    }

    private void completarTarjeta(View tarjeta, Plan plan) {
        ImageView imgPlan = tarjeta.findViewById(R.id.imgPlan);
        TextView txtTitulo = tarjeta.findViewById(R.id.txtPlanTitle);
        TextView txtLugarHora = tarjeta.findViewById(R.id.txtPlanPlaceTime);
        TextView txtPersonas = tarjeta.findViewById(R.id.txtPlanPeople);
        TextView txtCosto = tarjeta.findViewById(R.id.txtPlanCost);
        TextView txtOrganizador = tarjeta.findViewById(R.id.txtPlanHost);
        TextView txtEstado = tarjeta.findViewById(R.id.txtPlanStatus);
        Button btnSolicitud = tarjeta.findViewById(R.id.btnRequestJoin);

        imgPlan.setImageResource(plan.getImagenRes());
        txtTitulo.setText(plan.getTitulo());
        txtLugarHora.setText(getString(R.string.place_time_format,
                plan.getUbicacion(), plan.getFechaHora()));
        txtPersonas.setText(getString(R.string.people_format,
                plan.getParticipantes(), plan.getCapacidad()));
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

        UiSolicitud.aplicarEstado(this, plan, btnSolicitud, txtEstado);

        btnSolicitud.setOnClickListener(v -> {
            UiSolicitud.alternar(this, plan);
            UiSolicitud.aplicarEstado(this, plan, btnSolicitud, txtEstado);
        });

        // Tocar la tarjeta abre el detalle
        tarjeta.setOnClickListener(v -> abrirDetalle(plan));
    }

    // Abre Plan Details enviando el id del plan dentro de un Bundle
    private void abrirDetalle(Plan plan) {
        Intent intent = new Intent(this, PlanDetailActivity.class);
        Bundle paquete = new Bundle();
        paquete.putInt(PlanDetailActivity.EXTRA_PLAN_ID, plan.getId());
        intent.putExtras(paquete);
        startActivity(intent);
    }
}