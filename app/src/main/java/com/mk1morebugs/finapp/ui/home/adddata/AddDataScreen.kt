package com.mk1morebugs.finapp.ui.home.adddata

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.mk1morebugs.finapp.ui.categories.CategoriesScreen
import com.mk1morebugs.finapp.ui.theme.Shapes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDataScreen(
    viewModel: AddDataViewModel = viewModel(),
    navController: NavHostController,
    snackBarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val showBottomSheet = rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val openDateDialog = rememberSaveable { mutableStateOf(false) }

    var showAddResult by rememberSaveable { mutableStateOf(false) }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(150.dp))

        OutlinedTextField(
            value = uiState.about.orEmpty(),
            onValueChange = { viewModel.setDescription(it) },
            label = { Text("Описание") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = uiState.amount,
            singleLine = true,
            onValueChange = { viewModel.setAmount(it) },
            label = { Text("Сумма, ₽") },

            supportingText = {
                if (uiState.errorMessage == ErrorMessage.AmountOverLimit) {
                    Text("Превышен лимит в 2 147 483 647 ₽")
                } else if (uiState.errorMessage == ErrorMessage.AmountIsEmpty) {
                    Text("Обязательное поле")
                } else {
                    Text("Целое число \nили выражение вида: x * y =")
                }
            },

            isError = uiState.errorTextField
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

                // Выбор категории
                Button(
                    onClick = {
                        showBottomSheet.value = true
                    },
                    shape = Shapes.small
                ) {
                    Text(text = "Выбрать категорию")
                }

            // Выбор даты
            Button(
                onClick = { openDateDialog.value = true },
                shape = Shapes.small
            ) {
                Text(text = "Выбрать дату")
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                viewModel.addData()
                showAddResult = true
            }
        ) {
            Text(text = "Добавить запись")
        }


        if (showAddResult && uiState.isLoading) {
            CircularProgressIndicator()

        } else if (showAddResult) {
            if (uiState.errorMessage != null) {
                SideEffect {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            message = if (uiState.errorMessage == ErrorMessage.AmountIsEmpty) {
                                "Поле \"Сумма\" не может быть пустым!"
                            } else if (uiState.errorMessage == ErrorMessage.AmountNotInt) {
                                "Поле \"Сумма\" должно содержать число"
                            } else if (uiState.errorMessage == ErrorMessage.AmountOverLimit) {
                                "Превышен лимит в 2 147 483 647 ₽"
                            } else {
                                "Категория не выбрана!"
                            }
                        )
                    }
                }

            } else {
                SideEffect {
                    coroutineScope.launch {

                        snackBarHostState.showSnackbar(
                            message = "Запись успешно добавлена!"
                        )
                    }
                }
                navController.popBackStack()
            }
            showAddResult = false
        }

    }


    if (showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet.value = false },
            sheetState = sheetState,
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "Доход")

                Spacer(modifier = Modifier.width(10.dp))

                Switch(
                    checked = uiState.isIncome,
                    onCheckedChange = {
                        viewModel.setIsIncomeValue(it)
                        viewModel.updateData()
                    }
                )

            }

            CategoriesScreen(
                categories = uiState.categories,
                selectedCategoryId = uiState.selectedCategoryId,
                navController = navController,
                showBottomSheet = showBottomSheet,
                sheetState = sheetState,
                coroutineScope = coroutineScope,
                selectCategory = { viewModel.setCategory(selectedCategoryId = it) },
                deleteCategoryById = { viewModel.deleteCategoryById(id = it) }
            )
        }
    }

    if (openDateDialog.value) {
        ShowDatePicker(
            openDateDialog = openDateDialog,
            setDate = { viewModel.setDate(selectedDate = it) },
            selectedDate = uiState.selectedDate
        )
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDatePicker(
    openDateDialog: MutableState<Boolean>,
    setDate: (Long?) -> Unit,
    selectedDate: Long?
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate
    )
    val confirmEnabled = remember {
        derivedStateOf { datePickerState.selectedDateMillis != null }
    }


    DatePickerDialog(
        onDismissRequest = {
            openDateDialog.value = false
        },
        confirmButton = {
            TextButton(
                onClick = {
                    setDate(datePickerState.selectedDateMillis)
                    openDateDialog.value = false
                },
                enabled = confirmEnabled.value
            ) {
                Text("OK")
            }
        },

        dismissButton = {
            TextButton(
                onClick = { openDateDialog.value = false }
            ) {
                Text("Отмена")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
