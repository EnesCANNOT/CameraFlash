package com.candroid.learningcameraflash

import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.candroid.learningcameraflash.databinding.ActivityMainBinding
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cameraFlashSwitch.setOnCheckedChangeListener { _buttonView, isChecked ->
            switchFlash(isChecked)
        }
    }

    private fun switchFlash(isChecked: Boolean){
        if (this.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                val cameraManager: CameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
                val cameraId: String?

                try {
                    cameraId = cameraManager.cameraIdList.get(0)
                    cameraManager.setTorchMode(cameraId, isChecked)
                } catch (e: CameraAccessException){
                    e.printStackTrace()
                }
            } else{
                val camera: Camera = Camera.open()
                val parameters: Camera.Parameters = camera.getParameters()
                parameters.flashMode = Camera.Parameters.FLASH_MODE_TORCH
                camera.setParameters(parameters)

                val surfaceTexture: SurfaceTexture = SurfaceTexture(0)
                try {
                    camera.setPreviewTexture(surfaceTexture)
                } catch (e: IOException){
                    e.printStackTrace()
                }

                if (isChecked){
                    camera.startPreview()
                } else {
                    parameters.flashMode = Camera.Parameters.FLASH_MODE_OFF
                    camera.stopPreview()
                    camera.release()
                }
            }
        } else{
            Toast.makeText(this, "No flash light found", Toast.LENGTH_SHORT).show();
        }
    }
}