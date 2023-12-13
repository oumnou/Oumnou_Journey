package com.example.tabbedappportfolio.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.tabbedappportfolio.R
import com.example.tabbedappportfolio.adapter.ChatBootAdapter
import com.example.tabbedappportfolio.model.Message
import org.json.JSONObject

class FragmentThree : Fragment() {

    private lateinit var responseTV: TextView
    private lateinit var sendBtn: Button
    private lateinit var adapter: ChatBootAdapter
    private lateinit var queryEdt: EditText
    private lateinit var recyclerView: RecyclerView
    lateinit var list: ArrayList<Message>
    var url = "https://api.openai.com/v1/completions"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.chatbot, container, false)
        recyclerView = rootView.findViewById(R.id.rv_messages)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        list = arrayListOf()
        adapter = ChatBootAdapter(list)
        recyclerView.adapter = adapter

        queryEdt = rootView.findViewById(R.id.et_message)
        sendBtn = rootView.findViewById(R.id.btn_send)

        sendBtn.setOnClickListener {
            val message = queryEdt.text.toString()
            if (message.isNotBlank()) {

                getResponse(message)
            } else {
                Toast.makeText(requireContext(), "Please enter your question..", Toast.LENGTH_SHORT).show()
            }
        }
        return rootView
    }

    private fun getResponse(query: String) {
        list.add(Message(query, "Please wait..."))
        adapter.notifyDataSetChanged()

        // Your existing code for making API requests and handling responses
        val queue: RequestQueue = Volley.newRequestQueue(requireContext())
        val jsonObject: JSONObject? = JSONObject()

        jsonObject?.put("model", "text-davinci-003")
        jsonObject?.put("prompt", query)
        jsonObject?.put("temperature", 0)
        jsonObject?.put("max_tokens", 100)
        jsonObject?.put("top_p", 1)
        jsonObject?.put("frequency_penalty", 0.0)
        jsonObject?.put("presence_penalty", 0.0)

        val postRequest: JsonObjectRequest =
            object : JsonObjectRequest(Method.POST, url, jsonObject,
                Response.Listener { response ->



                    val responseMsg: String =
                        response.getJSONArray("choices").getJSONObject(0).getString("text")

                    // Updating the last item in the list with the actual response
                    list[list.size - 1] = Message(query, responseMsg)
                    adapter.notifyDataSetChanged()
                },








                Response.ErrorListener { error ->
                    Log.e("TAGAPI", "Error is: ${error.message}\nResponse: ${String(error.networkResponse.data)}")

                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["Content-Type"] = "application/json"
                    params["Authorization"] = "Bearer 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"
                    return params
                }
            }

        postRequest.retryPolicy = object : RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 50000
            }

            override fun getCurrentRetryCount(): Int {
                return 50000
            }

            @Throws(VolleyError::class)
            override fun retry(error: VolleyError) {
            }
        }

        queue.add(postRequest)
    }

    companion object {
        fun newInstance(): FragmentThree = FragmentThree()
    }
}
