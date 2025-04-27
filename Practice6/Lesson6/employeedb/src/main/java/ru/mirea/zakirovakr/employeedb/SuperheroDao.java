package ru.mirea.zakirovakr.employeedb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface SuperheroDao {
    @Query("SELECT * FROM superhero")
    List<Superhero> getAll();

    @Query("SELECT * FROM superhero WHERE id = :id")
    Superhero getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Superhero superhero);

    @Update
    void update(Superhero superhero);

    @Delete
    void delete(Superhero superhero);
}
