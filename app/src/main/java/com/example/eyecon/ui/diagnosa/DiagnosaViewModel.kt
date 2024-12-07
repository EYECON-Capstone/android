package com.example.eyecon.ui.diagnosa

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eyecon.data.dataclass.Diagnosa
import com.example.eyecon.data.dataclass.Rekomendasi
import com.example.eyecon.R

class DiagnosaViewModel : ViewModel() {
    private val _diagnosaList = MutableLiveData<List<Diagnosa>>()
    val diagnosaList: LiveData<List<Diagnosa>> = _diagnosaList

    fun loadDiagnosa(context: Context) {
        val list = listOf(
            Diagnosa(
                1,
                context.getString(R.string.red_eye_title),
                R.drawable.img_red_eye,
                context.getString(R.string.red_eye_subtitle),
                context.getString(R.string.red_eye_desc),
                listOf(
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.red_eye_rec1)),
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.red_eye_rec2))
                )
            ),
            Diagnosa(
                2,
                context.getString(R.string.conjunctivitis_title),
                R.drawable.img_conjunctivitis,
                context.getString(R.string.conjunctivitis_subtitle),
                context.getString(R.string.conjunctivitis_desc),
                listOf(
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.conjunctivitis_rec1)),
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.conjunctivitis_rec2))
                )
            ),
            Diagnosa(
                3,
                context.getString(R.string.myopia_title),
                R.drawable.img_myopia,
                context.getString(R.string.red_eye_subtitle),
                context.getString(R.string.myopia_desc),
                listOf(
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.myopia_rec1)),
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.myopia_rec2))
                )
            ),
            Diagnosa(
                4,
                context.getString(R.string.hyperopia_title),
                R.drawable.img_hyperopia,
                context.getString(R.string.red_eye_subtitle),
                context.getString(R.string.hyperopia_desc),
                listOf(
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.hyperopia_rec1)),
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.hyperopia_rec2))
                )
            ),
            Diagnosa(
                5,
                context.getString(R.string.astigmatism_title),
                R.drawable.img_astigmatism,
                context.getString(R.string.red_eye_subtitle),
                context.getString(R.string.astigmatism_desc),
                listOf(
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.astigmatism_rec1)),
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.astigmatism_rec2))
                )
            ),
            Diagnosa(
                6,
                context.getString(R.string.cataract_title),
                R.drawable.img_cataract,
                context.getString(R.string.red_eye_subtitle),
                context.getString(R.string.cataract_desc),
                listOf(
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.cataract_rec1)),
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.cataract_rec2))
                )
            ),
            Diagnosa(
                7,
                context.getString(R.string.glaucoma_title),
                R.drawable.img_glaucoma,
                context.getString(R.string.red_eye_subtitle),
                context.getString(R.string.glaucoma_desc),
                listOf(
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.glaucoma_rec1)),
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.glaucoma_rec2))
                )
            ),
            Diagnosa(
                8,
                context.getString(R.string.dry_eye_title),
                R.drawable.img_dry_eyes,
                context.getString(R.string.red_eye_subtitle),
                context.getString(R.string.dry_eye_desc),
                listOf(
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.dry_eye_rec1)),
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.dry_eye_rec2))
                )
            ),
            Diagnosa(
                9,
                context.getString(R.string.strabismus_title),
                R.drawable.img_strabismus,
                context.getString(R.string.red_eye_subtitle),
                context.getString(R.string.strabismus_desc),
                listOf(
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.strabismus_rec1)),
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.strabismus_rec2))
                )
            ),
            Diagnosa(
                10,
                context.getString(R.string.retinopathy_title),
                R.drawable.img_retinopathy,
                context.getString(R.string.red_eye_subtitle),
                context.getString(R.string.retinopathy_desc),
                listOf(
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.retinopathy_rec1)),
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.retinopathy_rec2))
                )
            ),
            Diagnosa(
                11,
                context.getString(R.string.keratitis_title),
                R.drawable.img_keratitis,
                context.getString(R.string.red_eye_subtitle),
                context.getString(R.string.keratitis_desc),
                listOf(
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.keratitis_rec1)),
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.keratitis_rec2))
                )
            ),
            Diagnosa(
                12,
                context.getString(R.string.amblyopia_title),
                R.drawable.img_amblyopia,
                context.getString(R.string.red_eye_subtitle),
                context.getString(R.string.amblyopia_desc),
                listOf(
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.amblyopia_rec1)),
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.amblyopia_rec2))
                )
            ),
            Diagnosa(
                13,
                context.getString(R.string.uveitis_title),
                R.drawable.img_uveitis,
                context.getString(R.string.red_eye_subtitle),
                context.getString(R.string.uveitis_desc),
                listOf(
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.uveitis_rec1)),
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.uveitis_rec2))
                )
            ),
            Diagnosa(
                14,
                context.getString(R.string.presbyopia_title),
                R.drawable.img_presbyopia,
                context.getString(R.string.red_eye_subtitle),
                context.getString(R.string.presbyopia_desc),
                listOf(
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.presbyopia_rec1)),
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.presbyopia_rec2))
                )
            ),
            Diagnosa(
                15,
                context.getString(R.string.pterygium_title),
                R.drawable.img_pterygium,
                context.getString(R.string.red_eye_subtitle),
                context.getString(R.string.pterygium_desc),
                listOf(
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.pterygium_rec1)),
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.pterygium_rec2))
                )
            ),
            Diagnosa(
                16,
                context.getString(R.string.retinoblastoma_title),
                R.drawable.img_retinoblastoma,
                context.getString(R.string.retinoblastoma_subtitle),
                context.getString(R.string.retinoblastoma_desc),
                listOf(
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.retinoblastoma_rec1)),
                    Rekomendasi(R.drawable.ic_medicine, context.getString(R.string.retinoblastoma_rec2))
                )
            )
        )
        _diagnosaList.value = list
    }
}