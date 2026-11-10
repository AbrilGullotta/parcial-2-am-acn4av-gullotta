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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText edtEmail, edtPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        // Si ya hay una sesión iniciada, entra directo a Discover
        if (auth.getCurrentUser() != null) {
            irADiscover();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        TextView txtIrRegistro = findViewById(R.id.txtGoRegister);

        btnLogin.setOnClickListener(v -> iniciarSesion());
        txtIrRegistro.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void iniciarSesion() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString();

        if (!validar(email, password)) return;

        btnLogin.setEnabled(false);

        // Firebase verifica el email y la contraseña
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, tarea -> {
                    btnLogin.setEnabled(true);
                    if (tarea.isSuccessful()) {
                        irADiscover();
                    } else {
                        Exception e = tarea.getException();
                        boolean credencialesMal = e instanceof FirebaseAuthInvalidCredentialsException
                                || e instanceof FirebaseAuthInvalidUserException;
                        Toast.makeText(this,
                                credencialesMal ? R.string.error_login : R.string.error_generic,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean validar(String email, String password) {
        boolean valido = true;
        if (email.isEmpty()) {
            edtEmail.setError(getString(R.string.error_required));
            valido = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError(getString(R.string.error_email));
            valido = false;
        }
        if (password.isEmpty()) {
            edtPassword.setError(getString(R.string.error_required));
            valido = false;
        }
        return valido;
    }

    // Abre Discover y borra Login del historial
    private void irADiscover() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}