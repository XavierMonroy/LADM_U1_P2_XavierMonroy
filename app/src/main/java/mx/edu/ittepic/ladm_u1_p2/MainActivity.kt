package mx.edu.ittepic.ladm_u1_p2

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Boton de permisos
        button3.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                // SI ENTRA EN ESTE IF, ES PORQUE NO TIENE LOS PERMISOS
                // EL SIGUIENTE CODIGO SOLICITA LOS PERMISOS
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
            }else{
                mensaje("PERMISOS YA OTORGADOS")
            }
        }

        // Boton guardar
        button.setOnClickListener {
            // Sino selecciona ningun radiobutton
            if(!radioButton.isChecked && !radioButton2.isChecked){
                mensaje("SELECCIONE UNA OPCION: ARCHIVO INTERNO O EXTERNO")
                return@setOnClickListener
            }
            // Guardar archivo en memoria interna
            else if(radioButton.isChecked){
                guardarArchivoInterno()
                return@setOnClickListener
            }
            // Guardar archivo en SD
            else if(radioButton2.isChecked){
                guardarArchivoSD()
                return@setOnClickListener
            }
        }

        // Boton leer
        button2.setOnClickListener {
            // Sino hay opcion seleccionada detenemos el proceso
            if(!radioButton.isChecked && !radioButton2.isChecked){
                mensaje("SELECCIONE UNA OPCION: ARCHIVO INTERNO O EXTERNO")
                return@setOnClickListener
            }
            // Guardar archivo en memoria interna
            else if(radioButton.isChecked){
                leerArchivoInterno()
                return@setOnClickListener
            }
            // Guardar archivo en memoria SD
            else if(radioButton2.isChecked){
                leerArchivoSD()
                return@setOnClickListener
            }
        }
    }

    //Leer un archivo interno
    private fun leerArchivoInterno(){
        var nameArchivo = editText4.text.toString()
        try {
            var flujoEntrada = BufferedReader(InputStreamReader(openFileInput(nameArchivo)))

            var data = flujoEntrada.readLine()

            ponerTexto(data)
            flujoEntrada.close()
        }catch (error : IOException){
            mensaje(error.message.toString())
        }
    }

    //Leer el archivo desde la SD
    fun leerArchivoSD(){
        var nameArchivo = editText4.text.toString()
        // Si no hay memoria SD detenemos el proceso
        if(noSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }
        // caso contrario continuamos con el proceso
        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,nameArchivo)

            var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))

            var data = flujoEntrada.readLine()

            ponerTexto(data)
            flujoEntrada.close()
        }catch (error : IOException){
            mensaje(error.message.toString())
        }
    }

    //Guardar un archivo interno
    fun guardarArchivoInterno(){
        var nameArchivo = editText4.text.toString()
        try {
            var flujoSalida = OutputStreamWriter(openFileOutput(nameArchivo, Context.MODE_PRIVATE))
            var data = editText.text.toString()

            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()

            mensaje("¡EXITO! Se guardo el archivo correctamente en MEMORIA INTERNA")
            ponerTexto("")
        }catch (error : IOException){
            mensaje(error.message.toString())
        }
    }

    // Guardar el archivo en SD
    fun guardarArchivoSD(){
        var nameArchivo = editText4.text.toString()
        // Si no hay memoria SD se detiene el proceso
        if(noSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }
        // Si pasa procedemos a guardar el archivo
        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath, nameArchivo)

            var flujoSalida = OutputStreamWriter(FileOutputStream(datosArchivo))
            var data = editText.text.toString()

            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()

            mensaje("¡EXITO! Se guardo el archivo correctamente en MEMORIA EXTERNA")
            ponerTexto("")
        }catch (error : IOException){
            mensaje(error.message.toString())
        }
    }

    // Verificar que se tenga una memoria SD
    fun noSD() : Boolean{
        var estado = Environment.getExternalStorageState()
        if(estado != Environment.MEDIA_MOUNTED){
            return true
        }
        return false
    }

    // Poner texto en el multiline
    fun ponerTexto(tex:String){
        editText.setText(tex)
    }

    // Funcion para mostrar un mensaje
    fun mensaje(m:String){
        AlertDialog.Builder(this).setTitle("ATENCION").setMessage((m)).setPositiveButton("OK"){d,i->}.show()
    }
}

