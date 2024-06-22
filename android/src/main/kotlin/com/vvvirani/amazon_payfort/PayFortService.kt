package com.vvvirani.amazon_payfort

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.payfort.fortpaymentsdk.FortSdk
import com.payfort.fortpaymentsdk.callbacks.FortCallBackManager
import com.payfort.fortpaymentsdk.domain.model.FortRequest
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class PayFortService {

    val payfortRequestCode = 1166

    private var options: PayFortOptions? = null

    private var channel: MethodChannel? = null

    private var fortCallback: FortCallBackManager? = null

    companion object {
        fun getEnvironment(environment: String?): String {
            return when (environment) {
                "test" -> {
                    return FortSdk.ENVIRONMENT.TEST
                }
                "production" -> {
                    return FortSdk.ENVIRONMENT.PRODUCTION
                }
                else -> FortSdk.ENVIRONMENT.TEST
            }
        }
    }

    fun initService(channel: MethodChannel, options: PayFortOptions) {
        if (fortCallback == null) {
            fortCallback = FortCallBackManager.Factory.create()
        }
        this.options = options
        this.channel = channel
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (fortCallback != null) {
            fortCallback?.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun callPayFort(
        activity: Activity,
        fortRequest: FortRequest,
        call: MethodCall,
        context: Context
    ) {
        val headerText = call.argument<String>("headerText")
        val headerIconUrl = call.argument<String>("headerIconUrl")
        val secondaryHeaderText = call.argument<String>("secondaryHeaderText")
        val iconUrls = call.argument<List<String>>("iconUrls")
        val headerTextColor = call.argument<Long>("headerTextColor")
        val secondaryHeaderTextColor = call.argument<Long>("secondaryHeaderTextColor")
        val toggleButtonActiveColor = call.argument<Long>("toggleButtonActiveColor")
        val toggleButtonInactiveColor = call.argument<Long>("toggleButtonInactiveColor")
        val cardNumberLabel = call.argument<String>("cardNumberLabel")
        val cardHolderLabel = call.argument<String>("cardHolderLabel")
        val expiryDateLabel = call.argument<String>("expiryDateLabel")
        val cvvLabel = call.argument<String>("cvvLabel")
        val textFieldHintColor = call.argument<Long>("textFieldHintColor")
        val saveCardDataLabel = call.argument<String>("saveCardDataLabel")
        val setCardDefaultLabel = call.argument<String>("setCardDefaultLabel")
        val addCardButtonLabel = call.argument<String>("addCardButtonLabel")
        val addCardButtonBackgroundColor = call.argument<Long>("addCardButtonBackgroundColor")

        val intent = Intent(context, CustomPaymentViewActivity::class.java).apply {
            putExtra("headerText", headerText)
            putExtra("headerIconUrl", headerIconUrl)
            putExtra("secondaryHeaderText", secondaryHeaderText)
            putStringArrayListExtra("iconUrls", ArrayList(iconUrls))
            putExtra("headerTextColor", headerTextColor?.toInt())
            putExtra("toggleButtonActiveColor", toggleButtonActiveColor?.toInt())
            putExtra("toggleButtonInactiveColor", toggleButtonInactiveColor?.toInt())
            putExtra("cardNumberLabel", cardNumberLabel)
            putExtra("cardHolderLabel", cardHolderLabel)
            putExtra("expiryDateLabel", expiryDateLabel)
            putExtra("cvvLabel", cvvLabel)
            putExtra("textFieldHintColor", textFieldHintColor?.toInt())
            putExtra("saveCardDataLabel", saveCardDataLabel)
            putExtra("setCardDefaultLabel", setCardDefaultLabel)
            putExtra("addCardButtonLabel", addCardButtonLabel)
            putExtra("addCardButtonBackgroundColor", addCardButtonBackgroundColor?.toInt())
            putExtra("environment", options?.environment)
            putExtra("fortRequest", fortRequest)
        }
        CustomPaymentViewActivity.channel = channel
        activity.startActivity(intent)
    }

    fun createSignature(shaType: String, concatenatedString: String): String? {
        try {
            val bytes = concatenatedString.toByteArray()
            val md = MessageDigest.getInstance(shaType)
            val digest = md.digest(bytes)
            return digest.fold("") { str, it -> str + "%02x".format(it) }
        } catch (e: NoSuchAlgorithmException) {
            Log.d("Signature Error", e.toString())
        }
        return null
    }
}
