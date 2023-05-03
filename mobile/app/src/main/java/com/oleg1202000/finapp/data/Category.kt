package com.oleg1202000.finapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories" ,
   /* indices = [
        Index("name", unique = true)
    ]*/

)
data class Category(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "is_income") val isIncome: Boolean, // Доход или расход
    @ColumnInfo(name = "color") val color: Long,
    @ColumnInfo(name = "path_to_icon") val pathToIcon: Int
)

data class CategoryWithoutIsIncome(
    @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "color") val color: Long,
    @ColumnInfo(name = "path_to_icon") val pathToIcon: Int
)
