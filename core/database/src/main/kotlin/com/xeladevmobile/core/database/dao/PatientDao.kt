package com.xeladevmobile.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.xeladevmobile.core.database.model.PatientEntity
import com.xeladevmobile.core.database.model.PatientWithAudios
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Query(value = "SELECT * FROM patients")
    fun getTopicEntities(): Flow<List<PatientEntity>>

    // Search for patients by name
    @Query(value = "SELECT * FROM patients WHERE name LIKE :name")
    fun searchByName(name: String): Flow<List<PatientEntity>>

    // Search for patients by surname
    @Query(value = "SELECT * FROM patients WHERE surname LIKE :surname")
    fun searchBySurname(surname: String): Flow<List<PatientEntity>>

    // Order patients by treatment date
    @Query(value = "SELECT * FROM patients ORDER BY treatment_date ASC")
    fun orderByTreatmentDateAsc(): Flow<List<PatientEntity>>

    @Transaction
    @Query("SELECT * FROM patients WHERE id = :patientId")
    fun getPatientWithAudios(patientId: String): Flow<PatientWithAudios>

    // If you want to fetch all patients with their audios, you could use:
    @Transaction
    @Query("SELECT * FROM patients")
    fun getAllPatientsWithAudios(): Flow<List<PatientWithAudios>>

    // Insert a new patient
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPatient(patient: PatientEntity)

    @Update
    suspend fun updatePatient(patient: PatientEntity)

    // Delete a patient
    @Query(value = "DELETE FROM patients WHERE id = :id")
    suspend fun deletePatient(id: String)


}