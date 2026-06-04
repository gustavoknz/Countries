package dev.gustavo.countries.data.local.database;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&\u00a8\u0006\u0007"}, d2 = {"Ldev/gustavo/countries/data/local/database/CountriesDatabase;", "Landroidx/room/RoomDatabase;", "()V", "countryDao", "Ldev/gustavo/countries/data/local/dao/CountryDao;", "countryDetailDao", "Ldev/gustavo/countries/data/local/dao/CountryDetailDao;", "local_debug"})
@androidx.room.Database(entities = {dev.gustavo.countries.data.local.entity.CountryEntity.class, dev.gustavo.countries.data.local.entity.CountryDetailEntity.class}, version = 1, exportSchema = false)
@androidx.room.TypeConverters(value = {dev.gustavo.countries.data.local.database.StringListConverter.class})
public abstract class CountriesDatabase extends androidx.room.RoomDatabase {
    
    public CountriesDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract dev.gustavo.countries.data.local.dao.CountryDao countryDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract dev.gustavo.countries.data.local.dao.CountryDetailDao countryDetailDao();
}