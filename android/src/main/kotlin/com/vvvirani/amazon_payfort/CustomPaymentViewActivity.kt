package com.vvvirani.amazon_payfort

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.payfort.fortpaymentsdk.callbacks.PayFortCallback
import com.payfort.fortpaymentsdk.domain.model.FortRequest
import com.payfort.fortpaymentsdk.views.CardCvvView
import com.payfort.fortpaymentsdk.views.CardExpiryView
import com.payfort.fortpaymentsdk.views.CardHolderNameView
import com.payfort.fortpaymentsdk.views.FortCardNumberView
import com.payfort.fortpaymentsdk.views.PayfortPayButton
import com.payfort.fortpaymentsdk.views.model.PayComponents
import io.flutter.plugin.common.MethodChannel

class CustomPaymentViewActivity : AppCompatActivity() {

    private lateinit var cardNumberView: FortCardNumberView
    private lateinit var cardHolderNameView: CardHolderNameView
    private lateinit var cardExpiryView: CardExpiryView
    private lateinit var cardCvvView: CardCvvView
    private lateinit var btnPay: PayfortPayButton
    private lateinit var switchSaveCardData: CupertinoSwitch
    private lateinit var switchSetCardDefault: CupertinoSwitch

    private var tag = this.javaClass.simpleName

    companion object {
        var channel: MethodChannel? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_payment_view)

        // Initialize views
        val tvHeader = findViewById<TextView>(R.id.tvHeader)
        val tvHeaderLabel = findViewById<TextView>(R.id.tvHeaderLabel)
        val ivHeaderIcon = findViewById<ImageView>(R.id.ivHeaderIcon)
        val ivIcon1 = findViewById<ImageView>(R.id.ivIcon1)
        val ivIcon2 = findViewById<ImageView>(R.id.ivIcon2)
        val tvSaveCardDataLabel = findViewById<TextView>(R.id.tvSaveCardDataLabel)
        val tvSetCardDefaultLabel = findViewById<TextView>(R.id.tvSetCardDefaultLabel)
        cardNumberView = findViewById(R.id.etCardNumberView)
        cardHolderNameView = findViewById(R.id.cardHolderNameView)
        cardExpiryView = findViewById(R.id.etCardExpiry)
        cardCvvView = findViewById(R.id.etCardCvv)
        btnPay = findViewById(R.id.btntPay)
        switchSaveCardData = findViewById(R.id.switchSaveCardData)
        switchSetCardDefault = findViewById(R.id.switchSetCardDefault)

        // Get parameters from the intent
        val headerText = intent.getStringExtra("headerText")
        val headerIconUrl = intent.getStringExtra("headerIconUrl")
        val secondaryHeaderText = intent.getStringExtra("secondaryHeaderText")
        val iconUrls = intent.getStringArrayListExtra("iconUrls")
        val headerTextColor = intent.getIntExtra("headerTextColor", Color.BLACK)
        val secondaryHeaderTextColor = intent.getIntExtra("secondaryHeaderTextColor", Color.BLACK)
        val toggleButtonActiveColor = intent.getIntExtra("toggleButtonActiveColor", Color.GREEN)
        val toggleButtonInactiveColor = intent.getIntExtra("toggleButtonInactiveColor", Color.GRAY)
        val cardNumberLabel = intent.getStringExtra("cardNumberLabel")
        val cardHolderLabel = intent.getStringExtra("cardHolderLabel")
        val expiryDateLabel = intent.getStringExtra("expiryDateLabel")
        val cvvLabel = intent.getStringExtra("cvvLabel")
        val textFieldHintColor = intent.getIntExtra("textFieldHintColor", Color.GRAY)
        val saveCardDataLabel = intent.getStringExtra("saveCardDataLabel")
        val setCardDefaultLabel = intent.getStringExtra("setCardDefaultLabel")
        val addCardButtonLabel = intent.getStringExtra("addCardButtonLabel")
        val addCardButtonBackgroundColor = intent.getIntExtra("addCardButtonBackgroundColor", Color.BLUE)
        val environment = intent.getStringExtra("environment")
        val fortRequest = intent.getSerializableExtra("fortRequest") as FortRequest

        // Set the received parameters to the views
        tvHeader.text = headerText
        tvHeader.setTextColor(headerTextColor)
        tvHeaderLabel.text = secondaryHeaderText
        tvHeaderLabel.setTextColor(secondaryHeaderTextColor)
        Glide.with(this).load(headerIconUrl).into(ivHeaderIcon)
        Glide.with(this).load(iconUrls?.get(0)).into(ivIcon1)
        Glide.with(this).load(iconUrls?.get(1)).into(ivIcon2)
        switchSaveCardData.isActivated = true
        switchSetCardDefault.isActivated = false
        cardNumberView.hint = cardNumberLabel
        cardHolderNameView.hint = cardHolderLabel
        cardExpiryView.hint = expiryDateLabel
        cardCvvView.hint = cvvLabel
//        cardNumberView.hintTextColor = textFieldHintColor
//        cardHolderNameView.hintTextColor = textFieldHintColor
//        cardExpiryView.hintTextColor = textFieldHintColor
//        cardCvvView.hintTextColor = textFieldHintColor
        tvSaveCardDataLabel.text = saveCardDataLabel
        tvSetCardDefaultLabel.text = setCardDefaultLabel
        btnPay.text = addCardButtonLabel

        switchSaveCardData.setActiveTrackColor(toggleButtonActiveColor)
        switchSaveCardData.setInactiveTrackColor(toggleButtonInactiveColor)
        switchSaveCardData.setSwitchOn(true)
        switchSetCardDefault.setActiveTrackColor(toggleButtonActiveColor)
        switchSetCardDefault.setInactiveTrackColor(toggleButtonInactiveColor)

        val callback = object : PayFortCallback {
            override fun startLoading() {
                btnPay.isEnabled = false
            }

            override fun onSuccess(
                requestParamsMap: Map<String, Any>,
                fortResponseMap: Map<String, Any>
            ) {
                Log.d(tag, "onSuccess : $requestParamsMap $fortResponseMap")
                channel?.invokeMethod("succeeded", fortResponseMap)
                finish()
                return
            }

            override fun onFailure(
                requestParamsMap: Map<String, Any>,
                fortResponseMap: Map<String, Any>
            ) {
                Log.d(tag, "onFailure : $requestParamsMap $fortResponseMap")
                val result: MutableMap<String, Any?> = HashMap()
                result["message"] = fortResponseMap["response_message"]
                channel?.invokeMethod("failed", result)
                finish()
                return
            }
        }
        val payComponents = PayComponents(
            cardNumberView,
            cvvView = cardCvvView,
            cardExpiryView,
            holderNameView = cardHolderNameView,
        )

        btnPay.setup(PayFortService.getEnvironment(environment), fortRequest, payComponents, callback)
    }
}
