package com.example.teamproject

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.teamproject.custom.CustomDBHelper
import com.example.teamproject.custom.CustomData

/*커스터마이징*/

// id는 Activity 이름, 역할을 추가한다.

// textViewCustomMenu1
// [textViewCustomSaturday, switchCustomSaturday], [textViewCustomSunday, switchCustomSunday]
// [textViewCustomToday, switchCustomToday], [textViewCustomDate, switchCustomDate]
// [textViewCustomSelect, spinnerCustomSelect], [textViewCustomColor, switchCustomColor]
// [textViewCustomFormat, switchCustomFormat]

// textViewCustomMenu2
// [textViewCustomBackground, spinnerCustomBackground], [textViewCustomMemo, spinnerCustomMemo]
// [textViewCustomRecord, spinnerCustomMemo]

// textViewCustomMenu3
// [textViewCustomReset, buttonReset]

class CustomFragment : Fragment() {

    lateinit var customDBHelper: CustomDBHelper

    private val selectItems = arrayOf(
            "GRAY","WHITE","RED","MAGENTA","YELLOW","GREEN","BLUE","CYAN"
    )
    private val backgroundItems = arrayOf(
            "목련", "벚꽃", "물망초", "그린 카네이션",
            "유채", "코스모스", "장미", "금잔화",
            "민들레", "도라지"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_custom, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        customDBHelper = CustomDBHelper(context)

        val switchSaturday = view?.findViewById<Switch>(R.id.switchCustomSaturday)
        val switchSunday = view?.findViewById<Switch>(R.id.switchCustomSunday)
        val switchToday = view?.findViewById<Switch>(R.id.switchCustomToday)
        val switchDate = view?.findViewById<Switch>(R.id.switchCustomDate)
        val spinnerColor = view?.findViewById<Spinner>(R.id.spinnerCustomColor)
        val spinnerSelect = view?.findViewById<Spinner>(R.id.spinnerCustomSelect)
        val switchFormat = view?.findViewById<Switch>(R.id.switchCustomFormat)
        val spinnerBackground = view?.findViewById<Spinner>(R.id.spinnerCustomBackground)
        val spinnerMemo = view?.findViewById<Spinner>(R.id.spinnerCustomMemo)
        val spinnerRecord = view?.findViewById<Spinner>(R.id.spinnerCustomRecord)
        val buttonReset = view?.findViewById<Button>(R.id.buttonCustomReset)

        if (switchSaturday != null && switchSunday != null && switchToday != null && //init
            switchDate != null  && spinnerColor != null && spinnerSelect != null && switchFormat != null &&
            spinnerBackground != null && spinnerMemo != null && spinnerRecord != null) {
            initSetting(customDBHelper, switchSaturday, switchSunday, switchToday, switchDate, spinnerColor,
                spinnerSelect, switchFormat, spinnerBackground, spinnerMemo, spinnerRecord)
        }

        if(switchSaturday != null) switchV1(customDBHelper, switchSaturday, "saturday") //Saturday Customizing
        if(switchSunday != null) switchV1(customDBHelper, switchSunday, "sunday") //Sunday Customizing
        if(switchToday != null) switchV1(customDBHelper, switchToday, "today") //Today Customizing
        if(switchDate != null) switchV1(customDBHelper, switchDate, "date") //Date Customizing
        if(spinnerColor != null) spinnerV2(customDBHelper,spinnerColor,"color") //Color Customizing
        if(spinnerSelect != null) spinnerV1(customDBHelper, spinnerSelect, "select") //Select Customizing
        if(switchFormat != null) switchV1(customDBHelper, switchFormat, "format") //Format Customizing
        if(spinnerBackground != null) spinnerV2(customDBHelper,spinnerBackground,"background") //Background Customizing
        if(spinnerMemo != null) spinnerV2(customDBHelper,spinnerMemo,"memo") //Memo Customizing
        if(spinnerRecord != null) spinnerV2(customDBHelper,spinnerRecord,"record") //Record Customizing
        buttonReset?.setOnClickListener {  //Customizing Reset
            alertDlg(customDBHelper)
        }
    }

    override fun onResume() {
        super.onResume()

        customDBHelper = CustomDBHelper(context)

        val switchSaturday = view?.findViewById<Switch>(R.id.switchCustomSaturday)
        val switchSunday = view?.findViewById<Switch>(R.id.switchCustomSunday)
        val switchToday = view?.findViewById<Switch>(R.id.switchCustomToday)
        val switchDate = view?.findViewById<Switch>(R.id.switchCustomDate)
        val spinnerColor = view?.findViewById<Spinner>(R.id.spinnerCustomColor)
        val spinnerSelect = view?.findViewById<Spinner>(R.id.spinnerCustomSelect)
        val switchFormat = view?.findViewById<Switch>(R.id.switchCustomFormat)
        val spinnerBackground = view?.findViewById<Spinner>(R.id.spinnerCustomBackground)
        val spinnerMemo = view?.findViewById<Spinner>(R.id.spinnerCustomMemo)
        val spinnerRecord = view?.findViewById<Spinner>(R.id.spinnerCustomRecord)

        if (switchSaturday != null && switchSunday != null && switchToday != null && //init
            switchDate != null  && spinnerColor != null && spinnerSelect != null && switchFormat != null &&
            spinnerBackground != null && spinnerMemo != null && spinnerRecord != null) {
            initSetting(customDBHelper, switchSaturday, switchSunday, switchToday, switchDate, spinnerColor,
                spinnerSelect, switchFormat, spinnerBackground, spinnerMemo, spinnerRecord)
        }
    }

    private fun initSetting(helper: CustomDBHelper, saturday:Switch, sunday:Switch, today:Switch, date:Switch,
                            color:Spinner, select:Spinner, format:Switch, background:Spinner,
                            memo:Spinner, record:Spinner){
        initSwitch(helper, saturday, "saturday")
        initSwitch(helper, sunday, "sunday")
        initSwitch(helper, today, "today")
        initSwitch(helper, date, "date")
        initSpinnerV2(helper, color, "color")
        initSpinnerV1(helper, select, "select")
        initSwitch(helper, format, "format")
        initSpinnerV2(helper, background, "background")
        initSpinnerV2(helper, memo, "memo")
        initSpinnerV2(helper, record, "record")
    }

    private fun initSwitch(helper: CustomDBHelper, switch: Switch, option: String){
        if(helper.findCustomizing(option)=="on") switch.setChecked(true)
    }

    private fun initSpinnerV1(helper: CustomDBHelper, spinner: Spinner, option: String){
        when(helper.findCustomizing(option)){
            "GRAY"->{spinner.setSelection(0)
                     spinner.setBackgroundColor(Color.GRAY)}
            "WHITE"-> {spinner.setSelection(1)
                       spinner.setBackgroundColor(Color.WHITE)}
            "RED"-> {spinner.setSelection(2)
                     spinner.setBackgroundColor(Color.RED)}
            "MAGENTA"-> {spinner.setSelection(3)
                         spinner.setBackgroundColor(Color.MAGENTA)}
            "YELLOW"-> {spinner.setSelection(4)
                        spinner.setBackgroundColor(Color.YELLOW)}
            "GREEN"-> {spinner.setSelection(5)
                       spinner.setBackgroundColor(Color.GREEN)}
            "BLUE"-> {spinner.setSelection(6)
                      spinner.setBackgroundColor(Color.BLUE)}
            "CYAN"-> {spinner.setSelection(7)
                      spinner.setBackgroundColor(Color.CYAN)}
        }
    }

    private fun initSpinnerV2(helper: CustomDBHelper, spinner: Spinner, option: String){
        when(helper.findCustomizing(option)){
            "#FFFFFF"->{spinner.setSelection(0)
                        spinner.setBackgroundColor(Color.parseColor("#FFFFFF"))}
            "#FBE4E4"->{spinner.setSelection(1)
                        spinner.setBackgroundColor(Color.parseColor("#FBE4E4"))}
            "#DDF0F3"->{spinner.setSelection(2)
                        spinner.setBackgroundColor(Color.parseColor("#DDF0F3"))}
            "#D1F3D2"->{spinner.setSelection(3)
                        spinner.setBackgroundColor(Color.parseColor("#D1F3D2"))}
            "#F8F5DA"->{spinner.setSelection(4)
                        spinner.setBackgroundColor(Color.parseColor("#F8F5DA"))}
            "#E7DDFA"->{spinner.setSelection(5)
                        spinner.setBackgroundColor(Color.parseColor("#E7DDFA"))}
            "#F44336"->{spinner.setSelection(6)
                        spinner.setBackgroundColor(Color.parseColor("#F44336"))}
            "#FF9800"->{spinner.setSelection(7)
                        spinner.setBackgroundColor(Color.parseColor("#FF9800"))}
            "#FFEB3B"->{spinner.setSelection(8)
                        spinner.setBackgroundColor(Color.parseColor("#FFEB3B"))}
            "#673AB7"->{spinner.setSelection(9)
                        spinner.setBackgroundColor(Color.parseColor("#673AB7"))}
        }
    }

    private fun switchV1(helper: CustomDBHelper, switch: Switch, option: String){
        switch?.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                val data = CustomData(option,"on")
                helper.updateCustomizing(data)
            } else {
                val data = CustomData(option,"off")
                helper.updateCustomizing(data)
            }
        }
    }

    private fun spinnerV1(helper: CustomDBHelper, spinner:Spinner, option:String){
        val spinnerAdapter = context?.let {
            ArrayAdapter(
                it.applicationContext, android.R.layout.simple_spinner_item, selectItems
            )
        }
        spinnerAdapter?.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner?.adapter = spinnerAdapter
        spinner?.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position){
                    0->{helper.updateCustomizing(CustomData(option,"GRAY"))
                        spinner.setBackgroundColor(Color.GRAY)}
                    1->{helper.updateCustomizing(CustomData(option,"WHITE"))
                        spinner.setBackgroundColor(Color.WHITE)}
                    2->{helper.updateCustomizing(CustomData(option,"RED"))
                        spinner.setBackgroundColor(Color.RED)}
                    3->{helper.updateCustomizing(CustomData(option,"MAGENTA"))
                        spinner.setBackgroundColor(Color.MAGENTA)}
                    4->{helper.updateCustomizing(CustomData(option,"YELLOW"))
                        spinner.setBackgroundColor(Color.YELLOW)}
                    5->{helper.updateCustomizing(CustomData(option,"GREEN"))
                        spinner.setBackgroundColor(Color.GREEN)}
                    6->{helper.updateCustomizing(CustomData(option,"BLUE"))
                        spinner.setBackgroundColor(Color.BLUE)}
                    7->{helper.updateCustomizing(CustomData(option,"CYAN"))
                        spinner.setBackgroundColor(Color.CYAN)}
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun spinnerV2(helper: CustomDBHelper, spinner:Spinner, option:String){
        val spinnerAdapter = context?.let {
            ArrayAdapter(
                it.applicationContext, android.R.layout.simple_spinner_item, backgroundItems
            )
        }
        spinnerAdapter?.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner?.adapter = spinnerAdapter
        spinner?.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position){
                    0->{helper.updateCustomizing(CustomData(option,"#FFFFFF"))
                        spinner.setBackgroundColor(Color.parseColor("#FFFFFF"))}
                    1->{helper.updateCustomizing(CustomData(option,"#FBE4E4"))
                        spinner.setBackgroundColor(Color.parseColor("#FBE4E4"))}
                    2->{helper.updateCustomizing(CustomData(option,"#DDF0F3"))
                        spinner.setBackgroundColor(Color.parseColor("#DDF0F3"))}
                    3->{helper.updateCustomizing(CustomData(option,"#D1F3D2"))
                        spinner.setBackgroundColor(Color.parseColor("#D1F3D2"))}
                    4->{helper.updateCustomizing(CustomData(option,"#F8F5DA"))
                        spinner.setBackgroundColor(Color.parseColor("#F8F5DA"))}
                    5->{helper.updateCustomizing(CustomData(option,"#E7DDFA"))
                        spinner.setBackgroundColor(Color.parseColor("#E7DDFA"))}
                    6->{helper.updateCustomizing(CustomData(option,"#F44336"))
                        spinner.setBackgroundColor(Color.parseColor("#F44336"))}
                    7->{helper.updateCustomizing(CustomData(option,"#FF9800"))
                        spinner.setBackgroundColor(Color.parseColor("#FF9800"))}
                    8->{helper.updateCustomizing(CustomData(option,"#FFEB3B"))
                        spinner.setBackgroundColor(Color.parseColor("#FFEB3B"))}
                    9->{helper.updateCustomizing(CustomData(option,"#673AB7"))
                        spinner.setBackgroundColor(Color.parseColor("#673AB7"))}
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun alertDlg(helper: CustomDBHelper){
        val builder = AlertDialog.Builder(context)
        builder.setMessage("초기화를 진행하시겠습니까?")
                .setTitle("설정 초기화")
                .setNegativeButton("No"){
                    _,_->
                }
                .setPositiveButton("Yes"){
                    _,_->
                    buttonR(helper)
                    Toast.makeText(context, "Customizing Reset", Toast.LENGTH_SHORT).show()
                }
        val dlg = builder.create()
        dlg.show()
    }

    private fun buttonR(helper: CustomDBHelper){
        helper.updateCustomizing(CustomData("saturday", "off"))
        helper.updateCustomizing(CustomData("sunday", "off"))
        helper.updateCustomizing(CustomData("today", "off"))
        helper.updateCustomizing(CustomData("date", "on"))
        helper.updateCustomizing(CustomData("format", "off"))
        helper.updateCustomizing(CustomData("color","#FFFFFF"))
        helper.updateCustomizing(CustomData("background","#FFFFFF"))
        helper.updateCustomizing(CustomData("memo","#FFFFFF"))
        helper.updateCustomizing(CustomData("record","#FFFFFF"))
        helper.updateCustomizing(CustomData("select","GRAY"))
    }
}