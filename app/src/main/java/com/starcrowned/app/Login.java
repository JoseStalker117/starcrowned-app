package com.starcrowned.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    
    private EditText etEmail, etPassword, etConfirmPassword, etFullName;
    private Button btnAction;
    private TextView tvTitle, tvToggle;
    private fbAuth firebaseAuth;
    private boolean isLoginMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        initializeViews();
        setupFirebaseAuth();
        setupClickListeners();
        checkIfUserLoggedIn();
    }
    
    private void initializeViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etFullName = findViewById(R.id.etFullName);
        btnAction = findViewById(R.id.btnAction);
        tvTitle = findViewById(R.id.tvTitle);
        tvToggle = findViewById(R.id.tvToggle);
    }
    
    private void setupFirebaseAuth() {
        firebaseAuth = new fbAuth();
    }
    
    private void setupClickListeners() {
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoginMode) {
                    performLogin();
                } else {
                    performRegister();
                }
            }
        });
        
        tvToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMode();
            }
        });
    }
    
    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        
        btnAction.setEnabled(false);
        btnAction.setText("Iniciando sesión...");
        
        firebaseAuth.loginUser(email, password, new fbAuth.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Login.this, "Bienvenido " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        navigateToMainActivity();
                    }
                });
            }
            
            @Override
            public void onFailure(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Login.this, "Error: " + error, Toast.LENGTH_LONG).show();
                        btnAction.setEnabled(true);
                        btnAction.setText("Iniciar Sesión");
                    }
                });
            }
        });
    }
    
    private void performRegister() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fullName.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }
        
        btnAction.setEnabled(false);
        btnAction.setText("Registrando...");
        
        firebaseAuth.registerUser(email, password, new fbAuth.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Login.this, "Registro exitoso. Bienvenido " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        navigateToMainActivity();
                    }
                });
            }
            
            @Override
            public void onFailure(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Login.this, "Error: " + error, Toast.LENGTH_LONG).show();
                        btnAction.setEnabled(true);
                        btnAction.setText("Registrarse");
                    }
                });
            }
        });
    }
    
    private void toggleMode() {
        isLoginMode = !isLoginMode;
        
        if (isLoginMode) {
            // Cambiar a modo login
            tvTitle.setText("Iniciar Sesión");
            btnAction.setText("Iniciar Sesión");
            tvToggle.setText("¿No tienes cuenta? Regístrate");
            etConfirmPassword.setVisibility(View.GONE);
            etFullName.setVisibility(View.GONE);
        } else {
            // Cambiar a modo registro
            tvTitle.setText("Registrarse");
            btnAction.setText("Registrarse");
            tvToggle.setText("¿Ya tienes cuenta? Inicia sesión");
            etConfirmPassword.setVisibility(View.VISIBLE);
            etFullName.setVisibility(View.VISIBLE);
        }
        
        // Limpiar campos
        etEmail.setText("");
        etPassword.setText("");
        etConfirmPassword.setText("");
        etFullName.setText("");
    }
    
    private void checkIfUserLoggedIn() {
        if (firebaseAuth.isUserLoggedIn()) {
            navigateToMainActivity();
        }
    }
    
    private void navigateToMainActivity() {
        // Aquí debes cambiar MainActivity por tu actividad principal
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}