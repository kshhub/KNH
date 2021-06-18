package com.example.teamproject.custom

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.teamproject.R

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
        "GRAY", "WHITE", "RED", "MAGENTA", "YELLOW", "GREEN", "BLUE", "CYAN"
    )
    private val backgroundItems = arrayOf(
        "목련", "벚꽃", "물망초", "그린 카네이션",
        "유채", "코스모스", "금잔화",
        "민들레", "도라지"
    )

    var checkV1:String = ""
    var checkV2_Color:String = ""
    var checkV2_Background:String = ""
    var checkV2_Memo:String = ""
    var checkV2_Record:String = ""
    var switch_saturday:String = ""
    var switch_sunday:String = ""
    var switch_today:String = ""
    var switch_date:String = ""
    var switch_format:String = ""

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
        val buttonSave = view?.findViewById<Button>(R.id.buttonCustomSave)

        if (spinnerSelect != null) spinnerV1(spinnerSelect) //Select Customizing
        if (spinnerColor != null) spinnerV2_color(spinnerColor) //Color Customizing
        if (spinnerBackground != null) spinnerV2_background(spinnerBackground) //Background Customizing
        if (spinnerMemo != null) spinnerV2_memo(spinnerMemo) //Memo Customizing
        if (spinnerRecord != null) spinnerV2_record(spinnerRecord) //Record Customizing
        if (switchSaturday != null) switchV1_saturday(switchSaturday) //Saturday Customizing
        if (switchSunday != null) switchV1_sunday(switchSunday) //Sunday Customizing
        if (switchToday != null) switchV1_today(switchToday) //Today Customizing
        if (switchDate != null) switchV1_date(switchDate) //Date Customizing
        if (switchFormat != null) switchV1_format(switchFormat) //Format Customizing

        if (switchSaturday != null && switchSunday != null && switchToday != null && //init
            switchDate != null && spinnerColor != null && spinnerSelect != null && switchFormat != null &&
            spinnerBackground != null && spinnerMemo != null && spinnerRecord != null
        ) {
            initSetting(
                customDBHelper, switchSaturday, switchSunday, switchToday, switchDate, spinnerColor,
                spinnerSelect, switchFormat, spinnerBackground, spinnerMemo, spinnerRecord
            )
        }

        buttonReset?.setOnClickListener {  //Customizing Reset
            alertDlg(customDBHelper)
        }
        buttonSave?.setOnClickListener {
            save(customDBHelper)
        }
    }

    private fun initSetting(
        helper: CustomDBHelper, saturday: Switch, sunday: Switch, today: Switch, date: Switch,
        color: Spinner, select: Spinner, format: Switch, background: Spinner,
        memo: Spinner, record: Spinner
    ) {
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

    private fun initSwitch(helper: CustomDBHelper, switch: Switch, option: String) {
        if (helper.findCustomizing(option) == "on"){
            switch.isChecked = true
        }else{
            switch.isChecked = false
        }
    }

    private fun initSpinnerV1(helper: CustomDBHelper, spinner: Spinner, option: String) {
        when (helper.findCustomizing(option)) {
            "GRAY" -> {
                spinner.setSelection(0)
                spinner.setBackgroundColor(Color.GRAY)
            }
            "WHITE" -> {
                spinner.setSelection(1)
                spinner.setBackgroundColor(Color.WHITE)
            }
            "RED" -> {
                spinner.setSelection(2)
                spinner.setBackgroundColor(Color.parseColor("#F68D83"))
            }
            "MAGENTA" -> {
                spinner.setSelection(3)
                spinner.setBackgroundColor(Color.MAGENTA)
            }
            "YELLOW" -> {
                spinner.setSelection(4)
                spinner.setBackgroundColor(Color.YELLOW)
            }
            "GREEN" -> {
                spinner.setSelection(5)
                spinner.setBackgroundColor(Color.GREEN)
            }
            "BLUE" -> {
                spinner.setSelection(6)
                spinner.setBackgroundColor(Color.parseColor("#00BCD4"))
            }
            "CYAN" -> {
                spinner.setSelection(7)
                spinner.setBackgroundColor(Color.CYAN)
            }
        }
    }

    private fun initSpinnerV2(helper: CustomDBHelper, spinner: Spinner, option: String) {
        when (helper.findCustomizing(option)) {
            "#FFFFFF" -> {
                spinner.setSelection(0)
                spinner.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
            "#FBE4E4" -> {
                spinner.setSelection(1)
                spinner.setBackgroundColor(Color.parseColor("#FBE4E4"))
            }
            "#E3F6F8" -> {
                spinner.setSelection(2)
                spinner.setBackgroundColor(Color.parseColor("#E3F6F8"))
            }
            "#D1F3D2" -> {
                spinner.setSelection(3)
                spinner.setBackgroundColor(Color.parseColor("#D1F3D2"))
            }
            "#F8F5DA" -> {
                spinner.setSelection(4)
                spinner.setBackgroundColor(Color.parseColor("#F8F5DA"))
            }
            "#E7DDFA" -> {
                spinner.setSelection(5)
                spinner.setBackgroundColor(Color.parseColor("#E7DDFA"))
            }
            "#FF9800" -> {
                spinner.setSelection(6)
                spinner.setBackgroundColor(Color.parseColor("#FF9800"))
            }
            "#FFEB3B" -> {
                spinner.setSelection(7)
                spinner.setBackgroundColor(Color.parseColor("#FFEB3B"))
            }
            "#673AB7" -> {
                spinner.setSelection(8)
                spinner.setBackgroundColor(Color.parseColor("#673AB7"))
            }
        }
    }

    private fun switchV1_saturday(switch: Switch) {
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switch_saturday = "on"
            } else {
                switch_saturday = "off"
            }
        }
    }

    private fun switchV1_sunday(switch: Switch) {
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switch_sunday = "on"
            } else {
                switch_sunday = "off"
            }
        }
    }

    private fun switchV1_today(switch: Switch) {
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switch_today = "on"
            } else {
                switch_today = "off"
            }
        }
    }

    private fun switchV1_date(switch: Switch) {
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switch_date = "on"
            } else {
                switch_date = "off"
            }
        }
    }

    private fun switchV1_format(switch: Switch) {
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switch_format = "on"
            } else {
                switch_format = "off"
            }
        }
    }

    private fun spinnerV1(spinner: Spinner) {
        val spinnerAdapter = context?.let {
            ArrayAdapter(
                it.applicationContext, android.R.layout.simple_spinner_item, selectItems
            )
        }
        spinnerAdapter?.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        checkV1="0"
                        spinner.setBackgroundColor(Color.GRAY)
                    }
                    1 -> {
                        checkV1="1"
                        spinner.setBackgroundColor(Color.WHITE)
                    }
                    2 -> {
                        checkV1="2"
                        spinner.setBackgroundColor(Color.parseColor("#F68D83"))
                    }
                    3 -> {
                        checkV1="3"
                        spinner.setBackgroundColor(Color.MAGENTA)
                    }
                    4 -> {
                        checkV1="4"
                        spinner.setBackgroundColor(Color.YELLOW)
                    }
                    5 -> {
                        checkV1="5"
                        spinner.setBackgroundColor(Color.GREEN)
                    }
                    6 -> {
                        checkV1="6"
                        spinner.setBackgroundColor(Color.parseColor("#00BCD4"))
                    }
                    7 -> {
                        checkV1="7"
                        spinner.setBackgroundColor(Color.CYAN)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun spinnerV2_color(spinner: Spinner) {

        val spinnerAdapter = context?.let {
            ArrayAdapter(
                it.applicationContext, android.R.layout.simple_spinner_item, backgroundItems
            )
        }
        spinnerAdapter?.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        checkV2_Color="0"
                        spinner.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    }
                    1 -> {
                        checkV2_Color="1"
                        spinner.setBackgroundColor(Color.parseColor("#FBE4E4"))
                    }
                    2 -> {
                        checkV2_Color="2"
                        spinner.setBackgroundColor(Color.parseColor("#E3F6F8"))
                    }
                    3 -> {
                        checkV2_Color="3"
                        spinner.setBackgroundColor(Color.parseColor("#D1F3D2"))
                    }
                    4 -> {
                        checkV2_Color="4"
                        spinner.setBackgroundColor(Color.parseColor("#F8F5DA"))
                    }
                    5 -> {
                        checkV2_Color="5"
                        spinner.setBackgroundColor(Color.parseColor("#E7DDFA"))
                    }
                    6 -> {
                        checkV2_Color="6"
                        spinner.setBackgroundColor(Color.parseColor("#FF9800"))
                    }
                    7 -> {
                        checkV2_Color="7"
                        spinner.setBackgroundColor(Color.parseColor("#FFEB3B"))
                    }
                    8 -> {
                        checkV2_Color="8"
                        spinner.setBackgroundColor(Color.parseColor("#673AB7"))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun spinnerV2_background(spinner: Spinner) {

        val spinnerAdapter = context?.let {
            ArrayAdapter(
                it.applicationContext, android.R.layout.simple_spinner_item, backgroundItems
            )
        }
        spinnerAdapter?.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        checkV2_Background="0"
                        spinner.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    }
                    1 -> {
                        checkV2_Background="1"
                        spinner.setBackgroundColor(Color.parseColor("#FBE4E4"))
                    }
                    2 -> {
                        checkV2_Background="2"
                        spinner.setBackgroundColor(Color.parseColor("#E3F6F8"))
                    }
                    3 -> {
                        checkV2_Background="3"
                        spinner.setBackgroundColor(Color.parseColor("#D1F3D2"))
                    }
                    4 -> {
                        checkV2_Background="4"
                        spinner.setBackgroundColor(Color.parseColor("#F8F5DA"))
                    }
                    5 -> {
                        checkV2_Background="5"
                        spinner.setBackgroundColor(Color.parseColor("#E7DDFA"))
                    }
                    6 -> {
                        checkV2_Background="6"
                        spinner.setBackgroundColor(Color.parseColor("#FF9800"))
                    }
                    7 -> {
                        checkV2_Background="7"
                        spinner.setBackgroundColor(Color.parseColor("#FFEB3B"))
                    }
                    8 -> {
                        checkV2_Background="8"
                        spinner.setBackgroundColor(Color.parseColor("#673AB7"))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun spinnerV2_memo(spinner: Spinner) {

        val spinnerAdapter = context?.let {
            ArrayAdapter(
                it.applicationContext, android.R.layout.simple_spinner_item, backgroundItems
            )
        }
        spinnerAdapter?.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        checkV2_Memo="0"
                        spinner.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    }
                    1 -> {
                        checkV2_Memo="1"
                        spinner.setBackgroundColor(Color.parseColor("#FBE4E4"))
                    }
                    2 -> {
                        checkV2_Memo="2"
                        spinner.setBackgroundColor(Color.parseColor("#E3F6F8"))
                    }
                    3 -> {
                        checkV2_Memo="3"
                        spinner.setBackgroundColor(Color.parseColor("#D1F3D2"))
                    }
                    4 -> {
                        checkV2_Memo="4"
                        spinner.setBackgroundColor(Color.parseColor("#F8F5DA"))
                    }
                    5 -> {
                        checkV2_Memo="5"
                        spinner.setBackgroundColor(Color.parseColor("#E7DDFA"))
                    }
                    6 -> {
                        checkV2_Memo="6"
                        spinner.setBackgroundColor(Color.parseColor("#FF9800"))
                    }
                    7 -> {
                        checkV2_Memo="7"
                        spinner.setBackgroundColor(Color.parseColor("#FFEB3B"))
                    }
                    8 -> {
                        checkV2_Memo="8"
                        spinner.setBackgroundColor(Color.parseColor("#673AB7"))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun spinnerV2_record(spinner: Spinner) {

        val spinnerAdapter = context?.let {
            ArrayAdapter(
                it.applicationContext, android.R.layout.simple_spinner_item, backgroundItems
            )
        }
        spinnerAdapter?.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        checkV2_Record="0"
                        spinner.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    }
                    1 -> {
                        checkV2_Record="1"
                        spinner.setBackgroundColor(Color.parseColor("#FBE4E4"))
                    }
                    2 -> {
                        checkV2_Record="2"
                        spinner.setBackgroundColor(Color.parseColor("#E3F6F8"))
                    }
                    3 -> {
                        checkV2_Record="3"
                        spinner.setBackgroundColor(Color.parseColor("#D1F3D2"))
                    }
                    4 -> {
                        checkV2_Record="4"
                        spinner.setBackgroundColor(Color.parseColor("#F8F5DA"))
                    }
                    5 -> {
                        checkV2_Record="5"
                        spinner.setBackgroundColor(Color.parseColor("#E7DDFA"))
                    }
                    6 -> {
                        checkV2_Record="6"
                        spinner.setBackgroundColor(Color.parseColor("#FF9800"))
                    }
                    7 -> {
                        checkV2_Record="7"
                        spinner.setBackgroundColor(Color.parseColor("#FFEB3B"))
                    }
                    8 -> {
                        checkV2_Record="8"
                        spinner.setBackgroundColor(Color.parseColor("#673AB7"))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun alertDlg(helper: CustomDBHelper) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("초기화를 진행하시겠습니까?")
            .setTitle("설정 초기화")
            .setNegativeButton("No") { _, _ ->
            }
            .setPositiveButton("Yes") { _, _ ->
                buttonR(helper)
                refreshing()
                Toast.makeText(context, "Customizing Reset", Toast.LENGTH_SHORT).show()
            }
        val dlg = builder.create()
        dlg.show()
    }

    private fun buttonR(helper: CustomDBHelper) {
        helper.updateCustomizing(CustomData("saturday", "off"))
        helper.updateCustomizing(CustomData("sunday", "off"))
        helper.updateCustomizing(CustomData("today", "off"))
        helper.updateCustomizing(CustomData("date", "on"))
        helper.updateCustomizing(CustomData("format", "off"))
        helper.updateCustomizing(CustomData("color", "#FFFFFF"))
        helper.updateCustomizing(CustomData("background", "#FFFFFF"))
        helper.updateCustomizing(CustomData("memo", "#FFFFFF"))
        helper.updateCustomizing(CustomData("record", "#FFFFFF"))
        helper.updateCustomizing(CustomData("select", "GRAY"))
    }

    private fun refreshing(){
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
            switchDate != null && spinnerColor != null && spinnerSelect != null && switchFormat != null &&
            spinnerBackground != null && spinnerMemo != null && spinnerRecord != null
        ) {
            initSetting(
                customDBHelper, switchSaturday, switchSunday, switchToday, switchDate, spinnerColor,
                spinnerSelect, switchFormat, spinnerBackground, spinnerMemo, spinnerRecord
            )
        }
    }

    private fun save(helper: CustomDBHelper){
        Toast.makeText(context, "Customizing Save", Toast.LENGTH_SHORT).show()
        when(switch_saturday){
            "on"->{
                helper.updateCustomizing(CustomData("saturday","on"))
            }
            "off"->{
                helper.updateCustomizing(CustomData("saturday","off"))
            }
        }
        when(switch_sunday){
            "on"->{
                helper.updateCustomizing(CustomData("sunday","on"))
            }
            "off"->{
                helper.updateCustomizing(CustomData("sunday","off"))
            }
        }
        when(switch_today){
            "on"->{
                helper.updateCustomizing(CustomData("today","on"))
            }
            "off"->{
                helper.updateCustomizing(CustomData("today","off"))
            }
        }
        when(switch_date){
            "on"->{
                helper.updateCustomizing(CustomData("date","on"))
            }
            "off"->{
                helper.updateCustomizing(CustomData("date","off"))
            }
        }
        when(switch_format){
            "on"->{
                helper.updateCustomizing(CustomData("format","on"))
            }
            "off"->{
                helper.updateCustomizing(CustomData("format","off"))
            }
        }
        when(checkV1){
            "0" -> {
                helper.updateCustomizing(CustomData("select", "GRAY"))
            }
            "1" -> {
                helper.updateCustomizing(CustomData("select", "WHITE"))
            }
            "2" -> {
                helper.updateCustomizing(CustomData("select", "RED"))
            }
            "3" -> {
                helper.updateCustomizing(CustomData("select", "MAGENTA"))
            }
            "4" -> {
                helper.updateCustomizing(CustomData("select", "YELLOW"))
            }
            "5" -> {
                helper.updateCustomizing(CustomData("select", "GREEN"))
            }
            "6" -> {
                helper.updateCustomizing(CustomData("select", "BLUE"))
            }
            "7" -> {
                helper.updateCustomizing(CustomData("select", "CYAN"))
            }
        }
        when (checkV2_Color) {
            "0" -> {
                helper.updateCustomizing(CustomData("color", "#FFFFFF"))
            }
            "1" -> {
                helper.updateCustomizing(CustomData("color", "#FBE4E4"))
            }
            "2" -> {
                helper.updateCustomizing(CustomData("color", "#E3F6F8"))
            }
            "3" -> {
                helper.updateCustomizing(CustomData("color", "#D1F3D2"))
            }
            "4" -> {
                helper.updateCustomizing(CustomData("color", "#F8F5DA"))
            }
            "5" -> {
                helper.updateCustomizing(CustomData("color", "#E7DDFA"))
            }
            "6" -> {
                helper.updateCustomizing(CustomData("color", "#FF9800"))
            }
            "7" -> {
                helper.updateCustomizing(CustomData("color", "#FFEB3B"))
            }
            "8" -> {
                helper.updateCustomizing(CustomData("color", "#673AB7"))
            }
        }
        when (checkV2_Background) {
            "0" -> {
                helper.updateCustomizing(CustomData("background", "#FFFFFF"))
            }
            "1" -> {
                helper.updateCustomizing(CustomData("background", "#FBE4E4"))
            }
            "2" -> {
                helper.updateCustomizing(CustomData("background", "#E3F6F8"))
            }
            "3" -> {
                helper.updateCustomizing(CustomData("background", "#D1F3D2"))
            }
            "4" -> {
                helper.updateCustomizing(CustomData("background", "#F8F5DA"))
            }
            "5" -> {
                helper.updateCustomizing(CustomData("background", "#E7DDFA"))
            }
            "6" -> {
                helper.updateCustomizing(CustomData("background", "#FF9800"))
            }
            "7" -> {
                helper.updateCustomizing(CustomData("background", "#FFEB3B"))
            }
            "8" -> {
                helper.updateCustomizing(CustomData("background", "#673AB7"))
            }
        }
        when (checkV2_Memo) {
            "0" -> {
                helper.updateCustomizing(CustomData("memo", "#FFFFFF"))
            }
            "1" -> {
                helper.updateCustomizing(CustomData("memo", "#FBE4E4"))
            }
            "2" -> {
                helper.updateCustomizing(CustomData("memo", "#E3F6F8"))
            }
            "3" -> {
                helper.updateCustomizing(CustomData("memo", "#D1F3D2"))
            }
            "4" -> {
                helper.updateCustomizing(CustomData("memo", "#F8F5DA"))
            }
            "5" -> {
                helper.updateCustomizing(CustomData("memo", "#E7DDFA"))
            }
            "6" -> {
                helper.updateCustomizing(CustomData("memo", "#FF9800"))
            }
            "7" -> {
                helper.updateCustomizing(CustomData("memo", "#FFEB3B"))
            }
            "8" -> {
                helper.updateCustomizing(CustomData("memo", "#673AB7"))
            }
        }
        when (checkV2_Record) {
            "0" -> {
                helper.updateCustomizing(CustomData("record", "#FFFFFF"))
            }
            "1" -> {
                helper.updateCustomizing(CustomData("record", "#FBE4E4"))
            }
            "2" -> {
                helper.updateCustomizing(CustomData("record", "#E3F6F8"))
            }
            "3" -> {
                helper.updateCustomizing(CustomData("record", "#D1F3D2"))
            }
            "4" -> {
                helper.updateCustomizing(CustomData("record", "#F8F5DA"))
            }
            "5" -> {
                helper.updateCustomizing(CustomData("record", "#E7DDFA"))
            }
            "6" -> {
                helper.updateCustomizing(CustomData("record", "#FF9800"))
            }
            "7" -> {
                helper.updateCustomizing(CustomData("record", "#FFEB3B"))
            }
            "8" -> {
                helper.updateCustomizing(CustomData("record", "#673AB7"))
            }
        }
    }
}