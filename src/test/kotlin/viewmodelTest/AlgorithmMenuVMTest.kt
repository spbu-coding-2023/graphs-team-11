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