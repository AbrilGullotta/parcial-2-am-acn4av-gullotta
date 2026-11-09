package com.davinci.crewup;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
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

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private LinearLayout containerCards;
    private LinearLayout containerFilters;

    // Categoría elegida en los filtros. null = todas
    private String categoriaSeleccionada = null;

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
        containerFilters = findViewById(R.id.containerFilters);
    }

    // onResume se ejecuta al abrir la pantalla y al volver desde otra,
    // así filtros y tarjetas siempre muestran los datos actualizados
    @Override
    protected void onResume() {
        super.onResume();
        crearFiltros();
        actualizarFiltros();
        mostrarPlanes();
    }

    // ---------- FILTROS (creados 100% desde Java) ----------

    private void crearFiltros() {
        containerFilters.removeAllViews();
        agregarFiltro(getString(R.string.filter_all), null);

        // Una categoría por cada tipo de plan existente, sin repetir
        Set<String> categorias = new LinkedHashSet<>();
        for (Plan plan : RepositorioPlanes.getPlanes()) {
            categorias.add(plan.getCategoria());
        }
        for (String categoria : categorias) {
            agregarFiltro(categoria, categoria);
        }
    }

    private void agregarFiltro(String texto, String categoria) {
        TextView chip = new TextView(this);
        chip.setText(texto);
        chip.setTag(categoria);
        chip.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_body));

        int paddingH = getResources().getDimensionPixelSize(R.dimen.space_m);
        int paddingV = getResources().getDimensionPixelSize(R.dimen.space_s);
        chip.setPadding(paddingH, paddingV, paddingH, paddingV);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMarginEnd(getResources().getDimensionPixelSize(R.dimen.space_s));
        chip.setLayoutParams(params);

        chip.setOnClickListener(v -> {
            categoriaSeleccionada = categoria;
            actualizarFiltros();
            mostrarPlanes();
        });

        containerFilters.addView(chip);
    }

    // Pinta de turquesa el filtro elegido y de blanco los demás
    private void actualizarFiltros() {
        for (int i = 0; i < containerFilters.getChildCount(); i++) {
            TextView chip = (TextView) containerFilters.getChildAt(i);
            boolean seleccionado = Objects.equals(chip.getTag(), categoriaSeleccionada);

            chip.setBackgroundResource(seleccionado
                    ? R.drawable.bg_filter_selected
                    : R.drawable.bg_filter_unselected);
            chip.setTextColor(ContextCompat.getColor(this,
                    seleccionado ? R.color.white : R.color.crew_navy));
            chip.setTypeface(null, seleccionado ? Typeface.BOLD : Typeface.NORMAL);
        }
    }

    // ---------- TARJETAS ----------

    private void mostrarPlanes() {
        containerCards.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();

        for (Plan plan : RepositorioPlanes.getPlanes()) {
            if (categoriaSeleccionada != null
                    && !categoriaSeleccionada.equals(plan.getCategoria())) {
                continue; // no pertenece al filtro elegido
            }
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