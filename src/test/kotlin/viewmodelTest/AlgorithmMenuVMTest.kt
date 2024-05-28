/*
 * This file is part of BDSM Graphs.
 * 
 * BDSM Graphs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BDSM Graphs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with . If not, see <https://www.gnu.org/licenses/>.
 */

package viewmodelTest

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import model.algoritms.Algoritm
import model.graph_model.GraphViewClass
import model.graph_model.graph_model_actions.Update
import viewmodel.AlgorithmMenuVM
import io.mockk.*
import kotlin.test.*

class AlgorithmMenuVMTest {
    private lateinit var viewModel: AlgorithmMenuVM

    @BeforeTest
    fun setup() {
        viewModel = AlgorithmMenuVM()
    }

    @Test
    fun `initial state is correct`() {
        assertEquals(250.dp, viewModel.menuWidth)
        assertTrue(viewModel.isMenuVisible.value)
        assertFalse(viewModel.isException.value)
        assertEquals("", viewModel.exceptionMessage.value)
    }

    @Test
    fun `toggleMenu changes isMenuVisible state`() {
        val initialState = viewModel.isMenuVisible.value

        viewModel.toggleMenu()

        assertEquals(!initialState, viewModel.isMenuVisible.value)

        viewModel.toggleMenu()

        assertEquals(initialState, viewModel.isMenuVisible.value)
    }

    @Test
    fun `runAlgorithm updates graphViewClass and changesAlgo state`() {
        val mockAlgorithm = mockk<Algoritm>()
        val mockGraphViewClass = mockk<GraphViewClass>(relaxed = true)
        val changedAlgo = mutableStateOf(false)
        val selected = mutableStateMapOf<String, Int>()

        val mockUpdate = mockk<Update>()
        every { mockAlgorithm.algoRun(any(), any()) } returns mockUpdate

        viewModel.runAlgorithm(mockAlgorithm, mockGraphViewClass, changedAlgo, selected)

        verify { mockGraphViewClass.applyUpdate(mockUpdate) }
        assertTrue(changedAlgo.value)
    }
}
