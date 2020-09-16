package com.dongcompany.knueverywhere.Login

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.NonNull
import com.dongcompany.knueverywhere.R
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RegistActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private var bitmap:Bitmap? = null
    private var uri: Uri? = null

    private var imageUploaded = false
    private var IDcheck = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist)
        val storage = FirebaseStorage.getInstance()
        val db = FirebaseFirestore.getInstance()

        var NameeditText = findViewById<EditText>(R.id.RegistActivity_NameEditText)
        var stdnumeditText = findViewById<EditText>(R.id.RegistActivity_StdNumEditText)
        var IDeditText:EditText = findViewById(R.id.RegistActivity_IDEditText)
        var IDchkButton = findViewById<Button>(R.id.RegistActivity_IDCheckButton)
        IDchkButton.setOnClickListener(View.OnClickListener {
            if(!IDcheck) {
                if(3 <= IDeditText.text.length && IDeditText.text.length <= 10) {
                    db.collection("users")
                            .get()
                            .addOnSuccessListener { result ->
                                var tmp = false
                                for (document in result) {
                                    val id = document.id
                                    if(id.toString().equals(IDeditText.text.toString())) {
                                        Toast.makeText(this, "같은 이름의 아이디가 이미 있습니다.", Toast.LENGTH_SHORT).show()
                                        tmp = true
                                        break
                                    }
                                }
                                if(!tmp) {
                                    Toast.makeText(this, "중복 검사가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                    IDeditText.isFocusableInTouchMode = false
                                    IDcheck = true
                                    IDchkButton.setText("변경하기")
                                }
                            }
                            .addOnFailureListener{ result ->
                                Log.e("MainActivity. 데이터로드 에러", "data load error")
                            }
                }
                else {
                    Toast.makeText(this, "ID 조건이 맞지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                IDeditText.isFocusableInTouchMode = true
                IDchkButton.setText("중복확인")
                IDcheck = false
            }
        })
        var pweditText = findViewById<EditText>(R.id.RegistActivity_PWEditText)
        var pwchkeditText = findViewById<EditText>(R.id.RegistActivity_PWCheckEditText)
        var phoneeditText = findViewById<EditText>(R.id.RegistActivity_PhoneEditText)

        //이미지 클릭 시 로컬 저장소로 가서 사진 가져오기
        imageView = findViewById(R.id.RegistActivity_ImageView)
        imageView.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1000);
        })


        //완료 버튼
        findViewById<Button>(R.id.RegistActivity_CompleteButton).setOnClickListener(View.OnClickListener {
            if(NameeditText.text.length == 0) {
                Toast.makeText(this, "이름을 입력 해 주세요.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if(stdnumeditText.text.length == 0) {
                Toast.makeText(this, "학번을 입력 해 주세요.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if(!IDcheck) {
                Toast.makeText(this, "ID를 확인 해 주세요.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if(!(8<=pweditText.text.length && pweditText.text.length<=12) ||
                    !(pweditText.text.toString().equals(pwchkeditText.text.toString())) ) {
                Toast.makeText(this, "비밀번호 조건, 확인 해 주세요.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if(phoneeditText.text.length == 0) {
                Toast.makeText(this, "전화번호를 입력 해 주세요.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if(!imageUploaded) {
                Toast.makeText(this, "학생증을 업로드 해 주세요.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            else {
                //db등록
                val a = hashMapOf(
                        "ID" to IDeditText.text.toString(),
                        "PW" to pweditText.text.toString(),
                        "권한" to false,
                        "연락처" to phoneeditText.text.toString(),
                        "이름" to NameeditText.text.toString(),
                        "학번" to stdnumeditText.text.toString(),
                        "체크박스_코스0" to false,
                        "체크박스_코스1" to false,
                        "체크박스_코스2" to false,
                        "체크박스_코스3" to false

                )
                db.collection("users").document(IDeditText.text.toString()).set(a)
                Toast.makeText(this, "등록되었습니다.", Toast.LENGTH_LONG).show()

                //학생증 업로드
                if(bitmap != null) {
                    val storageRef = storage.getReferenceFromUrl("gs://knu-everywhere.appspot.com/users/" + IDeditText.text.toString() + ".jpg")

                    val uploadTask : UploadTask =storageRef.putFile(uri!!);
                    uploadTask.addOnFailureListener {
                    }.addOnSuccessListener {
                        //Toast.makeText(this, "업로드 성공", Toast.LENGTH_SHORT).show()
                    }

                    finish();
                }

                //db 탐방정보 등록(all 비탐방)
                //경북대학교의 문
                val gateway = hashMapOf(
                        "CLEAR" to false,
                        "북문" to false, "농장문" to false, "테크노문" to false, "동문" to false, "정문" to false, "수의대문" to false
                        , "쪽문" to false, "조은문" to false, "솔로문" to false, "서문" to false, "수영장문" to false
                )
                db.collection("users").document(IDeditText.text.toString()).collection("경북대학교의 문")
                        .document("경북대학교의 문").set(gateway)
                //경북대학교의 식당
                val restaurent = hashMapOf(
                        "CLEAR" to false,
                        "경대리아" to false, "복현회관" to false, "복지관" to false, "종합정보센터" to false, "공대식당" to false
                )
                db.collection("users").document(IDeditText.text.toString()).collection("경북대학교의 식당")
                        .document("경북대학교의 식당").set(restaurent)
                //경북대학교의 주요 장소
                val landmark = hashMapOf(
                        "CLEAR" to false,
                        "대운동장" to false, "백호관" to false, "일청담" to false, "도서관" to false, "본관" to false, "백양로" to false
                        , "박물관" to false, "미술관" to false, "대강당" to false, "글로벌플라자" to false, "센트럴파크" to false
                )
                db.collection("users").document(IDeditText.text.toString()).collection("경북대학교의 주요 장소")
                        .document("경북대학교의 주요 장소").set(landmark)
                //경북대학교의 단과 대학
                val collage = hashMapOf(
                        "CLEAR" to false,
                        "공과대학 1호관" to false, "농업생명과학대학 1호관" to false, "IT대학 1호관" to false, "자연과학대학" to false, "수의과대학" to false
                        , "사회과학대학" to false, "경상대학" to false, "인문대학" to false, "사범대학" to false, "생활과학대학" to false
                        , "예술대학" to false, "약학대학" to false
                )
                db.collection("users").document(IDeditText.text.toString()).collection("경북대학교의 단과 대학")
                        .document("경북대학교의 단과 대학").set(collage)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1000) {
            if(resultCode == RESULT_OK) {
                try {
                    val inputStream : InputStream? = contentResolver.openInputStream(data?.data!!)

                    bitmap= BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()
                    imageView.setBackgroundResource(0)
                    imageView.setImageBitmap(bitmap)

                    uri = data?.data!!
                    imageUploaded = true
                }
                catch (e:Exception){
                    Toast.makeText(this,"사진 선택 에러",Toast.LENGTH_LONG).show()
                }
            }
            else if(resultCode == RESULT_CANCELED)
            {
                //Toast.makeText(this,"사진 선택 취소",Toast.LENGTH_LONG).show()
            }
        }
    }
}