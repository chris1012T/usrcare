package com.tku.usrcare.view.ui.scale

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.tku.usrcare.R
import com.tku.usrcare.api.ApiUSR
import com.tku.usrcare.model.Question
import com.tku.usrcare.model.ReturnSheet
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.ui.Loading
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun SheetTitle() {

}

@Composable
fun Scale(id: Int, navController: NavController) {
    val isLoadingVisible = remember { mutableStateOf(true) }
    val isOk = remember { mutableStateOf(false) }
    val scaleTitle = remember { mutableStateOf("") }
    val specialOption = remember { mutableStateListOf<String>() }
    val questions = remember { mutableStateListOf<Question>() }
    val context = LocalContext.current
    val nowQuestion = remember { mutableIntStateOf(0) }
    val startTime = remember { mutableStateOf("") }
    val endTime = remember { mutableStateOf("") }
    val timeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.TAIWAN)
    val nowMainColor =
        Color(android.graphics.Color.parseColor(SessionManager(context).getNowMainColor()))
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                navController.popBackStack()
                showDialog = false
            },
            title = {
                Text(text = "提示")
            },
            text = {
                Text("已完成。")
            },
            confirmButton = {
                TextButton(onClick = {
                    navController.popBackStack()
                    showDialog = false
                }) {
                    Text("確定")
                }
            },
            properties = DialogProperties() // 你可以使用此參數來設定對話框的其他屬性，例如是否可以取消、背景顏色等
        )
    }

    Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
        Loading(isLoadingVisible.value)
    }




    @Composable
    fun Content() {
        LaunchedEffect(Unit) {
            ApiUSR.getScale(context as Activity, id, onSuccess = { response ->
                scaleTitle.value = response.sheetTitle
                response.specialOption.forEach { (key, value) ->
                    specialOption.add("$key: $value")
                }

                questions.addAll(response.questions) // 將問題列表添加到可記憶的狀態中
                isOk.value = true
                //紀錄開始時間 YYYY-MM-ddTHH:mm:ss
                startTime.value = timeFormat.format(System.currentTimeMillis())
                Log.d("startTime", startTime.value)

            }, onError = {
                println(it)
                isOk.value = true
            })
        }
        if (isOk.value) {
            isLoadingVisible.value = false
            val answers =
                remember { mutableStateListOf(*IntArray(questions.size) { -1 }.toTypedArray()) }
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = questions[nowQuestion.intValue].ques, fontSize = 25.sp)
                val clickedIndex = remember { mutableIntStateOf(-1) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp, 50.dp, 12.dp, 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    questions[nowQuestion.intValue].ans.forEachIndexed { index, ans ->
                        // 如果此按鈕的索引與當前被點擊的索引匹配，則添加外框
                        val borderStroke = if (clickedIndex.intValue == index) BorderStroke(
                            6.dp, nowMainColor
                        ) else BorderStroke(0.dp, Color.Transparent)

                        Button(
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = nowMainColor.copy(
                                    0.2F
                                )
                            ), border = borderStroke, onClick = {
                                // 更新當前被點擊的按鈕的索引
                                // 如果當前索引已經被點擊，則重置為 -1
                                clickedIndex.intValue =
                                    if (clickedIndex.intValue == index) -1 else index
                                answers[nowQuestion.intValue] = index
                            },elevation = ButtonDefaults.elevation(
                                defaultElevation = 0.dp,
                                pressedElevation = 0.dp,
                                disabledElevation = 0.dp
                            )) {
                            Text(text = ans.toString(), fontSize = 40.sp)
                        }
                        Spacer(modifier = androidx.compose.ui.Modifier.weight(1f))
                    }
                }


                // 確認是否為最後一題
                if (nowQuestion.intValue == questions.size - 1) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(70.dp), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                clickedIndex.intValue =
                                    answers[nowQuestion.intValue - 1] // 恢復上一個問題的選擇
                                nowQuestion.intValue -= 1 // 返回上一題
                            },
                            modifier = Modifier.size(50.dp), // 設定按鈕大小
                            shape = CircleShape, // 設定按鈕為圓形
                            colors = ButtonDefaults.buttonColors(backgroundColor = nowMainColor) // 設定底色為橘色
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_back_arr), // 使用drawable中的圖標
                                contentDescription = "上一題", // 為無障礙功能提供描述
                                modifier = Modifier.size(24.dp) // 設定圖標大小
                            )
                        }
                        Button(
                            //圓角
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = nowMainColor), // 設定底色為橘色
                            onClick = {
                                // 檢查最後一題是否已被回答
                                if (answers[nowQuestion.intValue] != -1) {
                                    //TODO: 送出問卷
                                    //紀錄結束時間 YYYY-MM-ddTHH:mm:ss
                                    endTime.value = timeFormat.format(System.currentTimeMillis())
                                    Log.d("endTime", endTime.value)
                                    // 所有答案+1
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        answers.replaceAll { it + 1 }
                                    }
                                    else{
                                        for (i in answers.indices) {
                                            answers[i] += 1
                                        }
                                    }
                                    val returnSheet = ReturnSheet(
                                        answers.map { it.toString() }.toTypedArray(),
                                        startTime.value,
                                        endTime.value
                                    )
                                    ApiUSR.postSheetResult(
                                        context as Activity,
                                        id.toString(),
                                        returnSheet)
                                    showDialog = true
                                } else {
                                    // 您可以在這裡添加提示，告知用戶最後一題必須回答
                                }
                            }) {
                            Text(text = "送出", fontSize = 23.sp, color = Color.White, style = androidx.compose.ui.text.TextStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold))
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(70.dp), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (nowQuestion.intValue > 0) {
                            Button(
                                onClick = {
                                    clickedIndex.intValue =
                                        answers[nowQuestion.intValue - 1] // 恢復上一個問題的選擇
                                    nowQuestion.intValue -= 1 // 返回上一題
                                },
                                modifier = Modifier.size(50.dp), // 設定按鈕大小
                                shape = CircleShape, // 設定按鈕為圓形
                                colors = ButtonDefaults.buttonColors(backgroundColor = nowMainColor) // 設定底色為橘色
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_back_arr), // 使用drawable中的圖標
                                    contentDescription = "上一題", // 為無障礙功能提供描述
                                    modifier = Modifier.size(24.dp) // 設定圖標大小
                                )
                            }
                        } // 如果不是第一題，則顯示上一題按鈕
                        Spacer(modifier = androidx.compose.ui.Modifier.weight(1f))
                        Button(
                            onClick = {
                                if (answers[nowQuestion.intValue] != -1) {
                                    clickedIndex.intValue = -1 // 清除外框
                                    nowQuestion.intValue += 1 // 移動到下一題
                                    // 如果下一題已經被回答，則恢復選擇
                                    if (answers[nowQuestion.intValue] != -1) {
                                        clickedIndex.intValue = answers[nowQuestion.intValue]
                                    }
                                } else {
                                    //TODO: 跳出提示必答
                                }
                            },
                            modifier = Modifier.size(50.dp), // 設定按鈕大小
                            shape = CircleShape, // 設定按鈕為圓形
                            colors = ButtonDefaults.buttonColors(backgroundColor = nowMainColor) // 設定底色為橘色

                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_next_arr), // 使用drawable中的圖標
                                contentDescription = "下一題", // 為無障礙功能提供描述
                                modifier = Modifier.size(24.dp) // 設定圖標大小
                            )
                        }
                    }
                }
            }
        }
    }

    Column(modifier = Modifier.padding(20.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, nowMainColor, RoundedCornerShape(10.dp)) // 設定外框的寬度、顏色和圓角
                .background(nowMainColor.copy(alpha = 0.2F))
                .padding(20.dp), contentAlignment = Alignment.Center
        ) {
            Text(
                text = scaleTitle.value,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                fontSize = 25.sp
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        Content()
    }


}
