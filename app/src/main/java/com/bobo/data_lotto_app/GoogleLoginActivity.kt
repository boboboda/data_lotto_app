package com.bobo.data_lotto_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth

class GoogleLoginActivity : AppCompatActivity() {

    lateinit var googleSignInClient: GoogleSignInClient
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val RC_SIGN_IN = 1313

    companion object {
        val GOOGLE = "구글"
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            Log.d(GOOGLE, "구글 연결 성공")
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.d(GOOGLE, "구글 파이어 베이스 회원가입 실패 ${e.message.toString()}")
                startActivity(Intent(this@GoogleLoginActivity, MainActivity::class.java))
            }
        } else {
            Log.d(GOOGLE, "연결 실패")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = com.google.firebase.ktx.Firebase.auth

        googleLogin()
    }




    // 로그인 객체 생성
    fun googleLogin() {

        Log.d(GOOGLE, "Build Type: " + BuildConfig.BUILD_TYPE);

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleSignIn()
    }

    // 구글 회원가입
    private fun googleSignIn() {
        var signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }



    // account 객체에서 id 토큰 가져온 후 Firebase 인증
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {

        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d(GOOGLE, "토큰 ${account?.idToken}")
                toMainActivity(
                    account?.email!!,
                    auth.currentUser)
            }
        }
    }

    private fun toMainActivity(
        email: String,
        user: FirebaseUser?) {

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("email", "${email}")
        intent.putExtra("success", true)

        startActivity(intent)


        if (user != null) {
            startActivity(intent)
        }
    }
}