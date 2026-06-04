package dev.gustavo.countries.data.local.database;

import androidx.annotation.NonNull;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenDelegate;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import dev.gustavo.countries.data.local.dao.CountryDao;
import dev.gustavo.countries.data.local.dao.CountryDao_Impl;
import dev.gustavo.countries.data.local.dao.CountryDetailDao;
import dev.gustavo.countries.data.local.dao.CountryDetailDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class CountriesDatabase_Impl extends CountriesDatabase {
  private volatile CountryDao _countryDao;

  private volatile CountryDetailDao _countryDetailDao;

  @Override
  @NonNull
  protected RoomOpenDelegate createOpenDelegate() {
    final RoomOpenDelegate _openDelegate = new RoomOpenDelegate(1, "abe839ee1ff8080194131474a461f540", "fd20db9152edd71aee8ab49131130865") {
      @Override
      public void createAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `countries` (`cca3` TEXT NOT NULL, `commonName` TEXT NOT NULL, `capital` TEXT NOT NULL, `flagUrl` TEXT NOT NULL, `region` TEXT NOT NULL, PRIMARY KEY(`cca3`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `country_details` (`cca3` TEXT NOT NULL, `commonName` TEXT NOT NULL, `officialName` TEXT NOT NULL, `capital` TEXT NOT NULL, `flagUrl` TEXT NOT NULL, `region` TEXT NOT NULL, `subregion` TEXT NOT NULL, `languages` TEXT NOT NULL, `population` INTEGER NOT NULL, `borders` TEXT NOT NULL, `currencies` TEXT NOT NULL, PRIMARY KEY(`cca3`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        SQLite.execSQL(connection, "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'abe839ee1ff8080194131474a461f540')");
      }

      @Override
      public void dropAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `countries`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `country_details`");
      }

      @Override
      public void onCreate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      public void onOpen(@NonNull final SQLiteConnection connection) {
        internalInitInvalidationTracker(connection);
      }

      @Override
      public void onPreMigrate(@NonNull final SQLiteConnection connection) {
        DBUtil.dropFtsSyncTriggers(connection);
      }

      @Override
      public void onPostMigrate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      @NonNull
      public RoomOpenDelegate.ValidationResult onValidateSchema(
          @NonNull final SQLiteConnection connection) {
        final Map<String, TableInfo.Column> _columnsCountries = new HashMap<String, TableInfo.Column>(5);
        _columnsCountries.put("cca3", new TableInfo.Column("cca3", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountries.put("commonName", new TableInfo.Column("commonName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountries.put("capital", new TableInfo.Column("capital", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountries.put("flagUrl", new TableInfo.Column("flagUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountries.put("region", new TableInfo.Column("region", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysCountries = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesCountries = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCountries = new TableInfo("countries", _columnsCountries, _foreignKeysCountries, _indicesCountries);
        final TableInfo _existingCountries = TableInfo.read(connection, "countries");
        if (!_infoCountries.equals(_existingCountries)) {
          return new RoomOpenDelegate.ValidationResult(false, "countries(dev.gustavo.countries.data.local.entity.CountryEntity).\n"
                  + " Expected:\n" + _infoCountries + "\n"
                  + " Found:\n" + _existingCountries);
        }
        final Map<String, TableInfo.Column> _columnsCountryDetails = new HashMap<String, TableInfo.Column>(11);
        _columnsCountryDetails.put("cca3", new TableInfo.Column("cca3", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountryDetails.put("commonName", new TableInfo.Column("commonName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountryDetails.put("officialName", new TableInfo.Column("officialName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountryDetails.put("capital", new TableInfo.Column("capital", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountryDetails.put("flagUrl", new TableInfo.Column("flagUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountryDetails.put("region", new TableInfo.Column("region", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountryDetails.put("subregion", new TableInfo.Column("subregion", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountryDetails.put("languages", new TableInfo.Column("languages", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountryDetails.put("population", new TableInfo.Column("population", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountryDetails.put("borders", new TableInfo.Column("borders", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountryDetails.put("currencies", new TableInfo.Column("currencies", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysCountryDetails = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesCountryDetails = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCountryDetails = new TableInfo("country_details", _columnsCountryDetails, _foreignKeysCountryDetails, _indicesCountryDetails);
        final TableInfo _existingCountryDetails = TableInfo.read(connection, "country_details");
        if (!_infoCountryDetails.equals(_existingCountryDetails)) {
          return new RoomOpenDelegate.ValidationResult(false, "country_details(dev.gustavo.countries.data.local.entity.CountryDetailEntity).\n"
                  + " Expected:\n" + _infoCountryDetails + "\n"
                  + " Found:\n" + _existingCountryDetails);
        }
        return new RoomOpenDelegate.ValidationResult(true, null);
      }
    };
    return _openDelegate;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final Map<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final Map<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "countries", "country_details");
  }

  @Override
  public void clearAllTables() {
    super.performClear(false, "countries", "country_details");
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final Map<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(CountryDao.class, CountryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CountryDetailDao.class, CountryDetailDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final Set<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public CountryDao countryDao() {
    if (_countryDao != null) {
      return _countryDao;
    } else {
      synchronized(this) {
        if(_countryDao == null) {
          _countryDao = new CountryDao_Impl(this);
        }
        return _countryDao;
      }
    }
  }

  @Override
  public CountryDetailDao countryDetailDao() {
    if (_countryDetailDao != null) {
      return _countryDetailDao;
    } else {
      synchronized(this) {
        if(_countryDetailDao == null) {
          _countryDetailDao = new CountryDetailDao_Impl(this);
        }
        return _countryDetailDao;
      }
    }
  }
}
