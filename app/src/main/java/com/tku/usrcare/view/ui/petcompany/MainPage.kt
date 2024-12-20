package com.tku.usrcare.view.ui.petcompany

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.tku.usrcare.R
import com.tku.usrcare.view.component.TitleBox
import com.tku.usrcare.viewmodel.PetCompanyViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.tku.usrcare.view.component.FixedSizeText
import com.tku.usrcare.view.component.normalAlertDialog


@Composable
fun TopBar() {
    Column(
        Modifier
            .background(colorResource(id = R.color.bgPetCompany))
            .fillMaxWidth()
    ) {
        TitleBox(
            color = colorResource(id = R.color.btnPetcompanyColor),
            title = stringResource(id = R.string.pet_company),
            icon = painterResource(id = R.drawable.ic_petcompany)
        )
    }
}
//Store已經被移動到首頁轉為好物雜貨鋪
/*
@Composable
fun Store(petCompanyViewModel: PetCompanyViewModel, navController: NavHostController) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        /*Icon(//可能不要用icon寫
            painter = painterResource(id = R.drawable.ic_pet_health_bar),
            contentDescription = "親密度",
            modifier = Modifier
                .size(219.dp, 45.dp),
            tint = Color.Unspecified
        )*/
        Box(
            modifier = Modifier
                .size(100.dp)
                .shadow(elevation = 8.dp, shape = CircleShape)
                .border(
                    width = 5.dp,
                    color = colorResource(id = R.color.purple_200),
                    shape = CircleShape
                )
                .background(color = colorResource(id = R.color.bgSatKTV), CircleShape),
            contentAlignment = Alignment.Center
        )
        {
            Button(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                onClick = { navController.navigate("Store") },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.bgSatKTV),
                ), contentPadding = PaddingValues(1.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(50.dp),
                    painter = painterResource(id = R.drawable.ic_coin),
                    contentDescription = "寵物商店",
                    tint = Color.Unspecified
                )
            }
        }
    }
}
*/
var petName ="您的寵物"
@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun PetImage(petCompanyViewModel: PetCompanyViewModel, navController: NavHostController) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val heightFraction = 0.6//圖片高度佔螢幕高度的比例
    val boxHeight = (screenHeightDp * heightFraction).dp
    var petDetailVisible by remember { mutableStateOf(false) }
    val stepCount = petCompanyViewModel.stepCount.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }

    data class petInformation(
        val informationName: String,
        val number: Int,
    )
    val petInformations = listOf(
        petInformation("行走里程",stepCount.value),
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(boxHeight),
        //.padding(18.dp),
        //.background(color = Color.Blue),
        contentAlignment = Alignment.Center
    ) {
        Column {
            if (petDetailVisible) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(32.dp))
                        .background(color = colorResource(id = R.color.bgPetCompanyCard))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Box(contentAlignment = Alignment.TopEnd) {
                            Image(
                                painter = painterResource(id = R.drawable.pet),
                                contentDescription = "寵物小圖",
                                modifier = Modifier
                                    .height((screenHeightDp * 0.2).dp)
                                    .fillMaxWidth()
                                    .zIndex(1f)
                            )
                            Icon(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable { petDetailVisible = !petDetailVisible }
                                    .zIndex(2f),
                                painter = painterResource(id = R.drawable.close),
                                contentDescription = "返回",
                                tint = Color.Unspecified
                            )
                        }
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            FixedSizeText(
                                text = petName,
                                size = 80.dp,
                                fontWeight = FontWeight.Bold
                            )
                            TextButton(
                                modifier = Modifier
                                    .size(35.dp)
                                    .clip(RoundedCornerShape(24.dp)),
                                onClick = { showDialog = true },
                                contentPadding = PaddingValues(1.dp),
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(35.dp)
                                        .background(color = colorResource(id = R.color.bgPetCompanyCard)),
                                    painter = painterResource(id = R.drawable.pencil),
                                    contentDescription = "改名按鈕",
                                    tint = Color.Black,
                                )
                            }
                        }
                        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally)
                        {
                            items(petInformations) {
                                item ->
                                FixedSizeText(
                                text = item.informationName+":"+item.number+"步",
                                color = Color.Black,
                                size = 60.dp,
                                fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            } else {
                //Store(petCompanyViewModel = petCompanyViewModel, navController = navController)
                Image(
                    painter = painterResource(id = R.drawable.pet),
                    contentDescription = "寵物狗狗圖",
                    modifier = Modifier
                        .height((screenHeightDp * 0.45).dp)
                        .fillMaxWidth()
                        .clickable { petDetailVisible = !petDetailVisible })
            }
        }
    }
    if (showDialog){
        AlertDialog(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(5.dp, colorResource(id =R.color.btnPetcompanyColor),shape = RoundedCornerShape(16.dp)),
            backgroundColor = colorResource(id =R.color.bgPetCompany),
            onDismissRequest = { showDialog = false },
            title = {                Column {
                FixedSizeText(text = "此功能尚未完成!!",size=80.dp, fontWeight = FontWeight.Bold)
                TextField(
                    value = newName,//改成顯示目前名稱
                    onValueChange = { newName = it },
                    modifier = Modifier.fillMaxWidth().background(color = Color.White),
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFF2F3032),
                    ),
                    placeholder = {
                        FixedSizeText(
                            text="請輸入新名稱",
                            size=60.dp,
                            //color = colorResource(id = R.color.btnMoodScaleColor)
                        )
                    },
                    singleLine = true,
                )
            }},
            text = {
            },
            buttons = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { showDialog = false},
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id =R.color.btnPetcompanyColor),
                            contentColor = Color.White
                        ), modifier = Modifier.align(Alignment.CenterVertically).padding(8.dp)
                    ) {
                        FixedSizeText("取消", size = 60.dp, color = Color.White)
                    }
                    Button(
                        onClick = { showDialog = false},
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id =R.color.btnPetcompanyColor),
                            contentColor = Color.White
                        ), modifier = Modifier.align(Alignment.CenterVertically).padding(8.dp)
                    ) {
                        FixedSizeText("確認", size = 60.dp, color = Color.White)
                    }
                }
            },properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        )
    }
}


@Composable
fun UseItem() {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val heightFraction = 0.2//圖片高度佔螢幕高度的比例
    val boxHeight = (screenHeightDp * heightFraction).dp
    var showUseInformation by remember { mutableStateOf(false) }
    data class PetItem(val title: String, val image: Painter)

    val PetItems = listOf(
        PetItem("道具1", painterResource(id = R.drawable.ball)),
        PetItem("道具2", painterResource(id = R.drawable.food)),
        PetItem("道具3", painterResource(id = R.drawable.clean_item)),
    )

    normalAlertDialog(
        title = "提示",
        content = "你沒有足夠的道具",
        buttonText = "我知道了",
        showDialog=showUseInformation,
        onDismiss = { showUseInformation = false },
        onConfirm = { },
        color = colorResource(id = R.color.btnPetcompanyColor),
        backgroundColor = colorResource(id = R.color.bgPetCompany),)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(boxHeight),
        //.background(color= Color.Black),
        contentAlignment = Alignment.Center,
    )
    {
        LazyRow(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
            items(PetItems) { item ->
                Box(contentAlignment = Alignment.BottomEnd) {
                    Button(
                        onClick = {showUseInformation=true},
                        modifier = Modifier
                            .padding(12.dp)
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(
                                5.dp,
                                colorResource(id = R.color.btnPetcompanyColor),
                                CircleShape
                            ),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id=R.color.bgPetCompany),
                        )
                    ) {
                        Box(modifier = Modifier.zIndex(1f)) {
                            Image(
                                modifier = Modifier
                                    .zIndex(1f)
                                    .fillMaxSize()
                                    .padding(3.dp),
                                painter = item.image,
                                contentDescription = "道具圖片",
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(color = Color.Blue)
                            .size(40.dp)
                            .zIndex(2f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "0",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainPage(petCompanyViewModel: PetCompanyViewModel, navController: NavHostController) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .background(color = Color(ContextCompat.getColor(context, R.color.bgPetCompany))),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar()
            //HealthAndStore(petCompanyViewModel = petCompanyViewModel, navController = navController)
            PetImage(petCompanyViewModel, navController = navController)
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter)
            {
                UseItem()
            }
            //這邊可以添加新的function
        }
    }
}
