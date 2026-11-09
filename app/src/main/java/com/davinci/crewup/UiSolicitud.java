package com.davinci.crewup;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

// Lógica visual del botón de solicitud, compartida entre pantallas
public final class UiSolicitud {

    private UiSolicitud() { }

    public static void alternar(Context context, Plan plan) {
        plan.alternarSolicitud();
        if (plan.tieneSolicitudPendiente()) {
            Toast.makeText(context, context.getString(R.string.toast_request_sent,
                    plan.getOrganizador()), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.toast_request_cancelled,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static void aplicarEstado(Context context, Plan plan,
                                     Button btn, TextView txtEstado) {
        boolean pendiente = plan.tieneSolicitudPendiente();
        txtEstado.setVisibility(pendiente ? View.VISIBLE : View.GONE);

        if (pendiente) {
            btn.setEnabled(true);
            btn.setText(R.string.btn_cancel_request);
            btn.setTextColor(ContextCompat.getColor(context, R.color.white));
            btn.setBackgroundTintList(
                    ContextCompat.getColorStateList(context, R.color.crew_orange));
        } else if (plan.estaCompleto()) {
            btn.setEnabled(false);
            btn.setText(R.string.btn_plan_full);
            btn.setTextColor(ContextCompat.getColor(context, R.color.crew_gray));
            btn.setBackgroundTintList(
                    ContextCompat.getColorStateList(context, R.color.crew_gray_light));
        } else {
            btn.setEnabled(true);
            btn.setText(R.string.btn_request_join);
            btn.setTextColor(ContextCompat.getColor(context, R.color.white));
            btn.setBackgroundTintList(
                    ContextCompat.getColorStateList(context, R.color.crew_teal));
        }
    }
}