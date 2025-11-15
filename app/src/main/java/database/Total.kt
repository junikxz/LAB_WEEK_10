package database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "total")
// Every variables declared inside this class counts
// as declaring a new column inside the table
data class Total(
// @PrimaryKey sets a column into a primary key
    @PrimaryKey(autoGenerate = true)
// @ColumnInfo sets the name of the column
    @ColumnInfo(name = "id")
// The variable name can be different from the column name
    val id: Int = 0,
// Here we set another column to store the total value
    @ColumnInfo(name = "total")
    val total: Int = 0,
)