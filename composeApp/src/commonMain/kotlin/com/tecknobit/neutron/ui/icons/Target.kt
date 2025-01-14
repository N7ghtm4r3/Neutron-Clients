package com.tecknobit.neutron.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Target: ImageVector
	get() {
		if (_Target != null) {
			return _Target!!
		}
		_Target = ImageVector.Builder(
            name = "Target",
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
				moveTo(480f, 880f)
				quadToRelative(-83f, 0f, -156f, -31.5f)
				reflectiveQuadTo(197f, 763f)
				reflectiveQuadToRelative(-85.5f, -127f)
				reflectiveQuadTo(80f, 480f)
				reflectiveQuadToRelative(31.5f, -156f)
				reflectiveQuadTo(197f, 197f)
				reflectiveQuadToRelative(127f, -85.5f)
				reflectiveQuadTo(480f, 80f)
				reflectiveQuadToRelative(156f, 31.5f)
				reflectiveQuadTo(763f, 197f)
				reflectiveQuadToRelative(85.5f, 127f)
				reflectiveQuadTo(880f, 480f)
				reflectiveQuadToRelative(-31.5f, 156f)
				reflectiveQuadTo(763f, 763f)
				reflectiveQuadToRelative(-127f, 85.5f)
				reflectiveQuadTo(480f, 880f)
				moveToRelative(0f, -80f)
				quadToRelative(134f, 0f, 227f, -93f)
				reflectiveQuadToRelative(93f, -227f)
				reflectiveQuadToRelative(-93f, -227f)
				reflectiveQuadToRelative(-227f, -93f)
				reflectiveQuadToRelative(-227f, 93f)
				reflectiveQuadToRelative(-93f, 227f)
				reflectiveQuadToRelative(93f, 227f)
				reflectiveQuadToRelative(227f, 93f)
				moveToRelative(0f, -80f)
				quadToRelative(-100f, 0f, -170f, -70f)
				reflectiveQuadToRelative(-70f, -170f)
				reflectiveQuadToRelative(70f, -170f)
				reflectiveQuadToRelative(170f, -70f)
				reflectiveQuadToRelative(170f, 70f)
				reflectiveQuadToRelative(70f, 170f)
				reflectiveQuadToRelative(-70f, 170f)
				reflectiveQuadToRelative(-170f, 70f)
				moveToRelative(0f, -80f)
				quadToRelative(66f, 0f, 113f, -47f)
				reflectiveQuadToRelative(47f, -113f)
				reflectiveQuadToRelative(-47f, -113f)
				reflectiveQuadToRelative(-113f, -47f)
				reflectiveQuadToRelative(-113f, 47f)
				reflectiveQuadToRelative(-47f, 113f)
				reflectiveQuadToRelative(47f, 113f)
				reflectiveQuadToRelative(113f, 47f)
				moveToRelative(0f, -80f)
				quadToRelative(-33f, 0f, -56.5f, -23.5f)
				reflectiveQuadTo(400f, 480f)
				reflectiveQuadToRelative(23.5f, -56.5f)
				reflectiveQuadTo(480f, 400f)
				reflectiveQuadToRelative(56.5f, 23.5f)
				reflectiveQuadTo(560f, 480f)
				reflectiveQuadToRelative(-23.5f, 56.5f)
				reflectiveQuadTo(480f, 560f)
			}
		}.build()
		return _Target!!
	}

private var _Target: ImageVector? = null
