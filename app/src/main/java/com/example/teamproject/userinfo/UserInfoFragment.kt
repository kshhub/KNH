package com.example.teamproject.userinfo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.teamproject.R

class UserInfoFragment : Fragment() {

    lateinit var userInfoDBHelper: UserInfoDBHelper
    lateinit var genderFlag: String
    lateinit var goalFlag: String

    private val goalItems = arrayOf(
        "체중 감소", "체중 유지", "체중 증가"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userInfoDBHelper = UserInfoDBHelper(context)

        val textViewAge = view?.findViewById<TextView>(R.id.textViewUserInfoAge)
        val textViewHeight = view?.findViewById<TextView>(R.id.textViewUserInfoHeight)
        val textViewWeight = view?.findViewById<TextView>(R.id.textViewUserInfoWeight)
        val textViewGender = view?.findViewById<TextView>(R.id.textViewUserInfoGender)
        val textViewGoal = view?.findViewById<TextView>(R.id.textViewUserInfoGoal)
        val radioGroup = view?.findViewById<RadioGroup>(R.id.radioGroupUserInfo)
        val radioMan = view?.findViewById<RadioButton>(R.id.radioButtonUserInfoMan)
        val radioWoman = view?.findViewById<RadioButton>(R.id.radioButtonUserInfoWoman)
        val spinnerGoal = view?.findViewById<Spinner>(R.id.spinnerUserInfoGoal)
        val editTextAge = view?.findViewById<EditText>(R.id.editTextUserInfoAge)
        val editTextHeight = view?.findViewById<EditText>(R.id.editTextUserInfoHeight)
        val editTextWeight = view?.findViewById<EditText>(R.id.editTextUserInfoWeight)
        val buttonSave = view?.findViewById<Button>(R.id.buttonUserInfoSave)

        textViewAge?.text = userInfoDBHelper.findUserInfo("age")
        textViewGender?.text = userInfoDBHelper.findUserInfo("gender")
        textViewHeight?.text = userInfoDBHelper.findUserInfo("height")
        textViewWeight?.text = userInfoDBHelper.findUserInfo("weight")
        textViewGoal?.text = userInfoDBHelper.findUserInfo("goal")

        genderFlag = userInfoDBHelper.findUserInfo("gender")
        goalFlag = userInfoDBHelper.findUserInfo("goal")

        if (editTextAge != null && radioMan != null && radioWoman != null && editTextHeight != null //init
            && editTextWeight != null && spinnerGoal != null
        ) {
            initEditSetting(
                userInfoDBHelper, editTextAge, radioMan, radioWoman, editTextHeight,
                editTextWeight, spinnerGoal
            )
        }

        if (radioGroup != null) radioInput(radioGroup)
        if (spinnerGoal != null) spinnerInput(spinnerGoal)
        if (editTextAge != null && editTextHeight != null && editTextWeight != null && buttonSave != null) {
            buttonSave(userInfoDBHelper, buttonSave, editTextAge, editTextHeight, editTextWeight)
        }
    }

    override fun onResume() {
        super.onResume()

        userInfoDBHelper = UserInfoDBHelper(context)

        val textViewAge = view?.findViewById<TextView>(R.id.textViewUserInfoAge)
        val textViewHeight = view?.findViewById<TextView>(R.id.textViewUserInfoHeight)
        val textViewWeight = view?.findViewById<TextView>(R.id.textViewUserInfoWeight)
        val textViewGender = view?.findViewById<TextView>(R.id.textViewUserInfoGender)
        val textViewGoal = view?.findViewById<TextView>(R.id.textViewUserInfoGoal)
        val radioman = view?.findViewById<RadioButton>(R.id.radioButtonUserInfoMan)
        val radiowoman = view?.findViewById<RadioButton>(R.id.radioButtonUserInfoWoman)
        val spinnerGoal = view?.findViewById<Spinner>(R.id.spinnerUserInfoGoal)
        val editTextAge = view?.findViewById<EditText>(R.id.editTextUserInfoAge)
        val editTextHeight = view?.findViewById<EditText>(R.id.editTextUserInfoHeight)
        val editTextWeight = view?.findViewById<EditText>(R.id.editTextUserInfoWeight)

        textViewAge?.text = userInfoDBHelper.findUserInfo("age")
        textViewGender?.text = userInfoDBHelper.findUserInfo("gender")
        textViewHeight?.text = userInfoDBHelper.findUserInfo("height")
        textViewWeight?.text = userInfoDBHelper.findUserInfo("weight")
        textViewGoal?.text = userInfoDBHelper.findUserInfo("goal")

        genderFlag = userInfoDBHelper.findUserInfo("gender")
        goalFlag = userInfoDBHelper.findUserInfo("goal")

        if (editTextAge != null && radioman != null && radiowoman != null && editTextHeight != null //init
            && editTextWeight != null && spinnerGoal != null
        ) {
            initEditSetting(
                userInfoDBHelper, editTextAge, radioman, radiowoman, editTextHeight,
                editTextWeight, spinnerGoal
            )
        }
    }

    private fun initEditSetting(
        helper: UserInfoDBHelper, age: EditText, man: RadioButton, woman: RadioButton,
        height: EditText, weight: EditText, goal: Spinner
    ) {
        if (helper.findUserInfo("age") != "default") age.setText(helper.findUserInfo("age"))
        if (helper.findUserInfo("gender") != "default") {
            if (helper.findUserInfo("gender") == "man") man.isChecked = true
            else woman.isChecked = true
        }
        if (helper.findUserInfo("height") != "default") height.setText(helper.findUserInfo("height"))
        if (helper.findUserInfo("weight") != "default") weight.setText(helper.findUserInfo("weight"))
        if (helper.findUserInfo("goal") != "default") {
            when {
                helper.findUserInfo("goal") == "체중 감소" -> {
                    goal.setSelection(0)
                }
                helper.findUserInfo("goal") == "체중 유지" -> {
                    goal.setSelection(1)
                }
                else -> {
                    goal.setSelection(2)
                }
            }
        }
    }

    private fun radioInput(genderGroup: RadioGroup) {
        genderGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonUserInfoMan -> genderFlag = "남"
                R.id.radioButtonUserInfoWoman -> genderFlag = "여"
            }
        }
    }

    private fun spinnerInput(spinner: Spinner) {
        val spinnerAdapter = context?.let {
            ArrayAdapter(
                it.applicationContext, android.R.layout.simple_spinner_item, goalItems
            )
        }
        spinnerAdapter?.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener { // spinner 입력
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> goalFlag = "체중 감소"
                        1 -> goalFlag = "체중 유지"
                        2 -> goalFlag = "체중 증가"
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }

    private fun buttonSave(
        helper: UserInfoDBHelper, button: Button,
        editage: EditText, editheight: EditText, editweight: EditText
    ) {
        button.setOnClickListener {
            val age = editage.text.toString()
            val height = editheight.text.toString()
            val weight = editweight.text.toString()

            helper.updateUserInfo((UserInfoData("age", age)))
            if (genderFlag == "남") {
                helper.updateUserInfo(UserInfoData("gender", "man"))
            } else {
                helper.updateUserInfo(UserInfoData("gender", "woman"))
            }
            helper.updateUserInfo(UserInfoData("height", height))
            helper.updateUserInfo(UserInfoData("weight", weight))
            when (goalFlag) {
                "체중 감소" -> {
                    helper.updateUserInfo(UserInfoData("goal", "체중 감소"))
                }
                "체중 유지" -> {
                    helper.updateUserInfo(UserInfoData("goal", "체중 유지"))
                }
                else -> {
                    helper.updateUserInfo(UserInfoData("goal", "체중 증가"))
                }
            }
            Toast.makeText(context, "저장 완료", Toast.LENGTH_SHORT).show()
        }
    }

}