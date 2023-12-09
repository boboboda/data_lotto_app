package com.bobo.data_lotto_app.screens.auth

class GoogleSignInClient {
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == GOOGLE_LOGIN_CODE) {
//            var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
//            // result가 성공했을 때 이 값을 firebase에 넘겨주기
//            if(result!!.isSuccess) {
//                var account = result.signInAccount
//                // Second step
//                firebaseAuthWithGoogle(account)
//            }
//        }
//    }
//
//    fun firebaseAuthWithGoogle(account : GoogleSignInAccount?) {
//        var credential = GoogleAuthProvider.getCredential(account?.idToken,null)
//        auth?.signInWithCredential(credential)
//            ?.addOnCompleteListener {
//                    task ->
//                if(task.isSuccessful) {
//                    // Login, 아이디와 패스워드가 맞았을 때
////                    Toast.makeText(this,  "success", Toast.LENGTH_LONG).show()
//
//                } else {
//                    // Show the error message, 아이디와 패스워드가 틀렸을 때
//                }
//            }
//    }
}