package com.guillermo.nasadailyimageapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    // Initializing okHttp and picasso
    val okHttpClient = OkHttpClient()
    val picasso = Picasso.get()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Construct the url
        var url = HttpUrl.Builder()
            .scheme("https")
            .host("api.nasa.gov")
            .addPathSegments("planetary")
            .addPathSegments("apod")
            .addQueryParameter("api_key", "wyYLsgOP61VY3tpRvssX0Xqfmz3e4Ihgec3MFuMX")
            .build()

        var request = Request.Builder()
            .url(url)
            .build()

        getImageFromNasa(request)

    }
    // Get an image from the NASA Api
    fun getImageFromNasa(request: Request) {
        okHttpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failure")
                e.printStackTrace()

            }

            override fun onResponse(call: Call, response: Response) {
                println("Success")
                val result = response.body?.string()
                // Run the UI updates on the main uithread
                runOnUiThread {
                    try {
                        var json = JSONObject(result)

                        picasso.load(json.getString("hdurl")).into(nasaImageView)
                        titlePicture.text = json.getString("title")
                        datePicture.text = json.getString("date")
                    } catch (e: JSONException) {

                    }

                }
            }

        })
    }

}
