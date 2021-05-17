package com.example.teamproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Switch
import androidx.fragment.app.Fragment

/*커스터마이징*/

// id는 Activity 이름, 역할을 추가한다.
// textViewCustomizingSaturday, switchCustomizingSaturday
// textViewCustomizingSunday, switchCustomizingSunday
// textViewCustomizingToday, switchCustomizingToday
// textViewCustomizingDate, switchCustomizingDate
// textViewCustomizingSelect, spinnerCustomizingSelect
// textViewCustomizingColor, switchCustomizingColor
// textViewCustomizingFormat, switchCustomizingFormat
// textViewCustomizingMenu1, textViewCustomizingMenu2
// textViewCustomizingBackground, spinnerCustomizingBackground

class CustomizingFragment : Fragment() {

    lateinit var customizingDBHelper: CustomizingDBHelper
    private val selectItems = arrayOf(
            "GRAY","WHITE","RED","MAGENTA","YELLOW","GREEN","BLUE","CYAN"
    )
    private val backgroundItems = arrayOf(
            "목련", "벚꽃",
            "물망초", "그린 카네이션",
            "민들레", "코스모스"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_customizing, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        customizingDBHelper = CustomizingDBHelper(context)

        val switchSaturday = view?.findViewById<Switch>(R.id.switchCustomizingSaturday)
        val switchSunday = view?.findViewById<Switch>(R.id.switchCustomizingSunday)
        val switchToday = view?.findViewById<Switch>(R.id.switchCustomizingToday)
        val switchDate = view?.findViewById<Switch>(R.id.switchCustomizingDate)
        val spinnerColor = view?.findViewById<Spinner>(R.id.spinnerCustomizingColor)
        val spinnerSelect = view?.findViewById<Spinner>(R.id.spinnerCustomizingSelect)
        val switchFormat = view?.findViewById<Switch>(R.id.switchCustomizingFormat)
        val spinnerBackground = view?.findViewById<Spinner>(R.id.spinnerCustomizingBackground)

        if (switchSaturday != null && switchSunday != null && switchToday != null && //init
                switchDate != null  && spinnerColor != null && spinnerSelect != null && switchFormat != null &&
                spinnerBackground != null) {
            initSetting(customizingDBHelper, switchSaturday, switchSunday, switchToday,
                    switchDate, spinnerColor, spinnerSelect, switchFormat, spinnerBackground)
        }

        switchSaturday?.setOnCheckedChangeListener { _, isChecked -> //Saturday Customizing
            if(isChecked){
                val data = CustomizingData("saturday","on")
                customizingDBHelper.updateCustomizing(data)
            } else {
                val data = CustomizingData("saturday","off")
                customizingDBHelper.updateCustomizing(data)
            }
        }
        switchSunday?.setOnCheckedChangeListener { _, isChecked -> //Sunday Customizing
            if(isChecked){
                val data = CustomizingData("sunday","on")
                customizingDBHelper.updateCustomizing(data)
            } else {
                val data = CustomizingData("sunday","off")
                customizingDBHelper.updateCustomizing(data)
            }
        }
        switchToday?.setOnCheckedChangeListener { _, isChecked -> //Today Customizing
            if(isChecked){
                val data = CustomizingData("today","on")
                customizingDBHelper.updateCustomizing(data)
            } else {
                val data = CustomizingData("today","off")
                customizingDBHelper.updateCustomizing(data)
            }
        }
        switchDate?.setOnCheckedChangeListener { _, isChecked -> //Date Customizing
            if(isChecked){
                val data = CustomizingData("date","on")
                customizingDBHelper.updateCustomizing(data)
            } else {
                val data = CustomizingData("date","off")
                customizingDBHelper.updateCustomizing(data)
            }
        }
        val spinnerColoradapter = context?.let { //Background Color Customizing
            ArrayAdapter(
                    it.applicationContext, android.R.layout.simple_spinner_item, backgroundItems
            )
        }
        spinnerColoradapter?.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinnerColor?.adapter = spinnerColoradapter
        spinnerColor?.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position){
                    0->customizingDBHelper.updateCustomizing(CustomizingData("color","#FFFFFF"))
                    1->customizingDBHelper.updateCustomizing(CustomizingData("color","#FBE4E4"))
                    2->customizingDBHelper.updateCustomizing(CustomizingData("color","#DDF0F3"))
                    3->customizingDBHelper.updateCustomizing(CustomizingData("color","#D1F3D2"))
                    4->customizingDBHelper.updateCustomizing(CustomizingData("color","#F8F5DA"))
                    5->customizingDBHelper.updateCustomizing(CustomizingData("color","#E7DDFA"))
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        val spinnerSelectadapter = context?.let { //Select Color Customizing
            ArrayAdapter(
                    it.applicationContext, android.R.layout.simple_spinner_item, selectItems
            )
        }
        spinnerSelectadapter?.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinnerSelect?.adapter = spinnerSelectadapter
        spinnerSelect?.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position){
                    0->customizingDBHelper.updateCustomizing(CustomizingData("select","GRAY"))
                    1->customizingDBHelper.updateCustomizing(CustomizingData("select","WHITE"))
                    2->customizingDBHelper.updateCustomizing(CustomizingData("select","RED"))
                    3->customizingDBHelper.updateCustomizing(CustomizingData("select","MAGENTA"))
                    4->customizingDBHelper.updateCustomizing(CustomizingData("select","YELLOW"))
                    5->customizingDBHelper.updateCustomizing(CustomizingData("select","GREEN"))
                    6->customizingDBHelper.updateCustomizing(CustomizingData("select","BLUE"))
                    7->customizingDBHelper.updateCustomizing(CustomizingData("select","CYAN"))
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        switchFormat?.setOnCheckedChangeListener { _, isChecked -> //Format Customizing
            if(isChecked){
                val data = CustomizingData("format","on")
                customizingDBHelper.updateCustomizing(data)
            } else {
                val data = CustomizingData("format","off")
                customizingDBHelper.updateCustomizing(data)
            }
        }
        val spinnerBackgroundadapter = context?.let { //Background Color Customizing
            ArrayAdapter(
                    it.applicationContext, android.R.layout.simple_spinner_item, backgroundItems
            )
        }
        spinnerBackgroundadapter?.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinnerBackground?.adapter = spinnerBackgroundadapter
        spinnerBackground?.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position){
                    0->customizingDBHelper.updateCustomizing(CustomizingData("background","#FFFFFF"))
                    1->customizingDBHelper.updateCustomizing(CustomizingData("background","#FBE4E4"))
                    2->customizingDBHelper.updateCustomizing(CustomizingData("background","#DDF0F3"))
                    3->customizingDBHelper.updateCustomizing(CustomizingData("background","#D1F3D2"))
                    4->customizingDBHelper.updateCustomizing(CustomizingData("background","#F8F5DA"))
                    5->customizingDBHelper.updateCustomizing(CustomizingData("background","#E7DDFA"))
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val switchSaturday = view?.findViewById<Switch>(R.id.switchCustomizingSaturday)
        val switchSunday = view?.findViewById<Switch>(R.id.switchCustomizingSunday)
        val switchToday = view?.findViewById<Switch>(R.id.switchCustomizingToday)
        val switchDate = view?.findViewById<Switch>(R.id.switchCustomizingDate)
        val spinnerColor = view?.findViewById<Spinner>(R.id.spinnerCustomizingColor)
        val spinnerSelect = view?.findViewById<Spinner>(R.id.spinnerCustomizingSelect)
        val switchFormat = view?.findViewById<Switch>(R.id.switchCustomizingFormat)
        val spinnerBackground = view?.findViewById<Spinner>(R.id.spinnerCustomizingBackground)

        if (switchSaturday != null && switchSunday != null && switchToday != null && //init
                switchDate != null  && spinnerColor != null && spinnerSelect != null && switchFormat != null &&
                spinnerBackground != null) {
            initSetting(customizingDBHelper, switchSaturday, switchSunday, switchToday,
                    switchDate, spinnerColor, spinnerSelect, switchFormat, spinnerBackground)
        }
    }

    private fun initSetting(helper:CustomizingDBHelper, saturday:Switch,
                            sunday:Switch, today:Switch, date:Switch, color:Spinner, select:Spinner,
                            format:Switch, background:Spinner){
        if(helper.findCustomizing("saturday")=="on"){
            saturday.setChecked(true)
        }
        if(helper.findCustomizing("sunday")=="on"){
            sunday?.setChecked(true)
        }
        if(helper.findCustomizing("today")=="on"){
            today?.setChecked(true)
        }
        if(helper.findCustomizing("date")=="on"){
            date?.setChecked(true)
        }
        when(helper.findCustomizing("color")){
            "#FFFFFF"->color.setSelection(0)
            "#FBE4E4"->color.setSelection(1)
            "#DDF0F3"->color.setSelection(2)
            "#D1F3D2"->color.setSelection(3)
            "#F8F5DA"->color.setSelection(4)
            "#E7DDFA"->color.setSelection(5)
        }
        when(helper.findCustomizing("select")){
            "GRAY"-> select.setSelection(0)
            "WHITE"-> select.setSelection(1)
            "RED"-> select.setSelection(2)
            "MAGENTA"-> select.setSelection(3)
            "YELLOW"-> select.setSelection(4)
            "GREEN"-> select.setSelection(5)
            "BLUE"-> select.setSelection(6)
            "CYAN"-> select.setSelection(7)
        }
        if(helper.findCustomizing("format")=="on"){
            format?.setChecked(true)
        }
        when(helper.findCustomizing("background")){
            "#FFFFFF"->background.setSelection(0)
            "#FBE4E4"->background.setSelection(1)
            "#DDF0F3"->background.setSelection(2)
            "#D1F3D2"->background.setSelection(3)
            "#F8F5DA"->background.setSelection(4)
            "#E7DDFA"->background.setSelection(5)
        }
    }
}