package com.example.jettrivia.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.jettrivia.model.QuestionItem
import com.example.jettrivia.screens.QuestionsViewModel
import com.example.jettrivia.util.App_Colors

@SuppressLint("SuspiciousIndentation")
@Composable
fun Questions(viewModel: QuestionsViewModel) {
val questionindex = remember {
    mutableIntStateOf(0)

}
    val size = viewModel.data.value.data?.size


val progress = remember {
    mutableDoubleStateOf(0.0)
}
    if (size!=null)
    progress.doubleValue =  (questionindex.intValue/ size.toDouble())






    if (viewModel.data.value.loading
    ){
        Surface(
            modifier = Modifier
                .fillMaxSize()
            , color = App_Colors.mDarkPurple)
        {
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {


            CircularProgressIndicator()
        }}

    }
    else{
        viewModel.data.value.data?.let { QuestionDisplay(
            question = it[questionindex.intValue], questionIndex = questionindex,
            viewModel =viewModel,progress.doubleValue ) }


    }



}




@Composable
fun QuestionDisplay(
    question: QuestionItem, questionIndex:MutableState<Int>
    , viewModel: QuestionsViewModel, progress: Double
){
val pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f,10f), phase = 0f)

    val choiceState = remember(question){
question.choices.toMutableList()
}
    val answerstate = remember(question) {
        mutableStateOf<Int?>(null)
    }

    val correctAnswerState = remember(question) {
        mutableStateOf<Boolean?>(null)
    }

    val updateAnswer:(Int) ->Unit = remember(question) {
        {
            answerstate.value = it
            correctAnswerState.value = choiceState[it]==question.answer
        }
    }
Surface(
    modifier = Modifier
        .fillMaxSize()

       , color = App_Colors.mDarkPurple)
{
    Column(modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
// progresss
       Showprogress(progress)



// question trecker
        Questiontrecker(viewModel = viewModel, counter = questionIndex)

            // dooted line
        Drawdottedline(pathEffect = pathEffect)

        Column {
// question

            Text(text = question.question,
    modifier = Modifier
        .weight(1.2f)
        .padding(6.dp)
        .align(Alignment.Start)
     ,
 fontSize = 4.em, fontWeight = FontWeight.Bold, color = App_Colors.mOffWhite)






    // choices
            choiceState.forEachIndexed{
                index, s ->
Row(modifier = Modifier
    .weight(0.3f)
    .padding(3.dp)
    .fillMaxWidth()
    .height(45.dp)
    .border(
        width = 4.dp, brush = Brush.linearGradient(
            colors = listOf(App_Colors.mOffWhite, App_Colors.mOffDarkPurple, Color.Cyan)
        ), shape = RoundedCornerShape(15.dp)
    )
    .clip(
        RoundedCornerShape(
            topStartPercent = 50,
            topEndPercent = 50,
            bottomEndPercent = 50,
            bottomStartPercent = 50
        )
    )
    .background(Color.Transparent)
, verticalAlignment = Alignment.CenterVertically) {
RadioButton(selected =(answerstate.value==index) , onClick = {
    updateAnswer(index)
}, modifier = Modifier.padding(start = 16.dp)
, colors = RadioButtonDefaults.colors(selectedColor =
    if (correctAnswerState.value==true&&index==answerstate.value)
    {
        Color.Green}
else{
    Color.Red
}))// end

    Text(text = s,modifier = Modifier

        .padding(6.dp)

        ,
        fontSize = 4.em, fontWeight = FontWeight.Bold, color = App_Colors.mOffWhite)

}


            }

//
            Row(
                modifier = Modifier.weight(0.8f)
                , verticalAlignment = Alignment.Bottom
            ) {
Button(onClick = {
   if (questionIndex.value>0){ questionIndex.value--} }
,shape = RoundedCornerShape(10.dp)
, modifier = Modifier.weight(1f)) {
    Text(text = "Previous")
}


Spacer(modifier = Modifier.weight(1.5f))
                Button(
                    onClick = {

                    questionIndex.value++
Log.d("progressssssssssssssssssss","$")

                              }
                , modifier = Modifier
                        , shape = RoundedCornerShape(10.dp)
                , colors = buttonColors(Color.Magenta)) {
                    Text(text = "Next")

                }
            }


        }



    }

}
}



@Composable
fun Drawdottedline(pathEffect: androidx.compose.ui.graphics.PathEffect){
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)){

        drawLine(color = Color.LightGray, start = Offset(x = 0F,y= 0F),
            end = Offset(size.width, y = 0f)
, pathEffect =    pathEffect  )
    }


}




@Composable
fun Questiontrecker(counter:MutableState<Int>,
                    viewModel: QuestionsViewModel) {
val x = counter.value+1

        Text(
            text = "Question " + x + "/" + viewModel.data.value.data?.size,
            fontSize = 6.em,
            color = App_Colors.mOffWhite,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp, top = 30.dp)
        )



}



@Composable
fun Showprogress(score:Double){
val gradient = Brush.linearGradient(listOf(Color(0xFFF95075), Color(0xFFF95075)))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
            .height(45.dp)
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        App_Colors.mOffWhite, App_Colors.mLightGray, Color.Cyan, Color.Magenta
                    )
                ), shape = RoundedCornerShape(
                    topEndPercent = 50,
                    topStartPercent = 50,
                    bottomStartPercent = 50,
                    bottomEndPercent = 50
                )
            )
            .clip(
                RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50, bottomStartPercent = 50, bottomEndPercent = 50
                )
            )
            .background(Color.Transparent)
        , verticalAlignment = Alignment.CenterVertically
    ) {

Button(contentPadding = PaddingValues(1.dp)
    ,onClick = {  }
    , modifier = Modifier
        .fillMaxWidth(

            score.toFloat()
        )
        .background(brush = gradient)
    , enabled = false,
    elevation = null,
    colors = buttonColors(
        containerColor = Color.Transparent,

        disabledContainerColor = Color.Transparent
    )
) {

}
        Text(text = (score.toFloat()*10).toInt().toString())


    }
}

