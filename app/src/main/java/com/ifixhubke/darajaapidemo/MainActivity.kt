package com.ifixhubke.darajaapidemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.androidstudy.daraja.Daraja
import com.androidstudy.daraja.DarajaListener
import com.androidstudy.daraja.model.AccessToken
import com.androidstudy.daraja.model.LNMExpress
import com.androidstudy.daraja.model.LNMResult
import com.androidstudy.daraja.util.Env
import com.androidstudy.daraja.util.TransactionType

class MainActivity : AppCompatActivity() {

    private lateinit var sendButton: Button
    private lateinit var phoneNumEdtxt: EditText
    private lateinit var amountEdtxt: EditText

    private lateinit var daraja: Daraja

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sendButton = findViewById(R.id.payButton)
        phoneNumEdtxt = findViewById(R.id.phoneNumberEditText)
        amountEdtxt = findViewById(R.id.amountEditText)

        daraja = Daraja.with("Y8f7BfKhFLO8cgYut6ptVnwvR0EI08aw",
            "sRqWFn6PnEHgBvhz", Env.SANDBOX, object : DarajaListener<AccessToken> {
                override fun onResult(result: AccessToken) {
                    Toast.makeText(this@MainActivity, result.access_token, Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onError(error: String?) {
                    Toast.makeText(this@MainActivity, error.toString(), Toast.LENGTH_SHORT).show()
                }

            })

        sendButton.setOnClickListener {
            var phoneNumber = phoneNumEdtxt.text.toString().trim()
            var amount = amountEdtxt.text.toString().trim()

            val lnmExpress = LNMExpress(
                "174379", //Test credential but shortcode is mostly paybill number, email mpesa businnes fo clarification
                "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",  //https://developer.safaricom.co.ke/test_credentials
                TransactionType.CustomerPayBillOnline,  // TransactionType.CustomerPayBillOnline  <- Apply any of these two
                amount,
                phoneNumber,
                "174379",
                phoneNumber,
                "https://us-central1-mpesaapisecond.cloudfunctions.net/myCallbackUrl", // call back url send back payload info if the transactions went through. Its important inorder to update ui after user has paid, its essential but the service can work without it.
                "001ABC",
                "Goods Payment"
            )


            daraja.requestMPESAExpress(lnmExpress, object : DarajaListener<LNMResult> {
                override fun onResult(result: LNMResult) {
                    Toast.makeText(this@MainActivity, result.CustomerMessage, Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onError(error: String?) {
                    Toast.makeText(this@MainActivity, error.toString(), Toast.LENGTH_SHORT).show()
                }
            })

        }

    }
}