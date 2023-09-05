package com.example.project1

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Button

class MainActivity : AppCompatActivity() {

    //PARTNER: Ethan Deporter

    val DEFAULT_INPUT = "0"
    var firstNumber = DEFAULT_INPUT //first number in calculation
    var secondNumber = DEFAULT_INPUT //second number in calculation
    var operatorPressed = false //keep track of when operator pressed
    var operator = "" //keep track of specific operator pressed

    //allows calculator to remember number in case of clicking '=' multiple times
    var equalsNumber = ""

    //accounts for edge case described in inScribe
    //If the user presses =, and then clicks a new number, acts as 'clear' action.
    var equalsPressed = false


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val displayTextView: TextView =
            findViewById(R.id.calculatorText) //calculator display text variable
        displayTextView.text = DEFAULT_INPUT //default number shown on calculator (0)

        fun executeCalculation() { //given firstNumber, operator, secondNumber, do math and set number on screen. // result of calculation becomes firstNumber
            var result = ""

            if (equalsNumber != DEFAULT_INPUT) {
                secondNumber = equalsNumber
            }

            if (operator == "/") {
                result = (firstNumber.toFloat()/secondNumber.toFloat()).toString()
            }

            if (operator == "*") {
                result = (firstNumber.toFloat()*secondNumber.toFloat()).toString()
            }

            if (operator == "+") {
                result = (firstNumber.toFloat()+secondNumber.toFloat()).toString()
            }

            if (operator == "-") {
                result = (firstNumber.toFloat()-secondNumber.toFloat()).toString()
            }

            //converts number back to integer if possible
            //i.e. 5.0 -> 5
            if (result.endsWith(".0")) {
                result = result.removeSuffix(".0")
            }

            //update everything
            secondNumber = DEFAULT_INPUT
            firstNumber = result
            displayTextView.text = result
            operatorPressed = false
        }

        //array of the operator ids to iterate over to ensure all are not pressed.
        val operatorIds = arrayOf(
            R.id.divideButton, R.id.multiplyButton, R.id.subtractButton, R.id.addButton
        )

        fun resetOperators() { //resets press state of operators for clear button or when another operator is pressed
            for (button in operatorIds) {
                val buttonObj: Button = findViewById(button)
            }
        }

        //buttons 0-9
        val numberButtonIds = arrayOf(
            R.id.zeroButton, R.id.oneButton, R.id.twoButton, R.id.threeButton,
            R.id.fourButton, R.id.fiveButton, R.id.sixButton, R.id.sevenButton,
            R.id.eightButton, R.id.nineButton
        )

        //iterate over number buttons, setup click listeners
        for (buttonId in numberButtonIds) {
            val numberButton: Button = findViewById(buttonId)

            numberButton.setOnClickListener {
                val clickedNumber = resources.getResourceEntryName(buttonId)
                    .removeSuffix("Button") //"zeroButton" -> "zero"

                val strValue = when (clickedNumber) { //convert to number format ("zero" -> "0")
                    "zero" -> "0"
                    "one" -> "1"
                    "two" -> "2"
                    "three" -> "3"
                    "four" -> "4"
                    "five" -> "5"
                    "six" -> "6"
                    "seven" -> "7"
                    "eight" -> "8"
                    "nine" -> "9"
                    else -> throw IllegalArgumentException("Unsupported string value")
                }

                //clear and start new expression.

                /* accounts for edge case described where
                   this variable is originally defined. */
                if (equalsPressed) {
                    secondNumber = DEFAULT_INPUT
                    operator = ""
                    operatorPressed = false

                    firstNumber = strValue
                    displayTextView.text = firstNumber

                    equalsPressed = false
                }
                else {
                if (operatorPressed) { //operator pressed down, go to second number
                    if (secondNumber == DEFAULT_INPUT && strValue != "0") { //first number pressed in sequence
                        displayTextView.text = strValue
                        secondNumber = strValue
                    } else { //already numbers in, just add them
                        val currentText = displayTextView.text.toString() //current text displayed
                        displayTextView.text = "$currentText$strValue"
                        secondNumber += strValue
                    }
                } else { //operator not pressed, add to first number
                    if (firstNumber == DEFAULT_INPUT && strValue != "0") { //first number pressed in sequence
                        displayTextView.text = strValue
                        firstNumber = strValue
                    } else { //already numbers in, just add them
                        val currentText = displayTextView.text.toString() //current text displayed
                        displayTextView.text = "$currentText$strValue"
                        firstNumber += strValue
                    }
                }
            }


            }
        }

        //decimal button
        val decimalButton: Button = findViewById(R.id.decimalButton)
        decimalButton.setOnClickListener {
            equalsNumber = DEFAULT_INPUT

            //clear and start new expression.

            /* accounts for edge case described where
               this variable is originally defined. */
            if (equalsPressed) {
                secondNumber = DEFAULT_INPUT
                operator = ""
                operatorPressed = false

                firstNumber = "."
                displayTextView.text = firstNumber

                equalsPressed = false
            }

            if (operatorPressed) { //2nd number
                if ('.' !in secondNumber) { //ensure number doesnt already have a decimal
                    val currentText = displayTextView.text.toString() //current text displayed

                    if (secondNumber == DEFAULT_INPUT) {
                        displayTextView.text = "."
                    } else {
                        displayTextView.text = "$currentText."
                    }

                    secondNumber += "."
                }
            } else { //1st number
                if ('.' !in firstNumber) { //ensure number doesnt already have a decimal
                    firstNumber += "."

                    val currentText = displayTextView.text.toString() //current text displayed
                    displayTextView.text = "$currentText."
                }
            }
        }

        //clear button
        val clearButton: Button = findViewById(R.id.clearButton)
        clearButton.setOnClickListener {
            //make everything default
            firstNumber = DEFAULT_INPUT
            secondNumber = DEFAULT_INPUT
            equalsNumber = DEFAULT_INPUT
            operator = ""
            operatorPressed = false
            equalsPressed = false
            displayTextView.text = DEFAULT_INPUT

            //TODO: RESET BUTTON PRESS STATES FUNCTION
        }

        // +/- button
        val plusMinusButton: Button = findViewById(R.id.negPosButton)
        plusMinusButton.setOnClickListener {
            if (operatorPressed) { //2nd num
                if ('-' !in secondNumber) {
                    secondNumber = "-$secondNumber"
                    displayTextView.text = secondNumber
                }
                else {
                    secondNumber = secondNumber.replace("-","")
                    displayTextView.text = secondNumber
                }
            } else { //1st num
                if ('-' !in firstNumber) {
                    firstNumber = "-$firstNumber"
                    displayTextView.text = firstNumber
                }
                else {
                    firstNumber = firstNumber.replace("-","")
                    displayTextView.text = firstNumber
                }
            }
        }

        // '/' - division button
        val divisionButton: Button = findViewById(R.id.divideButton)
        divisionButton.setOnClickListener {
            equalsNumber = DEFAULT_INPUT
            if (equalsPressed) { //edge case
                equalsPressed = false
                secondNumber = DEFAULT_INPUT
                equalsNumber = ""
            }

            if (operatorPressed) { //execute calculation
                executeCalculation()
            }
            operatorPressed = true
            operator = "/"
        }

        // * - multiply button
        val multiplyButton: Button = findViewById(R.id.multiplyButton)
        multiplyButton.setOnClickListener {
            equalsNumber = DEFAULT_INPUT
            if (equalsPressed) { //edge case
                equalsPressed = false
                secondNumber = DEFAULT_INPUT
            }

            if (operatorPressed) {
                executeCalculation()
            }
            operatorPressed = true
            operator = "*"
        }

        // '-' - subtract button
        val subtractButton: Button = findViewById(R.id.subtractButton)
        subtractButton.setOnClickListener {
            equalsNumber = DEFAULT_INPUT
            if (equalsPressed) { //edge case
                equalsPressed = false
                secondNumber = DEFAULT_INPUT
            }

            if (operatorPressed) {
                executeCalculation()
            }
            operatorPressed = true
            operator = "-"
        }

        // '+' - addition button
        val addButton: Button = findViewById(R.id.addButton)
        addButton.setOnClickListener {
            equalsNumber = DEFAULT_INPUT
            if (equalsPressed) { //edge case
                equalsPressed = false
                secondNumber = DEFAULT_INPUT
            }

            if (operatorPressed) {
                executeCalculation()
            }
            operatorPressed = true
            operator = "+"
        }

        // % (divide current num by 100) button
        val percentButton: Button = findViewById(R.id.percentageButton)
        percentButton.setOnClickListener {

            if (operatorPressed) { //2nd number
                secondNumber = (secondNumber.toFloat()/100).toString()
                displayTextView.text = secondNumber
            } else { //1st number
                firstNumber = (firstNumber.toFloat()/100).toString()
                displayTextView.text = firstNumber
            }
        }

        // = button
        val equalsButton: Button = findViewById(R.id.equalsButton)
        equalsButton.setOnClickListener {
            if (equalsNumber == DEFAULT_INPUT) { //saves input so you can keep pressing = if you want
                equalsNumber = secondNumber
            }

            if (operatorPressed && secondNumber == DEFAULT_INPUT) { //account for if equals pressed when 1 number inputted
                secondNumber = firstNumber
                executeCalculation()
                operatorPressed = true
            }
            else {
                executeCalculation()
            }


            equalsPressed = true
        }
    }
}

