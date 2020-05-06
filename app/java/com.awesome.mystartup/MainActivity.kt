package com.awesome.mystartup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buEqual.setOnClickListener{

            var mypost:ArrayList<String> = prefix_postfix()
            var myans:Double = postfix_evaluation(mypost)

            var myAnsList = myans.toString().split(".")

            if ((myans/(myans.toInt()))== 1.toDouble()) {
                textOut.setText(myAnsList[0])
            } else {
                textOut.setText("%.2f".format(myans))
            }
        }
    }

    var exp = ""
    var mypostfix = ArrayList<String>()
    var priority = hashMapOf( "(" to 0, ")" to 0, "+" to 1, "-" to 1, "x" to 2, "/" to 2)

    //buIn gets called after any button is pressed
    fun buIn(view:View)
    {
        var buSelected = view as Button

        when(buSelected.id)
        {
            R.id.bu0 -> {exp += "0"}
            R.id.bu1 -> {exp += "1"}
            R.id.bu2 -> {exp += "2"}
            R.id.bu3 -> {exp += "3"}
            R.id.bu4 -> {exp += "4"}
            R.id.bu5 -> {exp += "5"}
            R.id.bu6 -> {exp += "6"}
            R.id.bu7 -> {exp += "7"}
            R.id.bu8 -> {exp += "8"}
            R.id.bu9 -> {exp += "9"}
            R.id.buStartBracket -> {exp += "("}
            R.id.buEndBracket -> {exp += ")"}
            R.id.buDot -> {exp += "."}
            R.id.buAdd -> {exp += "+"}
            R.id.buSub -> {exp += "-"}
            R.id.buMult -> {exp += "x"}
            R.id.buDiv -> {exp += "/"}
            R.id.buAC -> {
                exp = ""
                mypostfix.clear()
                textIn.setText(null)
                textOut.setText(null)
            }
            R.id.buDel -> {
                var temp_exp = ""
                for (each_char in exp)
                {
                    if (each_char != exp[exp.length - 1])
                    {
                        temp_exp += each_char
                    }
                }
                exp = temp_exp
            }
        }
        textIn.setText(exp)
    }

    fun prefix_postfix():ArrayList<String> //This should run only once and only after complete expression is entered
    {
        var mystack = ArrayList<String>()

        var temp = ""
        var index = 0
        while (index < exp.length)  // INCREMENT OF INDEX NEED TO BE DONE SOMEWHERE
        {

            if(priority.containsKey(exp[index].toString()))  //Check if it's an operator
            {
                if(temp != "")
                {
                    mypostfix.add(temp)
                    temp = ""
                } //Adds digit to mypostfix as a string


                if ((mystack.size != 0) && (priority.get(mystack.last())!! >= priority.get(exp[index].toString())!!) && (priority.get(exp[index].toString()) != 0))
                {  //This will not run if last elem of mystack is "(" and ")" due to priorities
                    while((priority[mystack.last()])!! >= (priority[exp[index].toString()]!!))
                    {
                        mypostfix.add(mystack.last())
                        mystack.removeAt((mystack.size - 1))

                        try {
                            var last_stack_priority = priority[mystack.last()]
                        }
                        catch (e:Exception)
                        { break } //Break the loop if mystack is empty and .last() is called

                    }  // if last elem in stack has higher priority than
                    //current elem, move the last elem to postfix and current elem to stack
                    if(mystack.size == 0)
                    {
                        mystack.add(exp[index].toString())
                        index += 1
                    }
                    else
                    {
                        mystack[mystack.size - 1] = exp[index].toString()
                        index += 1
                    }
                }
                else if (exp[index].toString() != ")")
                {
                    //This will run in case of "("
                    mystack.add(exp[index].toString())
                    index += 1

                }

                else if((exp[index].toString() == ")"))
                {
                    var count = mystack.size - 1
                    while(mystack[count] != "(")
                    {
                        print(mystack[count])
                        mypostfix.add(mystack[count]) //This is beautiful
                        mystack.removeAt(count) // Delete the element so no issues at the end
                        count -= 1
                    }
                    //Delete the opening bracket from mystack
                    mystack.removeAt(count)
                    index += 1
                }

            }



            else
            {
                temp += exp[index].toString() //Makes digit
                index += 1
            }

        }
        if(temp != "")
        {
            mypostfix.add(temp)
        } //Adds final digit
        var stack_count = mystack.size - 1
        while(stack_count >= 0)
        {
            mypostfix.add(mystack[stack_count])
            stack_count -= 1
        }
        return mypostfix
    }

    fun postfix_evaluation(mypostfix:ArrayList<String>):Double
    {
        var mystack = ArrayList<Double>()
        var top:Double
        var second_top:Double
        textOut.setText(mypostfix.toString())

        for (mychar in mypostfix)
        {
            if (!(priority.containsKey(mychar)))
            {
                mystack.add(mychar.toDouble())
            }

            else
            {
                top = mystack.last()
                mystack.removeAt((mystack.size - 1))
                second_top = mystack.last()
                mystack.removeAt((mystack.size-1))

                when(mychar)
                {
                    "+" -> {
                        top = second_top + top
                    }

                    "-" -> {
                        top = second_top - top
                    }

                    "x" -> {
                        top = second_top * top
                    }

                    "/" -> {
                        top = second_top / top
                    }
                }
                mystack.add(top)

            }
        }

        return mystack[0]
    }

}
