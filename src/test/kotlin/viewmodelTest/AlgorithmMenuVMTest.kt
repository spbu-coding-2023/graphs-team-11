package viewmodelTest

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import model.algoritms.Algoritm
import model.graph_model.GraphViewClass
import model.graph_model.graph_model_actions.Update
import org.junit.jupiter.api.BeforeAll
import viewmodel.AlgorithmMenuVM
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import io.mockk.*

class AlgorithmMenuVMTest {

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

    companion object {
        private lateinit var viewModel: AlgorithmMenuVM

        @JvmStatic
        @BeforeAll
        fun setup() {
            viewModel = AlgorithmMenuVM()
        }
    }
}