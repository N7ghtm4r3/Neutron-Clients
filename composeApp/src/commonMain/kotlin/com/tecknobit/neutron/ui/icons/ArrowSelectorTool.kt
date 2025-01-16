package com.tecknobit.neutron.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val ArrowSelectorTool: ImageVector
	get() {
		if (_ArrowSelectorTool != null) {
			return _ArrowSelectorTool!!
		}
		_ArrowSelectorTool = ImageVector.Builder(
            name = "ArrowSelectorTool",
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
				moveTo(320f, 550f)
				lineToRelative(79f, -110f)
				horizontalLineToRelative(170f)
				lineTo(320f, 244f)
				close()
				moveTo(551f, 880f)
				lineTo(406f, 568f)
				lineTo(240f, 800f)
				verticalLineToRelative(-720f)
				lineToRelative(560f, 440f)
				horizontalLineTo(516f)
				lineToRelative(144f, 309f)
				close()
				moveTo(399f, 440f)
			}
		}.build()
		return _ArrowSelectorTool!!
	}

private var _ArrowSelectorTool: ImageVector? = null
