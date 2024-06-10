package com.example.errorpcalculator

import java.text.NumberFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.HistoricalChange
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.temporal.TemporalAmount
import com.example.errorpcalculator.ui.theme.ErrorPCalculatorTheme
import kotlin.math.abs


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ErrorPCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    ErrorCalc()
                }
            }
        }
    }
}

@Composable
fun ErrorCalc(modifier: Modifier = Modifier) {
    //Variables de la medida tomada experimentalmente
    var medidaInput by remember { mutableStateOf("")}
    val medida = medidaInput.toDoubleOrNull()?: 0.0

    //Variables del valor teorico calculado
    var calculoInput by remember { mutableStateOf("")}
    val valCalculado = calculoInput.toDoubleOrNull()?: 1.0

    val errorPercent = calcularError(valorCalc = valCalculado, valorMed = medida)
    println(errorPercent)

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 32.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.calculate_error),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        //Añadimos los campos de texto
        //campo de texto para el valor calculado
        EditNumberField(
            label = R.string.calculated_value,
            leadingIcon = R.drawable.money,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,    //Cambiamos el tipo de teclado a uno numerico
                imeAction = ImeAction.Next),
            value = calculoInput,
            onValueChange = {calculoInput = it},
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        //campo de texto para el valor medido
        EditNumberField(
            label = R.string.measure_value,
            leadingIcon = R.drawable.money,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,    //Cambiamos el tipo de teclado a uno numerico
                imeAction = ImeAction.Done),
            value = medidaInput,
            onValueChange = {medidaInput = it},
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        //Texto que muestra el resultado
        Text(
            text = stringResource(R.string.error_percent, "$errorPercent%"),
            style = MaterialTheme.typography.displaySmall
        )

        Spacer(modifier = Modifier.height(150.dp))
    }
}

//Funcion composable para los campos de texto
@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        label = { Text(stringResource(label))},
        leadingIcon = {Icon(painter = painterResource(id = leadingIcon), null)},
        singleLine = true,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        modifier = modifier
    )
}

//Funcion que calcula el error obtenido
fun calcularError(valorCalc: Double, valorMed: Double): String {
    val errorT = abs(100*((valorCalc-valorMed)/valorCalc))

    return NumberFormat.getInstance().format(errorT)
}

