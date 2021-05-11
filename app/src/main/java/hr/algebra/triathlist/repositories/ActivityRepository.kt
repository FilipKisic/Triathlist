package hr.algebra.triathlist.repositories

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hr.algebra.triathlist.model.Activity

class ActivityRepository {

    companion object {
        fun getData(): ArrayList<Activity> {
            val database = FirebaseDatabase.getInstance().getReference("Activities")
            val list = ArrayList<Activity>()
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        list.clear()
                        for (e in dataSnapshot.children) {
                            val activity = e.getValue(Activity::class.java)
                            println("================================================================================")
                            println(activity)
                            list.add(activity!!)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("###################### FAILED")
                }
            }
            database.addValueEventListener(postListener)
            return list
        }
    }
}