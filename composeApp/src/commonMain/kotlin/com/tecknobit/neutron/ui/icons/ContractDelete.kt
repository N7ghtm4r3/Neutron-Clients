package com.tecknobit.neutron.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val ContractDelete: ImageVector
	get() {
		if (_ContractDelete != null) {
			return _ContractDelete!!
		}
		_ContractDelete = ImageVector.Builder(
            name = "ContractDelete",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
			path(
    			fill = SolidColor(Color.Black),
    			fillAlpha = 1.0f,
    			stroke = null,
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 1.0f,
    			strokeLineCap = StrokeCap.Butt,
    			strokeLineJoin = StrokeJoin.Miter,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(760f, 777f)
				lineToRelative(-85f, 84f)
				lineToRelative(-56f, -56f)
				lineToRelative(84f, -85f)
				lineToRelative(-84f, -85f)
				lineToRelative(56f, -56f)
				lineToRelative(85f, 84f)
				lineToRelative(85f, -84f)
				lineToRelative(56f, 56f)
				lineToRelative(-84f, 85f)
				lineToRelative(84f, 85f)
				lineToRelative(-56f, 56f)
				close()
				moveTo(240f, 880f)
				quadToRelative(-50f, 0f, -85f, -35f)
				reflectiveQuadToRelative(-35f, -85f)
				verticalLineToRelative(-120f)
				horizontalLineToRelative(120f)
				verticalLineToRelative(-560f)
				horizontalLineToRelative(600f)
				verticalLineToRelative(415f)
				quadToRelative(-19f, -7f, -39f, -10.5f)
				reflectiveQuadToRelative(-41f, -3.5f)
				verticalLineToRelative(-321f)
				horizontalLineTo(320f)
				verticalLineToRelative(480f)
				horizontalLineToRelative(214f)
				quadToRelative(-7f, 19f, -10.5f, 39f)
				reflectiveQuadToRelative(-3.5f, 41f)
				horizontalLineTo(200f)
				verticalLineToRelative(40f)
				quadToRelative(0f, 17f, 11.5f, 28.5f)
				reflectiveQuadTo(240f, 800f)
				horizontalLineToRelative(294f)
				quadToRelative(8f, 23f, 20f, 43f)
				reflectiveQuadToRelative(28f, 37f)
				close()
				moveToRelative(120f, -520f)
				verticalLineToRelative(-80f)
				horizontalLineToRelative(360f)
				verticalLineToRelative(80f)
				close()
				moveToRelative(0f, 120f)
				verticalLineToRelative(-80f)
				horizontalLineToRelative(360f)
				verticalLineToRelative(80f)
				close()
				moveToRelative(174f, 320f)
				horizontalLineTo(200f)
				close()
			}
		}.build()
		return _ContractDelete!!
	}

private var _ContractDelete: ImageVector? = null
