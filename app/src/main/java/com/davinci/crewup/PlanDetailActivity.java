package com.davinci.crewup;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlanDetailActivity extends AppCompatActivity {

    // Clave con la que se envía el id del plan en el Bundle
    public static final String EXTRA_PLAN_ID = "extra_plan_id";

    private Plan plan;
    private Button btnSolicitud;
    private TextView txtEstado;
    private LinearLayout containerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plan_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Recibir el paquete enviado desde Discover
        Bundle extras = getIntent().getExtras();
        int planId = (extras != null) ? extras.getInt(EXTRA_PLAN_ID, -1) : -1;
        plan = RepositorioPlanes.getPlanPorId(planId);

        if (plan == null) {
            finish();
            return;
        }

        btnSolicitud = findViewById(R.id.btnDetailRequest);
        txtEstado = findViewById(R.id.txtDetailStatus);
        containerInfo = findViewById(R.id.containerInfo);
        ImageButton btnBack = findViewById(R.id.btnBack);

        mostrarDatos();

        btnBack.setOnClickListener(v -> finish());
        btnSolicitud.setOnClickListener(v -> {
            UiSolicitud.alternar(this, plan);
            UiSolicitud.aplicarEstado(this, plan, btnSolicitud, txtEstado);
        });
    }

    private void mostrarDatos() {
        ImageView imgDetail = findViewById(R.id.imgDetail);
        TextView txtCategoria = findViewById(R.id.txtDetailCategory);
        TextView txtTitulo = findViewById(R.id.txtDetailTitle);
        TextView txtOrganizador = findViewById(R.id.txtDetailHost);
        TextView txtDescripcion = findViewById(R.id.txtDetailDescription);

        imgDetail.setImageResource(plan.getImagenRes());
        txtCategoria.setText(plan.getCategoria());
        txtTitulo.setText(plan.getTitulo());
        txtOrganizador.setText(getString(R.string.hosted_by_format,
                plan.getOrganizador(), plan.getRatingOrganizador()));
        txtDescripcion.setText(plan.getDescripcion());

        String costo = plan.esGratis()
                ? getString(R.string.free)
                : getString(R.string.cost_format, plan.getCosto());

        agregarFila(R.string.label_when, plan.getFechaHora());
        agregarFila(R.string.label_where, plan.getUbicacion());
        agregarFila(R.string.label_meeting_point, plan.getPuntoEncuentro());
        agregarFila(R.string.label_people, getString(R.string.people_plain_format,
                plan.getParticipantes(), plan.getCapacidad()));
        agregarFila(R.string.label_cost, costo);

        UiSolicitud.aplicarEstado(this, plan, btnSolicitud, txtEstado);
    }

    // Crea una fila "etiqueta — valor" y la agrega a la lista de detalles
    private void agregarFila(int etiquetaRes, String valor) {
        View fila = getLayoutInflater().inflate(R.layout.item_info_row, containerInfo, false);
        TextView txtLabel = fila.findViewById(R.id.txtInfoLabel);
        TextView txtValue = fila.findViewById(R.id.txtInfoValue);
        txtLabel.setText(etiquetaRes);
        txtValue.setText(valor);
        containerInfo.addView(fila);
    }
}