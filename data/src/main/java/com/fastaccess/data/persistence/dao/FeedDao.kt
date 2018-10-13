package com.fastaccess.data.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.fastaccess.data.persistence.models.FeedModel

/**
 * Created by Kosh on 06.07.18.
 */
@Dao
abstract class FeedDao : BaseDao<FeedModel>() {
    @Query("SELECT * FROM ${FeedModel.TABLE_NAME} ORDER BY `id` DESC")
    abstract fun getFeeds(): LiveData<List<FeedModel>>

    @Query("SELECT * FROM ${FeedModel.TABLE_NAME} ORDER BY `id` DESC LIMIT 5")
    abstract fun getMainFeeds(): LiveData<List<FeedModel>>

    @Query("DELETE FROM ${FeedModel.TABLE_NAME}") abstract fun deleteAll()
}