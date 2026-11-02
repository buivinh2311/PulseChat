package com.example.core_resource

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimens(
    val Space: SpaceDimen,
    val ButtonSize: ButtonSizeDimen,
    val ImageSize: ImageSizeDimen,
    val Radius: RadiusDimen,
    val Ripple: RippleDimen,
    val Layout: LayoutDimen,
    val Icon: IconDimen,
    val Divider: DividerDimen,
    val Border: BorderDimen
) {

    data class SpaceDimen(
        val Es: Dp,
        val Xxs: Dp,
        val Xs: Dp,
        val Sm: Dp,
        val Md: Dp,
        val Lg: Dp,
        val Xl: Dp,
        val Xxl: Dp,
        val El: Dp
    )

    data class ButtonSizeDimen(
        val Xs: Dp,
        val Sm: Dp,
        val Md: Dp
    )

    data class ImageSizeDimen(
        val Es: Dp,
        val Xxs: Dp,
        val Xs: Dp,
        val Sm: Dp,
        val Md: Dp,
        val Lg: Dp,
        val Xl: Dp,
        val Xxl: Dp
    )

    data class RadiusDimen(
        val Xxs: Dp,
        val Xs: Dp,
        val Sm: Dp,
        val Md: Dp,
        val Lg: Dp,
        val Xl: Dp,
        val Xxl: Dp
    )

    data class RippleDimen(
        val Sm: Dp,
        val Md: Dp,
        val Lg: Dp,
        val Xl: Dp
    )

    data class LayoutDimen(
        val TopBarHeight: Dp,
        val BottomBarHeight: Dp,
        val SearchBarHeight: Dp,
        val ChatItemHeight: Dp,
        val ButtonHeight: Dp,
        val StatusWidth: Dp,
        val IconButtonSize: Dp,
        val TextFieldHeight: Dp,
        val CoverPhotoHeight: Dp,
        val MinTextMessageWidth: Dp
    )

    data class IconDimen(
        val Xxs: Dp,
        val Xs: Dp,
        val Sm: Dp,
        val Md: Dp,
        val Lg: Dp,
        val Xl: Dp
    )

    data class DividerDimen(
        val Thin: Dp,
        val Normal: Dp,
        val Thick: Dp
    )

    data class BorderDimen(
        val Thin: Dp,
        val Normal: Dp,
        val Thick: Dp
    )
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun dimens(): Dimens {
    val width = LocalConfiguration.current.screenWidthDp
    val scale = width / 390f

    return Dimens(
        Space = Dimens.SpaceDimen(
            Es = (1 * scale).dp,
            Xxs = (2 * scale).dp,
            Xs = (4 * scale).dp,
            Sm = (8 * scale).dp,
            Md = (12 * scale).dp,
            Lg = (16 * scale).dp,
            Xl = (24 * scale).dp,
            Xxl = (48 * scale).dp,
            El = (56 * scale).dp
        ),

        ButtonSize = Dimens.ButtonSizeDimen(
            Xs = (16 * scale).dp,
            Sm = (40 * scale).dp,
            Md = (48 * scale).dp
        ),

        ImageSize = Dimens.ImageSizeDimen(
            Es = (12 * scale).dp,
            Xxs = (26 * scale).dp,
            Xs = (40 * scale).dp,
            Sm = (52 * scale).dp,
            Md = (56 * scale).dp,
            Lg = (64 * scale).dp,
            Xl = (72 * scale).dp,
            Xxl = (112 * scale).dp
        ),

        Radius = Dimens.RadiusDimen(
            Xxs = (2 * scale).dp,
            Xs = (4 * scale).dp,
            Sm = (8 * scale).dp,
            Md = (12 * scale).dp,
            Lg = (16 * scale).dp,
            Xl = (24 * scale).dp,
            Xxl = (32 * scale).dp
        ),

        Ripple = Dimens.RippleDimen(
            Sm = (20 * scale).dp,
            Md = (30 * scale).dp,
            Lg = (40 * scale).dp,
            Xl = (56 * scale).dp
        ),

        Layout = Dimens.LayoutDimen(
            TopBarHeight = (48 * scale).dp,
            BottomBarHeight = (56 * scale).dp,
            SearchBarHeight = (48 * scale).dp,
            ChatItemHeight = (64 * scale).dp,
            ButtonHeight = (36 * scale).dp,
            StatusWidth = (80 * scale).dp,
            IconButtonSize = (36 * scale).dp,
            TextFieldHeight = (48 * scale).dp,
            CoverPhotoHeight = (180 * scale).dp,
            MinTextMessageWidth = (56 * scale).dp
        ),

        Icon = Dimens.IconDimen(
            Xxs = (18 * scale).dp,
            Xs = (22 * scale).dp,
            Sm = (26 * scale).dp,
            Md = (30 * scale).dp,
            Lg = (32 * scale).dp,
            Xl = (40 * scale).dp
        ),

        Divider = Dimens.DividerDimen(
            Thin = (0.5 * scale).dp,
            Normal = (1 * scale).dp,
            Thick = (3 * scale).dp
        ),

        Border = Dimens.BorderDimen(
            Thin = (0.5 * scale).dp,
            Normal = (1 * scale).dp,
            Thick = (3 * scale).dp
        )
    )
}

val LocalAppDimens = staticCompositionLocalOf<Dimens> {
    error("AppDimens has not been provided")
}

val AppDimens: Dimens
    @Composable
    get() = LocalAppDimens.current