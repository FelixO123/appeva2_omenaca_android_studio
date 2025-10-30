package com.example.eva2_app_moviles

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eva2_app_moviles.ui.PanelAdminFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Cargar el PanelAdminFragment al iniciar la app
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, PanelAdminFragment())
            .commit()
    }
}
