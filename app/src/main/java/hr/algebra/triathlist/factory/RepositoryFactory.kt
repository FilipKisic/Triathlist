package hr.algebra.triathlist.factory

import android.content.Context
import hr.algebra.triathlist.dal.SqlHelper

fun getRepository(context: Context?) = SqlHelper(context)