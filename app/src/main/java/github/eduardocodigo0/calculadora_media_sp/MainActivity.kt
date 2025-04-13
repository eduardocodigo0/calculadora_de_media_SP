package github.eduardocodigo0.calculadora_media_sp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import github.eduardocodigo0.calculadora_media_sp.ui.theme.Calculadora_media_SPTheme

class MainActivity : ComponentActivity() {


    class ScreenState {
        val nota_maxima = 10.0
        val peso_alvo = 100.0
        val nota_minima = 0.0


        var nota1 by mutableStateOf("")
        var nota2 by mutableStateOf("")
        var nota3 by mutableStateOf("")
        var peso1 by mutableStateOf("")
        var peso2 by mutableStateOf("")
        var peso3 by mutableStateOf("")

        var result by mutableStateOf("")
        var error by mutableStateOf("")

        fun updateNota1(nota1: String) {
            this.nota1 = nota1
        }

        fun updateNota2(nota2: String) {
            this.nota2 = nota2
        }

        fun updateNota3(nota3: String) {
            this.nota3 = nota3
        }

        fun updatePeso1(peso1: String) {
            this.peso1 = peso1
        }

        fun updatePeso2(peso2: String) {
            this.peso2 = peso2
        }

        fun updatePeso3(peso3: String) {
            this.peso3 = peso3
        }

        fun calcular(context: Context) {
            var res = ""

            runCatching {

                val nota1 = nota1.toDouble()
                val nota2 = nota2.toDouble()
                val nota3 = nota3.toDouble()
                val peso1 = peso1.toDouble()
                val peso2 = peso2.toDouble()
                val peso3 = peso3.toDouble()

                if((peso1 + peso2 + peso3) != peso_alvo) {
                    res = context.getString(R.string.erro_peso_maior_que_100)
                    this.result = res
                    return
                }

                if(nota1 !in nota_minima..nota_maxima || nota2 !in nota_minima..nota_maxima || nota3 !in nota_minima..nota_maxima) {
                    res = context.getString(R.string.erro_nota_invalida)
                    this.result = res
                    return
                }
                val soma = (nota1 * peso1) + (nota2 * peso2) + (nota3 * peso3)
                val pesoTotal = peso1 + peso2 + peso3

                res = ((soma / pesoTotal)).toString()

            } .onFailure {
                res = context.getString(R.string.erro_indeterminado)
            }

            this.result = res
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Calculadora_media_SPTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val screenState by remember { mutableStateOf(ScreenState()) }
                    val state = rememberScrollState()
                    LaunchedEffect(Unit) { state.animateScrollTo(100) }

                    Column(modifier = Modifier
                        .padding(8.dp)
                        .verticalScroll(state)) {
                        CalcFields(
                            modifier = Modifier.padding(innerPadding),
                            screenState
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalcFields(modifier: Modifier = Modifier, screenState: MainActivity.ScreenState) {
    val context = LocalContext.current
    Column {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            DefaultVerticalSpacer()
            GradeField(
                name = stringResource(R.string.edit_nota1),
                screenState.nota1,
                screenState::updateNota1,
                screenState.peso1,
                screenState::updatePeso1
            )
            DefaultVerticalSpacer()
            GradeField(
                name = stringResource(R.string.edit_nota2),
                screenState.nota2,
                screenState::updateNota2,
                screenState.peso2,
                screenState::updatePeso2
            )
            DefaultVerticalSpacer()
            GradeField(
                name = stringResource(R.string.edit_nota3),
                screenState.nota3,
                screenState::updateNota3,
                screenState.peso3,
                screenState::updatePeso3
            )
        }


        DefaultVerticalSpacer()
        DefaultVerticalSpacer()
        Button(
            onClick = { screenState.calcular(context) },
            Modifier.width(256.dp),
            shape = ButtonDefaults.outlinedShape
        ) {
            Text(
                text = stringResource(R.string.bt_calcular),
                fontSize = TextUnit(20f, TextUnitType.Sp)
            )
        }

        DefaultVerticalSpacer()
        Text(
            text = "${stringResource(R.string.tv_resultado)} ${screenState.result}",
            fontSize = TextUnit(20f, TextUnitType.Sp)
        )
        DefaultVerticalSpacer()
    }
}


@Composable
fun GradeField(
    name: String,
    notaState: String,
    updateNota: (String) -> Unit,
    pesoState: String,
    updatePeso: (String) -> Unit
) {

    Row() {
        OutlinedTextField(
            value = notaState,
            maxLines = 1,
            onValueChange = { updateNota(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(name) }
        )
        DefaultHorizontalSpacer()
        OutlinedTextField(
            value = pesoState,
            onValueChange = { updatePeso(it) },
            maxLines = 1,
            label = { Text(stringResource(R.string.edit_peso)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}


@Composable
fun DefaultVerticalSpacer(modifier: Modifier = Modifier) {
    Spacer(modifier = Modifier.padding(vertical = 12.dp))
}

@Composable
fun DefaultHorizontalSpacer(modifier: Modifier = Modifier) {
    Spacer(modifier = Modifier.padding(horizontal = 6.dp))
}