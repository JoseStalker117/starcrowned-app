package com.starcrowned.app;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.util.Log;
import androidx.annotation.NonNull;

public class fbAuth {
    private static final String TAG = "fbAuth";
    private FirebaseAuth mAuth;
    
    // Interface para callbacks
    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onFailure(String error);
    }
    
    public fbAuth() {
        mAuth = FirebaseAuth.getInstance();
    }
    
    // Función para registrar usuario
    public void registerUser(String email, String password, AuthCallback callback) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            callback.onFailure("Email y contraseña son requeridos");
            return;
        }
        
        if (password.length() < 6) {
            callback.onFailure("La contraseña debe tener al menos 6 caracteres");
            return;
        }
        
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            callback.onSuccess(user);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            String errorMessage = task.getException() != null ? 
                                task.getException().getMessage() : "Error desconocido";
                            callback.onFailure(errorMessage);
                        }
                    }
                });
    }
    
    // Función para iniciar sesión
    public void loginUser(String email, String password, AuthCallback callback) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            callback.onFailure("Email y contraseña son requeridos");
            return;
        }
        
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            callback.onSuccess(user);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            String errorMessage = task.getException() != null ? 
                                task.getException().getMessage() : "Error desconocido";
                            callback.onFailure(errorMessage);
                        }
                    }
                });
    }
    
    // Función para cerrar sesión
    public void signOut() {
        mAuth.signOut();
        Log.d(TAG, "User signed out");
    }
    
    // Función para obtener el usuario actual
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }
    
    // Función para verificar si hay un usuario logueado
    public boolean isUserLoggedIn() {
        return getCurrentUser() != null;
    }
}
