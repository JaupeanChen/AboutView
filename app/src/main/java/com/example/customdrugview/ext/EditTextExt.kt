package com.example.customdrugview.ext

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Author: ChenGuiPing
 * Date: 2022/10/17
 * Description:
 */

@OptIn(ExperimentalCoroutinesApi::class)
fun EditText.textChangeFlow(): Flow<String> = callbackFlow {
    val watcher = object : TextWatcher {
        private var isUserInput = true

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            isUserInput = this@textChangeFlow.hasFocus()
        }

        override fun afterTextChanged(s: Editable?) {
            if (isUserInput) {
                //当用户输入时，发射数据
                //sendFlow(s?.toString().orEmpty())
            }
        }
    }
    addTextChangedListener(watcher)
    awaitClose { removeTextChangedListener(watcher) }
}