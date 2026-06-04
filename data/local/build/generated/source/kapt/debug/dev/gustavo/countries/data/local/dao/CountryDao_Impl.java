package dev.gustavo.countries.data.local.dao;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import dev.gustavo.countries.data.local.entity.CountryEntity;
import java.lang.Class;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class CountryDao_Impl implements CountryDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<CountryEntity> __insertAdapterOfCountryEntity;

  public CountryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfCountryEntity = new EntityInsertAdapter<CountryEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `countries` (`cca3`,`commonName`,`capital`,`flagUrl`,`region`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final CountryEntity entity) {
        if (entity.getCca3() == null) {
          statement.bindNull(1);
        } else {
          statement.bindText(1, entity.getCca3());
        }
        if (entity.getCommonName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getCommonName());
        }
        if (entity.getCapital() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getCapital());
        }
        if (entity.getFlagUrl() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getFlagUrl());
        }
        if (entity.getRegion() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getRegion());
        }
      }
    };
  }

  @Override
  public Object insertAll(final List<CountryEntity> countries,
      final Continuation<? super Unit> $completion) {
    if (countries == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __insertAdapterOfCountryEntity.insert(_connection, countries);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object getAllCountries(final Continuation<? super List<CountryEntity>> $completion) {
    final String _sql = "SELECT * FROM countries ORDER BY commonName ASC";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfCca3 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cca3");
        final int _columnIndexOfCommonName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "commonName");
        final int _columnIndexOfCapital = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "capital");
        final int _columnIndexOfFlagUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "flagUrl");
        final int _columnIndexOfRegion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "region");
        final List<CountryEntity> _result = new ArrayList<CountryEntity>();
        while (_stmt.step()) {
          final CountryEntity _item;
          final String _tmpCca3;
          if (_stmt.isNull(_columnIndexOfCca3)) {
            _tmpCca3 = null;
          } else {
            _tmpCca3 = _stmt.getText(_columnIndexOfCca3);
          }
          final String _tmpCommonName;
          if (_stmt.isNull(_columnIndexOfCommonName)) {
            _tmpCommonName = null;
          } else {
            _tmpCommonName = _stmt.getText(_columnIndexOfCommonName);
          }
          final String _tmpCapital;
          if (_stmt.isNull(_columnIndexOfCapital)) {
            _tmpCapital = null;
          } else {
            _tmpCapital = _stmt.getText(_columnIndexOfCapital);
          }
          final String _tmpFlagUrl;
          if (_stmt.isNull(_columnIndexOfFlagUrl)) {
            _tmpFlagUrl = null;
          } else {
            _tmpFlagUrl = _stmt.getText(_columnIndexOfFlagUrl);
          }
          final String _tmpRegion;
          if (_stmt.isNull(_columnIndexOfRegion)) {
            _tmpRegion = null;
          } else {
            _tmpRegion = _stmt.getText(_columnIndexOfRegion);
          }
          _item = new CountryEntity(_tmpCca3,_tmpCommonName,_tmpCapital,_tmpFlagUrl,_tmpRegion);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    final String _sql = "DELETE FROM countries";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
