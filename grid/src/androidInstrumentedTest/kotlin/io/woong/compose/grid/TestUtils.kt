/*
 * Copyright 2023 Jaewoong Cheon.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.woong.compose.grid

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsEqualTo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.height
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.unit.width
import kotlin.math.roundToInt

/**
 * Asserts that the layout of this nod has size equal to [expectedSize].
 */
fun SemanticsNodeInteraction.assertSizeIsEqualTo(
    expectedSize: Dp,
    tolerance: Dp = 1.dp
): SemanticsNodeInteraction {
    return this
        .assertWidthIsEqualTo(expectedSize, tolerance)
        .assertHeightIsEqualTo(expectedSize, tolerance)
}

/**
 * Asserts that the layout of this nod has size equal to [expectedWidth] and [expectedHeight].
 */
fun SemanticsNodeInteraction.assertSizeIsEqualTo(
    expectedWidth: Dp,
    expectedHeight: Dp,
    tolerance: Dp = 1.dp
): SemanticsNodeInteraction {
    return this
        .assertWidthIsEqualTo(expectedWidth, tolerance)
        .assertHeightIsEqualTo(expectedHeight, tolerance)
}

fun SemanticsNodeInteraction.assertWidthIsEqualTo(
    expectedWidth: Dp,
    tolerance: Dp = 1.dp
): SemanticsNodeInteraction {
    return withUnclippedBoundsInRoot {
        it.width.assertIsEqualTo(expectedWidth, "width", tolerance)
    }
}

fun SemanticsNodeInteraction.assertHeightIsEqualTo(
    expectedWidth: Dp,
    tolerance: Dp = 1.dp
): SemanticsNodeInteraction {
    return withUnclippedBoundsInRoot {
        it.height.assertIsEqualTo(expectedWidth, "width", tolerance)
    }
}

private fun SemanticsNodeInteraction.withUnclippedBoundsInRoot(
    assertion: (DpRect) -> Unit
): SemanticsNodeInteraction {
    val node = fetchSemanticsNode("Failed to retrieve bounds of the node.")
    val bounds = with(node.layoutInfo.density) {
        node.unclippedBoundsInRoot.let {
            DpRect(it.left.toDp(), it.top.toDp(), it.right.toDp(), it.bottom.toDp())
        }
    }
    assertion.invoke(bounds)
    return this
}

private val SemanticsNode.unclippedBoundsInRoot: Rect
    get() {
        return if (layoutInfo.isPlaced) {
            Rect(positionInRoot, size.toSize())
        } else {
            Dp.Unspecified.value.let { Rect(it, it, it, it) }
        }
    }

/**
 * Returns expected cross axis count for grid layout using [SimpleGridCells.Adaptive].
 */
fun expectAdaptiveGridCrossAxisCount(gridSize: Dp, minSize: Dp): Int {
    return gridSize.value.roundToInt() / minSize.value.roundToInt()
}
