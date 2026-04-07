package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.tisto.kmp.helper.ui.icon.MyIcon
import com.tisto.kmp.helper.ui.icon.myicon.IcCameraSolar
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.ui.theme.TextAppearance
import com.tisto.kmp.helper.utils.model.PickedImage

@Composable
fun CardImagePicker(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    elevation: Dp = 0.dp,
    strokeWidth: Dp = 0.3.dp,
    iconSize: Dp = 30.dp,
    strokeColor: Color = Colors.Gray4,
    contentPadding : Dp = Spacing.small,
    isShowLabel: Boolean = true,
    onPicker: ((PickedImage?) -> Unit)? = null
) {

    val picker = rememberImagePickerState()
    LaunchedEffect(picker.picked) {
        onPicker?.invoke(picker.picked)
    }

    LaunchedEffect(picker.errorMessage) {
        println("picker.errorMessage: ${picker.errorMessage}")
    }

    CustomCard(
        modifier = modifier,
        elevation = elevation,
        strokeWidth = strokeWidth,
        strokeColor = strokeColor
    ) {
        CustomCardBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            elevation = 0.dp,
            backgroundColor = Colors.Gray5,
            onClick = picker::pick
        ) {

            if (picker.bitmap != null) {
                Image(
                    bitmap = picker.bitmap!!,
                    contentDescription = picker.picked?.name,
                    modifier = Modifier
                        .fillMaxSize()
                )
            } else {
                if (!imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "photoUrl",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Icon(
                            imageVector = MyIcon.IcCameraSolar,
                            contentDescription = null,
                            modifier = Modifier.size(iconSize),
                            tint = Colors.Gray3
                        )

                        if (isShowLabel){
                            Spacer(modifier = Modifier.height(Spacing.small))

                            Text(
                                text = "Upload Photo",
                                style = TextAppearance.label2(),
                                color = Colors.Gray3
                            )
                        }

                    }
                }
            }

        }
    }

}


//@Preview
//@Composable
//private fun CardImagePicker1() {
//    CardImagePicker(
//        modifier = Modifier
//            .width(75.dp)
//            .height(75.dp),
//        contentPadding = 0.dp,
//        strokeWidth = 0.dp,
//        onPicker = {
////                updateRequest { copy(pickedImage = it) }
//        }
//    )
//}

@Preview
@Composable
private fun CardImagePicker2() {



    Text("Ini apa")
//    CardImagePicker(
//        modifier = Modifier
//            .width(75.dp)
//            .height(75.dp),
//        onPicker = {
//
//        }
//    )
}