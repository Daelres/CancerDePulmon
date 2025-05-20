package me.danielrestrepo.cancerdepulmon

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LungCancerApp("http://TU_IP_O_DOMINIO:8000")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LungCancerApp(baseUrl: String) {
    val context = LocalContext.current
    var gender by remember { mutableStateOf(1) }
    var age by remember { mutableStateOf(30f) }
    var smoking by remember { mutableStateOf(1) }
    var yellowF by remember { mutableStateOf(1) }
    var anxiety by remember { mutableStateOf(1) }
    var peerPressure by remember { mutableStateOf(1) }
    var chronicDisease by remember { mutableStateOf(1) }
    var fatigue by remember { mutableStateOf(1) }
    var allergy by remember { mutableStateOf(1) }
    var wheezing by remember { mutableStateOf(1) }
    var alcohol by remember { mutableStateOf(1) }
    var coughing by remember { mutableStateOf(1) }
    var shortBreath by remember { mutableStateOf(1) }
    var swallowing by remember { mutableStateOf(1) }
    var chestPain by remember { mutableStateOf(1) }

    var showDialog by remember { mutableStateOf(false) }
    var resultMsg by remember { mutableStateOf("") }

    val api = remember {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Detección Cáncer de Pulmón") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Género
            Text("Seleccione su género:")
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = gender == 1, onClick = { gender = 1 })
                Text("Masculino", modifier = Modifier.padding(end = 16.dp))
                RadioButton(selected = gender == 0, onClick = { gender = 0 })
                Text("Femenino")
            }
            // Edad
            Text("Edad: ${age.toInt()} años")
            Slider(
                value = age,
                onValueChange = { age = it },
                valueRange = 30f..90f,
                steps = 60
            )
            // Campos booleanos
            @Composable fun BoolField(label: String, state: Int, onSelect: (Int)->Unit) {
                Text(label)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = state == 1, onClick = { onSelect(1) })
                    Text("No", modifier = Modifier.padding(end = 16.dp))
                    RadioButton(selected = state == 2, onClick = { onSelect(2) })
                    Text("Sí")
                }
            }

            BoolField("¿Fuma habitualmente?", smoking) { smoking = it }
            BoolField("¿Dedos amarillos?", yellowF) { yellowF = it }
            BoolField("¿Ansiedad?", anxiety) { anxiety = it }
            BoolField("¿Presión social?", peerPressure) { peerPressure = it }
            BoolField("¿Enfermedad crónica?", chronicDisease) { chronicDisease = it }
            BoolField("¿Fatiga?", fatigue) { fatigue = it }
            BoolField("¿Alergia?", allergy) { allergy = it }
            BoolField("¿Sibilancias?", wheezing) { wheezing = it }
            BoolField("¿Consume alcohol?", alcohol) { alcohol = it }
            BoolField("¿Tos?", coughing) { coughing = it }
            BoolField("¿Falta de aliento?", shortBreath) { shortBreath = it }
            BoolField("¿Dificultad para tragar?", swallowing) { swallowing = it }
            BoolField("¿Dolor en el pecho?", chestPain) { chestPain = it }

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        val input = InputData(
                            GENDER = gender,
                            AGE = age.toInt(),
                            SMOKING = smoking,
                            YELLOW_FINGERS = yellowF,
                            ANXIETY = anxiety,
                            PEER_PRESSURE = peerPressure,
                            CHRONIC_DISEASE = chronicDisease,
                            FATIGUE_ = fatigue,
                            ALLERGY_ = allergy,
                            WHEEZING = wheezing,
                            ALCOHOL_CONSUMING = alcohol,
                            COUGHING = coughing,
                            SHORTNESS_OF_BREATH = shortBreath,
                            SWALLOWING_DIFFICULTY = swallowing,
                            CHEST_PAIN = chestPain
                        )
                        try {
                            val resp = api.predict(input)
                            withContext(Dispatchers.Main) {
                                resultMsg = if (resp.prediction == 1)
                                    "El paciente PRESENTA indicios de cáncer de pulmón."
                                else
                                    "El paciente NO presenta indicios de cáncer de pulmón."
                                showDialog = true
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Predecir")
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        TextButton(onClick = { showDialog = false }) { Text("Cerrar") }
                    },
                    text = { Text(resultMsg) }
                )
            }
        }
    }
}