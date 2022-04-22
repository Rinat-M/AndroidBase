package com.rino.homework02

import org.junit.Assert.*
import org.junit.Test
import java.lang.NullPointerException
import java.text.ParseException

class CalculatorTest {

    @Test
    fun calculator_OperationMultiply_Equals() {
        val calculator = Calculator().apply {
            enteredValue("2")
            applyOperation(Operation.MULTIPLY)
            enteredValue("2")
            applyOperation(Operation.CALCULATE)
        }

        assertEquals(4.0, calculator.operationResult, 0.01)
    }

    @Test
    fun calculator_OperationPlus_NotEquals() {
        val calculator = Calculator().apply {
            enteredValue("5")
            applyOperation(Operation.PLUS)
            enteredValue("5")
            applyOperation(Operation.CALCULATE)
        }

        assertNotEquals(5.0, calculator.operationResult, 0.01)
    }

    @Test
    fun calculator_IncorrectEnteredValue_Throws() {
        val calculator = Calculator().apply {
            enteredValue("A")
            applyOperation(Operation.DIVIDE)
            enteredValue("0")
        }

        assertThrows(ParseException::class.java) { calculator.invokeOperation() }
    }

    @Test
    fun calculator_EnteredValueIsNull_Throws() {
        val calculator = Calculator()
        assertThrows(NullPointerException::class.java) { calculator.enteredValue(null) }
    }

    @Test
    fun test_SameArray_Equals() {
        val firstArray = intArrayOf(1, 2, 3, 4, 5)
        val secondArray = intArrayOf(1, 2, 3, 4, 5)
        assertArrayEquals(firstArray, secondArray)
    }

    @Test
    fun test_SameArray_Same() {
        val firstArray = intArrayOf(1, 2, 3, 4, 5)
        val secondArray = firstArray
        assertSame(firstArray, secondArray)
    }

    @Test
    fun calculator_Operand1IsFilled_NotNull() {
        val calculator = Calculator().apply {
            enteredValue("2")
            applyOperation(Operation.DIVIDE)
            enteredValue("0")
        }

        assertNotNull(calculator.operand1)
    }

}