package com.davinci.crewup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText edtNombre, edtEmail, edtPassword, edtConfirmar;
    private Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Conexión con Firebase Authentication
        auth = FirebaseAuth.getInstance();

        edtNombre = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmar = findViewById(R.id.edtConfirmPassword);
        btnRegistrar = findViewById(R.id.btnRegister);
        TextView txtIrLogin = findViewById(R.id.txtGoLogin);

        btnRegistrar.setOnClickListener(v -> registrar());
        txtIrLogin.setOnClickListener(v -> finish()); // vuelve al Login
    }

    private void registrar() {
        String nombre = edtNombre.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString();
        String confirmar = edtConfirmar.getText().toString();

        if (!validar(nombre, email, password, confirmar)) return;

        // Evita que se toque dos veces mientras Firebase responde
        btnRegistrar.setEnabled(false);

        // Firebase crea la cuenta en la nube
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, tarea -> {
                    if (tarea.isSuccessful()) {
                        guardarNombre(nombre);
                    } else {
                        btnRegistrar.setEnabled(true);
                        int mensaje = (tarea.getException() instanceof FirebaseAuthUserCollisionException)
                                ? R.string.error_email_in_use
                                : R.string.error_generic;
                        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Guarda el nombre en el perfil de Firebase para mostrarlo en la app
    private void guardarNombre(String nombre) {
        FirebaseUser usuario = auth.getCurrentUser();
        if (usuario == null) return;

        UserProfileChangeRequest perfil = new UserProfileChangeRequest.Builder()
                .setDisplayName(nombre)
                .build();

        usuario.updateProfile(perfil).addOnCompleteListener(tarea -> {
            Toast.makeText(this, getString(R.string.toast_welcome, nombre),
                    Toast.LENGTH_SHORT).show();
            irADiscover();
        });
    }

    // Revisa los campos antes de enviarlos a Firebase
    private boolean validar(String nombre, String email, String password, String confirmar) {
        boolean valido = true;

        if (nombre.isEmpty()) {
            edtNombre.setError(getString(R.string.error_required));
            valido = false;
        }
        if (email.isEmpty()) {
            edtEmail.setError(getString(R.string.error_required));
            valido = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError(getString(R.string.error_email));
            valido = false;
        }
        if (password.length() < 6) {
            edtPassword.setError(getString(R.string.error_password_short));
            valido = false;
        }
        if (!password.equals(confirmar)) {
            edtConfirmar.setError(getString(R.string.error_password_match));
            valido = false;
        }
        return valido;
    }

    // Abre Discover y borra Registro del historial (el botón atrás no vuelve)
    private void irADiscover() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}