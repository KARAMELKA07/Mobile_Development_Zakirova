package ru.mirea.zakirovakr.employeedb;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Superhero {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public String superpower;
    public int powerLevel;
}
