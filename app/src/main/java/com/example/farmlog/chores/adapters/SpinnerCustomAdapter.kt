package com.example.farmlog.chores.adapters

import android.content.Context
import android.widget.ArrayAdapter
import com.example.farmlog.chores.models.LandNameSpinnerModel

class SpinnerCustomAdapter(ctx: Context, list: List<LandNameSpinnerModel>) : ArrayAdapter<LandNameSpinnerModel>(ctx,0, list) {

}