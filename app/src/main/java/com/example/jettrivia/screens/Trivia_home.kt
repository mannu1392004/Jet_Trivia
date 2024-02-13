package com.example.jettrivia.screens

import androidx.compose.runtime.Composable
import com.example.jettrivia.components.Questions

@Composable
fun TriviaHome(viewModel: QuestionsViewModel = androidx.lifecycle.viewmodel.compose.viewModel<QuestionsViewModel>() ) {
Questions(viewModel = viewModel)

}
