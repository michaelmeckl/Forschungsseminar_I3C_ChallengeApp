import android.os.Bundle
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity

import com.example.challengecovid.R

import kotlinx.android.synthetic.main.profile_view.*

class ProfileView : AppCompatActivity()  {

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_view)

         profile_picture.setOnClickListener {
             val popup = PopupWindow(this)
             val view = layoutInflater.inflate(R.layout.popup_edit_profile,null)
             popup.contentView = view
         }

    }


    }